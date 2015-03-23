package be.kuleuven.cs.swop.facade;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.ProjectManager;
import be.kuleuven.cs.swop.domain.ReservationManager;
import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.Timekeeper;
import be.kuleuven.cs.swop.domain.project.Project;
import be.kuleuven.cs.swop.domain.task.Task;


public class FacadeController {

    ProjectManager projectManager;
    ReservationManager reservationManager;

    /**
     * Full constructor
     */
    public FacadeController() {
        projectManager = new ProjectManager();
        reservationManager = new ReservationManager();
    }

    /**
     * Retrieve every Project managed by this program.
     *
     * @return Returns a Set of ProjectWrappers.
     *
     */
    public Set<ProjectWrapper> getProjects() {
        Set<ProjectWrapper> result = new HashSet<ProjectWrapper>();
        for (Project realProject : projectManager.getProjects()) {
            result.add(new ProjectWrapper(realProject));
        }
        return result;
    }

    /**
     * Retrieve every Task of the given Project.
     *
     * @param project This is a ProjectWrapper containing the Project from which the Tasks
     * will be returned.
     * @return Returns a Set of TaskWrappers containing the Tasks of the given Project.
     */
    public Set<TaskWrapper> getTasksOf(ProjectWrapper project) {
        Set<TaskWrapper> tasks = new HashSet<TaskWrapper>();
        for (Project p : projectManager.getProjects()) {
            for (Task t : p.getTasks()) {
                tasks.add(new TaskWrapper(t));
            }
        }
        return tasks;
    }

    /**
     * Creates a Project, adds it to the program and returns a wrapper containing it.
     *
     * @param data A ProjectData object containing all the information for creating a
     * Project.
     *
     * @throws IllegalArgumentException If some of the given data is incorrect.
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
            Project createdProject = projectManager.createProject(data.getTitle(), data.getDescription(), data.getDueTime());
            return new ProjectWrapper(createdProject);
        }
        else {
            Project createdProject = projectManager.createProject(data.getTitle(), data.getDescription(), data.getCreationTime(), data.getDueTime());
            return new ProjectWrapper(createdProject);
        }
    }

    /**
     * Creates a Task, adds it to the program and returns a wrapper containing it.
     *
     * @param project The ProjectWrapper containing the Project for which the new task
     * will be created.
     *
     * @param data A TaskData object containing all the information for creating a
     * Task.
     *
     * @throws IllegalArgumentException If some of the given data is incorrect.
     *
     * @return Returns a TaskWrapper containing the newly created Task.
     *
     */
    public TaskWrapper createTaskFor(ProjectWrapper project, TaskData data) throws IllegalArgumentException {
        if (project == null) { throw new IllegalArgumentException("Null project for task creation"); }
        if (data == null) { throw new IllegalArgumentException("Null task data for task creation"); }

        if (data.getDescription() == null) { throw new IllegalArgumentException("Null description for task creation"); }

        Task createdTask = project.getProject().createTask(data.getDescription(), data.getEstimatedDuration(), data.getAcceptableDeviation());
        for (TaskWrapper dependency: data.getDependencies()) {
            createdTask.addDependency(dependency.getTask());
        }

        return new TaskWrapper(createdTask);
    }

    /**
     * Creates an alternative Task for the specified Task,
     * adds it to the program and returns a wrapper containing it.
     *
     * @param task The TaskWrapper containing the Task for which this method creates an
     * alternative for.
     *
     * @param data A TaskData object containing all the information for creating a
     * Task.
     *
     * @throws IllegalArgumentException If some of the given data is incorrect.
     *
     * @return Returns a TaskWrapper containing the newly created Task.
     *
     */
    public TaskWrapper createAlternativeFor(TaskWrapper task, TaskData data) {
        if (task == null) { throw new IllegalArgumentException("Trying to create alternative for null task"); }

        Project project = projectManager.getProjectFor(task.getTask());

        TaskWrapper alternative = createTaskFor(new ProjectWrapper(project), data);

        task.getTask().setAlternative(alternative.getTask());

        return alternative;
    }
    
    /**
     * Set the alternative of a task.
     * @param task The task for which to set an alternative.
     * @param alternative The alternative for the task.
     */
    public void setAlternativeFor(TaskWrapper task, TaskWrapper alternative) {
        task.getTask().setAlternative(alternative.getTask());
    }
    
    /**
     * Add an existing task as dependency for another.
     * @param task The task to add a dependency to.
     * @param dependency The dependency.
     */
    public void addDependencyTo(TaskWrapper task, TaskWrapper dependency) {
        task.getTask().addDependency(dependency.getTask());
    }

    /**
     * Changes the status of a specified Task.
     *
     * @param task The TaskWrapper containing the Task for which this method changes the
     * status.
     *
     * @param statusData A TaskStatusData object containing the data for the new Task
     * status.
     *
     * @throws IllegalArgumentException If some of the status data is incorrect.
     *
     */
    public void updateTaskStatusFor(TaskWrapper task, TaskStatusData statusData) throws IllegalArgumentException {
        if (task == null) { throw new IllegalArgumentException("Null task for status update"); }
        if (statusData == null) { throw new IllegalArgumentException("Null statusdata for status update"); }

        if (statusData.getStartTime() == null) { throw new IllegalArgumentException("Null start time for status update"); }
        if (statusData.getEndTime() == null) { throw new IllegalArgumentException("Null end time for status update"); }

        TimePeriod timePeriod = new TimePeriod(statusData.getStartTime(), statusData.getEndTime());

        if (statusData.getSuccessful()) {
            task.getTask().finish(timePeriod);
        } else {
            task.getTask().fail(timePeriod);
        }
    }

    /**
     * Changes the program's system time.
     *
     * @param time The Date containing the new system time.
     *
     * @throws IllegalArgumentException If the given Date is invalid.
     *
     */
    public void updateSystemTime(LocalDateTime time) throws IllegalArgumentException {
        if (time == null) { throw new IllegalArgumentException("Null date for system time update"); }
        Timekeeper.setTime(time);
    }
}
