package be.kuleuven.cs.swop.domain.project;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.TaskWrapper;
import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.Timekeeper;
import be.kuleuven.cs.swop.domain.task.Task;


public class ProjectTest {

    private static long      milisPerMinute = 60 * 1000;
    private static long      milisPerHour   = 60 * milisPerMinute;

    private Project          project;
    private Project          timeProject;

    @Rule
    public ExpectedException exception      = ExpectedException.none();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        // Set time to zero
        Timekeeper.setTime(new Date(0));

        project = new Project("test", "desc", new Date(10), new Date(600010));
        timeProject = new Project("onTime", "description", new Date(0), new Date(24 * 60 * milisPerMinute));
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void canHaveAsTitleTest() {
        assertTrue(project.canHaveAsTitle("test title"));
        assertFalse(project.canHaveAsTitle("test \n title"));
    }

    @Test
    public void setTitleTest() {
        project.setTitle("test title");
        String title = project.getTitle();
        assertTrue("test title".equals(title));

        exception.expect(IllegalArgumentException.class);
        project.setTitle("test \n title");
    }

    @Test
    public void canHaveAsDescriptionTest() {
        assertTrue(project.canHaveAsDescription("test description \n with lines"));
        assertFalse(project.canHaveAsDescription(null));
    }

    @Test
    public void setDescriptionTest() {
        project.setDescription("test description \n with lines");
        assertTrue("test description \n with lines".equals(project.getDescription()));

        exception.expect(IllegalArgumentException.class);
        project.setDescription(null);
    }

    @Test
    public void isOngoingTest() {
        assertTrue(project.isOngoing());
        Task test = project.createTask("desc", 1, 0);
        assertTrue(project.isOngoing());
        test.finish(new TimePeriod(new Date(1), new Date(10)));
        assertFalse(project.isOngoing());

    }

    @Test
    public void isFinishedTest() {
        assertFalse(project.isFinished());
        Task test = project.createTask("desc", 1, 0);
        assertFalse(project.isFinished());
        test.finish(new TimePeriod(new Date(1), new Date(10)));
        assertTrue(project.isFinished());

    }

    @Test
    public void canHaveAsCreationTimeTest() {
        assertFalse(project.canHaveAsCreationTime(null));
        assertTrue(project.canHaveAsCreationTime(new Date(1)));
    }

    @Test
    public void canHaveAsDueTimeTest() {
        assertFalse(project.canHaveAsDueTime(null));
        assertFalse(project.canHaveAsDueTime(new Date(1)));
        assertTrue(project.canHaveAsDueTime(new Date(15)));
    }

    @Test
    public void setDueTimeTest() {
        try {
            project.setDueTime(null);
            fail();
        } catch (IllegalArgumentException e) {}

        try {
            project.setDueTime(new Date(1));
            fail();
        } catch (IllegalArgumentException e) {}

        Date date = new Date(15);
        project.setDueTime(date);
        assertEquals(date, project.getDueTime());
    }

    @Test
    public void createTaskTest() {
        Task test = project.createTask("desc", 10, 0);
        assertTrue(project.getTasks().contains(test));

    }

    @Test
    public void isOnTimeEmptyTest() {
        // An empty project with a due date in the future.
        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeOneTaskTest1() {
        timeProject.createTask("task1", 7 * milisPerHour, 0); // less then 8 hours of work
        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
    }

    public void isOnTimeOneTaskTest2() {
        timeProject.createTask("task1", 8 * milisPerHour, 0); // exactly one workday
        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeOneTaskTest3() {
        timeProject.createTask("task1", 9 * milisPerHour, 0); // more than 8 hours of work
        assertFalse(timeProject.isOnTime());
        assertTrue(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeTwoParallelTasksTest1() {
        timeProject.createTask("task1", 6 * milisPerHour, 0); // 6 hours and
        timeProject.createTask("task2", 7 * milisPerHour, 0.5); // 7 hours BUT these are parralellizable

        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeTwoParallelTasksTest2() {
        timeProject.createTask("task1", 6 * milisPerHour, 0); // 6 hours and
        timeProject.createTask("task2", 9 * milisPerHour, 0.5); // 7 hours BUT these are parralellisable

        assertFalse(timeProject.isOnTime());
        assertTrue(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeTwoSerialTasksTest1() {
        Task task1 = timeProject.createTask("task1", 2 * milisPerHour, 0); // 2 hours and
        Task task2 = timeProject.createTask("task2", 5 * milisPerHour, 0.5); // 5 hours BUT we need to add these up because 2 has to happen before 1
        task1.addDependency(task2);

        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeTwoSerialTasksTest2() {
        Task task1 = timeProject.createTask("task1", 4 * milisPerHour, 0); // 4 hours and
        Task task2 = timeProject.createTask("task2", 5 * milisPerHour, 0.5); // 5 hours BUT we need to add these up because 2 has to happen before 1
        task1.addDependency(task2);

        assertFalse(timeProject.isOnTime());
        assertTrue(timeProject.isOverTime());
    }

}
