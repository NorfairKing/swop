package be.kuleuven.cs.swop.domain.task.status;

import be.kuleuven.cs.swop.domain.task.RealTask;


public class UnavailableStatus extends TaskStatus {

    public UnavailableStatus(RealTask task) {
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
