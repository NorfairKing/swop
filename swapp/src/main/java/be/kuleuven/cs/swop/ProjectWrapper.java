package be.kuleuven.cs.swop;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.project.Project;
import be.kuleuven.cs.swop.domain.task.Task;


public class ProjectWrapper {

    private Project project;

    public ProjectWrapper(Project project) {
        setProject(project);
    }

    Project getProject() {
        return project;
    }

    protected boolean canHaveAsProject(Project project) {
        return project != null;
    }

    private void setProject(Project project) {
        if (!canHaveAsProject(project)) throw new IllegalArgumentException(ERROR_ILLEGAL_PROJECT);
        this.project = project;
    }

    public String getTitle() {
        return getProject().getTitle();
    }

    public String getDescription() {
        return getProject().getDescription();
    }

    public boolean isOngoing() {
        return getProject().isOngoing();
    }

    public boolean isFinished() {
        return getProject().isFinished();
    }

    public Date getCreationTime() {
        return getProject().getCreationTime();
    }

    public Date getDueTime() {
        return getProject().getDueTime();
    }

    public Set<TaskWrapper> getTasks() {
        Set<TaskWrapper> result = new HashSet<TaskWrapper>();
        for (Task realTask : project.getTasks()) {
            result.add(new TaskWrapper(realTask));
        }
        return result;
    }

    private final String ERROR_ILLEGAL_PROJECT = "Illegal project for project wrapper";
}
