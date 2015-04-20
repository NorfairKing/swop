package be.kuleuven.cs.swop.domain;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.domain.company.ProjectManager;
import be.kuleuven.cs.swop.domain.company.project.Project;


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
        projectManager = new ProjectManager();
    }

    @After
    public void tearDown() throws Exception {}


    @Test
    public void canHaveAsProject(){
        assertFalse(projectManager.canHaveAsProject(null));
        assertTrue(projectManager.canHaveAsProject(new Project("test", "test desc", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10))));
    }

    @Test
    public void createProjectTest(){
        Project project = projectManager.createProject("test", "test desc", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10));
        assertTrue(projectManager.getProjects().contains(project));

        try{   
            projectManager.createProject(null, "test desc", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10));
            fail();
        }catch(IllegalArgumentException e){}

        try{   
            projectManager.createProject("test", null, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10));
            fail();
        }catch(IllegalArgumentException e){}   

        try{   
            projectManager.createProject("test", "test desc", null, LocalDateTime.now().plusHours(10));
            fail();
        }catch(IllegalArgumentException e){}

        try{   
            projectManager.createProject("test", "test desc", LocalDateTime.now().plusHours(1), null);
            fail();
        }catch(IllegalArgumentException e){}
    }
}
