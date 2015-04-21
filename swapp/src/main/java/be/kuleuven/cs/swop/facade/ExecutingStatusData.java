package be.kuleuven.cs.swop.facade;

public class ExecutingStatusData extends TaskStatusData {
    
    private UserWrapper dev;
    
    public ExecutingStatusData(UserWrapper dev) {
        this.dev = dev;
    }
    
    @Override
    public boolean isFinal() {
        return false;
    }
	
	public UserWrapper getDeveloper() {
        return dev;
    }

}
