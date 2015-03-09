package be.kuleuven.cs.swop.domain.task.status;

import be.kuleuven.cs.swop.domain.task.Task;


public class FailedStatus extends TaskStatus {

    public FailedStatus(Task task) {
        super(task);
    }

    @Override
    public TaskStatus finish() {
        return this;
    }

    @Override
    public TaskStatus fail() {
        return this;
    }

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

}
