// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.student.decompiled;

import java.util.List;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import rs.etf.sab.operations.ShopOperations;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.GeneralOperations;

public class CityOperationsTest
{
    private TestHandler testHandler;
    private GeneralOperations generalOperations;
    private CityOperations cityOperations;
    private ShopOperations shopOperations;
    
    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull((Object)(this.testHandler = TestHandler.getInstance()));
        Assert.assertNotNull((Object)(this.cityOperations = this.testHandler.getCityOperations()));
        Assert.assertNotNull((Object)(this.generalOperations = this.testHandler.getGeneralOperations()));
        Assert.assertNotNull((Object)(this.shopOperations = this.testHandler.getShopOperations()));
        this.generalOperations.eraseAll();
    }
    
    @After
    public void tearDown() throws Exception {
        this.generalOperations.eraseAll();
    }
    
    @Test
    public void createCity() {
        final int cityVranje = this.cityOperations.createCity("Vranje");
        Assert.assertEquals(1L, (long)this.cityOperations.getCities().size());
        Assert.assertEquals((long)cityVranje, (long)this.cityOperations.getCities().get(0));
    }
    
    @Test
    public void insertShops() {
        final int cityId = this.cityOperations.createCity("Vranje");
        final int shopId1 = this.shopOperations.createShop("Gigatron", "Vranje");
        final int shopId2 = this.shopOperations.createShop("Teranova", "Vranje");
        final List<Integer> shops = (List<Integer>)this.cityOperations.getShops(cityId);
        Assert.assertEquals(2L, (long)shops.size());
        Assert.assertTrue(shops.contains(shopId1) && shops.contains(shopId2));
    }
    
    @Test
    public void connectCities() {
        final int cityVranje = this.cityOperations.createCity("Vranje");
        final int cityLeskovac = this.cityOperations.createCity("Leskovac");
        final int cityNis = this.cityOperations.createCity("Nis");
        Assert.assertNotEquals(-1L, (long)cityLeskovac);
        Assert.assertNotEquals(-1L, (long)cityVranje);
        Assert.assertNotEquals(-1L, (long)cityNis);
        this.cityOperations.connectCities(cityNis, cityVranje, 50);
        this.cityOperations.connectCities(cityVranje, cityLeskovac, 70);
        final List<Integer> connectedCities = (List<Integer>)this.cityOperations.getConnectedCities(cityVranje);
        Assert.assertEquals(2L, (long)connectedCities.size());
        Assert.assertTrue(connectedCities.contains(cityLeskovac) && connectedCities.contains(cityNis));
    }
}
