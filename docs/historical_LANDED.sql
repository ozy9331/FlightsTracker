---PROCESS RECORDS IN LANDED STATUS-----

/*
     landed -   14166269  - 75.29% from ALL

    departure_icao   NULL - 367
    departure_iata - NULL - 0
            unique_departure_iata_count - 5532
            unique_arrival_iata_count -5512

     flight_data NULL - 0 
     range_post_processing - NULL - 9185958 - 64.84% from landed records
                             <=0  2130 - 0.015% - a flight in which an aircraft takes off and lands at the same airport
   
    arrival_iata - NULL - 0
    airline_iata - NULL - 241760 -
    airline_icao - NULL - 143229 - 
    airline_iata & airline_icao - NULL - 140792 -- 0.993%
    aircraft_registration - NULL - 11960059 - 84.41%

*/

SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED'


SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND departure_icao is NULL;

SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND departure_iata is NULL;
-- 
SELECT COUNT(DISTINCT arrival_iata) AS unique_departure_iata_count
FROM [dbo].[historical_flight_raw]
WHERE arrival_iata IS NOT NULL;

--

SELECT COUNT(DISTINCT h.departure_iata) AS missing_departure_iata_count
FROM [dbo].[historical_flight_raw] h
LEFT JOIN [dbo].[airport] a ON h.departure_iata = a.iata_code
WHERE h.departure_iata IS NOT NULL
AND a.iata_code IS NULL;

-- 0 records with flight data NULL
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND flight_date is NULL;

-- 0 records with range_post_processing data NULL
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND range_post_processing is NULL;
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND (range_post_processing IS NULL OR range_post_processing <= 0);
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND range_post_processing <= 0;
-- a flight in which an aircraft takes off and lands at the same airport
SELECT * FROM historical_flight_raw WHERE flight_status = 'LANDED' AND range_post_processing <= 0;

--AIRPORTS  -- save to flight  departure_iata & airline_iata and map after migrate to table
------
-- 0 records with departure_iata  NULL
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND departure_iata is NULL;
-- 0 records with arrival_iata  NULL
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND arrival_iata is NULL;
--AIRLINE save to flight  airline_iata & airline_icao and map after migrate to table
   -- airline which don't have iata and icoa code  can be mapped via aircraft which have reg number
------
-- 241760 records with airline_iata  NULL
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND airline_iata is NULL;
-- 143229 records with airline_icao  NULL
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND airline_icao is NULL;
-- 140792  airline_iata&airline_icao - NULL
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND airline_icao IS NULL AND airline_iata IS NULL;
-- 140792 airline_iata & airline_icao & flight_codeshared_airline_iata & flight_codeshared_airline_icao - NULL
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND airline_icao IS NULL AND airline_iata IS NULL AND flight_codeshared_airline_iata is NULL AND flight_codeshared_airline_icao is NULL ;
--AIRCRAFT  save to flight  airline_iata & airline_icao and map after migrate to table
------  aircraft_registration - NULL - 11960059 - 84.41%
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND aircraft_registration is NULL;


--DEPARTURE AND ARRIVAL TIMES 
    -- update where departure actual is null with departure scheduled
    -- update where arrival actual is null with arrival scheduled
------  departure_actual - NULL - 25590 
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND departure_actual is NULL;
---departure_actual & departure_scheduled - 0
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND departure_actual is NULL AND departure_scheduled is NULL;
------  departure_actual - NULL - 20169 
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND arrival_actual is NULL;
--- arrival_actual & arrival_scheduled - 0
SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND arrival_actual is NULL AND arrival_scheduled is NULL;