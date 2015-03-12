package be.kuleuven.cs.swop.domain.project;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.task.Task;


public class ProjectTest {

    private Project project;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        project = new Project("test", "desc", new Date(10), new Date(600010));
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void canHaveAsTitleTest(){
        assertTrue(project.canHaveAsTitle("test title"));
        assertFalse(project.canHaveAsTitle("test \n title"));
    }

    @Test
    public void setTitleTest(){
        project.setTitle("test title");
        String title = project.getTitle();
        assertTrue("test title".equals(title));
        
        exception.expect(IllegalArgumentException.class);
        project.setTitle("test \n title");
    }

    @Test
    public void canHaveAsDescriptionTest(){
        assertTrue(project.canHaveAsDescription("test description \n with lines"));
        assertFalse(project.canHaveAsDescription(null));
    }

    @Test
    public void setDescriptionTest(){
        project.setDescription("test description \n with lines");
        assertTrue("test description \n with lines".equals(project.getDescription()));
        
        
        exception.expect(IllegalArgumentException.class);
        project.setDescription(null);
    }

    @Test
    public void isOngoingTest(){
        assertTrue(project.isOngoing());
        Task test = project.createTask("desc", 1, 0);
        assertTrue(project.isOngoing());
        test.finish(new TimePeriod(new Date(1), new Date(10)));
        assertFalse(project.isOngoing());

    }
    @Test
    public void isFinishedTest(){
        assertFalse(project.isFinished());
        Task test = project.createTask("desc", 1, 0);
        assertFalse(project.isFinished());
        test.finish(new TimePeriod(new Date(1), new Date(10)));
        assertTrue(project.isFinished());

    }

    @Test
    public void canHaveAsCreationTimeTest(){
        assertFalse(project.canHaveAsCreationTime(null));
        assertTrue(project.canHaveAsCreationTime(new Date(1)));
    }

    @Test
    public void canHaveAsDueTimeTest(){
        assertFalse(project.canHaveAsDueTime(null));
        assertFalse(project.canHaveAsDueTime(new Date(1)));
        assertTrue(project.canHaveAsDueTime(new Date(15)));
    }

    @Test
    public void setDueTimeTest(){
        try{   
            project.setDueTime(null);
            fail();
        }catch(IllegalArgumentException e){}
        
        try{   
            project.setDueTime(new Date(1));
            fail();
        }catch(IllegalArgumentException e){}
        
        Date date = new Date(15);
        project.setDueTime(date);
        assertEquals(date, project.getDueTime());
    }

    @Test
    public void createTaskTest(){
        Task test = project.createTask("desc", 10, 0);
        assertTrue(project.getTasks().contains(test));

    }

}
