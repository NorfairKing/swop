package be.kuleuven.cs.swop.domain.company.task;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.Company;
import be.kuleuven.cs.swop.domain.company.ConflictingPlannedTaskException;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;

public class TaskStatusTest {

    Company company;
    Developer developer;
    Project project;
    Task task;
    DateTimePeriod period = new DateTimePeriod(LocalDateTime.of(2015, 1, 1, 8, 0),LocalDateTime.of(2015, 1, 1, 9, 0));
    Set<Resource> res = new HashSet<Resource>();
     
    
    @Rule
	public ExpectedException exception = ExpectedException.none();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	    
        task = new Task(new TaskInfo("t description", 60, 0.5, new Requirements(new HashSet<>()), new HashSet<>()));
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testIncomplete() {
        assertFalse(task.wasFinishedEarly());
        assertFalse(task.wasFinishedLate());
        assertFalse(task.wasFinishedOnTime());
	}
	
	@Test
	public void ongoingBooleansTest(){
		TaskStatus status1 = new UnstartedStatus(task);
		assertTrue(status1.canFail());
		assertTrue(status1.canExecute());
		assertFalse(status1.canFinish());
		
		assertFalse(status1.isExecuting());
		assertFalse(status1.isFailed());
		assertFalse(status1.isFinished());
		assertFalse(status1.isFinal());
	}
	
	@Test
	public void executingBooleansTest(){
		TaskStatus status1 = new ExecutingStatus(task);
		assertTrue(status1.canFail());
		assertFalse(status1.canExecute());
		assertTrue(status1.canFinish());
		
		assertTrue(status1.isExecuting());
		assertFalse(status1.isFailed());
		assertFalse(status1.isFinished());
		assertFalse(status1.isFinal());
	}
	
	@Test
	public void finishedBooleansTest(){
		TaskStatus status1 = new FinishedStatus(task);
		assertFalse(status1.canFail());
		assertFalse(status1.canExecute());
		assertFalse(status1.canFinish());
		
		assertFalse(status1.isExecuting());
		assertFalse(status1.isFailed());
		assertTrue(status1.isFinished());
		assertTrue(status1.isFinal());
	}
	
	@Test
	public void failedBooleansTest(){
		TaskStatus status1 = new FailedStatus(task);
		assertFalse(status1.canFail());
		assertFalse(status1.canExecute());
		assertFalse(status1.canFinish());
		
		assertFalse(status1.isExecuting());
		assertTrue(status1.isFailed());
		assertFalse(status1.isFinished());
		assertTrue(status1.isFinal());
	}
	
	@Test(expected = IllegalStateException.class)
	public void executeExecutingStatusTest(){
		TaskStatus status1 = new ExecutingStatus(task);
		status1.execute();
	}
	
	@Test(expected = IllegalStateException.class)
	public void executeFinishedStatusTest(){
		TaskStatus status1 = new FinishedStatus(task);
		status1.execute();
	}
	
	@Test(expected = IllegalStateException.class)
	public void executeFailedStatusTest(){
		TaskStatus status1 = new FailedStatus(task);
		status1.execute();
	}
	
	@Test
	public void testExecuteThenFinish() throws ConflictingPlannedTaskException {
	    // can't finish task that isn't started
	    task.plan(new TaskPlanning(period,res ));
        try  {
            task.finish();
            fail();
        }
        catch (IllegalStateException e) { }
	    
	    task.execute();
	    
	    assertFalse(task.isFinished());
        assertFalse(task.isFinal());
	    assertFalse(task.isFailed());
	    assertEquals(null, task.getAlternative());
	    
	    task.finish();
	    
	    assertTrue(task.isFinished());
	    assertTrue(task.isFinal());
	    assertTrue(task.isFinishedOrHasFinishedAlternative());
	    assertTrue(task.wasFinishedOnTime());
	    assertFalse(task.wasFinishedLate());
	    assertEquals(task.getAlternative(), null);
	    assertFalse(task.wasFinishedEarly());
        assertFalse(task.isFailed());
        assertFalse(task.canFinish());
        assertFalse(task.isExecuting());
        
        try  {
            task.fail();
            fail();
        }
        catch (IllegalStateException e) { }
        
        try  {
            task.finish();
            fail();
        }
        catch (IllegalStateException e) { }
	}
	
	@Test
	public void testExecuteFinished() throws ConflictingPlannedTaskException {
	    task.execute();
        task.finish();
        try {
            task.execute();
            fail();
        }
        catch (IllegalStateException e) { }
	}
	
	@Test
	public void testFinishSetAlternative() throws ConflictingPlannedTaskException {
        task.execute();
        task.finish();
        try {
            task.execute();
            fail();
        }
        catch (IllegalStateException e) { }
	}
	
    @Test
    public void testFailAndSetAlternative() {
        Task alternative = new Task(new TaskInfo("t description", 100, 0.5, new Requirements(new HashSet<>()), new HashSet<>()));
        Set<Task> deps = new HashSet<Task>();
        deps.add(task);
        Task dep = new Task(new TaskInfo("t description", 100, 0.5, new Requirements(new HashSet<>()), deps));

        // can't set alternative if it hasn't failed yet
        try {
            task.setAlternative(alternative);
            fail();
        }
        catch (IllegalStateException e) { }
        
        assertEquals(null, task.getAlternative());
        
        task.fail();

        assertEquals(null, task.getAlternative());
        
        // can't set null as alternative
        try {
            task.setAlternative(null);
            fail();
        }
        catch (IllegalArgumentException e) { }
        
        // can't add itself as alterantive
        try {
            task.setAlternative(task);
            fail();
        }
        catch (IllegalArgumentException e) { }
        
        // can't add dependency as alternative
        try {
            task.setAlternative(dep);
            fail();
        }
        catch (IllegalArgumentException e) { }
        
        assertTrue(task.isFailed());
        assertTrue(task.isFinal());
        assertFalse(task.isFinished());
        assertFalse(task.isFinishedOrHasFinishedAlternative());
        
        task.setAlternative(alternative);
        
        assertTrue(task.isFailed());
        assertFalse(task.isFinished());
        assertFalse(task.isFinishedOrHasFinishedAlternative());
        assertNotEquals(null, task.getAlternative());
        assertEquals(task.getAlternative(), alternative);

        assertFalse(task.wasFinishedOnTime());
        assertFalse(task.wasFinishedEarly());
        assertFalse(task.wasFinishedLate());
        
        // can't set alternative twice
        try {
            task.setAlternative(alternative);
            fail();
        }
        catch (IllegalArgumentException e) { }
    }
	
//	@Test
//	public void testViaPlanMan() throws ConflictingPlannedTaskException {
//	    // can't start executing yet, because it isn't planned yet
//	    try {
//	        company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
//	        fail();
//	    }
//	    catch (IllegalStateException e) { }
//	    
//	    company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0),
//	            new HashSet<>(),
//	            new HashSet<>(Arrays.asList(developer)));
//	    
//	    company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
//        
//	    assertTrue(task.isExecuting());
//	}
//	
//	@Test
//    public void testViaPlanManFinish() throws ConflictingPlannedTaskException {
//        assertFalse(task.canFinish());
//        assertFalse(task.isFinal());
//        assertFalse(task.isFinished());
//        assertFalse(task.isFailed());
//	    
//        company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0),
//                new HashSet<>(),
//                new HashSet<>(Arrays.asList(developer)));
//        
//        company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
//        
//        assertTrue(task.isExecuting());
//        assertTrue(task.canFinish());
//        
//        company.finishTask(task, new DateTimePeriod(LocalDateTime.of(2015, 1, 1, 8, 0), LocalDateTime.of(2015, 1, 1, 9, 0)));
//        
//        assertTrue(task.isFinished());
//    }
//	
//	@Test
//    public void testViaPlanManFail() throws ConflictingPlannedTaskException {
//        company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0),
//                new HashSet<>(),
//                new HashSet<>(Arrays.asList(developer)));
//        
//        company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
//        
//        assertTrue(task.isExecuting());
//        
//        company.failTask(task, new DateTimePeriod(LocalDateTime.of(2015, 1, 1, 8, 0), LocalDateTime.of(2015, 1, 1, 9, 0)));
//        
//        assertTrue(task.isFailed());
//    }
//	
//
//    
//    @Test
//    public void testViaPlanManOngoing() throws ConflictingPlannedTaskException {
//        assertFalse(task.isExecuting());
//        assertFalse(task.isFailed());
//        assertFalse(task.isFinished());
//        
//        company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0),
//                new HashSet<>(),
//                new HashSet<>(Arrays.asList(developer)));
//        
//        assertFalse(task.isExecuting());
//        assertFalse(task.isFailed());
//        assertFalse(task.isFinished());
//    }
//    
//    @Test(expected = IllegalArgumentException.class)
//    public void setTaskInvalidTest(){
//    	TaskStatus status1 = new UnstartedStatus(task);
//    	status1.setDelegationTask(null);
//    }


}
