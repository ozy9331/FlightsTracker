package io.vitech.flights.tracker.repository;

import io.vitech.flights.tracker.entity.AircraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AircraftRepository  extends JpaRepository<AircraftEntity, Long> {

    @Query("SELECT a FROM AircraftEntity a WHERE a.aircraftType IS NULL")
    List<AircraftEntity> findAllWithNullAircraftType();

    @Modifying
    @Transactional
    @Query("UPDATE AircraftEntity a SET a.aircraftType = (SELECT at.id FROM AircraftType at WHERE at.iataCode = a.iataCode) WHERE a.aircraftType IS NULL")
    void updateAircraftTypeIdWhereNull();
}
