package be.kuleuven.cs.swop;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.domain.Timekeeper;


public class CreateProjectSessionTest {
    
    private static TestingUI  ui;
    private static FacadeController facade;
    private static SessionController controller;
    
    @Before
    public void setUp() throws Exception {
        ui = new TestingUI();
        facade = new FacadeController();
        controller = new SessionController(ui, facade);
        ui.start();
    }
    
    @Test
    public void test() {
        String title = "This is a title";
        String descr = "And this a description";
        Date time = new Date(Timekeeper.getTime().getTime() + 1000);
        ProjectData data = new ProjectData(title, descr, time);
        ui.setRequestProjectDate(data);
        
        int beforeProjectCount = facade.getProjects().size();
        
        controller.startCreateProjectSession();
        
        int afterProjectCount = facade.getProjects().size();
        
        assertEquals(afterProjectCount, beforeProjectCount + 1);
    }
    
    @Test
    public void flowTest() {
        Date time = new Date(Timekeeper.getTime().getTime() + 1000);
        ProjectData data = new ProjectData("Tit", "D", time);
        
        int beforeProjectCount = facade.getProjects().size();
        
        facade.createProject(data);
        
        int afterProjectCount = facade.getProjects().size();
        
        assertEquals(afterProjectCount, beforeProjectCount + 1);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestNull() {
        // The ui returns null as project data
        facade.createProject(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestInvalidTitle() {
        String title = "Invalid Title with newline: \n gnegne";
        String descr = "Good description";
        Date time = new Date(Timekeeper.getTime().getTime() + 1000);
        ProjectData data = new ProjectData(title, descr, time);
        
        // The user returns invalid project data
        facade.createProject(data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestInvalidDescr() {
        String title = "Good title";
        String descr = null;
        Date time = new Date(Timekeeper.getTime().getTime() + 1000);
        ProjectData data = new ProjectData(title, descr, time);
        
        // The user returns invalid project data
        facade.createProject(data);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void flowTestInvalidDue() {
        String title = "Good title";
        String descr = "Good descr";
        Date time = new Date(Timekeeper.getTime().getTime() - 1000); //due before creation.
        ProjectData data = new ProjectData(title, descr, time);
        
        // The user returns invalid project data
        facade.createProject(data);
    }
}
