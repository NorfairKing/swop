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
    public void canHaveAsDependencyTest() {
        Task t1 = new Task("task1",50,0);
        Task t2 = new Task("task2",60,0);
        Task t3 = new Task("task3",70,0);
        Task t4 = new Task("task4",80,0);
        Task t5 = new Task("task5",90,0);
        
        // null can never be a depencency
        assertFalse(t1.canHaveAsDependency(null));
        
        // A task cannot depend on itself
        assertFalse(t1.canHaveAsDependency(t1));
        
        // Some valid cases
        assertTrue(t1.canHaveAsDependency(t2));
        assertTrue(t1.canHaveAsDependency(t4));
        
        t1.addDependency(t2);
        t1.addDependency(t4);
        
        assertTrue(t2.canHaveAsDependency(t3));
        assertTrue(t2.canHaveAsDependency(t3));
        
        t2.addDependency(t3);
        t2.addDependency(t4);
        
        assertTrue(t3.canHaveAsDependency(t5));
        assertTrue(t4.canHaveAsDependency(t5));
        t3.addDependency(t5);
        t4.addDependency(t5);
        
        /*
         * Tree at this point:
         *      t1
         *     /  \
         *    v    v
         *   t4 <-- t2
         *   |       \
         *    \       v
         *     \      t3
         *      \    /
         *       v  v
         *        t5
         * 
         */
        
        // Tests for dependency loops
        assertFalse(t2.canHaveAsDependency(t1));
        
        assertFalse(t4.canHaveAsDependency(t1));
        assertFalse(t4.canHaveAsDependency(t2));
        
        assertFalse(t5.canHaveAsDependency(t1));
        assertFalse(t5.canHaveAsDependency(t2));
        assertFalse(t5.canHaveAsDependency(t3));
        assertFalse(t5.canHaveAsDependency(t4));
        
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
