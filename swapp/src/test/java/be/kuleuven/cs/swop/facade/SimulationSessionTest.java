package be.kuleuven.cs.swop.facade;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.SimulationStepData;


public class SimulationSessionTest extends BaseFacadeTest {
    
    @Before
    public void setUp() {
        simpleSetup();
    }
    
    @Test
    public void testCancel() {
        taskMan.updateSystemTime(LocalDateTime.of(2015, 1, 1, 8, 0));

        int projectCountBefore = taskMan.getProjects().size();
        
        // start simulation
        controller.startRunSimulationSession();
        
        // add project
        ui.addSimulationStepData(new SimulationStepData(true, false));
        ui.addRequestProjectDate(new ProjectData("Een project", "Een description", LocalDateTime.of(2015, 1, 2, 8, 0)));
        controller.startCreateProjectSession();
        
        assertEquals(projectCountBefore + 1, taskMan.getProjects().size());
        
        // start and cancel it, so nothing changes and we can cancel the simulation
        ui.addSimulationStepData(new SimulationStepData(false, false));
        ui.addRequestProject(null);
        controller.startShowProjectsSession();
        
        // project should be gone now
        assertEquals(projectCountBefore, taskMan.getProjects().size());
    }
    
    @Test
    public void testRealize() {
        taskMan.updateSystemTime(LocalDateTime.of(2015, 1, 1, 8, 0));
        
        int projectCountBefore = taskMan.getProjects().size();
        
        // start simulation
        controller.startRunSimulationSession();
        
        // add project
        ui.addSimulationStepData(new SimulationStepData(true, false));
        ui.addRequestProjectDate(new ProjectData("Een project", "Een description", LocalDateTime.of(2015, 1, 2, 8, 0)));
        controller.startCreateProjectSession();
        
        assertEquals(projectCountBefore + 1, taskMan.getProjects().size());
        
        // start and realize it, so nothing changes and we can cancel the simulation
        ui.addSimulationStepData(new SimulationStepData(false, true));
        ui.addRequestProject(null);
        controller.startShowProjectsSession();
        
        // nothing should have changed
        assertEquals(projectCountBefore + 1, taskMan.getProjects().size());
    } 
}
