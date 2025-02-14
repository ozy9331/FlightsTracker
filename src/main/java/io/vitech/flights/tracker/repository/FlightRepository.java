package io.vitech.flights.tracker.repository;

import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.entity.FlightEntity;
import io.vitech.flights.tracker.helper.AirportPair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, Long> {

    @Query("SELECT f FROM FlightEntity f WHERE f.departureAirport.id IS NULL AND f.flightStatus.status = ?1")
    List<FlightEntity> findAllByNullDepartureAirportIdAndStatus(String status);

    @Query("SELECT f FROM FlightEntity f WHERE f.arrivalAirport.id IS NULL AND f.flightStatus.status = ?1")
    List<FlightEntity> findAllByNullArrivalAirportIdAndStatus(String status);

    @Query("SELECT f.arrivalAirport FROM FlightEntity f GROUP BY f.arrivalAirport ORDER BY COUNT(f.arrivalAirport) DESC")
    List<AirportEntity> findTopArrivalAirports(Pageable pageable);

    @Query("SELECT f.departureAirport FROM FlightEntity f GROUP BY f.departureAirport ORDER BY COUNT(f.departureAirport) DESC")
    List<AirportEntity> findTopDepartureAirports(Pageable pageable);

    @Query("SELECT DISTINCT f.arrivalIataCode FROM FlightEntity f WHERE f.arrivalAirport IS NULL")
    List<String> findDistinctArrivalIataWhereAirportIsNull();

    @Query("SELECT DISTINCT f.departureIataCode FROM FlightEntity f WHERE f.departureAirport IS NULL")
    List<String> findDistinctDepartureIataWhereAirportIsNull();

    @Query("SELECT NEW io.vitech.flights.tracker.helper.       AirportPair(f.departureAirport.id, f.arrivalAirport.id, " +
            "dep.latitude, dep.longitude, arr.latitude, arr.longitude) " +
            "FROM FlightEntity f " +
            "JOIN AirportEntity dep ON f.departureAirport.id = dep.id " +
            "JOIN AirportEntity arr ON f.arrivalAirport.id = arr.id " +
            "WHERE f.departureAirport IS NOT NULL AND f.arrivalAirport IS NOT NULL AND f.range IS NULL " +
            "GROUP BY f.departureAirport.id, f.arrivalAirport.id, dep.latitude, dep.longitude, arr.latitude, arr.longitude")
    Set<AirportPair> findUniqueAirportPairs();

    @Modifying
    @Transactional
    @Query("UPDATE FlightEntity f SET f.arrivalAirport  = (SELECT a.id FROM AirportEntity a WHERE a.iataCode = f.arrivalIataCode) WHERE f.arrivalAirport IS NULL")
    void updateArrivalAirportIds();

    @Modifying
    @Transactional
    @Query("UPDATE FlightEntity f SET f.departureAirport  = (SELECT a.id FROM AirportEntity a WHERE a.iataCode = f.departureIataCode) WHERE f.departureAirport IS NULL")
    void updateDepartureAirportIds();

    @Modifying
    @Transactional
    @Query("UPDATE FlightEntity f SET f.range = :range WHERE f.departureAirport.id = :departureId AND f.arrivalAirport.id = :arrivalId AND f.range IS NULL")
    int updateFlightRange(@Param("departureId") Long departureId,
                          @Param("arrivalId") Long arrivalId,
                          @Param("range") Double range);


    @Query(value = """
    SELECT TOP (:limit) 
           a.id AS airportId, 
           a.airport_name AS airportName, 
           COUNT(f.id) AS totalFlights
    FROM flight f
    JOIN airport a ON f.arrival_airport_id = a.id
    WHERE (:startRange IS NULL OR f.range >= :startRange)
      AND (:endRange IS NULL OR f.range <= :endRange)
    GROUP BY a.id, a.airport_name
    ORDER BY totalFlights DESC
""", nativeQuery = true)
    List<Object[]> findTopDestinations(@Param("limit") int limit,
                                       @Param("startRange") Double startRange,
                                       @Param("endRange") Double endRange);


    @Query(value = """
    WITH FlightCounts AS (
        SELECT 
            DATENAME(WEEKDAY, f.flight_date) AS dayOfWeek, 
            COUNT(*) AS totalFlights
        FROM flight f
        WHERE (:startRange IS NULL OR f.range >= :startRange)
          AND (:endRange IS NULL OR f.range <= :endRange)
        GROUP BY DATENAME(WEEKDAY, f.flight_date)
    ), 
    MedianFlights AS (
        SELECT 
            PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY totalFlights) 
            OVER () AS medianValue 
        FROM FlightCounts
    )
    SELECT fc.dayOfWeek, fc.totalFlights 
    FROM FlightCounts fc
    JOIN MedianFlights mf 
    ON fc.totalFlights >= mf.medianValue
    ORDER BY fc.totalFlights DESC
""", nativeQuery = true)
    List<Object[]> findBusiestDays(@Param("startRange") Double startRange,
                                   @Param("endRange") Double endRange);
}
