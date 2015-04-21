package be.kuleuven.cs.swop.facade;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.Timekeeper;
import be.kuleuven.cs.swop.domain.company.Company;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.domain.company.user.Manager;
import be.kuleuven.cs.swop.domain.company.user.User;


@SuppressWarnings("serial")
public class TaskMan implements Serializable {

    private Timekeeper timeKeeper;
    private Company    company;

    /**
     * Full constructor
     */
    public TaskMan() {
        this.timeKeeper = new Timekeeper();
        this.company = new Company();
    }
    
    private Timekeeper getTimekeeper(){
        return this.timeKeeper;
    }
    private Company getCompany()
    {
        return this.company;
    }

    private UserWrapper wrapUser(User u) {
        return new UserWrapper(u);
    }

    private DeveloperWrapper wrapDeveloper(Developer d) {
        return new DeveloperWrapper(d);
    }

    private TaskWrapper wrapTask(Task t)
    {
        return new TaskWrapper(t);
    }

    private ProjectWrapper wrapProject(Project p)
    {
        return new ProjectWrapper(p);
    }

    private TaskPlanningWrapper wrapPlanning(TaskPlanning p) {
        return new TaskPlanningWrapper(p);
    }

    private ResourceWrapper wrapResource(Resource r) {
        return new ResourceWrapper(r);
    }

    private ResourceTypeWrapper wrapResourceType(ResourceType t) {
        return new ResourceTypeWrapper(t);
    }

    private <Type, WrapperType> Set<WrapperType> map(Set<? extends Type> presents, Function<Type, WrapperType> wrap) {
        Set<WrapperType> result = new HashSet<>();
        for (Type present : presents) {
            result.add(wrap.apply(present));
        }
        return result;
    }

    private <Type, WrapperType> List<WrapperType> map(List<? extends Type> presents, Function<Type, WrapperType> wrap) {
        List<WrapperType> result = new ArrayList<>();
        for (Type present : presents) {
            result.add(wrap.apply(present));
        }
        return result;
    }

    // Sorry, I just hate Java
    private <Type, WrapperType, ImageType, ImageTypeWrapper> Map<WrapperType, ImageTypeWrapper> map(Map<? extends Type, ImageType> presents, Function<Type, WrapperType> wrap,
            Function<ImageType, ImageTypeWrapper> wrapImage) {
        Map<WrapperType, ImageTypeWrapper> result = new HashMap<>();
        for (Type present : presents.keySet()) {
            result.put(wrap.apply(present), wrapImage.apply(presents.get(present)));
        }
        return result;
    }
    private <Type, ImageType, WrapperType> Set<WrapperType> map(Map<? extends Type, ImageType> presents, BiFunction<Type, ImageType, WrapperType > wrap) { 
        Set<WrapperType> result = new HashSet<>();
        for (Type present : presents.keySet()) {
            result.add(wrap.apply(present, presents.get(present)));
        }
        return result;
    }
    

    /**
     * Retrieve all known users
     * 
     * @return A set of all known users, currently only developers
     */
    public Set<UserWrapper> getUsers() {
        Set<UserWrapper> users = map(company.getDevelopers(), u -> wrapUser(u));
        users.add(wrapUser(new Manager("Manager")));
        return users;
    }

    /**
     * Retrieve every Project managed by this program.
     *
     * @return Returns a Set of ProjectWrappers.
     *
     */
    public Set<ProjectWrapper> getProjects() {
        return map(company.getProjects(), p -> wrapProject(p));
    }
    
    public Set<ResourceTypeWrapper> getResourceTypes() {
        return map(company.getResourceTypes(),r -> wrapResourceType(r));
    }

    /**
     * Retrieve every unplanned task of a given Project
     * 
     * @param project
     *            The projectwrapper containing the project from which the unplanned Tasks will be returned.
     * @return A set of taskwrappers containing the unplanned tasks of the given project.
     */
    public Set<TaskWrapper> getUnplannedTasksOf(ProjectWrapper project) {
        return map(company.getUnplannedTasksOf(project.getProject()), t -> wrapTask(t));
    }

    public TaskPlanningWrapper getPlanningFor(TaskWrapper task) {
        return wrapPlanning(company.getPlanningFor(task.getTask()));
    }

    public List<LocalDateTime> getPlanningTimeOptions(TaskWrapper task) {
        // FIXME Infinite loop it seems.
        return company.getPlanningTimeOptions(task.getTask(), AMOUNT_AVAILABLE_TASK_TIME_OPTIONS, timeKeeper.getTime());
    }

    public Map<ResourceTypeWrapper, List<ResourceWrapper>> getPlanningResourceOptions(TaskWrapper task, LocalDateTime time) {
        return map(company.getPlanningResourceOptions(task.getTask(), time), t -> wrapResourceType(t), l -> map(l, r -> wrapResource(r)));
    }

    public Set<DeveloperWrapper> getPlanningDeveloperOptions(TaskWrapper task, LocalDateTime time) {
        return map(company.getPlanningDeveloperOptions(task.getTask(), time), d -> wrapDeveloper(d));
    }

    public void createPlanning(TaskWrapper task, LocalDateTime time, Set<ResourceWrapper> resources, Set<DeveloperWrapper> developers) {
        company.createPlanning(task.getTask(), time, map(resources, p -> p.getResource()), map(developers, d -> d.getDeveloper()));
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
        if (data.getCreationTime() != null) {
            creationTime = timeKeeper.getTime();
        }

        return wrapProject(company.createProject(title, description, creationTime, dueTime));
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
        if (data.getRequirements() != null){
            reqs = map(data.getRequirements(),(t,i)-> new Requirement(i, t.getType()));
        }
       
        return wrapTask(company.createTaskFor(project.getProject(),descr,est,dev,deps,reqs));
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

        Project project = company.getProjectFor(task.getTask());

        TaskWrapper alternative = createTaskFor(new ProjectWrapper(project), data);

        task.getTask().addAlternative(alternative.getTask());

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
        company.setAlternativeFor(task.getTask(),alternative.getTask());
    }

    /**
     * Add an existing task as dependency for another.
     * 
     * @param task
     *            The task to add a dependency to.
     * @param dependency
     *            The dependency.
     */
    public void addDependencyTo(TaskWrapper task, TaskWrapper dependency) {
        company.addDependencyTo(task.getTask(), dependency.getTask());
    }

    /**
     * Changes the status of a specified Task.
     *
     * @param task
     *            The TaskWrapper containing the Task for which this method changes the status.
     *
     * @param statusData
     *            A TaskStatusData object containing the data for the new Task status.
     *
     * @throws IllegalArgumentException
     *             If some of the status data is incorrect.
     *
     */
    public void updateTaskStatusFor(TaskWrapper task, TaskStatusData statusData) throws IllegalArgumentException {
        if (task == null) { throw new IllegalArgumentException("Null task for status update"); }
        if (statusData == null) { throw new IllegalArgumentException("Null statusdata for status update"); }

        if (statusData.isFinal()) {
            PerformedStatusData performedStatusData = (PerformedStatusData) statusData;

            if (performedStatusData.getStartTime() == null) { throw new IllegalArgumentException("Null start time for status update"); }
            if (performedStatusData.getEndTime() == null) { throw new IllegalArgumentException("Null end time for status update"); }

            DateTimePeriod timePeriod = new DateTimePeriod(performedStatusData.getStartTime(), performedStatusData.getEndTime());

            if (performedStatusData.isSuccessful()) {
                company.finishTask(task.getTask(),timePeriod);
            } else {
                company.failTask(task.getTask(), timePeriod);
            }
        } else {
            IncompleteStatusData incompleteStatusData = (IncompleteStatusData) statusData;
            if (incompleteStatusData.isExecuting()) {
                company.startExecutingTask(task.getTask());
            } else {
                // TODO: What behavior do we want when an OngoingStatusData object is received?
            }

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

    // TODO: implement and document
    public ResourceTypeWrapper createResourceType(ResourceTypeData data) {
        String name = data.getName();
        Set<ResourceType> requires = map(data.getRequires(),r -> r.getType());
        Set<ResourceType> conflicts = map(data.getConflicts(), r -> r.getType());
        boolean selfConflicting = data.isSelfConflicting();
        
        TimePeriod availability = null;
        if (data.getAvailibility().length == 2) {
            availability = new TimePeriod(data.getAvailibility()[0], data.getAvailibility()[1]);
        }

        return wrapResourceType(company.createResourceType(name, requires, conflicts,selfConflicting,availability));
    }

    public ResourceWrapper createResource(ResourceData data) {
        String name = data.getName();
        ResourceType type = data.getType().getType();
        return wrapResource(company.createResource(name,type));
    }

    // TODO: implement and document
    public DeveloperWrapper createDeveloper(DeveloperData data) {
        String name = data.getName();
        return wrapDeveloper(company.createDeveloper(name));
    }


    // Memento pattern
    public Memento saveToMemento() {
        return new Memento(this);
    }

    public void restoreFromMemento(Memento memento) {
        
        company = memento.getSavedState().getCompany();
        timeKeeper = memento.getSavedState().getTimekeeper();
    }

    public static class Memento {

        private TaskMan state;

        public Memento(TaskMan state) {
            this.state = state.getDeepCopy();
        }

        TaskMan getSavedState() {
            return state;
        }
    }

    private TaskMan getDeepCopy() {
        TaskMan orig = this;
        TaskMan obj = null;
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
            obj = (TaskMan) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

    private static final int AMOUNT_AVAILABLE_TASK_TIME_OPTIONS = 3;

}
