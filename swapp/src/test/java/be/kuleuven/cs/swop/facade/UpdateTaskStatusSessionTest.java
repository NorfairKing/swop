package be.kuleuven.cs.swop.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.facade.ConflictingPlannedTaskWrapperException;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.facade.DeveloperWrapper;
import be.kuleuven.cs.swop.facade.ExecutingStatusData;
import be.kuleuven.cs.swop.facade.FinishedStatusData;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.TaskData;
import be.kuleuven.cs.swop.facade.TaskStatusData;
import be.kuleuven.cs.swop.facade.TaskWrapper;


public class UpdateTaskStatusSessionTest extends BaseFacadeTest {
    
    private ProjectWrapper project;
    private TaskWrapper task;
    
    @Before
    public void setUp() throws Exception {
        simpleSetup();
        
        project = taskMan.createProject(new ProjectData("Title", "Descr", taskMan.getSystemTime().plusHours(1)));
        task = taskMan.createTaskFor(project, new TaskData("TD", 500, .1));
    }
    
    @Test
    public void test() throws ConflictingPlannedTaskWrapperException {
        ui.addRequestTask(task);
        LocalDateTime time = LocalDateTime.of(2015, 1, 1, 8, 0);
        taskMan.updateSystemTime(time);
        
        DeveloperWrapper dev = user.asDeveloper();
        taskMan.createPlanning(task, time, new HashSet<Resource>(Arrays.asList(dev.getDeveloper())));

        TaskStatusData startData = new ExecutingStatusData();
        ui.addRequestTaskStatusData(startData);
        
        controller.startUpdateTaskStatusSession();
        
        LocalDateTime curTime = taskMan.getSystemTime();
        LocalDateTime finishDate = curTime.plusHours(1);
        TaskStatusData finishData = new FinishedStatusData(curTime, finishDate);
        
        ui.addRequestTask(task);
        ui.addRequestTaskStatusData(finishData);
        
        controller.startUpdateTaskStatusSession();

        assertTrue(task.isFinished());
        assertFalse(task.isFailed());
        assertEquals(task.getEstimatedOrRealFinishDate(taskMan.getSystemTime()), finishDate);
    }
    
    @Test
    public void flowTest() throws ConflictingPlannedTaskWrapperException {
        ui.addRequestTask(task);
        LocalDateTime time = LocalDateTime.of(2015, 1, 1, 8, 0);
        taskMan.updateSystemTime(time);
        
        DeveloperWrapper dev = user.asDeveloper();
        taskMan.createPlanning(task, time, new HashSet<Resource>(Arrays.asList(dev.getDeveloper())));

        TaskStatusData startData = new ExecutingStatusData();
        ui.addRequestTaskStatusData(startData);
        
        controller.startUpdateTaskStatusSession();
        
        LocalDateTime curTime = taskMan.getSystemTime();
        LocalDateTime finishDate = curTime.plusHours(1);
        TaskStatusData data = new FinishedStatusData(curTime, finishDate);
        
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
        TaskStatusData data = new FinishedStatusData(curTime, finishDate);
        
        taskMan.updateTaskStatusFor(task, data);
    }
}
