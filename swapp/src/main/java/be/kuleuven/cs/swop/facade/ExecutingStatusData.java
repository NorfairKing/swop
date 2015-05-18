package be.kuleuven.cs.swop.facade;

public class ExecutingStatusData extends TaskStatusData {
    
    public ExecutingStatusData() { }
    
    @Override
    public boolean isFinal() {
        return false;
    }

}
