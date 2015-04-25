package be.kuleuven.cs.swop.facade;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.TestingUI;
import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.user.Manager;
import be.kuleuven.cs.swop.facade.TaskMan;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.TaskData;


public class ResolveConflictsSessionTest {
    private static TestingUI  ui;
    private static TaskMan taskMan;
    private static SessionController controller;
    
    private ProjectWrapper project;
    
    private DeveloperWrapper dev;
    
    private ResourceTypeWrapper type;
    
    private ResourceWrapper res;
    
    @Before
    public void setUp() throws Exception {
        ui = new TestingUI();
        taskMan = new TaskMan();
        controller = new SessionController(ui, taskMan);
        controller.setCurrentUser(new UserWrapper(new Manager("Jake")));
        dev = taskMan.createDeveloper(new DeveloperData("Jane"));
        project = taskMan.createProject(new ProjectData("Title", "Descr", taskMan.getSystemTime().plusHours(1)));
        type = taskMan.createResourceType(new ResourceTypeData("type0",
        		new HashSet<ResourceTypeWrapper>(),
        		new HashSet<ResourceTypeWrapper>(),
        		false,
        		new LocalTime[0]));
        
        res = taskMan.createResource(new ResourceData("res", type));
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 8, 0));
      
        ui.start();
    }
    
    
    @Test
    public void exactlySideBySideTest(){
        Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
        
        Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
        res1.add(res);
        Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
        
        
        // plan first task
        ui.addRequestTask(task1);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        // plan second task
        ui.addRequestTask(task2);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 9, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        
        TaskPlanningWrapper plan1 = taskMan.getPlanningFor(task1);
        assertEquals(plan1.getTask(),task1);
        assertTrue(plan1.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 8, 0), LocalDateTime.of(2016, 1, 1, 9, 0))));
        
        TaskPlanningWrapper plan2 = taskMan.getPlanningFor(task2);
        assertEquals(plan2.getTask(),task2);
        assertTrue(plan2.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 9, 0), LocalDateTime.of(2016, 1, 1, 10, 0))));

    }

    @Test
    public void sameTimeDifferentDayTest(){
        Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("task1",60,0, req1));
        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("task2",60,0, req1));
        
        Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
        res1.add(res);
        Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
        
        
        // plan first task
        ui.addRequestTask(task1);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        // plan second task
        ui.addRequestTask(task2);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 2, 8, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        TaskPlanningWrapper plan1 = taskMan.getPlanningFor(task1);
        assertEquals(plan1.getTask(),task1);
        assertTrue(plan1.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 8, 0), LocalDateTime.of(2016, 1, 1, 9, 0))));
        
        TaskPlanningWrapper plan2 = taskMan.getPlanningFor(task2);
        assertEquals(plan2.getTask(),task2);
        assertTrue(plan2.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 2, 8, 0), LocalDateTime.of(2016, 1, 2, 9, 0))));
    }

    @Test
    public void simpleConflictTest(){
        Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("task1",60,0, req1));
        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("task2",60,0, req1));
        
        Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
        res1.add(res);
        Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
        
        
        // plan first task
        ui.addRequestTask(task1);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        // plan second task
        
        ui.addRequestTask(task2);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
    	// conflict! move first task
    	
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 9, 0));

        // provide info for second task again
        
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        TaskPlanningWrapper plan1 = taskMan.getPlanningFor(task1);
        assertEquals(plan1.getTask(),task1);
        assertTrue(plan1.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 9, 0), LocalDateTime.of(2016, 1, 1, 10, 0))));
        
        TaskPlanningWrapper plan2 = taskMan.getPlanningFor(task2);
        assertEquals(plan2.getTask(),task2);
        assertTrue(plan2.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 8, 0), LocalDateTime.of(2016, 1, 1, 9, 0))));

    }
    
    @Test
    public void multipleConflictTest(){
        Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("task1",60,0, req1));
        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("task2",60,0, req1));
        TaskWrapper task3 = taskMan.createTaskFor(project, new TaskData("task3",60,0, req1));
        
        Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
        res1.add(res);
        Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
        
        
        // plan first task
        ui.addRequestTask(task1);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        // plan second task
        ui.addRequestTask(task2);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 9, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();
        
        
        // plan third task
        ui.addRequestTask(task3);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 30));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
    	// conflict! either first or second task needs to be moved
    	
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 12, 0));
        
        // provide info for third task again

        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 30));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
    	// conflict! the other task needs to be moved
    	
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 13, 0));
        
        // provide info for third task again
        
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 30));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();         
        
        DateTimePeriod firstPeriod = new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0));
        DateTimePeriod secondPeriod = new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 13, 0), LocalDateTime.of(2016, 1, 1, 14, 0));
        
        TaskPlanningWrapper plan1 = taskMan.getPlanningFor(task1);
        assertEquals(plan1.getTask(),task1);
        assertTrue(plan1.getPeriod().equals(firstPeriod) || plan1.getPeriod().equals(secondPeriod));
        
        TaskPlanningWrapper plan2 = taskMan.getPlanningFor(task2);
        assertEquals(plan2.getTask(),task2);
        assertTrue(plan2.getPeriod().equals(firstPeriod) || plan2.getPeriod().equals(secondPeriod));
        
        assertFalse(plan1.getPeriod().equals(plan2.getPeriod()));
        
        TaskPlanningWrapper plan3 = taskMan.getPlanningFor(task3);
        assertEquals(plan3.getTask(),task3);
        assertTrue(plan3.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 8, 30), LocalDateTime.of(2016, 1, 1, 9, 30))));
    	
    }
    
    @Test
    public void simpleNestedConflictTest(){
        Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("task1",60,0, req1));
        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("task2",60,0, req1));
        TaskWrapper task3 = taskMan.createTaskFor(project, new TaskData("task3",60,0, req1));
        
        Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
        res1.add(res);
        Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
        
        
        // plan first task
        ui.addRequestTask(task1);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        // plan second task
        ui.addRequestTask(task2);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 10, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();
        
        
        // plan third task
        ui.addRequestTask(task3);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 30));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
    	// conflict! first task needs to be moved
    	
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 10, 30));
        
        // can't move first task due to conflict! second task needs to be moved!

        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 11, 30));
    	
    	// provide info for first task again
    	
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 10, 30));
        
        // provide info for third task again
        
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 30));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();
        
        
        TaskPlanningWrapper plan1 = taskMan.getPlanningFor(task1);
        assertEquals(plan1.getTask(),task1);
        assertTrue(plan1.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 10, 30), LocalDateTime.of(2016, 1, 1, 11, 30))));
        
        TaskPlanningWrapper plan2 = taskMan.getPlanningFor(task2);
        assertEquals(plan2.getTask(),task2);
        assertTrue(plan2.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 11, 30), LocalDateTime.of(2016, 1, 1, 12, 30))));
        
        
        TaskPlanningWrapper plan3 = taskMan.getPlanningFor(task3);
        assertEquals(plan3.getTask(),task3);
        assertTrue(plan3.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 8, 30), LocalDateTime.of(2016, 1, 1, 9, 30))));
    }
    
    @Test
    public void multipleNestedConflictTest(){
        Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("task1",60,0, req1));
        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("task2",60,0, req1));
        TaskWrapper task3 = taskMan.createTaskFor(project, new TaskData("task3",60,0, req1));
        TaskWrapper task4 = taskMan.createTaskFor(project, new TaskData("task4",60,0, req1));
        
        Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
        res1.add(res);
        Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
        
        
        // plan first task
        ui.addRequestTask(task1);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        // plan second task
        ui.addRequestTask(task2);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 10, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();
        
        
        // plan third task
        ui.addRequestTask(task3);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 11, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();
    	
    	// plan fourth task
    	ui.addRequestTask(task4);
    	ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 30));
    	ui.addSelectResourcesFor(res1);
    	ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
    	// conflict! first task needs to be moved
    	
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 10, 30));
        
        // can't move first task due to conflict! either task2 or task3 needs to be moved

        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 12, 0));
    	
    	// provide info for first task again
    	
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 10, 30));
        
        // can't move first task due to conflict! the other task needs to be moved

        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 13, 0));
    	
    	// provide info for first task again
    	
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 10, 30));
    	
        
        // provide info for fourth task again
        
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 30));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();
        
        DateTimePeriod firstPeriod = new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0));
        DateTimePeriod secondPeriod = new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 13, 0), LocalDateTime.of(2016, 1, 1, 14, 0));
        
        TaskPlanningWrapper plan1 = taskMan.getPlanningFor(task1);
        assertEquals(plan1.getTask(),task1);
        assertTrue(plan1.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 10, 30), LocalDateTime.of(2016, 1, 1, 11, 30))));
        
        TaskPlanningWrapper plan2 = taskMan.getPlanningFor(task2);
        assertEquals(plan2.getTask(),task2);
        assertTrue(plan2.getPeriod().equals(firstPeriod) || plan2.getPeriod().equals(secondPeriod));
                
        TaskPlanningWrapper plan3 = taskMan.getPlanningFor(task3);
        assertEquals(plan3.getTask(),task3);
        assertTrue(plan3.getPeriod().equals(firstPeriod) || plan3.getPeriod().equals(secondPeriod));
        
        assertFalse(plan2.getPeriod().equals(plan3.getPeriod()));
        
        TaskPlanningWrapper plan4 = taskMan.getPlanningFor(task4);
        assertEquals(plan4.getTask(),task4);
        assertTrue(plan4.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 8, 30), LocalDateTime.of(2016, 1, 1, 9, 30))));
    }

    
}
