package be.kuleuven.cs.swop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.facade.TaskMan;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.TaskData;
import be.kuleuven.cs.swop.facade.TaskStatusData;
import be.kuleuven.cs.swop.facade.TaskWrapper;


public class UpdateTaskStatusSessionTest {
    private static TestingUI  ui;
    private static TaskMan taskMan;
    private static SessionController controller;
    
    private ProjectWrapper project;
    private TaskWrapper task;
    
    @Before
    public void setUp() throws Exception {
        ui = new TestingUI();
        taskMan = new TaskMan();
        controller = new SessionController(ui, taskMan);
        ui.start();
        
        project = taskMan.createProject(new ProjectData("Title", "Descr", taskMan.getSystemTime().plusHours(1)));
        task = taskMan.createTaskFor(project, new TaskData("TD", 500, .1));
    }
    
    @Test
    public void test() {
        LocalDateTime curTime = taskMan.getSystemTime();
        LocalDateTime finishDate = curTime.plusHours(1);
        TaskStatusData data = new TaskStatusData(curTime, finishDate, true);
        
        ui.setRequestTask(task);
        ui.setRequestTaskStatusData(data);
        
        controller.startUpdateTaskStatusSession();

        assertTrue(task.isFinished());
        assertFalse(task.isFailed());
        assertEquals(task.getEstimatedOrRealFinishDate(taskMan.getSystemTime()), finishDate);
    }
    
    @Test
    public void flowTest() {
        LocalDateTime curTime = taskMan.getSystemTime();
        LocalDateTime finishDate = curTime.plusHours(1);
        TaskStatusData data = new TaskStatusData(curTime, finishDate, true);
            
    
        taskMan.updateTaskStatusFor(task, data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestNull() {
        taskMan.updateTaskStatusFor(task, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestInvalidDates() {
        LocalDateTime curTime = taskMan.getSystemTime();
        LocalDateTime finishDate = curTime.minusHours(1); //should be after the start-time.
        TaskStatusData data = new TaskStatusData(curTime, finishDate, true);
        
        taskMan.updateTaskStatusFor(task, data);
    }
}
