package be.kuleuven.cs.swop.domain.company.task;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.Resource;


public class TaskTest {

    private Task             task;
    private Requirements reqs = new Requirements(new HashSet<Requirement>());
    private TaskInfo taskInfo = new TaskInfo("Desc", 100, 0.1, reqs, new HashSet<Task>()); 
    private Set<Resource> res = new HashSet<Resource>();
    private Set<Task> deps = new HashSet<Task>();


    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        task = new Task(taskInfo);
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
    
    @Test
    public void isFinishedOrHasFinishedAlternativeTest() {
        DateTimePeriod period = new DateTimePeriod(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));

        assertFalse(task.isFinishedOrHasFinishedAlternative());
        // can't finish a task that has not been started.
        // Has to be 'executing first
        // Hi to whomever this is, if not me.
        // also to me, of course.
        task.execute();
        task.plan(new TaskPlanning(period, res));
        task.finish();
        assertTrue(task.isFinishedOrHasFinishedAlternative());

        Task task2 = new Task(taskInfo);
        Task task3 = new Task(taskInfo);
        task2.plan(new TaskPlanning(period, res));
        task2.fail();
        task2.setAlternative(task3);
        assertFalse(task2.isFinishedOrHasFinishedAlternative());
        task3.execute();
        task3.plan(new TaskPlanning(period, res));
        task3.finish();
        assertTrue(task2.isFinishedOrHasFinishedAlternative());
    }

    @Test
    public void wasFinishedOnTimeTest() {
        assertFalse(task.wasFinishedOnTime());

        Task task2 = new Task(new TaskInfo("Hi",10,0, reqs, deps));
        DateTimePeriod period = new DateTimePeriod(LocalDateTime.MIN.plusMinutes(1), LocalDateTime.MIN.plusMinutes(11));
        task2.execute();
        task2.plan(new TaskPlanning(period,res));
        task2.finish();
        assertTrue(task2.wasFinishedOnTime());

        Task task3 = new Task(new TaskInfo("Hi",5,1, reqs, deps));
        task3.execute();
        task3.plan(new TaskPlanning(period,res));
        task3.finish();
        assertTrue(task3.wasFinishedOnTime());

        Task task4 = new Task(new TaskInfo("Hi", 5, 0.5, reqs, deps));
        task4.execute();
        task4.plan(new TaskPlanning(period,res));
        task4.finish();
        assertFalse(task4.wasFinishedOnTime());

        Task task5 = new Task(new TaskInfo("Hi", 20, 0.1, reqs, deps));
        task5.execute();
        task5.plan(new TaskPlanning(period,res));
        task5.finish();
        assertFalse(task5.wasFinishedOnTime());
    }

    @Test
    public void wasFinishedEarlyTest() {
        assertFalse(task.wasFinishedEarly());

        Task task2 = new Task(new TaskInfo("Hi",10,0, reqs, deps));
        DateTimePeriod period = new DateTimePeriod(LocalDateTime.MIN.plusMinutes(1), LocalDateTime.MIN.plusMinutes(11));
        task2.execute();
        task2.plan(new TaskPlanning(period,res));
        task2.finish();
        assertFalse(task2.wasFinishedEarly());

        Task task3 = new Task(new TaskInfo("Hi",5,1, reqs, deps));
        task3.execute();
        task3.plan(new TaskPlanning(period,res));
        task3.finish();
        assertFalse(task3.wasFinishedEarly());

        Task task4 = new Task(new TaskInfo("Hi", 5, 0.5, reqs, deps));
        task4.execute();
        task4.plan(new TaskPlanning(period,res));
        task4.finish();
        assertFalse(task4.wasFinishedEarly());

        Task task5 = new Task(new TaskInfo("Hi", 20, 0.1, reqs, deps));
        task5.execute();
        task5.plan(new TaskPlanning(period,res));
        task5.finish();
        assertTrue(task5.wasFinishedEarly());
    }

    @Test
    public void wasFinishedLateTest() {
        assertFalse(task.wasFinishedLate());

        DateTimePeriod period = new DateTimePeriod(LocalDateTime.MIN.plusMinutes(1), LocalDateTime.MIN.plusMinutes(11));

        Task task2 = new Task(new TaskInfo("Hi",10,0, reqs, deps));
        task2.execute();
        task2.plan(new TaskPlanning(period,res));
        task2.finish();
        assertFalse(task2.wasFinishedLate());

        Task task3 = new Task(new TaskInfo("Hi",5,1, reqs, deps));
        task3.execute();
        task3.plan(new TaskPlanning(period,res));
        task3.finish();
        assertFalse(task3.wasFinishedLate());

        Task task4 = new Task(new TaskInfo("Hi", 5, 0.5, reqs, deps));
        task4.execute();
        task4.plan(new TaskPlanning(period,res));
        task4.finish();
        assertTrue(task4.wasFinishedLate());

        Task task5 = new Task(new TaskInfo("Hi", 20, 0.1, reqs, deps));
        task5.execute();
        task5.plan(new TaskPlanning(period,res));
        task5.finish();
        assertFalse(task5.wasFinishedLate());
    }

}
