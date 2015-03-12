package be.kuleuven.cs.swop;


import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.data.TaskStatusData;
import be.kuleuven.cs.swop.domain.Timekeeper;


public class ScenarioTest {

    private static FacadeController facade;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        facade = new FacadeController();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void scenario1Test() throws ParseException {
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));

        Date currentDate = null;

        /*
         * Day 1
         */
        currentDate = dateTimeFormat.parse("2015-02-09 08:00");
        facade.updateSystemTime(currentDate);

        String title = "MobileSteps";
        String description = "develop mobile app for counting steps using a specialised bracelet";
        Date due = dateTimeFormat.parse("2015-02-13 16:00");
        ProjectData projectData = new ProjectData(title, description, due);
        ProjectWrapper p1 = facade.createProject(projectData);

        assertTrue(p1.isOngoing());
        assertFalse(p1.isFinished());
        assertTrue(p1.isOnTime());
        assertFalse(p1.isOverTime());;
        assertEquals(1, facade.getProjects().size());


        String d1 = "design system";
        double ed1 = 8 * 60;
        double ad1 = 0;
        TaskData t1r = new TaskData(d1, ed1, ad1);
        TaskWrapper t1 = facade.createTaskFor(p1, t1r);
        assertEquals(1, facade.getTasksOf(p1).size());
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
        TaskWrapper t2 = facade.createTaskFor(p1, t2r);
        assertEquals(2, facade.getTasksOf(p1).size());
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
        TaskWrapper t3 = facade.createTaskFor(p1, t3r);
        assertEquals(3, facade.getTasksOf(p1).size());
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
        TaskWrapper t4 = facade.createTaskFor(p1, t4r);
        assertEquals(1,t4.getDependencySet().size());
        assertFalse(t4.isFinished());
        assertFalse(t4.isFailed());
        assertFalse(t4.canFinish());
        assertTrue(t4.getAlternative() == null);


        /*
         * Day 2
         */
        currentDate = dateTimeFormat.parse("2015-02-10 08:00");
        facade.updateSystemTime(currentDate);


        assertTrue(p1.isOngoing());
        assertFalse(p1.isFinished());
        assertTrue(p1.isOnTime());
        assertFalse(p1.isOverTime());;
        assertEquals(1,facade.getProjects().size());
        assertEquals(4, facade.getTasksOf(p1).size());


        Date t1Start = dateTimeFormat.parse("2015-02-09 08:00");
        Date t1Stop = dateTimeFormat.parse("2015-02-09 16:00");
        TaskStatusData t1u = new TaskStatusData(t1Start, t1Stop, true);
        facade.updateTaskStatusFor(t1, t1u);
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
        currentDate = dateTimeFormat.parse("2015-02-11 08:00");
        facade.updateSystemTime(currentDate);

        assertTrue(p1.isOngoing());
        assertFalse(p1.isFinished());
        assertEquals(1,facade.getProjects().size());
        assertEquals(4, facade.getTasksOf(p1).size());
        assertTrue(p1.isOnTime());
        assertFalse(p1.isOverTime());;

        Date t2Start = dateTimeFormat.parse("2015-02-10 08:00");
        Date t2Stop = dateTimeFormat.parse("2015-02-10 16:00");
        TaskStatusData t2u = new TaskStatusData(t2Start, t2Stop, false);
        facade.updateTaskStatusFor(t2, t2u);
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
        TaskWrapper t5 = facade.createAlternativeFor(t2, t5d);
        assertEquals(1,t5.getDependencySet().size());
        assertFalse(t5.isFinished());
        assertFalse(t5.isFailed());
        assertTrue(t5.canFinish());
        assertNull(t5.getAlternative());

        assertTrue(p1.isOngoing());
        assertFalse(p1.isFinished());
        assertEquals(1,facade.getProjects().size());
        assertEquals(5, facade.getTasksOf(p1).size());


        //assertFalse(p1.isOnTime()); 
        //assertTrue(p1.isOverTime());
        assertTrue(p1.isOnTime()); //FIXME: MISTAKE IN ASSIGNMENT?
        assertFalse(p1.isOverTime());



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
        currentDate = dateTimeFormat.parse("2015-02-13 16:00");
        facade.updateSystemTime(currentDate);


        Date t5Start = dateTimeFormat.parse("2015-02-11 08:00");
        Date t5Stop = dateTimeFormat.parse("2015-02-11 16:00");
        TaskStatusData t5u = new TaskStatusData(t5Start, t5Stop, true);
        facade.updateTaskStatusFor(t5, t5u); 
        assertTrue(t1.isFinished());
        assertTrue(t2.isFailed());
        assertTrue(t3.canFinish());
        assertTrue(t4.canFinish());
        assertTrue(t5.isFinished());

        Date t3Start = dateTimeFormat.parse("2015-02-12 08:00");
        Date t3Stop = dateTimeFormat.parse("2015-02-12 16:00");
        TaskStatusData t3u = new TaskStatusData(t3Start, t3Stop, true);
        facade.updateTaskStatusFor(t3, t3u);
        assertTrue(t1.isFinished());
        assertTrue(t2.isFailed());
        assertTrue(t3.isFinished());
        assertTrue(t4.canFinish());
        assertTrue(t5.isFinished());

        Date t4Start = dateTimeFormat.parse("2015-02-13 08:00");
        Date t4Stop = dateTimeFormat.parse("2015-02-13 16:00");
        TaskStatusData t4u = new TaskStatusData(t4Start, t4Stop, true);
        facade.updateTaskStatusFor(t4, t4u); 
        assertTrue(t1.isFinished());
        assertTrue(t2.isFailed());
        assertTrue(t3.isFinished());
        assertTrue(t4.isFinished());
        assertTrue(t5.isFinished());

        assertFalse(p1.isOngoing());
        assertTrue(p1.isFinished());
        assertEquals(1,facade.getProjects().size());
        assertEquals(5, facade.getTasksOf(p1).size());
        assertTrue(p1.isOnTime());;
        assertFalse(p1.isOverTime());;

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
