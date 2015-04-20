package be.kuleuven.cs.swop.domain.company.task;


import be.kuleuven.cs.swop.domain.DateTimePeriod;

import java.io.Serializable;
import java.time.LocalDateTime;


@SuppressWarnings("serial")
abstract class TaskStatus implements Serializable {

    private Task task;

    TaskStatus(Task task) {
        setTask(task);
    }

    /**
     * Checks whether the project containing this status is finished.
     *
     * @return Returns a true if Task containing this status is finished.
     */
    abstract boolean isFinished();

    /**
     * Checks whether the project containing this status is failed.
     *
     * @return Returns a true if Task containing this status is failed.
     */
    abstract boolean isFailed();
    
    abstract boolean isExecuting();

    /**
     * Checks whether the project containing this status can finish.
     *
     * @return Returns a true if Task containing this status can finish.
     */
    abstract boolean canFinish();

    /**
     * Checks whether the project containing this status can fail.
     *
     * @return Returns a true if Task containing this status can fail.
     */
    abstract boolean canFail();

    /**
     * Checks whether this status is final and therefore can't be changed.
     *
     * @return Returns a true if this status is final.
     */
    abstract boolean isFinal();

    protected Task getTask() {
        return task;
    }

    boolean canHaveAsTask(Task task) {
        return task != null;
    }

    void setTask(Task task) {
        if (!canHaveAsTask(task)) { throw new IllegalArgumentException(ERROR_ILLEGAL_TASK); }
        this.task = task;
    }

    abstract void finish(DateTimePeriod period);

    abstract void fail(DateTimePeriod period);
    
    abstract void execute();

    void goToStatus(TaskStatus status) {
        this.task.setStatus(status);
    }

    abstract LocalDateTime getEstimatedOrRealFinishDate(LocalDateTime currentDate);

    /**
     * Checks if this Task is finished or if the Task has failed, if it's alternative has finished.
     *
     * @return Returns true if this task or it's alternative is finished.
     */
    abstract boolean isFinishedOrHasFinishedAlternative();

    abstract Task getAlternative();

    abstract void setAlternative(Task alternative);

    abstract DateTimePeriod getPerformedDuring();

    /**
     * Checks whether or not this Task was finished within the acceptable deviation.
     *
     * @return Returns true if this Task has finished within the acceptable deviation. Otherwise it returns false, also when it hasn't finished yet.
     */
    abstract boolean wasFinishedOnTime();

    abstract boolean wasFinishedEarly();

    abstract boolean wasFinishedLate();

    private static final String ERROR_ILLEGAL_TASK = "Illegal task for status";
}
