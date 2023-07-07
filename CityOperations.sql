/*
int createCity​(java.lang.String name)
Creates new city.
Parameters:
name - the name of the city. Name of the cities must be unique.
Returns:
city id, or -1 on failure
*/
INSERT INTO City (Name) VALUES ('')

/*
java.util.List<java.lang.Integer> getCities()
Gets all cities
Returns:
ids of cities, null if failure
*/
SELECT ID
FROM City

/*
int connectCities​(int cityId1, int cityId2, int distance)
Connects two cities. There can be max one line between cities.
Parameters:
cityId1 - id of the first city
cityId2 - id of the second city
distance - distance between cities (distance is measured in days)
Returns:
line id, or -1 on failure
*/
SELECT ID
FROM Connection
WHERE CityID1 = 1 and CityID2 = 1

INSERT INTO Connection (Distance, CityID1, CityID2) VALUES (1, 1, 1)

/*
java.util.List<java.lang.Integer> getConnectedCities​(int cityId)
Get connected cities.
Parameters:
cityId - id of the city that connections are asked for
Returns:
list of connected cities ids
*/
SELECT CityID1
FROM Connection
WHERE CityID2 = 1

SELECT CityID2
FROM Connection
WHERE CityID1 = 1

/*
java.util.List<java.lang.Integer> getShops​(int cityId)
Get shops in the city.
Parameters:
cityId - id of the city
Returns:
list of ids of shops, null if failure
*/
SELECT M.ID
FROM Member M join Shop S on M.ID = S.ID
WHERE M.CityID = 1