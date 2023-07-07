/*
java.math.BigDecimal getBuyerTransactionsAmmount​(int buyerId)
Gets sum of all transactions amounts for buyer
Parameters:
buyerId - buyer's id
Returns:
sum of all transactions, 0 if there are not transactions, 
-1 if failure
*/
SELECT COALESCE(SUM(T.Amount), 0)
FROM [Transaction] T join TransactionBuyer TB on T.ID = TB.ID
WHERE OrderID in (
	SELECT O.ID
	FROM [Order] O
	WHERE O.BuyerID = 1
)

/*
java.math.BigDecimal getShopTransactionsAmmount​(int shopId)
Gets sum of all transactions amounts for shop
Parameters:
shopId - shop's id
Returns:
sum of all transactions, 0 if there are not transactions, 
-1 if failure
*/
SELECT COALESCE(SUM(T.Amount), 0)
FROM [Transaction] T join TransactionShop TS on T.ID = TS.ID
WHERE TS.ShopID = 1

/*
List<Integer> getTransationsForBuyer​(int buyerId)
Gets all transactions for buyer
Parameters:
buyerId - buyer id
Returns:
list of transations ids, null if failure
*/
SELECT T.ID
FROM [Transaction] T join TransactionBuyer TB on T.ID = TB.ID
WHERE OrderID in (
	SELECT O.ID
	FROM [Order] O
	WHERE O.BuyerID = 1
)

/*
int getTransactionForBuyersOrder​(int orderId)
Gets transaction that buyer made for paying an order.
Parameters:
orderId - order's id
Returns:
transaction's id, -1 if failure
*/
SELECT T.ID
FROM [Transaction] T join TransactionBuyer TB on T.ID = TB.ID
WHERE OrderID = 1

/*
int getTransactionForShopAndOrder​(int orderId, int shopId)
Gets transaction for recieved order that system made to shop.
Parameters:
orderId - order's id
shopId - shop's id
Returns:
transaction's id, -1 if failure
*/
SELECT T.ID
FROM [Transaction] T join TransactionShop TS on T.ID = TS.ID
WHERE TS.ShopID = 1 and T.OrderID = 1

/*
List<Integer> getTransationsForShop​(int shopId)
Gets all transactions for shop
Parameters:
shopId - buyer id
Returns:
list of transations ids, null if failure
*/
SELECT ID
FROM TransactionShop
WHERE ShopID = 1

/*
java.util.Calendar getTimeOfExecution​(int transactionId)
get transaction's execution time. Execution time must be equal
to order's recieve time.
Parameters:
transactionId - transaction's id
Returns:
time of execution, null if payment is not done or if failure
*/
SELECT O.ReceivedTime
FROM [Order] O join [Transaction] T on O.ID = T.OrderID
WHERE T.ID = 1

SELECT ExecutionTime
FROM [Transaction]
WHERE ID = 1

/*
BigDecimal getAmmountThatBuyerPayedForOrder​(int orderId)
Gets sum that buyer payed for an order
Parameters:
orderId - order's id
Returns:
ammount buyer payed for an order
*/
SELECT Price
FROM [Order]
WHERE ID = 1

/*
BigDecimal getAmmountThatShopRecievedForOrder​(int shopId, int orderId)
Gets sum that shop recieved for an order
Parameters:
shopId - shop's id
orderId - order's id
Returns:
ammount shop recieved for an order
*/
SELECT T.Amount
FROM [Transaction] T join TransactionShop TS on T.ID = TS.ID
WHERE TS.ShopID = 1 and T.OrderID = 1

/*
java.math.BigDecimal getTransactionAmount​(int transactionId)
Gets transaction's amount.
Parameters:
transactionId - transaction's id
Returns:
ammount that is transferd via transaction
*/
SELECT Amount
FROM [Transaction]
WHERE ID = 1

/*
java.math.BigDecimal getSystemProfit()
Gets system profit. System profit calculation is based only 
on arrived orders.
Returns:
system profit.
*/
SELECT COALESCE(SUM(Price * (5 - Discount) / 100.0), 0)
FROM [Order]
WHERE Status = 'arrived'