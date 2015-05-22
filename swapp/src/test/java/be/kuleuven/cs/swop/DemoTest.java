package be.kuleuven.cs.swop;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.company.BranchOffice;
import be.kuleuven.cs.swop.domain.company.Company;
import be.kuleuven.cs.swop.domain.company.ConflictingPlannedTaskException;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.domain.company.user.Manager;
import be.kuleuven.cs.swop.facade.TaskMan;


public class DemoTest {

    @Test
    public void test() throws FileNotFoundException {
        TaskMan t = buildDemoData();
        t.saveEverythingToFile("../save_files/import_me.json");
        
        TaskMan del = buildDelegationDemoData();
        del.saveEverythingToFile("../save_files/delegation_demo.json");
        
        TaskMan casc = buildCascadingConflictDemoData();
        casc.saveEverythingToFile("../save_files/cascading_demo.json");
    }
    
    
    @SuppressWarnings("unused")
    private TaskMan buildDemoData(){
        Company c = new Company();
        // Set Time
        LocalDateTime startTime1 = LocalDateTime.of(2015, 6, 1 , 9 , 0);
        c.updateSystemTime(startTime1);
        
        // Offices
        BranchOffice office1 = c.createBranchOffice("Location1");
        BranchOffice office2 = c.createBranchOffice("Location2");
        
        // Resource Types
        Set<ResourceType> requireSet = new HashSet<ResourceType>();
        Set<ResourceType> conflictSet = new HashSet<ResourceType>();
        TimePeriod dailyAvailability0 = new TimePeriod(LocalTime.of(12,00), LocalTime.of(17,00)); 

        ResourceType carType = c.createResourceType("Car", requireSet,conflictSet, false, null);
        ResourceType whiteBoardType = c.createResourceType("White Board", requireSet, conflictSet, false, null);
        conflictSet.add(whiteBoardType);
        ResourceType demoKitType = c.createResourceType("Demo Kit", requireSet, conflictSet, false, null);
        conflictSet.clear();
        requireSet.add(demoKitType);
        ResourceType conferenceRoomType = c.createResourceType("Conference Room", requireSet, conflictSet, true, null);
        requireSet.clear();
        ResourceType testingSetupType = c.createResourceType("Distributed Testing Setup", requireSet, conflictSet, false, null);
        ResourceType dataCenterType = c.createResourceType("Data Center", requireSet, conflictSet, false, dailyAvailability0);
        
        // Projects
        LocalDateTime endTime1 = LocalDateTime.of(2015, 6,  5, 18 , 0);
        LocalDateTime endTime2 = LocalDateTime.of(2015, 6,  22, 18 , 0);
        Project proj1 = office1.createProject("Project 1", "This is Project 1", startTime1, endTime1);
        Project proj2 = office1.createProject("Project 2", "This is Project 2", startTime1, endTime2);
        Project proj3 = office2.createProject("Project 3", "This is Project 3", startTime1, endTime1);
        
        // Resources
        
        // Office 1
        Resource car1 = office1.createResource("Car 1", carType);
        Resource car2 = office1.createResource("Car 2", carType);
        Resource whiteBoard1 = office1.createResource("White Board 1", whiteBoardType);
        Resource whiteBoard2 = office1.createResource("White Board 2", whiteBoardType);
        Resource demoKit1 = office1.createResource("Demo Kit 1", demoKitType);
        Resource demoKit2 = office1.createResource("Demo Kit 2", demoKitType);
        Resource conference1 = office1.createResource("The Big Conference Room", conferenceRoomType);
        Resource conference2 = office1.createResource("The Small Conference Room", conferenceRoomType);
        Resource testingSetup1 = office1.createResource("The Distributed Test Facility", testingSetupType);
        Resource dataCenter1 = office1.createResource("Data Center 1", dataCenterType);
        Developer dev1 = office1.createDeveloper("Ann");
        Developer dev2 = office1.createDeveloper("Bob");
        Developer dev3 = office1.createDeveloper("Charlie");
        Manager man1 = office1.createManager("Johan");

        
        // Office 2
        Resource car3 = office2.createResource("Car 3", carType);
        Resource car4 = office2.createResource("Car 4", carType);
        Resource whiteBoard3 = office2.createResource("White Board 3", whiteBoardType);
        Resource whiteBoard4 = office2.createResource("White Board 4", whiteBoardType);
        Resource demoKit3 = office2.createResource("Demo Kit 3", demoKitType);
        Resource demoKit4 = office2.createResource("Demo Kit 4", demoKitType);
        Resource conference3 = office2.createResource("The Big Conference Room #2", conferenceRoomType);
        Resource conference4 = office2.createResource("The Small Conference Room #2", conferenceRoomType);
        Resource testingSetup3 = office2.createResource("The Distributed Test Facility #2", testingSetupType);
        Resource dataCenter4 = office2.createResource("Data Center 2", dataCenterType);
        Developer dev4 = office2.createDeveloper("David");
        Developer dev5 = office2.createDeveloper("Evelyn");
        Developer dev6 = office2.createDeveloper("Fiona");
        Manager man2 = office2.createManager("Maria");
        
        // Tasks
        // Project 1
        Set<Task> deps = new HashSet<Task>();
        Set<Requirement> reqSet = new HashSet<Requirement>();
        reqSet.add(new Requirement(2, Developer.DEVELOPER_TYPE));
        reqSet.add(new Requirement(1, carType));
        reqSet.add(new Requirement(1, dataCenterType));
        Requirements reqs1 = new Requirements(reqSet);
        Task task1 = proj1.createTask("Upgrade server infrastructure", 120, 0, deps, reqs1);
        
        
        deps.add(task1);
        reqSet.clear();
        reqSet.add(new Requirement(1, Developer.DEVELOPER_TYPE));
        reqSet.add(new Requirement(1, conferenceRoomType));
        reqSet.add(new Requirement(1, demoKitType));
        Requirements reqs2 = new Requirements(reqSet);
        Task task2 = proj1.createTask("Prepare demo dataset", 90, 0, deps, reqs2);
        
        deps.clear();
        reqSet.clear();
        reqSet.add(new Requirement(1, Developer.DEVELOPER_TYPE));
        reqSet.add(new Requirement(1, conferenceRoomType));
        reqSet.add(new Requirement(1, demoKitType));
        Requirements reqs3 = new Requirements(reqSet);
        Task task3 = proj1.createTask("Install demo kit in conference room", 30, 0, deps, reqs3);

        deps.clear();
        deps.add(task2);
        deps.add(task3);
        reqSet.clear();
        reqSet.add(new Requirement(2, Developer.DEVELOPER_TYPE));
        reqSet.add(new Requirement(1, conferenceRoomType));
        reqSet.add(new Requirement(1, demoKitType));
        Requirements reqs4 = new Requirements(reqSet);
        Task task4 = proj1.createTask("Perform demo for clients", 60, 0, deps, reqs4);
        
        
        // Project 2
        deps.clear();
        reqSet.clear();
        reqSet.add(new Requirement(3, Developer.DEVELOPER_TYPE));
        reqSet.add(new Requirement(2, whiteBoardType));
        Requirements reqs5 = new Requirements(reqSet);
        Task task5 = proj2.createTask("Brainstorm session", 60, 0, deps, reqs5);
        
        // Project 3
        deps.clear();
        reqSet.clear();
        reqSet.add(new Requirement(1, Developer.DEVELOPER_TYPE));
        reqSet.add(new Requirement(1, testingSetupType));
        Requirements reqs6 = new Requirements(reqSet);
        Task task6 = proj3.createTask("Test the prototype", 180, 0, deps, reqs6);
            
        return new TaskMan(c);
    }
    
    
    @SuppressWarnings("unused")
    private TaskMan buildDelegationDemoData(){
        Company c = new Company();
        // Set Time
        LocalDateTime startTime1 = LocalDateTime.of(2015, 6, 1 , 9 , 0);
        c.updateSystemTime(startTime1);
        
        // Offices
        BranchOffice office1 = c.createBranchOffice("Location1");
        BranchOffice office2 = c.createBranchOffice("Location2");
        
        // Resource Types
        Set<ResourceType> requireSet = new HashSet<ResourceType>();
        Set<ResourceType> conflictSet = new HashSet<ResourceType>();
        TimePeriod dailyAvailability0 = new TimePeriod(LocalTime.of(12,00), LocalTime.of(17,00)); 
        
        ResourceType carType = c.createResourceType("Car", requireSet,conflictSet, false, null);
        
        // Projects
        LocalDateTime endTime1 = LocalDateTime.of(2015, 6,  5, 18 , 0);
        LocalDateTime endTime2 = LocalDateTime.of(2015, 6,  22, 18 , 0);
        Project proj1 = office1.createProject("Project 1", "This is Project 1", startTime1, endTime1);
        Project proj2 = office2.createProject("Project 2", "This is Project 2", startTime1, endTime2);
        
        // Resources
        
        // Office 1
        Resource car1 = office1.createResource("Car 1", carType);
        Developer dev1 = office1.createDeveloper("Ann");
        Developer dev2 = office1.createDeveloper("Bob");
        Developer dev3 = office1.createDeveloper("Charlie");
        Manager man1 = office1.createManager("Johan");
        
        
        // Office 2
        Resource car3 = office2.createResource("Car 3", carType);
        Developer dev4 = office2.createDeveloper("David");
        Manager man2 = office2.createManager("Maria");
        
        // Tasks
        // Project 1
        Set<Task> deps = new HashSet<Task>();
        Set<Requirement> reqSet = new HashSet<Requirement>();
        
        
        // Project 2
        deps.clear();
        reqSet.clear();
        reqSet.add(new Requirement(1, Developer.DEVELOPER_TYPE));
        reqSet.add(new Requirement(1, carType));
        Requirements reqs5 = new Requirements(reqSet);
        Set<Resource> ress = new HashSet<Resource>();
        ress.add(dev4);
        ress.add(car3);
        Task task5 = proj2.createTask("Prove P==NP", 60, 0, deps, reqs5);
        try {
            office2.createPlanning(task5, LocalDateTime.of(2015, 6, 1 , 10 , 0), ress);
        } catch (ConflictingPlannedTaskException e) {
            System.out.println("ERROR WHILE GENERATING DEMO");
            e.printStackTrace();
            return null;
        }
        
        
        return new TaskMan(c);
    }

    
    @SuppressWarnings("unused")
    private TaskMan buildCascadingConflictDemoData(){
        Company c = new Company();
        // Set Time
        LocalDateTime startTime1 = LocalDateTime.of(2015, 6, 1 , 9 , 0);
        c.updateSystemTime(startTime1);
        
        // Offices
        BranchOffice office1 = c.createBranchOffice("Location1");
        
        // Resource Types
        Set<ResourceType> requireSet = new HashSet<ResourceType>();
        Set<ResourceType> conflictSet = new HashSet<ResourceType>();
        TimePeriod dailyAvailability0 = new TimePeriod(LocalTime.of(12,00), LocalTime.of(17,00)); 
        
        ResourceType carType = c.createResourceType("Car", requireSet,conflictSet, false, null);
        
        // Projects
        LocalDateTime endTime1 = LocalDateTime.of(2015, 6,  5, 18 , 0);
        LocalDateTime endTime2 = LocalDateTime.of(2015, 6,  22, 18 , 0);
        Project proj1 = office1.createProject("Project 1", "This is Project 1", startTime1, endTime1);
        
        // Resources
        
        // Office 1
        Resource car1 = office1.createResource("Car 1", carType);
        Developer dev1 = office1.createDeveloper("Ann");
        Manager man1 = office1.createManager("Johan");
        
        // Tasks
        // Project 1
        Set<Task> deps = new HashSet<Task>();
        Set<Requirement> reqSet = new HashSet<Requirement>();
        
        
        // Project 2
        deps.clear();
        reqSet.clear();
        reqSet.add(new Requirement(1, Developer.DEVELOPER_TYPE));
        reqSet.add(new Requirement(1, carType));
        Requirements reqs5 = new Requirements(reqSet);
        Set<Resource> ress = new HashSet<Resource>();
        ress.add(dev1);
        ress.add(car1);
        Task task1 = proj1.createTask("Prove P==NP", 60, 0, deps, reqs5);
        Task task2 = proj1.createTask("Eat Pizza", 60, 0, deps, reqs5);
        Task task3 = proj1.createTask("Ask the Ultimate Question", 60, 0, deps, reqs5);
        Task task4 = proj1.createTask("Count to infinity", 60, 0, deps, reqs5);
        try {
            office1.createPlanning(task1, LocalDateTime.of(2015, 6, 1 , 10 , 0), ress);
            office1.createPlanning(task2, LocalDateTime.of(2015, 6, 1 , 11 , 0), ress);
            office1.createPlanning(task3, LocalDateTime.of(2015, 6, 1 , 12 , 0), ress);
        } catch (ConflictingPlannedTaskException e) {
            System.out.println("ERROR WHILE GENERATING DEMO");
            e.printStackTrace();
            return null;
        }
        
        
        return new TaskMan(c);
    }

}
