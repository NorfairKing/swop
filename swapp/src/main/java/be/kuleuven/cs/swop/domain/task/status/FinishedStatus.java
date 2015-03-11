package be.kuleuven.cs.swop.domain.task.status;


public class FinishedStatus extends TaskStatus{

    public FinishedStatus() {
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
