----- AIRLINES ---------
/*
Migration steps -13130
 --
Columns:
		○ Id - primary key
		○ fleet_average_age -  Null- 12172
		○ airline_id -  not match with id, no nulls
		○ callsign - NULL -3858, not unique
		○ hub_code -  NULL - 8746
		○ iata_code -  NULL - 9170
		○ icao_code -  NULL - 1167
		○ country_iso2 - NULL 731
		○ date_founded - NULL 12218
		○ iata_prefix_accounting-  NULL 11954
		○ airline_name - NULL-  21
		○ country_name - 677
		○ fleet_size - NULL -  12168
		○ Status - 
            active	7345
            disabled	564
            historical	4675
            historical/administration	1
            merged	72
            not_ready	43
            renamed	109
            restarting	5
            start_up	11
            unknown	305
        ○ Type 8197



 ignore airlines with null name 
    total 21 where 
 -- ignore airlines with name Blocked
     total 143    
*/



SELECT DISTINCT [status]
FROM [dbo].[airline_raw]
ORDER BY [status];

SELECT [status], COUNT(*) AS status_count
FROM [dbo].[airline_raw]
GROUP BY [status]
ORDER BY [status];



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