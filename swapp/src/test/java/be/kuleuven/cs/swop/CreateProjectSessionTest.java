package be.kuleuven.cs.swop;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.facade.TaskMan;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.SessionController;

public class CreateProjectSessionTest {
    
    private static TestingUI  ui;
    private static TaskMan taskMan;
    private static SessionController controller;
    
    @Before
    public void setUp() throws Exception {
        ui = new TestingUI();
        taskMan = new TaskMan();
        controller = new SessionController(ui, taskMan);
        ui.start();
    }
    
    @Test
    public void test() {
        String title = "This is a title";
        String descr = "And this a description";
        LocalDateTime time = taskMan.getSystemTime().plusHours(1);
        ProjectData data = new ProjectData(title, descr, time);
        ui.setRequestProjectDate(data);
        
        int beforeProjectCount = taskMan.getProjects().size();
        
        controller.startCreateProjectSession();
        
        int afterProjectCount = taskMan.getProjects().size();
        
        assertEquals(afterProjectCount, beforeProjectCount + 1);
    }
    
    @Test
    public void flowTest() {
        LocalDateTime time = taskMan.getSystemTime().plusHours(1);
        ProjectData data = new ProjectData("Tit", "D", time);
        
        int beforeProjectCount = taskMan.getProjects().size();
        
        taskMan.createProject(data);
        
        int afterProjectCount = taskMan.getProjects().size();
        
        assertEquals(afterProjectCount, beforeProjectCount + 1);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestNull() {
        // The ui returns null as project data
        taskMan.createProject(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestInvalidTitle() {
        String title = "Invalid Title with newline: \n gnegne";
        String descr = "Good description";
        LocalDateTime time = taskMan.getSystemTime().plusHours(1000);
        ProjectData data = new ProjectData(title, descr, time);
        
        // The user returns invalid project data
        taskMan.createProject(data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestInvalidDescr() {
        String title = "Good title";
        String descr = null;
        LocalDateTime time = taskMan.getSystemTime().plusHours(1);
        ProjectData data = new ProjectData(title, descr, time);
        
        // The user returns invalid project data
        taskMan.createProject(data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestInvalidDue() {
        String title = "Good title";
        String descr = "Good descr";
        LocalDateTime time = taskMan.getSystemTime().minusHours(1); //due before creation.
        ProjectData data = new ProjectData(title, descr, time);
        
        // The user returns invalid project data
        taskMan.createProject(data);
    }
}
