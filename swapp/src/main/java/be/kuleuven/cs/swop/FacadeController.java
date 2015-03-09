package be.kuleuven.cs.swop;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.domain.ProjectManager;
import be.kuleuven.cs.swop.domain.project.Project;

public class FacadeController {
	ProjectManager projectManager;

	public FacadeController() {
		projectManager = new ProjectManager();
	}
	
	public FacadeController(String initialisationFilePath){
		projectManager = new ProjectManager(initialisationFilePath);
	}

	public Set<ProjectWrapper> getProjects() {
		Set<ProjectWrapper> result = new HashSet<ProjectWrapper>();
		for( Project realProject : projectManager.getProjects()){
			result.add(new ProjectWrapper(realProject));
		}
		return  result;
	}
	
	public void createProject(ProjectData data){
		projectManager.createProject(data.getTitle(), data.getDescription(), data.getDueTime());
	}
	
	public void createTaskFor(ProjectWrapper project, TaskData data) {
		project.getProject().createTask(data.getDescription(), data.getEstimatedDuration(), data.getAcceptableDeviation());
	}
}
