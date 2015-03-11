package be.kuleuven.cs.swop.domain.task.status;


public class UnavailableStatus extends TaskStatus {

    public UnavailableStatus() {}

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
        return false;
    }

    @Override
    public boolean canFail() {
        return true;
    }
}
