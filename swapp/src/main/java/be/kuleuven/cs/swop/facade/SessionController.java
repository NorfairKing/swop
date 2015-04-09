package be.kuleuven.cs.swop.facade;


import java.util.List;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.UserInterface;


public class SessionController {

    private UserInterface ui;
    private TaskMan       taskMan;

    /**
     * Full constructor
     *
     * @param ui
     *            The UserInterface used to interact with the user.
     *
     * @param facade
     *            The FacadeController that collects and protects the information from the TaskMan.
     *
     */
    public SessionController(UserInterface ui, TaskMan facade) {
        setUi(ui);
        setFacade(facade);
    }

    /**
     *
     * Retrieves the user interface.
     *
     * @return Returns a UserInterface.
     *
     */
    public UserInterface getUi() {
        return ui;
    }

    /**
     * Checks whether or not the given user interface can be used by our program.
     *
     * @param ui
     *            The UserInterface to be checked for validity.
     *
     * @return Returns true if the given user interface is valid.
     *
     */
    protected boolean canHaveAsUserInterface(UserInterface ui) {
        return ui != null;
    }

    private void setUi(UserInterface ui) {
        if (!canHaveAsUserInterface(ui)) throw new IllegalArgumentException(ERROR_ILLEGAL_UI);
        this.ui = ui;
        this.ui.setSessionController(this);
    }

    /**
     * Retrieves the facade of this program.
     *
     * @return Returns a FacadeController.
     *
     */
    public TaskMan getTaskMan() {
        return taskMan;
    }

    /**
     * Checks whether or not the given facade is a valid facade for this program.
     *
     * @param facade
     *            The FacadeController to be checked for validity.
     *
     * @return Returns true if the given facade is valid for this program.
     *
     */
    protected boolean canHaveAsFacade(TaskMan facade) {
        return facade != null;
    }

    private void setFacade(TaskMan facade) {
        if (!canHaveAsFacade(facade)) throw new IllegalArgumentException(ERROR_ILLEGAL_FACADE);
        this.taskMan = facade;
    }
    
    /**
     * Starts a session to select the user.
     * This isn't specified in the exercise, but in the current design we need this.
     */
    public void startSelectUserSession() {
        Set<UserWrapper> users = getTaskMan().getUsers();
        
        UserWrapper user = getUi().selectUser(users);
        
        if (user == null) return;
        
        getTaskMan().setActiveUser(user);
    }

    /**
     * Starts the "show projects" use case and lets the user interface show a list all the projects.
     */
    public void startShowProjectsSession() {
        // The user indicates he wants to see an overview of all projects
        // The system shows a list of projects
        Set<ProjectWrapper> projects = getTaskMan().getProjects();
        getUi().showProjects(projects);

        // The user selects a project to view more details
        ProjectWrapper project = getUi().selectProject(projects);

        // if the user indicates he wants to leave the overview.
        if (project == null) return;

        // The system presents an overview of the project details
        getUi().showProject(project);

        // The user selects a task for more details
        TaskWrapper task = getUi().selectTask(project.getTasks());

        // if the user indicates he wants to leave the overview.
        if (task == null) return;

        // The system presents an overview of the task details
        getUi().showTask(task);
    }

    /**
     * Starts the "create project" use case which lets the user interface request the information necessary for creating a new project from the user.
     */
    public void startCreateProjectSession() {
        do {
            // The user indicates he wants to create a project
            // The system asks for the required data
            ProjectData data = getUi().getProjectData();

            // if the user indicates he wants to leave the overview.
            if (data == null) return; // ui should return null if user cancels.

            // The project is created using the data provided by the user
            try {
                getTaskMan().createProject(data);
                // finish only when successful
                break;
            } catch (IllegalArgumentException e) {
                getUi().showError("Failed to create task: " + e.getMessage());
            }
        } while (true); // loop until user gives proper data, or cancels.
    }

    /**
     * Starts the "create task" use case which lets the user interface request the information necessary for creating a new task from the user.
     */
    public void startCreateTaskSession() {
        // The user indicates he wants to create a new task
        do {
            // the system asks for which project to create a task
            // slight deviation from use-case, as they don't specify when the user should select this
            Set<ProjectWrapper> projects = getTaskMan().getProjects();
            ProjectWrapper project = getUi().selectProject(projects);

            // If the user indicates he wants to leave the overview.
            if (project == null) return;

            // The system shows the task creation form
            TaskData data = getUi().getTaskData();

            // if the user indiactes he wants to leave the overview.
            if (data == null) return;

            // The system creates the task
            try {
                getTaskMan().createTaskFor(project, data);
                // Finish only when successful
                break;
            } catch (IllegalArgumentException e) {
                getUi().showError("Failed to create task: " + e.getMessage());
            }
        } while (true); // loop until proper data is given, or the user cancels.
    }

    public void startPlanTaskSession() {
        // The system shows a list of all currently unplanned tasks and the project they belong to.
        Set<ProjectWrapper> allProjects = taskMan.getProjects();
        Map<ProjectWrapper, Set<TaskWrapper>> unplannedTaskMap = new HashMap<ProjectWrapper, Set<TaskWrapper>>();

        for (ProjectWrapper p : allProjects) {
            Set<TaskWrapper> unplannedTasks = taskMan.getUnplannedTasksOf(p);
            if (!unplannedTasks.isEmpty()) {
                unplannedTaskMap.put(p, unplannedTasks);
            }
        }
        // The user selects the tasks he wants to plan
        TaskWrapper selectedTask = getUi().selectTaskFromProjects(unplannedTaskMap);
        if (selectedTask == null) return;

        List<LocalDateTime> timeOptions = taskMan.getPlanningTimeOptions(selectedTask);

        // The system shows the first three possible starting times.
        // The user selects a proposed time
        LocalDateTime chosenTime = getUi().selectTime(timeOptions);
        if (chosenTime == null) return;

        Map<ResourceTypeWrapper, List<ResourceWrapper>> resourceOptions = taskMan.getPlanningResourceOptions(selectedTask, chosenTime);
        Set<ResourceWrapper> chosenResources = getUi().selectResourcesFor(resourceOptions);
        if (chosenResources == null) return;

        Set<DeveloperWrapper> developerOptions = taskMan.getPlanningDeveloperOptions(selectedTask, chosenTime);
        Set<DeveloperWrapper> chosenDevelopers = getUi().selectDevelopers(developerOptions);
        if (chosenDevelopers == null) return;

        taskMan.createPlanning(selectedTask, chosenTime, chosenResources, chosenDevelopers);
    }

    public void startResolveConflictSession() {

    }

    /**
     * Starts the "update task status" use case which lets the user interface request the information necessary for updating the task status from the user.
     */
    public void startUpdateTaskStatusSession() {
        // User indicates he wants to update the status of a task

        // The system show a list of all available tasks and the projects they belong to
        // The user selects a task he wants to change
        Set<ProjectWrapper> projects = getTaskMan().getProjects();
        TaskWrapper task = getUi().selectTaskFromProjects(projects);

        // if the user indicates he wants to leave the overview.
        if (task == null) return;

        do {
            try {
                // The user enters all required details
                TaskStatusData statusData = getUi().getUpdateStatusData();

                // if the user cancels.
                if (statusData == null) return;

                // the system updates the task status
                getTaskMan().updateTaskStatusFor(task, statusData);

                // Finish wel successful
                break;
            } catch (IllegalArgumentException e) {
                getUi().showError("Failed to update task: " + e.getMessage());
            } catch (IllegalStateException e) {
                getUi().showError("The task can't be updated in it's current state: " + e.getMessage());
            }
        } while (true); // loop until proper data was given or user canceled.
    }

    /**
     * Starts the "advance time" use case which lets the user interface request a new time for the system from the user.
     */
    public void startAdvanceTimeSession() {
        // The user indicates he wants to modify the system time
        // The system allows the user to choose a new time
        LocalDateTime time = getUi().getTimeStamp();

        // if the user cancels.
        if (time == null) return;

        // the system updates the system time.
        getTaskMan().updateSystemTime(time);
    }

    public void startRunSimulationSession() {
        UserInterface ui = getUi().getSimulationUI();
        TaskMan simulationFacade = getTaskMan().getDeepCopy();
        new SessionController(ui, simulationFacade);
        boolean successful = ui.start();
        if (successful) {
            this.taskMan = simulationFacade;
        }
    }

    private static final String ERROR_ILLEGAL_UI     = "Invalid user interface for session controller.";
    private static final String ERROR_ILLEGAL_FACADE = "Invalid facade controller for session controller.";
}
