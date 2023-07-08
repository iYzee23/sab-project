// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.student.decompiled;

import org.junit.Test;
import java.math.BigDecimal;
import java.util.Calendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import rs.etf.sab.operations.TransactionOperations;
import rs.etf.sab.operations.OrderOperations;
import rs.etf.sab.operations.BuyerOperations;
import rs.etf.sab.operations.ArticleOperations;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.ShopOperations;
import rs.etf.sab.operations.GeneralOperations;

public class PublicModuleTest
{
    private TestHandler testHandler;
    private GeneralOperations generalOperations;
    private ShopOperations shopOperations;
    private CityOperations cityOperations;
    private ArticleOperations articleOperations;
    private BuyerOperations buyerOperations;
    private OrderOperations orderOperations;
    private TransactionOperations transactionOperations;
    
    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull((Object)(this.testHandler = TestHandler.getInstance()));
        Assert.assertNotNull((Object)(this.shopOperations = this.testHandler.getShopOperations()));
        Assert.assertNotNull((Object)(this.cityOperations = this.testHandler.getCityOperations()));
        Assert.assertNotNull((Object)(this.articleOperations = this.testHandler.getArticleOperations()));
        Assert.assertNotNull((Object)(this.buyerOperations = this.testHandler.getBuyerOperations()));
        Assert.assertNotNull((Object)(this.orderOperations = this.testHandler.getOrderOperations()));
        Assert.assertNotNull((Object)(this.transactionOperations = this.testHandler.getTransactionOperations()));
        Assert.assertNotNull((Object)(this.generalOperations = this.testHandler.getGeneralOperations()));
        this.generalOperations.eraseAll();
    }
    
    @After
    public void tearDown() throws Exception {
        this.generalOperations.eraseAll();
    }
    
    @Test
    public void test() {
        final Calendar initialTime = Calendar.getInstance();
        initialTime.clear();
        initialTime.set(2018, 0, 1);
        this.generalOperations.setInitialTime(initialTime);
        final Calendar receivedTime = Calendar.getInstance();
        receivedTime.clear();
        receivedTime.set(2018, 0, 22);
        final int cityB = this.cityOperations.createCity("B");
        final int cityC1 = this.cityOperations.createCity("C1");
        final int cityA = this.cityOperations.createCity("A");
        final int cityC2 = this.cityOperations.createCity("C2");
        final int cityC3 = this.cityOperations.createCity("C3");
        final int cityC4 = this.cityOperations.createCity("C4");
        final int cityC5 = this.cityOperations.createCity("C5");
        this.cityOperations.connectCities(cityB, cityC1, 8);
        this.cityOperations.connectCities(cityC1, cityA, 10);
        this.cityOperations.connectCities(cityA, cityC2, 3);
        this.cityOperations.connectCities(cityC2, cityC3, 2);
        this.cityOperations.connectCities(cityC3, cityC4, 1);
        this.cityOperations.connectCities(cityC4, cityA, 3);
        this.cityOperations.connectCities(cityA, cityC5, 15);
        this.cityOperations.connectCities(cityC5, cityB, 2);
        final int shopA = this.shopOperations.createShop("shopA", "A");
        final int shopC2 = this.shopOperations.createShop("shopC2", "C2");
        final int shopC3 = this.shopOperations.createShop("shopC3", "C3");
        this.shopOperations.setDiscount(shopA, 20);
        this.shopOperations.setDiscount(shopC2, 50);
        final int laptop = this.articleOperations.createArticle(shopA, "laptop", 1000);
        final int monitor = this.articleOperations.createArticle(shopC2, "monitor", 200);
        final int stolica = this.articleOperations.createArticle(shopC3, "stolica", 100);
        final int sto = this.articleOperations.createArticle(shopC3, "sto", 200);
        this.shopOperations.increaseArticleCount(laptop, 10);
        this.shopOperations.increaseArticleCount(monitor, 10);
        this.shopOperations.increaseArticleCount(stolica, 10);
        this.shopOperations.increaseArticleCount(sto, 10);
        final int buyer = this.buyerOperations.createBuyer("kupac", cityB);
        this.buyerOperations.increaseCredit(buyer, new BigDecimal("20000"));
        final int order = this.buyerOperations.createOrder(buyer);
        this.orderOperations.addArticle(order, laptop, 5);
        this.orderOperations.addArticle(order, monitor, 4);
        this.orderOperations.addArticle(order, stolica, 10);
        this.orderOperations.addArticle(order, sto, 4);
        Assert.assertNull((Object)this.orderOperations.getSentTime(order));
        Assert.assertTrue("created".equals(this.orderOperations.getState(order)));
        this.orderOperations.completeOrder(order);
        Assert.assertTrue("sent".equals(this.orderOperations.getState(order)));
        final int buyerTransactionId = this.transactionOperations.getTransationsForBuyer(buyer).get(0);
        Assert.assertEquals((Object)initialTime, (Object)this.transactionOperations.getTimeOfExecution(buyerTransactionId));
        Assert.assertNull((Object)this.transactionOperations.getTransationsForShop(shopA));
        final BigDecimal shopAAmount = new BigDecimal("5").multiply(new BigDecimal("1000")).setScale(3);
        final BigDecimal shopAAmountWithDiscount = new BigDecimal("0.8").multiply(shopAAmount).setScale(3);
        final BigDecimal shopC2Amount = new BigDecimal("4").multiply(new BigDecimal("200")).setScale(3);
        final BigDecimal shopC2AmountWithDiscount = new BigDecimal("0.5").multiply(shopC2Amount).setScale(3);
        final BigDecimal shopC3AmountWithDiscount;
        final BigDecimal shopC3Amount = shopC3AmountWithDiscount = new BigDecimal("10").multiply(new BigDecimal("100")).add(new BigDecimal("4").multiply(new BigDecimal("200"))).setScale(3);
        final BigDecimal amountWithoutDiscounts = shopAAmount.add(shopC2Amount).add(shopC3Amount).setScale(3);
        final BigDecimal amountWithDiscounts = shopAAmountWithDiscount.add(shopC2AmountWithDiscount).add(shopC3AmountWithDiscount).setScale(3);
        final BigDecimal systemProfit = amountWithDiscounts.multiply(new BigDecimal("0.05")).setScale(3);
        final BigDecimal shopAAmountReal = shopAAmountWithDiscount.multiply(new BigDecimal("0.95")).setScale(3);
        final BigDecimal shopC2AmountReal = shopC2AmountWithDiscount.multiply(new BigDecimal("0.95")).setScale(3);
        final BigDecimal shopC3AmountReal = shopC3AmountWithDiscount.multiply(new BigDecimal("0.95")).setScale(3);
        Assert.assertEquals((Object)amountWithDiscounts, (Object)this.orderOperations.getFinalPrice(order));
        Assert.assertEquals((Object)amountWithoutDiscounts.subtract(amountWithDiscounts), (Object)this.orderOperations.getDiscountSum(order));
        Assert.assertEquals((Object)amountWithDiscounts, (Object)this.transactionOperations.getBuyerTransactionsAmmount(buyer));
        Assert.assertEquals((Object)this.transactionOperations.getShopTransactionsAmmount(shopA), (Object)new BigDecimal("0").setScale(3));
        Assert.assertEquals((Object)this.transactionOperations.getShopTransactionsAmmount(shopC2), (Object)new BigDecimal("0").setScale(3));
        Assert.assertEquals((Object)this.transactionOperations.getShopTransactionsAmmount(shopC3), (Object)new BigDecimal("0").setScale(3));
        Assert.assertEquals((Object)new BigDecimal("0").setScale(3), (Object)this.transactionOperations.getSystemProfit());
        this.generalOperations.time(2);
        Assert.assertEquals((Object)initialTime, (Object)this.orderOperations.getSentTime(order));
        Assert.assertNull((Object)this.orderOperations.getRecievedTime(order));
        Assert.assertEquals((long)this.orderOperations.getLocation(order), (long)cityA);
        this.generalOperations.time(9);
        Assert.assertEquals((long)this.orderOperations.getLocation(order), (long)cityA);
        this.generalOperations.time(8);
        Assert.assertEquals((long)this.orderOperations.getLocation(order), (long)cityC5);
        this.generalOperations.time(5);
        Assert.assertEquals((long)this.orderOperations.getLocation(order), (long)cityB);
        Assert.assertEquals((Object)receivedTime, (Object)this.orderOperations.getRecievedTime(order));
        Assert.assertEquals((Object)shopAAmountReal, (Object)this.transactionOperations.getShopTransactionsAmmount(shopA));
        Assert.assertEquals((Object)shopC2AmountReal, (Object)this.transactionOperations.getShopTransactionsAmmount(shopC2));
        Assert.assertEquals((Object)shopC3AmountReal, (Object)this.transactionOperations.getShopTransactionsAmmount(shopC3));
        Assert.assertEquals((Object)systemProfit, (Object)this.transactionOperations.getSystemProfit());
        final int shopATransactionId = this.transactionOperations.getTransactionForShopAndOrder(order, shopA);
        Assert.assertNotEquals(-1L, (long)shopATransactionId);
        Assert.assertEquals((Object)receivedTime, (Object)this.transactionOperations.getTimeOfExecution(shopATransactionId));
    }
}
