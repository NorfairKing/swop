package be.kuleuven.cs.swop.domain.company;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.task.TaskInfo;
import be.kuleuven.cs.swop.domain.company.user.Developer;


public class PlanningDepartmentTest {
    LocalDateTime epoch          = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
    DateTimePeriod period = new DateTimePeriod(epoch.plusHours(7), epoch.plusHours(8));
    PlanningDepartment manager;
    Company company = new Company();
    BranchOffice office = new BranchOffice("office", company);
    Project project;

    Set<Resource> ress = new HashSet<Resource>();
    Requirements reqs = new Requirements(new HashSet<Requirement>());
    Set<Task> deps = new HashSet<Task>();
    Task task = new Task(new TaskInfo("Task1", 60, 0, reqs, deps));
    
    
    @Before
    public void Setup() {
        manager = new PlanningDepartment(office);
        project = office.createProject("Project", "project desc", epoch, epoch.plusWeeks(1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createPlanningNullTaskTest() throws ConflictingPlannedTaskException {
        manager.createPlanning(null, period, ress, false);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createPlanningNullPeriodTest() throws ConflictingPlannedTaskException {
        manager.createPlanning(task, null, ress, false);
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void createPlanningNullRessTest() throws ConflictingPlannedTaskException {
        manager.createPlanning(task, period, null, false);
    }
    
    @Test
    public void addProperPlanningTest() throws ConflictingPlannedTaskException {
        assertEquals(0, manager.getTaskPlannings().size());
        
        Task task = project.createTask("Task1", 60, 0, deps, reqs);

        manager.createPlanning(task, period, ress, false);

        
        assertEquals(1, manager.getTaskPlannings().size());
        assertTrue(task.getPlanning() != null);
        assertTrue(manager.getTaskPlannings().contains(task.getPlanning()));
    }
    
    @Test
    public void busyDeveloperTest() throws ConflictingPlannedTaskException {
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 8, 0);
        Task task = project.createTask("werkje voor de grompot", 120, 1, deps, reqs);
        Developer dev = new Developer("de grompot");
        
        manager.createPlanning(task, new DateTimePeriod(time, time.plusMinutes(task.getEstimatedDuration())), new HashSet<>(Arrays.asList(dev)), false);
        
        Task otherTask = project.createTask("ander werkje", 60, 1, deps, reqs);

        assertFalse(manager.isAvailableFor(dev, otherTask, time));
        assertFalse(manager.isAvailableFor(dev, otherTask, time.plusHours(1)));
        
        assertTrue(manager.isAvailableFor(dev, otherTask, time.plusHours(2)));
        assertTrue(manager.isAvailableFor(dev, otherTask, time.minusHours(1)));
    }
    
    @Test
    public void planningTimesTest() {
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 8, 0);
        Task task = new Task(new TaskInfo("brijen", 120, 1,reqs, deps));
        office.createDeveloper("grootmoe");

        // nothing planned yet, so we get as many time options as we ask for
        assertEquals(3, manager.getPlanningTimeOptions(task, 3, time).size());
        assertEquals(5, manager.getPlanningTimeOptions(task, 5, time).size());
        
        ResourceType machine = new ResourceType("brij machien");
        Requirement req = new Requirement(1, machine);
        Requirements reqs = new Requirements(new HashSet<>(Arrays.asList(req)));
        Task hardTask = new Task(new TaskInfo("brijen met machien", 120, 1, reqs, deps));
        
        // the system doesn't have a machine, so can't find any time spot where we can plan this
        assertEquals(0, manager.getPlanningTimeOptions(hardTask, 3, time).size());
    }
    
    @Test
    public void planningDeveloperOptionsTest() {
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 8, 0);
        ResourceType machine = new ResourceType("schup");
        Requirement req = new Requirement(1, machine);
        Requirements reqs = new Requirements(new HashSet<>(Arrays.asList(req)));
        Task task = new Task(new TaskInfo("wortels planten", 120, 1, reqs, deps));
        Developer dev = office.createDeveloper("grootva");
        
        Set<Developer> options = manager.getPlanningDeveloperOptions(task, time);
        assertTrue( options.contains(dev) );
    }
    
    @Test
    public void planningResourceOptionsTest() {
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 8, 0);
        ResourceType machine = new ResourceType("schup");
        Requirement req = new Requirement(1, machine);
        Requirements reqs = new Requirements(new HashSet<>(Arrays.asList(req)));
        Task task = new Task(new TaskInfo("wortels planten", 120, 1, reqs, deps));
        
        Resource res = office.createResource("een goede schup", machine);
        
        Map<ResourceType, List<Resource>> options = manager.getPlanningResourceOptions(task, time);
        assertEquals(res, ((List<Resource>)options.get(machine)).get(0) );
    }
    
    @Test
    public void assignedTaskTest() throws ConflictingPlannedTaskException {
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 8, 0);
        Task task = new Task(new TaskInfo("knipogen naar de vrouwen voor 2 uur aan een stuk :o", 120, 0, reqs, deps));
        Resource dev = office.createDeveloper("bouwvakker");
        manager.createPlanning(task,new DateTimePeriod(time, time.plusMinutes(task.getEstimatedDuration())), new HashSet<>(Arrays.asList(dev)), false);
        
        Set<Task> tasks = new HashSet<>(Arrays.asList(task));
        assertEquals(1, manager.getAssignedTasksOf(tasks, (Developer) dev).size());
    }
}
