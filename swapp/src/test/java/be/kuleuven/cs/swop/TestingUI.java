package be.kuleuven.cs.swop;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.UserInterface;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.facade.BranchOfficeWrapper;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.SimulationStepData;
import be.kuleuven.cs.swop.facade.TaskData;
import be.kuleuven.cs.swop.facade.TaskWrapper;
import be.kuleuven.cs.swop.facade.UserWrapper;


public class TestingUI implements UserInterface {

    private SessionController           sessionController;
    private List<UserWrapper>           selectUser = new ArrayList<>();
    private List<LocalDateTime>         requestTime = new ArrayList<>();
    private List<ProjectData>           requestProjectData = new ArrayList<>();
    private List<ProjectWrapper>        requestProject = new ArrayList<>();
    private List<TaskData>              requestTaskData = new ArrayList<>();
    private List<TaskWrapper>           requestTask = new ArrayList<>();
    private List<LocalDateTime>         requestSelectTime = new ArrayList<>();
    private List<Set<Resource>>         requestResourcesSet = new ArrayList<>();
    private List<SimulationStepData>    requestSimStepData = new ArrayList<>();
    private List<Boolean>               shouldAddBreak = new ArrayList<>();
    private List<BranchOfficeWrapper>   requestOffice = new ArrayList<>();
    private List<String>                requestFileName = new ArrayList<>();
    private Map<Object, Boolean>        replaceOnAdd = new HashMap<>();
    private List<Boolean>               requestExecute = new ArrayList<>();
    private List<Boolean>               requestFinish = new ArrayList<>();
    private List<Set<Resource>>         requestNewResources = new ArrayList<>();
    
    public TestingUI(){
    	super();
    }
    
    
    /**
     * Return the items in the list in the order they were added, keep returning the last added item if the list gets exhausted
     * and track if this has occurred
     * @param list
     * @return
     */
    private <T> T getNext(List<T> list){
    	if(list.isEmpty()){
    		return null;
    	}
    	
    	T result = list.get(0);
    	
    	if(list.size() > 1){
    		list.remove(0);
    	}else{
    		replaceOnAdd.put(list, true);
    	}
    	
    	return result;
    }
    
    /**
     * Legacy support for older TestingUI. If the list was exhausted before adding a new item, replace the previous item.
     * This mimics the behavior of doing a single set, get and setting again in the old TestingUI
     * @param list
     * @param input
     */
    private <T> void addNext(List<T> list, T input){
    	if(replaceOnAdd.containsKey(list) && replaceOnAdd.get(list)){
    		list.remove(0);
    	}
    	
    	list.add(input);
    	replaceOnAdd.put(list, false);
    }
    
    public void addSelectUser(UserWrapper user) {
        addNext(selectUser, user);
    }
    
    @Override
    public UserWrapper selectUser(Set<UserWrapper> users) {
        return getNext(selectUser);
    }

    @Override
    public void showProjects(Set<ProjectWrapper> projects) {}

    @Override
    public void showProject(ProjectWrapper project) {}

    @Override
    public void showTasks(Set<TaskWrapper> tasks) {}

    @Override
    public void showTask(TaskWrapper task) {}
    
	@Override
	public void showTaskPlanningContext(TaskWrapper task) {}

    /**
     * Set the request project.
     * 
     * @param proj
     *            The project that should be returned when calling selectProject
     */
    public void addRequestProject(ProjectWrapper proj) {
    	addNext(requestProject, proj);
    }

    /**
     * Ignores the set and just returns the requestProject.
     */
    @Override
    public ProjectWrapper selectProject(Set<ProjectWrapper> projects) {
        return getNext(requestProject);
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

    public void addRequestTask(TaskWrapper task) {
    	addNext(requestTask, task);
    }

    /**
     * Ignores the list and just returns the set request task.
     */
    @Override
    public TaskWrapper selectTaskFromProjects(Set<ProjectWrapper> projects) {
        return getNext(requestTask);
    }

    @Override
    public TaskWrapper selectTaskFromProjects(Map<ProjectWrapper, Set<TaskWrapper>> projectMap) {
        return getNext(requestTask);
    }
    
    public void addRequestTaskData(TaskData data) {
    	addNext(requestTaskData, data);
    }

    @Override
    public TaskData getTaskData(Set<ResourceType> types) {
        return getNext(requestTaskData);
    }

    public void addRequestProjectDate(ProjectData data) {
    	addNext(requestProjectData, data);
    }

    @Override
    public ProjectData getProjectData() {
        return getNext(requestProjectData);
    }

    public void addRequestTime(LocalDateTime time) {
    	addNext(requestTime, time);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        return getNext(requestTime);
    }

    @Override
    public void showError(String error) {
        //System.out.println(error);
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

    public void addSelectTime(LocalDateTime time) {
    	addNext(requestSelectTime, time);
    }

    @Override
    public LocalDateTime selectTime(List<LocalDateTime> options) {
        return getNext(requestSelectTime);
    }
    
    public void addSelectResourcesFor(Set<Resource> set) {
    	addNext(requestResourcesSet, set);
    }

    @Override
    public Set<Resource> selectResourcesFor(Map<ResourceType, List<Resource>> options, Set<Requirement> requirements) {
        return getNext(requestResourcesSet);
    }
    
    public void addSimulationStepData(SimulationStepData data) {
    	addNext(requestSimStepData, data);
    }
    
    @Override
    public SimulationStepData getSimulationStepData() {
        return getNext(requestSimStepData);
    }
    
    public void addShouldAddBreak(boolean addBreak) {
        addNext(shouldAddBreak, addBreak);
    }

    @Override
    public boolean askToAddBreak() {
        return getNext(shouldAddBreak);
    }
    
    public void addOffice(BranchOfficeWrapper office) {
        addNext(requestOffice, office);
    }

    @Override
    public BranchOfficeWrapper selectOffice(Set<BranchOfficeWrapper> offices) {
        return getNext(requestOffice);
    }

    public void addFileName(String filename) {
        addNext(requestFileName, filename);
    }
    
    @Override
    public String getFileName() {
        return getNext(requestFileName);
    }

    public void addExecute(boolean ex) {
    	addNext(requestExecute, ex);
    }

	@Override
	public boolean askExecute() throws ExitEvent {
		return getNext(requestExecute);
	}

	public void addFinish(boolean fin) {
		addNext(requestFinish, fin);
	}

	@Override
	public boolean askFinish() throws ExitEvent {
		return getNext(requestFinish);
	}

	public void addSelectNewResources(Set<Resource> ress) {
		addNext(requestNewResources, ress);
	}

	@Override
	public Set<Resource> askSelectnewResources(Set<Resource> resources,
			Map<ResourceType, List<Resource>> resourceOptions,
			Set<Requirement> reqs) throws ExitEvent {
		return getNext(requestNewResources);
	}


}
