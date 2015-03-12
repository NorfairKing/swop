package be.kuleuven.cs.swop;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.FacadeController;
import be.kuleuven.cs.swop.SessionController;
import be.kuleuven.cs.swop.domain.Timekeeper;



public class AdvanceTimeSessionTest {
    
    private static TestingUI  ui;
    private static FacadeController facade;
    private static SessionController controller;
    
    @Before
    public void setUp() throws Exception {
        ui = new TestingUI();
        facade = new FacadeController();
        controller = new SessionController(ui, facade);
        ui.start();
    }
    
    @Test
    public void test() {
        Date time = new Date();
        ui.setRequestTime(time);
        
        controller.startAdvanceTimeSession();
        
        assertEquals(Timekeeper.getTime(), time);
    }
    
    @Test
    public void flowTest() {
        // The user indicates he wants to modify the system time
        // The system allows the user to choose a new time
        ui.setRequestTime(new Date());
        
        Date time = ui.getTimeStamp();

        // if the user cancels.
        if (time == null) return;

        // the system updates the system time.
        facade.updateSystemTime(time);
        
        assertEquals(Timekeeper.getTime(), time);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestNull() {
        // The user indicates he wants to modify the system time
        // The system allows the user to choose a new time
        ui.setRequestTime(null);
        
        Date time = ui.getTimeStamp();

        // if the user cancels.
        //if (time == null) return; let's assume this check isn't done in the session

        // the system updates the system time.
        facade.updateSystemTime(time);
        
        assertEquals(Timekeeper.getTime(), time);
    }
}
