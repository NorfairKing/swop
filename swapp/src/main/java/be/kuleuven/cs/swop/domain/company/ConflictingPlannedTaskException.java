package be.kuleuven.cs.swop.domain.company;


import be.kuleuven.cs.swop.domain.company.task.Task;


@SuppressWarnings("serial")
public class ConflictingPlannedTaskException extends Exception {

    private final Task task;

    ConflictingPlannedTaskException(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
