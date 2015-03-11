package be.kuleuven.cs.swop.domain.task;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class TaskTest {

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

    @Test
    public void test() {
        // TODO test something
    }
    
    @Test
    public void canHaveAsDependencyTest() {
        Task t1 = new Task("task1",50,0);
        Task t2 = new Task("task2",60,0);
        Task t3 = new Task("task3",60,0);
        Task t4 = new Task("task4",60,0);
        Task t5 = new Task("task5",60,0);
        
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
        t3.addDependency(t5);
        t4.addDependency(t5);
        
        // Tests for dependency loops
        assertFalse(t2.canHaveAsDependency(t1));
        
        assertFalse(t4.canHaveAsDependency(t1));
        assertFalse(t4.canHaveAsDependency(t2));
        
        assertFalse(t5.canHaveAsDependency(t1));
        assertFalse(t5.canHaveAsDependency(t2));
        assertFalse(t5.canHaveAsDependency(t3));
        assertFalse(t5.canHaveAsDependency(t4));
        
    }

}
