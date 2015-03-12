package be.kuleuven.cs.swop.domain.project;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import javax.jws.Oneway;

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
    private static long      minutesPerHour = 60;

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
        timeProject.createTask("task1", 7 * minutesPerHour, 0); // less then 8 hours of work
        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
    }

    public void isOnTimeOneTaskTest2() {
        timeProject.createTask("task1", 8 * minutesPerHour, 0); // exactly one workday
        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeOneTaskTest3() {
        timeProject.createTask("task1", 9 * minutesPerHour, 0); // more than 8 hours of work
        assertFalse(timeProject.isOnTime());
        assertTrue(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeTwoParallelTasksTest1() {
        timeProject.createTask("task1", 6 * minutesPerHour, 0); // 6 hours and
        timeProject.createTask("task2", 7 * minutesPerHour, 0.5); // 7 hours BUT these are parralellizable

        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeTwoParallelTasksTest2() {
        timeProject.createTask("task1", 6 * minutesPerHour, 0); // 6 hours and
        timeProject.createTask("task2", 9 * minutesPerHour, 0.5); // 7 hours BUT these are parralellisable

        assertFalse(timeProject.isOnTime());
        assertTrue(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeTwoSerialTasksTest1() {
        Task task1 = timeProject.createTask("task1", 2 * minutesPerHour, 0); // 2 hours and
        Task task2 = timeProject.createTask("task2", 5 * minutesPerHour, 0.5); // 5 hours BUT we need to add these up because 2 has to happen before 1
        task1.addDependency(task2);

        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeTwoSerialTasksTest2() {
        Task task1 = timeProject.createTask("task1", 4 * minutesPerHour, 0); // 4 hours and
        Task task2 = timeProject.createTask("task2", 5 * minutesPerHour, 0.5); // 5 hours BUT we need to add these up because 2 has to happen before 1
        task1.addDependency(task2);

        assertFalse(timeProject.isOnTime());
        assertTrue(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeOneTaskWithAlternativeTest1() {
        Task task1 = timeProject.createTask("task1", 2 * minutesPerHour, 0);
        Task task2 = timeProject.createTask("task2", 5 * minutesPerHour, 0.5);

        Timekeeper.setTime(new Date(10 * milisPerHour));
        task1.fail(new TimePeriod(new Date(8*milisPerHour), new Date(10 * milisPerHour)));
        task1.setAlternative(task2);

        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeOneTaskWithAlternativeTest2() {
        Task task1 = timeProject.createTask("task1", 2 * minutesPerHour, 0);
        Task task2 = timeProject.createTask("task2", 7 * minutesPerHour, 0.5);

        Timekeeper.setTime(new Date(10 * milisPerHour));
        task1.fail(new TimePeriod(new Date(8*milisPerHour), new Date(10 * milisPerHour)));
        task1.setAlternative(task2);

        assertFalse(timeProject.isOnTime());
        assertTrue(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeOneTaskWithAlternativeTest3() {
        Task task1 = timeProject.createTask("task1", 4 * minutesPerHour, 0);
        Task task2 = timeProject.createTask("task2", 5 * minutesPerHour, 0.5);

        Timekeeper.setTime(new Date(10 * milisPerHour));
        task1.fail(new TimePeriod(new Date(8*milisPerHour), new Date(10 * milisPerHour)));
        task1.setAlternative(task2);

        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeOneTaskWithAlternativeTest4() {
        Task task1 = timeProject.createTask("task1", 4 * minutesPerHour, 0);
        Task task2 = timeProject.createTask("task2", 5 * minutesPerHour, 0.5);

        Timekeeper.setTime(new Date(12 * milisPerHour));
        task1.fail(new TimePeriod(new Date(8*milisPerHour), new Date(12 * milisPerHour)));
        task1.setAlternative(task2);

        assertFalse(timeProject.isOnTime());
        assertTrue(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeTwoLayersTest1() {
        Task task1 = timeProject.createTask("task1", 2 * minutesPerHour, 0); // 2 hours and
        Task task2 = timeProject.createTask("task2", 3 * minutesPerHour, 0.5); // 5 hours BUT we need to add these up because 2 has to happen before 1
        Task task3 = timeProject.createTask("task3", 4 * minutesPerHour, 0.5); // 5 hours BUT we need to add these up because 2 has to happen before 1
        Task task4 = timeProject.createTask("task4", 5 * minutesPerHour, 0.5); // 5 hours BUT we need to add these up because 2 has to happen before 1

        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
        
        task1.addDependency(task2);

        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());

        task2.addDependency(task3);

        assertFalse(timeProject.isOnTime());
        assertTrue(timeProject.isOverTime());

        task2.addDependency(task4);

        assertFalse(timeProject.isOnTime());
        assertTrue(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeTwoLayersTest2() {
        Task task1 = timeProject.createTask("task1", 2 * minutesPerHour, 0); // 2 hours and
        Task task2 = timeProject.createTask("task2", 3 * minutesPerHour, 0.5); // 5 hours BUT we need to add these up because 2 has to happen before 1
        Task task3 = timeProject.createTask("task3", 4 * minutesPerHour, 0.5); // 5 hours BUT we need to add these up because 2 has to happen before 1
        Task task4 = timeProject.createTask("task4", 5 * minutesPerHour, 0.5); // 5 hours BUT we need to add these up because 2 has to happen before 1

        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());

        task1.addDependency(task2);

        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());

        task1.addDependency(task3);

        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());

        task3.addDependency(task4);

        assertFalse(timeProject.isOnTime());
        assertTrue(timeProject.isOverTime());
    }

    @Test
    public void isOnTimeLotOfTasksTest() {
        Project p = new Project("title", "description", new Date(0), new Date(5 * 24 * milisPerHour)); // 24 hours of possible work

        Task task1 = p.createTask("task1", 1 * minutesPerHour, 0);
        Task task2 = p.createTask("task2", 2 * minutesPerHour, 0);
        Task task3 = p.createTask("task3", 3 * minutesPerHour, 0);
        Task task4 = p.createTask("task4", 4 * minutesPerHour, 0);
        Task task5 = p.createTask("task5", 5 * minutesPerHour, 0);
        Task task6 = p.createTask("task6", 6 * minutesPerHour, 0);
        Task task7 = p.createTask("task7", 7 * minutesPerHour, 0);
        Task task8 = p.createTask("task8", 8 * minutesPerHour, 0);
        Task task9 = p.createTask("task9", 9 * minutesPerHour, 0);

        task1.addDependency(task2);
        task1.addDependency(task3);
        task1.addDependency(task4);

        task2.addDependency(task5);
        task2.addDependency(task6);
        task2.addDependency(task7);

        task3.addDependency(task8);
        task8.addDependency(task9);
        
        assertTrue(p.isOnTime()); // 9+8+3+4 <= 24
        assertFalse(p.isOverTime());
        
        Timekeeper.setTime(new Date(12 * milisPerHour));
        task4.finish(new TimePeriod(new Date(8*milisPerHour),new Date(12*milisPerHour)));
        
        assertFalse(p.isOnTime()); // 9+8+3+4 > 20
        assertTrue(p.isOverTime());
    }

    @Test
    public void isOnTimeOneFinishedTaskTest1() {
        Task task1 = timeProject.createTask("task1", 2 * minutesPerHour, 0); // 2 hours and
        
        Timekeeper.setTime(new Date(2 * milisPerHour));
        task1.finish(new TimePeriod(new Date(0),new Date(2*milisPerHour)));
        
        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
        
        Timekeeper.setTime(new Date(24 * milisPerHour));
        
        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
    }
    
    @Test
    public void isOnTimeOneFinishedTaskTest2() {
        Task task1 = timeProject.createTask("task1", 2 * minutesPerHour, 0); // 2 hours and
        
        Timekeeper.setTime(new Date(15* milisPerHour));
        task1.finish(new TimePeriod(new Date(12*milisPerHour),new Date(14*milisPerHour)));
        
        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
        
        Timekeeper.setTime(new Date(24 * milisPerHour));
        
        assertTrue(timeProject.isOnTime());
        assertFalse(timeProject.isOverTime());
    }
    
    @Test
    public void isOnTimeSuperComplexTest(){
        Timekeeper.setTime(new Date(8 * milisPerHour));
        Project p = new Project("title", "description", new Date(0), new Date(7 * 24 * milisPerHour)); // 40 hours of possible work

        Task task1 = p.createTask("task1", 1 * minutesPerHour, 0);
        Task task2 = p.createTask("task2", 2 * minutesPerHour, 0);
        Task task3 = p.createTask("task3", 3 * minutesPerHour, 0);
        Task task4 = p.createTask("task4", 4 * minutesPerHour, 0);
        Task task5 = p.createTask("task5", 5 * minutesPerHour, 0);
        
        task1.addDependency(task2);
        task1.addDependency(task3);
        task1.addDependency(task4);
        
        Timekeeper.setTime(new Date(12* milisPerHour));
        task4.fail(new TimePeriod(new Date(8*milisPerHour), new Date(10*milisPerHour)));
        task4.setAlternative(task5);
        
        
        assertTrue(p.isOnTime()); // 5+1 hours to go in 36 hours
        assertFalse(p.isOverTime());
        
        
        Task task6 = p.createTask("task6", 6 * minutesPerHour, 0);
        Task task7 = p.createTask("task7", 7 * minutesPerHour, 0);
        
        
        task5.addDependency(task6);
        task5.addDependency(task7);
        
        Timekeeper.setTime(new Date(16* milisPerHour));
        task7.finish(new TimePeriod(new Date(12*milisPerHour),new Date(16*milisPerHour)));
        task2.finish(new TimePeriod(new Date(12*milisPerHour),new Date(16*milisPerHour)));
        
        assertTrue(p.isOnTime()); // 12 hours to go in 32 hours
        assertFalse(p.isOverTime());
        
        
        Task task8 = p.createTask("task8", 8 * minutesPerHour, 0);
        Task task9 = p.createTask("task9", 9 * minutesPerHour, 0);
        Task task10 = p.createTask("task10", 10 * minutesPerHour, 0);
        Task task11 = p.createTask("task11", 11 * minutesPerHour, 0);
        Task task12 = p.createTask("task12", 12 * minutesPerHour, 0);
        
        task3.addDependency(task8);
        task8.addDependency(task9);
        task9.addDependency(task10);
        task9.addDependency(task11);
        task9.addDependency(task12);
        
        assertFalse(p.isOnTime()); // 12+9+8+3+1=33 hours to go in 32 hours
        assertTrue(p.isOverTime());
        
        task9.fail(new TimePeriod(new Date(12*milisPerHour), new Date(16*milisPerHour)));
        Task task13 = p.createTask("task13", 13 * minutesPerHour, 0);
        task9.setAlternative(task13);
        
        assertTrue(p.isOnTime()); //13+8+3+1 hours to go in 32 hours
        assertFalse(p.isOverTime());
        
        
        task13.fail(new TimePeriod(new Date(12*milisPerHour), new Date(16*milisPerHour)));
        Task task23 = p.createTask("task23", 23 * minutesPerHour, 0);
        task13.setAlternative(task23);
        
        assertFalse(p.isOnTime()); // 23+8+3+1 hours to go in 32 hours
        assertTrue(p.isOverTime());
    }
}
