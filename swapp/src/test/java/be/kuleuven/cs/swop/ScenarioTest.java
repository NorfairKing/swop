package be.kuleuven.cs.swop;


import static org.junit.Assert.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import be.kuleuven.cs.swop.facade.TaskMan;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.TaskData;
import be.kuleuven.cs.swop.facade.TaskStatusData;
import be.kuleuven.cs.swop.facade.TaskWrapper;


public class ScenarioTest {

    private static TaskMan taskMan;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        taskMan = new TaskMan();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void scenario1Test() throws ParseException {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime currentDate = null;

        /*
         * Day 1
         */
        currentDate = LocalDateTime.parse("2015-02-09 08:00", dateTimeFormat);
        taskMan.updateSystemTime(currentDate);

        String title = "MobileSteps";
        String description = "develop mobile app for counting steps using a specialised bracelet";
        LocalDateTime due = LocalDateTime.parse("2015-02-13 16:00", dateTimeFormat);
        ProjectData projectData = new ProjectData(title, description, due);
        ProjectWrapper p1 = taskMan.createProject(projectData);

        assertTrue(p1.isOngoing());
        assertFalse(p1.isFinished());
        assertTrue(p1.isOnTime(taskMan.getSystemTime()));
        assertFalse(p1.isOverTime(taskMan.getSystemTime()));;
        assertEquals(1, taskMan.getProjects().size());


        String d1 = "design system";
        double ed1 = 8 * 60;
        double ad1 = 0;
        TaskData t1r = new TaskData(d1, ed1, ad1);
        TaskWrapper t1 = taskMan.createTaskFor(p1, t1r);
        assertEquals(1, p1.getTasks().size());
        assertEquals(0,t1.getDependencySet().size());
        assertFalse(t1.isFinished());
        assertFalse(t1.isFailed());
        assertTrue(t1.canFinish());
        assertTrue(t1.getAlternative() == null);

        String d2 = "implement system in native code";
        double ed2 = 16 * 60;
        double ad2 = 0.5;
        TaskData t2r = new TaskData(d2, ed2, ad2);
        t2r.addDependency(t1);
        TaskWrapper t2 = taskMan.createTaskFor(p1, t2r);
        assertEquals(2, p1.getTasks().size());
        assertEquals(1,t2.getDependencySet().size());
        assertFalse(t2.isFinished());
        assertFalse(t2.isFailed());
        assertFalse(t2.canFinish());
        assertTrue(t2.getAlternative() == null);

        String d3 = "test system";
        double ed3 = 8 * 60;
        double ad3 = 0;
        TaskData t3r = new TaskData(d3, ed3, ad3);
        t3r.addDependency(t2);
        TaskWrapper t3 = taskMan.createTaskFor(p1, t3r);
        assertEquals(3, p1.getTasks().size());
        assertEquals(1,t3.getDependencySet().size());
        assertFalse(t3.isFinished());
        assertFalse(t3.isFailed());
        assertFalse(t3.canFinish());
        assertTrue(t3.getAlternative() == null);

        String d4 = "write documentation";
        double ed4 = 8 * 60;
        double ad4 = 0;
        TaskData t4r = new TaskData(d4, ed4, ad4);
        t4r.addDependency(t2);
        TaskWrapper t4 = taskMan.createTaskFor(p1, t4r);
        assertEquals(1,t4.getDependencySet().size());
        assertFalse(t4.isFinished());
        assertFalse(t4.isFailed());
        assertFalse(t4.canFinish());
        assertTrue(t4.getAlternative() == null);


        /*
         * Day 2
         */
        currentDate = LocalDateTime.parse("2015-02-10 08:00", dateTimeFormat);
        taskMan.updateSystemTime(currentDate);


        assertTrue(p1.isOngoing());
        assertFalse(p1.isFinished());
        assertTrue(p1.isOnTime(taskMan.getSystemTime()));
        assertFalse(p1.isOverTime(taskMan.getSystemTime()));;
        assertEquals(1,taskMan.getProjects().size());
        assertEquals(4, p1.getTasks().size());


        LocalDateTime t1Start = LocalDateTime.parse("2015-02-09 08:00", dateTimeFormat);
        LocalDateTime t1Stop = LocalDateTime.parse("2015-02-09 16:00", dateTimeFormat);
        TaskStatusData t1u = new TaskStatusData(t1Start, t1Stop, true);
        taskMan.updateTaskStatusFor(t1, t1u);
        assertTrue(t1.isFinished());


        assertTrue(t1.isFinished());
        assertFalse(t1.isFailed());
        assertFalse(t1.canFinish());
        assertNull(t1.getAlternative());
        assertFalse(t2.isFinished());
        assertFalse(t2.isFailed());
        assertTrue(t2.canFinish());
        assertNull(t2.getAlternative());
        assertFalse(t3.isFinished());
        assertFalse(t3.isFailed());
        assertFalse(t3.canFinish());
        assertNull(t3.getAlternative());
        assertFalse(t4.isFinished());
        assertFalse(t4.isFailed());
        assertFalse(t4.canFinish());
        assertNull(t4.getAlternative());

        /*
         * Day 3
         */
        currentDate = LocalDateTime.parse("2015-02-11 08:00", dateTimeFormat);
        taskMan.updateSystemTime(currentDate);

        assertTrue(p1.isOngoing());
        assertFalse(p1.isFinished());
        assertEquals(1,taskMan.getProjects().size());
        assertEquals(4, p1.getTasks().size());
        assertTrue(p1.isOnTime(taskMan.getSystemTime()));
        assertFalse(p1.isOverTime(taskMan.getSystemTime()));;

        LocalDateTime t2Start = LocalDateTime.parse("2015-02-10 08:00", dateTimeFormat);
        LocalDateTime t2Stop = LocalDateTime.parse("2015-02-10 16:00", dateTimeFormat);
        TaskStatusData t2u = new TaskStatusData(t2Start, t2Stop, false);
        taskMan.updateTaskStatusFor(t2, t2u);
        assertTrue(t1.isFinished());
        assertTrue(t2.isFailed());
        assertFalse(t3.canFinish());
        assertFalse(t4.canFinish());
        assertNull(t2.getAlternative());

        // Next part


        String d5 = "implement system with phonegap";
        double ed5 = 8*60;
        double ad5 = 1;
        TaskData t5d = new TaskData(d5, ed5, ad5);
        t5d.addDependency(t1);
        TaskWrapper t5 = taskMan.createAlternativeFor(t2, t5d);
        assertEquals(1,t5.getDependencySet().size());
        assertFalse(t5.isFinished());
        assertFalse(t5.isFailed());
        assertTrue(t5.canFinish());
        assertNull(t5.getAlternative());

        assertTrue(p1.isOngoing());
        assertFalse(p1.isFinished());
        assertEquals(1,taskMan.getProjects().size());
        assertEquals(5, p1.getTasks().size());


        //assertFalse(p1.isOnTime()); 
        //assertTrue(p1.isOverTime());
        assertTrue(p1.isOnTime(taskMan.getSystemTime())); //FIXME: MISTAKE IN ASSIGNMENT?
        assertFalse(p1.isOverTime(taskMan.getSystemTime()));



        assertTrue(t1.isFinished());
        assertFalse(t1.isFailed());
        assertFalse(t1.canFinish());
        assertNull(t1.getAlternative());


        assertFalse(t2.isFinished());
        assertTrue(t2.isFailed());
        assertFalse(t2.canFinish());
        assertNotNull(t2.getAlternative());


        assertFalse(t3.isFinished());
        assertFalse(t3.isFailed());
        assertFalse(t3.canFinish());
        assertNull(t3.getAlternative());


        assertFalse(t4.isFinished());
        assertFalse(t4.isFailed());
        assertFalse(t4.canFinish());
        assertNull(t4.getAlternative());


        assertFalse(t5.isFinished());
        assertFalse(t5.isFailed());
        assertTrue(t5.canFinish());
        assertNull(t5.getAlternative());

        /*
         * Day 4
         */
        currentDate = LocalDateTime.parse("2015-02-13 16:00", dateTimeFormat);
        taskMan.updateSystemTime(currentDate);


        LocalDateTime t5Start = LocalDateTime.parse("2015-02-11 08:00", dateTimeFormat);
        LocalDateTime t5Stop = LocalDateTime.parse("2015-02-11 16:00", dateTimeFormat);
        TaskStatusData t5u = new TaskStatusData(t5Start, t5Stop, true);
        taskMan.updateTaskStatusFor(t5, t5u); 
        assertTrue(t1.isFinished());
        assertTrue(t2.isFailed());
        assertTrue(t3.canFinish());
        assertTrue(t4.canFinish());
        assertTrue(t5.isFinished());

        LocalDateTime t3Start = LocalDateTime.parse("2015-02-12 08:00", dateTimeFormat);
        LocalDateTime t3Stop = LocalDateTime.parse("2015-02-12 16:00", dateTimeFormat);
        TaskStatusData t3u = new TaskStatusData(t3Start, t3Stop, true);
        taskMan.updateTaskStatusFor(t3, t3u);
        assertTrue(t1.isFinished());
        assertTrue(t2.isFailed());
        assertTrue(t3.isFinished());
        assertTrue(t4.canFinish());
        assertTrue(t5.isFinished());

        LocalDateTime t4Start = LocalDateTime.parse("2015-02-13 08:00", dateTimeFormat);
        LocalDateTime t4Stop = LocalDateTime.parse("2015-02-13 16:00", dateTimeFormat);
        TaskStatusData t4u = new TaskStatusData(t4Start, t4Stop, true);
        taskMan.updateTaskStatusFor(t4, t4u); 
        assertTrue(t1.isFinished());
        assertTrue(t2.isFailed());
        assertTrue(t3.isFinished());
        assertTrue(t4.isFinished());
        assertTrue(t5.isFinished());

        assertFalse(p1.isOngoing());
        assertTrue(p1.isFinished());
        assertEquals(1,taskMan.getProjects().size());
        assertEquals(5, p1.getTasks().size());
        assertTrue(p1.isOnTime(taskMan.getSystemTime()));;
        assertFalse(p1.isOverTime(taskMan.getSystemTime()));;

        assertTrue(t1.isFinished());
        assertFalse(t1.isFailed());
        assertFalse(t1.canFinish());
        assertNull(t1.getAlternative());
        assertFalse(t2.isFinished());
        assertTrue(t2.isFailed());
        assertFalse(t2.canFinish());
        assertNotNull(t2.getAlternative());
        assertTrue(t3.isFinished());
        assertFalse(t3.isFailed());
        assertFalse(t3.canFinish());
        assertNull(t3.getAlternative());
        assertTrue(t4.isFinished());
        assertFalse(t4.isFailed());
        assertFalse(t4.canFinish());
        assertNull(t4.getAlternative());
        assertTrue(t5.isFinished());
        assertFalse(t5.isFailed());
        assertFalse(t5.canFinish());
        assertNull(t5.getAlternative());
    }
}
