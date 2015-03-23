package be.kuleuven.cs.swop.domain.resource;


import java.time.LocalTime;


public class TimeConstrainedResourceType extends ResourceType {

    private LocalTime startTimeConstraint;
    private LocalTime endTimeConstraint;

    // TODO constructor

    public LocalTime getStartTimeConstraint() {
        return startTimeConstraint;
    }

    protected boolean canHaveAsStartTimeConstraint(LocalTime startTimeConstraint) {
        return startTimeConstraint != null;
    }

    private void setStartTimeConstraint(LocalTime startTimeConstraint) {
        if (!canHaveAsEndTimeConstraint(startTimeConstraint)) { throw new IllegalArgumentException(ERROR_ILLEGAL_START_TIME_CONSTRAINT); }
        this.startTimeConstraint = startTimeConstraint;
    }

    public LocalTime getEndTimeConstraint() {
        return endTimeConstraint;
    }

    protected boolean canHaveAsEndTimeConstraint(LocalTime endTimeConstraint) {
        return endTimeConstraint != null && startTimeConstraint != null && startTimeConstraint.isBefore(endTimeConstraint);
    }

    private void setEndTimeConstraint(LocalTime endTimeConstraint) {
        if (!canHaveAsEndTimeConstraint(endTimeConstraint)) { throw new IllegalArgumentException(ERROR_ILLEGAL_END_TIME_CONSTRAINT); }
        this.endTimeConstraint = endTimeConstraint;
    }

    private static final String ERROR_ILLEGAL_START_TIME_CONSTRAINT = "Illegal start time constraint for time-constrained resource type";
    private static final String ERROR_ILLEGAL_END_TIME_CONSTRAINT   = "Illegal start time constraint for time-constrained resource type";
}