package be.kuleuven.cs.swop.domain;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.domain.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.resource.Requirement;
import be.kuleuven.cs.swop.domain.resource.Resource;
import be.kuleuven.cs.swop.domain.resource.ResourceType;
import be.kuleuven.cs.swop.domain.task.Task;
import be.kuleuven.cs.swop.domain.user.Developer;

import com.google.common.collect.ImmutableSet;


public class PlanningManager implements Serializable {

    private Set<TaskPlanning> plannings = new HashSet<TaskPlanning>();
    private Set<Resource> resources = new HashSet<Resource>();
    private Set<Developer> developers = new HashSet<Developer>();

    public Set<TaskPlanning> getTaskPlannings() {
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

    public List<LocalDateTime> getPlanningTimeOptions(Task task, int n) {
        LocalDateTime currentTime = Timekeeper.getTime();
        currentTime = currentTime.plusMinutes(60-currentTime.getMinute());
        List<LocalDateTime> timeOptions = new ArrayList<LocalDateTime>();
        while (timeOptions.size() < n) {
            if (this.isValidTimeForTask(currentTime,task))
                timeOptions.add(currentTime);
            currentTime = currentTime.plusHours(1);
        }
        return timeOptions;
    }

    private boolean isValidTimeForTask(LocalDateTime time, Task task) {
        Set<Resource> usedResources = new HashSet<Resource>();
        Set<Developer> usedDevelopers = new HashSet<Developer>();
        for (TaskPlanning planning : this.plannings) {
            if (planning.getPeriod().isDuring(time)) {
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

    private static String ERROR_ILLEGAL_TASK_PLANNING = "Illegal TaskPlanning in Planning manager.";

    public Map<ResourceType, List<Resource>> getPlanningResourceOptions(Task task, LocalDateTime time) {
        // TODO Auto-generated method stub
        return null;
    }


    public Set<Developer> getPlanningDeveloperOptions(Task task, LocalDateTime time) {
        // TODO Auto-generated method stub
        return null;
    }

    public void createPlanning(Task task, LocalDateTime time, Map<ResourceType, Resource> rss, Set<Developer> devs) {
        // TODO Auto-generated method stub
    }

}
