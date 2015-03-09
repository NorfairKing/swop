package be.kuleuven.cs.swop.domain.project;


import java.util.Date;
import java.util.Set;
import java.util.UUID;

import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.domain.task.RealTask;


public class ProjectProxy implements Project {

    private Project project;

    public ProjectProxy(Project project) {
        setProject(project);
    }

    private Project getProject() {
        return project;
    }

    protected boolean canHaveAsProject(Project project) {
        return project != null;
    }

    private void setProject(Project project) {
        if (!canHaveAsProject(project)) throw new IllegalArgumentException(ERROR_ILLEGAL_PROJECT);
        this.project = project;
    }

    @Override
    public String getTitle() {
        return getProject().getTitle();
    }

    @Override
    public void setTitle(String title) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_PROJECT);
    }

    @Override
    public String getDescription() {
        return getProject().getDescription();
    }

    @Override
    public void setDescription(String description) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_PROJECT);
    }

    @Override
    public boolean isOngoing() {
        return getProject().isOngoing();
    }

    @Override
    public boolean isFinished() {
        return getProject().isFinished();
    }

    @Override
    public Date getCreationTime() {
        return getProject().getCreationTime();
    }

    @Override
    public Date getDueTime() {
        return getProject().getDueTime();
    }

    @Override
    public void setDueTime(Date dueTime) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_PROJECT);
    }

    @Override
    public void addTask(RealTask task) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_PROJECT);
    }

    @Override
    public RealTask createTask(TaskData data) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_PROJECT);
    }

    @Override
    public Set<RealTask> getTasks() {
        // TODO Protect this
        return getProject().getTasks();
    }

    @Override
    public UUID getId() {
        return getProject().getId();
    }

    private final String ERROR_ILLEGAL_ACCESS_PROJECT = "Seg gij vuile kakker, blijft hier eens af!";
    private final String ERROR_ILLEGAL_PROJECT = "Illegal project for project proxy";
}
