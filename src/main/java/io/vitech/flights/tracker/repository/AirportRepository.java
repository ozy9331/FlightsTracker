package io.vitech.flights.tracker.repository;

import io.vitech.flights.tracker.entity.AirportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<AirportEntity, Long> {
    Optional<AirportEntity> findByIataCodeAndName(String iataCode, String name);

    @Query("SELECT a FROM AirportEntity a WHERE a.name LIKE :name%")
    Page<AirportEntity> findByName(String name, PageRequest pageRequest);

    @Query("SELECT a FROM AirportEntity a WHERE a.city.name LIKE ':city%'")
    Page<AirportEntity> findByCityName(String city, PageRequest pageRequest);

    @Query("SELECT a FROM AirportEntity a WHERE a.name LIKE :name% AND a.city.name LIKE :city%")
    Page<AirportEntity> findByNameAndCityName(String name, String city, PageRequest pageRequest);

    @Query("SELECT a FROM AirportEntity a WHERE a.latitude IS NULL OR a.longitude IS NULL")
    List<AirportEntity> findAllWithNullLatitudeOrLongitude();

    @Modifying
    @Transactional
    @Query("UPDATE AirportEntity a SET a.latitude = :latitude, a.longitude = :longitude, a.city.id = :cityId WHERE a.iataCode = :iataCode AND name = :name" )
    int updateAirportDetailsByIataCode(String name, Double latitude, Double longitude, Long cityId, String iataCode);
}
