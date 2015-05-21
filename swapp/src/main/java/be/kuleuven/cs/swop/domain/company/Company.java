package be.kuleuven.cs.swop.domain.company;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.company.delegation.Delegation;
import be.kuleuven.cs.swop.domain.company.delegation.DelegationOffice;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.resource.TimeConstrainedResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;

import com.google.common.collect.ImmutableSet;


public class Company implements Serializable {

    
    private LocalDateTime                           time             = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
    private final Set<BranchOffice>                 offices          = new HashSet<BranchOffice>();
    private final DelegationOffice                  delegationOffice;
    private Set<ResourceType>                       resourceTypes    = new HashSet<ResourceType>();
    private Map<BranchOffice, BranchOffice.Memento> officeMementos   = new HashMap<BranchOffice, BranchOffice.Memento>();
    
    public Company() {
        resourceTypes.add(Developer.DEVELOPER_TYPE);
        delegationOffice = new DelegationOffice(this);
//        seedData();
    }
    
    
    //FIXME: remove me when no longer needed!
    private void seedData(){
        // Set Time
        LocalDateTime startTime1 = LocalDateTime.of(2015, 6, 1 , 9 , 0);
        this.time = startTime1;
        
    	// Offices
    	BranchOffice office1 = new BranchOffice("Office 1", this);
    	BranchOffice office2 = new BranchOffice("Office 2", this);
    	offices.add(office1);
    	offices.add(office2);
    	
    	// Resource Types
    	Set<ResourceType> requireSet = new HashSet<ResourceType>();
    	Set<ResourceType> conflictSet = new HashSet<ResourceType>();
    	TimePeriod dailyAvailability0 = new TimePeriod(LocalTime.of(12,00), LocalTime.of(17,00)); 

    	ResourceType carType = createResourceType("Car", requireSet,conflictSet, false, null);
    	ResourceType whiteBoardType = createResourceType("White Board", requireSet, conflictSet, false, null);
    	conflictSet.add(whiteBoardType);
    	ResourceType demoKitType = createResourceType("Demo Kit", requireSet, conflictSet, false, null);
    	conflictSet.clear();
    	requireSet.add(demoKitType);
    	ResourceType conferenceRoomType = createResourceType("Conference Room", requireSet, conflictSet, true, null);
    	requireSet.clear();
    	ResourceType testingSetupType = createResourceType("Distributed Testing Setup", requireSet, conflictSet, false, null);
    	ResourceType dataCenterType = createResourceType("Data Center", requireSet, conflictSet, false, dailyAvailability0);
    	
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
        
        // Tasks
        // Project 1
//        Set<Task> deps = new HashSet<Task>();
//        Set<Requirement> reqSet = new HashSet<Requirement>();
//        reqSet.add(new Requirement(2, Developer.DEVELOPER_TYPE));
//        reqSet.add(new Requirement(1, carType));
//        reqSet.add(new Requirement(1, dataCenterType));
//        Requirements reqs1 = new Requirements(reqSet);
//        Task task1 = proj1.createTask("Upgrade server infrastructure", 120, 0, deps, reqs1);
//        
//        
//        deps.add(task1);
//        reqSet.clear();
//        reqSet.add(new Requirement(1, Developer.DEVELOPER_TYPE));
//        reqSet.add(new Requirement(1, conferenceRoomType));
//        reqSet.add(new Requirement(1, demoKitType));
//        Requirements reqs2 = new Requirements(reqSet);
//        Task task2 = proj1.createTask("Prepare demo dataset", 90, 0, deps, reqs2);
//        
//        deps.clear();
//        reqSet.clear();
//        reqSet.add(new Requirement(1, Developer.DEVELOPER_TYPE));
//        reqSet.add(new Requirement(1, conferenceRoomType));
//        reqSet.add(new Requirement(1, demoKitType));
//        Requirements reqs3 = new Requirements(reqSet);
//        Task task3 = proj1.createTask("Install demo kit in conference room", 30, 0, deps, reqs3);
//
//        deps.clear();
//        deps.add(task2);
//        deps.add(task3);
//        reqSet.clear();
//        reqSet.add(new Requirement(2, Developer.DEVELOPER_TYPE));
//        reqSet.add(new Requirement(1, conferenceRoomType));
//        reqSet.add(new Requirement(1, demoKitType));
//        Requirements reqs4 = new Requirements(reqSet);
//        Task task4 = proj1.createTask("Perform demo for clients", 60, 0, deps, reqs4);
//        
//        
//        // Project 2
//        deps.clear();
//        reqSet.clear();
//        reqSet.add(new Requirement(3, Developer.DEVELOPER_TYPE));
//        reqSet.add(new Requirement(2, whiteBoardType));
//        Requirements reqs5 = new Requirements(reqSet);
//        Task task5 = proj2.createTask("Brainstorm session", 60, 0, deps, reqs5);
//        
//        // Project 3
//        deps.clear();
//        reqSet.clear();
//        reqSet.add(new Requirement(1, Developer.DEVELOPER_TYPE));
//        reqSet.add(new Requirement(1, testingSetupType));
//        Requirements reqs6 = new Requirements(reqSet);
//        Task task6 = proj3.createTask("Test the prototype", 180, 0, deps, reqs6);
    	
    }
    
    public ImmutableSet<BranchOffice> getOffices() {
        return ImmutableSet.copyOf(offices);
    }

    public Set<Developer> getDevelopers(AuthenticationToken at) {
        return at.getOffice().getDevelopers();
    }

    public ImmutableSet<Project> getProjects(AuthenticationToken at) {
        return at.getOffice().getProjects();
    }

    public ImmutableSet<Project> getAllProjects() {
        Set<Project> all = new HashSet<>();
        for (BranchOffice office : offices) {
            all.addAll(office.getProjects());
        }
        return ImmutableSet.copyOf(all);
    }

    public BranchOffice getOfficeOf(Project project) {
        for (BranchOffice office : offices) {
            if (office.getProjects().contains(project)) { return office; }
        }
        return null;
    }

    public Set<ResourceType> getResourceTypes() {
        return new HashSet<ResourceType>(resourceTypes);
    }

    public Set<Task> getUnplannedTasksOf(Project project, AuthenticationToken at) {
        return at.getOffice().getUnplannedTasksOf(project);
    }

    private DelegationOffice getDelegationOffice() {
        return delegationOffice;
    }

    public Set<Task> getAssignedTasksOf(Project project, AuthenticationToken at) {
        if (at.isDeveloper()) {
            return at.getOffice().getAssignedTasksOf(project, at.getAsDeveloper());
        }
        else {
            return new HashSet<>();
        }
    }

    public List<LocalDateTime> getPlanningTimeOptions(Task task, int amount, LocalDateTime time, AuthenticationToken at) {
        return at.getOffice().getPlanningTimeOptions(task, amount, time);
    }

    public Map<ResourceType, List<Resource>> getPlanningResourceOptions(Task task, LocalDateTime time, AuthenticationToken at) {
        return at.getOffice().getPlanningResourceOptions(task, time);
    }

    public Set<Developer> getPlanningDeveloperOptions(Task task, LocalDateTime time, AuthenticationToken at) {
        return at.getOffice().getPlanningDeveloperOptions(task, time);
    }
    
    public Project getDelegationProject(AuthenticationToken at){
        return at.getOffice().getDelegationProject();
    }

    public void createPlanning(Task task, LocalDateTime time, Set<Resource> rss, AuthenticationToken at) throws ConflictingPlannedTaskException {
        at.getOffice().createPlanning(task, time, rss);
    }

    public void createPlanningWithBreak(Task task, LocalDateTime time, Set<Resource> rss, AuthenticationToken at) throws ConflictingPlannedTaskException {
        at.getOffice().createPlanning(task, time, rss, true);
    }

    public void removePlanning(TaskPlanning planning, AuthenticationToken at) {
        at.getOffice().removePlanning(planning);
    }

    public Project createProject(String title, String description, LocalDateTime creationTime, LocalDateTime dueTime, AuthenticationToken at) {
        return at.getOffice().createProject(title, description, creationTime, dueTime);
    }

    public Task createTaskFor(Project project, String description, long estimatedDuration, double acceptableDeviation, Set<Task> dependencies, Requirements requirements) {
        return project.createTask(description, estimatedDuration, acceptableDeviation, dependencies, requirements);
    }

    public Set<Resource> getResources(AuthenticationToken at) {
        return at.getOffice().getResources();
    }

    public Project getProjectFor(Task task, AuthenticationToken at) {
        return at.getOffice().getProjectFor(task);
    }

    public void setAlternativeFor(Task t, Task alt) {
        t.setAlternative(alt);
    }

    public void finishTask(Task t, AuthenticationToken at) throws ConflictingPlannedTaskException {
        at.getOffice().finishTask(t, time);
    }

    public void failTask(Task t, AuthenticationToken at) throws ConflictingPlannedTaskException {
        at.getOffice().failTask(t, time);
    }

    public void startExecutingTask(Task t, Set<Resource> resources, AuthenticationToken at) throws ConflictingPlannedTaskException { //FIXME
        if (at.isDeveloper()) {
            at.getOffice().startExecutingTask(t, time, resources);
        }
        else {
            throw new IllegalArgumentException("Manager is trying to execute a task.");
        }
    }

    /**
     * Creates a new ResourceType.
     *
     * @param name
     *            The name of the new ResourceType
     * @param requires
     *            The Set containing the dependencies of this type of resource
     * @param conflicts
     *            The Set containing the conflicting types of resources, if a task requires a resource of this type, then it cannot require one of the conflicting types
     * @param selfConflicting
     *            A boolean that, when true, a task requiring a resource of this type, can only reserve one of this type
     * @param availability
     *            The period for when a resource of this type is available during the day
     * @return The new ResourceType
     */
    public ResourceType createResourceType(String name, Set<ResourceType> requires, Set<ResourceType> conflicts, boolean selfConflicting, TimePeriod availability) {
        ResourceType type;
        if (availability != null) {
            type = new TimeConstrainedResourceType(name, requires, conflicts, selfConflicting, availability);
        } else {
            type = new ResourceType(name, requires, conflicts, selfConflicting);
        }
        resourceTypes.add(type);
        return type;
    }

    public Resource createResource(String name, ResourceType type, AuthenticationToken at) {
        return at.getOffice().createResource(name, type);
    }

    public Developer createDeveloper(String name, AuthenticationToken at) {
        return at.getOffice().createDeveloper(name);
    }

    public boolean isTaskAvailableFor(LocalDateTime time, Task task, AuthenticationToken at) {
        if(time == null){
            time = this.time;
        }
        if (at.isDeveloper()) {
            return at.getOffice().isTaskAvailableFor(time, at.getAsDeveloper(), task);
        }
        else {
            return false;
        }
    }

    public void startSimulationFor(AuthenticationToken at) {
        // no nested simulations allowed
        if (isInASimulationFor(at)) { throw new IllegalStateException(); }

        officeMementos.put(at.getOffice(), at.getOffice().saveToMemento());
    }

    public void realizeSimulationFor(AuthenticationToken at) {
        if (!isInASimulationFor(at)) { throw new IllegalStateException(); }
        officeMementos.remove(at.getOffice());
        delegationOffice.processBuffer();
    }

    public void cancelSimulationFor(AuthenticationToken at) {
        if (!isInASimulationFor(at)) { throw new IllegalStateException(); }

        BranchOffice.Memento officeMemento = officeMementos.get(at.getOffice());
        at.getOffice().restoreFromMemento(officeMemento);
        officeMementos.remove(at.getOffice());
        delegationOffice.rollbackSimulation(at.getOffice());
    }

    public boolean isInASimulationFor(AuthenticationToken at) {
        return officeMementos.containsKey(at.getOffice());
    }
    
    public boolean isInASimulation(BranchOffice office){
        return officeMementos.containsKey(office);
    }

    /**
     * Changes the program's system time.
     *
     * @param time
     *            The Date containing the new system time.
     *
     * @throws IllegalArgumentException
     *             If the given Date is invalid.
     *
     */
    public void updateSystemTime(LocalDateTime time) throws IllegalArgumentException {
        if (time == null) { throw new IllegalArgumentException("Null date for system time update"); }
        this.time = time;
    }

    /**
     * Returns the current system time
     *
     * @return The current system time
     */
    public LocalDateTime getSystemTime() {
        return time;
    }

    // TODO: Proper checking
    public void delegateTask(Task task, BranchOffice newOffice) {
        BranchOffice oldOffice = getOfficeFromTask(task);
        Delegation del = getDelegationOffice().createDelegation(task, oldOffice, newOffice);
    }

    public BranchOffice getOfficeFromTask(Task task) {
        for (BranchOffice office : offices) {
            if (office.hasTask(task)) { return office; }
        }
        return null;
    }

}
