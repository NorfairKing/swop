package be.kuleuven.cs.swop;

import java.time.LocalDateTime;
import java.util.Set;

import be.kuleuven.cs.swop.ProjectWrapper;
import be.kuleuven.cs.swop.SessionController;
import be.kuleuven.cs.swop.TaskWrapper;
import be.kuleuven.cs.swop.UserInterface;
import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.data.TaskStatusData;


public class TestingUI implements UserInterface {

    private SessionController sessionController;
    private LocalDateTime requestTime;
    private ProjectData requestProjectData;
    private ProjectWrapper requestProject;
    private TaskData requestTaskData;
    private TaskWrapper requestTask;
    private TaskStatusData requestTaskStatusData;
    
    @Override
    public void showProjects(Set<ProjectWrapper> projects) { }

    @Override
    public void showProject(ProjectWrapper project) { }

    @Override
    public void showTasks(Set<TaskWrapper> tasks) { }

    @Override
    public void showTask(TaskWrapper task) { }

    /**
     * Set the request project.
     * @param proj The project that should be returned when calling selectProject
     */
    public void setRequestProject(ProjectWrapper proj) {
        requestProject = proj;
    }
    /**
     * Ignores the set and just returns the requestProject.
     */
    @Override
    public ProjectWrapper selectProject(Set<ProjectWrapper> projects) {
        return requestProject;
    }

    @Override
    public TaskWrapper selectTask(Set<TaskWrapper> tasks) {
        if (tasks.size() == 0) {
            return null;
        }
        else {
            return tasks.stream().findFirst().get();
        }
    }

    public void setRequestTask(TaskWrapper task) {
        requestTask = task;
    }
    /**
     * Ignores the list and just returns the set request task.
     */
    @Override
    public TaskWrapper selectTaskFromProjects(Set<ProjectWrapper> projects) {
        return requestTask;
    }

    
    public void setRequestTaskData(TaskData data) {
        requestTaskData = data;
    }
    @Override
    public TaskData getTaskData() {
        return requestTaskData;
    }

    
    public void setRequestTaskStatusData(TaskStatusData data) {
        requestTaskStatusData = data;
    }
    
    @Override
    public TaskStatusData getUpdateStatusData() {
        return requestTaskStatusData;
    }

    public void setRequestProjectDate(ProjectData data) {
        requestProjectData = data;
    }
    
    @Override
    public ProjectData getProjectData() {
        return requestProjectData;
    }

    public void setRequestTime(LocalDateTime time) {
        requestTime = time;
    }
    
    @Override
    public LocalDateTime getTimeStamp() {
        return requestTime;
    }

    @Override
    public void showError(String error) { }

    @Override
    public SessionController getSessionController() {
        return sessionController;
    }

    @Override
    public void setSessionController(SessionController session) {
        sessionController = session;
    }

    @Override
    public void start() { }

}
