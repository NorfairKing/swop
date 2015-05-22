package be.kuleuven.cs.swop.domain.company;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.task.TaskInfo;


public class BranchOfficeTest {
    
    private BranchOffice office;
    private Company company = new Company();

    @Before
    public void setUp() throws Exception {
        office = new BranchOffice("Office 1", company);
    }


    @Test
    public void canHaveAsProject() {
        assertFalse(office.canHaveAsProject(null));
        assertTrue(office.canHaveAsProject(new Project("test", "test desc", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10))));
    }

    @Test
    public void createProjectValidTest() {
        Project project = office.createProject("test", "test desc", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10));
        assertTrue(office.getProjects().contains(project));
    }
    
    @Test
    public void getTaskForProjectTest() {
        Project project = office.createProject("test", "test desc", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10));
        Task task = project.createTask("task", 10, 1);
        
        assertEquals(project, office.getProjectFor(task));
        assertEquals(null, office.getProjectFor(new Task(new TaskInfo("non-existant task", 10, 1, new Requirements(new HashSet<>()), new HashSet<>()))));
        assertEquals(null, office.getProjectFor(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProjectInvalidTitleTest() {
        office.createProject(null, "test desc", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProjectInvalidDescriptionTest() {
        office.createProject("test", null, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProjectInvalidCreationTimeTest() {
        office.createProject("test", "test desc", null, LocalDateTime.now().plusHours(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProjectInvalidDueTimeTest() {
        office.createProject("test", "test desc", LocalDateTime.now().plusHours(1), null);
    }

}
