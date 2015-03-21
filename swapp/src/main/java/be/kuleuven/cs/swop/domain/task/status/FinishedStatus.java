package be.kuleuven.cs.swop.domain.task.status;


public class FinishedStatus extends TaskStatus {

    public FinishedStatus() {}

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

}
