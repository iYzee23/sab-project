// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.student.decompiled;

import java.util.List;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import rs.etf.sab.operations.BuyerOperations;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.GeneralOperations;

public class BuyerOperationsTest
{
    private TestHandler testHandler;
    private GeneralOperations generalOperations;
    private CityOperations cityOperations;
    private BuyerOperations buyerOperations;
    
    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull((Object)(this.testHandler = TestHandler.getInstance()));
        Assert.assertNotNull((Object)(this.cityOperations = this.testHandler.getCityOperations()));
        Assert.assertNotNull((Object)(this.generalOperations = this.testHandler.getGeneralOperations()));
        Assert.assertNotNull((Object)(this.buyerOperations = this.testHandler.getBuyerOperations()));
        this.generalOperations.eraseAll();
    }
    
    @After
    public void tearDown() throws Exception {
        this.generalOperations.eraseAll();
    }
    
    @Test
    public void insertBuyer() {
        final int cityId = this.cityOperations.createCity("Kragujevac");
        Assert.assertNotEquals(-1L, (long)cityId);
        final int buyerId = this.buyerOperations.createBuyer("Pera", cityId);
        Assert.assertNotEquals(-1L, (long)buyerId);
    }
    
    @Test
    public void changeCity() {
        final int cityId1 = this.cityOperations.createCity("Kragujevac");
        final int cityId2 = this.cityOperations.createCity("Beograd");
        final int buyerId = this.buyerOperations.createBuyer("Lazar", cityId1);
        this.buyerOperations.setCity(buyerId, cityId2);
        final int cityId3 = this.buyerOperations.getCity(buyerId);
        Assert.assertEquals((long)cityId2, (long)cityId3);
    }
    
    @Test
    public void credit() {
        final int cityId = this.cityOperations.createCity("Kragujevac");
        final int buyerId = this.buyerOperations.createBuyer("Pera", cityId);
        final BigDecimal credit1 = new BigDecimal("1000.000").setScale(3);
        BigDecimal creditReturned = this.buyerOperations.increaseCredit(buyerId, credit1);
        Assert.assertEquals((Object)credit1, (Object)creditReturned);
        final BigDecimal credit2 = new BigDecimal("500");
        this.buyerOperations.increaseCredit(buyerId, credit2).setScale(3);
        creditReturned = this.buyerOperations.getCredit(buyerId);
        Assert.assertEquals((Object)credit1.add(credit2).setScale(3), (Object)creditReturned);
    }
    
    @Test
    public void orders() {
        final int cityId = this.cityOperations.createCity("Kragujevac");
        final int buyerId = this.buyerOperations.createBuyer("Pera", cityId);
        final int orderId1 = this.buyerOperations.createOrder(buyerId);
        final int orderId2 = this.buyerOperations.createOrder(buyerId);
        Assert.assertNotEquals(-1L, (long)orderId1);
        Assert.assertNotEquals(-1L, (long)orderId2);
        final List<Integer> orders = (List<Integer>)this.buyerOperations.getOrders(buyerId);
        Assert.assertEquals(2L, (long)orders.size());
        Assert.assertTrue(orders.contains(orderId1) && orders.contains(orderId2));
    }
}
