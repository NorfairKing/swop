package be.kuleuven.cs.swop;


import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.data.TaskStatusData;


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
        DateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        
        Date currentDate = null;
        
        currentDate = dateTimeFormat.parse("2015-02-09 08:00");
        facade.updateSystemTime(currentDate);

        String title = "MobileSteps";
        String description = "develop mobile app for counting steps using a specialised bracelet";
        Date due = dayFormat.parse("2015-02-13");
        ProjectData projectData = new ProjectData(title, description, due);
        ProjectWrapper p1 = facade.createProject(projectData);

        assertEquals(1, facade.getProjects().size());


        String d1 = "design system";
        double ed1 = 8 * 60;
        double ad1 = 0;
        TaskData task1 = new TaskData(d1, ed1, ad1);
        // TODO dependencies
        TaskWrapper t1 = facade.createTaskFor(p1, task1);
        assertEquals(1, facade.getTasksOf(p1).size());

        String d2 = "implement system in native code";
        double ed2 = 16 * 60;
        double ad2 = 0.5;
        TaskData task2 = new TaskData(d2, ed2, ad2);
        // TODO dependencies
        TaskWrapper t2 = facade.createTaskFor(p1, task2);
        assertEquals(2, facade.getTasksOf(p1).size());

        String d3 = "test system";
        double ed3 = 8 * 60;
        double ad3 = 0;
        TaskData task3 = new TaskData(d3, ed3, ad3);
        // TODO dependencies
        TaskWrapper t3 = facade.createTaskFor(p1, task3);
        assertEquals(3, facade.getTasksOf(p1).size());

        String d4 = "write documentation";
        double ed4 = 8 * 60;
        double ad4 = 0;
        TaskData task4 = new TaskData(d4, ed4, ad4);
        // TODO dependencies
        TaskWrapper t4 = facade.createTaskFor(p1, task4);
        assertEquals(4, facade.getTasksOf(p1).size());
        assertTrue(p1.isOngoing());

        currentDate = dateTimeFormat.parse("2015-02-10 08:00");
        facade.updateSystemTime(currentDate);
        assertTrue(p1.isOngoing());
        // TODO assertTrue(project1.isOnTime());
        
        
        Date t1Start = dateTimeFormat.parse("2015-02-09 08:00");
        Date t1Stop = dateTimeFormat.parse("2015-02-09 16:00");
        TaskStatusData t1UpdateData = new TaskStatusData(t1Start, t1Stop, true);
        facade.updateTaskStatusFor(t1, t1UpdateData);
 
        
        
        
    }
}
