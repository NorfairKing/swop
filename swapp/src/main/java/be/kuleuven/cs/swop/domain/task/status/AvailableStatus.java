package be.kuleuven.cs.swop.domain.task.status;

import be.kuleuven.cs.swop.domain.task.RealTask;


public class AvailableStatus extends TaskStatus{

    public AvailableStatus(RealTask task) {
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

}
