// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.student.decompiled;

//import javax.validation.constraints.NotNull;
import rs.etf.sab.operations.TransactionOperations;
import rs.etf.sab.operations.ShopOperations;
import rs.etf.sab.operations.OrderOperations;
import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.operations.BuyerOperations;
import rs.etf.sab.operations.ArticleOperations;

public class TestHandler
{
    private static TestHandler testHandler;
    private ArticleOperations articleOperations;
    private BuyerOperations buyerOperations;
    private CityOperations cityOperations;
    private GeneralOperations generalOperations;
    private OrderOperations orderOperations;
    private ShopOperations shopOperations;
    private TransactionOperations transactionOperations;
    
    public TestHandler( final ArticleOperations articleOperations,  final BuyerOperations buyerOperations,  final CityOperations cityOperations, final GeneralOperations generalOperations, final OrderOperations orderOperations, final ShopOperations shopOperations, final TransactionOperations transactionOperations) {
        this.articleOperations = articleOperations;
        this.buyerOperations = buyerOperations;
        this.cityOperations = cityOperations;
        this.generalOperations = generalOperations;
        this.orderOperations = orderOperations;
        this.shopOperations = shopOperations;
        this.transactionOperations = transactionOperations;
    }
    
    public static void createInstance(final ArticleOperations articleOperations, final BuyerOperations buyerOperations, final CityOperations cityOperations, final GeneralOperations generalOperations, final OrderOperations orderOperations, final ShopOperations shopOperations, final TransactionOperations transactionOperations) {
        TestHandler.testHandler = new TestHandler(articleOperations, buyerOperations, cityOperations, generalOperations, orderOperations, shopOperations, transactionOperations);
    }
    
    static TestHandler getInstance() {
        return TestHandler.testHandler;
    }
    
    public ArticleOperations getArticleOperations() {
        return this.articleOperations;
    }
    
    public BuyerOperations getBuyerOperations() {
        return this.buyerOperations;
    }
    
    public CityOperations getCityOperations() {
        return this.cityOperations;
    }
    
    public GeneralOperations getGeneralOperations() {
        return this.generalOperations;
    }
    
    public OrderOperations getOrderOperations() {
        return this.orderOperations;
    }
    
    public ShopOperations getShopOperations() {
        return this.shopOperations;
    }
    
    public TransactionOperations getTransactionOperations() {
        return this.transactionOperations;
    }
    
    static {
        TestHandler.testHandler = null;
    }
}
