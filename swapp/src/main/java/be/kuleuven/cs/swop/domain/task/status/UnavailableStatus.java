package be.kuleuven.cs.swop.domain.task.status;

import be.kuleuven.cs.swop.domain.task.Task;


public class UnavailableStatus extends TaskStatus {

    public UnavailableStatus(Task task) {
        super(task);
    }

    @Override
    public TaskStatus finish() {
        return this;
    }

    @Override
    public TaskStatus fail() {
        return new FailedStatus(getTask());
    }

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
