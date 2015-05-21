package be.kuleuven.cs.swop.domain.company.planning;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.user.Developer;


/**
 * Set<Reservable> reservations A class that represents the reservation of a task. It also includes the reservation of all needed resources and the developers assigned to do it. It ofcourse also
 * includes a planned starting time.
 *
 */
@SuppressWarnings("serial")
public class TaskPlanning implements Serializable {

    private final Set<Resource> reservations;
    private final LocalDateTime   plannedStartTime;
    private final long taskDuration;

    @SuppressWarnings("unused")
    protected TaskPlanning() {reservations = null;plannedStartTime = null; taskDuration = 0;} //for automatic (de)-serialization
    public TaskPlanning(LocalDateTime plannedStartTime, Set<Resource> reservations, long taskDuration) {
        if (!canHaveAsReservations(reservations)) { throw new IllegalArgumentException(ERROR_INVALID_RESERVATIONS); }
        this.reservations = reservations;
        if (!canHaveAsPlannedStartTime(plannedStartTime)) { throw new IllegalArgumentException(ERROR_INVALID_STARTIME); }
        this.plannedStartTime = plannedStartTime;
        if(!canHaveAsTaskDuration(taskDuration)){
            throw new IllegalArgumentException(ERROR_INVALID_DURATION);
        }
        this.taskDuration = taskDuration;
    }

    protected boolean canHaveAsReservations(Set<Resource> reservations) {
        for (Resource r : reservations) {
            if (r == null) { return false; }
        }
        return true;
    }

    public ImmutableSet<Resource> getReservations() {
        return ImmutableSet.copyOf(reservations);
    }
    

    public Set<Developer> getDevelopers() {
        Set<Developer> devs = new HashSet<Developer>();
        for(Resource res : reservations){
        	if(res.getType() == Developer.DEVELOPER_TYPE){
        		devs.add((Developer) res);
        	}
        }
        return devs;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((plannedStartTime == null) ? 0 : plannedStartTime.hashCode());
        result = prime * result + ((reservations == null) ? 0 : reservations.hashCode());
        result = prime * result + (int) (taskDuration ^ (taskDuration >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TaskPlanning other = (TaskPlanning) obj;
        if (plannedStartTime == null) {
            if (other.plannedStartTime != null) return false;
        } else if (!plannedStartTime.equals(other.plannedStartTime)) return false;
        if (reservations == null) {
            if (other.reservations != null) return false;
        } else if (!reservations.equals(other.reservations)) return false;
        if (taskDuration != other.taskDuration) return false;
        return true;
    }

    private static final String ERROR_INVALID_RESERVATIONS = "Invalid reservations for planning.";
    private static final String ERROR_INVALID_STARTIME     = "Invalid startime for this planning.";
    private static final String ERROR_INVALID_DURATION    = "Invalid duration for this planning.";
}
