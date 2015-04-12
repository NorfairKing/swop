package be.kuleuven.cs.swop.domain;


import java.io.Serializable;
import java.time.LocalTime;


@SuppressWarnings("serial")
public class TimePeriod implements Serializable {

    private LocalTime startTime;
    private LocalTime stopTime;

    /**
     * Full Constructor
     *
     * @param start
     *            The Time containing the start of this period.
     *
     * @param stop
     *            The Time containing the end of this period.
     *
     */
    public TimePeriod(LocalTime start, LocalTime stop) {
        setStartTime(start);
        setStopTime(stop);
    }

    /**
     * Retries the time on which this period starts.
     *
     * @return The Date containing the start of this period.
     *
     */
    public LocalTime getStartTime() {
        return (LocalTime) startTime;
    }

    /**
     * Checks whether or not the given time is a valid beginning for this period, it's valid when the Date isn't null.
     *
     * @param startTime
     *            The Time containing the time to be checked if it is a valid beginning for this period.
     *
     * @return Returns true if the given time is a valid beginning for the period.
     *
     */
    protected boolean canHaveAsStartTime(LocalTime startTime) {
        return startTime != null;
    }

    private void setStartTime(LocalTime startTime) {
        if (!canHaveAsStartTime(startTime)) throw new IllegalArgumentException(ERROR_ILLEGAL_START_TIME);
        this.startTime = startTime;
    }

    /**
     * Retries the time on which this period ends.
     *
     * @return The Time containing the end of this period.
     *
     */
    public LocalTime getStopTime() {
        return (LocalTime) stopTime;
    }

    /**
     * Checks whether or not the given time is a valid ending for this period, it's valid when the Date isn't null.
     *
     * @param stopTime
     *            The Time containing the time to be checked if it is a valid ending for this perdiod.
     *
     * @return Returns true if the given time is a valid beginning for the period.
     *
     */
    protected boolean canHaveAsStopTime(LocalTime stopTime) {
        return stopTime != null && startTime != null && startTime.isBefore(stopTime);
    }

    /**
     * Has to be used AFTER setStartTime().
     */
    private void setStopTime(LocalTime stopTime) {
        if (!canHaveAsStopTime(stopTime)) throw new IllegalArgumentException(ERROR_ILLEGAL_STOP_TIME);
        this.stopTime = stopTime;
    }

    public boolean isDuring(LocalTime time) {
        return time.isAfter(this.getStartTime()) && time.isBefore(this.getStopTime());
    }

    public boolean isDuring(TimePeriod period) {
        return period.getStartTime().isAfter(this.getStartTime()) && period.getStopTime().isBefore(this.getStopTime());
    }

    private static final String ERROR_ILLEGAL_START_TIME = "Illegal start time for time span.";
    private static final String ERROR_ILLEGAL_STOP_TIME  = "Illegal stop time for time span.";
}
