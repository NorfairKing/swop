package be.kuleuven.cs.swop.domain.task.status;

import be.kuleuven.cs.swop.domain.task.RealTask;



public abstract class TaskStatus {

    protected RealTask task;

    public TaskStatus(RealTask task) {
        setTask(task);
    }

    public RealTask getTask() {
        return task;
    }

    protected static boolean canHaveAsTask(RealTask task) {
        return task != null;
    }

    private void setTask(RealTask task) {
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
