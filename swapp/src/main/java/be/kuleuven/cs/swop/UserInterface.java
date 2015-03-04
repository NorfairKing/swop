package be.kuleuven.cs.swop;

import be.kuleuven.cs.swop.domain.Project;
import be.kuleuven.cs.swop.domain.Task;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserInterface {
	
    public void showProjects(List<Project> projects);
    public void showProject(Project project);
    public void showTasks(List<Task> tasks);
    public Project selectProject(List<Project> projects);
    public Task selectTask(List<Task> tasks);
    public Map<String,String> provideInfo(Map<String,String> requirements);
    public Date selectTimeStamp();
}
