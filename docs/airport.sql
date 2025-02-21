---- AIRPORT -----
/*
    record count - 6706
    	○ Id - primary key
		○ Api_id - 
		○ Gmt_id -  
		○ Airport_id - same with id/ dublicate
		○ city_iata_code
        ○ iata_code
		○ icao_code
		○ country_iso2
		○ geoname_id
		○ Latitude
		○ Longitude
		○ airport_name
		○ country_name
		○ phone_number
timezone

*/

-- 6706
SELECT COUNT(*) AS record_count FROM airport_raw;
-- 
SELECT COUNT(*) AS record_count FROM airport_raw WHERE id = airport_id;
SELECT COUNT(*) AS record_count FROM airport_raw WHERE iata_code is null

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