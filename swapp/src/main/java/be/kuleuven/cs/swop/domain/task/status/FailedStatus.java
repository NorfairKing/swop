package be.kuleuven.cs.swop.domain.task.status;

import be.kuleuven.cs.swop.domain.task.Task;


public class FailedStatus extends TaskStatus {

    public FailedStatus(Task task) {
        super(task);
    }

}
