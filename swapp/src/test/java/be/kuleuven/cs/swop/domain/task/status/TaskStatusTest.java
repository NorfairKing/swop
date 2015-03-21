package be.kuleuven.cs.swop.domain.task.status;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TaskStatusTest {
	private AvailableStatus available;
	private FailedStatus failed;
	private FinishedStatus finished;
	private UnavailableStatus unavailable;


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
		available = new AvailableStatus();
		failed = new FailedStatus();
		finished = new FinishedStatus();
		unavailable = new UnavailableStatus();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void isFinishedTest(){
		assertFalse(available.isFinished());
		assertFalse(failed.isFinished());
		assertTrue(finished.isFinished());
		assertFalse(unavailable.isFinished());

	}
	
	@Test
	public void isFailedTest(){
		assertFalse(available.isFailed());
		assertTrue(failed.isFailed());
		assertFalse(finished.isFailed());
		assertFalse(unavailable.isFailed());
	}
	
	@Test
	public void canFinishTest(){
		assertTrue(available.canFinish());
		assertFalse(failed.canFinish());
		assertFalse(finished.canFinish());
		assertFalse(unavailable.canFinish());
	}
	
	@Test
	public void canFailTest(){
		assertTrue(available.canFail());
		assertFalse(failed.canFail());
		assertFalse(finished.canFail());
		assertTrue(unavailable.canFail());
	}
	
	@Test
	public void isFinalTest(){
		assertFalse(available.isFinal());
		assertTrue(failed.isFinal());
		assertTrue(finished.isFinal());
		assertFalse(unavailable.isFinal());
	}

}
