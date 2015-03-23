package be.kuleuven.cs.swop.domain.task;

import be.kuleuven.cs.swop.domain.TimePeriod;


public class FinishedStatus extends TaskStatus {


    public FinishedStatus(Task task) {
        super(task);
    }

    /**
     * Checks whether the project containing this status is finished.
     *
     * @return Returns true.
     */
    @Override
    public boolean isFinished() {
        return true;
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
        throw new IllegalStateException(ERROR_SET_ALTERNATIVE);
    }
    @Override
    void fail(TimePeriod period) {
        throw new IllegalStateException(ERROR_FAIL);
    }
    @Override
    void finish(TimePeriod period) {
        throw new IllegalStateException(ERROR_FINISH);
    } 
    private static String ERROR_FINISH= "Can't finish a finished task.";
    private static String ERROR_FAIL   = "Can't fail a finished task.";
 
    private static String ERROR_SET_ALTERNATIVE = "Can't set an alternative for a finished task.";

    @Override
    protected boolean canHaveAsAlternative(Task task) {
        return false;
    }

}
