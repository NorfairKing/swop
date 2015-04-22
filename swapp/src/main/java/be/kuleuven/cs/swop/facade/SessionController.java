package be.kuleuven.cs.swop.facade;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.UserInterface;


public class SessionController {

    private UserInterface   ui;
    private TaskMan         taskMan;
    private TaskMan.Memento taskManBackup;
    private UserWrapper     currentUser;

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

    public UserWrapper getCurrentUser() {
        return this.currentUser;
    }

    protected boolean canHaveAsCurrentUser(UserWrapper user) {
        return user != null;
    }

    private void setCurrentUser(UserWrapper user) {
        if (!canHaveAsCurrentUser(user)) { throw new IllegalArgumentException(ERROR_ILLEGAL_USER); }
        this.currentUser = user;
    }

    public boolean isLoggedIn() {
        return this.currentUser != null;
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
     * Starts a session to select the user. This isn't specified in the exercise, but in the current design we need this.
     */
    public void startSelectUserSession() {
        Set<UserWrapper> users = getTaskMan().getUsers();
        UserWrapper user = getUi().selectUser(users);
        if (user == null) { return; }
        setCurrentUser(user);
    }

    /**
     * Starts the "show projects" use case and lets the user interface show a list all the projects.
     */
    public void startShowProjectsSession() {
        if (!isLoggedIn()) {
            getUi().showError(ERROR_NO_LOGIN);
            return;
        }
        // The user indicates he wants to see an overview of all projects
        // The system shows a list of projects
        Set<ProjectWrapper> projects = getTaskMan().getProjects();
        getUi().showProjects(projects);

        // The user selects a project to view more details
        ProjectWrapper project = getUi().selectProject(projects);

        // if the user indicates he wants to leave the overview.
        if (project == null) {
            handleSimulationStep();
            return;
        }

        // The system presents an overview of the project details
        getUi().showProject(project);

        // The user selects a task for more details
        TaskWrapper task = getUi().selectTask(project.getTasks());

        // if the user indicates he wants to leave the overview.
        if (task == null) {
            handleSimulationStep();
            return;
        }

        // The system presents an overview of the task details
        getUi().showTask(task);

        // End session
        handleSimulationStep();
    }

    /**
     * Starts the "create project" use case which lets the user interface request the information necessary for creating a new project from the user.
     */
    public void startCreateProjectSession() {
        if (!isLoggedIn()) {
            getUi().showError(ERROR_NO_LOGIN);
            return;
        }
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

        // End session
        handleSimulationStep();
    }

    /**
     * Starts the "create task" use case which lets the user interface request the information necessary for creating a new task from the user.
     */
    public void startCreateTaskSession() {
        if (!isLoggedIn()) {
            getUi().showError(ERROR_NO_LOGIN);
            return;
        }
        // The user indicates he wants to create a new task
        do {
            // the system asks for which project to create a task
            // slight deviation from use-case, as they don't specify when the user should select this
            Set<ProjectWrapper> projects = getTaskMan().getProjects();
            ProjectWrapper project = getUi().selectProject(projects);

            // If the user indicates he wants to leave the overview.
            if (project == null) break;

            // The system shows the task creation form
            Set<ResourceTypeWrapper> resourceTypes = getTaskMan().getResourceTypes();
            TaskData data = getUi().getTaskData(resourceTypes);

            // if the user indicates he wants to leave the overview.
            if (data == null) break;

            // The system creates the task
            try {
                getTaskMan().createTaskFor(project, data);
                // Finish only when successful
                break;
            } catch (IllegalArgumentException e) {
                getUi().showError("Failed to create task: " + e.getMessage());
            }
        } while (true); // loop until proper data is given, or the user cancels.

        // End session
        handleSimulationStep();
    }

    public void startPlanTaskSession() {
        if (!isLoggedIn()) {
            getUi().showError(ERROR_NO_LOGIN);
            return;
        }
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
        if (selectedTask == null) {
            handleSimulationStep();
            return;
        }

        List<LocalDateTime> timeOptions = taskMan.getPlanningTimeOptions(selectedTask);

        // The system shows the first three possible starting times.
        // The user selects a proposed time
        LocalDateTime chosenTime = getUi().selectTime(timeOptions);
        if (chosenTime == null) {
            handleSimulationStep();
            return;
        }

        Map<ResourceTypeWrapper, List<ResourceWrapper>> resourceOptions = taskMan.getPlanningResourceOptions(selectedTask, chosenTime);
        Set<ResourceWrapper> chosenResources = getUi().selectResourcesFor(resourceOptions);
        if (chosenResources == null) {
            handleSimulationStep();
            return;
        }

        Set<DeveloperWrapper> developerOptions = taskMan.getPlanningDeveloperOptions(selectedTask, chosenTime);
        Set<DeveloperWrapper> chosenDevelopers = getUi().selectDevelopers(developerOptions);
        if (chosenDevelopers == null) {
            handleSimulationStep();
            return;
        }

        taskMan.createPlanning(selectedTask, chosenTime, chosenResources, chosenDevelopers);

        // End session
        handleSimulationStep();
    }

    public void startResolveConflictSession() {
        if (!isLoggedIn()) {
            getUi().showError(ERROR_NO_LOGIN);
            return;
        }

        // End session
        handleSimulationStep();
    }

    /**
     * Starts the "update task status" use case which lets the user interface request the information necessary for updating the task status from the user.
     */
    public void startUpdateTaskStatusSession() {
        if (!isLoggedIn()) {
            getUi().showError(ERROR_NO_LOGIN);
            return;
        }
        // User indicates he wants to update the status of a task

        // The system show a list of all available tasks and the projects they belong to
        // The user selects a task he wants to change
        Set<ProjectWrapper> projects = getTaskMan().getProjects();
        TaskWrapper task = getUi().selectTaskFromProjects(projects);

        // if the user indicates he wants to leave the overview.
        if (task == null) {
            handleSimulationStep();
            return;
        }

        if (task.isFinal()) {
            getUi().showError("Can't update task: this task is already final.");
            handleSimulationStep();
            return;
        }

        do {
            try {
                // The user enters all required details
                TaskStatusData statusData = getUi().getUpdateStatusData(task);

                // if the user cancels.
                if (statusData == null) break;

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

        // End session
        handleSimulationStep();
    }

    /**
     * Starts the "advance time" use case which lets the user interface request a new time for the system from the user.
     */
    public void startAdvanceTimeSession() {
        if (!isLoggedIn()) {
            getUi().showError(ERROR_NO_LOGIN);
            return;
        }
        // The user indicates he wants to modify the system time
        // The system allows the user to choose a new time
        LocalDateTime time = getUi().getTimeStamp();

        // if the user cancels.
        if (time == null) {
            handleSimulationStep();
            return;
        }

        // the system updates the system time.
        getTaskMan().updateSystemTime(time);

        // End session
        handleSimulationStep();
    }

    public void startRunSimulationSession() {
        if (!isLoggedIn()) {
            getUi().showError(ERROR_NO_LOGIN);
            return;
        }
        // The user indicates he wants to start a simulation
        taskManBackup = getTaskMan().saveToMemento();
    }

    private void handleSimulationStep() {
        if (taskManBackup != null) {
            SimulationStepData simData = getUi().getSimulationStepData();
            if (!simData.getContinueSimulation()) {
                if (simData.getRealizeSimulation()) {
                    realizeSimulation();
                }
                else {
                    cancelSimulation();
                }
            }
        }
    }

    private void realizeSimulation() {
        // The user indicates he wants to realize the simulation
        // Nothing has to be done. Just throw away the backup.
        taskManBackup = null;
    }

    private void cancelSimulation() {
        // The user indicates he wants to cancel the simulation.
        getTaskMan().restoreFromMemento(taskManBackup);
        taskManBackup = null;
    }

    private static final String ERROR_NO_LOGIN       = "No user has logged in, please log in first.";
    private static final String ERROR_ILLEGAL_USER   = "Invalid user for session controller.";
    private static final String ERROR_ILLEGAL_UI     = "Invalid user interface for session controller.";
    private static final String ERROR_ILLEGAL_FACADE = "Invalid facade controller for session controller.";
}
