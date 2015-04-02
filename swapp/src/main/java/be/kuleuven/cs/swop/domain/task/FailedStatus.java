package be.kuleuven.cs.swop.domain.task;


import be.kuleuven.cs.swop.domain.TimePeriod;


public class FailedStatus extends TaskStatus {

    public FailedStatus(Task task) {
        super(task);
    }

    /**
     * Checks whether the project containing this status is finished.
     *
     * @return Returns false.
     */
    @Override
    public boolean isFinished() {
        return false;
    }

    /**
     * Checks whether the project containing this status is failed.
     *
     * @return Returns true.
     */
    @Override
    public boolean isFailed() {
        return true;
    }

    /**
     * Checks whether this status is final and therefore can't be changed.
     *
     * @return Returns true.
     */
    @Override
    public boolean isFinal() {
        return true;
    }

    /**
     * Checks whether the project containing this status can finish.
     *
     * @return Returns false.
     */
    @Override
    public boolean canFinish() {
        return false;
    }

    /**
     * Checks whether the project containing this status can fail.
     *
     * @return Returns false.
     */
    @Override
    public boolean canFail() {
        return false;
    }

    @Override
    public void setAlternative(Task alternative) {
        getTask().setAlternative(alternative);
    }

    @Override
    void fail(TimePeriod period) {
        throw new IllegalStateException(ERROR_FAIL);
    }

    @Override
    void finish(TimePeriod period) {
        throw new IllegalStateException(ERROR_FINISH);
    }

    private static String ERROR_FINISH = "Can't finish a failed task.";
    private static String ERROR_FAIL   = "Can't fail a failed task.";
    @Override
    protected boolean canHaveAsAlternative(Task task) {
        return task != null;
    }

}
