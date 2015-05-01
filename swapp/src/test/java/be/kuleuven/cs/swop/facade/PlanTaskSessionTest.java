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
import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.domain.company.user.Manager;
import be.kuleuven.cs.swop.facade.TaskMan;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.TaskData;


public class PlanTaskSessionTest {
    private static TestingUI  ui;
    private static TaskMan taskMan;
    private static SessionController controller;
    
    private ProjectWrapper project;
    
    private int typeCount = 6;
    private int resourcesPerType = 2;
    private DeveloperWrapper dev;
    
    // type 0: no requirements, No conflicts, no self-conflict, no daily availability
    // type 1: no requirements, conflicts with type0, no self-conflict, no daily availability
    // type 2: requires type 0, No conflicts, no self-conflict, no daily availability
    // type 3: no requirements, No conflicts, self-conflicts, no daily availability
    // type 4: requires type2, conflicts with type 3, no self-conflict, no daily availability
    // type 5: no requirements, No conflicts, no self-conflict, daily availability between 9:00 and 12:00
    private ResourceTypeWrapper[] types = new ResourceTypeWrapper[typeCount];
    
    private ResourceWrapper[][] resources= new ResourceWrapper[typeCount][resourcesPerType];
    
    @Before
    public void setUp() throws Exception {
        ui = new TestingUI();
        taskMan = new TaskMan();
        controller = new SessionController(ui, taskMan);
        controller.setCurrentUser(new UserWrapper(new Manager("Jake")));
        dev = taskMan.createDeveloper(new DeveloperData("Jane"));
        project = taskMan.createProject(new ProjectData("Title", "Descr", taskMan.getSystemTime().plusHours(1)));
        types[0] = taskMan.createResourceType(new ResourceTypeData("type0",
        		new HashSet<ResourceTypeWrapper>(),
        		new HashSet<ResourceTypeWrapper>(),
        		false,
        		new LocalTime[0]));
        
        Set<ResourceTypeWrapper> type0Set = new HashSet<ResourceTypeWrapper>();
        type0Set.add( types[0]);
        
        types[1] = taskMan.createResourceType(new ResourceTypeData("type1",
        		new HashSet<ResourceTypeWrapper>(),
        		type0Set,
        		false,
        		new LocalTime[0]));
        
        types[2] = taskMan.createResourceType(new ResourceTypeData("type2",
        		type0Set,
        		new HashSet<ResourceTypeWrapper>(),
        		false,
        		new LocalTime[0]));
        
        types[3] = taskMan.createResourceType(new ResourceTypeData("type3",
        		new HashSet<ResourceTypeWrapper>(),
        		new HashSet<ResourceTypeWrapper>(),
        		true,
        		new LocalTime[0]));
        
        Set<ResourceTypeWrapper> type2Set = new HashSet<ResourceTypeWrapper>();
        type2Set.add(types[2]);
        Set<ResourceTypeWrapper> type3Set = new HashSet<ResourceTypeWrapper>();
        type3Set.add(types[3]);
        
        types[4] = taskMan.createResourceType(new ResourceTypeData("type4",
        		type2Set,
        		type3Set,
        		false,
        		new LocalTime[0]));
        
        LocalTime[] daily = new LocalTime[2];
        daily[0] = LocalTime.of(8, 0);
        daily[1] = LocalTime.of(12, 0);
        types[5] = taskMan.createResourceType(new ResourceTypeData("type5",
        		new HashSet<ResourceTypeWrapper>(),
        		new HashSet<ResourceTypeWrapper>(),
        		false,
        		daily ));
        
        
        for(int i = 0; i < typeCount; i++){
        	for(int j = 0; j < resourcesPerType; j++){
        		resources[i][j] = taskMan.createResource(new ResourceData("type " + i + " num " + j, types[i]));
        	}
        }
                
        ui.start();
    }
    
    
    @Test
    public void noRequirementsTest(){
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
        Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
        
        Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
        Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
        
        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();
        
        TaskPlanningWrapper plan = taskMan.getPlanningFor(task1);
        assertEquals(plan.getTask(),task1);
        assertTrue(plan.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0))));
        assertTrue(plan.getReservations().isEmpty());
        assertTrue(plan.getDevelopers().isEmpty());
    }
    
    @Test
    public void onlyDevTest(){
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	dev1.add(dev);
    	
    	ui.addRequestTask(task1);
    	ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
    	ui.addSelectResourcesFor(res1);
    	ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	controller.startPlanTaskSession();
    	
        TaskPlanningWrapper plan = taskMan.getPlanningFor(task1);
        assertEquals(plan.getTask(),task1);
        assertTrue(plan.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0))));
        assertTrue(plan.getReservations().isEmpty());
        assertTrue(plan.getDevelopers().size() == 1);
        assertTrue(plan.getDevelopers().contains(dev));
    }
    
    @Test
    public void withBreakTest() {
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",180,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	dev1.add(dev);
    	
    	
    	ui.addRequestTask(task1);
    	ui.addSelectTime(taskMan.getSystemTime());
    	ui.addSelectResourcesFor(res1);
    	ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(true);
    	controller.startPlanTaskSession();
    	
        TaskPlanningWrapper plan = taskMan.getPlanningFor(task1);
        assertEquals(plan.getTask(),task1);
        assertTrue(plan.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 11, 0), LocalDateTime.of(2016, 1, 1, 15, 0))));
        assertTrue(plan.getReservations().isEmpty());
        assertTrue(plan.getDevelopers().size() == 1);
        assertTrue(plan.getDevelopers().contains(dev));
    }
    
    @Test
    public void withBreakNoDevelopersTest() {
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",180,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();    	
    	
    	ui.addRequestTask(task1);
    	ui.addSelectTime(taskMan.getSystemTime());
    	ui.addSelectResourcesFor(res1);
    	ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(true);
    	controller.startPlanTaskSession(); 
    	
        TaskPlanningWrapper plan = taskMan.getPlanningFor(task1);
        assertEquals(plan.getTask(),task1);
        assertTrue(plan.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 11, 0), LocalDateTime.of(2016, 1, 1, 14, 0))));
        assertTrue(plan.getReservations().isEmpty());
        assertTrue(plan.getDevelopers().isEmpty());
    }
    
    @Test
    public void withBreakWrongTimeTest() {
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 14, 0));
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",180,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();    	
    	dev1.add(dev);

    	ui.addRequestTask(task1);
    	ui.addSelectTime(taskMan.getSystemTime());
    	ui.addSelectResourcesFor(res1);
    	ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(true);
    	controller.startPlanTaskSession();
    	
        TaskPlanningWrapper plan = taskMan.getPlanningFor(task1);
        assertEquals(plan.getTask(),task1);
        assertTrue(plan.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 14, 0), LocalDateTime.of(2016, 1, 1, 17, 0))));
        assertTrue(plan.getReservations().isEmpty());
        assertTrue(plan.getDevelopers().size() == 1);
        assertTrue(plan.getDevelopers().contains(dev));
    }
    
    @Test
    public void requirementsSatisfiedTest() {
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	req1.put(types[0],2);
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	res1.add(resources[0][0]);
    	res1.add(resources[0][1]);
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();
        
        TaskPlanningWrapper plan = taskMan.getPlanningFor(task1);
        assertEquals(plan.getTask(),task1);
        assertTrue(plan.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0))));
        assertTrue(plan.getReservations().size() == 2);
        assertTrue(plan.getReservations().contains(resources[0][0]));
        assertTrue(plan.getReservations().contains(resources[0][1]));
        assertTrue(plan.getDevelopers().isEmpty());
        }
    
    @Test
    public void requirementsSatisfiedWithExtraTest() {
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	req1.put(types[0],2);
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	res1.add(resources[0][0]);
    	res1.add(resources[0][1]);
    	res1.add(resources[3][0]);
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();
        
        TaskPlanningWrapper plan = taskMan.getPlanningFor(task1);
        assertEquals(plan.getTask(),task1);
        assertTrue(plan.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0))));
        assertTrue(plan.getReservations().size() == 3);
        assertTrue(plan.getReservations().contains(resources[0][0]));
        assertTrue(plan.getReservations().contains(resources[0][1]));
        assertTrue(plan.getReservations().contains(resources[3][0]));
        assertTrue(plan.getDevelopers().isEmpty());
        
        }
    
    @Test
    public void RecursiverequirementsSatisfiedTest() {
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	req1.put(types[2],1);
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	res1.add(resources[0][0]);
    	res1.add(resources[2][0]);
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();
        
        TaskPlanningWrapper plan = taskMan.getPlanningFor(task1);
        assertEquals(plan.getTask(),task1);
        assertTrue(plan.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0))));
        assertTrue(plan.getReservations().size() == 2);
        assertTrue(plan.getReservations().contains(resources[0][0]));
        assertTrue(plan.getReservations().contains(resources[2][0]));
        assertTrue(plan.getDevelopers().isEmpty());
        
        }
    
    @Test
    public void dailyAvailibilityValidTest() {
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 9, 0));
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	res1.add(resources[5][0]);
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime());
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();
        
        TaskPlanningWrapper plan = taskMan.getPlanningFor(task1);
        assertEquals(plan.getTask(),task1);
        assertTrue(plan.getPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 9, 0), LocalDateTime.of(2016, 1, 1, 10, 0))));
        assertTrue(plan.getReservations().size() == 1);
        assertTrue(plan.getReservations().contains(resources[5][0]));
        assertTrue(plan.getDevelopers().isEmpty());
        }
    
    
    
    
    
    //TODO: Is this intended behavior? -> A non-manager can plan a task.
    @Test
    public void notAManagerTest() {
        controller.setCurrentUser(new UserWrapper(new Developer("Dave")));
        
        Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
        
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));

    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
        
        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();
        
        
    }
    
    @Test(expected = RuntimeException.class)
    public void notEnoughResourcesTest() {
        Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
        req1.put(types[0], 2);
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
        
        Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
        res1.add(resources[0][0]);
        
        Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();      
    	
    }
    
    @Test(expected = RuntimeException.class)
    public void conflictingResourcesTest() {
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
    	res1.add(resources[0][0]);
    	res1.add(resources[1][0]);
    	
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();   	
    	
    }
    
    @Test(expected = RuntimeException.class)
    public void selfConflictingResourcesTest() {
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
    	res1.add(resources[3][0]);
    	res1.add(resources[3][1]);
    	
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();     	
    	
    }
    
    @Test(expected = RuntimeException.class)
    public void resourceDependencyNotMetTest() {
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
    	res1.add(resources[2][0]);
    	
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();   	
    	
    }
    
    @Test(expected = RuntimeException.class)
    public void dailyAvailibilityInvalidTest() {
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 7, 0));
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	res1.add(resources[5][0]);
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime());
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();    
        }
    
    @Test
    public void nullTaskTest() {
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();    	
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
    	ui.addRequestTask(null);
    	ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
    	ui.addSelectResourcesFor(res1);
    	ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	controller.startPlanTaskSession();   	
    	
    }
    
    
    @Test
    public void nullstartTimeTest() {
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
    	
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
    	ui.addRequestTask(task1);
    	ui.addSelectTime(null);
    	ui.addSelectResourcesFor(res1);
    	ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	controller.startPlanTaskSession();   	
    	
    }
    
    @Test
    public void nullResourceTest() {
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(null);
        ui.addSelectDevelopers(dev1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();   	
    	
    }
    
    @Test
    public void nullDeveloperTest() {
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();    	

    	ui.addRequestTask(task1);
    	ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
    	ui.addSelectResourcesFor(res1);
    	ui.addSelectDevelopers(null);
    	ui.addShouldAddBreak(false);
    	controller.startPlanTaskSession();   	
    	
    }
    
    
    @Test(expected = RuntimeException.class)
    public void nullTaskinWrapperTest() {
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();    	
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
    	ui.addRequestTask(new TaskWrapper(null));
    	ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
    	ui.addSelectResourcesFor(res1);
    	ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	controller.startPlanTaskSession();   	
    	
    }
    
    
    @Test(expected = RuntimeException.class)
    public void nullResourceInWrapperTest() {
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
    	res1.add(new ResourceWrapper(null));
    	
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addSelectDevelopers(dev1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();   	
    	
    }
    
    @Test(expected = RuntimeException.class)
    public void nullDeveloperinWrapperTest() {
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();    	
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	dev1.add(new DeveloperWrapper(null));

    	ui.addRequestTask(task1);
    	ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
    	ui.addSelectResourcesFor(res1);
    	ui.addSelectDevelopers(dev1);
    	ui.addShouldAddBreak(false);
    	controller.startPlanTaskSession();   	
    	
    }
    
}
