// 
// Decompiled by Procyon v0.5.36
// 

package rs.etf.sab.student.decompiled;

import org.junit.Test;
import java.util.Calendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;
import rs.etf.sab.operations.GeneralOperations;

public class GeneralOperationsTest
{
    private TestHandler testHandler;
    private GeneralOperations generalOperations;
    
    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull((Object)(this.testHandler = TestHandler.getInstance()));
        Assert.assertNotNull((Object)(this.generalOperations = this.testHandler.getGeneralOperations()));
        this.generalOperations.eraseAll();
    }
    
    @After
    public void tearDown() throws Exception {
        this.generalOperations.eraseAll();
    }
    
    @Test
    public void general() {
        final Calendar time = Calendar.getInstance();
        time.clear();
        time.set(2018, 0, 1);
        this.generalOperations.setInitialTime(time);
        Calendar currentTime = this.generalOperations.getCurrentTime();
        Assert.assertEquals((Object)time, (Object)currentTime);
        this.generalOperations.time(40);
        currentTime = this.generalOperations.getCurrentTime();
        final Calendar newTime = Calendar.getInstance();
        newTime.clear();
        newTime.set(2018, 1, 10);
        Assert.assertEquals((Object)currentTime, (Object)newTime);
    }
}
