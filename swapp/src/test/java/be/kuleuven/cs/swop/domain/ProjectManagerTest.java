package be.kuleuven.cs.swop.domain;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.domain.project.Project;


public class ProjectManagerTest {

    private ProjectManager projectManager;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        projectManager = new ProjectManager("");
    }

    @After
    public void tearDown() throws Exception {}


    @Test
    public void canHaveAsProject(){
        assertFalse(projectManager.canHaveAsProject(null));
        assertTrue(projectManager.canHaveAsProject(new Project("test", "test desc", new Date(1), new Date(10))));
    }

    @Test
    public void createProjectTest(){
        Project project = projectManager.createProject("test", "test desc", new Date(1), new Date(10));
        assertTrue(projectManager.getProjects().contains(project));

        try{   
            projectManager.createProject(null, "test desc", new Date(1), new Date(10));
            fail();
        }catch(IllegalArgumentException e){}

        try{   
            projectManager.createProject("test", null, new Date(1), new Date(10));
            fail();
        }catch(IllegalArgumentException e){}   

        try{   
            projectManager.createProject("test", "test desc", null, new Date(10));
            fail();
        }catch(IllegalArgumentException e){}

        try{   
            projectManager.createProject("test", "test desc", new Date(1), null);
            fail();
        }catch(IllegalArgumentException e){}
    }

    @Test
    public void readProjectsFromFileTest() {
        ProjectManager pm = new ProjectManager("src/test/java/be/kuleuven/cs/swop/defaulttest.tman");
        
        assertEquals(pm.getProjects().size(), 3);
        
        Project projectX = pm.getProjects().stream()
                .filter(p -> p.getTitle().equals("project x"))
                .findFirst().get();
        Project projectY = pm.getProjects().stream()
                .filter(p -> p.getTitle().equals("project y"))
                .findFirst().get();
        Project projectZ = pm.getProjects().stream()
                .filter(p -> p.getTitle().equals("project z"))
                .findFirst().get();

        assertEquals(projectX.getTasks().size(), 1);
        assertEquals(projectY.getTasks().size(), 4);
        assertEquals(projectZ.getTasks().size(), 2);
    }
}
