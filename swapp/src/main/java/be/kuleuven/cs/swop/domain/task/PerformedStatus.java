package be.kuleuven.cs.swop.domain.task;


import java.time.LocalDateTime;

import be.kuleuven.cs.swop.domain.TimeCalculator;
import be.kuleuven.cs.swop.domain.TimePeriod;


public abstract class PerformedStatus extends TaskStatus {

    private TimePeriod performedDuring;

    PerformedStatus(Task task, TimePeriod performedDuring) {
        super(task);
        this.performedDuring(performedDuring);
    }

    /**
     * Checks whether the project containing this status can finish.
     *
     * @return Returns false.
     */
    @Override
    boolean canFinish() {
        return false;
    }

    /**
     * Checks whether the project containing this status can fail.
     *
     * @return Returns false.
     */
    @Override
    boolean canFail() {
        return false;
    }

    /**
     * Checks whether this status is final and therefore can't be changed.
     *
     * @return Returns true.
     */
    @Override
    boolean isFinal() {
        return true;
    }

    @Override
    void fail(TimePeriod period) {
        throw new IllegalStateException(ERROR_FAIL);
    }

    @Override
    void finish(TimePeriod period) {
        throw new IllegalStateException(ERROR_FINISH);
    }

    /**
     * Retrieves the TimePeriod for when this Task was performed.
     *
     * @return Returns a TimePeriod for when the Task was performed or null if the Task isn't failed of finished yet.
     */
    @Override
    TimePeriod getPerformedDuring() {
        return performedDuring;
    }

    abstract LocalDateTime getEstimatedOrRealFinishDate(java.time.LocalDateTime currentDate);

    /**
     * Checks whether or not this Task can be performed during the given timespan.
     *
     * @param timespan
     *            The TimePeriod that has to be checked.
     * @return Returns true if this Task could be performed during the given TimePeriod.
     */
    protected boolean canHaveBeenPerfomedDuring(TimePeriod timespan) {
        return timespan != null;
    }

    /**
     * Sets the timespan for this Task for when it was performed.
     *
     * @param timespan
     *            The TimePeriod for when during the Task was performed.
     */
    void performedDuring(TimePeriod timespan) {
        if (!canHaveBeenPerfomedDuring(timespan)) { throw new IllegalArgumentException(ERROR_ILLEGAL_PERFORMED_DURING); }
        this.performedDuring = timespan;
    }

    double getRealDuration() {
        return (double) TimeCalculator.getDurationMinutes(
                getPerformedDuring().getStartTime(),
                getPerformedDuring().getStopTime());
    }

    private static String ERROR_ILLEGAL_PERFORMED_DURING = "Illegal timepriod performed during for performed status.";
    private static String ERROR_FINISH                   = "Can't finish a performed task.";
    private static String ERROR_FAIL                     = "Can't fail a performed task.";
}
