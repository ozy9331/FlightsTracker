package io.vitech.flights.tracker.repository;

import io.vitech.flights.tracker.entity.AircraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AircraftRepository  extends JpaRepository<AircraftEntity, Long> {
}
