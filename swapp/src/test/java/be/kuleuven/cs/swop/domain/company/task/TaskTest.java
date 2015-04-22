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
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;


public class TaskTest {

    private double           delta     = 10e-15;
    private Task             task;
    private ResourceType     selfConflictingResourceType;
    private ResourceType     regularResourceType;
    private ResourceType     conflictingResourceType;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        task = new Task("Desc", 100, 0.1);

        selfConflictingResourceType = new ResourceType("selfconflicting", true);
        regularResourceType = new ResourceType("regular");

        Set<ResourceType> conflicts = new HashSet();
        conflicts.add(regularResourceType);
        conflictingResourceType = new ResourceType("conclicting", null, conflicts, false);
    }

    @Test
    public void constructorValidTest1() {
        new Task("Desc", 100, 0.1);
    }

    @Test
    public void constructorValidTest2() {
        new Task("Desc", 100, 0.1, new HashSet());
    }

    @Test
    public void constructorValidTest3() {
        Set<Requirement> reqs = new HashSet();
        // self conflicting requirement, but only one is needed so this should work.
        reqs.add(new Requirement(1, new ResourceType("type", null, null, true)));
        new Task("Desc", 100, 0.1, reqs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidDescriptionTest() {
        new Task(null, 100, 0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidEstimatedDurationTest1() {
        new Task("desc", 0, 0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidEstimatedDurationTest2() {
        new Task("desc", -2, 0.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidAcceptableDeviationTest1() {
        new Task("desc", 20, -0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidAcceptableDeviationTest2() {
        new Task("desc", 20, Double.NEGATIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidAcceptableDeviationTest3() {
        new Task("desc", 20, Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidAcceptableDeviationTest4() {
        new Task("desc", 20, Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidRequirementConflictingTest11() {
        Set<Requirement> reqs = new HashSet();
        reqs.add(new Requirement(2, selfConflictingResourceType));// self conflicting requirement, but only one is needed so this should work.
        new Task("Desc", 100, 0.1, reqs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidRequirementConflictingTest2() {
        // self conflicting requirement, but only one is needed so this should work.
        Set<Requirement> reqs = new HashSet();
        reqs.add(new Requirement(2, regularResourceType));
        reqs.add(new Requirement(1, conflictingResourceType));
        new Task("Desc", 100, 0.1, reqs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorInvalidRequirementsNonUniqueTypesTest() {
        Set<Requirement> reqs = new HashSet();
        reqs.add(new Requirement(2, regularResourceType));
        reqs.add(new Requirement(2, regularResourceType));
        new Task("Desc", 100, 0.1, reqs);
    }

    @Test
    public void canHaveAsDescriptionTest() {
        assertTrue(task.canHaveAsDescription("Testdescription"));
        assertFalse(task.canHaveAsDescription(null));
    }

    @Test
    public void setDescriptionTest() {
        String desc = "Testdescription";
        task.setDescription(desc);
        assertEquals(desc, task.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setDescriptionNullTest() {
        task.setDescription(null);
    }

    @Test
    public void canHaveAsEstimatedDurationTest() {
        assertTrue(task.canHaveAsEstimatedDuration(10));
        assertFalse(task.canHaveAsEstimatedDuration(0));
        assertFalse(task.canHaveAsEstimatedDuration(-1));
    }

    @Test
    public void setEstimatedDurationTest() {
        long dur = 10;
        task.setEstimatedDuration(dur);
        assertTrue(dur == task.getEstimatedDuration());

        try {
            task.setEstimatedDuration(-1);
            fail();
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void canHaveAsDependencyTest() {
        Task t1 = new Task("task1", 50, 0);
        Task t2 = new Task("task2", 60, 0);
        Task t3 = new Task("task3", 70, 0);
        Task t4 = new Task("task4", 80, 0);
        Task t5 = new Task("task5", 90, 0);

        // null can never be a depencency
        assertFalse(t1.canHaveAsDependency(null));

        // A task cannot depend on itself
        assertFalse(t1.canHaveAsDependency(t1));

        // Some valid cases
        assertTrue(t1.canHaveAsDependency(t2));
        assertTrue(t1.canHaveAsDependency(t4));

        t1.addDependency(t2);
        t1.addDependency(t4);

        assertTrue(t2.canHaveAsDependency(t3));
        assertTrue(t2.canHaveAsDependency(t3));

        t2.addDependency(t3);
        t2.addDependency(t4);

        assertTrue(t3.canHaveAsDependency(t5));
        assertTrue(t4.canHaveAsDependency(t5));
        t3.addDependency(t5);
        t4.addDependency(t5);

        // Tests for dependency loops
        assertFalse(t2.canHaveAsDependency(t1));

        assertFalse(t4.canHaveAsDependency(t1));
        assertFalse(t4.canHaveAsDependency(t2));

        assertFalse(t5.canHaveAsDependency(t1));
        assertFalse(t5.canHaveAsDependency(t2));
        assertFalse(t5.canHaveAsDependency(t3));
        assertFalse(t5.canHaveAsDependency(t4));

    }

    @Test
    public void addDependencyTest() {
        Task task2 = new Task("Hi", 1, 0);
        task.addDependency(task2);
        assertTrue(task.getDependencySet().contains(task2));

        // Limited testing here because canHaveAsDependencyTest goes in depth
        exception.expect(IllegalArgumentException.class);
        task.addDependency(null);
    }

    @Test
    public void canHaveAsDeviationTest() {
        assertTrue(task.canHaveAsDeviation(0.5));
        assertTrue(task.canHaveAsDeviation(0));
        assertFalse(task.canHaveAsDeviation(-0.5));
        assertFalse(task.canHaveAsDeviation(Double.NaN));
        assertFalse(task.canHaveAsDeviation(Double.POSITIVE_INFINITY));

    }

    @Test
    public void setAcceptableDeviationValidTest1() {
        task.setAcceptableDeviation(0.5);
        assertEquals(0.5, task.getAcceptableDeviation(), delta);
    }

    public void setAcceptableDeviationValidTest2() {
        task.setAcceptableDeviation(0);
        assertEquals(0, task.getAcceptableDeviation(), delta);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setAcceptableDeviationInvalidTest1() {
        task.setAcceptableDeviation(-0.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setAcceptableDeviationInvalidTest2() {
        task.setAcceptableDeviation(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setAcceptableDeviationInvalidTest3() {
        task.setAcceptableDeviation(Double.POSITIVE_INFINITY);

    }
    
    @Test
    public void setAlternativeTest() {
        Task task2 = new Task("Hi", 1, 0);
        DateTimePeriod period = new DateTimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));

        try {
            task.addAlternative(task2);
            fail();
        } catch (IllegalStateException e) {}

        try {
            task.addAlternative(null);
            fail();
        } catch (IllegalStateException e) {}

        task.fail(period);

        try {
            task.addAlternative(task);
            fail();
        } catch (IllegalArgumentException e) {}

        task.addAlternative(task2);
        assertTrue(task.getAlternative() == task2);
    }

    @Test
    public void getDependencySetTest() {
        Task task2 = new Task("Hi", 1, 0);
        assertFalse(task.canHaveAsDependency(null));
        assertTrue(task.canHaveAsDependency(task2));
        task.addDependency(task2);
        task.addDependency(task2); // should be fine, but not add anything
        assertTrue(task.getDependencySet().contains(task2));
    }

    @Test
    public void finishTest() {
        assertFalse(task.isFinished());
        DateTimePeriod period = new DateTimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        task.execute();
        task.finish(period);
        assertTrue(task.isFinished());
    }

    @Test
    public void finishInvalidTest() {
        DateTimePeriod period = new DateTimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        Task dep = new Task("Hi", 1, 0);
        task.addDependency(dep);
        exception.expect(IllegalStateException.class);
        task.finish(period);

        Task task2 = new Task("Hi", 1, 0);
        task2.fail(period);
        exception.expect(IllegalStateException.class);
        task2.finish(period);

        Task task3 = new Task("Hi", 1, 0);
        task3.finish(period);
        exception.expect(IllegalStateException.class);
        task3.finish(period);
    }

    @Test
    public void failTest() {
        assertFalse(task.isFailed());
        DateTimePeriod period = new DateTimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        task.fail(period);
        assertTrue(task.isFailed());

        Task task2 = new Task("Hi", 1, 0);
        Task dep = new Task("Hi", 1, 0);
        task2.addDependency(dep);
        task2.fail(period);
        assertTrue(task.isFailed());
    }

    @Test
    public void failInvalidTest() {
        DateTimePeriod period = new DateTimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        task.fail(period);
        exception.expect(IllegalStateException.class);
        task.fail(period);

        Task task2 = new Task("Hi", 1, 0);
        task2.finish(period);
        exception.expect(IllegalStateException.class);
        task2.fail(period);
    }

    @Test
    public void isFinishedOrHasFinishedAlternativeTest() {
        DateTimePeriod period = new DateTimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));

        assertFalse(task.isFinishedOrHasFinishedAlternative());
        // can't finish a task that has not been started.
        // Has to be 'executing first
        // Hi to whomever this is, if not me.
        // also to me, of course.
        task.execute();
        task.finish(period);
        assertTrue(task.isFinishedOrHasFinishedAlternative());

        Task task2 = new Task("Hi", 1, 0);
        Task task3 = new Task("Hi2", 1, 0);
        task2.fail(period);
        task2.addAlternative(task3);
        assertFalse(task2.isFinishedOrHasFinishedAlternative());
        task3.execute();
        task3.finish(period);
        assertTrue(task2.isFinishedOrHasFinishedAlternative());
    }

    @Test
    public void wasFinishedOnTimeTest() {
        assertFalse(task.wasFinishedOnTime());

        Task task2 = new Task("Hi", 10, 0);
        DateTimePeriod period = new DateTimePeriod(LocalDateTime.MIN.plusMinutes(1), LocalDateTime.MIN.plusMinutes(11));
        task2.execute();
        task2.finish(period);
        assertTrue(task2.wasFinishedOnTime());

        Task task3 = new Task("Hi", 5, 1);
        task3.execute();
        task3.finish(period);
        assertTrue(task3.wasFinishedOnTime());

        Task task4 = new Task("Hi", 5, 0.5);
        task4.execute();
        task4.finish(period);
        assertFalse(task4.wasFinishedOnTime());

        Task task5 = new Task("Hi", 20, 0.1);
        task5.execute();
        task5.finish(period);
        assertFalse(task5.wasFinishedOnTime());
    }

    @Test
    public void wasFinishedEarlyTest() {
        assertFalse(task.wasFinishedEarly());

        Task task2 = new Task("Hi", 10, 0);
        DateTimePeriod period = new DateTimePeriod(LocalDateTime.MIN.plusMinutes(1), LocalDateTime.MIN.plusMinutes(11));
        task2.execute();
        task2.finish(period);
        assertFalse(task2.wasFinishedEarly());

        Task task3 = new Task("Hi", 5, 1);
        task3.execute();
        task3.finish(period);
        assertFalse(task3.wasFinishedEarly());

        Task task4 = new Task("Hi", 5, 0.5);
        task4.execute();
        task4.finish(period);
        assertFalse(task4.wasFinishedEarly());

        Task task5 = new Task("Hi", 20, 0.1);
        task5.execute();
        task5.finish(period);
        assertTrue(task5.wasFinishedEarly());
    }

    @Test
    public void wasFinishedLateTest() {
        assertFalse(task.wasFinishedLate());

        DateTimePeriod period = new DateTimePeriod(LocalDateTime.MIN.plusMinutes(1), LocalDateTime.MIN.plusMinutes(11));

        Task task2 = new Task("Hi", 10, 0);
        task2.execute();
        task2.finish(period);
        assertFalse(task2.wasFinishedLate());

        Task task3 = new Task("Hi", 5, 1);
        task3.execute();
        task3.finish(period);
        assertFalse(task3.wasFinishedLate());

        Task task4 = new Task("Hi", 5, 0.5);
        task4.execute();
        task4.finish(period);
        assertTrue(task4.wasFinishedLate());

        Task task5 = new Task("Hi", 20, 0.1);
        task5.execute();
        task5.finish(period);
        assertFalse(task5.wasFinishedLate());
    }

    @Test
    public void requirementTests() {
        Task task = new Task("task", 10, 1, null);
        assertEquals(0, task.getRequirements().size());

        Task task2 = new Task("task", 10, 1,
                new HashSet<>());
        assertEquals(0, task2.getRequirements().size());

        assertEquals(0, task2.getRecursiveRequirements().size());
    }

    @Test
    public void recursiveRequirementTests() {
        Task task0 = new Task("task", 10, 1,
                new HashSet<>());

        assertEquals(0, task0.getRecursiveRequirements().size());

        Task task1 = new Task("task", 10, 1,
                new HashSet<>(Arrays.asList(
                        new Requirement(1,
                                new ResourceType("res", new HashSet<>(), new HashSet<>(), false)
                        )
                        )
                ));
        assertEquals(1, task1.getRecursiveRequirements().size());

        ResourceType dependOn = new ResourceType("res", new HashSet<>(), new HashSet<>(), false);
        ResourceType depender = new ResourceType("res",
                new HashSet<>(Arrays.asList(dependOn)), new HashSet<>(), false);
        Task task2 = new Task("task", 10, 1,
                new HashSet<>(Arrays.asList(new Requirement(1, depender))
                ));
        assertEquals(2, task2.getRecursiveRequirements().size());
    }

}
