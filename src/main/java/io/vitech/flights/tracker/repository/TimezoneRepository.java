package io.vitech.flights.tracker.repository;

import io.vitech.flights.tracker.entity.CityEntity;
import io.vitech.flights.tracker.entity.TimezoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimezoneRepository extends JpaRepository<TimezoneEntity, Long> {

    Optional<TimezoneEntity> findByTimezone(String timezone);
}