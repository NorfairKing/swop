package be.kuleuven.cs.swop;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.SimulationStepData;
import be.kuleuven.cs.swop.facade.TaskMan;


public class SimulationSessionTest {
    
    private static TestingUI  ui;
    private static TaskMan taskMan;
    private static SessionController controller;
    
    @Before
    public void setUp() {
        ui = new TestingUI();
        taskMan = new TaskMan();
        controller = new SessionController(ui, taskMan);
        ui.start();
    }
    
    @Test
    public void testCancel() {
        taskMan.updateSystemTime(LocalDateTime.of(2015, 1, 1, 8, 0));
        
        // start simulation
        controller.startRunSimulationSession();
        
        // add project
        ui.setSimulationStepData(new SimulationStepData(true, false));
        ui.setRequestProjectDate(new ProjectData("Een project", "Een description", LocalDateTime.of(2015, 1, 2, 8, 0)));
        controller.startCreateProjectSession();
        
        assertEquals(taskMan.getProjects().size(), 1);
        
        // start and cancel it, so nothing changes and we can cancel the simulation
        ui.setSimulationStepData(new SimulationStepData(false, false));
        ui.setRequestProject(null);
        controller.startShowProjectsSession();
        
        // project should be gone now
        assertEquals(taskMan.getProjects().size(), 0);
    }
    
    @Test
    public void testRealize() {
        taskMan.updateSystemTime(LocalDateTime.of(2015, 1, 1, 8, 0));
        
        // start simulation
        controller.startRunSimulationSession();
        
        // add project
        ui.setSimulationStepData(new SimulationStepData(true, false));
        ui.setRequestProjectDate(new ProjectData("Een project", "Een description", LocalDateTime.of(2015, 1, 2, 8, 0)));
        controller.startCreateProjectSession();
        
        assertEquals(taskMan.getProjects().size(), 1);
        
        // start and cancel it, so nothing changes and we can cancel the simulation
        ui.setSimulationStepData(new SimulationStepData(false, true));
        ui.setRequestProject(null);
        controller.startShowProjectsSession();
        
        // nothing should have changed
        assertEquals(taskMan.getProjects().size(), 1);
    } 
}
