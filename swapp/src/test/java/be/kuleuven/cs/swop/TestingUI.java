package be.kuleuven.cs.swop;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.UserInterface;
import be.kuleuven.cs.swop.facade.DeveloperWrapper;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.ResourceTypeWrapper;
import be.kuleuven.cs.swop.facade.ResourceWrapper;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.TaskData;
import be.kuleuven.cs.swop.facade.TaskStatusData;
import be.kuleuven.cs.swop.facade.TaskWrapper;


public class TestingUI implements UserInterface {

    private SessionController sessionController;
    private LocalDateTime     requestTime;
    private ProjectData       requestProjectData;
    private ProjectWrapper    requestProject;
    private TaskData          requestTaskData;
    private TaskWrapper       requestTask;
    private TaskStatusData    requestTaskStatusData;

    @Override
    public void showProjects(Set<ProjectWrapper> projects) {}

    @Override
    public void showProject(ProjectWrapper project) {}

    @Override
    public void showTasks(Set<TaskWrapper> tasks) {}

    @Override
    public void showTask(TaskWrapper task) {}

    /**
     * Set the request project.
     * 
     * @param proj
     *            The project that should be returned when calling selectProject
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
    public void showError(String error) {}

    @Override
    public SessionController getSessionController() {
        return sessionController;
    }

    @Override
    public void setSessionController(SessionController session) {
        sessionController = session;
    }

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public TaskWrapper selectTaskFromProjects(Map<ProjectWrapper, Set<TaskWrapper>> projectMap) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LocalDateTime selectTime(List<LocalDateTime> options) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<ResourceTypeWrapper, ResourceWrapper> selectResourcesFor(Map<ResourceTypeWrapper, List<ResourceWrapper>> options) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<DeveloperWrapper> selectDevelopers(Set<DeveloperWrapper> developerOptions) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserInterface getSimulationUI() {
        return this;
    }

}
