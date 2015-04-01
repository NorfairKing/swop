package be.kuleuven.cs.swop;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.facade.TaskMan;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.TaskData;


public class CreateTaskSessionTest {
    private static TestingUI  ui;
    private static TaskMan taskMan;
    private static SessionController controller;
    
    @Before
    public void setUp() throws Exception {
        ui = new TestingUI();
        taskMan = new TaskMan();
        controller = new SessionController(ui, taskMan);
        
        taskMan.createProject(new ProjectData("Title", "Descr", taskMan.getSystemTime().plusHours(1)));
        
        ui.start();
    }
    
    @Test
    public void test() {
        ProjectWrapper project = taskMan.getProjects().stream().findFirst().get();
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
        ProjectWrapper project = taskMan.getProjects().stream().findFirst().get();
        TaskData data = new TaskData("Descr", 50, .5);
        
        int taskCountBefore = project.getTasks().size();
        
        taskMan.createTaskFor(project, data);

        int taskCountAfter = project.getTasks().size();
        
        assertEquals(taskCountAfter, taskCountBefore + 1);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestNull() {
        ProjectWrapper project = taskMan.getProjects().stream().findFirst().get();
        
        taskMan.createTaskFor(project, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestNullDescr() {
        ProjectWrapper project = taskMan.getProjects().stream().findFirst().get();
        String descr = null;
        double dur = 50;
        double dev = .5;
        TaskData data = new TaskData(descr, dur, dev);
        
        taskMan.createTaskFor(project, data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestInvalidDur() {
        ProjectWrapper project = taskMan.getProjects().stream().findFirst().get();
        String descr = "Good descr";
        double dur = -50; //can't be negative
        double dev = .5;
        TaskData data = new TaskData(descr, dur, dev);
        
        taskMan.createTaskFor(project, data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestInvalidDev() {
        ProjectWrapper project = taskMan.getProjects().stream().findFirst().get();
        String descr = "Good descr";
        double dur = 50;
        double dev = -.5; //can't be negative
        TaskData data = new TaskData(descr, dur, dev);
        
        taskMan.createTaskFor(project, data);
    }
}
