package io.vitech.flights.tracker.processor.impl;

import io.vitech.flights.tracker.helper.AirportPair;
import io.vitech.flights.tracker.helper.DistanceCalculator;
import io.vitech.flights.tracker.openai.OpenAIService;
import io.vitech.flights.tracker.processor.BaseProcessor;
import io.vitech.flights.tracker.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.vitech.flights.tracker.helper.DistanceCalculator.calculateDistance;


@Component
@ConditionalOnProperty(name = "vitech.tracker.processor.range.enabled", havingValue = "true")
public class RangeProcessor extends BaseProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RangeProcessor.class);

    private final ExecutorService executor = Executors.newFixedThreadPool(100); // Use 10 parallel threads

    private FlightRepository flightRepository;

    public RangeProcessor(final OpenAIService openAIService, final FlightRepository flightRepository) {
        super(openAIService);
        this.flightRepository = flightRepository;
    }

    @Override
    public void process() {
        LOGGER.debug(PROCESSING_START_LOG_MSG_TEMPLATE, this.getClass().getSimpleName());

        Set<AirportPair>  airportPairs = flightRepository.findUniqueAirportPairs();
        LOGGER.debug("Unique airport pairs found: {}", airportPairs.size());
//        airportPairs.forEach(airportPair -> {
//            LOGGER.debug("Calculating range for airport pair: {} - {}", airportPair.getDepartureAirportId(), airportPair.getArrivalAirportId());
//            double range = calculateDistance(airportPair.getDepartureLatitude(),
//                    airportPair.getDepartureLongitude(),
//                    airportPair.getArrivalLatitude(),
//                    airportPair.getArrivalLongitude());
//
//            LOGGER.debug("Range calculated: {}", range);
//            if(Double.isNaN(range)) {
//                LOGGER.warn("Range is NaN for airport pair: {} - {}", airportPair.getDepartureAirportId(), airportPair.getArrivalAirportId());
//                return;
//            }
//            flightRepository.updateFlightRange(airportPair.getDepartureAirportId(), airportPair.getArrivalAirportId(), range);
//            flightRepository.updateFlightRange(airportPair.getArrivalAirportId(), airportPair.getDepartureAirportId(),  range);
//        });

        updateFlightRangesParallel(new ArrayList<>(airportPairs));

        LOGGER.debug("I am working,  hop hop hop...");
        LOGGER.debug(PROCESSING_END_MSG_TEMPLATE, this.getClass().getSimpleName());
    }


    @Transactional
    public void updateFlightRangesParallel(List<AirportPair> airportPairs) {
        int batchSize = 10; // Adjust batch size based on DB performance
        List<List<AirportPair>> batches = partitionList(airportPairs, batchSize);

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (List<AirportPair> batch : batches) {
            futures.add(CompletableFuture.runAsync(() -> processBatch(batch), executor));
        }

        // Wait for all threads to finish
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void processBatch(List<AirportPair> batch) {
       try {
           batch.forEach(pair -> {
               if (pair.getDepartureLatitude() == null || pair.getDepartureLongitude() == null
                       || pair.getArrivalLatitude() == null || pair.getArrivalLongitude() == null) {
                   //LOGGER.warn("Skipping airport pair with missing coordinates: {} - {}", pair.getDepartureAirportId(), pair.getArrivalAirportId());
                   return;
               }

               double calculatedRange = DistanceCalculator.calculateDistance(pair.getDepartureLatitude(), pair.getDepartureLatitude(),
                       pair.getArrivalLatitude(), pair.getArrivalLongitude());
                //LOGGER.debug("Range calculated: {} object {}", calculatedRange, pair);

               flightRepository.updateFlightRange(pair.getDepartureAirportId(), pair.getArrivalAirportId(), calculatedRange);
               flightRepository.updateFlightRange(pair.getArrivalAirportId(), pair.getDepartureAirportId(), calculatedRange);
           });
       }catch (Exception e) {
            LOGGER.error("Error processing batch", e);
            throw new RuntimeException(e);
        }
    }

    private List<List<AirportPair>> partitionList(List<AirportPair> list, int size) {
        List<List<AirportPair>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }



}
