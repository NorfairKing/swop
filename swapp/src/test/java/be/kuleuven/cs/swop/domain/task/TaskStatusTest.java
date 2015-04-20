package be.kuleuven.cs.swop.domain.task;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.domain.company.task.FailedStatus;
import be.kuleuven.cs.swop.domain.company.task.FinishedStatus;
import be.kuleuven.cs.swop.domain.company.task.OngoingStatus;
import be.kuleuven.cs.swop.domain.company.task.Task;

public class TaskStatusTest {

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
	}

	@After
	public void tearDown() throws Exception {
	}
	


}
