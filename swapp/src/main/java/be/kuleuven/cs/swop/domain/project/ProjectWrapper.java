package be.kuleuven.cs.swop.domain.project;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.domain.task.Task;
import be.kuleuven.cs.swop.domain.task.TaskWrapper;


public class ProjectWrapper{

    private Project project;

    public ProjectWrapper(Project project) {
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

    public String getTitle() {
        return getProject().getTitle();
    }

    public void setTitle(String title) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_PROJECT);
    }

    public String getDescription() {
        return getProject().getDescription();
    }

    public void setDescription(String description) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_PROJECT);
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

    public void setDueTime(Date dueTime) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_PROJECT);
    }

    public void addTask(Task task) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_PROJECT);
    }

    public Task createTask(TaskData data) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_PROJECT);
    }
    
	public Set<TaskWrapper> getTasks() {
		Set<TaskWrapper> result = new HashSet<TaskWrapper>();
		for( Task realTask : project.getTasks()){
			result.add(new TaskWrapper(realTask));
		}
		return  result;
	}
    private final String ERROR_ILLEGAL_ACCESS_PROJECT = "Seg gij vuile kakker, blijft hier eens af!";
    private final String ERROR_ILLEGAL_PROJECT = "Illegal project for project proxy";
}
