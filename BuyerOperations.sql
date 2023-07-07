/*
int createBuyer​(java.lang.String name, int cityId)
Creates new buyer with 0 credit.
Parameters:
name - name of the buyer
cityId - id of the city
Returns:
buyer's id, or -1 if failure
*/
INSERT INTO Member (CityID) VALUES (1)

INSERT INTO Buyer (ID, Name, Credit) VALUES (1, '', 0)

/*
int setCity​(int buyerId, int cityId)
Changes city for buyer.
Parameters:
buyerId - id of the buyer
cityId - id of the city
Returns:
1 if success, -1 if failure
*/
UPDATE Member
SET CityID = 1
WHERE ID = 1

/*
int getCity​(int buyerId)
Gets city for buyer.
Parameters:
buyerId - buyer's id
Returns:
city's id, -1 if failure
*/
SELECT CityID
FROM Member
WHERE ID = 1

/*
java.math.BigDecimal increaseCredit​(int buyerId, java.math.BigDecimal credit)
Increases buyer's credit.
Parameters:
buyerId - id of the buyer
credit - credit
Returns:
credit after addition
*/
UPDATE Buyer
SET Credit = Credit + 5.0
WHERE ID = 1

SELECT Credit
FROM Buyer
WHERE ID = 1

/*
int createOrder​(int buyerId)
Creates empty order.
Parameters:
buyerId - buyer id
Returns:
id of the order, -1 in failure
*/
INSERT INTO [Order] (BuyerID) VALUES (1)

/*
java.util.List<java.lang.Integer> getOrders​(int buyerId)
Gets all orders for buyer
Parameters:
buyerId - buyer id
Returns:
list of order's ids for buyer
*/
SELECT ID
FROM [Order]
WHERE BuyerID = 1

/*
java.math.BigDecimal getCredit​(int buyerId)
Gets credit for buyer.
Parameters:
buyerId - buyer's id
Returns:
credit for buyer
*/
SELECT Credit
FROM Buyer
WHERE ID = 1