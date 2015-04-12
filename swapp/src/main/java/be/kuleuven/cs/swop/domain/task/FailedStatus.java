package be.kuleuven.cs.swop.domain.task;


import java.time.LocalDateTime;

import be.kuleuven.cs.swop.domain.DateTimePeriod;


@SuppressWarnings("serial")
public class FailedStatus extends PerformedStatus {

    private Task alternative;

    FailedStatus(Task task, DateTimePeriod performedDuring) {
        super(task, performedDuring);
    }

    /**
     * Checks whether the project containing this status is finished.
     *
     * @return Returns false.
     */
    @Override
    boolean isFinished() {
        return false;
    }

    /**
     * Checks whether the project containing this status is failed.
     *
     * @return Returns true.
     */
    @Override
    boolean isFailed() {
        return true;
    }

    @Override
    void setAlternative(Task alternative) {
        if (!canHaveAsAlternative(alternative)) { throw new IllegalArgumentException(ERROR_ILLEGAL_ALTERNATIVE); }
        this.alternative = alternative;
    }

    /**
     * Retrieves the alternative Task for this Task for when this Task has failed.
     *
     * @return The alternative Task.
     */
    @Override
    Task getAlternative() {
        return alternative;
    }

    /**
     * Checks whether of not the given Task is a valid alternative for this Task, this Task can't have an alternative Task when the new alternative is null, when this Task already has a alternative
     * and when the new alternative Task doesn't create a dependency loop when the new alternative is replaced by this Task, this Task has to be failed before you can set an alternative.
     *
     * @param alternative
     *            The Task to be checked as possible alternative for this Task.
     * @return Returns true if the given Task can be an alternative for this Task.
     */
    protected boolean canHaveAsAlternative(Task alternative) {
        if (alternative == null) { return false; }
        if (this.alternative != null) { return false; }
        if (alternative.containsDependency(getTask())) { return false; }
        if (getTask() == alternative) { return false; }
        return true;
    }

    @Override
    boolean isFinishedOrHasFinishedAlternative() {
        if (alternative == null) return false;
        return alternative.isFinishedOrHasFinishedAlternative();
    }

    @Override
    LocalDateTime getEstimatedOrRealFinishDate(LocalDateTime currentDate) {
        if (getAlternative() == null) {
            // Makes no sense but the assignment said so...
            return getPerformedDuring().getStopTime();
        }
        else {
            return getAlternative().getEstimatedOrRealFinishDate(currentDate);
        }
    }

    @Override
    boolean wasFinishedOnTime() {
        return false;
    }

    @Override
    boolean wasFinishedEarly() {
        return false;
    }

    @Override
    boolean wasFinishedLate() {
        return false;
    }

    private static final String ERROR_ILLEGAL_ALTERNATIVE = "Illegal alternative for failed status.";
}
