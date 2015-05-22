package be.kuleuven.cs.swop.facade;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.user.Developer;


public class PlanTaskSessionTest extends BaseFacadeTest {

    private ProjectWrapper project;

    private int            typeCount        = 6;
    private int            resourcesPerType = 2;
    private Developer      dev;

    // type 0: no requirements, No conflicts, no self-conflict, no daily availability
    // type 1: no requirements, conflicts with type0, no self-conflict, no daily availability
    // type 2: requires type 0, No conflicts, no self-conflict, no daily availability
    // type 3: no requirements, No conflicts, self-conflicts, no daily availability
    // type 4: requires type2, conflicts with type 3, no self-conflict, no daily availability
    // type 5: no requirements, No conflicts, no self-conflict, daily availability between 9:00 and 12:00
    private ResourceType[] types            = new ResourceType[typeCount];

    private Resource[][]   resources        = new Resource[typeCount][resourcesPerType];

    @Before
    public void setUp() throws Exception {
        simpleSetup();

        dev = taskMan.createDeveloper("Jane");
        project = taskMan.createProject(new ProjectData("Title", "Descr", taskMan.getSystemTime().plusHours(1)));
        types[0] = taskMan.createResourceType(new ResourceTypeData("type0",
                new HashSet<ResourceType>(),
                new HashSet<ResourceType>(),
                false,
                new LocalTime[0]));

        Set<ResourceType> type0Set = new HashSet<ResourceType>();
        type0Set.add(types[0]);

        types[1] = taskMan.createResourceType(new ResourceTypeData("type1",
                new HashSet<ResourceType>(),
                type0Set,
                false,
                new LocalTime[0]));

        types[2] = taskMan.createResourceType(new ResourceTypeData("type2",
                type0Set,
                new HashSet<ResourceType>(),
                false,
                new LocalTime[0]));

        types[3] = taskMan.createResourceType(new ResourceTypeData("type3",
                new HashSet<ResourceType>(),
                new HashSet<ResourceType>(),
                true,
                new LocalTime[0]));

        Set<ResourceType> type2Set = new HashSet<ResourceType>();
        type2Set.add(types[2]);
        Set<ResourceType> type3Set = new HashSet<ResourceType>();
        type3Set.add(types[3]);

        types[4] = taskMan.createResourceType(new ResourceTypeData("type4",
                type2Set,
                type3Set,
                false,
                new LocalTime[0]));

        LocalTime[] daily = new LocalTime[2];
        daily[0] = LocalTime.of(8, 0);
        daily[1] = LocalTime.of(12, 0);
        types[5] = taskMan.createResourceType(new ResourceTypeData("type5",
                new HashSet<ResourceType>(),
                new HashSet<ResourceType>(),
                false,
                daily));

        for (int i = 0; i < typeCount; i++) {
            for (int j = 0; j < resourcesPerType; j++) {
                resources[i][j] = taskMan.createResource(new ResourceData("type " + i + " num " + j, types[i]));
            }
        }

        ui.start();
    }

    @Test
    public void noRequirementsTest() {
        taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

        TaskPlanning plan = task1.getPlanning();

        assertTrue(plan.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0))));
        assertEquals(1, plan.getReservations().size());
        assertEquals(1, plan.getDevelopers().size());
    }

    @Test
    public void onlyDevTest() {
        taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

        TaskPlanning plan = task1.getPlanning();

        assertTrue(plan.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0))));
        assertEquals(1, plan.getReservations().size());
        assertEquals(1, plan.getDevelopers().size());
        assertTrue(plan.getDevelopers().contains(dev));
    }

    @Test
    public void withBreakTest() {
        taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 180, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime());
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(true);
        controller.startPlanTaskSession();

        TaskPlanning plan = task1.getPlanning();

        assertTrue(plan.getPlannedStartTime().equals(LocalDateTime.of(2016, 1, 1, 11, 0)));
        assertEquals(1, plan.getReservations().size());
        assertEquals(1, plan.getDevelopers().size());
        assertTrue(plan.getDevelopers().contains(dev));
    }

    @Test
    public void withBreakNoDevelopersTest() {
        taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 180, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime());
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(true);
        controller.startPlanTaskSession();

        TaskPlanning plan = task1.getPlanning();

        assertTrue(plan.getPlannedStartTime().equals(LocalDateTime.of(2016, 1, 1, 11, 0)));
        assertEquals(1, plan.getReservations().size());
        assertEquals(1, plan.getDevelopers().size());
    }

    @Test
    public void withBreakWrongTimeTest() {
        taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 14, 0));
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 180, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime());
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(true);
        controller.startPlanTaskSession();

        TaskPlanning plan = task1.getPlanning();

        assertTrue(plan.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 14, 0), LocalDateTime.of(2016, 1, 1, 17, 0))));
        assertEquals(1, plan.getReservations().size());
        assertEquals(1, plan.getDevelopers().size());
        assertTrue(plan.getDevelopers().contains(dev));
    }

    @Test
    public void requirementsSatisfiedTest() {
        taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
        Map<ResourceType, Integer> req1 = new HashMap<>();
        req1.put(types[0], 2);
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(resources[0][0]);
        res1.add(resources[0][1]);
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

        TaskPlanning plan = task1.getPlanning();

        assertTrue(plan.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0))));
        assertEquals(3, plan.getReservations().size());
        assertTrue(plan.getReservations().contains(resources[0][0]));
        assertTrue(plan.getReservations().contains(resources[0][1]));
        assertEquals(1, plan.getDevelopers().size());
    }

    @Test
    public void requirementsSatisfiedWithExtraTest() {
        taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
        Map<ResourceType, Integer> req1 = new HashMap<>();
        req1.put(types[0], 2);
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(resources[0][0]);
        res1.add(resources[0][1]);
        res1.add(resources[3][0]);
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

        TaskPlanning plan = task1.getPlanning();

        assertTrue(plan.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0))));
        assertEquals(4, plan.getReservations().size());
        assertTrue(plan.getReservations().contains(resources[0][0]));
        assertTrue(plan.getReservations().contains(resources[0][1]));
        assertTrue(plan.getReservations().contains(resources[3][0]));
        assertTrue(plan.getReservations().contains(dev));
        assertTrue(plan.getDevelopers().contains(dev));
    }

    @Test
    public void RecursiverequirementsSatisfiedTest() {
        taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 11, 0));
        Map<ResourceType, Integer> req1 = new HashMap<>();
        req1.put(types[2], 1);
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(resources[0][0]);
        res1.add(resources[2][0]);
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

        TaskPlanning plan = task1.getPlanning();

        assertTrue(plan.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 12, 0), LocalDateTime.of(2016, 1, 1, 13, 0))));
        assertEquals(3, plan.getReservations().size());
        assertTrue(plan.getReservations().contains(resources[0][0]));
        assertTrue(plan.getReservations().contains(resources[2][0]));
        assertTrue(plan.getReservations().contains(dev));
        assertTrue(plan.getDevelopers().contains(dev));

    }

    @Test
    public void dailyAvailibilityValidTest() {
        taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 9, 0));
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(resources[5][0]);
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime());
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

        TaskPlanning plan = task1.getPlanning();

        assertTrue(plan.getEstimatedPeriod().equals(new DateTimePeriod(LocalDateTime.of(2016, 1, 1, 9, 0), LocalDateTime.of(2016, 1, 1, 10, 0))));
        assertEquals(2, plan.getReservations().size());
        assertTrue(plan.getReservations().contains(resources[5][0]));
        assertEquals(1, plan.getDevelopers().size());
        assertTrue(plan.getReservations().contains(dev));
        assertTrue(plan.getDevelopers().contains(dev));
    }

    @Test(expected = RuntimeException.class)
    public void notEnoughResourcesTest() {
        Map<ResourceType, Integer> req1 = new HashMap<>();
        req1.put(types[0], 100);
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(resources[0][0]);
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

    }

    @Test(expected = RuntimeException.class)
    public void conflictingResourcesTest() {
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(resources[0][0]);
        res1.add(resources[1][0]);
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

    }

    @Test(expected = RuntimeException.class)
    public void selfConflictingResourcesTest() {
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(resources[3][0]);
        res1.add(resources[3][1]);
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

    }

    @Test(expected = RuntimeException.class)
    public void resourceDependencyNotMetTest() {
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(resources[2][0]);
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

    }

    @Test(expected = RuntimeException.class)
    public void dailyAvailibilityInvalidTest() {
        taskMan.updateSystemTime(LocalDateTime.of(2016, 1, 1, 7, 0));
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(resources[5][0]);
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime());
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();
    }

    @Test
    public void nullTaskTest() {
        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(dev);

        ui.addRequestTask(null);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

    }

    @Test
    public void nullstartTimeTest() {
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(null);
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

    }

    @Test
    public void nullResourceTest() {
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(null);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();
    }

    @Test
    public void nullDeveloperTest() {
        Map<ResourceType, Integer> req1 = new HashMap<>();
        TaskWrapper task1 = taskMan.createTaskFor(project, new TaskData("desc", 60, 0, req1));

        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(dev);

        ui.addRequestTask(task1);
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

    }

    @Test(expected = RuntimeException.class)
    public void nullTaskinWrapperTest() {
        Set<Resource> res1 = new HashSet<Resource>();
        res1.add(dev);
        res1.add(dev);

        ui.addRequestTask(new TaskWrapper(null));
        ui.addSelectTime(taskMan.getSystemTime().plusHours(1));
        ui.addSelectResourcesFor(res1);
        ui.addShouldAddBreak(false);
        controller.startPlanTaskSession();

    }

}
