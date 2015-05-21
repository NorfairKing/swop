package be.kuleuven.cs.swop.domain.company.task;


import java.time.LocalDateTime;

import be.kuleuven.cs.swop.domain.TimeCalculator;
import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.delegation.Delegation;


@SuppressWarnings("serial")
public abstract class IncompleteStatus extends TaskStatus {

    protected IncompleteStatus() {super();} //for automatic (de)-serialization
    IncompleteStatus(Task task) {
        super(task);
    }

    @Override
    LocalDateTime getEstimatedOrRealFinishDate(java.time.LocalDateTime currentDate) {
        LocalDateTime lastOfDependencies = getTask().getLatestEstimatedOrRealFinishDateOfDependencies(currentDate);
        LocalDateTime now = currentDate;
        if (lastOfDependencies.isBefore(now)) {
            lastOfDependencies = now;
        }

        return TimeCalculator.addWorkingMinutes(lastOfDependencies, (long) getTask().getEstimatedDuration());
    }

    @Override
    boolean wasFinishedEarly() {
        return false;
    }

    @Override
    boolean wasFinishedOnTime() {
        return false;
    }

    @Override
    boolean wasFinishedLate() {
        return false;
    }

    @Override
    void fail() {
        goToStatus(new FailedStatus(getTask()));
    }

    @Override
    boolean isFinishedOrHasFinishedAlternative() {
        return false;
    }

    @Override
    void setAlternative(Task alternative) {
        throw new IllegalStateException(ERROR_SET_ALTERNATIVE_ERROR);
    }
    
    @Override
    void delegate(Delegation del){
    	goToStatus(new DelegatedStatus(getTask(), del));
    }

    private static final String ERROR_SET_ALTERNATIVE_ERROR = "Can't set an alternative for an ongoing task.";

}
