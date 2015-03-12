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
    public void flowTestFail() {
        facade.createProject(null);
    }
}
