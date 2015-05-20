package be.kuleuven.cs.swop.domain.company.task;


import java.time.LocalDateTime;

import be.kuleuven.cs.swop.domain.TimeCalculator;
import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.delegation.Delegation;


@SuppressWarnings("serial")
public abstract class CompletedStatus extends TaskStatus {

    private final DateTimePeriod performedDuring;

    protected CompletedStatus() {super(); performedDuring = null;} //for automatic (de)-serialization
    CompletedStatus(Task task, DateTimePeriod performedDuring) {
        super(task);
        if (!canHaveBeenPerfomedDuring(performedDuring)) { throw new IllegalArgumentException(ERROR_ILLEGAL_PERFORMED_DURING); }
        this.performedDuring = performedDuring;
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
    boolean isExecuting() {
        return false;
    }

    @Override
    void fail(DateTimePeriod period) {
        throw new IllegalStateException(ERROR_FAIL);
    }

    @Override
    void finish(DateTimePeriod period) {
        throw new IllegalStateException(ERROR_FINISH);
    }

    @Override
    void execute() {
        throw new IllegalStateException(ERROR_EXECUTE);
    }

    @Override
    void delegate(Delegation del) {
        throw new IllegalStateException(ERROR_DELEGATE);
    }

    /**
     * Retrieves the TimePeriod for when this Task was performed.
     *
     * @return Returns a TimePeriod for when the Task was performed or null if the Task isn't failed of finished yet.
     */
    @Override
    DateTimePeriod getPerformedDuring() {
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
    protected boolean canHaveBeenPerfomedDuring(DateTimePeriod timespan) {
        return timespan != null;
    }

    double getRealDuration() {
        return (double) TimeCalculator.getDurationMinutes(
                getPerformedDuring().getStartTime(),
                getPerformedDuring().getStopTime());
    }

    @Override
    boolean canExecute() {
        return false;
    }

    public DateTimePeriod getEstimatedOrPlanningPeriod() {
        return this.getPerformedDuring();
    }
    
    @Override
    boolean canPlan() {
        return false;
    }
    
    private static final String ERROR_ILLEGAL_PERFORMED_DURING = "Illegal timepriod performed during for performed status.";
    private static final String ERROR_FINISH                   = "Can't finish a performed task.";
    private static final String ERROR_FAIL                     = "Can't fail a performed task.";
    private static final String ERROR_EXECUTE                  = "Can't execute a performed task.";
    private static final String ERROR_DELEGATE                 = "Can't delegate a performed task.";

}
