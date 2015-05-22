package be.kuleuven.cs.swop.domain.company;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.CopyHelper;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.domain.company.user.Manager;
import be.kuleuven.cs.swop.domain.company.user.User;

import com.google.common.collect.ImmutableSet;


@SuppressWarnings("serial")
public class BranchOffice implements Serializable {

    private final String         location;

    private final Set<Project>   projects   = new HashSet<Project>();
    private final Company        company;
    private final PlanningDepartment      planningDepartment;
    private Project        		 delegationProject;
    private final Set<Resource>  resources  = new HashSet<Resource>();
    private final Set<User> employees = new HashSet<User>();

    /**
     * Constructor, creates a new BranchOffice with a given location and within the given
     * company.
     *
     * @param location A String that specifies the location of the new BranchOffice
     * @param company The Company for which this BranchOffice will be created.
     */
    public BranchOffice(String location, Company company) {
        this.location = location;
        this.company = company;
        this.planningDepartment = new PlanningDepartment(this);
        this.delegationProject = createProject("Delegated tasks", "Tasks that have been delegated to this office.", LocalDateTime.now(), LocalDateTime.MAX);
    }

    // Getters and setters of internal state
    /**
     * Restrieves the location of this office.
     *
     * @return The String with the location of this BranchOffice.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Retrieves all developers that work at this office.
     *
     * @return a Set containing all Developers of this BranchOffice.
     */
    public Set<Developer> getDevelopers() {
        Set<Developer> devs = new HashSet<Developer>();
        for(Resource res : resources){
            if(res.getType() == Developer.DEVELOPER_TYPE){
                devs.add((Developer) res);
            }
        }
        return devs;
    }

    /**
     * Retrieves all projects this office does.
     *
     * @return An ImmutableSet containing all of the Projects.
     */
    public ImmutableSet<Project> getProjects() {
        return ImmutableSet.copyOf(projects);
    }

    /**
     * Retrieves all resources this office has.
     *
     * @return A Set containing all of the Resources.
     */
    public Set<Resource> getResources() {
        return new HashSet<Resource>(resources);
    }

    /**
     * Retrieves all the types of resources this office has.
     *
     * @return A Set containing all of the ResourceTypes.
     */
    public Set<ResourceType> getResourceTypes() {
        return company.getResourceTypes();
    }

    /**
     * Retrieves a Project containing all of that Tasks that are delicated to this office.
     *
     * @return The delegation Project.
     */
    public Project getDelegationProject(){
        return this.delegationProject;
    }

    /**
     * Retrieves every planning of every task of this office.
     *
     * @return A Set with all of the TaskPlannings.
     */
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

    /**
     * Retrieves every task that still needs to be planned of the give project.
     *
     * @param project The Project that will be checked for unplanned Tasks.
     * @return A Set containing all of the unplanned Tasks.
     */
    public Set<Task> getUnplannedTasksOf(Project project) {
        return this.planningDepartment.getUnplannedTasksFrom(project.getTasks());
    }

    /**
     * Retrieves all the plannings of a project
     *
     * @param project
     *            The project for which to get all plannings
     * @return A set of all the planning
     */
    public Set<TaskPlanning> getPlanningsFor(Project project) {
        return this.planningDepartment.getPlanningsFor(project.getTasks());
    }

    /**
     * Retrieve the tasks of the given project assigned to the given developer.
     *
     * @param project The Project that contains the Tasks.
     * @param dev The Developer that is assigned to the retrieved Tasks.
     * @return A Set containing all of the assigned Tasks.
     */
    public Set<Task> getAssignedTasksOf(Project project, Developer dev) {
        return this.planningDepartment.getAssignedTasksOf(project.getTasks(), dev);
    }

    /**
     * Retrieves the task of a given planning.
     *
     * @param planning The TaskPlanning for which the retrieved Task is meant for.
     *
     * @return The Task for wich the given TaskPlanning was meant for.
     */
    public Task getTaskFor(TaskPlanning planning) {
        for (Project proj : getProjects()) {
            for (Task task : proj.getTasks()) {
                if (task.getPlanning() == planning) { return task; }
            }
        }
        return null;
    }

    /**
     * Retrieves several options for when the given Task can be planned.
     *
     * @param task The Task for which the time options will be retrieved.
     * @param amount How many options will be retrieved.
     * @param time The retrieved times will be after this LocalDateTime.
     * @return A List containing the LocalDateTimes.
     */
    public List<LocalDateTime> getPlanningTimeOptions(Task task, int amount, LocalDateTime time) {
        return this.planningDepartment.getPlanningTimeOptions(task, amount, time);
    }

    /**
     * Retrieves the Resources which can be reserved for planning the given Task
     * at the given time.
     *
     * @param task The to be planned Task.
     * @param time The LocalDateTime when the Task would be planned.
     * @return A Map containing the ResourcesTypes the given Task needs
     * and Lists containig the available Resources of that type.
     */
    public Map<ResourceType, List<Resource>> getPlanningResourceOptions(Task task, LocalDateTime time) {
        return this.planningDepartment.getPlanningResourceOptions(task, time);
    }

    /**
     * Retrieves the Developers which can be assigned to the Task for the given
     * time.
     *
     * @param task The to be planned Task.
     * @param time The LocalDateTime when the Task would be planned.
     * @return A Set containing the Developers.
     */
    public Set<Developer> getPlanningDeveloperOptions(Task task, LocalDateTime time) {
        return this.planningDepartment.getPlanningDeveloperOptions(task, time);
    }

    /**
     * Creates a new planning for the given Task.
     *
     * @param task The to be planned Task.
     * @param time The LocalDateTime when the given Task will be planned.
     * @param rss A Set containing the reserved Resources.
     * @param withBreak A boolean that specifies whether or not the planning should
     * include a break for the developers.
     * @throws ConflictingPlannedTaskException When the to be created TaskPlanning would
     * cause conflicts.
     */
    public void createPlanning(Task task, LocalDateTime time, Set<Resource> rss, boolean withBreak) throws ConflictingPlannedTaskException {
        this.planningDepartment.createPlanning(task, new DateTimePeriod(time, time.plusMinutes(task.getEstimatedDuration())), rss, withBreak);
    }

    /**
     * Creates a new planning for the given Task.
     *
     * @param task The to be planned Task.
     * @param time The LocalDateTime when the given Task will be planned.
     * @param rss A Set containing the reserved Resources.
     * @throws ConflictingPlannedTaskException When the to be created TaskPlanning would
     * cause conflicts.
     */
    public void createPlanning(Task task, LocalDateTime time, Set<Resource> rss) throws ConflictingPlannedTaskException {
        createPlanning(task, time, rss, false);
    }

    /**
     * Removes a planning, freeing everything that it reserved.
     *
     * @param planning
     *            The planning that will be removed
     */
    public void removePlanning(TaskPlanning planning) {
        this.planningDepartment.removePlanning(planning);
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
        return this.planningDepartment.isAvailableFor(dev, task, time);
    }

    // finish, fail and executing has to happen through the planningManager
    // that's the class that can decide about this
    // We can't enforce this in java, but we enforce it with mind-power

    /**
     * Set the given task to finished.
     * @param t The Task that is now finished.
     * @throws ConflictingPlannedTaskException If this causes a conflict with another
     * planning.
     */
    public void finishTask(Task t, LocalDateTime time) throws ConflictingPlannedTaskException {
        this.planningDepartment.finishTask(t, time);
    }

    /**
     * Set the given task to failed.
     * @param t The Task that is now failed.
     * @throws ConflictingPlannedTaskException If this causes a conflict with another
     * planning.
     */
    public void failTask(Task t, LocalDateTime time) throws ConflictingPlannedTaskException {
        this.planningDepartment.failTask(t, time);
    }

    /**
     * Set the given taks to an executing status.
     *
     * @param t The Task that will now start executing.
     * @param resources A Set containing the Resources that the Tasks reserves.
     * @throws ConflictingPlannedTaskException If this creates a conflict with another
     * planning.
     * @throws IllegalArgumentException If a manager tries to execute a Task.
     */
    public void startExecutingTask(Task t, LocalDateTime time, Set<Resource> resources) throws ConflictingPlannedTaskException {
        this.planningDepartment.startExecutingTask(t, time, resources);
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

    public Set<User> getUsers() {
        return ImmutableSet.copyOf(this.employees);
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
        employees.add(dev);
        return dev;
    }

    /**
     * Creates a new manager with the given name.
     *
     * @return The newly created Manager.
     */
    public Manager createManager(String name) {
        Manager man = new Manager(name);
        employees.add(man);
        return man;
    }

    /**
     * Checks whether this office has the given task.
     *
     * @param task The Task that will be checked.
     * @return True if this BranchOffice has this Task.
     */
    public boolean hasTask(Task task) {
        Project proj = getProjectFor(task);
        return proj != null;
    }

    /**
     * Creates a new Task that was delegated from another office.
     *
     * @param description
     *            The description for the new Task.
     *
     * @param estimatedDuration
     *            The estimated duration it will take to complete the Task.
     *
     * @param acceptableDeviation
     *            The acceptable deviation of time for completing the Task.
     *
     * @return The newly created Task.
     *
     */
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
        this.resources.clear();
        this.resources.addAll(memento.resources);

        this.projects.clear();
        this.projects.addAll(memento.projects);

        this.delegationProject = memento.delegationProject;
    }

    public static class Memento implements Serializable {

        private final Set<Resource> resources;
        private final Set<Project> projects;
        private final Project delegationProject;

        @SuppressWarnings("unchecked")
		public Memento(BranchOffice state) {
            this.resources = (Set<Resource>)CopyHelper.getDeepCopyOf(state.resources);
            this.projects = (Set<Project>)CopyHelper.getDeepCopyOf(state.projects);
            this.delegationProject = (Project)CopyHelper.getDeepCopyOf(state.delegationProject);
        }
    }

    private static final String ERROR_ILLEGAL_PROJECT         = "Invalid project for project manager";



}
