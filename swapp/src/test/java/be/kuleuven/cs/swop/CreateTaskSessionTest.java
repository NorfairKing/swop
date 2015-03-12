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
    
    @Test
    public void flowTest() {
        ProjectWrapper project = facade.getProjects().stream().findFirst().get();
        TaskData data = new TaskData("Descr", 50, .5);
        
        int taskCountBefore = project.getTasks().size();
        
        facade.createTaskFor(project, data);

        int taskCountAfter = project.getTasks().size();
        
        assertEquals(taskCountAfter, taskCountBefore + 1);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestNull() {
        ProjectWrapper project = facade.getProjects().stream().findFirst().get();
        
        facade.createTaskFor(project, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestNullDescr() {
        ProjectWrapper project = facade.getProjects().stream().findFirst().get();
        String descr = null;
        double dur = 50;
        double dev = .5;
        TaskData data = new TaskData(descr, dur, dev);
        
        facade.createTaskFor(project, data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestInvalidDur() {
        ProjectWrapper project = facade.getProjects().stream().findFirst().get();
        String descr = "Good descr";
        double dur = -50; //can't be negative
        double dev = .5;
        TaskData data = new TaskData(descr, dur, dev);
        
        facade.createTaskFor(project, data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestInvalidDev() {
        ProjectWrapper project = facade.getProjects().stream().findFirst().get();
        String descr = "Good descr";
        double dur = 50;
        double dev = -.5; //can't be negative
        TaskData data = new TaskData(descr, dur, dev);
        
        facade.createTaskFor(project, data);
    }
}
