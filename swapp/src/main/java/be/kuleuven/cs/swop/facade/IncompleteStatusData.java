package be.kuleuven.cs.swop.facade;

public abstract class IncompleteStatusData extends TaskStatusData {

	
	public abstract boolean isExecuting();
	
	@Override
    public boolean isFinal(){
    	return false;
    }

}
