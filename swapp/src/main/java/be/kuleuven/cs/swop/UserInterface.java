package be.kuleuven.cs.swop;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.data.TaskStatusData;

import java.util.Set;

public interface UserInterface {
    
    public void showProjects(Set<ProjectWrapper> projects);
    public void showProject(ProjectWrapper project);
    public void showTasks(Set<TaskWrapper> tasks);
    public void showTask(TaskWrapper task);
    public ProjectWrapper selectProject(Set<ProjectWrapper> projects);
    public TaskWrapper selectTask(Set<TaskWrapper> tasks);
    public TaskWrapper selectTaskFromProjects(Set<ProjectWrapper> projects);
    public TaskData getTaskData();
    public TaskStatusData getUpdateStatusData();
    public ProjectData getProjectData();
    
    public void showError(String error);
    
    public SessionController getSessionController();
    public void setSessionController(SessionController session);
}
