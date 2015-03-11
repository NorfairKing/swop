package be.kuleuven.cs.swop.domain;


import java.util.Date;


public class TimePeriod {

    private Date startTime;
    private Date stopTime;

    /**
     * Full Constructor
     *
     * @param start The Date containing the start of this peroid.
     *
     * @param stop The Date containing the end of this peroid.
     *
     */
    public TimePeriod(Date start, Date stop) {
        setStartTime(start);
        setStopTime(stop);
    }

    /**
     * Retries the time on which this period starts.
     *
     * @return The Date containing the start of this period.
     *
     */
    public Date getStartTime() {
        return (Date) startTime.clone();
    }

    /**
     * Checks whether or not the given time is a valid beginning for this period,
     * it's valid when the Date isn't null.
     *
     * @param startTime The Date containing the time to be checked if it is a valid
     * beginning for this perdiod.
     *
     * @return Returns true if the given time is a valid beginning for the period.
     *
     */
    protected boolean canHaveAsStartTime(Date startTime) {
        return startTime != null;
    }

    private void setStartTime(Date startTime) {
        if (!canHaveAsStartTime(startTime)) throw new IllegalArgumentException(ERROR_ILLEGAL_START_TIME);
        this.startTime = startTime;
    }

    /**
     * Retries the time on which this period ends.
     *
     * @return The Date containing the end of this period.
     *
     */
    public Date getStopTime() {
        return (Date) stopTime.clone();
    }

    /**
     * Checks whether or not the given time is a valid ending for this period,
     * it's valid when the Date isn't null.
     *
     * @param stopTime The Date containing the time to be checked if it is a valid
     * ending for this perdiod.
     *
     * @return Returns true if the given time is a valid beginning for the period.
     *
     */
    protected boolean canHaveAsStopTime(Date stopTime) {
        return stopTime != null && startTime != null && startTime.before(stopTime);
    }

    /**
     * Has to be used AFTER setStartTime().
     */
    private void setStopTime(Date stopTime) {
        if (!canHaveAsStopTime(stopTime)) throw new IllegalArgumentException(ERROR_ILLEGAL_STOP_TIME);
        this.stopTime = stopTime;
    }

    private static final String ERROR_ILLEGAL_START_TIME = "Illegal start time for time span.";
    private static final String ERROR_ILLEGAL_STOP_TIME  = "Illegal stop time for time span.";
}
