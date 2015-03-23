package be.kuleuven.cs.swop.facade;

import java.time.LocalDateTime;

public class TaskStatusData {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean       successful;

    public TaskStatusData(LocalDateTime startTime, LocalDateTime endTime, boolean successful) {
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setSuccessful(successful);
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

    public boolean getSuccessful() {
        return successful;
    }

    private void setSuccessful(boolean successful) {
        this.successful = successful;
    }

}
