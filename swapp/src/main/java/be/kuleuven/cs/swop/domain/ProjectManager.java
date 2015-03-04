package be.kuleuven.cs.swop.domain;


import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.project.Project;


public class ProjectManager {

    private Set<Project> projects = new HashSet<Project>();

    public ProjectManager() {}

    public Set<Project> getProjects() {
        return projects;
    }

    protected boolean canHaveAsProject(Project project) {
        return project != null;
    }
    
    public void addProject(Project project){
        if (!canHaveAsProject(project) )throw new IllegalArgumentException(ERROR_ILLEGAL_PROJECT);
        projects.add(project);
    }

    private static final String ERROR_ILLEGAL_PROJECT = "Invalid project for project manager";
}
