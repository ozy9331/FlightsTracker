package io.vitech.flights.tracker.repository;

import io.vitech.flights.tracker.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, Long> {

    @Query("SELECT f FROM FlightEntity f WHERE f.departureAirport.id IS NULL AND f.flightStatus.status = ?1")
    List<FlightEntity> findAllByNullDepartureAirportIdAndStatus(String status);

    @Query("SELECT f FROM FlightEntity f WHERE f.arrivalAirport.id IS NULL AND f.flightStatus.status = ?1")
    List<FlightEntity> findAllByNullArrivalAirportIdAndStatus(String status);

}
