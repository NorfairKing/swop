package be.kuleuven.cs.swop.domain.task.status;

import be.kuleuven.cs.swop.domain.task.RealTask;


public class FinishedStatus extends TaskStatus{

    public FinishedStatus(RealTask task) {
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

}
