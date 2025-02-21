package io.vitech.flights.tracker.processor.impl

import io.vitech.flights.tracker.helper.AirportPair
import io.vitech.flights.tracker.openai.OpenAIService
import io.vitech.flights.tracker.repository.FlightRepository
import spock.lang.Specification

import java.util.concurrent.ExecutorService

class RangeProcessorTest extends Specification {

    def openAIService = Mock(OpenAIService)
    def flightRepository = Mock(FlightRepository)
    def rangeProcessor = new RangeProcessor(openAIService, flightRepository)

    def "ProcessBatch should update flight ranges"() {
        given:
            def batch = [new AirportPair(1, 2, 10.0, 20.0, 30.0, 40.0)]
            flightRepository.updateFlightRange(_, _, _) >> { args -> }

        when:
            rangeProcessor.processBatch(batch)

        then:
            1 * flightRepository.updateFlightRange(1, 2, _)
            1 * flightRepository.updateFlightRange(2, 1, _)
    }

    def "PartitionList should partition list into batches"() {
        given:
            def list = (1..20).collect { new AirportPair(it, it + 1, 10.0, 20.0, 30.0, 40.0) }
            def size = 10

        when:
            def partitions = rangeProcessor.partitionList(list, size)

        then:
            partitions.size() == 2
            partitions[0].size() == 10
            partitions[1].size() == 10
    }

}
