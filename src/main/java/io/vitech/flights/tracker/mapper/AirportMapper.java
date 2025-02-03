package io.vitech.flights.tracker.mapper;

import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.entity.CityEntity;
import io.vitech.flights.tracker.openai.model.AirportGptModel;
import io.vitech.flights.tracker.repository.CityRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.logging.Logger;

@Mapper(config = BaseMapper.class)
public interface AirportMapper extends BaseMapper<AirportEntity, AirportGptModel> {
     Logger LOGGER = Logger.getLogger(AirportMapper.class.getName());

    @Override
    @Mapping(source = "iata_code", target = "iata")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "city.timezone", target = "timezone")
    AirportGptModel toDto(AirportEntity airportEntity);

    @Override
    AirportEntity toEntity(AirportGptModel airportGptModel);

    @AfterMapping
    default void afterToEntity(AirportGptModel airportGptModel, @MappingTarget AirportEntity airportEntity, @Autowired CityRepository cityRepository) {
        LOGGER.info("Mapping airport entity from GptModel");
        if (airportGptModel.getCityName() != null) {
            LOGGER.info("City name is not null");
            Optional<CityEntity> cityEntityOptional = cityRepository.findByNameAndTimezone(airportGptModel.getCityName(), airportGptModel.getTimezone());
            CityEntity cityEntity = cityEntityOptional.orElseGet(() -> {
                LOGGER.info("City entity is not found, creating new one");
                CityEntity newCity = new CityEntity();
                newCity.setName(airportGptModel.getCityName());
                newCity.setTimezone(airportGptModel.getTimezone());
                return newCity;
            });
            LOGGER.info("City entity is " + cityEntity);
            airportEntity.setCity(cityEntity);
        }
    }
}