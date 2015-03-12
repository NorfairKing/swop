package be.kuleuven.cs.swop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.data.TaskStatusData;
import be.kuleuven.cs.swop.domain.Timekeeper;


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
        
        project = facade.createProject(new ProjectData("Title", "Descr", new Date(Timekeeper.getTime().getTime() + 1)));
        task = facade.createTaskFor(project, new TaskData("TD", 500, .1));
    }
    
    @Test
    public void test() {
        Date curTime = Timekeeper.getTime();
        Date finishDate = new Date(curTime.getTime() + 1000);
        TaskStatusData data = new TaskStatusData(curTime, finishDate, true);
        
        ui.setRequestTask(task);
        ui.setRequestTaskStatusData(data);
        
        controller.startUpdateTaskStatusSession();

        assertTrue(task.getTask().isFinished());
        assertFalse(task.getTask().isFailed());
        assertEquals(task.getEstimatedOrRealFinishDate(), finishDate);
    }
    
    @Test
    public void flowTest() {
        Date curTime = Timekeeper.getTime();
        Date finishDate = new Date(curTime.getTime() + 1000);
        TaskStatusData data = new TaskStatusData(curTime, finishDate, true);
            
    
        facade.updateTaskStatusFor(task, data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestNull() {
        facade.updateTaskStatusFor(task, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestInvalidDates() {
        Date curTime = Timekeeper.getTime();
        Date finishDate = new Date(curTime.getTime() - 1000); //should be after the start-time.
        TaskStatusData data = new TaskStatusData(curTime, finishDate, true);
        
        facade.updateTaskStatusFor(task, data);
    }
}
