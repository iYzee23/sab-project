// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.student.decompiled;

import static java.lang.invoke.StringConcatFactory.makeConcatWithConstants;
import org.junit.runner.Result;
import org.junit.runner.Request;
import org.junit.runner.JUnitCore;

public final class TestRunner
{
    private static final int MAX_POINTS_ON_PUBLIC_TEST = 10;
    private static final Class[] UNIT_TEST_CLASSES;
    private static final Class[] UNIT_TEST_CLASSES_PRIVATE;
    private static final Class[] MODULE_TEST_CLASSES;
    private static final Class[] MODULE_TEST_CLASSES_PRIVATE;
    
    private static double runUnitTestsPublic() {
        int numberOfSuccessfulCases = 0;
        int numberOfAllCases = 0;
        double points = 0.0;
        final JUnitCore jUnitCore = new JUnitCore();
        for (final Class testClass : TestRunner.UNIT_TEST_CLASSES) {
            System.out.println(testClass.getName());
            final Request request = Request.aClass(testClass);
            final Result result = jUnitCore.run(request);
            numberOfAllCases = result.getRunCount();
            numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
            if (numberOfSuccessfulCases < 0) {
                numberOfSuccessfulCases = 0;
            }
           
            System.out.println(numberOfSuccessfulCases);
            System.out.println(numberOfAllCases);
            final double points_curr = numberOfSuccessfulCases * 6.0 / numberOfAllCases / TestRunner.UNIT_TEST_CLASSES.length;
            System.out.println(points_curr);
            points += points_curr;
        }
        return points;
    }
    
    private static double runModuleTestsPublic() {
        int numberOfSuccessfulCases = 0;
        int numberOfAllCases = 0;
        double points = 0.0;
        final JUnitCore jUnitCore = new JUnitCore();
        for (final Class testClass : TestRunner.MODULE_TEST_CLASSES) {
            System.out.println(testClass.getName());
            final Request request = Request.aClass(testClass);
            final Result result = jUnitCore.run(request);
            numberOfAllCases = result.getRunCount();
            numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
            if (numberOfSuccessfulCases < 0) {
                numberOfSuccessfulCases = 0;
            }
            System.out.println(numberOfSuccessfulCases);
            System.out.println( numberOfAllCases);
            final double points_curr = numberOfSuccessfulCases / TestRunner.MODULE_TEST_CLASSES.length * 4;
            System.out.println(points_curr);
            points += points_curr;
        }
        return points;
    }
    
    private static double runPublic() {
        double res = 0.0;
        res += runUnitTestsPublic();
        res += runModuleTestsPublic();
        return res;
    }
    
    private static double runUnitTestsPrivate() {
        int numberOfSuccessfulCases = 0;
        int numberOfAllCases = 0;
        double points = 0.0;
        final JUnitCore jUnitCore = new JUnitCore();
        for (final Class testClass : TestRunner.UNIT_TEST_CLASSES_PRIVATE) {
            System.out.println(testClass.getName());
            final Request request = Request.aClass(testClass);
            final Result result = jUnitCore.run(request);
            numberOfAllCases = result.getRunCount();
            numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
            if (numberOfSuccessfulCases < 0) {
                numberOfSuccessfulCases = 0;
            }
            System.out.println(numberOfSuccessfulCases);
            System.out.println(numberOfAllCases);
            final double points_curr = numberOfSuccessfulCases * 2.0 / numberOfAllCases / TestRunner.UNIT_TEST_CLASSES_PRIVATE.length;
            System.out.println(points_curr);
            points += points_curr;
        }
        return points;
    }
    
    private static double runModuleTestsPrivate() {
        int numberOfSuccessfulCases = 0;
        int numberOfAllCases = 0;
        double points = 0.0;
        final JUnitCore jUnitCore = new JUnitCore();
        for (final Class testClass : TestRunner.MODULE_TEST_CLASSES_PRIVATE) {
            System.out.println(testClass.getName());
            final Request request = Request.aClass(testClass);
            final Result result = jUnitCore.run(request);
            numberOfAllCases = result.getRunCount();
            numberOfSuccessfulCases = result.getRunCount() - result.getFailureCount();
            if (numberOfSuccessfulCases < 0) {
                numberOfSuccessfulCases = 0;
            }
            System.out.println(numberOfSuccessfulCases);
            System.out.println(numberOfAllCases);
            final double points_curr = numberOfSuccessfulCases * 8.0 / numberOfAllCases / TestRunner.MODULE_TEST_CLASSES_PRIVATE.length;
            System.out.println(points_curr);
            points += points_curr;
        }
        return points;
    }
    
    private static double runPrivate() {
        double res = 0.0;
        res += runUnitTestsPrivate();
        res += runModuleTestsPrivate();
        return res;
    }
    
    public static void runTests() {
        final double resultsPublic = runPublic();
        System.out.println(resultsPublic);
    }
    
    static {
        UNIT_TEST_CLASSES = new Class[] { BuyerOperationsTest.class, CityOperationsTest.class, GeneralOperationsTest.class, ShopOperationsTest.class };
        UNIT_TEST_CLASSES_PRIVATE = new Class[0];
        MODULE_TEST_CLASSES = new Class[] { PublicModuleTest.class };
        MODULE_TEST_CLASSES_PRIVATE = new Class[0];
    }
}
