package be.kuleuven.cs.swop.domain;


import java.time.LocalDateTime;


public class TimePeriod {

    private LocalDateTime startTime;
    private LocalDateTime stopTime;

    /**
     * Full Constructor
     *
     * @param start The Date containing the start of this peroid.
     *
     * @param stop The Date containing the end of this peroid.
     *
     */
    public TimePeriod(LocalDateTime start, LocalDateTime stop) {
        setStartTime(start);
        setStopTime(stop);
    }

    /**
     * Retries the time on which this period starts.
     *
     * @return The Date containing the start of this period.
     *
     */
    public LocalDateTime getStartTime() {
        return (LocalDateTime) startTime;
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
    protected boolean canHaveAsStartTime(LocalDateTime startTime) {
        return startTime != null;
    }

    private void setStartTime(LocalDateTime startTime) {
        if (!canHaveAsStartTime(startTime)) throw new IllegalArgumentException(ERROR_ILLEGAL_START_TIME);
        this.startTime = startTime;
    }

    /**
     * Retries the time on which this period ends.
     *
     * @return The Date containing the end of this period.
     *
     */
    public LocalDateTime getStopTime() {
        return (LocalDateTime) stopTime;
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
    protected boolean canHaveAsStopTime(LocalDateTime stopTime) {
        return stopTime != null && startTime != null && startTime.isBefore(stopTime);
    }

    /**
     * Has to be used AFTER setStartTime().
     */
    private void setStopTime(LocalDateTime stopTime) {
        if (!canHaveAsStopTime(stopTime)) throw new IllegalArgumentException(ERROR_ILLEGAL_STOP_TIME);
        this.stopTime = stopTime;
    }

    public boolean isDuring(LocalDateTime time) {
        return time.isAfter(this.getStartTime()) && time.isBefore(this.getStopTime());
    }

    private static final String ERROR_ILLEGAL_START_TIME = "Illegal start time for time span.";
    private static final String ERROR_ILLEGAL_STOP_TIME  = "Illegal stop time for time span.";
}
