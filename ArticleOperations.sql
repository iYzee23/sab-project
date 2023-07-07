/* 
int createArticle​(int shopId, java.lang.String articleName, int articlePrice)
Creates new article in shop with count 0.
Parameters:
shopId - shop id
articleName - article name
articlePrice - price of the article
Returns:
id of the article, -1 in failure
*/
INSERT INTO Article (Name, Price) VALUES ('', 5)
INSERT INTO Catalog (Count, ArticleID, ShopID) VALUES (0, 1, 1)