package io.vitech.flights.tracker.repository;

import io.vitech.flights.tracker.entity.AirlineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlineReposity extends JpaRepository<AirlineEntity, Long> {
}
