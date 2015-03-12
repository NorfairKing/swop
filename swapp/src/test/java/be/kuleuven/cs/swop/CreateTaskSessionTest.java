package be.kuleuven.cs.swop;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.domain.Timekeeper;


public class CreateTaskSessionTest {
    private static TestingUI  ui;
    private static FacadeController facade;
    private static SessionController controller;
    
    @Before
    public void setUp() throws Exception {
        ui = new TestingUI();
        facade = new FacadeController();
        controller = new SessionController(ui, facade);
        
        facade.createProject(new ProjectData("Title", "Descr", new Date(Timekeeper.getTime().getTime() + 1)));
        
        ui.start();
    }
    
    @Test
    public void test() {
        ProjectWrapper project = facade.getProjects().stream().findFirst().get();
        ui.setRequestProject(project);
        
        TaskData data = new TaskData("Descr", 50, .5);
        ui.setRequestTaskData(data);
        
        int taskCountBefore = project.getTasks().size();
        
        controller.startCreateTaskSession();
        
        int taskCountAfter = project.getTasks().size();
        
        assertEquals(taskCountAfter, taskCountBefore + 1);
    }
}
