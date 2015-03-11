package be.kuleuven.cs.swop.domain.task.status;


public abstract class TaskStatus {

    public TaskStatus() {
    }

    public abstract boolean isFinished();
    public abstract boolean isFailed();
    public abstract boolean canFinish();
    public abstract boolean canFail();
    public abstract boolean isFinal();
    
    }
