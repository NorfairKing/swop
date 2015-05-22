package be.kuleuven.cs.swop.facade;


import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.company.AuthenticationToken;
import be.kuleuven.cs.swop.domain.company.Authenticator;
import be.kuleuven.cs.swop.domain.company.BranchOffice;
import be.kuleuven.cs.swop.domain.company.Company;
import be.kuleuven.cs.swop.domain.company.ConflictingPlannedTaskException;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Manager;


public class TaskMan {

    private Company             company;
    private Authenticator       authenticator = new Authenticator();
    private AuthenticationToken authenticationToken;

    /**
     * Full constructor
     */
    public TaskMan() {
        this(new Company());
        this.company = new Company();
    }

    public TaskMan(Company company) {
        if (!canHaveAsCompany(company)) { throw new IllegalArgumentException(ERROR_ILLEGAL_COMPANY); }
        this.company = company;
    }

    private Company getCompany()
    {
        return this.company;
    }

    protected boolean canHaveAsCompany(Company company) {
        return company != null;
    }

    public AuthenticationToken getCurrentAuthenticationToken() {
        return authenticationToken;
    }

    public boolean isAuthenticated() {
        return authenticationToken != null;
    }

    public void setCurrentAuthenticationToken(AuthenticationToken token) {
        this.authenticationToken = token;
    }

    public void requestAuthenticationFor(BranchOfficeWrapper office, UserWrapper user) {
        authenticationToken = authenticator.createFor(office.getOffice(), user.getUser());
    }

    // Wrapping functions
    private <Type, WrapperType> Set<WrapperType> map(Set<? extends Type> presents, Function<Type, WrapperType> wrap) {
        Set<WrapperType> result = new HashSet<>();
        for (Type present : presents) {
            result.add(wrap.apply(present));
        }
        return result;
    }

    private <Type, ImageType, WrapperType> Set<WrapperType> map(Map<? extends Type, ImageType> presents, BiFunction<Type, ImageType, WrapperType> wrap) {
        Set<WrapperType> result = new HashSet<>();
        for (Type present : presents.keySet()) {
            result.add(wrap.apply(present, presents.get(present)));
        }
        return result;
    }

    public Set<BranchOfficeWrapper> getOffices() {
        return map(company.getOffices(), o -> new BranchOfficeWrapper(o));
    }

    /**
     * Retrieve all users for the given branch office
     *
     * @param office
     *            The branch office from which you want the users
     * @return A set of all known users, currently only developers
     */
    public Set<UserWrapper> getUsersFrom(BranchOfficeWrapper office) {
        Set<UserWrapper> users = map(office.getOffice().getDevelopers(), u -> new UserWrapper(u));
        users.add(new UserWrapper(new Manager("Manager")));
        return users;
    }

    /**
     * Retrieve every Project managed by your current office.
     *
     * @return Returns a Set of ProjectWrappers.
     *
     */
    public Set<ProjectWrapper> getProjects() {
        return map(company.getProjects(authenticationToken), p -> new ProjectWrapper(p));
    }

    /**
     * Retrieve every Project managed by this program.
     *
     * @return Returns a Set of ProjectWrappers.
     *
     */
    public Set<ProjectWrapper> getAllProjects() {
        return map(company.getAllProjects(), p -> new ProjectWrapper(p));
    }

    /**
     * Searches all known branchoffices to see which manages the project.
     * 
     * @param project
     *            The project for which you want the office
     * @return The office that manages the project
     */
    public BranchOfficeWrapper getOfficeOf(ProjectWrapper project) {
        return new BranchOfficeWrapper(company.getOfficeOf(project.getProject()));
    }

    /**
     * Retrieve all resource types of the company
     *
     * @return A set of the resources types
     */
    public Set<ResourceType> getResourceTypes() {
        return company.getResourceTypes();
    }

    /**
     * Retrieve every unplanned task of a given Project
     *
     * @param project
     *            The projectwrapper containing the project from which the unplanned Tasks will be returned.
     * @return A set of taskwrappers containing the unplanned tasks of the given project.
     */
    public Set<TaskWrapper> getUnplannedTasksOf(ProjectWrapper project) {
        return map(company.getUnplannedTasksOf(project.getProject(), authenticationToken), t -> new TaskWrapper(t));
    }

    /**
     * Retrieves the assigned tasks from a single project for current authenticated user
     *
     * @param project
     *            The project to retrieve from
     * @return The assigned tasks
     */
    public Set<TaskWrapper> getAssignedTasksOf(ProjectWrapper project) {
        return map(company.getAssignedTasksOf(project.getProject(), authenticationToken), t -> new TaskWrapper(t));
    }

    /**
     * Retrieves some suggestions for possible planning times of a task Currently gives you the first 3 starting at the current system time
     *
     * @param task
     *            The task for which you want a possible time
     * @return A list of suggested time options
     */
    public List<LocalDateTime> getPlanningTimeOptions(TaskWrapper task) {
        return company.getPlanningTimeOptions(
                task.getTask(),
                AMOUNT_AVAILABLE_TASK_TIME_OPTIONS,
                company.getSystemTime(),
                authenticationToken);
    }

    /**
     * Retrieves a list of options for each resource type needed by a task.
     *
     * @param task
     *            The task for which you want the options
     * @param time
     *            The time on which you cant to use the resources
     * @return The list with options
     */
    public Map<ResourceType, List<Resource>> getPlanningResourceOptions(TaskWrapper task, LocalDateTime time) {
        return company.getPlanningResourceOptions(task.getTask(), time, authenticationToken);
    }

    public Set<Resource> getResources() {
        return company.getResources(authenticationToken);
    }

    /**
     * Retrieves a list of developers that can work on the task on a given time
     *
     * @param task
     *            The task for which you need developers
     * @param time
     *            The time on which you need developers
     * @return The possible developers
     */
    public Set<DeveloperWrapper> getPlanningDeveloperOptions(TaskWrapper task, LocalDateTime time) {
        return map(company.getPlanningDeveloperOptions(task.getTask(), time, authenticationToken), d -> new DeveloperWrapper(d));
    }

    public ProjectWrapper getDelegationProject() {
        return new ProjectWrapper(company.getDelegationProject(authenticationToken));
    }

    /**
     * Create a planning for a task
     *
     * @param task
     *            The task to plan
     * @param time
     *            The time on which it is planned for the task to start
     * @param resources
     *            The resources to reserve for this task
     * @throws ConflictingPlannedTaskWrapperException
     *             If the created planning would cause a conflict
     */
    public void createPlanning(TaskWrapper task, LocalDateTime time, Set<Resource> resources) throws ConflictingPlannedTaskWrapperException {
        try {
            company.createPlanning(task.getTask(), time, resources, authenticationToken);
        } catch (ConflictingPlannedTaskException e) {
            throw new ConflictingPlannedTaskWrapperException(new TaskWrapper(e.getTask()));
        }
    }

    /**
     * Create a planning for a task, this planning will include a break for the developers.
     *
     * @param task
     *            The task to plan
     * @param time
     *            The time on which it is planned for the task to start
     * @param resources
     *            The resources to reserve for this task
     * @throws ConflictingPlannedTaskWrapperException
     *             IF the created planning would cause a conflict
     */
    public void createPlanningWithBreak(TaskWrapper task, LocalDateTime time, Set<Resource> resources) throws ConflictingPlannedTaskWrapperException {
    	try {
			company.createPlanningWithBreak(task.getTask(), time, resources, authenticationToken);
		} catch (ConflictingPlannedTaskException e) {
			throw new ConflictingPlannedTaskWrapperException(new TaskWrapper(e.getTask()));
		}
    }

    public void removePlanning(TaskPlanning planning) {
        company.removePlanning(planning, authenticationToken);
    }

    /**
     * Creates a Project, adds it to the program and returns a wrapper containing it.
     *
     * @param data
     *            A ProjectData object containing all the information for creating a Project.
     *
     * @throws IllegalArgumentException
     *             If some of the given data is incorrect.
     *
     * @return Returns a ProjectWrapper containing the newly created Project.
     *
     */
    public ProjectWrapper createProject(ProjectData data) {
        if (data == null) { throw new IllegalArgumentException("Null projectdata for project creation"); }
        if (data.getDescription() == null) { throw new IllegalArgumentException("Null description for project creation"); }
        if (data.getTitle() == null) { throw new IllegalArgumentException("Null title for project creation"); }
        if (data.getDueTime() == null) { throw new IllegalArgumentException("Null due time for project creation"); }

        String title = data.getTitle();
        String description = data.getDescription();
        LocalDateTime dueTime = data.getDueTime();
        LocalDateTime creationTime = data.getCreationTime();
        if (data.getCreationTime() == null) {
            creationTime = company.getSystemTime();
        }

        return new ProjectWrapper(company.createProject(title, description, creationTime, dueTime, authenticationToken));
    }

    /**
     * Creates a Task, adds it to the program and returns a wrapper containing it.
     *
     * @param project
     *            The ProjectWrapper containing the Project for which the new task will be created.
     *
     * @param data
     *            A TaskData object containing all the information for creating a Task.
     *
     * @throws IllegalArgumentException
     *             If some of the given data is incorrect.
     *
     * @return Returns a TaskWrapper containing the newly created Task.
     *
     */
    public TaskWrapper createTaskFor(ProjectWrapper project, TaskData data) throws IllegalArgumentException {
        if (project == null) { throw new IllegalArgumentException("Null project for task creation"); }
        if (data == null) { throw new IllegalArgumentException("Null task data for task creation"); }

        if (data.getDescription() == null) { throw new IllegalArgumentException("Null description for task creation"); }

        String descr = data.getDescription();
        long est = data.getEstimatedDuration();
        double dev = data.getAcceptableDeviation();
        Set<Task> deps = map(data.getDependencies(), t -> t.getTask());
        Set<Requirement> reqs = null;
        if (data.getRequirements() != null) {
            reqs = map(data.getRequirements(), (t, i) -> new Requirement(i, t));
        }

        return new TaskWrapper(company.createTaskFor(project.getProject(), descr, est, dev, deps, new Requirements(reqs)));
    }

    /**
     * Creates an alternative Task for the specified Task, adds it to the program and returns a wrapper containing it.
     *
     * @param task
     *            The TaskWrapper containing the Task for which this method creates an alternative for.
     *
     * @param data
     *            A TaskData object containing all the information for creating a Task.
     *
     * @throws IllegalArgumentException
     *             If some of the given data is incorrect.
     *
     * @return Returns a TaskWrapper containing the newly created Task.
     *
     */
    public TaskWrapper createAlternativeFor(TaskWrapper task, TaskData data) {
        if (task == null) { throw new IllegalArgumentException("Trying to create alternative for null task"); }

        Project project = company.getProjectFor(task.getTask(), authenticationToken);

        TaskWrapper alternative = createTaskFor(new ProjectWrapper(project), data);

        task.getTask().setAlternative(alternative.getTask());

        return alternative;
    }

    /**
     * Set the alternative of a task.
     *
     * @param task
     *            The task for which to set an alternative.
     * @param alternative
     *            The alternative for the task.
     */
    public void setAlternativeFor(TaskWrapper task, TaskWrapper alternative) {
        company.setAlternativeFor(task.getTask(), alternative.getTask());
    }

    /**
     * Complete a task
     *
     * @param task
     *            The TaskWrapper containing the Task for which this method changes the status.
     *
     * @param success Whether it was finished successfully or failed.
     *
     * @throws IllegalArgumentException
     *             If some of the status data is incorrect.
     * @throws ConflictingPlannedTaskWrapperException  When conflicting with another task
     *
     */
    public void completeTask(TaskWrapper task, boolean success) throws IllegalArgumentException, ConflictingPlannedTaskWrapperException {
        if (task == null) { throw new IllegalArgumentException("Null task for status update"); }

        try {
            if (success) {
                company.finishTask(task.getTask(), authenticationToken);
            } else {
                company.failTask(task.getTask(), authenticationToken);
            }
        } catch (ConflictingPlannedTaskException e) {
            throw new ConflictingPlannedTaskWrapperException(new TaskWrapper(e.getTask()));
        }
    }
    
    public void executeTask(TaskWrapper task, Set<Resource> resources) throws IllegalArgumentException, ConflictingPlannedTaskWrapperException {
        if (task == null) { throw new IllegalArgumentException("Null task for status update"); }
        if (resources == null) { throw new IllegalArgumentException("Null setof Resources for status update"); }

        try {
            company.startExecutingTask(task.getTask(), resources, authenticationToken);
        } catch (ConflictingPlannedTaskException e) {
            throw new ConflictingPlannedTaskWrapperException(new TaskWrapper(e.getTask()));
        }
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
        company.updateSystemTime(time);
    }

    /**
     * Returns the current system time
     *
     * @return The current system time
     */
    public LocalDateTime getSystemTime() {
        return company.getSystemTime();
    }

    /**
     * Creates a new resource type in the system
     *
     * @param data
     *            The data needed to create the type
     * @return The created type
     */
    public ResourceType createResourceType(ResourceTypeData data) {
        String name = data.getName();
        Set<ResourceType> requires = data.getRequires();
        Set<ResourceType> conflicts = data.getConflicts();
        boolean selfConflicting = data.isSelfConflicting();

        TimePeriod availability = null;
        if (data.getAvailibility().length == 2) {
            availability = new TimePeriod(data.getAvailibility()[0], data.getAvailibility()[1]);
        }

        return company.createResourceType(name, requires, conflicts, selfConflicting, availability);
    }

    /**
     * Creates a new resource in the system
     *
     * @param data
     *            The data needed to create the resource
     * @return The created resource
     */
    public Resource createResource(ResourceData data) {
        String name = data.getName();
        ResourceType type = data.getType();
        return company.createResource(name, type, authenticationToken);
    }

    /**
     * Check if the task is available. This is the 'available' described in the second iteration. Alternatively could be called 'canMoveToExecuting'
     *
     * @param time
     *            The time to check for
     * @param task
     *            The task to check
     * @return Whether or not it is available
     */
    public boolean isTaskAvailableFor(LocalDateTime time, TaskWrapper task) {
        return getCompany().isTaskAvailableFor(time, task.getTask(), authenticationToken);
    }

    /**
     * Create a new developer in the system
     *
     * @param data
     *            The data needed to create a developer
     * @return The newly created developer
     */
    public DeveloperWrapper createDeveloper(DeveloperData data) {
        String name = data.getName();
        return new DeveloperWrapper(company.createDeveloper(name, authenticationToken));
    }

    public void delegateTask(TaskWrapper wrappedTask, BranchOfficeWrapper wrappedOffice) {
        Task task = wrappedTask.getTask();
        BranchOffice newOffice = wrappedOffice.getOffice();
        getCompany().delegateTask(task, newOffice);
    }

    public BranchOfficeWrapper getOfficeToWhichThisTaskIsDelegated(TaskWrapper task) {
        if (task.getTask().getDelegation() == null) return null;
        return new BranchOfficeWrapper(task.getTask().getDelegation().getNewOffice());
    }

    public void startSimulation() {
        company.startSimulationFor(authenticationToken);
    }

    public void realizeSimulation() {
        company.realizeSimulationFor(authenticationToken);
    }

    public void cancelSimulation() {
        company.cancelSimulationFor(authenticationToken);
    }

    public boolean isInASimulation() {
        return company.isInASimulationFor(authenticationToken);
    }

    /**
     * Saves the company to disk.
     * 
     * @param path
     *            Where to save it.
     * @throws FileNotFoundException
     *             If you can't save there.
     */
    public void saveEverythingToFile(String path) throws FileNotFoundException {
    	JSONReader.writeToDisk(path, company);
    }

    /**
     * Loads the company from disk.
     * 
     * @param path
     *            Where to load from.
     * @throws FileNotFoundException
     *             If there is no file found.
     */
    public void loadEverythingFromFile(String path) throws FileNotFoundException {
        company = (Company) JSONReader.readFromDisk(path);
    }

    private static final int    AMOUNT_AVAILABLE_TASK_TIME_OPTIONS = 3;
    private static final String ERROR_ILLEGAL_COMPANY              = "Invalid company for TaskMan";
}
