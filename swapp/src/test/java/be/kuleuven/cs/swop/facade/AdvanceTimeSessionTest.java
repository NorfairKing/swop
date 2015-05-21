package be.kuleuven.cs.swop.facade;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

public class AdvanceTimeSessionTest extends BaseFacadeTest {
    
    @Before
    public void setUp() throws Exception {
        simpleSetup();
    }
    
    @Test
    public void test() {
        LocalDateTime time = LocalDateTime.now();
        ui.addRequestTime(time);
        
        controller.startAdvanceTimeSession();
        
        assertEquals(taskMan.getSystemTime(), time);
    }
    
    @Test
    public void flowTest() {
        // The user indicates he wants to modify the system time
        // The system allows the user to choose a new time
        ui.addRequestTime(LocalDateTime.now());
        
        LocalDateTime time = ui.getTimeStamp();

        // if the user cancels.
        if (time == null) return;

        // the system updates the system time.
        taskMan.updateSystemTime(time);
        
        assertEquals(taskMan.getSystemTime(), time);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestNull() {
        // The user indicates he wants to modify the system time
        // The system allows the user to choose a new time
        ui.addRequestTime(null);
        
        LocalDateTime time = ui.getTimeStamp();

        // if the user cancels.
        //if (time == null) return; let's assume this check isn't done in the session

        // the system updates the system time.
        taskMan.updateSystemTime(time);
        
        assertEquals(taskMan.getSystemTime(), time);
    }
}
