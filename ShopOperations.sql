/*
int createShop​(java.lang.String name, java.lang.String cityName)
Creates new shop with 0% discount. Shops must have unique name.
Parameters:
name - name of the shop
cityName - name of the city
Returns:
id of the shop, or -1 in failure
*/
SELECT ID
FROM City
WHERE Name = ''

INSERT INTO Member (CityID) VALUES (1)

INSERT INTO Shop (ID, Name) VALUES (1, '')

/*
int setCity​(int shopId, java.lang.String cityName)
Changes city for shop.
Parameters:
shopId - id of the shop
cityName - name of the city
Returns:
1 on success, -1 on failure
*/
SELECT ID
FROM City
WHERE Name = ''

UPDATE Member
SET CityID = 1
WHERE ID = 1

/*
int getCity​(int shopId)
Gets city's id
Parameters:
shopId - city for shop
Returns:
city's id
*/
SELECT CityID
FROM Member
WHERE ID = 1

/*
int setDiscount​(int shopId, int discountPercentage)
Sets discount for shop.
Parameters:
shopId - id of the shop
discountPercentage - discount in percentege
Returns:
1 on success, -1 on failure
*/
UPDATE Shop
SET Discount = 5
WHERE ID = 1

/*
int increaseArticleCount​(int articleId, int increment)
Increases number of articles in the shop.
Parameters:
articleId - id of the article
increment - number of articles to be stored in shop
Returns:
number of articles after storing, -1 in failure
*/
UPDATE Catalog
SET Count = Count + 5
WHERE ArticleID = 1

SELECT Count
FROM Catalog
WHERE ArticleID = 1

/*
int getArticleCount​(int articleId)
Gets count og articles in shop.
Parameters:
articleId - id of the article
Returns:
number of articles in shop
*/
SELECT Count
FROM Catalog
WHERE ArticleID = 1

/*
java.util.List<java.lang.Integer> getArticles​(int shopId)
Gets all articles.
Parameters:
shopId - shop's id
Returns:
gets all article's ids in shop
*/
SELECT ArticleID
FROM Catalog
WHERE ShopID = 1

/*
int getDiscount​(int shopId)
Get discount for shop.
Parameters:
shopId - shop's id
Returns:
discount percentage
*/
SELECT Discount
FROM Shop
WHERE ID = 1