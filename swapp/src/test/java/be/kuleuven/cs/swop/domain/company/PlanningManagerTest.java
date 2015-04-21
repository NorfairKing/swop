package be.kuleuven.cs.swop.domain.company;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.task.Task;


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
}
