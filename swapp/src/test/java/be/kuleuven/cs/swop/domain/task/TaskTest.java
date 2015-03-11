package be.kuleuven.cs.swop.domain.task;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.domain.TimePeriod;


public class TaskTest {

    private Task             task;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        task = new Task("Desc", 100, 0.1);
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void canHaveAsDescriptionTest() {
        assertTrue(task.canHaveAsDescription("Testdescription"));
        assertFalse(task.canHaveAsDescription(null));
    }

    @Test
    public void setDescriptionTest() {
        String desc = "Testdescription";
        task.setDescription(desc);
        assertEquals(desc, task.getDescription());
    }

    @Test
    public void setDescriptionNullTest() {
        exception.expect(IllegalArgumentException.class);
        task.setDescription(null);
    }

    @Test
    public void canHaveAsEstimatedDurationTest() {
        assertTrue(task.canHaveAsEstimatedDuration(10));
        assertFalse(task.canHaveAsEstimatedDuration(-1));
        assertFalse(task.canHaveAsEstimatedDuration(Double.NaN));
        assertFalse(task.canHaveAsEstimatedDuration(Double.POSITIVE_INFINITY));

    }

    @Test
    public void setEstimatedDurationTest() {
        double dur = 10;
        task.setEstimatedDuration(dur);
        assertTrue(dur == task.getEstimatedDuration());

        exception.expect(IllegalArgumentException.class);
        task.setEstimatedDuration(-1);

        exception.expect(IllegalArgumentException.class);
        task.setEstimatedDuration(Double.NaN);

        exception.expect(IllegalArgumentException.class);
        task.setEstimatedDuration(Double.POSITIVE_INFINITY);
    }

    @Test
    public void canHaveAsDependencyTest() {
        Task t1 = new Task("task1", 50, 0);
        Task t2 = new Task("task2", 60, 0);
        Task t3 = new Task("task3", 70, 0);
        Task t4 = new Task("task4", 80, 0);
        Task t5 = new Task("task5", 90, 0);

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
         * Tree at this point: t1 / \ v v t4 <-- t2 | \ \ v \ t3 \ / v v t5
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
    public void addDependencyTest() {

        // Limited testing here because canHaveAsDependencyTest goes in depth
        exception.expect(IllegalArgumentException.class);
        task.addDependency(null);

        Task task2 = new Task("Hi", 1, 0);
        task.addDependency(task2);
        assertTrue(task2.getDependencySet().contains(task2));
    }

    @Test
    public void canHaveAsDeviationTest() {
        assertTrue(task.canHaveAsDeviation(0.5));
        assertTrue(task.canHaveAsDeviation(0));
        assertFalse(task.canHaveAsDeviation(-0.5));
        assertFalse(task.canHaveAsDeviation(Double.NaN));
        assertFalse(task.canHaveAsDeviation(Double.POSITIVE_INFINITY));

    }

    @Test
    public void setAcceptableDeviation() {
        task.setAcceptableDeviation(0.5);
        assertTrue(0.5 == task.getAcceptableDeviation());

        task.setAcceptableDeviation(0);
        assertTrue(0 == task.getAcceptableDeviation());

        exception.expect(IllegalArgumentException.class);
        task.setAcceptableDeviation(-0.5);

        exception.expect(IllegalArgumentException.class);
        task.setAcceptableDeviation(Double.NaN);

        exception.expect(IllegalArgumentException.class);
        task.setAcceptableDeviation(Double.POSITIVE_INFINITY);

    }

    @Test
    public void canHaveAsAlternativeTest() {
        Task task2 = new Task("Hi", 1, 0);
        TimePeriod period = new TimePeriod(new Date(1), new Date(2));
        assertFalse(task.canHaveAsAlternative(task2));

        task.fail(period);

        assertTrue(task.canHaveAsAlternative(task2));

        task2.fail(period);
        assertTrue(task.canHaveAsAlternative(task2));

        assertFalse(task.canHaveAsAlternative(null));

        // TODO check for loop
    }

    @Test
    public void setAlternativeTest() {
        Task task2 = new Task("Hi", 1, 0);
        TimePeriod period = new TimePeriod(new Date(1), new Date(2));

        exception.expect(IllegalArgumentException.class);
        task.canHaveAsAlternative(task2);

        exception.expect(IllegalArgumentException.class);
        task.setAlternative(null);

        task.fail(period);

        exception.expect(IllegalArgumentException.class);
        task.setAlternative(task);

        task.setAlternative(task2);
        assertTrue(task.getAlternative() == task2);
    }

    @Test
    public void canHaveBeenPerfomedDuringTest() {

    }

}
