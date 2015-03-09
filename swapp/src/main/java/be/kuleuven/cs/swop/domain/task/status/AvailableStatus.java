package be.kuleuven.cs.swop.domain.task.status;

import be.kuleuven.cs.swop.domain.task.Task;


public class AvailableStatus extends TaskStatus{

    public AvailableStatus(Task task) {
        super(task);
    }

    @Override
    public TaskStatus finish() {
        return new FinishedStatus(getTask());
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
        return true;
    }

    @Override
    public boolean canFail() {
        return true;
    }

}
