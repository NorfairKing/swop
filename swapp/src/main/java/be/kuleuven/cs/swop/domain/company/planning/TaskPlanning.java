package be.kuleuven.cs.swop.domain.company.planning;


import java.io.Serializable;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;


@SuppressWarnings("serial")
public class TaskPlanning implements Serializable {

    private Set<Developer> developers   = new HashSet<Developer>();
    private Task           task;
    private LocalDateTime  plannedStartTime;
    private Set<Resource>  resources = new HashSet<Resource>();
    
    public TaskPlanning(Set<Developer> developers, Task task, LocalDateTime plannedStartTime){
        this(developers,task,plannedStartTime,null);
    }

    public TaskPlanning(Set<Developer> developers, Task task, LocalDateTime plannedStartTime, Set<Resource> resources) {
        setDevelopers(developers);
        setTask(task); //task has to be set before the reservations
        setPlannedStartTime(plannedStartTime);
        setResources(resources);
    }

    public ImmutableSet<Developer> getDevelopers() {
        return ImmutableSet.copyOf(developers);
    }

    private void setDevelopers(Set<Developer> developers) {
        if(developers == null){
            developers = new HashSet();
        }
        developers.forEach(d -> addDeveloper(d));
    }

    protected boolean canHaveAsDeveloper(Developer developer) {
        return developer != null;
    }
    
    private void addDeveloper(Developer developer){
        if (!canHaveAsDeveloper(developer)) {
            throw new InvalidParameterException(ERROR_INVALID_DEVELOPER);
        }
        this.developers.add(developer);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        if (!canHaveAsTask(task)) {
            throw new IllegalArgumentException(ERROR_INVALID_TASK);
        }
        this.task = task;
    }

    protected boolean canHaveAsTask(Task task) {
        return task != null;
    }

    public LocalDateTime getPlannedStartTime() {
        return plannedStartTime;
    }

    public void setPlannedStartTime(LocalDateTime plannedStartTime) {
        if (!canHaveAsPlannedStartTime(plannedStartTime)) {
            throw new IllegalArgumentException(ERROR_INVALID_STARTIME);
        }
        this.plannedStartTime = plannedStartTime;
    }

    protected boolean canHaveAsPlannedStartTime(LocalDateTime plannedStartTime) {
        return plannedStartTime != null;
    }

    public ImmutableSet<Resource> getReservations() {
        return ImmutableSet.copyOf(resources);
    }
    
    private boolean satisfiedRequirements(Set<Resource> resources){
        for(Requirement req : task.getRecursiveRequirements()){
            if(!req.isSatisfiedWith(resources)){
                return false;
            }
        }
        return true;
    }

    private void setResources(Set<Resource> resources) {
        if (resources == null){
            resources = new HashSet();
        }
        if (!satisfiedRequirements(resources)){
            throw new IllegalArgumentException(ERROR_INVALID_RESERVATIONS);
        }
        this.resources.clear();
        resources.forEach(r -> addResource(r));
    }
    
    protected boolean canHaveAsResource(Resource resource){
        return resource != null;
    }
    
    private void addResource(Resource resource){
        if(!canHaveAsResource(resource)){
            throw new IllegalArgumentException(ERROR_INVALID_RESOURCE);
        }
        this.resources.add(resource);
    }


    public DateTimePeriod getEstimatedOrRealPeriod() {
        if (getTask().isFailed() || getTask().isFinished()) {
            return getTask().getPerformedDuring();
        } else {
            long taskDur = getTask().getEstimatedDuration();
            LocalDateTime estimatedEndTime = getPlannedStartTime().plusMinutes(taskDur);
            return new DateTimePeriod(getPlannedStartTime(), estimatedEndTime);
        }
    }

    private static final String ERROR_INVALID_DEVELOPER = "Invalid developer for planning.";
    private static final String ERROR_INVALID_RESERVATIONS = "Invalid resource set for planning.";
    private static final String ERROR_INVALID_RESOURCE = "Invalid resource  for planning.";
    private static final String ERROR_INVALID_STARTIME = "Invalid startime for this planning.";
    private static final String ERROR_INVALID_TASK = "Invalid task for this planning.";
}
