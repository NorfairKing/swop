package be.kuleuven.cs.swop.domain.task.status;


public class AvailableStatus extends TaskStatus {

    public AvailableStatus() {}

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isFailed() {
        return false;
    }

    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public boolean canFinish() {
        return true;
    }

    @Override
    public boolean canFail() {
        return true;
    }

}
