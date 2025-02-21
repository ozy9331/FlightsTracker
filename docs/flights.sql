


-- all records 14166269
SELECT COUNT(*) from flight

-- 309901
SELECT COUNT(*) from flight WHERE departure_airport_id is null 
-- 181798
SELECT COUNT(*) from flight WHERE arrival_airport_id is null 








/*
  
    Total rows - 18818207

    seperate records by status 
    
    UNKNOWN  -   325800
    CANCELED -   434562
    DIVERTED -    40034
    landed -   14166269
    active -    1676726
    scheduled-  2174816

*/



SELECT COUNT (*) from historical_flight_raw WHERE flight_status='LANDED' 

 ---- So, 9,185,958 is approximately 64.84% of 14,166,269.
SELECT COUNT(*) from flight WHERE range is NULL
SELECT COUNT(*) from flight 
DELETE from flight
-- GMT NULL 333
SELECT COUNT(*) from airport_raw WHERE gmt is NULL
-- API ID  NULL 0
SELECT COUNT(*) from airport_raw WHERE api_id is NULL
-- airport_id NULL 0
SELECT COUNT(*) from airport_raw WHERE airport_id is NULL
-- GMT NULL 333
SELECT COUNT(*) from airport_raw WHERE gmt is NULL
-- GMT NULL 333
SELECT COUNT(*) from airport_raw WHERE gmt is NULL

DELETE FROM DATABASECHANGELOGLOCK WHERE ID = 1;
SELECT * FROM DATABASECHANGELOGLOCK


WITH airline_match AS (
    SELECT id, iata_code AS airline_code FROM airline
    UNION ALL
    SELECT id, icao_code AS airline_code FROM airline
)
INSERT INTO flight (
    flight_date,
    departure_time,
    arrival_time,
    departure_iata_code,
    arrival_iata_code,
    range,
    departure_airport_id,
    arrival_airport_id,
    flight_status_id,
    aircraft_id,
    airline_id
)
SELECT
    hfr.flight_date,
    hfr.departure_scheduled,
    hfr.arrival_scheduled,
    hfr.departure_iata,
    hfr.arrival_iata,
    hfr.range_post_processing,
    1, 1, 1,1,
    --ac.id AS aircraft_id,
    al.id AS airline_id
FROM historical_flight_raw hfr
--LEFT JOIN aircraft ac ON hfr.aircraft_registration = ac.aircraft_reg
LEFT JOIN airline_match al ON hfr.airline_iata = al.airline_code;




---PROCESS RECORDS IN LANDED STATUS-----
/*
     landed -   14166269  - 75.29% from all
        
     flight_data NULL - 0 
     range_post_processing - NULL - 9185958 - 64.84% from landed records
                             <=0  2130 - 0.015% - a flight in which an aircraft takes off and lands at the same airport
    departure_iata_code - NULL - 0
    arrival_iata - NULL - 0
    airline_iata -NULL - 241760 -
    airline_icao -NULL - 143229 - 
    airline_iata & airline_icao - NULL - 140792 -- 0.993%
    aircraft_registration - NULL - 11960059 - 84.41%

*/

SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED';

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

---
SELECT COUNT(*) from flight
DELETE from flight

INSERT INTO flight (
    flight_date,
    departure_time,
    arrival_time,
    range,
    departure_iata_code,
    arrival_iata_code,
    airline_iata,
    airline_icao,
    flight_status_id,
    departure_airport_id,
    arrival_airport_id,
    aircraft_id,
    airline_id
)
SELECT
    hfr.flight_date,
    hfr.departure_actual AS departure_time,
    hfr.arrival_actual AS arrival_time,
    hfr.range_post_processing AS range,
    hfr.departure_iata AS departure_iata_code,
    hfr.arrival_iata AS arrival_iata_code,
    hfr.airline_iata AS airline_iata,
    hfr.airline_icao AS airline_icao,
    (SELECT id FROM flight_status WHERE status = 'LANDED') AS flight_status_id,
    NULL AS departure_airport_id, 
    NULL AS arrival_airport_id,   
    a.id AS aircraft_id,          
    NULL AS airline_id            
FROM dbo.historical_flight_raw hfr
LEFT JOIN aircraft a ON hfr.aircraft_registration = a.aircraft_reg
WHERE hfr.flight_status = 'LANDED';

ALTER TABLE airport
DROP CONSTRAINT uk_airport_name_iata_code;







SELECT arrival_airport_id, COUNT(*) AS count
FROM flight
GROUP BY arrival_airport_id DESK;


SELECT COUNT(*) from flight WHERE arrival_airport_id = 4592


  SELECT COUNT(*) from city

