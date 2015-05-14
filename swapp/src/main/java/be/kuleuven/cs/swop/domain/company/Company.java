package be.kuleuven.cs.swop.domain.company;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.Timekeeper;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;

import com.google.common.collect.ImmutableSet;


public class Company {

    private final Timekeeper timeKeeper = new Timekeeper();
    private final Set<BranchOffice> offices = new HashSet<BranchOffice>();
    private final DelegationOffice delegationOffice = new DelegationOffice();
    public Company() {
        
    }
    
    public ImmutableSet<BranchOffice> getOffices() {
        return ImmutableSet.copyOf(offices);
    }
    
    public ImmutableSet<Developer> getDevelopers(AuthenticationToken at) {
        return at.getOffice().getDevelopers();
    }
    
    public ImmutableSet<Project> getProjects(AuthenticationToken at) {
        return at.getOffice().getProjects();
    }
    
    public ImmutableSet<ResourceType> getResourceTypes(AuthenticationToken at) {
        return at.getOffice().getResourceTypes();
    }
    
    public Set<Task> getUnplannedTasksOf(Project project, AuthenticationToken at) {
        return at.getOffice().getUnplannedTasksOf(project);
    }

    private DelegationOffice getDelegationOffice() {
        return delegationOffice;
    }
    
    public Set<Task> getAssignedTasksOf(Project project, AuthenticationToken at){
        // FIXME make nicer, don't use instanceof
        if (at.getUser() instanceof Developer) {
            return at.getOffice().getAssignedTasksOf(project, (Developer)at.getUser());
        }
        else {
            return new HashSet<>();
        }
    }
    
    public TaskPlanning getPlanningFor(Task task, AuthenticationToken at) {
        return at.getOffice().getPlanningFor(task);
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

    public void createPlanning(Task task, LocalDateTime time, Set<Resource> rss, Set<Developer> devs, AuthenticationToken at) throws ConflictingPlanningException {
        at.getOffice().createPlanning(task, time, rss, devs);
    }

    public void createPlanningWithBreak(Task task, LocalDateTime time, Set<Resource> rss, Set<Developer> devs, AuthenticationToken at) throws ConflictingPlanningException {
        at.getOffice().createPlanningWithBreak(task, time, rss, devs);
    }
    
    public void removePlanning(TaskPlanning planning, AuthenticationToken at){      
        at.getOffice().removePlanning(planning);      
    }

    public Project createProject(String title, String description, LocalDateTime creationTime, LocalDateTime dueTime, AuthenticationToken at) {
        return at.getOffice().createProject(title, description, creationTime, dueTime);
    }

    public Task createTaskFor(Project project, String description, long estimatedDuration, double acceptableDeviation, Set<Task> dependencies, Set<Requirement> requirements) {
        return project.createTask(description, estimatedDuration, acceptableDeviation, dependencies, requirements);
    }
    
    public Set<Resource> getResources(AuthenticationToken at) {       
        return at.getOffice().getResources();     
    }

    public Project getProjectFor(Task task, AuthenticationToken at){
        return at.getOffice().getProjectFor(task);
    }
    
    public void setAlternativeFor(Task t, Task alt){
        t.setAlternative(alt);
    }

    public void addDependencyTo(Task t,Task dep){
        t.addDependency(dep);
    }
    
    public void finishTask(Task t, DateTimePeriod period, AuthenticationToken at){
        at.getOffice().finishTask(t, period);
    }

    public void failTask(Task t,DateTimePeriod period, AuthenticationToken at){
        at.getOffice().failTask(t, period);
    }

    public void startExecutingTask(Task t, LocalDateTime time, Developer dev, AuthenticationToken at) {
        at.getOffice().startExecutingTask(t, time, dev);
    }

    public ResourceType createResourceType(String name, Set<ResourceType> requires, Set<ResourceType> conflicts, boolean selfConflicting, TimePeriod availability, AuthenticationToken at){
        return at.getOffice().createResourceType(name, requires, conflicts, selfConflicting, availability);
    }

    public Resource createResource(String name, ResourceType type, AuthenticationToken at){
        return at.getOffice().createResource(name, type);
    }

    public Developer createDeveloper(String name, AuthenticationToken at) {
        return at.getOffice().createDeveloper(name);
    }
    
    public boolean isTaskAvailableFor(LocalDateTime time, Developer dev,Task task, AuthenticationToken at) {
        return at.getOffice().isTaskAvailableFor(time, dev, task);
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
        timeKeeper.setTime(time);
    }

    /**
     * Returns the current system time
     *
     * @return The current system time
     */
    public LocalDateTime getSystemTime() {
        return timeKeeper.getTime();
    }
    
}
