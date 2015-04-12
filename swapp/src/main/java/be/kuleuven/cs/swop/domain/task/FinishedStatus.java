package be.kuleuven.cs.swop.domain.task;


import java.time.LocalDateTime;

import be.kuleuven.cs.swop.domain.DateTimePeriod;


@SuppressWarnings("serial")
public class FinishedStatus extends PerformedStatus {

    FinishedStatus(Task task, DateTimePeriod performedDuring) {
        super(task, performedDuring);
    }

    @Override
    boolean isFinished() {
        return true;
    }

    @Override
    boolean isFailed() {
        return false;
    }

    @Override
    void setAlternative(Task alternative) {
        throw new IllegalStateException(ERROR_SET_ALTERNATIVE);
    }

    @Override
    boolean isFinishedOrHasFinishedAlternative() {
        return true;
    }

    /**
     * Checks whether or not this Task was finished within the acceptable deviation.
     *
     * @return Returns true if this Task has finished within the acceptable deviation. Otherwise it returns false, also when it hasn't finished yet.
     */
    boolean wasFinishedOnTime() {
        double realDuration = getRealDuration();
        return realDuration >= getTask().getBestDuration() && realDuration <= getTask().getWorstDuration();
    }

    /**
     * Checks whether or not this Task was finished after the estimated duration plus the acceptable deviation.
     *
     * @return Returns true if the Task has finished before after the estimated duration plus the acceptable deviation. Otherwise it returns false, also when it hasn't finished yet.
     */
    boolean wasFinishedLate() {
        return getRealDuration() > getTask().getWorstDuration();
    }

    @Override
    LocalDateTime getEstimatedOrRealFinishDate(LocalDateTime currentDate) {
        return getPerformedDuring().getStopTime();
    }

    @Override
    Task getAlternative() {
        return null;
    }

    @Override
    boolean wasFinishedEarly() {
        return getRealDuration() <= getTask().getBestDuration();
    }

    private static String ERROR_SET_ALTERNATIVE = "Can't set an alternative for a finished task.";
}
