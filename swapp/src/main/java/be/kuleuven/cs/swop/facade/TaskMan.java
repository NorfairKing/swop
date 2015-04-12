package be.kuleuven.cs.swop.facade;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import be.kuleuven.cs.swop.domain.PlanningManager;
import be.kuleuven.cs.swop.domain.ProjectManager;
import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.Timekeeper;
import be.kuleuven.cs.swop.domain.project.Project;
import be.kuleuven.cs.swop.domain.resource.Resource;
import be.kuleuven.cs.swop.domain.resource.ResourceType;
import be.kuleuven.cs.swop.domain.task.Task;
import be.kuleuven.cs.swop.domain.user.Developer;
import be.kuleuven.cs.swop.domain.user.User;


@SuppressWarnings("serial")
public class TaskMan implements Serializable {

	private ProjectManager  projectManager;
	private PlanningManager planningManager;
	private Timekeeper      timeKeeper;

	/**
	 * Full constructor
	 */
	public TaskMan() {
		setProjectManager(new ProjectManager());
		setPlanningManager(new PlanningManager());
		setTimeKeeper(new Timekeeper());
	}

	// Getters and setters of internal state
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

	private Timekeeper getTimeKeeper() {
		return timeKeeper;
	}

	private void setTimeKeeper(Timekeeper timeKeeper) {
		this.timeKeeper = timeKeeper;
	}

	/**
	 * Retrieve all known users
	 * 
	 * @return A set of all known users, currently only developers
	 */
	public Set<UserWrapper> getUsers() {
		//FIXME Also return manager
		Set<UserWrapper> result = new HashSet<>();
		for (User user: getPlanningManager().getDevelopers()) {
			result.add(new UserWrapper(user));
		}
		return result;
	}

	/**
	 * Sets the active user of the system
	 * @param user The user to make active.
	 */
	public void setActiveUser(UserWrapper user) {
		getPlanningManager().setActiveUser(user.getUser());
	}

	/**
	 * Retrieve every Project managed by this program.
	 *
	 * @return Returns a Set of ProjectWrappers.
	 *
	 */
	public Set<ProjectWrapper> getProjects() {
		Set<ProjectWrapper> result = new HashSet<ProjectWrapper>();
		for (Project realProject : getProjectManager().getProjects()) {
			result.add(new ProjectWrapper(realProject));
		}
		return result;
	}

	/**
	 * Retrieve every unplanned task of a given Project
	 * 
	 * @param project
	 *            The projectwrapper containing the project from which the unplanned Tasks will be returned.
	 * @return A set of taskwrappers containing the unplanned tasks of the given project.
	 */
	public Set<TaskWrapper> getUnplannedTasksOf(ProjectWrapper project) {
		Set<TaskWrapper> allTasks = project.getTasks();
		Set<TaskWrapper> unplannedTasks = new HashSet<TaskWrapper>();
		for (TaskWrapper t : allTasks) {
			if (getPlanningManager().isUnplanned(t.getTask())) {
				unplannedTasks.add(t);
			}
		}
		return unplannedTasks;
	}

	public TaskPlanningWrapper getPlanningFor(TaskWrapper task) {
		return new TaskPlanningWrapper(getPlanningManager().getPlanningFor(task.getTask()));
	}

	public List<LocalDateTime> getPlanningTimeOptions(TaskWrapper task) {
		// FIXME Infinite loop it seems.
		return getPlanningManager().getPlanningTimeOptions(task.getTask(), AMOUNT_AVAILABLE_TASK_TIME_OPTIONS, getTimeKeeper().getTime());
	}

	public Map<ResourceTypeWrapper, List<ResourceWrapper>> getPlanningResourceOptions(TaskWrapper task, LocalDateTime time) {
		Map<ResourceType, List<Resource>> options = getPlanningManager().getPlanningResourceOptions(task.getTask(), time);
		Map<ResourceTypeWrapper, List<ResourceWrapper>> wrappedOptions = new HashMap<ResourceTypeWrapper, List<ResourceWrapper>>();
		for (ResourceType t : options.keySet()) {
			ResourceTypeWrapper typeWrapper = new ResourceTypeWrapper(t);
			wrappedOptions.put(typeWrapper, new ArrayList<ResourceWrapper>());
			for (Resource r : options.get(t)) {
				ResourceWrapper resourceWrapper = new ResourceWrapper(r);
				wrappedOptions.get(typeWrapper).add(resourceWrapper);
			}
		}
		return wrappedOptions;

	}

	public Set<DeveloperWrapper> getPlanningDeveloperOptions(TaskWrapper task, LocalDateTime time) {
		Set<Developer> devOptions = getPlanningManager().getPlanningDeveloperOptions(task.getTask(), time);
		Set<DeveloperWrapper> wrappedDevOptions = new HashSet<DeveloperWrapper>();
		for (Developer d : devOptions) {
			wrappedDevOptions.add(new DeveloperWrapper(d));
		}
		return wrappedDevOptions;
	}

	public void createPlanning(TaskWrapper task, LocalDateTime time, Set<ResourceWrapper> resources, Set<DeveloperWrapper> developers) {
		Set<Resource> rss = resources.stream().map(p -> p.getResource()).collect(Collectors.toSet());
		Set<Developer> devs = new HashSet<Developer>();
		for (DeveloperWrapper d : developers) {
			devs.add(d.getDeveloper());
		}
		getPlanningManager().createPlanning(task.getTask(), time, rss, devs);
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

		if (data.getCreationTime() == null) {
			Project createdProject = getProjectManager().createProject(data.getTitle(), data.getDescription(), getTimeKeeper().getTime(), data.getDueTime());
			return new ProjectWrapper(createdProject);
		}
		else {
			Project createdProject = getProjectManager().createProject(data.getTitle(), data.getDescription(), data.getCreationTime(), data.getDueTime());
			return new ProjectWrapper(createdProject);
		}
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

		Task createdTask = project.getProject().createTask(data.getDescription(), data.getEstimatedDuration(), data.getAcceptableDeviation());
		for (TaskWrapper dependency : data.getDependencies()) {
			createdTask.addDependency(dependency.getTask());
		}

		return new TaskWrapper(createdTask);
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

		Project project = getProjectManager().getProjectFor(task.getTask());

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
		task.getTask().addAlternative(alternative.getTask());
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
		task.getTask().addDependency(dependency.getTask());
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

		if(statusData.isFinal()){
			
			PerformedStatusData performedStatusData = (PerformedStatusData) statusData;

			if (performedStatusData.getStartTime() == null) { throw new IllegalArgumentException("Null start time for status update"); }
			if (performedStatusData.getEndTime() == null) { throw new IllegalArgumentException("Null end time for status update"); }

			DateTimePeriod timePeriod = new DateTimePeriod(performedStatusData.getStartTime(), performedStatusData.getEndTime());

			if (performedStatusData.isSuccessful()) {
				task.getTask().finish(timePeriod);
			} else {
				task.getTask().fail(timePeriod);
			}
		}else{
			IncompleteStatusData incompleteStatusData = (IncompleteStatusData) statusData;
			if(incompleteStatusData.isExecuting()){
				task.getTask().execute();
			}else{
				//TODO: What behavior do we want when an OngoingStatusData object is received?
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
		getTimeKeeper().setTime(time);
	}

	/**
	 * Returns the current system time
	 * 
	 * @return The current system time
	 */
	public LocalDateTime getSystemTime() {
		return getTimeKeeper().getTime();
	}

	// TODO: implement and document
	public ResourceTypeWrapper createResourceType(ResourceTypeData data){
		Set<ResourceType> requires = new HashSet<ResourceType>();
		Set<ResourceType> conflicts = new HashSet<ResourceType>();
		for(ResourceTypeWrapper type : data.getRequires()){
			requires.add(type.getType());
		}
		for(ResourceTypeWrapper type : data.getConflicts()){
			conflicts.add(type.getType());
		}
		
		TimePeriod availability = null;
		if(data.getAvailibility().length == 2){
			availability = new TimePeriod(data.getAvailibility()[0], data.getAvailibility()[1]);
		}
		
		
		ResourceType type = getPlanningManager().createResourceType(data.getName(), requires, conflicts, data.isSelfConflicting(), availability);
		return new ResourceTypeWrapper(type);
	}

	// TODO: implement and document
	public ResourceWrapper createResource(ResourceData data){
		Resource res = getPlanningManager().createResource(data.getName(), data.getType().getType());
		return new ResourceWrapper(res);
	}

	//TODO: implement and document
	public DeveloperWrapper createDeveloper(DeveloperData data){
		Developer dev = getPlanningManager().createDeveloper(data.getName());
		return new DeveloperWrapper(dev);
	}

	// Memento pattern
	public Memento saveToMemento() {
		return new Memento(this);
	}
	public void restoreFromMemento(Memento memento) {
		setPlanningManager(memento.getSavedState().getPlanningManager());
		setProjectManager(memento.getSavedState().getProjectManager());
		setTimeKeeper(memento.getSavedState().getTimeKeeper());
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
