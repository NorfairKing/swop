package be.kuleuven.cs.swop.domain.company.planning;


import java.io.Serializable;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.Reservable;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;


/**
 * Set<Reservable> reservations A class that represents the reservation of a task. It also includes the reservation of all needed resources and the developers assigned to do it. It ofcourse also
 * includes a planned starting time.
 *
 */
@SuppressWarnings("serial")
public class TaskPlanning implements Serializable {

    private final Set<Reservable> reservations;
    private final LocalDateTime   plannedStartTime;
    private final long taskDuration;

    public TaskPlanning(LocalDateTime plannedStartTime, Set<Reservable> reservations, long taskDuration) {
        if (!canHaveAsReservations(reservations)) { throw new IllegalArgumentException(ERROR_INVALID_RESERVATIONS); }
        this.reservations = reservations;
        if (!canHaveAsPlannedStartTime(plannedStartTime)) { throw new IllegalArgumentException(ERROR_INVALID_STARTIME); }
        this.plannedStartTime = plannedStartTime;
        if(!canHaveAsTaskDuration(taskDuration)){
            throw new IllegalArgumentException(ERROR_INVALID_DURATION);
        }
        this.taskDuration = taskDuration;
    }

    protected boolean canHaveAsReservations(Set<Reservable> reservations) {
        for (Reservable r : reservations) {
            if (r == null) { return false; }
        }
        return false; // TODO
    }

    public ImmutableSet<Reservable> getReservations() {
        return ImmutableSet.copyOf(reservations);
    }
    

    public ImmutableSet<Developer> getDevelopers() {
        return null;
        //FIXME: implement me!
    }

    public LocalDateTime getPlannedStartTime() {
        return plannedStartTime;
    }

    protected boolean canHaveAsPlannedStartTime(LocalDateTime plannedStartTime) {
        return plannedStartTime != null;
    }
    
    protected boolean canHaveAsTaskDuration(long taskDuration){
        return taskDuration > 0;
    }
    
    public long getTaskDuration(){
        return this.taskDuration;
    }

    /* TODO MOVE TO REQUIREMENTS CLASS
    private boolean satisfiedRequirements(Set<Resource> resources) {
        for (Requirement req : task.getRecursiveRequirements()) {
            if (!req.isSatisfiedWith(resources)) { return false; }
        }
        return true;
    }*/
    public DateTimePeriod getEstimatedPeriod(){
        LocalDateTime estimatedEndTime = plannedStartTime.plusMinutes(this.taskDuration);
        return new DateTimePeriod(plannedStartTime, estimatedEndTime);
    }
    public boolean includesBreak() {
        return false;
    }

    private static final String ERROR_INVALID_RESERVATIONS = "Invalid reservations for planning.";
    private static final String ERROR_INVALID_STARTIME     = "Invalid startime for this planning.";
    private static final String ERROR_INVALID_DURATION    = "Invalid duration for this planning.";
}
