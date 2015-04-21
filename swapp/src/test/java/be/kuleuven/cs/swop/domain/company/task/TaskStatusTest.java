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
        company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0),
                new HashSet<>(),
                new HashSet<>(Arrays.asList(developer)));
        
        company.startExecutingTask(task, LocalDateTime.of(2015, 1, 1, 8, 0), developer);
        
        assertTrue(task.isExecuting());
        
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
