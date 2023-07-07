/*
java.util.Calendar time​(int days)
Time to pass in simulation.
Parameters:
days - number of days that will pass in simulation after this method call
Returns:
current time
*/
UPDATE [Order]
SET Status = 'arrived', ReceivedTime = ''
WHERE ID = 1

UPDATE [Order]
SET Assembled = 1
WHERE ID = 1

UPDATE [Order]
SET CityID = 1
WHERE ID = 1

SELECT Distance
FROM Connection
WHERE (CityID1 = 1 and CityID2 = 2) or (CityID1 = 2 and CityID2 = 1)

/*
void eraseAll()
Clears data in database.
*/
EXEC sp_MSForEachTable 'DISABLE TRIGGER ALL ON ?'

EXEC sp_MSForEachTable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL'

EXEC sp_MSForEachTable 'DELETE FROM ?'

EXEC sp_MSForEachTable 'ALTER TABLE ? CHECK CONSTRAINT ALL'

EXEC sp_MSForEachTable 'ENABLE TRIGGER ALL ON ?'