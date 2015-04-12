package be.kuleuven.cs.swop.facade;

import java.time.LocalDateTime;

public abstract class PerformedStatusData extends TaskStatusData {

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public PerformedStatusData(LocalDateTime startTime, LocalDateTime endTime) {
        this.setStartTime(startTime);
        this.setEndTime(endTime);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    private void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    private void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public abstract boolean isSuccessful();
    
    public boolean isFinal(){
    	return true;
    }

}
