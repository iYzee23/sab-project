// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.student.decompiled;

import java.util.List;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import rs.etf.sab.operations.ArticleOperations;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.ShopOperations;
import rs.etf.sab.operations.GeneralOperations;

public class ShopOperationsTest
{
    private TestHandler testHandler;
    private GeneralOperations generalOperations;
    private ShopOperations shopOperations;
    private CityOperations cityOperations;
    private ArticleOperations articleOperations;
    
    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull((Object)(this.testHandler = TestHandler.getInstance()));
        Assert.assertNotNull((Object)(this.shopOperations = this.testHandler.getShopOperations()));
        Assert.assertNotNull((Object)(this.cityOperations = this.testHandler.getCityOperations()));
        Assert.assertNotNull((Object)(this.articleOperations = this.testHandler.getArticleOperations()));
        Assert.assertNotNull((Object)(this.generalOperations = this.testHandler.getGeneralOperations()));
        this.generalOperations.eraseAll();
    }
    
    @After
    public void tearDown() throws Exception {
        this.generalOperations.eraseAll();
    }
    
    @Test
    public void createShop() {
        final int cityId = this.cityOperations.createCity("Kragujevac");
        Assert.assertNotEquals(-1L, (long)cityId);
        final int shopId = this.shopOperations.createShop("Gigatron", "Kragujevac");
        Assert.assertEquals((long)shopId, (long)this.cityOperations.getShops(cityId).get(0));
    }
    
    @Test
    public void setCity() {
        this.cityOperations.createCity("Kragujevac");
        final int shopId = this.shopOperations.createShop("Gigatron", "Kragujevac");
        final int cityId2 = this.cityOperations.createCity("Subotica");
        this.shopOperations.setCity(shopId, "Subotica");
        Assert.assertEquals((long)shopId, (long)this.cityOperations.getShops(cityId2).get(0));
    }
    
    @Test
    public void discount() {
        this.cityOperations.createCity("Kragujevac");
        final int shopId = this.shopOperations.createShop("Gigatron", "Kragujevac");
        this.shopOperations.setDiscount(shopId, 20);
        Assert.assertEquals(20L, (long)this.shopOperations.getDiscount(shopId));
    }
    
    @Test
    public void articles() {
        this.cityOperations.createCity("Kragujevac");
        final int shopId = this.shopOperations.createShop("Gigatron", "Kragujevac");
        final int articleId = this.articleOperations.createArticle(shopId, "Olovka", 10);
        Assert.assertNotEquals(-1L, (long)articleId);
        final int articleId2 = this.articleOperations.createArticle(shopId, "Gumica", 5);
        Assert.assertNotEquals(-1L, (long)articleId2);
        this.shopOperations.increaseArticleCount(articleId, 5);
        this.shopOperations.increaseArticleCount(articleId, 2);
        final int articleCount = this.shopOperations.getArticleCount(articleId);
        Assert.assertEquals(7L, (long)articleCount);
        final List<Integer> articles = this.shopOperations.getArticles(shopId);
        Assert.assertEquals(2L, (long)articles.size());
        Assert.assertTrue(articles.contains(articleId) && articles.contains(articleId2));
    }
}
