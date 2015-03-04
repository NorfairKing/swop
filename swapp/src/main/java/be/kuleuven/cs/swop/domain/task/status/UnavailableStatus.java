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
}
