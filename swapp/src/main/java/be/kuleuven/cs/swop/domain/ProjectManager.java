package be.kuleuven.cs.swop.domain;


import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import be.kuleuven.cs.swop.domain.project.RealProject;


public class ProjectManager {

    private final Set<RealProject> projects = new HashSet<RealProject>();
    private Calendar currentTime;
    
    public ProjectManager() {
    	currentTime = Calendar.getInstance();
    	currentTime.setTimeInMillis(0);
    }

    public Set<RealProject> getProjects() {
        return projects;
    }
    
    public RealProject getProject(UUID id) {
    	for (RealProject project: projects) {
    		if (project.getId().equals(id)) {
    			return project;
    		}
    	}
    	return null;
    }

    protected boolean canHaveAsProject(RealProject project) {
        return project != null;
    }
    
    private void addProject(RealProject project){
        if (!canHaveAsProject(project) ) throw new IllegalArgumentException(ERROR_ILLEGAL_PROJECT);
        projects.add(project);
    }
    
    public RealProject createProject(String title, String description, Date dueTime) {
    	Date creationTime = new Date();
    	return createProject(title, description, creationTime, dueTime);
    }
    
    
    // Manual setting of creationTime and returning the created project is needed for the importer
    public RealProject createProject(String title, String description, Date creationTime, Date dueTime){
    	RealProject project = new RealProject(title, description, creationTime, dueTime);
    	addProject(project);
    	return project;
    }

    private static final String ERROR_ILLEGAL_PROJECT = "Invalid project for project manager";
}
