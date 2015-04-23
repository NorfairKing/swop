package be.kuleuven.cs.swop.domain.company;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;


public class PlanningManagerTest {
    PlanningManager manager;
    
    @Before
    public void Setup() {
        manager = new PlanningManager();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addInvaledPlanningTest() {
        manager.addPlanning(null);
    }
    
    @Test
    public void addProperPlanningTest() {
        assertEquals(0, manager.getTaskPlannings().size());
        
        TaskPlanning planning = new TaskPlanning(
                new HashSet<>(), new Task("taskie", 10, 1), 
                LocalDateTime.of(2000, 1, 1, 8, 0));
        manager.addPlanning(planning);
        
        assertEquals(1, manager.getTaskPlannings().size());
        assertTrue(manager.getTaskPlannings().contains(planning));
    }
    
    @Test
    public void busyDeveloperTest() {
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 8, 0);
        Task task = new Task("werkje voor de grompot", 120, 1);
        Developer dev = new Developer("de grompot");
        
        TaskPlanning planning = new TaskPlanning(
                new HashSet<>(Arrays.asList(dev)), task, 
                time);
        manager.addPlanning(planning);
        
        Task otherTask = new Task("ander werkje", 60, 1);

        assertFalse(manager.isAvailableFor(dev, otherTask, time));
        assertFalse(manager.isAvailableFor(dev, otherTask, time.plusHours(1)));
        
        assertTrue(manager.isAvailableFor(dev, otherTask, time.plusHours(2)));
        assertTrue(manager.isAvailableFor(dev, otherTask, time.minusHours(1)));
    }
    
    @Test
    public void planningTimesTest() {
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 8, 0);
        Task task = new Task("brijen", 120, 1);
        Developer dev = manager.createDeveloper("grootmoe");

        // nothing planned yet, so we get as many time options as we ask for
        assertEquals(3, manager.getPlanningTimeOptions(task, 3, time).size());
        assertEquals(5, manager.getPlanningTimeOptions(task, 5, time).size());
        
        ResourceType machine = new ResourceType("brij machien");
        Requirement req = new Requirement(1, machine);
        Task hardTask = new Task("brijen met machien", 120, 1,
                new HashSet<>(Arrays.asList(req)));
        
        // the system doesn't have a machine, so can't find any time spot where we can plan this
        assertEquals(0, manager.getPlanningTimeOptions(hardTask, 3, time).size());
        
        TaskPlanning planning = new TaskPlanning(new HashSet<>(Arrays.asList(dev)), task, time);
        manager.addPlanning(planning);

        // still no machine
        assertEquals(0, manager.getPlanningTimeOptions(hardTask, 3, time).size());
        Task task2 = new Task("nog brijen", 120, 1);
        // should get 3 time options still, but they'll be a little later
        assertEquals(3, manager.getPlanningTimeOptions(task2, 3, time).size());
    }
    
    @Test
    public void planningDeveloperOptionsTest() {
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 8, 0);
        ResourceType machine = new ResourceType("schup");
        Requirement req = new Requirement(1, machine);
        Task task = new Task("wortels planten", 120, 1,
                new HashSet<>(Arrays.asList(req)));
        Developer dev = manager.createDeveloper("grootva");
        
        Set<Developer> options = manager.getPlanningDeveloperOptions(task, time);
        assertTrue( options.contains(dev) );
    }
    
    @Test
    public void planningResourceOptionsTest() {
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 8, 0);
        ResourceType machine = new ResourceType("schup");
        Requirement req = new Requirement(1, machine);
        Task task = new Task("wortels planten", 120, 1,
                new HashSet<>(Arrays.asList(req)));
        
        Resource res = manager.createResource("een goede schup", machine);
        
        Map<ResourceType, List<Resource>> options = manager.getPlanningResourceOptions(task, time);
        assertEquals(res, ((List<Resource>)options.get(machine)).get(0) );
    }
    
    @Test
    public void assignedTaskTest() {
        LocalDateTime time = LocalDateTime.of(2000, 1, 1, 8, 0);
        Task task = new Task("knipogen naar de vrouwen voor 2 uur aan een stuk :o", 120, 0);
        Developer dev = manager.createDeveloper("bouwvakker");
        TaskPlanning planning = new TaskPlanning(new HashSet<>(Arrays.asList(dev)), task, time);
        manager.addPlanning(planning);
        
        Set<Task> tasks = new HashSet<>(Arrays.asList(task));
        assertEquals(1, manager.getAssignedTasksOf(tasks, dev).size());
    }
}
