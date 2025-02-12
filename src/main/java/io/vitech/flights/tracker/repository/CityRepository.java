package io.vitech.flights.tracker.repository;

import io.vitech.flights.tracker.entity.CityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {

    @Query("SELECT c FROM CityEntity c JOIN c.timezone t WHERE c.name = :name AND t.timezone = :timezone")
    Optional<CityEntity> findByNameAndTimezone(String name, String timezone);

    @Query("SELECT c FROM CityEntity c WHERE c.name LIKE :name%")
    Page<CityEntity> findByName(String name, PageRequest pageRequest);
}