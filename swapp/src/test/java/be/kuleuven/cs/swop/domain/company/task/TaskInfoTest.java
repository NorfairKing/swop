package be.kuleuven.cs.swop.domain.company.task;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;


public class TaskInfoTest {

    private TaskInfo             taskInfo;
    private ResourceType     selfConflictingResourceType;
    private ResourceType     regularResourceType;
    private Requirements reqs = new Requirements(new HashSet<Requirement>());


    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        taskInfo = new TaskInfo("Desc", 100, 0.1, reqs, new HashSet<Task>());
    }

    @Test
    public void constructorValidTest() {
        new TaskInfo("Desc", 100, 0.1, reqs, new HashSet<Task>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidDescriptionTest() {
        new TaskInfo(null, 100, 0.1, reqs, new HashSet<Task>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidEstimatedDurationTest1() {
        new TaskInfo("Desc", 0, 0.1, reqs, new HashSet<Task>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidEstimatedDurationTest2() {
        new TaskInfo("Desc", -2, 0.1, reqs, new HashSet<Task>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidAcceptableDeviationTest1() {
        new TaskInfo("Desc", 100, -0.5, reqs, new HashSet<Task>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidAcceptableDeviationTest2() {
        new TaskInfo("Desc", 100, Double.NEGATIVE_INFINITY, reqs, new HashSet<Task>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidAcceptableDeviationTest3() {
        new TaskInfo("Desc", 100, Double.POSITIVE_INFINITY, reqs, new HashSet<Task>());

    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidAcceptableDeviationTest4() {
        new TaskInfo("Desc", 100, Double.NaN, reqs, new HashSet<Task>());

    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidRequirementConflictingTest11() {
        Set<Requirement> reqsSet = new HashSet<>();
        reqsSet.add(new Requirement(2, selfConflictingResourceType));// self conflicting requirement, but only one is needed so this should work.
        reqs = new Requirements(reqsSet);
        new TaskInfo("Desc", 100, 0.1, reqs, new HashSet<Task>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidRequirementConflictingTest2() {
        Set<Requirement> reqsSet = new HashSet<>();
        reqsSet.add(new Requirement(2, regularResourceType));
        reqsSet.add(new Requirement(1, selfConflictingResourceType));// self conflicting requirement, but only one is needed so this should work.
        reqs = new Requirements(reqsSet);
        new TaskInfo("Desc", 100, 0.1, reqs, new HashSet<Task>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidRequirementsNonUniqueTypesTest() {
        Set<Requirement> reqsSet = new HashSet<>();
        reqsSet.add(new Requirement(2, regularResourceType));
        reqsSet.add(new Requirement(2, regularResourceType));
        reqs = new Requirements(reqsSet);
        new TaskInfo("Desc", 100, 0.1, reqs, new HashSet<Task>());
    }

    @Test
    public void canHaveAsDescriptionTest() {
        assertTrue(taskInfo.canHaveAsDescription("Testdescription"));
        assertFalse(taskInfo.canHaveAsDescription(null));
    }
    @Test
    public void canHaveAsEstimatedDurationTest() {
        assertTrue(taskInfo.canHaveAsEstimatedDuration(10));
        assertFalse(taskInfo.canHaveAsEstimatedDuration(0));
        assertFalse(taskInfo.canHaveAsEstimatedDuration(-1));
    }

    @Test
    public void canHaveAsDependencyTest() {
        Set<Task> deps = new HashSet<Task>();
        deps.add(new Task(new TaskInfo("derp", 60, 0, reqs, new HashSet<Task>())));
        new TaskInfo("Desc", 100, 0.1, reqs, deps);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void canHaveAsDependencyInvalidTest() {
        Set<Task> deps = new HashSet<Task>();
        deps.add(null);
        new TaskInfo("Desc", 100, 0.1, reqs, deps);
    }

    @Test
    public void containsDependencyTest(){
        Set<Task> deps = new HashSet<Task>();
        Task test = new Task(new TaskInfo("derp", 60, 0, reqs, new HashSet<Task>()));
        deps.add(test);
        taskInfo = new TaskInfo("Desc", 100, 0.1, reqs, deps);
    	assertFalse(taskInfo.containsDependency(null));
    	assertTrue(taskInfo.containsDependency(test));
    }

    @Test
    public void canHaveAsDeviationTest() {
        assertTrue(taskInfo.canHaveAsDeviation(0.5));
        assertTrue(taskInfo.canHaveAsDeviation(0));
        assertFalse(taskInfo.canHaveAsDeviation(-0.5));
        assertFalse(taskInfo.canHaveAsDeviation(Double.NaN));
        assertFalse(taskInfo.canHaveAsDeviation(Double.POSITIVE_INFINITY));

    }

    @Test
    public void getDependencySetTest() {
        Set<Task> deps = new HashSet<Task>();
        Task test = new Task(new TaskInfo("derp", 60, 0, reqs, new HashSet<Task>()));
        deps.add(test);
        taskInfo = new TaskInfo("Desc", 100, 0.1, reqs, deps);
        assertTrue(taskInfo.getDependencySet().contains(test));
    }

}
