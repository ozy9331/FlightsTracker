package io.vitech.flights.tracker.repository;

import io.vitech.flights.tracker.entity.AirportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirportRepository extends JpaRepository<AirportEntity, Long> {
    Optional<AirportEntity> findByIataCodeAndName(String iataCode, String name);
}
