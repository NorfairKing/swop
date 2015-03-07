package be.kuleuven.cs.swop.domain.task.status;

import be.kuleuven.cs.swop.domain.task.Task;



public abstract class TaskStatus {

    protected Task task;

    public TaskStatus(Task task) {
        setTask(task);
    }

    public Task getTask() {
        return task;
    }

    protected static boolean canHaveAsTask(Task task) {
        return task != null;
    }

    private void setTask(Task task) {
        if (!canHaveAsTask(task)) throw new IllegalArgumentException(ERROR_ILLEGAL_TASK);
        this.task = task;
    }

    public abstract TaskStatus finish();
    public abstract TaskStatus fail();
    
    public boolean isFinished(){
    	return false;
    }
    
    
    private static final String ERROR_ILLEGAL_TASK = "Invalid task for task status";
}