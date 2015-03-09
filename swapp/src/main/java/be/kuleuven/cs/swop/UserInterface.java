package be.kuleuven.cs.swop;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.domain.project.RealProject;
import be.kuleuven.cs.swop.domain.task.Task;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface UserInterface {
    
    public void showProjects(Set<RealProject> projects);
    public void showProject(RealProject project);
    public void showTasks(Set<Task> tasks);
    public void showTask(Task task);
    public RealProject selectProject(Set<RealProject> projects);
    public Task selectTask(Set<Task> tasks);
    public TaskData getTaskDate();
    public Map<String,String> provideInfo(Map<String,String> requirements);
    public Date selectTimeStamp();
    public ProjectData getProjectData();
    
    public void showError(String error);
    
    public SessionController getSessionController();
    public void setSessionController(SessionController session);
}
