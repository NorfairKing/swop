package be.kuleuven.cs.swop.domain.company;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;

import com.google.common.collect.ImmutableSet;

/**
 * A company.
 * This is the main class that gives access to the rest of the domain.
 */
@SuppressWarnings("serial")
public class BranchOffice implements Serializable {

    private final String location;
    
    private ProjectManager  projectManager;
    private PlanningManager planningManager;
    private Project delegationProject;

    public BranchOffice(String location) {
        this.location = location;
        
        setProjectManager(new ProjectManager());
        setPlanningManager(new PlanningManager());
        
        delegationProject = getProjectManager().createProject("Delegated tasks", "Tasks that have been delegated to this office.", LocalDateTime.now(), LocalDateTime.now());
    }

    // Getters and setters of internal state
    public String getLocation() {
        return location;
    }
    
    private ProjectManager getProjectManager() {
        return projectManager;
    }

    private void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    private PlanningManager getPlanningManager() {
        return planningManager;
    }

    private void setPlanningManager(PlanningManager planningManager) {
        this.planningManager = planningManager;
    }

    // Passthrough getters    
    public ImmutableSet<Developer> getDevelopers() {
        return getPlanningManager().getDevelopers();
    }

    public ImmutableSet<Project> getProjects() {
        return getProjectManager().getProjects();
    }
    
    public Set<Resource> getResources() {		
    	return getPlanningManager().getResources();		
    }

    public ImmutableSet<ResourceType> getResourceTypes() {
        return getPlanningManager().getResourceTypes();
    }

    public Project getProjectFor(Task task){
        return getProjectManager().getProjectFor(task);
    }

    public Set<Task> getUnplannedTasksOf(Project project) {
        return planningManager.getUnplannedTasksFrom(project.getTasks());
    }

    /**
     * Retrieves all the plannings of a project
     *
     * @param project The project for which to get all plannings
     * @return A set of all the planning
     */
    public Set<TaskPlanning> getPlanningsFor(Project project){
        return planningManager.getPlanningsFor(project.getTasks());
    }

    public Set<Task> getAssignedTasksOf(Project project, Developer dev){
        return planningManager.getAssignedTasksOf(project.getTasks(), dev);
    }

    public TaskPlanning getPlanningFor(Task task) {
        return getPlanningManager().getPlanningFor(task);
    }

    public List<LocalDateTime> getPlanningTimeOptions(Task task, int amount, LocalDateTime time) {
        return getPlanningManager().getPlanningTimeOptions(task, amount, time);
    }

    public Map<ResourceType, List<Resource>> getPlanningResourceOptions(Task task, LocalDateTime time) {
        return getPlanningManager().getPlanningResourceOptions(task, time);
    }

    public Set<Developer> getPlanningDeveloperOptions(Task task, LocalDateTime time) {
        return getPlanningManager().getPlanningDeveloperOptions(task, time);
    }

    public void createPlanning(Task task, LocalDateTime time, Set<Resource> rss, Set<Developer> devs) throws ConflictingPlanningException {
        getPlanningManager().createPlanning(task, time, rss, devs);
    }

    public void createPlanningWithBreak(Task task, LocalDateTime time, Set<Resource> rss, Set<Developer> devs) throws ConflictingPlanningException {
        getPlanningManager().createPlanningWithBreak(task, time, rss, devs);
    }
    
    public void removePlanning(TaskPlanning planning){		
	    	getPlanningManager().removePlanning(planning);		
    }

    public Project createProject(String title, String description, LocalDateTime creationTime, LocalDateTime dueTime) {
        return getProjectManager().createProject(title, description, creationTime, dueTime);
    }

    /**
     * Check if the task is available.
     * This is the 'available' described in the second iteration.
     * Alternatively could be called 'canMoveToExecuting'
     *
     * @param time The time to check for
     * @param dev The developer for whom the task might be available
     * @param task The task to check
     * @return Whether or not it is available
     */
    public boolean isTaskAvailableFor(LocalDateTime time, Developer dev,Task task) {
        return getPlanningManager().isTier2AvailableFor(time, dev, task);
    }

    // finish, fail and executing has to happen through the planningManager
    // that's the class that can decide about this
    // We can't enforce this in java, but we enforce it with mind-power
    public void finishTask(Task t, DateTimePeriod period){
        getPlanningManager().finishTask(t, period);
    }

    public void failTask(Task t,DateTimePeriod period){
        getPlanningManager().failTask(t, period);
    }

    public void startExecutingTask(Task t, LocalDateTime time, Developer dev) {
        getPlanningManager().startExecutingTask(t, time, dev);
    }

    public ResourceType createResourceType(String name, Set<ResourceType> requires, Set<ResourceType> conflicts, boolean selfConflicting, TimePeriod availability){
        return getPlanningManager().createResourceType(name, requires, conflicts, selfConflicting, availability);
    }

    public Resource createResource(String name, ResourceType type){
        return getPlanningManager().createResource(name, type);
    }

    public Developer createDeveloper(String name) {
        return getPlanningManager().createDeveloper(name);
    }
    
    public boolean hasTask(Task task){
    	 Project proj = projectManager.getProjectFor(task);
    	 return proj != null;
    }
    public Task createDelegationTask(String description, long estimatedDuration, double acceptableDeviation, Set<Requirement> requirements){
    	Task task = delegationProject.createTask(description, estimatedDuration, acceptableDeviation, requirements);
    	return task;
    }
    
    // Memento for simulation
    /**
     * Creates a memento of the system
     *
     * @return The created memento
     */
    public Memento saveToMemento() {
        return new Memento(this);
    }

    /**
     * Restore the system from a memento
     *
     * @param memento The memento to restore from
     */
    public void restoreFromMemento(Memento memento) {
        projectManager = memento.getSavedState().projectManager;
        planningManager = memento.getSavedState().planningManager;
        delegationProject = memento.getSavedState().delegationProject;
    }

    public static class Memento {

        private BranchOffice state;

        public Memento(BranchOffice state) {
            this.state = state.getDeepCopy();
        }

        BranchOffice getSavedState() {
            return state;
        }
    }
    
    /**
     * Creates a deep copy of this class.
     * This is done by writing it to a bytestream, using the build-in java serializer
     * and then reading it out again.
     * This gives a very clean way to prevent duplication by multiple references, or
     * problems with looping references.
     * It does however take a bit more memory because value classes and final variables 
     * will also be copied where they aren't strictly needed.
     *
     * @return A deep copy of this class.
     */
    private BranchOffice getDeepCopy() {
        BranchOffice orig = this;
        BranchOffice obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            obj = (BranchOffice) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

}
