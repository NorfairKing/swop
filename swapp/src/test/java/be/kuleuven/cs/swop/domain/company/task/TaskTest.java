package be.kuleuven.cs.swop.domain.company.task;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;


public class TaskTest {

    private double           delta     = 10e-15;
    private Task             task;
    private ResourceType     selfConflictingResourceType;
    private ResourceType     regularResourceType;
    private ResourceType     conflictingResourceType;
    private Requirements reqs = new Requirements(new HashSet<Requirement>());
    private TaskInfo taskInfo = taskInfo = new TaskInfo("Desc", 100, 0.1, reqs, new HashSet<Task>()); 

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        task = new Task(taskInfo);

        selfConflictingResourceType = new ResourceType("selfconflicting", true);
        regularResourceType = new ResourceType("regular");

        Set<ResourceType> conflicts = new HashSet<>();
        conflicts.add(regularResourceType);
        conflictingResourceType = new ResourceType("conclicting", null, conflicts, false);
    }

    @Test
    public void constructorValidTest1() {
        new Task(taskInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidTest() {
        new Task(null);
    }

    
    @Test
    public void setStatusTest() {
    	TaskStatus testStatus = new ExecutingStatus(task);
    	task.setStatus(testStatus);
    	assertTrue(task.isExecuting());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setStatusWrongTaskTest() {
    	Task testTask = new Task(taskInfo);
    	TaskStatus testStatus = new ExecutingStatus(testTask);
    	task.setStatus(testStatus);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void setStatusNullTest() {
    	task.setStatus(null);
    }

}
