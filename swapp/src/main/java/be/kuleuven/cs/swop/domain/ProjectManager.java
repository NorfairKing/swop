package be.kuleuven.cs.swop.domain;


import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import be.kuleuven.cs.swop.domain.project.Project;


public class ProjectManager {

    private final Set<Project> projects = new HashSet<Project>();
    private Calendar currentTime;
    
    public ProjectManager() {
    	currentTime = Calendar.getInstance();
    	currentTime.setTimeInMillis(0);
    }

    public Set<Project> getProjects() {
        return projects;
    }
    
    public Project getProject(UUID id) {
    	for (Project project: projects) {
    		if (project.getId().equals(id)) {
    			return project;
    		}
    	}
    	return null;
    }

    protected boolean canHaveAsProject(Project project) {
        return project != null;
    }
    
    private void addProject(Project project){
        if (!canHaveAsProject(project) ) throw new IllegalArgumentException(ERROR_ILLEGAL_PROJECT);
        projects.add(project);
    }
    
    public Project createProject(String title, String description, Date dueTime) {
    	Date creationTime = new Date();
    	return createProject(title, description, creationTime, dueTime);
    }
    
    
    // Manual setting of creationTime and returning the created project is needed for the importer
    public Project createProject(String title, String description, Date creationTime, Date dueTime){
    	Project project = new Project(title, description, creationTime, dueTime);
    	addProject(project);
    	return project;
    }

    private static final String ERROR_ILLEGAL_PROJECT = "Invalid project for project manager";
}
