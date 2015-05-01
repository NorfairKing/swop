package be.kuleuven.cs.swop.domain.company.planning;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;


public class TaskPlanningTest {
    
    Set<Developer> devSet;
    Set<Resource> resSet;
    Task task;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        devSet = new HashSet<>();
        devSet.add(new Developer("Sean"));
        
        resSet = new HashSet<>();
        
        task = new Task("description", 50, .5);
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void constructorValidTest() {
        new TaskPlanning(devSet, task, LocalDateTime.of(2015, 02, 20, 12, 0));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorInvalidDevelopersTest(){
        devSet.add(null);
        new TaskPlanning(devSet, task, LocalDateTime.of(2015, 02, 20, 12, 0));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorInvalidTaskTest(){
        new TaskPlanning(devSet, null, LocalDateTime.of(2015, 02, 20, 12, 0));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorInvalidPlannedStartTimeTest(){
        new TaskPlanning(devSet, task, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorInvalidResourcesTest(){
        resSet.add(null);
        new TaskPlanning(devSet, task, LocalDateTime.of(2015,02,20,12,0),resSet);
    }
    

}
