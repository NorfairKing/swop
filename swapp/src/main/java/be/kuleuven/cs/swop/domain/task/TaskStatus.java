package be.kuleuven.cs.swop.domain.task;


import java.io.Serializable;

import be.kuleuven.cs.swop.domain.TimePeriod;


public abstract class TaskStatus implements Serializable {

    private Task task;

    public TaskStatus(Task task) {
        setTask(task);
    }

    /**
     * Checks whether the project containing this status is finished.
     *
     * @return Returns a true if Task containing this status is finished.
     */
    public abstract boolean isFinished();

    /**
     * Checks whether the project containing this status is failed.
     *
     * @return Returns a true if Task containing this status is failed.
     */
    public abstract boolean isFailed();

    /**
     * Checks whether the project containing this status can finish.
     *
     * @return Returns a true if Task containing this status can finish.
     */
    public abstract boolean canFinish();

    /**
     * Checks whether the project containing this status can fail.
     *
     * @return Returns a true if Task containing this status can fail.
     */
    public abstract boolean canFail();

    /**
     * Checks whether this status is final and therefore can't be changed.
     *
     * @return Returns a true if this status is final.
     */
    public abstract boolean isFinal();

    public abstract void setAlternative(Task alternative);

    public Task getTask() {
        return task;
    }

    protected boolean canHaveAsTask(Task task) {
        return task != null;
    }

    public void setTask(Task task) {
        if (!canHaveAsTask(task)) { throw new IllegalArgumentException(ERROR_ILLEGAL_TASK); }
        this.task = task;
    }

    abstract void finish(TimePeriod period);

    abstract void fail(TimePeriod period);

    protected void goToStatus(TaskStatus status) {
        this.task.setStatus(status);
    }

    protected abstract boolean canHaveAsAlternative(Task task);

    private static final String ERROR_ILLEGAL_TASK = "Illegal task for status";
}
