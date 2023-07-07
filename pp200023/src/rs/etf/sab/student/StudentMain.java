package rs.etf.sab.student;

import rs.etf.sab.operations.*;
import org.junit.Test;
import rs.etf.sab.student.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.tests.*;

public class StudentMain {

    public static void main(String[] args) {

        ArticleOperations articleOperations = new pp200023_ArticleOperations();
        BuyerOperations buyerOperations = new pp200023_BuyerOperations();
        CityOperations cityOperations = new pp200023_CityOperations();
        GeneralOperations generalOperations = new pp200023_GeneralOperations();
        OrderOperations orderOperations = new pp200023_OrderOperations();
        ShopOperations shopOperations = new pp200023_ShopOperations();
        TransactionOperations transactionOperations = new pp200023_TransactionOperations();

        TestHandler.createInstance(
                articleOperations,
                buyerOperations,
                cityOperations,
                generalOperations,
                orderOperations,
                shopOperations,
                transactionOperations
        );
        
        TestRunner.runTests();
    }
}
