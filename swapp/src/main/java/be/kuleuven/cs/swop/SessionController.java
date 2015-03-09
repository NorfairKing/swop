package be.kuleuven.cs.swop;

import java.util.Set;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.domain.project.RealProject;
import be.kuleuven.cs.swop.domain.task.RealTask;


public class SessionController {

    private UserInterface ui;
    private FacadeController facade;

    public SessionController(UserInterface ui, FacadeController facade) {
        setUi(ui);
        setFacade(facade);
    }

    public UserInterface getUi() {
        return ui;
    }

    protected boolean canHaveAsUserInterface(UserInterface ui) {
        return ui != null;
    }

    private void setUi(UserInterface ui) {
        if (!canHaveAsUserInterface(ui)) throw new IllegalArgumentException(ERROR_ILLEGAL_UI);
        this.ui = ui;
        this.ui.setSessionController(this);
    }

    public FacadeController getFacade() {
        return facade;
    }

    protected boolean canHaveAsFacade(FacadeController facade) {
        return facade != null;
    }

    private void setFacade(FacadeController facade) {
        if (!canHaveAsFacade(facade)) throw new IllegalArgumentException(ERROR_ILLEGAL_FACADE);
        this.facade = facade;
    }
    
    
    
    public void startShowProjectsSession() {
    	// The user indicates he wants to see an overview of all projects
    	// The system shows a list of projects
    	Set<RealProject> projects = getFacade().getProjects();
        getUi().showProjects(projects);
    	
    	// The user selects a project to view more details
    	RealProject project = getUi().selectProject(projects);
    	
    	if (project == null) return;
        
        // The system presents an overview of the project details
        getUi().showProject(project);
        
        // The user selects a task for more details
        RealTask task = getUi().selectTask(project.getTasks());
        
        if (task == null) return;
        
        // The system presents an overview of the task details
        getUi().showTask(task);
    }
    
    public void startCreateProjectSession() {
    	// The user indicates he wants to create a project
    	// The system asks for the required data
    	ProjectData data = getUi().getProjectData();
    	
    	if (data == null) return; //ui should return null if user cancels.
    	
    	// The project is created using the data provided by the user
    	getFacade().createProject(data.getTitle(), data.getDescription(), data.getDueTime());
    	
    	// TODO Re-ask for data when user entered invalid info.
    }
    
    public void startCreateTaskSession() {
    	Set<RealProject> projects = getFacade().getProjects();
    	RealProject project = getUi().selectProject(projects);
    	
    	if (project == null) return;
    	
    	TaskData data = getUi().getTaskDate();
    	
    	if (data == null) return;
    	
    	try {
    		project.createTask(data);
    	}
    	catch (Exception exc) {
    		getUi().showError("Failed to create task: " + exc.getMessage());
    		// Retry if something went wrong.
    		startCreateTaskSession();
    	}
    }
    
    public void startUpdateTaskStatusSession() {
    	// TODO
    }
    
    public void startAdvanceTimeSession() {
    	// TODO
    }
    
    
    
    private static final String ERROR_ILLEGAL_UI = "Invalid user interface for session controller.";
    private static final String ERROR_ILLEGAL_FACADE = "Invalid facade controller for session controller.";
}
