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
    
    Set<Resource> resSet;
    Task task;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        
        resSet = new HashSet<>();
        resSet.add(new Developer("Sean"));
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void constructorValidTest() {
        new TaskPlanning(LocalDateTime.of(2015, 02, 20, 12, 0), resSet, 50);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorInvalidResourcesTest(){
        resSet.add(null);
        new TaskPlanning(LocalDateTime.of(2015, 02, 20, 12, 0), resSet, 50);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorInvalidPlannedStartTimeTest(){
        new TaskPlanning(null, resSet, 50);
    }

}
