----- AIRPORTS---------
/*
    total records - 6706


*/

-- GMT NULL 333
SELECT COUNT(*) from airport_raw WHERE gmt is NULL
-- API ID  NULL 0
SELECT COUNT(*) from airport_raw WHERE api_id is NULL
-- airport_id NULL 0
SELECT COUNT(*) from airport_raw WHERE airport_id is NULL
-- city_iata_code NULL 229
SELECT COUNT(*) from airport_raw WHERE city_iata_code is NULL
-- icao_code NULL 0
SELECT COUNT(*) from airport_raw WHERE icao_code is NULL
-- country_iso2  NULL 2
SELECT COUNT(*) from airport_raw WHERE country_iso2 is NULL
-- geoname_id NULL 124
SELECT COUNT(*) from airport_raw WHERE geoname_id is NULL
-- Latitude  NULL 6
SELECT COUNT(*) from airport_raw WHERE Latitude  is NULL
-- Longitude  NULL 6
SELECT COUNT(*) from airport_raw WHERE Longitude  is NULL
-- Latitude & Longitute NULL 6 elements same entries
SELECT COUNT(*) from airport_raw WHERE Longitude  is NULL AND Latitude is NULL 
-- airport_name NULL 0
SELECT COUNT(*) from airport_raw WHERE airport_name is NULL
-- country_name  NULL 204
SELECT COUNT(*) from airport_raw WHERE country_name is NULL
-- phone_number NULL 6525
SELECT COUNT(*) from airport_raw WHERE phone_number is NULL
-- timezone  NULL 6
SELECT COUNT(*) from airport_raw WHERE timezone  is NULL



-- Latitude & Longitute NULL 6 elements same entries
SELECT * from airport_raw WHERE Longitude  is NULL AND Latitude is NULL 