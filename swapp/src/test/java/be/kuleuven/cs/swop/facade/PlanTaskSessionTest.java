package be.kuleuven.cs.swop.facade;

import static org.junit.Assert.assertEquals;

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


public class PlanTaskSessionTest {
    private static TestingUI  ui;
    private static TaskMan taskMan;
    private static SessionController controller;
    
    private ProjectWrapper project;
    
    private int typeCount = 6;
    private int resourcesPerType = 2;
    
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
    public void noRequirementsTest() throws ConflictingPlanningWrapperException{
        Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
        
        Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
        Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();

        taskMan.createPlanning(task1, taskMan.getSystemTime().plusHours(1), res1, dev1); //TODO: check if this worked
    }
    
    @Test
    public void requirementsSatisfiedTest() throws ConflictingPlanningWrapperException{
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	req1.put(types[0],2);
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	res1.add(resources[0][0]);
    	res1.add(resources[0][1]);
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
    	taskMan.createPlanning(task1, taskMan.getSystemTime().plusHours(1), res1, dev1); //TODO: check if this worked
    }
    
    @Test
    public void requirementsSatisfiedWithExtraTest() throws ConflictingPlanningWrapperException{
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	req1.put(types[0],2);
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	res1.add(resources[0][0]);
    	res1.add(resources[0][1]);
    	res1.add(resources[1][0]);
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
    	taskMan.createPlanning(task1, taskMan.getSystemTime().plusHours(1), res1, dev1); //TODO: check if this worked
    }
    
    @Test
    public void RecursiverequirementsSatisfiedTest() throws ConflictingPlanningWrapperException{
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	req1.put(types[2],1);
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	res1.add(resources[0][0]);
    	res1.add(resources[2][0]);
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
    	taskMan.createPlanning(task1, taskMan.getSystemTime().plusHours(1), res1, dev1); //TODO: check if this worked
    }
    
    @Test
    public void dailyAvailibilityValidTest() throws ConflictingPlanningWrapperException{
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 9, 0));
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	res1.add(resources[5][0]);
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
    	taskMan.createPlanning(task1, taskMan.getSystemTime(), res1, dev1); //TODO: check if this worked
    }
    
    
    
    
    
    //TODO: Is this intended behavior?
    @Test
    public void notAManagerTest() throws ConflictingPlanningWrapperException{
        controller.setCurrentUser(new UserWrapper(new Developer("Dave")));
        
        Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
        
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));

        taskMan.createPlanning(task1, taskMan.getSystemTime().plusHours(1), new HashSet<ResourceWrapper>(), new HashSet<DeveloperWrapper>());
        
        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void notEnoughResourcesTest() throws ConflictingPlanningWrapperException{
        Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
        req1.put(types[0], 2);
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
        
        Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
        res1.add(resources[0][0]);
        
        Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();

        taskMan.createPlanning(task1, taskMan.getSystemTime().plusHours(1), res1, dev1);
        
    	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void conflictingResourcesTest() throws ConflictingPlanningWrapperException{
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
    	res1.add(resources[0][0]);
    	res1.add(resources[1][0]);
    	
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
    	taskMan.createPlanning(task1, taskMan.getSystemTime().plusHours(1), res1, dev1);
    	
    	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void selfConflictingResourcesTest() throws ConflictingPlanningWrapperException{
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
    	res1.add(resources[3][0]);
    	res1.add(resources[3][1]);
    	
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
    	taskMan.createPlanning(task1, taskMan.getSystemTime().plusHours(1), res1, dev1);
    	
    	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void resourceDependencyNotMetTest() throws ConflictingPlanningWrapperException{
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
    	res1.add(resources[2][0]);
    	
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
    	taskMan.createPlanning(task1, taskMan.getSystemTime().plusHours(1), res1, dev1);
    	
    	
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void dailyAvailibilityInvalidTest() throws ConflictingPlanningWrapperException{
    	taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 8, 0));
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();        
    	res1.add(resources[5][0]);
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
    	taskMan.createPlanning(task1, taskMan.getSystemTime(), res1, dev1); //TODO: check if this worked
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void nullResourceTest() throws ConflictingPlanningWrapperException{
    	Map<ResourceTypeWrapper, Integer> req1 = new HashMap<>();
    	TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc",60,0, req1));
    	
    	Set<ResourceWrapper> res1 = new HashSet<ResourceWrapper>();
    	res1.add(null);
    	
    	Set<DeveloperWrapper> dev1 = new HashSet<DeveloperWrapper>();
    	
    	taskMan.createPlanning(task1, taskMan.getSystemTime().plusHours(1), res1, dev1);
    	
    	
    }
    
}
