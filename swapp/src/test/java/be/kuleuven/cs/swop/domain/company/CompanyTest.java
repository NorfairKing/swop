package be.kuleuven.cs.swop.domain.company;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.task.Task;


public class CompanyTest {
    // lost of functionality is just passthrough, not testing that again
    
    Company company;
    Project project;
    
    @Before
    public void Setup() {
        company = new Company();
        project = company.createProject("proj", "descr",
                LocalDateTime.of(2015, 1, 1, 8, 0), LocalDateTime.of(2015, 1, 1, 10, 0));
    }
    
    @Test
    public void getUnplannedTasksOfTest() throws ConflictingPlanningException {
        assertEquals(0, company.getUnplannedTasksOf(project).size());
        
        Task task = project.createTask("task", 10, 1);
        
        assertEquals(1, company.getUnplannedTasksOf(project).size());
        
        company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0), 
                new HashSet<>(), new HashSet<>());

        assertEquals(0, company.getUnplannedTasksOf(project).size());
    }
    
    @Test
    public void getDevelopersTest() {
        assertEquals(0, company.getDevelopers().size());
        
        company.createDeveloper("floris");

        assertEquals(1, company.getDevelopers().size());
    }
}
