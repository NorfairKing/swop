package be.kuleuven.cs.swop.domain.company.delegation;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import be.kuleuven.cs.swop.domain.company.AuthenticationToken;
import be.kuleuven.cs.swop.domain.company.BranchOffice;
import be.kuleuven.cs.swop.domain.company.Company;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.facade.BranchOfficeWrapper;
import be.kuleuven.cs.swop.facade.TaskMan;


public class DelegationOfficeTest {
    
    private Company company = new Company();
    private TaskMan taskMan = new TaskMan(company);
    private BranchOffice office1;
    private BranchOffice office2;
    private BranchOffice office3;
    private Project project;
    private Task delTask;
    private AuthenticationToken at;
    private Set<Task> deps;
    private Set<Requirement> reqSet;


    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        office1 = company.createBranchOffice("Office 1");
        office1.createManager("manager1");
        office2 = company.createBranchOffice("Office 2");
        office2.createManager("manager2");
        office3 = company.createBranchOffice("Office 3");
        office3.createManager("manager3");
        
        
        project = office1.createProject("Test Project", "To test with", LocalDateTime.now(), LocalDateTime.now().plusDays(5));
        
        ResourceType type1 = company.createResourceType("TestType", new HashSet<>(),  new HashSet<>(), false, null);
        Requirement req1 = new Requirement(2,type1);
        reqSet = new HashSet<Requirement>();
        reqSet.add(req1);
        
        Task depTask = project.createTask("Dep Task", 60, 0);
        deps = new HashSet<Task>();
        deps.add(depTask);

        delTask = project.createTask("Task 1", 60, 0, deps, new Requirements(reqSet));
    }

    @After
    public void tearDown() throws Exception {}
    
    
    private boolean haveSameInfo(Task task1, Task task2){
        if(!task1.getDescription().equals(task2.getDescription())){ return false;}
        if(task1.getEstimatedDuration() != task2.getEstimatedDuration()){ return false;}
        if(task1.getAcceptableDeviation() != task2.getAcceptableDeviation()){ return false;}
        // Can't have references to tasks in other offices, so having dependencies there doesn't make sense
//        if(!task1.getDependencySet().equals(task2.getDependencySet())){ return false; }
        if(!task1.getRequirements().equals(task2.getRequirements())){ return false; }
        return true;
    }
    
    private AuthenticationToken auth(BranchOffice office){
        taskMan.requestAuthenticationFor(new BranchOfficeWrapper(office), office.getUsers().stream().findFirst().get());
        return taskMan.getCurrentAuthenticationToken();
    }
    
    @Test
    public void simpleDelegationTest(){
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertFalse(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 0);
        
        company.delegateTask(delTask, office2);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertTrue(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 1);
        
        assertTrue(office2.getDelegationProject().getTasks().contains(delTask.getDelegation().getDelegationTask()));
        assertTrue(haveSameInfo(delTask, delTask.getDelegation().getDelegationTask()));
    }
    
    @Test
    public void chainDelegationTest(){
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertFalse(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 0);
        assertTrue(office3.getDelegationProject().getTasks().size() == 0);
        
        company.delegateTask(delTask, office2);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertTrue(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 1);
        assertTrue(office3.getDelegationProject().getTasks().size() == 0);
        
        Task task2 = delTask.getDelegation().getDelegationTask();
        
        assertTrue(office2.getDelegationProject().getTasks().contains(task2));
        assertTrue(haveSameInfo(delTask, task2));
        
        company.delegateTask(task2, office3);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertTrue(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 1);
        assertTrue(office3.getDelegationProject().getTasks().size() == 1);
        
        Task task3 = task2.getDelegation().getDelegationTask();
        
        assertTrue(office3.getDelegationProject().getTasks().contains(task3));
        assertTrue(haveSameInfo(delTask, task3));
    }
    
    @Test
    public void simpleSimulationDelegationTest(){
        at = auth(office1);
        company.startSimulationFor(at);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertFalse(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 0);
        
        
        company.delegateTask(delTask, office2);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertTrue(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 0);
        
        company.realizeSimulationFor(at);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertTrue(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 1);
        
        assertTrue(office2.getDelegationProject().getTasks().contains(delTask.getDelegation().getDelegationTask()));
        assertTrue(haveSameInfo(delTask, delTask.getDelegation().getDelegationTask()));
    }
    
    @Test
    public void complexSimulationDelegationTest(){
        
        Task delTask2 = project.createTask("Task 2", 120, 0.5, deps, new Requirements(reqSet));

        at = auth(office1);
        company.startSimulationFor(at);

        at = auth(office3);
        company.startSimulationFor(at);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertFalse(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 0);
        
        assertFalse(delTask2.isExecuting());
        assertFalse(delTask2.isFailed());
        assertFalse(delTask2.isFinished());
        assertFalse(delTask2.isFinal());
        assertFalse(delTask2.isDelegated());
        assertTrue(office3.getDelegationProject().getTasks().size() == 0);
        
        
        company.delegateTask(delTask, office2);
        company.delegateTask(delTask2, office3);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertTrue(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 0);
        
        assertFalse(delTask2.isExecuting());
        assertFalse(delTask2.isFailed());
        assertFalse(delTask2.isFinished());
        assertFalse(delTask2.isFinal());
        assertTrue(delTask2.isDelegated());
        assertTrue(office3.getDelegationProject().getTasks().size() == 0);
        
        at = auth(office1);
        company.realizeSimulationFor(at);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertTrue(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 1);
        
        assertFalse(delTask2.isExecuting());
        assertFalse(delTask2.isFailed());
        assertFalse(delTask2.isFinished());
        assertFalse(delTask2.isFinal());
        assertTrue(delTask2.isDelegated());
        assertTrue(office3.getDelegationProject().getTasks().size() == 0);
        
        assertTrue(office2.getDelegationProject().getTasks().contains(delTask.getDelegation().getDelegationTask()));
        assertTrue(haveSameInfo(delTask, delTask.getDelegation().getDelegationTask()));
        
        at = auth(office3);
        company.realizeSimulationFor(at);
        
        assertFalse(delTask2.isExecuting());
        assertFalse(delTask2.isFailed());
        assertFalse(delTask2.isFinished());
        assertFalse(delTask2.isFinal());
        assertTrue(delTask2.isDelegated());
        assertTrue(office3.getDelegationProject().getTasks().size() == 1);
        
        assertTrue(office3.getDelegationProject().getTasks().contains(delTask2.getDelegation().getDelegationTask()));
        assertTrue(haveSameInfo(delTask2, delTask2.getDelegation().getDelegationTask()));
    }
    
    @Test
    public void simpleSimulationRollbackDelegationTest(){
        at = auth(office1);
        company.startSimulationFor(at);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertFalse(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 0);
        
        
        company.delegateTask(delTask, office2);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertTrue(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 0);
        
        company.cancelSimulationFor(at);
        
        assertTrue(office2.getDelegationProject().getTasks().size() == 0);
    }
    
    @Test
    public void complexSimulationDelegationRollbackTest(){
        
        Task delTask2 = project.createTask("Task 2", 120, 0.5, deps, new Requirements(reqSet));
        
        at = auth(office1);
        company.startSimulationFor(at);
        
        at = auth(office3);
        company.startSimulationFor(at);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertFalse(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 0);
        
        assertFalse(delTask2.isExecuting());
        assertFalse(delTask2.isFailed());
        assertFalse(delTask2.isFinished());
        assertFalse(delTask2.isFinal());
        assertFalse(delTask2.isDelegated());
        assertTrue(office3.getDelegationProject().getTasks().size() == 0);
        
        
        company.delegateTask(delTask, office2);
        company.delegateTask(delTask2, office3);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertTrue(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 0);
        
        assertFalse(delTask2.isExecuting());
        assertFalse(delTask2.isFailed());
        assertFalse(delTask2.isFinished());
        assertFalse(delTask2.isFinal());
        assertTrue(delTask2.isDelegated());
        assertTrue(office3.getDelegationProject().getTasks().size() == 0);
        
        at = auth(office1);
        company.realizeSimulationFor(at);
        
        assertFalse(delTask.isExecuting());
        assertFalse(delTask.isFailed());
        assertFalse(delTask.isFinished());
        assertFalse(delTask.isFinal());
        assertTrue(delTask.isDelegated());
        assertTrue(office2.getDelegationProject().getTasks().size() == 1);
        
        assertFalse(delTask2.isExecuting());
        assertFalse(delTask2.isFailed());
        assertFalse(delTask2.isFinished());
        assertFalse(delTask2.isFinal());
        assertTrue(delTask2.isDelegated());
        assertTrue(office3.getDelegationProject().getTasks().size() == 0);
        
        assertTrue(office2.getDelegationProject().getTasks().contains(delTask.getDelegation().getDelegationTask()));
        assertTrue(haveSameInfo(delTask, delTask.getDelegation().getDelegationTask()));
        
        at = auth(office3);
        company.cancelSimulationFor(at);
        
        assertFalse(delTask2.isExecuting());
        assertFalse(delTask2.isFailed());
        assertFalse(delTask2.isFinished());
        assertFalse(delTask2.isFinal());
        assertTrue(delTask2.isDelegated());
        assertTrue(office3.getDelegationProject().getTasks().size() == 1);
        
        assertTrue(office3.getDelegationProject().getTasks().contains(delTask2.getDelegation().getDelegationTask()));
        assertTrue(haveSameInfo(delTask2, delTask2.getDelegation().getDelegationTask()));
    }

}
