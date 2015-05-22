package be.kuleuven.cs.swop.domain.company;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.User;


public class CompanyTest {
    // lost of functionality is just passthrough, not testing that again
    
    Company company;
    BranchOffice office;
    User user;
    AuthenticationToken token;
    Project project;
    
    @Before
    public void Setup() {
    	company = new Company();
    	office = company.createBranchOffice("Here");
    	user = company.getOffices().stream().findFirst().get().createDeveloper("Abc");
    	token = new AuthenticationToken(office, user);
        project = company.createProject("proj", "descr",
                LocalDateTime.of(2015, 1, 1, 8, 0), LocalDateTime.of(2015, 1, 1, 10, 0), token);
    }
    
    @Test
    public void getUnplannedTasksOfTest() throws ConflictingPlannedTaskException {
        assertEquals(0, company.getUnplannedTasksOf(project, token).size());
        
        Task task = project.createTask("task", 10, 1);
        
        assertEquals(1, company.getUnplannedTasksOf(project, token).size());
        
        company.createPlanning(task, LocalDateTime.of(2015, 1, 1, 8, 0), 
                new HashSet<>(), token);

        assertEquals(0, company.getUnplannedTasksOf(project, token).size());
    }
    
    @Test
    public void getDevelopersTest() {
        assertEquals(1, company.getDevelopers(token).size());
        
        company.createDeveloper("floris", token);

        assertEquals(2, company.getDevelopers(token).size());
    }
}
