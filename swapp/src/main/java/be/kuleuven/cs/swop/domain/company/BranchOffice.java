package be.kuleuven.cs.swop.domain.company;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;

import com.google.common.collect.ImmutableSet;


/**
 * A company. This is the main class that gives access to the rest of the domain.
 */
@SuppressWarnings("serial")
public class BranchOffice implements Serializable {

    private final String         location;

    private final Set<Project>   projects   = new HashSet<Project>();
    private final Company        company;
    private PlanningManager      planningManager;
    private Project              delegationProject;
    private final Set<Resource>  resources  = new HashSet<Resource>();

    public BranchOffice(String location, Company company) {
        this.location = location;
        this.company = company;
        this.planningManager = new PlanningManager(this);
        this.delegationProject = createProject("Delegated tasks", "Tasks that have been delegated to this office.", LocalDateTime.now(), LocalDateTime.MAX);
    }

    // Getters and setters of internal state
    public String getLocation() {
        return location;
    }

    // Passthrough getters
    public Set<Developer> getDevelopers() {
        Set<Developer> devs = new HashSet<Developer>();
        for(Resource res : resources){
            if(res.getType() == Developer.DEVELOPER_TYPE){
                devs.add((Developer) res);
            }
        }
        return devs;
    }

    public ImmutableSet<Project> getProjects() {
        return ImmutableSet.copyOf(projects);
    }

    public Set<Resource> getResources() {
        return ImmutableSet.copyOf(resources);
    }

    public ImmutableSet<ResourceType> getResourceTypes() {
        return company.getResourceTypes();
    }
    
    public Project getDelegationProject(){
        return this.delegationProject;
    }

    public Set<TaskPlanning> getTaskPlannings() {
        Set<TaskPlanning> plannings = new HashSet<TaskPlanning>();
        for (Project proj : getProjects()) {
            for (Task task : proj.getTasks()) {
                if (task.getPlanning() != null) {
                    plannings.add(task.getPlanning());
                }
            }
        }
        return plannings;
    }

    protected boolean canHaveAsTaskPlanning(TaskPlanning planning) {
        return planning != null;
    }

    /**
     * Tries to find the project of the given task and returns it. Returns null if no project was found, or the given task was null.
     * 
     * @param task
     *            The task to find the project for
     * @return The project to which the task belongs
     */
    public Project getProjectFor(Task task) {
        for (Project project : getProjects()) {
            if (project.containsTask(task)) { return project; }
        }

        return null;
    }

    public Set<Task> getUnplannedTasksOf(Project project) {
        return this.planningManager.getUnplannedTasksFrom(project.getTasks());
    }

    /**
     * Retrieves all the plannings of a project
     *
     * @param project
     *            The project for which to get all plannings
     * @return A set of all the planning
     */
    public Set<TaskPlanning> getPlanningsFor(Project project) {
        return this.planningManager.getPlanningsFor(project.getTasks());
    }

    public Set<Task> getAssignedTasksOf(Project project, Developer dev) {
        return this.planningManager.getAssignedTasksOf(project.getTasks(), dev);
    }

    public Task getTaskFor(TaskPlanning planning) {
        for (Project proj : getProjects()) {
            for (Task task : proj.getTasks()) {
                if (task.getPlanning() == planning) { return task; }
            }
        }
        return null;
    }

    public List<LocalDateTime> getPlanningTimeOptions(Task task, int amount, LocalDateTime time) {
        return this.planningManager.getPlanningTimeOptions(task, amount, time);
    }

    public Map<ResourceType, List<Resource>> getPlanningResourceOptions(Task task, LocalDateTime time) {
        return this.planningManager.getPlanningResourceOptions(task, time);
    }

    public Set<Developer> getPlanningDeveloperOptions(Task task, LocalDateTime time) {
        return this.planningManager.getPlanningDeveloperOptions(task, time);
    }

    public void createPlanning(Task task, LocalDateTime time, Set<Resource> rss) throws ConflictingPlannedTaskException {
        this.planningManager.createPlanning(task, time, rss);
    }

    public void createPlanningWithBreak(Task task, LocalDateTime time, Set<Resource> rss) throws ConflictingPlannedTaskException {
        this.planningManager.createPlanningWithBreak(task, time, rss);
    }

    /**
     * Removes a planning, freeing everything that it reserved.
     *
     * @param planning
     *            The planning that will be removed
     */
    public void removePlanning(TaskPlanning planning) {
        this.planningManager.removePlanning(planning);
    }

    /**
     * Creates and returns a new Project with the given arguments, this method is used by the importer of the yaml file because the creationTime is specified.
     *
     * @param title
     *            A String containing the title for the new Project.
     *
     * @param description
     *            A String containing the description for the new Project.
     *
     * @param creationTime
     *            A Date containing the time when the new Project was created.
     *
     * @param dueTime
     *            A Date containing the time for when the Project is due to be completed.
     *
     * @return Returns the newly created Project.
     */
    public Project createProject(String title, String description, LocalDateTime creationTime, LocalDateTime dueTime) {
        Project project = new Project(title, description, creationTime, dueTime);
        addProject(project);
        return project;
    }

    protected boolean canHaveAsProject(Project project) {
        return project != null;
    }

    private void addProject(Project project) {
        if (!canHaveAsProject(project)) throw new IllegalArgumentException(ERROR_ILLEGAL_PROJECT);
        projects.add(project);
    }

    /**
     * Check if the task is available. This is the 'available' described in the second iteration. Alternatively could be called 'canMoveToExecuting'
     *
     * @param time
     *            The time to check for
     * @param dev
     *            The developer for whom the task might be available
     * @param task
     *            The task to check
     * @return Whether or not it is available
     */
    public boolean isTaskAvailableFor(LocalDateTime time, Developer dev, Task task) {
        return this.planningManager.isTier2AvailableFor(time, dev, task);
    }

    // finish, fail and executing has to happen through the planningManager
    // that's the class that can decide about this
    // We can't enforce this in java, but we enforce it with mind-power
    public void finishTask(Task t, DateTimePeriod period) {
        this.planningManager.finishTask(t, period);
    }

    public void failTask(Task t, DateTimePeriod period) {
        this.planningManager.failTask(t, period);
    }

    public void startExecutingTask(Task t, LocalDateTime time, Developer dev) {
        this.planningManager.startExecutingTask(t, time, dev);
    }

    /**
     * Creates a new resource and keeps track of it.
     *
     * @param name
     *            The name of the new resource
     * @param type
     *            The ResourceType of the new resource
     * @return Returns the newly created resource
     */
    public Resource createResource(String name, ResourceType type) {
        Resource resource = new Resource(type, name);
        resources.add(resource);
        return resource;
    }

    /**
     * Creates a new developer and keeps track of it.
     *
     * @param name
     *            The name for the new developer
     * @return Returns the newly created developer
     */
    public Developer createDeveloper(String name) {
        Developer dev = new Developer(name);
        resources.add(dev);
        return dev;
    }

    public boolean hasTask(Task task) {
        Project proj = getProjectFor(task);
        return proj != null;
    }

    public Task createDelegationTask(String description, long estimatedDuration, double acceptableDeviation, Requirements requirements) {
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
     * @param memento
     *            The memento to restore from
     */
    public void restoreFromMemento(Memento memento) {
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
     * Creates a deep copy of this class. This is done by writing it to a bytestream, using the build-in java serializer and then reading it out again. This gives a very clean way to prevent
     * duplication by multiple references, or problems with looping references. It does however take a bit more memory because value classes and final variables will also be copied where they aren't
     * strictly needed.
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

    private static final String ERROR_ILLEGAL_TASK_PLANNING   = "Illegal TaskPlanning in Planning manager.";
    private static final String ERROR_ILLEGAL_EXECUTING_STATE = "Can't execute a task that isn't available.";
    private static final String ERROR_ILLEGAL_TASK            = "Illegal task provided.";
    private static final String ERROR_ILLEGAL_DATETIME        = "Illegal date provided.";
    private static final String ERROR_ILLEGAL_RESOURCE        = "Illegal resource provided.";
    private static final String ERROR_ILLEGAL_DEVELOPER       = "Illegal developer provided.";
    private static final String ERROR_ILLEGAL_RESOURCE_SET    = "The given set of resources is not possible.";
    private static final String ERROR_RESOURCE_NOT_AVAILABLE  = "A resource is not available at that time.";
    private static final String ERROR_TASK_ALREADY_PLANNED    = "The given Task already has a planning.";
    private static final String ERROR_ILLEGAL_PROJECT         = "Invalid project for project manager";

}
