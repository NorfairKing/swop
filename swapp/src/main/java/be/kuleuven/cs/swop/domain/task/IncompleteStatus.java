package be.kuleuven.cs.swop.domain.task;


import java.time.LocalDateTime;

import be.kuleuven.cs.swop.domain.TimeCalculator;
import be.kuleuven.cs.swop.domain.DateTimePeriod;


public abstract class IncompleteStatus extends TaskStatus {

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
    void fail(DateTimePeriod period) {
        goToStatus(new FailedStatus(getTask(), period));
    }

    @Override
    boolean isFinishedOrHasFinishedAlternative() {
        return false;
    }

    @Override
    DateTimePeriod getPerformedDuring() {
        return null;
    }

    @Override
    void setAlternative(Task alternative) {
        throw new IllegalStateException(ERROR_SET_ALTERNATIVE_ERROR);
    }

    private static String ERROR_SET_ALTERNATIVE_ERROR = "Can't set an alternative for an ongoing task.";

}
