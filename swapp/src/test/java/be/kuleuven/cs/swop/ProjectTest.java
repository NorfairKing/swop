package be.kuleuven.cs.swop;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProjectTest {
	
	Project simpleProject;
	Calendar calendar;
	
	@Before
	public void testSetup() {
		calendar = Calendar.getInstance();
		calendar.setTime(new Date()); // sets calendar time/date
		calendar.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
	    
		simpleProject = new Project("Test", "A test", calendar.getTime());
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
	
	// Status tests
	
	@Test
	public void testChangeStatusValid() {
		simpleProject.setStatus(ProjectStatus.FINISHED);
		assertEquals(simpleProject.getStatus(), ProjectStatus.FINISHED);
		
		simpleProject.setStatus(ProjectStatus.ONGOING);
		assertEquals(simpleProject.getStatus(), ProjectStatus.ONGOING);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testChangeStatusNull() {
		simpleProject.setStatus(null);
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
