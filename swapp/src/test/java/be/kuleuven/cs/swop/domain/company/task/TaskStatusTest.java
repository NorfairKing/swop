package be.kuleuven.cs.swop.domain.company.task;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.Company;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;

public class TaskStatusTest {

    Company company;
    Developer developer;
    Project project;
    Task task;
    
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
	    company = new Company();
	    developer = company.createDeveloper("Andy");
        project = company.createProject("title", "description", LocalDateTime.of(2015, 1, 1, 8, 0), LocalDateTime.of(2015, 1, 1, 16, 0));
        task = company.createTaskFor(project, "t description", 100, 0.5, new HashSet<>(), new HashSet<>());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testIncomplete() {
        assertFalse(task.wasFinishedEarly());
        assertFalse(task.wasFinishedLate());
        assertFalse(task.wasFinishedOnTime());
        assertEquals(null, task.getPerformedDuring());
        assertEquals(
                LocalDateTime.of(2015, 1, 1, 8, 0).plusMinutes(100),
                task.getEstimatedOrRealFinishDate(LocalDateTime.of(2015, 1, 1, 8, 0))
            );
	}
	
	@Test
	public void testExecuteThenFinish() {
	    DateTimePeriod period = new DateTimePeriod(
                LocalDateTime.of(2015, 1, 1, 8, 0),
                LocalDateTime.of(2015, 1, 1, 9, 0)
                );
	    
	    // can't finish task that isn't started
	    try {
	        company.finishTask(task,
	            new DateTimePeriod(
	                    LocalDateTime.of(2015, 1, 1, 8, 0),
	                    LocalDateTime.of(2015, 1, 1, 9, 0)
	                    )
	            );
	    }
	    catch (IllegalStateException e) { }
	    
	    company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0),
	            new HashSet<>(), new HashSet<>(Arrays.asList(developer)));
	    company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
	    
	    try {
	        company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
	    }
	    catch (IllegalStateException e) { }
	    
	    assertFalse(task.isFinished());
        assertFalse(task.isFinal());
	    assertFalse(task.isFailed());
	    assertEquals(null, task.getAlternative());
	    
	    try  {
	        company.finishTask(task, null);
            fail();
        }
        catch (IllegalArgumentException e) { }
	    
	    company.finishTask(task, period);
	    
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
            task.fail(period);
            fail();
        }
        catch (IllegalStateException e) { }
        
        try  {
            task.finish(period);
            fail();
        }
        catch (IllegalStateException e) { }
        
        assertEquals(task.getEstimatedOrRealFinishDate(LocalDateTime.of(2016, 1, 1, 9, 0)), LocalDateTime.of(2015, 1, 1, 9, 0));
	}
	
	@Test
	public void testExecuteFinished() {
	    company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0),
                new HashSet<>(), new HashSet<>(Arrays.asList(developer)));
        company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
        
        company.finishTask(task, 
                new DateTimePeriod(
                        LocalDateTime.of(2015, 1, 1, 8, 0),
                        LocalDateTime.of(2015, 1, 1, 9, 0)
                        )
                );
        
        try {
            company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
            fail();
        }
        catch (IllegalStateException e) { }
	}
	
	@Test
	public void testFinishSetAlternative() {
	    company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0),
                new HashSet<>(), new HashSet<>(Arrays.asList(developer)));
        company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
        company.finishTask(task, 
                new DateTimePeriod(
                        LocalDateTime.of(2015, 1, 1, 8, 0),
                        LocalDateTime.of(2015, 1, 1, 9, 0)
                        )
                );
        
        try {
            task.addAlternative(new Task("", 10, 10));
        }
        catch (IllegalStateException e) { }
	}
	
    @Test
    public void testFailAndSetAlternative() {
        Task alternative = new Task("random", 12, 1);
        
        // can't set alternative if it hasn't failed yet
        try {
            task.addAlternative(alternative);
            fail();
        }
        catch (IllegalStateException e) { }
        
        assertEquals(null, task.getAlternative());
        
        company.failTask(task,
                new DateTimePeriod(LocalDateTime.of(2015, 1, 1, 8, 0),
                        LocalDateTime.of(2015, 1, 1, 9, 0)));

        assertEquals(null, task.getAlternative());
        
        // can't set null as alternative
        try {
            task.addAlternative(null);
            fail();
        }
        catch (IllegalArgumentException e) { }
        
        // can't add itself as alterantive
        try {
            task.addAlternative(task);
            fail();
        }
        catch (IllegalArgumentException e) { }
        
        // can't add dependency as alternative
        try {
            Task temp = new Task("random", 12, 1);
            temp.addDependency(task);
            task.addAlternative(temp);
            fail();
        }
        catch (IllegalArgumentException e) { }
        
        assertTrue(task.isFailed());
        assertTrue(task.isFinal());
        assertFalse(task.isFinished());
        assertFalse(task.isFinishedOrHasFinishedAlternative());
        assertEquals(
                task.getEstimatedOrRealFinishDate(LocalDateTime.of(2015, 1, 1, 9, 0)),
                LocalDateTime.of(2015, 1, 1, 9, 0)
                );
        
        task.addAlternative(alternative);
        
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
            task.addAlternative(new Task("alt", 12, 3));
            fail();
        }
        catch (IllegalArgumentException e) { }
    }
	
	@Test
	public void testViaPlanMan() {
	    // can't start executing yet, because it isn't planned yet
	    try {
	        company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
	        fail();
	    }
	    catch (IllegalStateException e) { }
	    
	    company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0),
	            new HashSet<>(),
	            new HashSet<>(Arrays.asList(developer)));
	    
	    company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
        
	    assertTrue(task.isExecuting());
	}
	
	@Test
    public void testViaPlanManFinish() {
        assertFalse(task.canFinish());
        assertFalse(task.isFinal());
        assertFalse(task.isFinished());
        assertFalse(task.isFailed());
	    
        company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0),
                new HashSet<>(),
                new HashSet<>(Arrays.asList(developer)));
        
        company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
        
        assertTrue(task.isExecuting());
        assertTrue(task.canFinish());
        
        company.finishTask(task, new DateTimePeriod(LocalDateTime.of(2015, 1, 1, 8, 0), LocalDateTime.of(2015, 1, 1, 9, 0)));
        
        assertTrue(task.isFinished());
    }
	
	@Test
    public void testViaPlanManFail() {
        company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0),
                new HashSet<>(),
                new HashSet<>(Arrays.asList(developer)));
        
        company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
        
        assertTrue(task.isExecuting());
        
        company.failTask(task, new DateTimePeriod(LocalDateTime.of(2015, 1, 1, 8, 0), LocalDateTime.of(2015, 1, 1, 9, 0)));
        
        assertTrue(task.isFailed());
    }
	

    
    @Test
    public void testViaPlanManOngoing() {
        assertFalse(task.isExecuting());
        assertFalse(task.isFailed());
        assertFalse(task.isFinished());
        
        company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0),
                new HashSet<>(),
                new HashSet<>(Arrays.asList(developer)));
        
        assertFalse(task.isExecuting());
        assertFalse(task.isFailed());
        assertFalse(task.isFinished());
    }


}
