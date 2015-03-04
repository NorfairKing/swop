package be.kuleuven.cs.swop;

import be.kuleuven.cs.swop.domain.project.Project;
import be.kuleuven.cs.swop.domain.task.Task;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface UserInterface {
    
    public void showProjects(Set<Project> projects);
    public void showProject(Project project);
    public void showTasks(Set<Task> tasks);
    public void showTask(Task task);
    public Project selectProject(Set<Project> projects);
    public Task selectTask(Set<Task> tasks);
    public Map<String,String> provideInfo(Map<String,String> requirements);
    public Date selectTimeStamp();
    
    public SessionController getSessionController();
    public void setSessionController(SessionController session);
}
