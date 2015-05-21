package be.kuleuven.cs.swop.domain.company.planning;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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
    private final LocalDateTime   plannedEndTime;

    @SuppressWarnings("unused")
    protected TaskPlanning() {reservations = null;plannedStartTime = null;plannedEndTime = null;} //for automatic (de)-serialization
    public TaskPlanning(LocalDateTime plannedStartTime, Set<Resource> reservations, long duration) {
        if (!canHaveAsReservations(reservations)) { throw new IllegalArgumentException(ERROR_INVALID_RESERVATIONS); }
        this.reservations = reservations;
        if (!canHaveAsPlannedStartTime(plannedStartTime)) { throw new IllegalArgumentException(ERROR_INVALID_STARTIME); }
        this.plannedStartTime = plannedStartTime;
        if (!canHaveAsTaskDuration(duration)) { throw new IllegalArgumentException(ERROR_INVALID_DURATION); }
        this.plannedEndTime = plannedStartTime.plusMinutes(duration);

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
    
    public LocalDateTime getPlannedEndTime() {
        return plannedEndTime;
    }

    protected boolean canHaveAsPlannedStartTime(LocalDateTime plannedStartTime) {
        return plannedStartTime != null;
    }
    
    protected boolean canHaveAsPlannedEndTime(LocalDateTime plannedEndTime) {
        if(plannedStartTime == null) return false;
        if(plannedEndTime.isBefore(plannedStartTime)) return false;
        return true;
    }
    
    protected boolean canHaveAsTaskDuration(long taskDuration){
        return taskDuration > 0;
    }
    
    public long getTaskDuration(){
        return plannedStartTime.until(plannedEndTime, ChronoUnit.MINUTES);
    }

    /* TODO MOVE TO REQUIREMENTS CLASS
    private boolean satisfiedRequirements(Set<Resource> resources) {
        for (Requirement req : task.getRecursiveRequirements()) {
            if (!req.isSatisfiedWith(resources)) { return false; }
        }
        return true;
    }*/
    public DateTimePeriod getEstimatedPeriod(){
        return new DateTimePeriod(plannedStartTime, plannedEndTime);
    }
    public boolean includesBreak() {
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((plannedStartTime == null) ? 0 : plannedStartTime.hashCode());
        result = prime * result + ((reservations == null) ? 0 : reservations.hashCode());
        result = prime * result + ((plannedEndTime == null) ? 0 : plannedEndTime.hashCode());
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
        if (plannedEndTime == null) {
            if (other.plannedEndTime != null) return false;
        } else if (!plannedEndTime.equals(other.plannedEndTime)) return false;
        if (reservations == null) {
            if (other.reservations != null) return false;
        } else if (!reservations.equals(other.reservations)) return false;
        return true;
    }

    private static final String ERROR_INVALID_RESERVATIONS = "Invalid reservations for planning.";
    private static final String ERROR_INVALID_STARTIME     = "Invalid start time for this planning.";
    private static final String ERROR_INVALID_DURATION    = "Invalid duration for this planning.";}
