package be.kuleuven.cs.swop;


import static org.junit.Assert.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import be.kuleuven.cs.swop.facade.ConflictingPlanningWrapperException;
import be.kuleuven.cs.swop.facade.DeveloperData;
import be.kuleuven.cs.swop.facade.DeveloperWrapper;
import be.kuleuven.cs.swop.facade.ExecutingStatusData;
import be.kuleuven.cs.swop.facade.FailedStatusData;
import be.kuleuven.cs.swop.facade.FinishedStatusData;
import be.kuleuven.cs.swop.facade.ResourceData;
import be.kuleuven.cs.swop.facade.ResourceTypeData;
import be.kuleuven.cs.swop.facade.ResourceTypeWrapper;
import be.kuleuven.cs.swop.facade.ResourceWrapper;
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
    public void scenario1Test() throws ParseException, ConflictingPlanningWrapperException {
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

        
        ResourceTypeWrapper whiteboardType = taskMan.createResourceType(new ResourceTypeData("whiteboard", 
        		new HashSet<ResourceTypeWrapper>(), 
        		new HashSet<ResourceTypeWrapper>(), false, new LocalTime[0]));
        ResourceWrapper whiteboard = taskMan.createResource(new ResourceData("whiteboard 1", whiteboardType));
        
        ResourceTypeWrapper testSetupType = taskMan.createResourceType(new ResourceTypeData("distributed testing setup", 
        		new HashSet<ResourceTypeWrapper>(), 
        		new HashSet<ResourceTypeWrapper>(), false, new LocalTime[0]));
        ResourceWrapper testSetup = taskMan.createResource(new ResourceData("test setup 1", testSetupType));
        
        DeveloperWrapper devX = taskMan.createDeveloper(new DeveloperData("Mister X"));
        DeveloperWrapper devY = taskMan.createDeveloper(new DeveloperData("Miss Y"));

        String d1 = "design system";
        long ed1 = 480;
        double ad1 = 0;
        Map<ResourceTypeWrapper, Integer> req1 = new HashMap<ResourceTypeWrapper, Integer>();
        req1.put(whiteboardType, 1);
        TaskData t1r = new TaskData(d1, ed1, ad1, req1);
        TaskWrapper t1 = taskMan.createTaskFor(p1, t1r);
        assertEquals(1, p1.getTasks().size());
        assertEquals(0,t1.getDependencySet().size());
        assertFalse(t1.isFinished());
        assertFalse(t1.isFailed());
        assertFalse(t1.canFinish());
        assertTrue(t1.getAlternative() == null);
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t1)); // False because it is not planned yet

        String d2 = "implement system in native code";
        long ed2 = 128;
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
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t2));

        String d3 = "test system";
        long ed3 = 480;
        double ad3 = 0;
        Map<ResourceTypeWrapper, Integer> req2 = new HashMap<ResourceTypeWrapper, Integer>();
        req2.put(testSetupType, 1);
        TaskData t3r = new TaskData(d3, ed3, ad3, req2);
        t3r.addDependency(t2);
        TaskWrapper t3 = taskMan.createTaskFor(p1, t3r);
        assertEquals(3, p1.getTasks().size());
        assertEquals(1,t3.getDependencySet().size());
        assertFalse(t3.isFinished());
        assertFalse(t3.isFailed());
        assertFalse(t3.canFinish());
        assertTrue(t3.getAlternative() == null);
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t3));


        String d4 = "write documentation";
        long ed4 = 480;
        double ad4 = 0;
        TaskData t4r = new TaskData(d4, ed4, ad4);
        t4r.addDependency(t2);
        TaskWrapper t4 = taskMan.createTaskFor(p1, t4r);
        assertEquals(1,t4.getDependencySet().size());
        assertFalse(t4.isFinished());
        assertFalse(t4.isFailed());
        assertFalse(t4.canFinish());
        assertTrue(t4.getAlternative() == null);
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t4));
        
        
        //FIXME: check if these worked?
        Set<ResourceWrapper> t1res = new HashSet<ResourceWrapper>();
        t1res.add(whiteboard);      
        Set<DeveloperWrapper> t1dev = new HashSet<DeveloperWrapper>();
        t1dev.add(devX);
        taskMan.createPlanning(t1, LocalDateTime.of(2015, 2, 9, 9, 0), t1res, t1dev);
        
        
        Set<ResourceWrapper> t2res = new HashSet<ResourceWrapper>();
        Set<DeveloperWrapper> t2dev = new HashSet<DeveloperWrapper>();
        t2dev.add(devX);
        t2dev.add(devY);
        taskMan.createPlanning(t2, LocalDateTime.of(2015, 2, 10, 10, 0), t2res, t2dev);
        
        
        Set<ResourceWrapper> t3res = new HashSet<ResourceWrapper>();
        t3res.add(testSetup);      
        Set<DeveloperWrapper> t3dev = new HashSet<DeveloperWrapper>();
        t3dev.add(devX);
        taskMan.createPlanning(t3, LocalDateTime.of(2015, 2, 12, 9, 0), t3res, t3dev);
        
        Set<ResourceWrapper> t4res = new HashSet<ResourceWrapper>();
        Set<DeveloperWrapper> t4dev = new HashSet<DeveloperWrapper>();
        t4dev.add(devX);
        taskMan.createPlanning(t4, LocalDateTime.of(2015, 2, 13, 12, 0), t4res, t4dev);
        
        assertTrue(taskMan.isTaskAvailableFor(currentDate, devX, t1));
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t2));
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t3));
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t4));

        
        taskMan.updateTaskStatusFor(t1, new ExecutingStatusData(devX.getAsUser()));

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
        TaskStatusData t1u = new FinishedStatusData(t1Start, t1Stop);
        taskMan.updateTaskStatusFor(t1, t1u);
        assertTrue(t1.isFinished());


        taskMan.updateTaskStatusFor(t2, new ExecutingStatusData(devX.getAsUser()));
        
        
        assertTrue(t1.isFinished());
        assertFalse(t1.isFailed());
        assertFalse(t1.canFinish());
        assertNull(t1.getAlternative());
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t1));
        assertFalse(t2.isFinished());
        assertFalse(t2.isFailed());
        assertTrue(t2.canFinish());
        assertNull(t2.getAlternative());
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t2));
        assertFalse(t3.isFinished());
        assertFalse(t3.isFailed());
        assertFalse(t3.canFinish());
        assertNull(t3.getAlternative());
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t3));
        assertFalse(t4.isFinished());
        assertFalse(t4.isFailed());
        assertFalse(t4.canFinish());
        assertNull(t4.getAlternative());
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t4));


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
        TaskStatusData t2u = new FailedStatusData(t2Start, t2Stop);
        taskMan.updateTaskStatusFor(t2, t2u);
        assertTrue(t1.isFinished());
        assertTrue(t2.isFailed());
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t3));
        assertFalse(t3.canFinish());
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t4));
        assertFalse(t4.canFinish());
        assertNull(t2.getAlternative());

        // Next part


        String d5 = "implement system with phonegap";
        long ed5 = 480;
        double ad5 = 1;
        TaskData t5d = new TaskData(d5, ed5, ad5);
        t5d.addDependency(t1);
        TaskWrapper t5 = taskMan.createAlternativeFor(t2, t5d);
        assertEquals(1,t5.getDependencySet().size());
        assertFalse(t5.isFinished());
        assertFalse(t5.isFailed());
        assertFalse(t5.canFinish());
        assertNull(t5.getAlternative());
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t5));

        assertTrue(p1.isOngoing());
        assertFalse(p1.isFinished());
        assertEquals(1,taskMan.getProjects().size());
        assertEquals(5, p1.getTasks().size());

        taskMan.createPlanning(t5, LocalDateTime.of(2015, 2, 11, 8, 0), t2res, t2dev);
        
        
        //assertFalse(p1.isOnTime()); 
        //assertTrue(p1.isOverTime());
        assertTrue(p1.isOnTime(taskMan.getSystemTime())); //FIXME: MISTAKE IN ASSIGNMENT? Is it still?
        assertFalse(p1.isOverTime(taskMan.getSystemTime()));



        assertTrue(t1.isFinished());
        assertFalse(t1.isFailed());
        assertFalse(t1.canFinish());
        assertNull(t1.getAlternative());
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t1));


        assertFalse(t2.isFinished());
        assertTrue(t2.isFailed());
        assertFalse(t2.canFinish());
        assertNotNull(t2.getAlternative());
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t2));


        assertFalse(t3.isFinished());
        assertFalse(t3.isFailed());
        assertFalse(t3.canFinish());
        assertNull(t3.getAlternative());
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t3));


        assertFalse(t4.isFinished());
        assertFalse(t4.isFailed());
        assertFalse(t4.canFinish());
        assertNull(t4.getAlternative());
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t4));



        assertFalse(t5.isFinished());
        assertFalse(t5.isFailed());
        assertFalse(t5.canFinish());
        assertNull(t5.getAlternative());
        assertTrue(taskMan.isTaskAvailableFor(currentDate, devX, t5));
        
        
        taskMan.updateTaskStatusFor(t5, new ExecutingStatusData(devX.getAsUser()));
        
        assertEquals(1,t5.getDependencySet().size());
        assertFalse(t5.isFinished());
        assertFalse(t5.isFailed());
        assertTrue(t5.canFinish());
        assertNull(t5.getAlternative());
        assertFalse(taskMan.isTaskAvailableFor(currentDate, devX, t5));

        
        /*
         * Day 4 - NOT IN ASSIGNMENT
         */
        currentDate = LocalDateTime.parse("2015-02-12 08:00", dateTimeFormat);
        taskMan.updateSystemTime(currentDate);
        
        LocalDateTime t5Start = LocalDateTime.parse("2015-02-11 08:00", dateTimeFormat);
        LocalDateTime t5Stop = LocalDateTime.parse("2015-02-11 16:00", dateTimeFormat);
        TaskStatusData t5u = new FinishedStatusData(t5Start, t5Stop);
        taskMan.updateTaskStatusFor(t5, t5u); 
        assertTrue(t1.isFinished());
        assertTrue(t2.isFailed());
        assertTrue(taskMan.isTaskAvailableFor(currentDate, devX, t3));
        assertTrue(taskMan.isTaskAvailableFor(currentDate, devX, t4));
        assertTrue(t5.isFinished());
        
        taskMan.updateTaskStatusFor(t3, new ExecutingStatusData(devX.getAsUser()));
        assertTrue(t3.canFinish());
        
        
        /*
         * End of Day 4 - NOT IN ASSIGNMENT
         */
        
        LocalDateTime t3Start = LocalDateTime.parse("2015-02-12 08:00", dateTimeFormat);
        LocalDateTime t3Stop = LocalDateTime.parse("2015-02-12 16:00", dateTimeFormat);
        TaskStatusData t3u = new FinishedStatusData(t3Start, t3Stop);
        taskMan.updateTaskStatusFor(t3, t3u);
        assertTrue(t1.isFinished());
        assertTrue(t2.isFailed());
        assertTrue(t3.isFinished());
        assertTrue(taskMan.isTaskAvailableFor(currentDate, devX, t4));
        assertTrue(t5.isFinished());
        
        /*
         * Day 5 - Morning - NOT IN ASSIGNMENT
         */
        
        
        taskMan.updateTaskStatusFor(t4, new ExecutingStatusData(devX.getAsUser()));
        assertTrue(t4.canFinish());
        
        /*
         * Day 5
         */
        currentDate = LocalDateTime.parse("2015-02-13 16:00", dateTimeFormat);
        taskMan.updateSystemTime(currentDate);

        LocalDateTime t4Start = LocalDateTime.parse("2015-02-13 08:00", dateTimeFormat);
        LocalDateTime t4Stop = LocalDateTime.parse("2015-02-13 16:00", dateTimeFormat);
        TaskStatusData t4u = new FinishedStatusData(t4Start, t4Stop);
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
