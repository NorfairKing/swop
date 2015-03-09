package be.kuleuven.cs.swop.domain.task.status;

import be.kuleuven.cs.swop.domain.task.Task;


public class FinishedStatus extends TaskStatus{

    public FinishedStatus(Task task) {
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
    
    @Override
    public boolean isFinished(){
    	return true;
    }

    @Override
    public boolean isFailed() {
        return false;
    }

    @Override
    public boolean isFinal() {
        return true;
    }

    @Override
    public boolean canFinish() {
        return false;
    }

    @Override
    public boolean canFail() {
        return false;
    }

}
