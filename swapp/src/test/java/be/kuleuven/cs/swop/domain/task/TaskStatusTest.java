package be.kuleuven.cs.swop.domain.task;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.domain.task.FailedStatus;
import be.kuleuven.cs.swop.domain.task.FinishedStatus;
import be.kuleuven.cs.swop.domain.task.OngoingStatus;

public class TaskStatusTest {
	private FailedStatus failed;
	private FinishedStatus finished;
	private OngoingStatus unavailable;

    private Task             task;

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
        task = new Task("Desc", 100, 0.1);
		failed = new FailedStatus(task);
		finished = new FinishedStatus(task);
		unavailable = new OngoingStatus(task);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void isFinishedTest(){
		assertFalse(failed.isFinished());
		assertTrue(finished.isFinished());
		assertFalse(unavailable.isFinished());

	}
	
	@Test
	public void isFailedTest(){
		assertTrue(failed.isFailed());
		assertFalse(finished.isFailed());
		assertFalse(unavailable.isFailed());
	}
	
	@Test
	public void canFinishTest(){
		assertFalse(failed.canFinish());
		assertFalse(finished.canFinish());
		assertTrue(unavailable.canFinish());
	}
	
	@Test
	public void canFailTest(){
		assertFalse(failed.canFail());
		assertFalse(finished.canFail());
		assertTrue(unavailable.canFail());
	}
	
	@Test
	public void isFinalTest(){
		assertTrue(failed.isFinal());
		assertTrue(finished.isFinal());
		assertFalse(unavailable.isFinal());
	}

}
