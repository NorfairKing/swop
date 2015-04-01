package be.kuleuven.cs.swop;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.facade.TaskMan;
import be.kuleuven.cs.swop.facade.SessionController;

public class AdvanceTimeSessionTest {
    
    private static TestingUI  ui;
    private static TaskMan taskMan;
    private static SessionController controller;
    
    @Before
    public void setUp() throws Exception {
        ui = new TestingUI();
        taskMan = new TaskMan();
        controller = new SessionController(ui, taskMan);
        ui.start();
    }
    
    @Test
    public void test() {
        LocalDateTime time = LocalDateTime.now();
        ui.setRequestTime(time);
        
        controller.startAdvanceTimeSession();
        
        assertEquals(taskMan.getSystemTime(), time);
    }
    
    @Test
    public void flowTest() {
        // The user indicates he wants to modify the system time
        // The system allows the user to choose a new time
        ui.setRequestTime(LocalDateTime.now());
        
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
        ui.setRequestTime(null);
        
        LocalDateTime time = ui.getTimeStamp();

        // if the user cancels.
        //if (time == null) return; let's assume this check isn't done in the session

        // the system updates the system time.
        taskMan.updateSystemTime(time);
        
        assertEquals(taskMan.getSystemTime(), time);
    }
}
