package be.kuleuven.cs.swop.domain;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.resource.Requirement;
import be.kuleuven.cs.swop.domain.resource.Resource;
import be.kuleuven.cs.swop.domain.resource.ResourceType;
import be.kuleuven.cs.swop.domain.task.Task;
import be.kuleuven.cs.swop.domain.user.Developer;
import be.kuleuven.cs.swop.domain.user.User;
import be.kuleuven.cs.swop.domain.TimeCalculator;

public class PlanningManager implements Serializable {

    private Set<TaskPlanning> plannings = new HashSet<TaskPlanning>();
    private Set<Resource> resources = new HashSet<Resource>();
    private Set<ResourceType> resourceTypes = new HashSet<ResourceType>();
    private Set<Developer> developers = new HashSet<Developer>();
    private User activeUser;

    public ImmutableSet<TaskPlanning> getTaskPlannings() {
        return ImmutableSet.copyOf(plannings);
    }

    protected boolean canHaveAsTaskPlanning(TaskPlanning planning){
        return planning != null;
    }
    public void addPlanning(TaskPlanning planning) {
        if (!canHaveAsTaskPlanning(planning)) throw new IllegalArgumentException(ERROR_ILLEGAL_TASK_PLANNING);
        plannings.add(planning);
    }

    public boolean isPlanned(Task task) {
        return this.getPlanningFor(task) != null;
    }

    public boolean isUnplanned(Task task) {
        return !this.isPlanned(task);
    }

    public TaskPlanning getPlanningFor(Task task) {
        for (TaskPlanning planning : this.plannings)
        {
            if (task == planning.getTask())
                return planning;
        }
        return null;
    }

    public List<LocalDateTime> getPlanningTimeOptions(Task task, int n, LocalDateTime currentTime) {
        currentTime = currentTime.plusMinutes(60-currentTime.getMinute());
        List<LocalDateTime> timeOptions = new ArrayList<LocalDateTime>();
        if (this.resources.isEmpty()) //safety checks
            return timeOptions;
        if (task.getRequirements().stream().anyMatch(p -> !hasResourcesOfType(p.getType(),this.resources, p.getAmount())))
            return timeOptions;
        for (int i = 0; timeOptions.size() < n && i < 2000; i++) { //2000 iterations for safety, is this dirty?
            if (this.isValidTimeForTask(currentTime,task))
                timeOptions.add(currentTime);
            currentTime = TimeCalculator.addWorkingMinutes(currentTime,60);
        }
        return timeOptions;
    }

    private boolean isValidTimeForTask(LocalDateTime time, Task task) {
        Set<Resource> usedResources = new HashSet<Resource>();
        Set<Developer> usedDevelopers = new HashSet<Developer>();
        for (TaskPlanning planning : this.plannings) {
            if (planning.getEstimatedOrRealPeriod().isDuring(time)) {
                usedResources.addAll(planning.getReservations());
                usedDevelopers.addAll(planning.getDevelopers());
            }
        }
        Set<Resource> availableResources = new HashSet<Resource>(this.resources);
        availableResources.removeAll(usedResources);
        Set<Developer> availableDevelopers = new HashSet<Developer>(this.developers);
        availableDevelopers.removeAll(usedDevelopers);
        for (Requirement req : task.getRequirements()) {
            if (!hasResourcesOfType(req.getType(), availableResources, req.getAmount()))
                return false;
        }
        if(availableDevelopers.isEmpty())
            return false;
        return true;
    }

    private boolean hasResourcesOfType(ResourceType type, Set<Resource> resources, int number) {
        int counter = 0;
        for (Resource resource : resources) {
            if (resource.getType() == type) {
                counter++;
            }
            if (counter >= number)
                return true;
        }
        return false;
    }

    public Map<ResourceType, List<Resource>> getPlanningResourceOptions(Task task, LocalDateTime time) {
        Set<Resource> usedResources = new HashSet<Resource>();
        for (TaskPlanning planning : this.plannings) {
            if (planning.getEstimatedOrRealPeriod().isDuring(time)) {
                usedResources.addAll(planning.getReservations());
            }
        }
        Set<Resource> availableResources = new HashSet<Resource>(this.resources);
        availableResources.removeAll(usedResources);
        Map<ResourceType,List<Resource>> map = new HashMap<ResourceType,List<Resource>>();
        for (Requirement req : task.getRequirements()) {
            map.put(req.getType(), this.resources.stream().filter( p -> p.isOfType(req.getType())).collect(Collectors.toList()));
        }
        return map;
    }


    public Set<Developer> getPlanningDeveloperOptions(Task task, LocalDateTime time) {
        Set<Developer> usedDevelopers = new HashSet<Developer>();
        for (TaskPlanning planning : this.plannings) {
            if (planning.getEstimatedOrRealPeriod().isDuring(time)) {
                usedDevelopers.addAll(planning.getDevelopers());
            }
        }
        Set<Developer> availableDevelopers = new HashSet<Developer>(this.developers);
        availableDevelopers.removeAll(usedDevelopers);
        return availableDevelopers;
    }

    public void createPlanning(Task task, LocalDateTime estimatedStartTime, Set<Resource> resources, Set<Developer> devs) {
        TaskPlanning newplanning = new TaskPlanning(devs, task, estimatedStartTime, resources);
        this.plannings.add(newplanning);
    }
    
    public ImmutableSet<Developer> getDevelopers() {
        return ImmutableSet.copyOf(developers);
    }
    
    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User user) {
        if (!canHaveAsActiveUser(user)) {
            throw new IllegalArgumentException(ERROR_ILLEGAL_ACTIVE_USER);
        }
        this.activeUser = user;
    }
    
    public boolean canHaveAsActiveUser(User user) {
        // this seems a bit silly but makes every consistent
        // if we later decide to put restrictions on this refactoring will be easier.
        return true;
    }

    public Developer createDeveloper(String name){
        Developer dev = new Developer(name);
        developers.add(dev);
        return dev;
    }

    public Resource createResource(String name, ResourceType type){
        Resource resource = new Resource(type,name);
        resources.add(resource);
        return resource;
    }
    
    public ResourceType createResourceType(String name, Set<ResourceType> requires, Set<ResourceType> conflicts){
        ResourceType type = new ResourceType(name,requires,conflicts);
        resourceTypes.add(type);
        return type;
    }

    private static String ERROR_ILLEGAL_TASK_PLANNING = "Illegal TaskPlanning in Planning manager.";
    private static String ERROR_ILLEGAL_ACTIVE_USER = "Illegal active user in Planning manager.";
}
