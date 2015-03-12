package be.kuleuven.cs.swop;


import java.util.Date;
import java.util.Set;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.data.TaskStatusData;


public class SessionController {

    private UserInterface    ui;
    private FacadeController facade;

    /**
     * Full constructor
     *
     * @param ui The UserInterface used to interact with the user.
     *
     * @param facade The FacadeController that collects and protects the information from
     * the TaskMan.
     *
     */
    public SessionController(UserInterface ui, FacadeController facade) {
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
     * @param ui The UserInterface to be checked for validity.
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
    public FacadeController getFacade() {
        return facade;
    }

    /**
     * Checks whether or not the given facade is a valid facade for this program.
     *
     * @param facade The FacadeController to be checked for validity.
     *
     * @return Returns true if the given facade is valid for this program.
     *
     */
    protected boolean canHaveAsFacade(FacadeController facade) {
        return facade != null;
    }

    private void setFacade(FacadeController facade) {
        if (!canHaveAsFacade(facade)) throw new IllegalArgumentException(ERROR_ILLEGAL_FACADE);
        this.facade = facade;
    }

    /**
     * Starts the "show projects" use case and lets the user interface show a list all the
     * projects.
     */
    public void startShowProjectsSession() {
        // The user indicates he wants to see an overview of all projects
        // The system shows a list of projects
        Set<ProjectWrapper> projects = getFacade().getProjects();
        getUi().showProjects(projects);

        // The user selects a project to view more details
        ProjectWrapper project = getUi().selectProject(projects);

        if (project == null) return;

        // The system presents an overview of the project details
        getUi().showProject(project);

        // The user selects a task for more details
        TaskWrapper task = getUi().selectTask(project.getTasks());

        if (task == null) return;

        // The system presents an overview of the task details
        getUi().showTask(task);
    }

    /**
     * Starts the "create project" use case which lets the user interface request
     * the information necessary for creating a new project from the user.
     */
    public void startCreateProjectSession() {
        do {
            // The user indicates he wants to create a project
            // The system asks for the required data
            ProjectData data = getUi().getProjectData();

            if (data == null) return; // ui should return null if user cancels.

            // The project is created using the data provided by the user
            try {
                getFacade().createProject(data);
                // finish only when successful
                break;
            } catch (IllegalArgumentException e) {
                getUi().showError("Failed to create task: " + e.getMessage());
            }
        } while (true);
    }

    /**
     * Starts the "create task" use case which lets the user interface request
     * the information necessary for creating a new task from the user.
     */
    public void startCreateTaskSession() {
        do {
            Set<ProjectWrapper> projects = getFacade().getProjects();
            ProjectWrapper project = getUi().selectProject(projects);

            if (project == null) return;

            TaskData data = getUi().getTaskData();

            if (data == null) return;

            try {
                getFacade().createTaskFor(project, data);
                // Finish only when successful
                break;
            } catch (IllegalArgumentException e) {
                getUi().showError("Failed to create task: " + e.getMessage());
            }
        } while (true);
    }

    /**
     * Starts the "update task status" use case which lets the user interface request
     * the information necessary for updating the task status from the user.
     */
    public void startUpdateTaskStatusSession() {
        // User indicates he wants to update the status of a task
        Set<ProjectWrapper> projects = getFacade().getProjects();
        TaskWrapper task = getUi().selectTaskFromProjects(projects);

        if (task == null) return;

        do {
            try {
                TaskStatusData statusData = getUi().getUpdateStatusData();

                if (statusData == null) return;

                getFacade().updateTaskStatusFor(task, statusData);

                // Finish wel successful
                break;
            } catch (IllegalArgumentException e) {
                getUi().showError("Failed to update task: " + e.getMessage());
            } catch (IllegalStateException e) {
                getUi().showError("The task can't be updated in it's current state: " + e.getMessage());
            }
        } while (true);
    }

    /**
     * Starts the "advance time" use case which lets the user interface request a new time
     * for the system from the user.
     */
    public void startAdvanceTimeSession() {
        // The user indicates he wants to modify the system time
        // The system allows the user to choose a new time
        Date time = getUi().getTimeStamp();

        if (time == null) return;

        getFacade().updateSystemTime(time);
    }

    private static final String ERROR_ILLEGAL_UI     = "Invalid user interface for session controller.";
    private static final String ERROR_ILLEGAL_FACADE = "Invalid facade controller for session controller.";
}
