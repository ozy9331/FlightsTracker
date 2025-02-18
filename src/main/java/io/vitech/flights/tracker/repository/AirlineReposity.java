package io.vitech.flights.tracker.repository;

import io.vitech.flights.tracker.entity.AirlineEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AirlineReposity extends JpaRepository<AirlineEntity, Long> {

    @Query("SELECT c FROM AirlineEntity c WHERE c.name LIKE :name%")
    Page<AirlineEntity> findByName(String name, PageRequest pageRequest);

    @Query("SELECT DISTINCT a FROM AirlineEntity a WHERE a.fleetSize is NULL")
    List<AirlineEntity> findAllWithNullFleetSize();

    @Query("SELECT DISTINCT a FROM AirlineEntity a WHERE a.name is NULL")
    List<AirlineEntity> findAllWithNullName();

    @Modifying
    @Transactional
    @Query("UPDATE AirlineEntity a SET a.name = :name, a.iataCode = :iataCode, a.icaoCode = :icaoCode, a.fleetSize = :fleetSize, a.dateFounded = :dateFounded WHERE a.id = :id")
    int updateAirlineById(Long id, String name, String iataCode, String icaoCode, Integer fleetSize, String dateFounded);
}
