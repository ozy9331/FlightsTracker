package io.vitech.flights.tracker.service

import io.vitech.flights.tracker.entity.AirportEntity
import io.vitech.flights.tracker.entity.FlightEntity
import io.vitech.flights.tracker.repository.FlightRepository
import io.vitech.flights.tracker.repository.impl.FlightRepositoryCustomImpl
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification

import java.time.LocalDate

class FlightServiceTest extends Specification {
    def flightRepositoryCustom = Mock(FlightRepositoryCustomImpl)
    def flightRepository = Mock(FlightRepository)
    def flightService = new FlightService(flightRepositoryCustom, flightRepository)

    def "getAllFlights should return a page of flights"() {
        given:
            Pageable pageable = PageRequest.of(0, 10)
            Page<FlightEntity> flightPage = Mock(Page)
            flightRepository.findAll(pageable) >> flightPage

        when:
            def result = flightService.getAllFlights(pageable, null, null)

        then:
            result == flightPage
    }

    def "getFlightById should return a flight entity"() {
        given:
            def flightId = 1L
            def flightEntity = new FlightEntity()
            flightRepository.findById(flightId) >> Optional.of(flightEntity)

        when:
            def result = flightService.getFlightById(flightId.intValue())

        then:
            result == flightEntity
    }

    def "getTopArrivalAirports should return a list of top arrival airports"() {
        given:
            def limit = 5
            def airportEntities = [new AirportEntity(), new AirportEntity()]
            flightRepository.findTopArrivalAirports(PageRequest.of(0, limit)) >> airportEntities

        when:
            def result = flightService.getTopArrivalAirports(limit)

        then:
            result == airportEntities
    }

    def "getTopDepartureAirports should return a list of top departure airports"() {
        given:
            def limit = 5
            def airportEntities = [new AirportEntity(), new AirportEntity()]
            flightRepository.findTopDepartureAirports(PageRequest.of(0, limit)) >> airportEntities

        when:
            def result = flightService.getTopDepartureAirports(limit)

        then:
            result == airportEntities
    }

    def "getTopArrivalAirports with date range should return a list of top arrival airports"() {
        given:
            def limit = 5
            def startDate = LocalDate.now().minusDays(10)
            def endDate = LocalDate.now()
            def rangeStart = 0
            def rangeEnd = 100
            def airportEntities = [new AirportEntity(), new AirportEntity()]
            flightRepositoryCustom.findTopArrivalAirports(PageRequest.of(0, limit), startDate, endDate, rangeStart, rangeEnd) >> airportEntities

        when:
            def result = flightService.getTopArrivalAirports(limit, startDate, endDate, rangeStart, rangeEnd)

        then:
            result == airportEntities
    }

    def "getTopDepartureAirports with date range should return a list of top departure airports"() {
        given:
            def limit = 5
            def startDate = LocalDate.now().minusDays(10)
            def endDate = LocalDate.now()
            def rangeStart = 0
            def rangeEnd = 100
            def airportEntities = [new AirportEntity(), new AirportEntity()]
            flightRepository.findTopDepartureAirports(PageRequest.of(0, limit)) >> airportEntities

        when:
            def result = flightService.getTopDepartureAirports(limit, startDate, endDate, rangeStart, rangeEnd)

        then:
            result == airportEntities
    }
}
