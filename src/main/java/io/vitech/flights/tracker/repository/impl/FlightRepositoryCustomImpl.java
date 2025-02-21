package io.vitech.flights.tracker.repository.impl;

import io.vitech.flights.tracker.entity.AirportEntity;
import io.vitech.flights.tracker.entity.FlightEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightRepositoryCustomImpl {

    @PersistenceContext
    private EntityManager entityManager;


    public List<AirportEntity> findTopArrivalAirports(Pageable pageable, LocalDate startDate, LocalDate endDate, Integer rangeStart, Integer rangeEnd) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AirportEntity> query = cb.createQuery(AirportEntity.class);
        Root<FlightEntity> flight = query.from(FlightEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(flight.get("arrivalTime"), startDate.atStartOfDay()));
        }
        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(flight.get("arrivalTime"), endDate.atTime(23, 59, 59)));
        }
        if (rangeStart != null) {
            predicates.add(cb.greaterThanOrEqualTo(flight.get("range"), rangeStart));
        }
        if (rangeEnd != null) {
            predicates.add(cb.lessThanOrEqualTo(flight.get("range"), rangeEnd));
        }

        query.select(flight.get("arrivalAirport"))
                .where(predicates.toArray(new Predicate[0]))
                .groupBy(flight.get("arrivalAirport"))
                .orderBy(cb.desc(cb.count(flight.get("arrivalAirport"))));

        return entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
