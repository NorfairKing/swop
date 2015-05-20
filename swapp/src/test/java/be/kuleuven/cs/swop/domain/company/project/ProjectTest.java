package be.kuleuven.cs.swop.domain.company.project;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;


public class ProjectTest {
    
    private static long      minutesPerHour = 60;
    private static LocalDateTime epoch = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);

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
        project = new Project("test", "test description \n with lines", epoch.plusHours(1), epoch.plusHours(11));
        timeProject = new Project("onTime", "description", epoch, epoch.plusDays(1));
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void canHaveAsTitleTest() {
        assertTrue(project.canHaveAsTitle("test title"));
        assertFalse(project.canHaveAsTitle("test \n title"));
    }

    @Test
    public void titleTest() {
        assertTrue(project.getTitle().equals("test"));
        exception.expect(IllegalArgumentException.class);
        project = new Project("test \n title", "desc", epoch.plusHours(1), epoch.plusHours(11));
    }
    
    @Test
    public void titleNullTest() {
        exception.expect(IllegalArgumentException.class);
        project = new Project(null, "desc", epoch.plusHours(1), epoch.plusHours(11));
    }

    @Test
    public void canHaveAsDescriptionTest() {
        assertTrue(project.canHaveAsDescription("test description \n with lines"));
        assertFalse(project.canHaveAsDescription(null));
    }

    @Test
    public void descriptionTest() {
        assertTrue("test description \n with lines".equals(project.getDescription()));
        exception.expect(IllegalArgumentException.class);
        project = new Project("test", null, epoch.plusHours(1), epoch.plusHours(11));

    }

    @Test
    public void isOngoingTest() {
        assertTrue(project.isOngoing());
        Task test = project.createTask("desc", 1, 0);
        assertTrue(project.isOngoing());
        test.execute();
        test.finish(new DateTimePeriod(epoch, epoch.plusHours(1)));
        assertFalse(project.isOngoing());

    }

    @Test
    public void isFinishedTest() {
        assertFalse(project.isFinished());
        Task test = project.createTask("desc", 1, 0);
        assertFalse(project.isFinished());
        test.execute();
        test.finish(new DateTimePeriod(epoch, epoch.plusHours(1)));
        assertTrue(project.isFinished());
    }

    @Test
    public void canHaveAsCreationTimeTest() {
        assertFalse(project.canHaveAsCreationTime(null));
        assertTrue(project.canHaveAsCreationTime(epoch));
    }
    @Test
    public void creationTimeTest() {
        try {
            project = new Project("test", "test description \n with lines", null, epoch.plusHours(1));
            fail();
        } catch (IllegalArgumentException e) {}

        try {
            project = new Project("test", "test description \n with lines", epoch.plusHours(1), epoch.minusHours(1));
            fail();
        } catch (IllegalArgumentException e) {}

        LocalDateTime date = epoch.plusHours(2);
        project = new Project("test", "test description \n with lines", date, epoch.plusHours(5));
        assertEquals(date, project.getCreationTime());
    }

    @Test
    public void canHaveAsDueTimeTest() {
        assertFalse(project.canHaveAsDueTime(null));
        assertFalse(project.canHaveAsDueTime(epoch.minusHours(1)));
        assertTrue(project.canHaveAsDueTime(epoch.plusHours(10)));
    }

    @Test
    public void DueTimeTest() {
        try {
            project = new Project("test", "test description \n with lines", epoch.plusHours(1), null);
            fail();
        } catch (IllegalArgumentException e) {}

        try {
            project = new Project("test", "test description \n with lines", epoch.plusHours(1), epoch.minusHours(1));
            fail();
        } catch (IllegalArgumentException e) {}

        LocalDateTime date = epoch.plusHours(2);
        project = new Project("test", "test description \n with lines", epoch.plusHours(1), date);
        assertEquals(date, project.getDueTime());
    }
    
    @Test
    public void canHaveAsTaskTest() {
        Task test = project.createTask("desc", 10, 0);
        assertTrue(Project.canHaveAsTask(test));
        assertFalse(Project.canHaveAsTask(null));
    }

    @Test
    public void createTaskTest() {
        Task test = project.createTask("desc", 10, 0);
        assertTrue(project.getTasks().contains(test));

    }
    
    @Test
    public void createTaskWithReqsTest() {
    	Requirements reqs = new Requirements(new HashSet<Requirement>());
    	Task test = project.createTask("desc", 10, 0, reqs);
    	assertTrue(project.getTasks().contains(test));
    	
    	Set<Requirement> reqSet = new HashSet<Requirement>();
    	reqSet.add(new Requirement(1,new ResourceType("testtype")));
        reqs = new Requirements(reqSet);
    	Task test2 = project.createTask("desc", 10, 0, reqs);
    	assertTrue(project.getTasks().contains(test2));
    	
    }
    
    @Test
    public void createTaskWithReqsAndDepsTest() {
    	Set<Requirement> reqSet = new HashSet<Requirement>();
        Requirements reqs = new Requirements(reqSet);
    	Set<Task> deps = new HashSet<Task>();
    	Task test = project.createTask("desc", 10, 0, deps, reqs);
    	assertTrue(project.getTasks().contains(test));
    	
    	reqSet.add(new Requirement(1,new ResourceType("testtype")));
        reqs = new Requirements(reqSet);
    	Task test2 = project.createTask("desc", 10, 0, deps, reqs);
    	assertTrue(project.getTasks().contains(test2));
    	
    	deps.add(test);
		Task test3 = project.createTask("desc", 10, 0, deps, reqs);
    	assertTrue(project.getTasks().contains(test3));
    	
    }

    @Test
    public void isOnTimeEmptyTest() {
        // An empty project with a due date in the future.
        assertTrue(timeProject.isOnTime(epoch));
        assertFalse(timeProject.isOverTime(epoch));
    }

    @Test
    public void isOnTimeOneTaskTest1() {
        timeProject.createTask("task1", 7 * minutesPerHour, 0); // less then 8 hours of work
        assertTrue(timeProject.isOnTime(epoch));
        assertFalse(timeProject.isOverTime(epoch));
    }

    public void isOnTimeOneTaskTest2() {
        timeProject.createTask("task1", 8 * minutesPerHour, 0); // exactly one workday
        assertTrue(timeProject.isOnTime(epoch));
        assertFalse(timeProject.isOverTime(epoch));
    }

    @Test
    public void isOnTimeOneTaskTest3() {
        timeProject.createTask("task1", 9 * minutesPerHour, 0); // more than 8 hours of work
        assertFalse(timeProject.isOnTime(epoch));
        assertTrue(timeProject.isOverTime(epoch));
    }

    @Test
    public void isOnTimeTwoParallelTasksTest1() {
        timeProject.createTask("task1", 6 * minutesPerHour, 0); // 6 hours and
        timeProject.createTask("task2", 7 * minutesPerHour, 0.5); // 7 hours BUT these are parralellizable

        assertTrue(timeProject.isOnTime(epoch));
        assertFalse(timeProject.isOverTime(epoch));
    }

    @Test
    public void isOnTimeTwoParallelTasksTest2() {
        timeProject.createTask("task1", 6 * minutesPerHour, 0); // 6 hours and
        timeProject.createTask("task2", 9 * minutesPerHour, 0.5); // 7 hours BUT these are parralellisable

        assertFalse(timeProject.isOnTime(epoch));
        assertTrue(timeProject.isOverTime(epoch));
    }

    @Test
    public void isOnTimeTwoSerialTasksTest1() {
        Set<Requirement> reqSet = new HashSet<Requirement>();
        Requirements reqs = new Requirements(reqSet);
        Set<Task> deps = new HashSet<Task>();
        Task task1 = timeProject.createTask("task1", 2 * minutesPerHour, 0); // 2 hours and
        deps.add(task1);
        Task task2 = timeProject.createTask("task2", 5 * minutesPerHour, 0.5, deps, reqs); // 5 hours BUT we need to add these up because 2 has to happen before 1

        assertTrue(timeProject.isOnTime(epoch));
        assertFalse(timeProject.isOverTime(epoch));
    }

    @Test
    public void isOnTimeTwoSerialTasksTest2() {
        Set<Requirement> reqSet = new HashSet<Requirement>();
        Requirements reqs = new Requirements(reqSet);
        Set<Task> deps = new HashSet<Task>();
        Task task1 = timeProject.createTask("task1", 4 * minutesPerHour, 0); // 4 hours and
        deps.add(task1);
        Task task2 = timeProject.createTask("task2", 5 * minutesPerHour, 0.5, deps, reqs); // 5 hours BUT we need to add these up because 2 has to happen before 1

        assertFalse(timeProject.isOnTime(epoch));
        assertTrue(timeProject.isOverTime(epoch));
    }

    @Test
    public void isOnTimeOneTaskWithAlternativeTest1() {
        Task task1 = timeProject.createTask("task1", 2 * minutesPerHour, 0);
        Task task2 = timeProject.createTask("task2", 5 * minutesPerHour, 0.5);

        LocalDateTime curTime = epoch.plusHours(10);
        task1.fail(new DateTimePeriod(epoch.plusHours(8), epoch.plusHours(10)));
        task1.setAlternative(task2);

        assertTrue(timeProject.isOnTime(curTime));
        assertFalse(timeProject.isOverTime(curTime));
    }

    @Test
    public void isOnTimeOneTaskWithAlternativeTest2() {
        Task task1 = timeProject.createTask("task1", 2 * minutesPerHour, 0);
        Task task2 = timeProject.createTask("task2", 7 * minutesPerHour, 0.5);

        LocalDateTime curTime = epoch.plusHours(10);
        task1.fail(new DateTimePeriod(epoch.plusHours(8), epoch.plusHours(10)));
        task1.setAlternative(task2);

        assertFalse(timeProject.isOnTime(curTime));
        assertTrue(timeProject.isOverTime(curTime));
    }

    @Test
    public void isOnTimeOneTaskWithAlternativeTest3() {
        Task task1 = timeProject.createTask("task1", 4 * minutesPerHour, 0);
        Task task2 = timeProject.createTask("task2", 5 * minutesPerHour, 0.5);

        LocalDateTime curTime = epoch.plusHours(10);
        task1.fail(new DateTimePeriod(epoch.plusHours(8), epoch.plusHours(10)));
        task1.setAlternative(task2);

        assertTrue(timeProject.isOnTime(curTime));
        assertFalse(timeProject.isOverTime(curTime));
    }

    @Test
    public void isOnTimeOneTaskWithAlternativeTest4() {
        Task task1 = timeProject.createTask("task1", 4 * minutesPerHour, 0);
        Task task2 = timeProject.createTask("task2", 5 * minutesPerHour, 0.5);

        LocalDateTime curTime = epoch.plusHours(12);
        task1.fail(new DateTimePeriod(epoch.plusHours(8), epoch.plusHours(12)));
        task1.setAlternative(task2);

        assertFalse(timeProject.isOnTime(curTime));
        assertTrue(timeProject.isOverTime(curTime));
    }

    @Test
    public void isOnTimeTwoLayersTest1() {
        Set<Requirement> reqSet = new HashSet<Requirement>();
        Requirements reqs = new Requirements(reqSet);
        Set<Task> deps = new HashSet<Task>();
        Task task1 = timeProject.createTask("task1", 2 * minutesPerHour, 0); // 2 hours and

        assertTrue(timeProject.isOnTime(epoch));
        assertFalse(timeProject.isOverTime(epoch));
        
        deps.add(task1);
        Task task2 = timeProject.createTask("task2", 3 * minutesPerHour, 0.5, deps, reqs);

        assertTrue(timeProject.isOnTime(epoch));
        assertFalse(timeProject.isOverTime(epoch));

        deps.clear();
        deps.add(task2);
        Task task3 = timeProject.createTask("task3", 4 * minutesPerHour, 0.5, deps, reqs);


        assertFalse(timeProject.isOnTime(epoch));
        assertTrue(timeProject.isOverTime(epoch));

        deps.clear();
        deps.add(task3);
        Task task4 = timeProject.createTask("task4", 5 * minutesPerHour, 0.5, deps, reqs);

        assertFalse(timeProject.isOnTime(epoch));
        assertTrue(timeProject.isOverTime(epoch));
    }

    @Test
    public void isOnTimeTwoLayersTest2() {
        Set<Requirement> reqSet = new HashSet<Requirement>();
        Requirements reqs = new Requirements(reqSet);
        Set<Task> deps = new HashSet<Task>();
        
        Task task1 = timeProject.createTask("task1", 5 * minutesPerHour, 0.5); // 5 hours BUT we need to add these up because 2 has to happen before 1
        deps.add(task1);
        Task task2 = timeProject.createTask("task2", 4 * minutesPerHour, 0.5, deps, reqs); // 5 hours BUT we need to add these up because 2 has to happen before 1
        Task task3 = timeProject.createTask("task3", 3 * minutesPerHour, 0.5); // 5 hours BUT we need to add these up because 2 has to happen before 1
        deps.clear();
        deps.add(task2);
        deps.add(task3);
        Task task4 = timeProject.createTask("task4", 2 * minutesPerHour, 0.5, deps, reqs); // 5 hours BUT we need to add these up because 2 has to happen before 1
        assertFalse(timeProject.isOnTime(epoch));
        assertTrue(timeProject.isOverTime(epoch));
    }

    @Test
    public void isOnTimeLotOfTasksTest() {
        Project p = new Project("title", "description", epoch, epoch.plusDays(5)); // 24 hours of possible work
        Set<Requirement> reqSet = new HashSet<Requirement>();
        Requirements reqs = new Requirements(reqSet);
        Set<Task> deps = new HashSet<Task>();

        
        Task task5 = p.createTask("task5", 5 * minutesPerHour, 0);
        Task task6 = p.createTask("task6", 6 * minutesPerHour, 0);
        Task task7 = p.createTask("task7", 7 * minutesPerHour, 0);
        deps.add(task5);
        deps.add(task6);
        deps.add(task7);
        Task task2 = p.createTask("task2", 2 * minutesPerHour, 0, deps, reqs);
       
        
        Task task9 = p.createTask("task9", 9 * minutesPerHour, 0);
        deps.clear();
        deps.add(task9);
        Task task8 = p.createTask("task8", 8 * minutesPerHour, 0);
        
        
        deps.clear();
        deps.add(task8);
        Task task3 = p.createTask("task3", 3 * minutesPerHour, 0, deps, reqs);
        
        
        Task task4 = p.createTask("task4", 4 * minutesPerHour, 0);
        deps.clear();
        deps.add(task2);
        deps.add(task3);
        deps.add(task4);
        Task task1 = p.createTask("task1", 1 * minutesPerHour, 0, deps, reqs);


        
        assertTrue(p.isOnTime(epoch)); // 9+8+3+4 <= 24
        assertFalse(p.isOverTime(epoch));
        
        LocalDateTime curTime = epoch.plusHours(12);
        task4.execute();
        task4.finish(new DateTimePeriod(epoch.plusHours(8), epoch.plusHours(12)));
        
        assertFalse(p.isOnTime(curTime)); // 9+8+3+4 > 20
        assertTrue(p.isOverTime(curTime));
    }

    @Test
    public void isOnTimeOneFinishedTaskTest1() {
        Task task1 = timeProject.createTask("task1", 2 * minutesPerHour, 0); // 2 hours and
        
        LocalDateTime curTime = epoch.plusHours(2);
        task1.execute();
        task1.finish(new DateTimePeriod(epoch, epoch.plusHours(2)));
        
        assertTrue(timeProject.isOnTime(curTime));
        assertFalse(timeProject.isOverTime(curTime));
        
        curTime = epoch.plusHours(24);
        
        assertTrue(timeProject.isOnTime(curTime));
        assertFalse(timeProject.isOverTime(curTime));
    }
    
    @Test
    public void isOnTimeOneFinishedTaskTest2() {
        Task task1 = timeProject.createTask("task1", 2 * minutesPerHour, 0); // 2 hours and
        
        LocalDateTime curTime = epoch.plusHours(15);
        task1.execute();
        task1.finish(new DateTimePeriod(epoch.plusHours(12), epoch.plusHours(14)));
        
        assertTrue(timeProject.isOnTime(curTime));
        assertFalse(timeProject.isOverTime(curTime));
        
        curTime = epoch.plusHours(24);
        
        assertTrue(timeProject.isOnTime(curTime));
        assertFalse(timeProject.isOverTime(curTime));
    }
    
    @Test
    public void isOnTimeSuperComplexTest(){
        LocalDateTime curTime = epoch.plusHours(8);
        Project p = new Project("title", "description", epoch, epoch.plusDays(7)); // 40 hours of possible work

        Task task1 = p.createTask("task1", 1 * minutesPerHour, 0);
        Task task2 = p.createTask("task2", 2 * minutesPerHour, 0);
        Task task3 = p.createTask("task3", 3 * minutesPerHour, 0);
        Task task4 = p.createTask("task4", 4 * minutesPerHour, 0);
        Task task5 = p.createTask("task5", 5 * minutesPerHour, 0);
        
        task1.addDependency(task2);
        task1.addDependency(task3);
        task1.addDependency(task4);
        
        curTime = epoch.plusHours(12);
        task4.fail(new DateTimePeriod(epoch.plusHours(8), epoch.plusHours(10)));
        task4.setAlternative(task5);
        
        
        assertTrue(p.isOnTime(curTime)); // 5+1 hours to go in 36 hours
        assertFalse(p.isOverTime(curTime));
        
        
        Task task6 = p.createTask("task6", 6 * minutesPerHour, 0);
        Task task7 = p.createTask("task7", 7 * minutesPerHour, 0);
        
        
        task5.addDependency(task6);
        task5.addDependency(task7);
        
        curTime = epoch.plusHours(16);
        task7.execute(); // this should never be done in the domain, but is useful for testing
        task7.finish(new DateTimePeriod(epoch.plusHours(12), epoch.plusHours(16)));
        task2.execute();
        task2.finish(new DateTimePeriod(epoch.plusHours(12), epoch.plusHours(16)));
        
        assertTrue(p.isOnTime(curTime)); // 12 hours to go in 32 hours
        assertFalse(p.isOverTime(curTime));
        
        
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
        
        assertFalse(p.isOnTime(curTime)); // 12+9+8+3+1=33 hours to go in 32 hours
        assertTrue(p.isOverTime(curTime));
        
        task9.fail(new DateTimePeriod(epoch.plusHours(12), epoch.plusHours(16)));
        Task task13 = p.createTask("task13", 13 * minutesPerHour, 0);
        task9.setAlternative(task13);
        
        assertTrue(p.isOnTime(curTime)); //13+8+3+1 hours to go in 32 hours
        assertFalse(p.isOverTime(curTime));
        
        
        task13.fail(new DateTimePeriod(epoch.plusHours(12), epoch.plusHours(16)));
        Task task23 = p.createTask("task23", 23 * minutesPerHour, 0);
        task13.setAlternative(task23);
        
        assertFalse(p.isOnTime(curTime)); // 23+8+3+1 hours to go in 32 hours
        assertTrue(p.isOverTime(curTime));
    }
}
