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

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.TaskData;


public class ResolveConflictsSessionTest extends BaseFacadeTest{
    
    private ProjectWrapper project;
    
    private ResourceType type;
    
    private Resource res;
    
    private static Developer dev;
    
    @Before
    public void setUp() throws Exception {
        simpleSetup();
    	
    	dev =new Developer("Dave");
        project = taskMan.createProject(new ProjectData("Title", "Descr", taskMan.getSystemTime().plusHours(1)));
        type = taskMan.createResourceType(new ResourceTypeData("type0",
        		new HashSet<ResourceType>(),
        		new HashSet<ResourceType>(),
        		false,
        		new LocalTime[0]));
        
        res = taskMan.createResource(new ResourceData("res", type));
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 8, 0));
      
        ui.start();
    }
    
    
    @Test
    public void exactlySideBySideTest(){
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
        
        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(res);
        res1.add(dev);
        
        
        // plan first task
        ui.addRequestTask(task1);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        // plan second task
        ui.addRequestTask(task2);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 9, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        
        TaskPlanning plan1 = task1.getPlanning();
        assertTrue(plan1.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 8, 0), LocalDateTime.of(2016, 1, 1, 9, 0))));
        
        TaskPlanning plan2 = task2.getPlanning();
        assertTrue(plan2.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 9, 0), LocalDateTime.of(2016, 1, 1, 10, 0))));

    }

    @Test
    public void sameTimeDifferentDayTest(){
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("task1",60,0, req1));
        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("task2",60,0, req1));
        
        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(res);
        res1.add(dev);
        
        
        // plan first task
        ui.addRequestTask(task1);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        // plan second task
        ui.addRequestTask(task2);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 2, 8, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        TaskPlanning plan1 = task1.getPlanning();
        assertTrue(plan1.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 8, 0), LocalDateTime.of(2016, 1, 1, 9, 0))));
        
        TaskPlanning plan2 = task2.getPlanning();
        assertTrue(plan2.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 2, 8, 0), LocalDateTime.of(2016, 1, 2, 9, 0))));
    }

    @Test
    public void simpleConflictTest(){
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("task1",60,0, req1));
        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("task2",60,0, req1));
        
        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(res);
        res1.add(dev);
        
        
        // plan first task
        ui.addRequestTask(task1);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        // plan second task
        
        ui.addRequestTask(task2);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
    	// conflict! move first task
    	
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 9, 0));

        // provide info for second task again
        
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        TaskPlanning plan1 = task1.getPlanning();
        assertTrue(plan1.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 9, 0), LocalDateTime.of(2016, 1, 1, 10, 0))));
        
        TaskPlanning plan2 = task2.getPlanning();
        assertTrue(plan2.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 8, 0), LocalDateTime.of(2016, 1, 1, 9, 0))));

    }
    
    @Test
    public void devConflictTest(){
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("task1",60,0, req1));
        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("task2",60,0, req1));
        
        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(dev);
        
        // plan first task
        ui.addRequestTask(task1);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        
        controller.startPlanTaskSession(); 
        
        // plan second task
        
        ui.addRequestTask(task2);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        
        // conflict! move first task
        
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 9, 0));

        // provide info for second task again
        
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        
        controller.startPlanTaskSession(); 
        
        TaskPlanning plan1 = task1.getPlanning();
        assertTrue(plan1.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 9, 0), LocalDateTime.of(2016, 1, 1, 10, 0))));
        
        TaskPlanning plan2 = task2.getPlanning();
        assertTrue(plan2.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 8, 0), LocalDateTime.of(2016, 1, 1, 9, 0))));

    }
    
    @Test
    public void multipleConflictTest(){
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("task1",60,0, req1));
        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("task2",60,0, req1));
        TaskWrapper task3 = taskMan.createTaskFor(project, new TaskData("task3",60,0, req1));
        
        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(res);
        res1.add(dev);
        
        
        // plan first task
        ui.addRequestTask(task1);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        // plan second task
        ui.addRequestTask(task2);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 9, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();
        
        
        // plan third task
        ui.addRequestTask(task3);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 30));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
    	// conflict! either first or second task needs to be moved
    	
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 12, 0));
        
        // provide info for third task again

        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 30));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
    	// conflict! the other task needs to be moved
    	
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 13, 0));
        
        // provide info for third task again
        
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 30));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();         
        
        DateTimePeriod firstPeriod = new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0));
        DateTimePeriod secondPeriod = new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 13, 0), LocalDateTime.of(2016, 1, 1, 14, 0));
        
        TaskPlanning plan1 = task1.getPlanning();
        assertTrue(plan1.getEstimatedPeriod().equals(firstPeriod) || plan1.getEstimatedPeriod().equals(secondPeriod));
        
        TaskPlanning plan2 = task2.getPlanning();
        assertTrue(plan2.getEstimatedPeriod().equals(firstPeriod) || plan2.getEstimatedPeriod().equals(secondPeriod));
        
        assertFalse(plan1.getEstimatedPeriod().equals(plan2.getEstimatedPeriod()));
        
        TaskPlanning plan3 = task3.getPlanning();
        assertTrue(plan3.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 8, 30), LocalDateTime.of(2016, 1, 1, 9, 30))));
    	
    }
    
    @Test
    public void simpleNestedConflictTest(){
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("task1",60,0, req1));
        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("task2",60,0, req1));
        TaskWrapper task3 = taskMan.createTaskFor(project, new TaskData("task3",60,0, req1));
        
        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(res);
        res1.add(dev);
        
        
        // plan first task
        ui.addRequestTask(task1);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        // plan second task
        ui.addRequestTask(task2);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 10, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();
        
        
        // plan third task
        ui.addRequestTask(task3);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 30));
        ui.addSelectResourcesFor(res1);
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
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();
        
        
        TaskPlanning plan1 = task1.getPlanning();
        assertTrue(plan1.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 10, 30), LocalDateTime.of(2016, 1, 1, 11, 30))));
        
        TaskPlanning plan2 = task2.getPlanning();
        assertTrue(plan2.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 11, 30), LocalDateTime.of(2016, 1, 1, 12, 30))));
        
        
        TaskPlanning plan3 = task3.getPlanning();
        assertTrue(plan3.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 8, 30), LocalDateTime.of(2016, 1, 1, 9, 30))));
    }
    
    @Test
    public void multipleNestedConflictTest(){
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("task1",60,0, req1));
        TaskWrapper task2 = taskMan.createTaskFor(project, new TaskData("task2",60,0, req1));
        TaskWrapper task3 = taskMan.createTaskFor(project, new TaskData("task3",60,0, req1));
        TaskWrapper task4 = taskMan.createTaskFor(project, new TaskData("task4",60,0, req1));
        
        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(res);
        res1.add(dev);
        
        
        // plan first task
        ui.addRequestTask(task1);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        
        // plan second task
        ui.addRequestTask(task2);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 10, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();
        
        
        // plan third task
        ui.addRequestTask(task3);
        ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 11, 0));
        ui.addSelectResourcesFor(res1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();
    	
    	// plan fourth task
    	ui.addRequestTask(task4);
    	ui.addSelectTime(LocalDateTime.of(2016, 1, 1, 8, 30));
    	ui.addSelectResourcesFor(res1);
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
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession();
        
        DateTimePeriod firstPeriod = new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0));
        DateTimePeriod secondPeriod = new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 13, 0), LocalDateTime.of(2016, 1, 1, 14, 0));
        
        TaskPlanning plan1 = task1.getPlanning();
        assertTrue(plan1.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 10, 30), LocalDateTime.of(2016, 1, 1, 11, 30))));
        
        TaskPlanning plan2 = task2.getPlanning();
        assertTrue(plan2.getEstimatedPeriod().equals(firstPeriod) || plan2.getEstimatedPeriod().equals(secondPeriod));
                
        TaskPlanning plan3 = task3.getPlanning();
        assertTrue(plan3.getEstimatedPeriod().equals(firstPeriod) || plan3.getEstimatedPeriod().equals(secondPeriod));
        
        assertFalse(plan2.getEstimatedPeriod().equals(plan3.getEstimatedPeriod()));
        
        TaskPlanning plan4 = task4.getPlanning();
        assertTrue(plan4.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 8, 30), LocalDateTime.of(2016, 1, 1, 9, 30))));
    }

    
}
