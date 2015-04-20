package be.kuleuven.cs.swop.domain.company;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;

import com.google.common.collect.ImmutableSet;


public class Company {

    private ProjectManager  projectManager;
    private PlanningManager planningManager;

    public Company() {
        setProjectManager(new ProjectManager());
        setPlanningManager(new PlanningManager());

    }

    // Getters and setters of internal state
    private ProjectManager getProjectManager() {
        return projectManager;
    }

    private void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    private PlanningManager getPlanningManager() {
        return planningManager;
    }

    private void setPlanningManager(PlanningManager planningManager) {
        this.planningManager = planningManager;
    }

    public ImmutableSet<Developer> getDevelopers() {
        return getPlanningManager().getDevelopers();
    }

    public ImmutableSet<Project> getProjects() {
        return getProjectManager().getProjects();
    }
    
    public ImmutableSet<ResourceType> getResourceTypes() {
        return getPlanningManager().getResourceTypes();
    }
    
    public Project getProjectFor(Task task){
        return getProjectManager().getProjectFor(task);
    }

    public Set<Task> getUnplannedTasksOf(Project project) {
        Set<Task> all = project.getTasks();

        Set<Task> unplannedTasks = new HashSet<Task>();
        for (Task t : all) {
            if (planningManager.isUnplanned(t)) {
                unplannedTasks.add(t);
            }
        }
        return unplannedTasks;
    }

    public TaskPlanning getPlanningFor(Task task) {
        return getPlanningManager().getPlanningFor(task);
    }

    public List<LocalDateTime> getPlanningTimeOptions(Task task, int amount, LocalDateTime time) {
        // FIXME Infinite loop it seems.
        return getPlanningManager().getPlanningTimeOptions(task, amount, time);
    }

    public Map<ResourceType, List<Resource>> getPlanningResourceOptions(Task task, LocalDateTime time) {
        return getPlanningManager().getPlanningResourceOptions(task, time);
    }

    public Set<Developer> getPlanningDeveloperOptions(Task task, LocalDateTime time) {
        return getPlanningManager().getPlanningDeveloperOptions(task, time);
    }

    public void createPlanning(Task task, LocalDateTime time, Set<Resource> rss, Set<Developer> devs) {
        getPlanningManager().createPlanning(task, time, rss, devs);
    }

    public Project createProject(String title, String description, LocalDateTime creationTime, LocalDateTime dueTime) {
        return getProjectManager().createProject(title, description, creationTime, dueTime);
    }

    public Task createTaskFor(Project project, String description, long estimatedDuration, double acceptableDeviation, Set<Task> dependencies, Set<Requirement> requirements) {
        Task t = project.createTask(description, estimatedDuration, acceptableDeviation);
        dependencies.forEach(d -> t.addDependency(d));
        return t;
    }
    
    public void setAlternativeFor(Task t, Task alt){
        t.addAlternative(alt);
    }
    
    public void addDependencyTo(Task t,Task dep){
        t.addDependency(dep);
    }
    
    public void finishTask(Task t, DateTimePeriod period){
        t.finish(period);
    }
    
    public void failTask(Task t,DateTimePeriod period){
        t.fail(period);
    }
    
    public void startExecutingTask(Task t){
        t.execute();
    }
    
    public ResourceType createResourceType(String name, Set<ResourceType> requires, Set<ResourceType> conflicts, boolean selfConflicting, TimePeriod availability){
        return getPlanningManager().createResourceType(name, requires, conflicts, selfConflicting, availability);
    }
    
    public Resource createResource(String name, ResourceType type){
        return getPlanningManager().createResource(name, type);
    }

    public Developer createDeveloper(String name) {
        return getPlanningManager().createDeveloper(name);
    }


}
