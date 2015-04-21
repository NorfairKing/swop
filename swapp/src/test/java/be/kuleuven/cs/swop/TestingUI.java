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
import be.kuleuven.cs.swop.facade.SimulationStepData;
import be.kuleuven.cs.swop.facade.TaskData;
import be.kuleuven.cs.swop.facade.TaskStatusData;
import be.kuleuven.cs.swop.facade.TaskWrapper;
import be.kuleuven.cs.swop.facade.UserWrapper;


public class TestingUI implements UserInterface {

    private SessionController sessionController;
    private UserWrapper       selectUser;
    private LocalDateTime     requestTime;
    private ProjectData       requestProjectData;
    private ProjectWrapper    requestProject;
    private TaskData          requestTaskData;
    private TaskWrapper       requestTask;
    private TaskStatusData    requestTaskStatusData;
    private LocalDateTime     requestSelectTime;
    private Set<ResourceWrapper> requestResourcesSet;
    private Set<DeveloperWrapper> requestDevelopersSet;
    private SimulationStepData requestSimStepData;
    
    public void setSelectUser(UserWrapper user) {
        selectUser = user;
    }
    
    @Override
    public UserWrapper selectUser(Set<UserWrapper> users) {
        return selectUser;
    }

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

    @Override
    public TaskWrapper selectTaskFromProjects(Map<ProjectWrapper, Set<TaskWrapper>> projectMap) {
        return requestTask;
    }
    
    public void setRequestTaskData(TaskData data) {
        requestTaskData = data;
    }

    @Override
    public TaskData getTaskData(Set<ResourceTypeWrapper> types) {
        return requestTaskData;
    }

    public void setRequestTaskStatusData(TaskStatusData data) {
        requestTaskStatusData = data;
    }

    @Override
    public TaskStatusData getUpdateStatusData(TaskWrapper task) {
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
    public void showError(String error) {
        System.out.println(error);
        throw new RuntimeException(error);
    }

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

    public void setSelectTime(LocalDateTime time) {
        requestSelectTime = time;
    }

    @Override
    public LocalDateTime selectTime(List<LocalDateTime> options) {
        return requestSelectTime;
    }
    
    public void setSelectResourcesFor(Set<ResourceWrapper> set) {
        requestResourcesSet = set;
    }

    @Override
    public Set<ResourceWrapper> selectResourcesFor(Map<ResourceTypeWrapper, List<ResourceWrapper>> options) {
        return requestResourcesSet;
    }
    
    public void setSelectDevelopers(Set<DeveloperWrapper> set) {
        requestDevelopersSet = set;
    }

    @Override
    public Set<DeveloperWrapper> selectDevelopers(Set<DeveloperWrapper> developerOptions) {
        return requestDevelopersSet;
    }
    
    public void setSimulationStepData(SimulationStepData data) {
        requestSimStepData = data;
    }
    
    @Override
    public SimulationStepData getSimulationStepData() {
        return requestSimStepData;
    }

}
