﻿/*
void eraseAll()
Clears data in database.
*/
EXEC sp_MSForEachTable 'DISABLE TRIGGER ALL ON ?'

EXEC sp_MSForEachTable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL'

EXEC sp_MSForEachTable 'DELETE FROM ?'

EXEC sp_MSForEachTable 'ALTER TABLE ? CHECK CONSTRAINT ALL'

EXEC sp_MSForEachTable 'ENABLE TRIGGER ALL ON ?'