package be.kuleuven.cs.swop;

import java.util.Set;

import be.kuleuven.cs.swop.domain.ProjectManager;
import be.kuleuven.cs.swop.domain.project.Project;


public class FacadeController {
	
	ProjectManager projectManager;
    
    public FacadeController(){
    	projectManager = new ProjectManager();
    }
    
    public Set<Project> getProjects() {
    	return projectManager.getProjects();
    }
    
    public void initialiseWith(String initFile){
        // TODO initialise with file.
    }
    
}
