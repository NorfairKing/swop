package be.kuleuven.cs.swop.domain.company;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.task.Task;


public class ProjectManagerTest {

    private ProjectManager projectManager;

    @Before
    public void setUp() throws Exception {
        projectManager = new ProjectManager();
    }

    @Test
    public void canHaveAsProject() {
        assertFalse(projectManager.canHaveAsProject(null));
        assertTrue(projectManager.canHaveAsProject(new Project("test", "test desc", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10))));
    }

    @Test
    public void createProjectValidTest() {
        Project project = projectManager.createProject("test", "test desc", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10));
        assertTrue(projectManager.getProjects().contains(project));
    }
    
    @Test
    public void getTaskForProjectTest() {
        Project project = projectManager.createProject("test", "test desc", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10));
        Task task = project.createTask("task", 10, 1);
        
        assertEquals(project, projectManager.getProjectFor(task));
        assertEquals(null, projectManager.getProjectFor(new Task("non-existant task", 10, 1)));
        assertEquals(null, projectManager.getProjectFor(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProjectInvalidTitleTest() {
        projectManager.createProject(null, "test desc", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProjectInvalidDescriptionTest() {
        projectManager.createProject("test", null, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProjectInvalidCreationTimeTest() {
        projectManager.createProject("test", "test desc", null, LocalDateTime.now().plusHours(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProjectInvalidDueTimeTest() {
        projectManager.createProject("test", "test desc", LocalDateTime.now().plusHours(1), null);
    }
}
