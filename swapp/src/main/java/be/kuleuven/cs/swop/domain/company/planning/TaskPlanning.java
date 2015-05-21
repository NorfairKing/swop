package be.kuleuven.cs.swop.domain.company.planning;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private final DateTimePeriod period;
    private final boolean withBreak;

    protected TaskPlanning() {reservations = null;period = null;withBreak = false;} //for automatic (de)-serialization
    public TaskPlanning(DateTimePeriod period, Set<Resource> reservations, boolean withBreak) {
        if (!canHaveAsReservations(reservations)) { throw new IllegalArgumentException(ERROR_INVALID_RESERVATIONS); }
        this.reservations = reservations;
        if (!canHaveAsPeriod(period)) { throw new IllegalArgumentException(ERROR_INVALID_STARTIME); }
        this.withBreak = withBreak;
        if(withBreak){
            this.period = new DateTimePeriod(period.getStartTime(), period.getStopTime().plusMinutes(Developer.BREAK_TIME));
        }else{
            this.period = period;
        }
    }
    public TaskPlanning(DateTimePeriod period, Set<Resource> reservations) {
        this(period,reservations,false);
    }

    protected boolean canHaveAsReservations(Set<Resource> reservations) {
        for (Resource r : reservations) {
            if (r == null) { return false; }
        }
        return true;
    }

    public Set<Resource> getReservations() {
        Set<Resource> ress = new HashSet<Resource>();
        for(Resource res : reservations){
            ress.add(res);
        }
        return ress;
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
        return period.getStartTime();
    }
    
    public LocalDateTime getPlannedEndTime() {
        return period.getStopTime();
    }

    protected boolean canHaveAsPeriod(DateTimePeriod period) {
        return period != null;
    }
    
    
    public long getTaskDuration(){
        return getPlannedStartTime().until(getPlannedEndTime(), ChronoUnit.MINUTES);
    }

    /* TODO MOVE TO REQUIREMENTS CLASS
    private boolean satisfiedRequirements(Set<Resource> resources) {
        for (Requirement req : task.getRecursiveRequirements()) {
            if (!req.isSatisfiedWith(resources)) { return false; }
        }
        return true;
    }*/
    public DateTimePeriod getEstimatedPeriod(){
        return period;
    }
    public boolean includesBreak() {
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((period == null) ? 0 : period.hashCode());
        result = prime * result + ((reservations == null) ? 0 : reservations.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TaskPlanning other = (TaskPlanning) obj;
        if (period == null) {
            if (other.period != null) return false;
        } else if (!period.equals(other.period)) return false;
        if (reservations == null) {
            if (other.reservations != null) return false;
        } else if (!reservations.equals(other.reservations)) return false;
        return true;
    }

    private static final String ERROR_INVALID_RESERVATIONS = "Invalid reservations for planning.";
    private static final String ERROR_INVALID_STARTIME     = "Invalid start time for this planning.";
    private static final String ERROR_INVALID_ENDTIME     = "Invalid end time for this planning.";
}
