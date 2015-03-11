package be.kuleuven.cs.swop.domain.task;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class TaskTest {
	private Task task;

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
		task = new Task("Desc",100,0.1);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void canHaveAsDescriptionTest() {
		assertTrue(task.canHaveAsDescription("Testdescription"));
		assertFalse(task.canHaveAsDescription(null));
	}

	@Test
	public void setDescriptionTest() {
		task.setDescription("Testdescription");
	}

	@Test
	public void setDescriptionNullTest() {
		exception.expect(IllegalArgumentException.class);
		task.setDescription(null);        
	}

}
