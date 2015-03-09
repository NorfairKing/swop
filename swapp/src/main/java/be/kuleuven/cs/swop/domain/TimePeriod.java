package be.kuleuven.cs.swop.domain;


import java.util.Date;


public class TimePeriod {

    private Date startTime;
    private Date stopTime;

    public TimePeriod(Date start, Date stop) {
        setStartTime(start);
        setStopTime(stop);
    }

    public Date getStartTime() {
        return startTime;
    }

    protected boolean canHaveAsStartTime(Date startTime) {
        return startTime != null;
    }

    private void setStartTime(Date startTime) {
        if (!canHaveAsStartTime(startTime)) throw new IllegalArgumentException(ERROR_ILLEGAL_START_TIME);
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    protected boolean canHaveAsStopTime(Date stopTime) {
        return stopTime != null && startTime.before(stopTime);
    }

    /**
     * Has to be used AFTER setStartTime().
     * @param stopTime
     */
    private void setStopTime(Date stopTime) {
        if (!canHaveAsStartTime(startTime)) throw new IllegalArgumentException(ERROR_ILLEGAL_STOP_TIME);
        this.stopTime = stopTime;
    }

    private static final String ERROR_ILLEGAL_START_TIME = "Illegal start time for time span.";
    private static final String ERROR_ILLEGAL_STOP_TIME = "Illegal stop time for time span.";
}
