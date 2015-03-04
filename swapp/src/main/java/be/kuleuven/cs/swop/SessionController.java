package be.kuleuven.cs.swop;

import java.util.Set;

import be.kuleuven.cs.swop.domain.project.Project;
import be.kuleuven.cs.swop.domain.task.Task;


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
    
    
    
    public void showProjectsSession() {
    	// The user indicates he wants to see an overview of all projects
    	// The system shows a list of projects
    	Set<Project> projects = getFacade().getProjects();
        getUi().showProjects(projects);
    	
    	// The user selects a project to view more details
    	Project project = getUi().selectProject(projects);
        
        // The system presents an overview of the project details
        getUi().showProject(project);
        
        // The user selects a task for more details
        Task task = getUi().selectTask(project.getTasks());
        
        // The system presents an overview of the task details
        getUi().showTask(task);
    }
    
    
    
    private static final String ERROR_ILLEGAL_UI = "Invalid user interface for session controller.";
    private static final String ERROR_ILLEGAL_FACADE = "Invalid facade controller for session controller.";
}
