package be.kuleuven.cs.swop.domain.task.status;


public abstract class TaskStatus {

    public TaskStatus() {}

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

}
