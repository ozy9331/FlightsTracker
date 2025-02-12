package io.vitech.flights.tracker.mapper;

import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.entity.CityEntity;
import io.vitech.flights.tracker.entity.TimezoneEntity;
import io.vitech.flights.tracker.openai.model.AirportPromptModel;
import io.vitech.flights.tracker.repository.CityRepository;
import io.vitech.flights.tracker.repository.TimezoneRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.logging.Logger;

@Mapper(componentModel = "spring", config = BaseMapper.class)
public interface AirportMapper extends BaseMapper<AirportEntity, AirportPromptModel> {
     Logger LOGGER = Logger.getLogger(AirportMapper.class.getName());

    @Override
    @Mapping(source = "iataCode", target = "iata")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "city.timezone.timezone", target = "timezone")
    AirportPromptModel toDto(AirportEntity airportEntity);

    @AfterMapping
    default void afterToDto(@MappingTarget AirportPromptModel airportPromptModel) {
        if ("N/A".equals(airportPromptModel.getIata())) {
            airportPromptModel.setIata(null);
        }
    }

    @Override
    AirportEntity toEntity(AirportPromptModel airportPromptModel);

    @AfterMapping
    default void afterToEntity(AirportPromptModel airportPromptModel, @MappingTarget AirportEntity airportEntity, @Autowired CityRepository cityRepository, @Autowired TimezoneRepository timezoneRepository) {
        LOGGER.info("Mapping airport entity from GptModel");
        if (airportPromptModel.getCityName() != null) {
            LOGGER.info("City name is not null");
            Optional<CityEntity> cityEntityOptional = cityRepository.findByNameAndTimezone(airportPromptModel.getCityName(), airportPromptModel.getTimezone());
            CityEntity cityEntity = cityEntityOptional.orElseGet(() -> {
                LOGGER.info("City entity is not found, creating new one");
                CityEntity newCity = new CityEntity();
                newCity.setName(airportPromptModel.getCityName());
                TimezoneEntity timezoneEntity = timezoneRepository.findByTimezone(airportPromptModel.getTimezone()).orElseGet(() -> {
                    TimezoneEntity newTimezone = new TimezoneEntity();
                    newTimezone.setTimezone(airportPromptModel.getTimezone());
                    return newTimezone;
                });
                newCity.setTimezone(timezoneEntity);
                return newCity;
            });
            LOGGER.info("City entity is " + cityEntity);
            airportEntity.setCity(cityEntity);
        }
    }

}