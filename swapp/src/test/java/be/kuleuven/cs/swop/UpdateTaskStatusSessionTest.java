package be.kuleuven.cs.swop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.Timekeeper;
import be.kuleuven.cs.swop.facade.FacadeController;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.TaskData;
import be.kuleuven.cs.swop.facade.TaskStatusData;
import be.kuleuven.cs.swop.facade.TaskWrapper;


public class UpdateTaskStatusSessionTest {
    private static TestingUI  ui;
    private static FacadeController facade;
    private static SessionController controller;
    
    private ProjectWrapper project;
    private TaskWrapper task;
    
    @Before
    public void setUp() throws Exception {
        ui = new TestingUI();
        facade = new FacadeController();
        controller = new SessionController(ui, facade);
        ui.start();
        
        project = facade.createProject(new ProjectData("Title", "Descr", Timekeeper.getTime().plusHours(1)));
        task = facade.createTaskFor(project, new TaskData("TD", 500, .1));
    }
    
    @Test
    public void test() {
        LocalDateTime curTime = Timekeeper.getTime();
        LocalDateTime finishDate = curTime.plusHours(1);
        TaskStatusData data = new TaskStatusData(curTime, finishDate, true);
        
        ui.setRequestTask(task);
        ui.setRequestTaskStatusData(data);
        
        controller.startUpdateTaskStatusSession();

        assertTrue(task.isFinished());
        assertFalse(task.isFailed());
        assertEquals(task.getEstimatedOrRealFinishDate(), finishDate);
    }
    
    @Test
    public void flowTest() {
        LocalDateTime curTime = Timekeeper.getTime();
        LocalDateTime finishDate = curTime.plusHours(1);
        TaskStatusData data = new TaskStatusData(curTime, finishDate, true);
            
    
        facade.updateTaskStatusFor(task, data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestNull() {
        facade.updateTaskStatusFor(task, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestInvalidDates() {
        LocalDateTime curTime = Timekeeper.getTime();
        LocalDateTime finishDate = curTime.minusHours(1); //should be after the start-time.
        TaskStatusData data = new TaskStatusData(curTime, finishDate, true);
        
        facade.updateTaskStatusFor(task, data);
    }
}
