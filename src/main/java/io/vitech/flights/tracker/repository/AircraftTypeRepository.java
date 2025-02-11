package io.vitech.flights.tracker.repository;

import io.vitech.flights.tracker.entity.AircraftType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AircraftTypeRepository extends JpaRepository<AircraftType, Long> {
    Optional<AircraftType> findByIataCodeAndType(String iataShortCode, String aircraftType);
}