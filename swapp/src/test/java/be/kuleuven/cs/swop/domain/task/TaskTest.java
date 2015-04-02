package be.kuleuven.cs.swop.domain.task;


import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.domain.TimePeriod;


public class TaskTest {
    private double delta = 10e-15;
    private Task             task;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        task = new Task("Desc", 100, 0.1);
    }

    @After
    public void tearDown() throws Exception {}

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

    @Test
    public void setDescriptionNullTest() {
        exception.expect(IllegalArgumentException.class);
        task.setDescription(null);
    }

    @Test
    public void canHaveAsEstimatedDurationTest() {
        assertTrue(task.canHaveAsEstimatedDuration(10));
        assertFalse(task.canHaveAsEstimatedDuration(-1));
        assertFalse(task.canHaveAsEstimatedDuration(Double.NaN));
        assertFalse(task.canHaveAsEstimatedDuration(Double.POSITIVE_INFINITY));

    }

    @Test
    public void setEstimatedDurationTest() {
        long dur = 10;
        task.setEstimatedDuration(dur);
        assertTrue(dur == task.getEstimatedDuration());

        try{
            task.setEstimatedDuration(-1);
            fail();
        }catch(IllegalArgumentException e){}
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

        /*
         * Tree at this point: t1 / \ v v t4 <-- t2 | \ \ v \ t3 \ / v v t5
         */

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
    public void setAcceptableDeviation() {
        task.setAcceptableDeviation(0.5);
        assertEquals(0.5, task.getAcceptableDeviation(), delta);

        task.setAcceptableDeviation(0);
        assertEquals(0,task.getAcceptableDeviation(),delta);

        try{
            task.setAcceptableDeviation(-0.5);
            fail();
        }catch(IllegalArgumentException e){}

        try{
            task.setAcceptableDeviation(Double.NaN);
            fail();
        }catch(IllegalArgumentException e){}

        try{
            task.setAcceptableDeviation(Double.POSITIVE_INFINITY);
            fail();
        }catch(IllegalArgumentException e){}

    }

    @Test
    public void canHaveAsAlternativeTest() {
        Task task2 = new Task("Hi", 1, 0);
        TimePeriod period = new TimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        assertFalse(task.canHaveAsAlternative(task2));

        task.fail(period);

        assertTrue(task.canHaveAsAlternative(task2));

        task2.fail(period);
        assertTrue(task.canHaveAsAlternative(task2));

        assertFalse(task.canHaveAsAlternative(null));

        task = new Task("Hi", 1, 0);
        task2 = new Task("Hi", 1, 0);
        Task task3 = new Task("Hi", 1, 0);
        Task task4 = new Task("Hi", 1, 0);
        task3.addDependency(task);
        task2.addDependency(task3);
        task4.addDependency(task);
        task.fail(period);
        assertFalse(task.canHaveAsAlternative(task2));
        assertFalse(task.canHaveAsAlternative(task4));

    }

    @Test
    public void setAlternativeTest() {
        Task task2 = new Task("Hi", 1, 0);
        TimePeriod period = new TimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));

        try{
            task.setAlternative(task2);
            fail();
        }catch(IllegalArgumentException e){}

        try{
            task.setAlternative(null);
            fail();
        }catch(IllegalArgumentException e){}

        task.fail(period);


        try{
            task.setAlternative(task);
            fail();
        }catch(IllegalArgumentException e){}

        task.setAlternative(task2);
        assertTrue(task.getAlternative() == task2);
    }

    @Test
    public void canHaveBeenPerfomedDuringTest() {
        TimePeriod period = new TimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        assertTrue(task.canHaveBeenPerfomedDuring(period));
        assertFalse(task.canHaveBeenPerfomedDuring(null));
        task.finish(period);
        assertFalse(task.canHaveBeenPerfomedDuring(period));
        assertTrue(period == task.getPerformedDuring());
    }

    @Test
    public void getDependencySetTest(){
        Task task2 = new Task("Hi",1,0);
        task.addDependency(task2);
        assertTrue(task.getDependencySet().contains(task2));
    }

    @Test
    public void canHaveAsStatusTest(){
        assertFalse(task.canHaveAsStatus(null));
        assertTrue(task.canHaveAsStatus(new OngoingStatus(task)));
        assertTrue(task.canHaveAsStatus(new FinishedStatus(task)));
        assertTrue(task.canHaveAsStatus(new FailedStatus(task)));

    }

    @Test
    public void finishTest(){
        assertFalse(task.isFinished());
        TimePeriod period = new TimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        task.finish(period);
        assertTrue(task.isFinished());
    }

    @Test
    public void finishInvalidTest(){
        TimePeriod period = new TimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        Task dep = new Task("Hi",1,0);
        task.addDependency(dep);
        exception.expect(IllegalStateException.class);
        task.finish(period);

        Task task2 = new Task("Hi",1,0);
        task2.fail(period);
        exception.expect(IllegalStateException.class);
        task2.finish(period);

        Task task3 = new Task("Hi",1,0);
        task3.finish(period);
        exception.expect(IllegalStateException.class);
        task3.finish(period);
    }

    @Test
    public void failTest(){
        assertFalse(task.isFailed());
        TimePeriod period = new TimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        task.fail(period);
        assertTrue(task.isFailed());

        Task task2 = new Task("Hi",1,0);
        Task dep = new Task("Hi",1,0);
        task2.addDependency(dep);
        task2.fail(period);
        assertTrue(task.isFailed());
    }

    @Test
    public void failInvalidTest(){
        TimePeriod period = new TimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        task.fail(period);
        exception.expect(IllegalStateException.class);
        task.fail(period);

        Task task2 = new Task("Hi",1,0);
        task2.finish(period);
        exception.expect(IllegalStateException.class);
        task2.fail(period);
    }

    @Test
    public void isFinishedOrHasFinishedAlternativeTest(){
        TimePeriod period = new TimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));

        assertFalse(task.isFinishedOrHasFinishedAlternative());
        task.finish(period);
        assertTrue(task.isFinishedOrHasFinishedAlternative());

        Task task2 = new Task("Hi",1,0);
        Task task3 = new Task("Hi2",1,0);
        task2.fail(period);
        task2.setAlternative(task3);
        assertFalse(task2.isFinishedOrHasFinishedAlternative());
        task3.finish(period);
        assertTrue(task2.isFinishedOrHasFinishedAlternative());
    }

    @Test
    public void wasFinishedOnTimeTest(){
        assertFalse(task.wasFinishedOnTime());

        Task task2 = new Task("Hi",10,0);
        TimePeriod period = new TimePeriod(LocalDateTime.MIN.plusMinutes(1), LocalDateTime.MIN.plusMinutes(11));
        task2.finish(period);
        assertTrue(task2.wasFinishedOnTime());

        Task task3 = new Task("Hi",5,1);
        task3.finish(period);
        assertTrue(task3.wasFinishedOnTime());

        Task task4 = new Task("Hi",5,0.5);
        task4.finish(period);
        assertFalse(task4.wasFinishedOnTime());

        Task task5 = new Task("Hi",20,0.1);
        task5.finish(period);
        assertFalse(task5.wasFinishedOnTime());
    }

    @Test
    public void wasFinishedEarlyTest(){
        assertFalse(task.wasFinishedEarly());

        Task task2 = new Task("Hi",10,0);
        TimePeriod period = new TimePeriod(LocalDateTime.MIN.plusMinutes(1), LocalDateTime.MIN.plusMinutes(11));
        task2.finish(period);
        assertFalse(task2.wasFinishedEarly());

        Task task3 = new Task("Hi",5,1);
        task3.finish(period);
        assertFalse(task3.wasFinishedEarly());

        Task task4 = new Task("Hi",5,0.5);
        task4.finish(period);
        assertFalse(task4.wasFinishedEarly());

        Task task5 = new Task("Hi",20,0.1);
        task5.finish(period);
        assertTrue(task5.wasFinishedEarly());
    }

    @Test
    public void wasFinishedLateTest(){
        assertFalse(task.wasFinishedLate());

        TimePeriod period = new TimePeriod(LocalDateTime.MIN.plusMinutes(1), LocalDateTime.MIN.plusMinutes(11));

        Task task2 = new Task("Hi",10,0);
        task2.finish(period);
        assertFalse(task2.wasFinishedLate());

        Task task3 = new Task("Hi",5,1);
        task3.finish(period);
        assertFalse(task3.wasFinishedLate());

        Task task4 = new Task("Hi",5,0.5);
        task4.finish(period);
        assertTrue(task4.wasFinishedLate());

        Task task5 = new Task("Hi",20,0.1);
        task5.finish(period);
        assertFalse(task5.wasFinishedLate());
    }

}
