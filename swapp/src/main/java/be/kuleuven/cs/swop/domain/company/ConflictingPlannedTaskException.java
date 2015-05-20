package be.kuleuven.cs.swop.domain.company;


import be.kuleuven.cs.swop.domain.company.task.Task;


@SuppressWarnings("serial")
public class ConflictingPlannedTaskException extends Exception {

    private Task task;

    ConflictingPlannedTaskException(Task task) {
        this.setTask(task);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
