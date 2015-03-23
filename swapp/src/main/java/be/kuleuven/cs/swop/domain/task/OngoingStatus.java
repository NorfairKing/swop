package be.kuleuven.cs.swop.domain.task;


import be.kuleuven.cs.swop.domain.TimePeriod;


public class OngoingStatus extends TaskStatus {

    public OngoingStatus(Task task) {
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
     * @return Returns false.
     */
    @Override
    public boolean isFailed() {
        return false;
    }

    /**
     * Checks whether this status is final and therefore can't be changed.
     *
     * @return Returns false.
     */
    @Override
    public boolean isFinal() {
        return false;
    }

    /**
     * Checks whether the project containing this status can finish.
     *
     * @return Returns false.
     */
    @Override
    public boolean canFinish() {
        return !getTask().hasUnfinishedDependencies();
    }

    /**
     * Checks whether the project containing this status can fail.
     *
     * @return Returns true.
     */
    @Override
    public boolean canFail() {
        return true;
    }

    @Override
    public void setAlternative(Task alternative) {
        throw new IllegalStateException(ERROR_SET_ALTERNATIVE_ERROR);
    }

    @Override
    void finish(TimePeriod period) {
        if (getTask().hasUnfinishedDependencies()) {
            throw new IllegalStateException(ERROR_FINISH);
        }
        else {
            getTask().performedDuring(period);
            goToStatus(new FinishedStatus(getTask()));
        }
    }

    @Override
    void fail(TimePeriod period) {
        getTask().performedDuring(period);
        goToStatus(new FailedStatus(getTask()));
    }

    private static String ERROR_FINISH                = "Can't finish a task with unfinished dependencies.";

    private static String ERROR_SET_ALTERNATIVE_ERROR = "Can't set an alternative for an ongoing task.";

    @Override
    protected boolean canHaveAsAlternative(Task task) {
        return false;
    }

}
