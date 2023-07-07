/*
int addArticle​(int orderId, int articleId, int count)
Adds article to order. It adds articles only if there 
are enough of them in shop. If article is in order already, 
it only increases count.
Parameters:
orderId - order id
articleId - article id
count - number of articles to be added
Returns:
item id (item contains information about number of 
article instances in particular order), -1 if failure
*/
SELECT Count
FROM Catalog
WHERE ID = 1

SELECT Price
FROM Article
WHERE ID = 1

SELECT ID
FROM Item
WHERE ArticleID = 1 and OrderID = 1

UPDATE Item
SET Count = Count + 5, 
	Price = Price + 5
WHERE ArticleID = 1

INSERT INTO Item (Count, Price, ArticleID, OrderID)
VALUES (1, 1, 1, 1)

UPDATE Catalog
SET Count = Count - 5
WHERE ArticleID = 1

/*
int removeArticle​(int orderId, int articleId)
Removes article from order.
Parameters:
orderId - order id
articleId - article id
Returns:
1 if success, -1 if failure
*/
SELECT Count
FROM Item
WHERE ArticleID = 1 and OrderID = 1

DELETE FROM Item
WHERE ArticleID = 1 and OrderID = 1

UPDATE Catalog
SET Count = Count + 5
WHERE ArticleID = 1

/*
java.util.List<java.lang.Integer> getItems​(int orderId)
Get all items for order.
Parameters:
orderId - order's id
Returns:
list of item ids for an order
*/
SELECT ID
FROM Item
WHERE OrderID = 1

/*
int completeOrder​(int orderId)
Sends order to the system. Order will be immediately sent.
Parameters:
orderId - oreder id
Returns:
1 if success, -1 if failure
*/


/*
java.math.BigDecimal getFinalPrice​(int orderId)
Gets calculated final price after all discounts.
Parameters:
orderId - order id
Returns:
final price. Sum that buyer have to pay. -1 if failure 
or if order is not completed
*/

/*
java.math.BigDecimal getDiscountSum​(int orderId)
Gets calculated discount for the order
Parameters:
orderId - order id
Returns:
total discount, -1 if failure or if order is not completed
*/
SELECT Status
FROM [Order]
WHERE ID = 1

SELECT COALESCE(SUM(Price * Discount / 100.0), 0)
FROM Item
WHERE OrderId = 1

/*
java.lang.String getState​(int orderId)
Gets state of the order.
Parameters:
orderId - order's id
Returns:
state of the order
*/
SELECT Status
FROM [Order]
WHERE ID = 1

/*
java.util.Calendar getSentTime​(int orderId)
Gets order's sending time
Parameters:
orderId - order's id
Returns:
order's sending time, null if failure
*/
SELECT SentTime
FROM [Order]
WHERE ID = 1

/*
java.util.Calendar getRecievedTime​(int orderId)
Gets time when order arrived to buyer's city.
Parameters:
orderId - order id
Returns:
order's recieve time, null if failure
*/
SELECT ReceivedTime
FROM [Order]
WHERE ID = 1

/*
int getBuyer​(int orderId)
Gets buyer.
Parameters:
orderId - order's id
Returns:
buyer's id
*/
SELECT BuyerID
FROM [Order]
WHERE ID = 1

/*
int getLocation​(int orderId)
Gets location for an order. If order is 
assembled and order is moving from city C1 to city C2 
then location of an order is city C1. If order 
is not yet assembled then location of the order is 
location of the shop that is closest to buyer's city. 
If order is in state "created" then location is -1.
Parameters:
orderId - order's id
Returns:
id of city, -1 if failure
*/
SELECT Status
FROM [Order]
WHERE ID = 1

SELECT CityID
FROM [Order]
WHERE ID = 1