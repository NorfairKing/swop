package be.kuleuven.cs.swop;


import java.util.Date;
import java.util.Set;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.data.TaskStatusData;


public interface UserInterface {

    /**
     * Shows a list of Projects this program manages to the user.
     *
     * @param projects The Set containing the ProjectWrappers specifying the Projects.
     */
    public void showProjects(Set<ProjectWrapper> projects);

    /**
     * Shows information about a single Project to the user.
     *
     * @param project The ProjectWrapper that contains the Project that will be shown.
     */
    public void showProject(ProjectWrapper project);

    /**
     * Shows a list of Tasks to the user.
     *
     * @param tasks The Set containing the TaskWrappers specifying the Tasks.
     */
    public void showTasks(Set<TaskWrapper> tasks);

    /**
     * Shows a information about a single Task to the user.
     * @param task The TaskWrapper that contains the Task that will be shown.
     */
    public void showTask(TaskWrapper task);

    /**
     * Shows a list of Projects and makes the user select one.
     *
     * @param projects The Set of ProjectWrappers specifying the Projects from which the
     * user needs to select from.
     *
     * @return Returns a ProjectWrapper containing the Project selected by the user.
     */
    public ProjectWrapper selectProject(Set<ProjectWrapper> projects);

    /**
     * Shows a list of Tasks and makes the user select one.
     *
     * @param tasks The Set of TaskWrappers specifying the Tasks from which the
     * user needs to select from.
     *
     * @return Returns a TaskWrapper containing the Task selected by the user.
     */
    public TaskWrapper selectTask(Set<TaskWrapper> tasks);

    /**
     * Shows a list of Projects and makes the user select one.
     *
     * @param projects The Set of ProjectWrappers specifying the Projects from which the
     * user needs to select from.
     *
     * @return Returns a ProjectWrapper containing the Project selected by the user.
     */
    public TaskWrapper selectTaskFromProjects(Set<ProjectWrapper> projects);

    /**
     * Requests data from the user for creating a task.
     *
     * @return Returns a TaskData object containing the data for creating a Task.
     */
    public TaskData getTaskData();

    /**
     * Request data from the user for finishing or failing a Task,
     * @return Returns A TaskStatusData object containing the information necessary for
     * changing the Task's status.
     */
    public TaskStatusData getUpdateStatusData();

    /**
     * Request data from the user necessary for creating a new Project.
     *
     * @return Returns a ProjectData object containing the data necessary for creating a
     * new project.
     */
    public ProjectData getProjectData();

    /**
     * Requests a timestamp from the user.
     *
     * @return Returns a Date containing the requested timestamp.
     */
    public Date getTimeStamp();

    /**
     * Show an error to the user.
     * @param error The String containing the error that will be shown to the user.
     */
    public void showError(String error);

    /**
     * Retrieves the session controller for this program.
     *
     * @return Returns the SessionController for this program.
     */
    public SessionController getSessionController();

    /**
     * Changes the interface's session controller to the given session controller.
     *
     * @param session The new SessionController for this user interface.
     */
    public void setSessionController(SessionController session);

    /**
     * Starts the user interface
     */
    public void start();
}
