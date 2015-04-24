package be.kuleuven.cs.swop.facade;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.TestingUI;
import be.kuleuven.cs.swop.domain.company.user.Developer;
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
        
        
        //TODO: check if this worked

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
        
        //TODO: check if this worked
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
        
        ui.addSelectTime(LocalDateTime.of(2016, 1, 2, 8, 0));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	
        controller.startPlanTaskSession(); 
        //TODO: check if this worked

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
        
        //TODO: check if this worked
    	
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
    	
    	// conflict! either first task needs to be moved
    	
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
        
        //TODO: check if this worked
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
        
        //TODO: check if this worked
    }

    
}
