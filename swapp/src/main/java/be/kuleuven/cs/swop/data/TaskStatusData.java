package be.kuleuven.cs.swop.data;


import java.util.Date;


public class TaskStatusData {

    private Date    startTime;
    private Date    endTime;
    private boolean successful;

    public TaskStatusData(Date startTime, Date endTime, boolean successful) {
        this.setStartTime(startTime);
        this.setEndTime(endTime);
        this.setSuccessful(successful);
    }

    public Date getStartTime() {
        return startTime;
    }

    private void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    private void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean getSuccessful() {
        return successful;
    }

    private void setSuccessful(boolean successful) {
        this.successful = successful;
    }

}
