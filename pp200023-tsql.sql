/* 
Isplate prodavnicama za pristiglu porudžbinu 
uraditi okidačem:
TR_TRANSFER_MONEY_TO_SHOPS
*/
CREATE TRIGGER TR_TRANSFER_MONEY_TO_SHOPS
	ON [Order]
	AFTER Update
	AS
BEGIN
	DECLARE @kursor cursor
	DECLARE @orderId int
	DECLARE @buyerTransaction int
	DECLARE @receivedTime timestamp
	
	DECLARE @shopKursor cursor
	DECLARE @shopId int
	DECLARE @price decimal(10,3)

	SET @kursor = cursor FOR
	SELECT I.ID, I.ReceivedTime
	FROM Inserted I, Deleted D
	WHERE I.ID = D.ID and I.Status = 'arrived' and D.Status <> 'arrived'

	OPEN @kursor
	FETCH NEXT FROM @kursor
	INTO @orderId, @receivedTime

	WHILE @@FETCH_STATUS = 0
	BEGIN
		SELECT @buyerTransaction = T.ID
		FROM [Transaction] T join TransactionBuyer TB on T.ID = TB.ID
		WHERE T.OrderID = @orderId

		UPDATE [Transaction]
		SET ExecutionTime = @receivedTime
		WHERE ID = @buyerTransaction

		SET @shopKursor = cursor FOR
		SELECT S.ID, COALESCE(SUM(Price * (100 - I.Discount) / 100.0), 0)
		FROM Item I join Catalog C on I.ArticleID = C.ArticleID join Shop S on C.ShopID = S.ID
		WHERE OrderID = @orderId
		GROUP BY S.ID

		OPEN @shopKursor
		FETCH NEXT FROM @shopKursor
		INTO @shopId, @price

		WHILE @@FETCH_STATUS = 0
		BEGIN
			INSERT INTO [Transaction] (Amount, ExecutionTime, OrderID)
			VALUES (@price, @receivedTime, @orderId)

			INSERT INTO TransactionShop (ID, ShopID)
			VALUES (@@IDENTITY, @shopId)

			UPDATE Shop
			SET Balance = Balance + @price
			WHERE ID = @shopId

			FETCH NEXT FROM @shopKursor
			INTO @shopId, @price
		END

		CLOSE @shopKursor
		DEALLOCATE @shopKursor
		
		FETCH NEXT FROM @kursor
		INTO @orderId, @receivedTime
	END

	CLOSE @kursor
	DEALLOCATE @kursor
END

/*
Računanje krajnje cene porudžbine (sa popustima) 
uraditi procedurom:
SP_FINAL_PRICE
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
CREATE PROCEDURE SP_FINAL_PRICE
	@orderId int,
	@finalPrice decimal(10,3) OUTPUT
	AS
BEGIN
	DECLARE @price decimal(10,3)
	DECLARE @status varchar(100)

	SELECT @status = Status
	FROM [Order]
	WHERE ID = @orderId

	IF @status = 'created'
	BEGIN
		SET @finalPrice = -1
		RETURN
	END

	SELECT @price = Price
	FROM [Order]
	WHERE ID = @orderId

	SET @finalPrice = COALESCE(@price, -1)
END