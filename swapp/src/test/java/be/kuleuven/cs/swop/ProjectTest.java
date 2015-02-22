package be.kuleuven.cs.swop;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class ProjectTest {
	
	Project simpleProject;
	Project otherProject;
	
	Task simpleTask;
	Task otherTask;
	Calendar calendar;
	
	@Before
	public void testSetup() {
		calendar = Calendar.getInstance();
		calendar.setTime(new Date()); // sets calendar time/date
		calendar.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
	    
		simpleProject = new Project("Test", "A test", calendar.getTime());
		otherProject = new Project("Other", "Another", calendar.getTime());
		simpleTask = new Task();
		otherTask = new Task();
	}
	
	// Title tests
	
	@Test
	public void testChangeTitleValid() {
		simpleProject.setTitle(VALID_PROJECT_TITLE);
		assertEquals(simpleProject.getTitle(), VALID_PROJECT_TITLE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testChangeTitleInvalid() {
		simpleProject.setTitle(INVALID_PROJECT_TITLE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testChangeTitleNull() {
		simpleProject.setTitle(null);
	}
	
	// Description tests
	
	@Test
	public void testChangeDescriptionValid() {
		simpleProject.setDescription(VALID_PROJECT_DESCRIPTION);
		assertEquals(simpleProject.getDescription(), VALID_PROJECT_DESCRIPTION);
		
		simpleProject.setDescription("");
		assertEquals(simpleProject.getDescription(), "");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testChangeDescriptionNull() {
		simpleProject.setDescription(null);
	}
	
	// Task tests
	@Test(expected = IllegalArgumentException.class)
	public void testAddTaskNull() {
		simpleProject.addTask(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddTaskOfOther() {
		otherProject.addTask(simpleTask);
		simpleProject.addTask(simpleTask);
	}
	
	@Test
	public void testAddTaskValid() {
		simpleProject.addTask(simpleTask);
	}
	
	// Status tests
	@Test
	public void testStatus_AllTasksFinished() {
		simpleProject.addTask(simpleTask);
		simpleProject.addTask(otherTask);
		simpleTask.setStatus(TaskStatus.FINISHED);
		otherTask.setStatus(TaskStatus.FINISHED);
		
		assertEquals(simpleProject.getStatus(), ProjectStatus.FINISHED);
	}
	
	@Test
	public void testStatus_AllTasksFailed() {
		simpleProject.addTask(simpleTask);
		simpleProject.addTask(otherTask);
		simpleTask.setStatus(TaskStatus.FAILED);
		otherTask.setStatus(TaskStatus.FAILED);
		
		assertEquals(simpleProject.getStatus(), ProjectStatus.FINISHED);
	}
	
	@Test
	public void testStatus_SomeTasksFinishedSomeTasksFailed() {
		simpleProject.addTask(simpleTask);
		simpleProject.addTask(otherTask);
		simpleTask.setStatus(TaskStatus.FINISHED);
		otherTask.setStatus(TaskStatus.FAILED);
		
		assertEquals(simpleProject.getStatus(), ProjectStatus.FINISHED);
	}
	
	@Test
	public void testStatus_NoTasks() {
		assertEquals(simpleProject.getStatus(), ProjectStatus.ONGOING);
	}
	
	@Test
	public void testStatus_SomeTasksFinishedSomeTasksOngoing() {
		simpleProject.addTask(simpleTask);
		simpleProject.addTask(otherTask);
		simpleTask.setStatus(TaskStatus.FINISHED);
		otherTask.setStatus(TaskStatus.AVAILABLE);
		
		assertEquals(simpleProject.getStatus(), ProjectStatus.ONGOING);
	}
	
	// Due time tests
	
	@Test(expected = IllegalArgumentException.class)
	public void testChangeDueTimeNull() {
		simpleProject.setDueTime(null);
	}
	
	@Test
	public void testChangeDueTimeAfter() {
		calendar.setTime(simpleProject.getCreationTime());
		calendar.add(Calendar.DAY_OF_MONTH, 5);
		
		simpleProject.setDueTime(calendar.getTime());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testChangeDueTimeSame() {
		simpleProject.setDueTime(simpleProject.getCreationTime());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testChangeDueTimeBefore() {
		calendar.setTime(simpleProject.getCreationTime());
		calendar.add(Calendar.DAY_OF_MONTH, -5);
		
		simpleProject.setDueTime(calendar.getTime());
	}
	
	static final String VALID_PROJECT_TITLE = "A valid title";
	static final String INVALID_PROJECT_TITLE = "";
	static final String VALID_PROJECT_DESCRIPTION = "A valid description";
}
