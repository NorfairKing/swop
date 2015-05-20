package be.kuleuven.cs.swop;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.facade.BranchOfficeWrapper;
import be.kuleuven.cs.swop.facade.DeveloperWrapper;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.RequirementWrapper;
import be.kuleuven.cs.swop.facade.ResourceTypeWrapper;
import be.kuleuven.cs.swop.facade.ResourceWrapper;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.SimulationStepData;
import be.kuleuven.cs.swop.facade.TaskData;
import be.kuleuven.cs.swop.facade.TaskStatusData;
import be.kuleuven.cs.swop.facade.TaskWrapper;
import be.kuleuven.cs.swop.facade.UserWrapper;


public interface UserInterface {
    
    public BranchOfficeWrapper selectOffice(Set<BranchOfficeWrapper> offices);
    
    /**
     * Shows a list of all users and makes the user select one.
     * 
     * @param users A list of all known users.
     * @return The selected user from the users list.
     */
    public UserWrapper selectUser(Set<UserWrapper> users);

    /**
     * Shows a list of Projects this program manages to the user.
     *
     * @param projects
     *            The Set containing the ProjectWrappers specifying the Projects.
     */
    public void showProjects(Set<ProjectWrapper> projects);

    /**
     * Shows information about a single Project to the user.
     *
     * @param project
     *            The ProjectWrapper that contains the Project that will be shown.
     */
    public void showProject(ProjectWrapper project);

    /**
     * Shows a list of Tasks to the user.
     *
     * @param tasks
     *            The Set containing the TaskWrappers specifying the Tasks.
     */
    public void showTasks(Set<TaskWrapper> tasks);

    /**
     * Shows a information about a single Task to the user.
     * 
     * @param task
     *            The TaskWrapper that contains the Task that will be shown.
     */
    public void showTask(TaskWrapper task);
    
    public void showTaskPlanningContext(TaskWrapper task);

    /**
     * Shows a list of Projects and makes the user select one.
     *
     * @param projects
     *            The Set of ProjectWrappers specifying the Projects from which the user needs to select from.
     *
     * @return Returns a ProjectWrapper containing the Project selected by the user.
     */
    public ProjectWrapper selectProject(Set<ProjectWrapper> projects);

    /**
     * Shows a list of Tasks and makes the user select one.
     *
     * @param tasks
     *            The Set of TaskWrappers specifying the Tasks from which the user needs to select from.
     *
     * @return Returns a TaskWrapper containing the Task selected by the user.
     */
    public TaskWrapper selectTask(Set<TaskWrapper> tasks);

    /**
     * Shows a list of Projects and makes the user select one.
     *
     * @param projects
     *            The Set of ProjectWrappers specifying the Projects from which the user needs to select from.
     *
     * @return Returns a TaskWrapper containing the Task selected by the user.
     */
    public TaskWrapper selectTaskFromProjects(Set<ProjectWrapper> projects);

    /**
     * 
     * Shows a list of Projects and makes the user select one.
     * 
     * @param projectMap
     *            A map of projects to tasks that belong to them that the user should select from.
     *
     * @return Returns a TaskWrapper containing the Task selected by the user.
     */
    public TaskWrapper selectTaskFromProjects(Map<ProjectWrapper, Set<TaskWrapper>> projectMap);
    
    /**
     * Shows a list of suggested times and returns a time
     * The returned time can be one of the suggestions, but doesn't have to be
     * 
     * @param options A list of selected times
     * @return Any time
     */
    public LocalDateTime selectTime(List<LocalDateTime> options);
    
     /**
     * Shows a form in which, for each of the resource types, the user can select specific resources
     * 
     * @param options A map of each resource type to several options for the actual resources
     * @param requirements The Set of requirements that need to be satisfied.
     * @return A set of all selected resources, at least one for each type.
     */
    public Set<ResourceWrapper> selectResourcesFor(Map<ResourceTypeWrapper,List<ResourceWrapper>> options, Set<RequirementWrapper> requirements);

    /**
     * Shows a list of developers for the user to select one or more from.
     * 
     * @param developerOptions The developers to select from
     * @return A subset of the given developers
     */
    public Set<DeveloperWrapper> selectDevelopers(Set<DeveloperWrapper> developerOptions);

    /**
     * Asks the user when planning a task if the user wants to include a break in the
     * planning.
     *
     * @return True if the user chose "yes".
     */
    public boolean askToAddBreak();

    /**
     * Requests data from the user for creating a task.
     *
     * @param types The types of resources to choose from to add as resource
     * @return Returns a TaskData object containing the data for creating a Task
     */
    public TaskData getTaskData(Set<ResourceTypeWrapper> types);

    /**
     * Request data from the user for finishing or failing a Task,
     *
     * @param task The task for which the status will be updated
     * @return Returns A TaskStatusData object containing the information necessary for changing the Task's status.
     */
    public TaskStatusData getUpdateStatusData(TaskWrapper task);

    /**
     * Request data from the user necessary for creating a new Project.
     *
     * @return Returns a ProjectData object containing the data necessary for creating a new project.
     */
    public ProjectData getProjectData();

    /**
     * Requests a timestamp from the user.
     *
     * @return Returns a Date containing the requested timestamp.
     */
    public LocalDateTime getTimeStamp();

    /**
     * Show an error to the user.
     * 
     * @param error
     *            The String containing the error that will be shown to the user.
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
     * @param session
     *            The new SessionController for this user interface.
     */
    public void setSessionController(SessionController session);

    /**
     * Asks the user if he wants to continue the simulation
     * @return True if the user wants to continue, false if not.
     */
    public SimulationStepData getSimulationStepData();
    
    public String getFileName();
    
    /**
     * Starts the user interface
     * 
     * @return returns whether the usage of the interface was successful.
     */
    public boolean start();
}
