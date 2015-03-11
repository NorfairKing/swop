package be.kuleuven.cs.swop.domain.task.status;


public class AvailableStatus extends TaskStatus {

    public AvailableStatus() {}

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
     * @return Returns true.
     */
    @Override
    public boolean canFinish() {
        return true;
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

}
