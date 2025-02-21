
SELECT 
    sqlserver_start_time, 
    cpu_count, 
    physical_memory_kb / 1024 AS Memory_MB 
FROM sys.dm_os_sys_info;

SELECT COUNT(*) FROM flight

--Drop newly created tables 

DROP TABLE IF EXISTS dbo.flight;
DROP TABLE IF EXISTS dbo.flight_status;
DROP TABLE IF EXISTS dbo.aircraft;
DROP TABLE IF EXISTS dbo.airline;
DROP TABLE IF EXISTS dbo.airport;
DROP TABLE IF EXISTS dbo.cities;
DROP TABLE IF EXISTS dbo.timezones;
DROP TABLE IF EXISTS dbo.aircraft_type;
DROP TABLE IF EXISTS dbo.airline_status;


-- Drop entriest for upgrade in liquibase system tracking table

DELETE FROM DATABASECHANGELOG
WHERE AUTHOR = 'yuzdzhan.ahmed';


DELETE FROM DATABASECHANGELOGLOCK;

---- CITY -----
-- entires - 9370

SELECT COUNT(*) AS record_count_cities_raw
FROM cities_raw;

--
SELECT * FROM cities_raw WHERE city_name = 'Columbus'
--1 record with iata code null, 
SELECT * FROM cities_raw WHERE iata_code is NULL

SELECT COUNT(*) FROM cities_raw WHERE longitude is NULL AND latitude is NULL

SELECT COUNT(*) FROM cities_raw WHERE id = city_id

SELECT * FROM cities_raw WHERE gmt is NULL


SELECT COUNT(*) FROM aircraft WHERE id = city_id
SELECT * from aircraft WHERE aircraft_type_id is NULL

--uniqua containt can be achived via combination of city_name and iata code 
SELECT 
    city_name, 
    iata_code, 
    COUNT(*) AS duplicate_count
FROM cities_raw
GROUP BY city_name, iata_code
HAVING COUNT(*) > 1
ORDER BY duplicate_count DESC;

--filter city where the city name and count is same

SELECT city_name, timezone, COUNT(*) AS duplicate_count
FROM [flights-tracker-db-test-1].dbo.[cities_raw]
GROUP BY city_name, timezone
HAVING COUNT(*) > 1;

SELECT iata_code, COUNT(*) AS duplicate_count
FROM dbo.[city]
GROUP BY iata_code
HAVING COUNT(*) > 1;

-- 
SELECT 'cities_raw' AS table_name, COUNT(*) AS record_count
FROM [dbo].[cities_raw]
UNION ALL
SELECT 'city' AS table_name, COUNT(*) AS record_count
FROM [dbo].[city];




---- AIRPORT -----

--
SELECT COUNT(*) AS record_count
FROM [flights-tracker-db-test-1].[dbo].[airport_raw];


-- SELECT airport_name, COUNT(*) AS duplicate_count
-- FROM airport_raw
-- GROUP BY airport_name
-- HAVING COUNT(*) > 1;

SELECT COUNT(*) AS record_count
FROM (
    SELECT DISTINCT airport_name, iata_code
    FROM airport_raw
    WHERE airport_name IS NOT NULL
    AND iata_code IS NOT NULL
) AS distinct_airports;


-- find records where iata_code and city_iata_code is not equal
SELECT *
FROM [flights-tracker-db-test-1].[dbo].[airport_raw]
WHERE iata_code <> city_iata_code;


-- get records from the airport_raw table where city_iata_code is not present in the city table
SELECT *
FROM [dbo].[airport_raw] ar
WHERE ar.city_iata_code IS NOT NULL
AND ar.city_iata_code NOT IN (
    SELECT c.iata_code
    FROM [dbo].[city] c
);

SELECT COUNT(*)  as record_count
FROM ( 
SELECT * FROM [flights-tracker-db-test-1].[dbo].[airport_raw] ar
WHERE ar.city_iata_code IS NOT NULL
AND ar.city_iata_code IN (
    SELECT c.iata_code
    FROM [dbo].[city] c
)) as airtport_city_count;

SELECT COUNT(*) AS record_count
FROM [dbo].[airport];


------ AIRLINES ---------
/*
Migration steps
 --ignore airlines with null name 
    total 21 where 
 -- ignore airlines with name Blocked
     total 143    
*/

SELECT COUNT(*) AS record_count_airline_raw FROM [dbo].[airline_raw];

SELECT COUNT(*) AS callsign FROM [dbo].[airline_raw] WHERE callsign is NULL;

SELECT * FROM [dbo].[airline_raw] WHERE airline_name IS NULL;
SELECT COUNT(*) FROM [dbo].[airline_raw] WHERE [type] IS NULL
 and iata_code is NULL;

SELECT * FROM [dbo].[airline_raw] WHERE callsign IS NULL;

SELECT * FROM [dbo].[airline_raw] WHERE callsign = 'ABALAIR';

SELECT COUNT(*) FROM [dbo].[airline_raw] WHERE id = airline_id 
SELECT COUNT(*) FROM [dbo].[airline_raw] WHERE airline_id is NULL

SELECT * FROM [dbo].[airline_raw] WHERE icao_code IS NULL;

SELECT callsign, COUNT(*)
FROM [dbo].[airline_raw]
GROUP BY callsign
HAVING COUNT(*) > 1;

SELECT COUNT(*) 
SELECT *
FROM [dbo].[airline_raw] 
WHERE icao_code IS NULL
AND iata_code IS NULL;

SELECT *
FROM [dbo].[airline_raw]
WHERE airline_name = 'Alaska Seaplane Service';

--SELECT COUNT(*)
SELECT*
FROM [dbo].[airline_raw]
WHERE airline_name IS NOT NULL
AND airline_name NOT IN ('Blocked')


--airline name and icao code null count

SELECT 
    (SELECT COUNT(*) FROM [dbo].[airline_raw] WHERE airline_name IS NULL) AS airline_name_null_count,
    (SELECT COUNT(*) FROM [dbo].[airline_raw] WHERE icao_code IS NULL) AS icao_code_null_count;


SELECT airline_name, COUNT(*)
FROM [dbo].[airline_raw]
GROUP BY airline_name
HAVING COUNT(*) > 1;


SELECT airline_name, COUNT(*) AS duplicate_count
FROM [dbo].[airline_raw]
WHERE icao_code IS NULL
AND iata_code IS NULL
GROUP BY airline_name
HAVING COUNT(*) > 1;

SELECT DISTINCT COUNT(*)
FROM airline_raw ar
WHERE airline_name IS NOT NULL
AND airline_name NOT IN ('Blocked');

SELECT COUNT(*) from dbo.airline;

--- AIRCRAFT TYPE-----

/*
    Total records -313
    aircraft_name - 2 dublicates 
                    Boeing 727-200
                    Boeing 737-300

    iata_code -     1 dublicate 330
                        



*/
SELECT COUNT(*) as record_count_old from aircraft_type_raw;
SELECT COUNT(*)  from aircraft_type_raw WHERE aircraft_name is NULL
SELECT COUNT(*)  from aircraft_type_raw WHERE iata_code = '787'

SELECT *  from aircraft_type_raw WHERE iata_code = '787'

SELECT * from aircraft_type_raw WHERE iata_code = '330';


SELECT *
FROM airplane_raw ar
LEFT JOIN aircraft_type_raw atr 
    ON ar.iata_code_short = atr.iata_code
WHERE atr.iata_code IS NULL;

SELECT iata_code, COUNT(*) AS count
FROM [dbo].[aircraft_type]
GROUP BY iata_code
HAVING COUNT(*) > 1;






------ AIRCRAFTS ---------
/*
Migration steps
   -airplane_raw 
        total records - 19093
        records with plane_owner NULL - 11187
        production line null - 39

    - iata_code 
        total records - 
        records with plane_owner NULL - 



   -every airplane has reg number
   -airline_id is not valid identifier for maping to airline/, unique for all entries
   -not every aircraft belogns to airline, there was a custom airplanes
*/

SELECT COUNT(*) as record_count_old_airplane_raw from airplane_raw;
SELECT COUNT(*) as record_count_old from aircraft_type_raw;

SELECT COUNT(*) from airplane_raw where registration_date  IS NULL
SELECT COUNT(*) from airplane_raw where rollout_date IS NULL

SELECT COUNT(*) from airplane_raw where production_line is NULL 

SELECT * from airplane_raw where plane_status is NULL 
SELECT COUNT(*) from airplane_raw where plane_status = 'inactive'

 SELECT * from airplane_raw where production_line is NULL 


SELECT DISTINCT plane_status
FROM [dbo].[airplane_raw];

SELECT,, COUNT(*) AS count
FROM [dbo].[aircraft_type_raw]
GROUP BY iata_code
HAVING COUNT(*) > 1;



--287
SELECT COUNT(*) from airplane_raw where first_flight_date IS NULL AND delivery_date is NULL AND registration_date is NULL

SELECT * from airplane_raw where model_code is null

SELECT * from airplane_raw where registration_number = 'G-NIKO'

SELECT registration_number, COUNT(*) AS count
FROM [dbo].[airplane_raw]
GROUP BY registration_number
HAVING COUNT(*) > 1;


SELECT COUNT(*) as record_count from dbo.aircraft;
SELECT COUNT(*) from airplane_raw
WHERE plane_owner IS NULL;

INSERT INTO aircraft (aircraft_model, aircraft_reg, aita_assignment, aircraft_age)
SELECT production_line, registration_number, iata_code_short, plane_age 
FROM airplane_raw
WHERE registration_number IS NOT NULL;



/*
    --aircraft_type_raw---
   total records in DB 313
    1 dublicate iata code, no NULL records
    4 dublicate aircraft_name code , no null records


*/
SELECT * from aircraft_type_raw WHERE aircraft_name is NULL;
SELECT COUNT(*) from aircraft_type_raw;
SELECT COUNT(*) from aircraft_type;

SELECT *
FROM aircraft_type
WHERE iata_code IN (
    SELECT iata_code
    FROM aircraft_type
    GROUP BY iata_code
    HAVING COUNT(*) > 1
);


SELECT *
FROM aircraft_type
WHERE iata_code IN (
    SELECT iata_code
    FROM aircraft_type_raw
    GROUP BY iata_code
    HAVING COUNT(*) > 1
);

SELECT COUNT(*)
FROM aircraft_type_raw
WHERE aircraft_name IN (
    SELECT aircraft_name
    FROM aircraft_type_raw
    GROUP BY aircraft_name
    HAVING COUNT(*) > 1
);


SELECT *
FROM [dbo].[airplane_raw]
WHERE [production_line] IS NULL;

SELECT COUNT(*)
FROM [dbo].[airplane_raw]
WHERE [iata_code_long] = [model_name];


SELECT COUNT(*) FROM [dbo].[airplane_raw] WHERE registration_number IS NULL;

SELECT *
FROM [dbo].[airplane_raw]
WHERE [registration_number] IN (
    SELECT [registration_number]
    FROM [dbo].[airplane_raw]
    GROUP BY [registration_number]
    HAVING COUNT(*) > 1
);



/*  
    MIGRATE TO FLIGHT

    Total records - 18818207
                

    flight data - no records with NULL
    flight_status = NULL - 61750 - UPDATE THEM TO UNKNOWN
    
    departure_airport NULL - 270471
    departure_airport & departure_iata NULL - 0

    - no elements with departure IATA null;

    airline_name NULL 158370
    

    arrival_airport   NULL - 285584
 
 */

 SELECT COUNT(*) from historical_flight_raw as "historical flight total";
 
SELECT COUNT(*) from historical_flight_raw WHERE flight_status IS NULL;

SELECT * FROM flight_status;

UPDATE historical_flight_raw
       SET flight_status = 'UNKNOWN'
       WHERE flight_status IS NULL;

SELECT COUNT(*) from historical_flight_raw WHERE flight_date IS NULL;

SELECT COUNT(*) from historical_flight_raw WHERE departure_airport IS NULL;
SELECT COUNT(*) from historical_flight_raw WHERE departure_airport IS NULL AND departure_iata is NULL;

SELECT * from historical_flight_raw WHERE departure_airport IS NULL;

SELECT COUNT(*) from historical_flight_raw WHERE airline_icao IS NULL;

SELECT * from historical_flight_raw 
WHERE departure_airport IS NULL
AND departure_iata;


SELECT COUNT(*) from historical_flight_raw WHERE airline_name is NULL;

SELECT * FROM airport WHERE iata_code = 'TFU';

SELECT COUNT(*) from historical_flight_raw WHERE arrival_airport IS NULL;

SELECT name
    FROM pg_timezone_names
    WHERE utc_offset = INTERVAL '+08:00';


SELECT COUNT(*)
FROM historical_flight_raw hfr
JOIN airport a ON hfr.departure_iata = a.iata_code
WHERE hfr.departure_iata IS NOT NULL 
AND hfr.departure_airport is NULL;

SELECT COUNT(*)
FROM historical_flight_raw hfr
WHERE hfr.departure_iata IS NULL;


SELECT COUNT(*)
FROM historical_flight_raw hfr
--JOIN airport a ON hfr.departure_iata = a.iata_code
WHERE hfr.departure_iata IS NOT NULL 
AND hfr.departure_airport IS NULL;


SELECT DISTINCT hfr.*
FROM historical_flight_raw hfr
LEFT JOIN airport a ON hfr.departure_iata = a.iata_code
WHERE a.iata_code IS NULL
AND hfr.departure_iata IS NOT NULL
AND hfr.departure_airport IS NULL;

SELECT * FROM historical_flight_raw where destination_iata is null

SELECT flight_date, departure_iata_code, arrival_iata_code, COUNT(*)
FROM flight
GROUP BY flight_date, departure_iata_code, arrival_iata_code
HAVING COUNT(*) > 1;

SELECT * FROM historical_flight_raw WHERE departure_iata = 'KIX' AND arrival_iata = 'HGH' AND  flight_date = '2023-11-05';

SELECT COUNT(*) from historical_flight_raw;

SELECT COUNT(*) from flight;

SELECT COUNT(*) from historical_flight_raw where airline_icao is NULL;

SELECT COUNT(*) from historical_flight_raw where airline_icao is NULL AND airline_icao is NULL;

/*
  seperate records by status 
    UNKNOWN  -   325800
    CANCELED -   434562
    DIVERTED -    40034
    landed -   14166269
    active -    1676726
    scheduled-  2174816
*/

SELECT COUNT(*) as unknown from historical_flight_raw WHERE flight_status = 'UNKNOWN'
SELECT COUNT(*) as CANCELED from historical_flight_raw WHERE flight_status = 'CANCELLED'
SELECT COUNT(*) as diverted from historical_flight_raw WHERE flight_status = 'DIVERTED'
SELECT COUNT(*) as landed from historical_flight_raw WHERE flight_status = 'LANDED'
SELECT COUNT(*) as active from historical_flight_raw WHERE flight_status = 'ACTIVE'
SELECT COUNT(*) as scheduled from historical_flight_raw WHERE flight_status = 'SCHEDULED'

/*
    MIGRATE CANCELLED FLIGHTS - 434562

    all canceled flights has departure_iata
    airline 
    airport
    arrival airport
    
*/

SELECT COUNT(*) as CANCELED from historical_flight_raw WHERE flight_status = 'CANCELLED' AND departure_iata is NULL;
SELECT COUNT(*) as CANCELED from historical_flight_raw WHERE flight_status = 'CANCELLED' AND departure_actual is NULL;

INSERT INTO flight (flight_date, departure_time, arrival_time, departure_iata_code, arrival_iata_code, departure_airport_id, arrival_airport_id, flight_status_id, aircraft_id, airline_id)
SELECT flight_date, departure_scheduled, arrival_scheduled, departure_iata, NULL, NULL, NULL, fs.id, NULL, NULL
FROM historical_flight_raw hfr
JOIN flight_status fs ON fs.status = 'CANCELLED'
WHERE flight_status = 'CANCELLED';

SELECT COUNT(*) as CANCELED from [flights-tracker-db-test-02].dbo.flight WHERE flight_status_id = 5 AND departure_actual is NULL;


/*
    MIGRATE DIVERTED FLIGHTS - 40034

    all diverted flights has departure_iata 
    all diverted flights has arrival_iata





    airline 
    airport
    
*/


SELECT COUNT(*) as DIVERTED_departure_iata_NULL from historical_flight_raw WHERE flight_status = 'DIVERTED' 
AND departure_iata is NULL;

SELECT COUNT(*) as DIVERTED_arrival_iata_NULL from historical_flight_raw WHERE flight_status = 'DIVERTED' 
AND arrival_iata is NULL;


SELECT COUNT(*) as DIVERTED_departure_actual_NULL from historical_flight_raw WHERE flight_status = 'DIVERTED' 
AND departure_actual is NULL AND departure_estimated is NULL;

SELECT * from historical_flight_raw 
WHERE flight_status = 'ACTIVE' 
AND departure_actual is NULL AND departure_estimated IS NULL;

SELECT COUNT(*) as DIVERTED_arrival_actual_NULL from historical_flight_raw WHERE flight_status = 'DIVERTED' 
AND arrival_actual is NULL;


SELECT COUNT(*) as DIVERTED from historical_flight_raw WHERE flight_status = 'CANCELLED' AND departure_actual is NULL;


--UPDATE daperture_actual where is not present with deperture_estimated 
UPDATE historical_flight_raw
SET departure_actual = departure_estimated
WHERE departure_actual IS NULL
AND departure_estimated IS NOT NULL
AND flight_status IN ('DIVERTED', 'LANDED', 'ACTIVE');

UPDATE historical_flight_raw
SET arrival_actual = arrival_estimated
WHERE arrival_actual IS NULL
AND arrival_estimated IS NOT NULL
AND flight_status IN ('LANDED');



-- count 35027
SELECT COUNT(*) as DIVERTED_aircraft_reg_NULL from historical_flight_raw WHERE flight_status = 'DIVERTED' 
AND aircraft_registration is NULL AND aircraft_iata is NULL;

SELECT COUNT(*) as DIVERTED_aircraft_reg_NULL from historical_flight_raw WHERE flight_status = 'DIVERTED' 
AND aircraft_iata is NULL;


-- Problem comes from airline ID to insert more than expected

SELECT COUNT(*) as DIVERTED_aircraft_reg_NULL from historical_flight_raw WHERE flight_status = 'DIVERTED' 
AND airline_iata is NULL 


SELECT * from historical_flight_raw WHERE flight_status = 'DIVERTED' 
AND airline_iata is NULL 


UPDATE historical_flight_raw
SET airline_name = 'N/A', 
    airline_iata = 'N/A', 
    airline_icao = 'N/A'
WHERE (airline_name IS NULL OR airline_name = 'empty' OR airline_name = 'Private owner')
    AND airline_iata IS NULL 
    AND airline_icao IS NULL;


DELETE FROM flight;

SELECT COUNT(*) FROM flight;

INSERT INTO flight 
            (
                flight_date, 
                departure_time, 
                arrival_time, 
                departure_iata_code, 
                arrival_iata_code, 
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
                NULL, 
                NULL, 
                fs.id AS flight_status_id, 
                ac.id AS aircraft_id, 
                al.id AS airline_id
            FROM historical_flight_raw hfr
            JOIN flight_status fs ON fs.status = 'DIVERTED'
            LEFT JOIN aircraft ac ON hfr.aircraft_registration = ac.aircraft_reg
            LEFT JOIN airline al ON hfr.airline_iata = al.iata_code
            WHERE hfr.flight_status = 'DIVERTED';


/*

    Total rows - 18818207

    With all joins     - 19343247

    airline JOIN       - 19343247
    aircraft JOIN      - 19343247
    flight_status JOIN - 19343247
    arrival_airport JOIN - 19343247

*/



SELECT COUNT (*) from historical_flight_raw WHERE flight_status='LANDED'
SELECT COUNT(*) from flight 
DELETE from flight

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
    airline_iata - NULL - 241760 -
    airline_icao - NULL - 143229 - 
    airline_iata & airline_icao - NULL - 140792 -- 0.993%
    aircraft_registration - NULL - 11960059 - 84.41%

*/

SELECT COUNT(*) FROM historical_flight_raw WHERE flight_status = 'LANDED' AND aircraft_icao is NULL;

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

