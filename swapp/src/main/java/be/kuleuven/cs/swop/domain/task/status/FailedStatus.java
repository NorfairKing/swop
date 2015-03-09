package be.kuleuven.cs.swop.domain.task.status;

import be.kuleuven.cs.swop.domain.task.RealTask;


public class FailedStatus extends TaskStatus {

    public FailedStatus(RealTask task) {
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

}
