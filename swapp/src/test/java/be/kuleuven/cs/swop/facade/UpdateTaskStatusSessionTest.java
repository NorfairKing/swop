package be.kuleuven.cs.swop.facade;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.user.Developer;


public class UpdateTaskStatusSessionTest extends BaseFacadeTest {

    private ProjectWrapper project;
    private TaskWrapper    task;
    private Developer      dev;

    @Before
    public void setUp() throws Exception {
        simpleSetup();

        dev = taskMan.createDeveloper("John");
        project = taskMan.createProject(new ProjectData("Title", "Descr", taskMan.getSystemTime().plusHours(1)));
        task = taskMan.createTaskFor(project, new TaskData("TD", 60, .1));
    }

    private void loginAsDev() {
        taskMan.requestAuthenticationFor(office, dev);
    }

    @Test
    public void ExecuteAndFinishTest() throws ConflictingPlannedTaskWrapperException {
        ui.addRequestTask(task);
        LocalDateTime time1 = LocalDateTime.of(2015, 1, 1, 8, 0);
        taskMan.updateSystemTime(time1);

        Set<Resource> resourceSet = new HashSet<Resource>(Arrays.asList(dev));
        taskMan.createPlanning(task, time1, resourceSet);

        ui.addExecute(true);
        ui.addSelectNewResources(resourceSet);

        loginAsDev();

        controller.startUpdateTaskStatusSession();

        LocalDateTime time2 = LocalDateTime.of(2015, 1, 1, 9, 0);
        taskMan.updateSystemTime(time2);

        ui.addRequestTask(task);
        ui.addFinish(true);

        controller.startUpdateTaskStatusSession();

        assertTrue(task.isFinished());
        assertFalse(task.isFailed());
        assertTrue(task.getPlanning().getPlannedStartTime().equals(time1));
        assertTrue(task.getPlanning().getPlannedEndTime().equals(time2));
    }

    @Test
    public void failTest() throws ConflictingPlannedTaskWrapperException {
        ui.addRequestTask(task);
        LocalDateTime time = LocalDateTime.of(2015, 1, 1, 8, 0);
        taskMan.updateSystemTime(time);

        Set<Resource> resourceSet = new HashSet<Resource>(Arrays.asList(dev));
        taskMan.createPlanning(task, time, resourceSet);

        ui.addExecute(false);

        controller.startUpdateTaskStatusSession();
        assertFalse(task.isFinished());
        assertTrue(task.isFailed());
        assertTrue(task.getPlanning() == null);
    }

    @Test
    public void executeAndFailTest() throws ConflictingPlannedTaskWrapperException {
        ui.addRequestTask(task);
        LocalDateTime time1 = LocalDateTime.of(2015, 1, 1, 8, 0);
        taskMan.updateSystemTime(time1);

        Set<Resource> resourceSet = new HashSet<Resource>(Arrays.asList(dev));
        taskMan.createPlanning(task, time1, resourceSet);

        loginAsDev();

        ui.addExecute(true);
        ui.addSelectNewResources(resourceSet);

        controller.startUpdateTaskStatusSession();

        LocalDateTime time2 = LocalDateTime.of(2015, 1, 1, 9, 0);
        taskMan.updateSystemTime(time2);

        ui.addRequestTask(task);
        ui.addFinish(false);

        controller.startUpdateTaskStatusSession();

        assertFalse(task.isFinished());
        assertTrue(task.isFailed());
        assertTrue(task.getPlanning().getPlannedStartTime().equals(time1));
        assertTrue(task.getPlanning().getPlannedEndTime().equals(time2));
    }

    @Test
    public void executeConflictTest() throws ConflictingPlannedTaskWrapperException {
        Set<Resource> resourceSet = new HashSet<Resource>(Arrays.asList(dev));

        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("task 2", 60, .1));
        taskMan.createPlanning(task2, LocalDateTime.of(2015, 1, 1, 9, 0), resourceSet);

        ui.addRequestTask(task);
        LocalDateTime time0 = LocalDateTime.of(2015, 1, 1, 8, 0);
        LocalDateTime time1 = LocalDateTime.of(2015, 1, 1, 8, 30);
        LocalDateTime time2 = LocalDateTime.of(2015, 1, 1, 9, 30);
        LocalDateTime time3 = LocalDateTime.of(2015, 1, 1, 10, 30);

        taskMan.createPlanning(task, time0, resourceSet);

        taskMan.updateSystemTime(time1);

        loginAsDev();

        ui.addExecute(true);
        ui.addSelectNewResources(resourceSet);
        ui.addSelectTime(time2);

        controller.startUpdateTaskStatusSession();

        assertFalse(task.isFinished());
        assertFalse(task.isFailed());
        assertTrue(task.isExecuting());
        assertTrue(task.getPlanning().getPlannedStartTime().equals(time1));
        assertTrue(task.getPlanning().getPlannedEndTime().equals(time2));

        assertFalse(task2.isFinished());
        assertFalse(task2.isFailed());
        assertFalse(task2.isExecuting());
        assertTrue(task2.isPlanned());
        assertTrue(task2.getPlanning().getPlannedStartTime().equals(time2));
        assertTrue(task2.getPlanning().getPlannedEndTime().equals(time3));
    }

    @Test
    public void finishConflictTest() throws ConflictingPlannedTaskWrapperException {
        Set<Resource> resourceSet = new HashSet<Resource>(Arrays.asList(dev));

        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("task 2", 60, .1));
        taskMan.createPlanning(task2, LocalDateTime.of(2015, 1, 1, 10, 0), resourceSet);

        ui.addRequestTask(task);
        LocalDateTime time1 = LocalDateTime.of(2015, 1, 1, 9, 0);
        taskMan.updateSystemTime(time1);

        taskMan.createPlanning(task, time1, resourceSet);

        LocalDateTime time2 = LocalDateTime.of(2015, 1, 1, 10, 30);
        LocalDateTime time3 = LocalDateTime.of(2015, 1, 1, 11, 30);

        loginAsDev();

        // task 1 planned 9->10, task 2 planned 10->11

        // current time 9:00 -> start executing task 1
        ui.addExecute(true);
        ui.addSelectNewResources(resourceSet);

        controller.startUpdateTaskStatusSession();

        // current time 10:30 -> finish task 1
        taskMan.updateSystemTime(time2);
        ui.addFinish(true);

        // CONFLICT! move task 2 to 10:30
        ui.addSelectTime(time2);

        controller.startUpdateTaskStatusSession();

        assertTrue(task.isFinished());
        assertFalse(task.isFailed());
        assertTrue(task.getPlanning().getPlannedStartTime().equals(time1));
        assertTrue(task.getPlanning().getPlannedEndTime().equals(time2));

        assertFalse(task2.isFinished());
        assertFalse(task2.isFailed());
        assertFalse(task2.isExecuting());
        assertTrue(task2.isPlanned());
        assertTrue(task2.getPlanning().getPlannedStartTime().equals(time2));
        assertTrue(task2.getPlanning().getPlannedEndTime().equals(time3));
    }

    @Test
    public void failConflictTest() throws ConflictingPlannedTaskWrapperException {
        Set<Resource> resourceSet = new HashSet<Resource>(Arrays.asList(dev));

        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("TD", 60, .1));
        taskMan.createPlanning(task2, LocalDateTime.of(2015, 1, 1, 10, 0), resourceSet);

        ui.addRequestTask(task);
        LocalDateTime time1 = LocalDateTime.of(2015, 1, 1, 9, 0);
        taskMan.updateSystemTime(time1);

        taskMan.createPlanning(task, time1, resourceSet);

        LocalDateTime time2 = LocalDateTime.of(2015, 1, 1, 10, 30);
        LocalDateTime time3 = LocalDateTime.of(2015, 1, 1, 11, 30);

        loginAsDev();

        // task 1 planned 9->10, task 2 planned 10->11

        // current time 9:00 -> start executing task 1
        ui.addExecute(true);
        ui.addSelectNewResources(resourceSet);

        controller.startUpdateTaskStatusSession();

        // current time 10:30 -> fail task 1
        taskMan.updateSystemTime(time2);
        ui.addFinish(false);

        // CONFLICT! move task 2 to 10:30
        ui.addSelectTime(time2);

        controller.startUpdateTaskStatusSession();

        assertFalse(task.isFinished());
        assertTrue(task.isFailed());
        assertTrue(task.getPlanning().getPlannedStartTime().equals(time1));
        assertTrue(task.getPlanning().getPlannedEndTime().equals(time2));

        assertFalse(task2.isFinished());
        assertFalse(task2.isFailed());
        assertFalse(task2.isExecuting());
        assertTrue(task2.isPlanned());
        assertTrue(task2.getPlanning().getPlannedStartTime().equals(time2));
        assertTrue(task2.getPlanning().getPlannedEndTime().equals(time3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void flowExecuteNullSetTest() throws IllegalArgumentException, ConflictingPlannedTaskWrapperException {
        taskMan.executeTask(task, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void flowExecuteNullTaskTest() throws IllegalArgumentException, ConflictingPlannedTaskWrapperException {
        taskMan.executeTask(null, new HashSet<Resource>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void flowCompleteNullTaskTest() throws IllegalArgumentException, ConflictingPlannedTaskWrapperException {
        taskMan.completeTask(null, true);
    }
}
