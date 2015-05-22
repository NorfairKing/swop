package be.kuleuven.cs.swop.domain.company;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.company.delegation.DelegationOffice;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.resource.TimeConstrainedResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.domain.company.user.Manager;

import com.google.common.collect.ImmutableSet;


@SuppressWarnings("serial")
public class Company implements Serializable {

    private LocalDateTime                           time             = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
    private final Set<BranchOffice>                 offices          = new HashSet<BranchOffice>();
    private final DelegationOffice                  delegationOffice;
    private Set<ResourceType>                       resourceTypes    = new HashSet<ResourceType>();
    private Map<BranchOffice, BranchOffice.Memento> officeMementos   = new HashMap<BranchOffice, BranchOffice.Memento>();

    /**
     * Constructor
     */
    public Company() {
        resourceTypes.add(Developer.DEVELOPER_TYPE);
        delegationOffice = new DelegationOffice(this);
    }

    /**
     * Creates a BranchOffice for this Company with the given location.
     *
     * @param location The location where the new BranchOffice is situated
     * @return The new BranchOffice
     */
    public BranchOffice createBranchOffice(String location){
        BranchOffice bo = new BranchOffice(location, this);
        this.offices.add(bo);
        return bo;
    }

    /**
     * Retrieves all the BranchOffices
     *
     * @return A ImmutableSet containing all of the BranchOffices.
     */
    public ImmutableSet<BranchOffice> getOffices() {
        return ImmutableSet.copyOf(offices);
    }

    /**
     * Retrieves all of the Developers of the BranchOffice where the
     * currently logged in user works.
     *
     * @param at The AuthenticationToken of the currently logged in user.
     *
     * @return A Set containing the Developers.
     */
    public Set<Developer> getDevelopers(AuthenticationToken at) {
        return at.getOffice().getDevelopers();
    }

    /**
     * Retrieves all of the Projects of the BranchOffice where the
     * currently logged in user works.
     *
     * @param at The AuthenticationToken of the currently logged in user.
     * @return An ImmutableSet containing the Projects.
     */
    public ImmutableSet<Project> getProjects(AuthenticationToken at) {
        return at.getOffice().getProjects();
    }

    /**
     * Retrieves every project of every BranchOffice.
     *
     * @return An ImmutableSet containing every Project.
     */
    public ImmutableSet<Project> getAllProjects() {
        Set<Project> all = new HashSet<>();
        for (BranchOffice office : offices) {
            all.addAll(office.getProjects());
        }
        return ImmutableSet.copyOf(all);
    }

    /**
     * Retrieves the BranchOffice where they work on the given Project.
     *
     * @param project The project for which the BranchOffice will be found.
     * @return The BranchOffice where they work on the given Project.
     */
    public BranchOffice getOfficeOf(Project project) {
        for (BranchOffice office : offices) {
            if (office.getProjects().contains(project)) { return office; }
        }
        return null;
    }

    /**
     * Retrieves all of the types of resources available in this Company.
     *
     * @return A Set containing the resourceTypes.
     */
    public Set<ResourceType> getResourceTypes() {
        return new HashSet<ResourceType>(resourceTypes);
    }

    /**
     * Retrieves the Tasks of the given project of the BranchOffice the currently
     * logged in user works at that still need to be planned.
     *
     * @param project The Project of which the unplanned Tasks will be retrieved.
     * @param at The AuthenticationToken of the currently logged in user.
     * @return A Set containing the unplanned Tasks.
     */
    public Set<Task> getUnplannedTasksOf(Project project, AuthenticationToken at) {
        return at.getOffice().getUnplannedTasksOf(project);
    }

    private DelegationOffice getDelegationOffice() {
        return delegationOffice;
    }

    /**
     * Retrieve the Tasks of the given project where the currently logged in user
     * is assigned to.
     *
     * @param project The Project for which the Tasks will be retrieved.
     * @param at The AuthenticationToken of the currently logged in user.
     * @return A Set containing those Tasks.
     */
    public Set<Task> getAssignedTasksOf(Project project, AuthenticationToken at) {
        if (at.isDeveloper()) {
            return at.getOffice().getAssignedTasksOf(project, at.getAsDeveloper());
        }
        else {
            return new HashSet<>();
        }
    }

    /**
     * Retrieves several options for when the given Task can be planned.
     *
     * @param task The Task for which the time options will be retrieved.
     * @param amount How many options will be retrieved.
     * @param time The retrieved times will be after this LocalDateTime.
     * @param at The AuthenticationToken of the currently logged in user.
     * @return A List containing the LocalDateTimes.
     */
    public List<LocalDateTime> getPlanningTimeOptions(Task task, int amount, LocalDateTime time, AuthenticationToken at) {
        return at.getOffice().getPlanningTimeOptions(task, amount, time);
    }

    /**
     * Retrieves the Resources which can be reserved for planning the given Task
     * at the given time.
     *
     * @param task The to be planned Task.
     * @param time The LocalDateTime when the Task would be planned.
     * @param at The AuthenticationToken of the currently logged in user.
     * @return A Map containing the ResourcesTypes the given Task needs
     * and Lists containig the available Resources of that type.
     */
    public Map<ResourceType, List<Resource>> getPlanningResourceOptions(Task task, LocalDateTime time, AuthenticationToken at) {
        return at.getOffice().getPlanningResourceOptions(task, time);
    }

    /**
     * Retrieves the Developers which can be assigned to the Task for the given
     * time.
     *
     * @param task The to be planned Task.
     * @param time The LocalDateTime when the Task would be planned.
     * @param at The AuthenticationToken of the currently logged in user.
     * @return A Set containing the Developers.
     */
    public Set<Developer> getPlanningDeveloperOptions(Task task, LocalDateTime time, AuthenticationToken at) {
        return at.getOffice().getPlanningDeveloperOptions(task, time);
    }

    /**
     * Retrieve the Project containing all of the delegated Tasks
     * for the BranchOffice of the currently logged in user.
     *
     * @param at The AuthenticationToken of the currently logged in user.
     * @return The Project.
     */
    public Project getDelegationProject(AuthenticationToken at){
        return at.getOffice().getDelegationProject();
    }

    /**
     * Creates a new planning for the given Task.
     *
     * @param task The to be planned Task.
     * @param time The LocalDateTime when the given Task will be planned.
     * @param rss A Set containing the reserved Resources.
     * @param at The AuthenticationToken of the currently logged in user.
     * @throws ConflictingPlannedTaskException When the to be created TaskPlanning would
     * cause conflicts.
     */
    public void createPlanning(Task task, LocalDateTime time, Set<Resource> rss, AuthenticationToken at) throws ConflictingPlannedTaskException {
        at.getOffice().createPlanning(task, time, rss);
    }

    /**
     * Creates a new planning for the given Task,
     * this planning will include a break for the developers.
     *
     * @param task The to be planned Task.
     * @param time The LocalDateTime when the given Task will be planned.
     * @param rss A Set containing the reserved Resources.
     * @param at The AuthenticationToken of the currently logged in user.
     * @throws ConflictingPlannedTaskException When the to be created TaskPlanning would
     * cause conflicts.
     */
    public void createPlanningWithBreak(Task task, LocalDateTime time, Set<Resource> rss, AuthenticationToken at) throws ConflictingPlannedTaskException {
        at.getOffice().createPlanning(task, time, rss, true);
    }

    /**
     * Remove the given planning.
     *
     * @param planning The to be removed TaskPlanning.
     * @param at The AuthenticationToken of the currently logged in user.
     */
    public void removePlanning(TaskPlanning planning, AuthenticationToken at) {
        at.getOffice().removePlanning(planning);
    }

    /**
     * Creates a new project for the BranchOffice of the currently logged in user.
     *
     * @param title A String that is the title for this new Project.
     * @param description A String that is the description for this new Project.
     * @param creationTime A LocalDateTime that specifies when the new Project was
     * created.
     * @param dueTime A LocalDateTime that specifies when the Project should be finished.
     * @param at The AuthenticationToken of the currently logged in user.
     * @return The newly created Project.
     */
    public Project createProject(String title, String description, LocalDateTime creationTime, LocalDateTime dueTime, AuthenticationToken at) {
        return at.getOffice().createProject(title, description, creationTime, dueTime);
    }

    /**
     * Creates a task for the given project.
     *
     * @param project The Project for which the new Task will be created.
     * @param description A String that is the description for this new Task.
     * @param estimatedDuration A long that is an estimation duration for how long the
     * Task will take untill it is completed in minutes.
     * @param acceptableDeviation A double that is a percentage that specifies how how
     * much too late the Task may be completed.
     * @param dependencies A Set containing the Tasks this Task requires to be completed
     * first.
     * @param requirements The Requirements specifies which resources are required to
     * complete this Task.
     * @return The newly created Task.
     */
    public Task createTaskFor(Project project, String description, long estimatedDuration, double acceptableDeviation, Set<Task> dependencies, Requirements requirements) {
        return project.createTask(description, estimatedDuration, acceptableDeviation, dependencies, requirements);
    }

    /**
     * Retrieves all of the resources of the BranchOffice where the currently logged in
     * user works.
     *
     * @param at The AuthenticationToken of the currently logged in user.
     * @return A Set containing those Resources.
     */
    public Set<Resource> getResources(AuthenticationToken at) {
        return at.getOffice().getResources();
    }

    /**
     * Retrieves the project for which the given task was created.
     *
     * @param task The Task for which we do the lookup.
     * @param at The AuthenticationToken of the currently logged in user.
     * @return The Project that contains the given Task.
     */
    public Project getProjectFor(Task task, AuthenticationToken at) {
        return at.getOffice().getProjectFor(task);
    }

    /**
     * Set an alternative task for the given task, e.g. for when the given task has
     * failed.
     *
     * @param t The Task that will get an alternative Task.
     * @param alt The alternative Task.
     */
    public void setAlternativeFor(Task t, Task alt) {
        t.setAlternative(alt);
    }

    /**
     * Set the given task to finished.
     * @param t The Task that is now finished.
     * @param at The AuthenticationToken of the currently logged in user.
     * @throws ConflictingPlannedTaskException If this causes a conflict with another
     * planning.
     */
    public void finishTask(Task t, AuthenticationToken at) throws ConflictingPlannedTaskException {
        at.getOffice().finishTask(t, time);
    }

    /**
     * Set the given task to failed.
     * @param t The Task that is now failed.
     * @param at The AuthenticationToken of the currently logged in user.
     * @throws ConflictingPlannedTaskException If this causes a conflict with another
     * planning.
     */
    public void failTask(Task t, AuthenticationToken at) throws ConflictingPlannedTaskException {
        at.getOffice().failTask(t, time);
    }

    /**
     * Set the given taks to an executing status.
     *
     * @param t The Task that will now start executing.
     * @param resources A Set containing the Resources that the Tasks reserves.
     * @param at The AuthenticationToken of the currently logged in user.
     * @throws ConflictingPlannedTaskException If this creates a conflict with another
     * planning.
     * @throws IllegalArgumentException If a manager tries to execute a Task.
     */
    public void startExecutingTask(Task t, Set<Resource> resources, AuthenticationToken at) throws ConflictingPlannedTaskException {
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

    /**
     * Creates a new resource.
     *
     * @param name A string that is the name of the new Resource.
     * @param type ResourceType of the new Resource.
     * @param at The AuthenticationToken of the currently logged in user.
     * @return The newly created Resource.
     */
    public Resource createResource(String name, ResourceType type, AuthenticationToken at) {
        return at.getOffice().createResource(name, type);
    }

    /**
     * Creates a new developer.
     *
     * @param name A string that is the name of the new Developer.
     * @param at The AuthenticationToken of the currently logged in user.
     * @return The newly created Developer.
     */
    public Developer createDeveloper(String name, AuthenticationToken at) {
        return createDeveloper(name, at.getOffice());
    }
    
    public Developer createDeveloper(String name, BranchOffice bo){
        return bo.createDeveloper(name);
    }
    
    public Manager createManager(String name, AuthenticationToken at){
        return createManager(name, at.getOffice());
    }
    
    public Manager createManager(String name, BranchOffice bo){
        return bo.createManager(name);
    }

    /**
     * Check if the task is available. This is the 'available' described in the second iteration.
     *
     * @param time
     *            The time to check for
     * @param task
     *            The task to check
     * @param at The AuthenticationToken of the currently logged in user.
     * @return Whether or not it is available
     */
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

    /**
     * Start a simulation for the BranchOffice of the currently logged in user.
     *
     * @param at The AuthenticationToken of the currently logged in user.
     *
     * @throws IllegalStateException If an attempt is made to do a nested simulation.
     */
    public void startSimulationFor(AuthenticationToken at) {
        // no nested simulations allowed
        if (isInASimulationFor(at)) { throw new IllegalStateException(); }

        officeMementos.put(at.getOffice(), at.getOffice().saveToMemento());
    }

    /**
     * Commit the changes of the current simulation.
     *
     * @param at The AuthenticationToken of the currently logged in user.
     *
     * @throws IllegalStateException When the the BranchOffice of the current user is not
     * in a simulation.
     */
    public void realizeSimulationFor(AuthenticationToken at) {
        if (!isInASimulationFor(at)) { throw new IllegalStateException(); }
        officeMementos.remove(at.getOffice());
        delegationOffice.processBuffer();
    }

    /**
     * Throw away the changes of the current simulation.
     *
     * @param at The AuthenticationToken of the currently logged in user.
     *
     * @throws IllegalStateException When the the BranchOffice of the current user is not
     * in a simulation.
     */
    public void cancelSimulationFor(AuthenticationToken at) {
        if (!isInASimulationFor(at)) { throw new IllegalStateException(); }

        BranchOffice.Memento officeMemento = officeMementos.get(at.getOffice());
        at.getOffice().restoreFromMemento(officeMemento);
        officeMementos.remove(at.getOffice());
        delegationOffice.rollbackSimulation(at.getOffice());
    }

    /**
     * Checks if the BranchOffice of the current user is in a simulation.
     *
     * @param at The AuthenticationToken of the currently logged in user.
     * @return True if it is in a simulation.
     */
    public boolean isInASimulationFor(AuthenticationToken at) {
        return officeMementos.containsKey(at.getOffice());
    }

    /**
     * Checks if the given BranchOffice is in a simulation.
     *
     * @param office The BranchOffice that will be checked.
     * @return True if it is in a simulation.
     */
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

    /**
     * Delegate the given task to another office.
     *
     * @param task The Task that will be delegated.
     * @param newOffice The BranchOffice to where the Task will be delegated.
     */
    public void delegateTask(Task task, BranchOffice newOffice) {
        BranchOffice oldOffice = getOfficeFromTask(task);
        getDelegationOffice().createDelegation(task, oldOffice, newOffice);
    }

    /**
     * Retrieve the BranchOffice where the given Task is.
     *
     * @param task The Task for which the BranchOffice will be retrieved.
     * @return The BranchOffice where the given Task is.
     */
    public BranchOffice getOfficeFromTask(Task task) {
        for (BranchOffice office : offices) {
            if (office.hasTask(task)) { return office; }
        }
        return null;
    }

}
