package be.kuleuven.cs.swop.domain.task.status;


public class FailedStatus extends TaskStatus {

    public FailedStatus() {}

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isFailed() {
        return true;
    }

    @Override
    public boolean isFinal() {
        return true;
    }

    @Override
    public boolean canFinish() {
        return false;
    }

    @Override
    public boolean canFail() {
        return false;
    }

}
