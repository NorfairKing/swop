package be.kuleuven.cs.swop.domain.planning;


import java.io.Serializable;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.resource.Resource;
import be.kuleuven.cs.swop.domain.task.Task;
import be.kuleuven.cs.swop.domain.user.Developer;


@SuppressWarnings("serial")
public class TaskPlanning implements Serializable {

    private Set<Developer> developers   = new HashSet<Developer>();
    private Task           task;
    private LocalDateTime  plannedStartTime;
    private Set<Resource>  reservations = new HashSet<Resource>();

    public TaskPlanning(Set<Developer> developers, Task task, LocalDateTime plannedStartTime, Set<Resource> reservations) {
        setDevelopers(developers);
        setTask(task);
        setPlannedStartTime(plannedStartTime);
        setReservations(reservations);
    }

    public ImmutableSet<Developer> getDevelopers() {
        return ImmutableSet.copyOf(developers);
    }

    private void setDevelopers(Set<Developer> developers) {
        if (!canHaveAsDevelopers(developers)) {
            throw new InvalidParameterException("Invalid developers set for planning.");
        }
        this.developers.clear();
        this.developers.addAll(developers);
    }

    protected boolean canHaveAsDevelopers(Set<Developer> developers) {
        return !developers.isEmpty();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        if (!canHaveAsTask(task)) {
            throw new IllegalArgumentException("Invalid task for this planning.");
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
            throw new IllegalArgumentException("Invalid startime for this planning.");
        }
        this.plannedStartTime = plannedStartTime;
    }

    protected boolean canHaveAsPlannedStartTime(LocalDateTime plannedStartTime) {
        return plannedStartTime != null;
    }

    public ImmutableSet<Resource> getReservations() {
        return ImmutableSet.copyOf(reservations);
    }

    private void setReservations(Set<Resource> reservations) {
        if (!canHaveAsReservations(reservations)) {
            throw new InvalidParameterException("Invalid reservations set for planning.");
        }
        this.reservations.clear();
        this.reservations.addAll(reservations);
    }

    protected boolean canHaveAsReservations(Set<Resource> reservations) {
        return reservations != null;
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

}
