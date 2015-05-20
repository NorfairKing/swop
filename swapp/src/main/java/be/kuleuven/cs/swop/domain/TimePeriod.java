package be.kuleuven.cs.swop.domain;


import java.io.Serializable;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


/**
 * A period between two given hours in a single day
 */
@SuppressWarnings("serial")
public class TimePeriod implements Serializable {

    private final LocalTime startTime;
    private final LocalTime stopTime;

    @SuppressWarnings("unused")
    // used for automatic (de)serializing
    private TimePeriod() {
        this.startTime = this.stopTime = null;
    }

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
    public TimePeriod(LocalTime startTime, LocalTime stopTime) {
        if (startTime == null) throw new IllegalArgumentException(ERROR_ILLEGAL_START_TIME);
        if (stopTime == null) throw new IllegalArgumentException(ERROR_ILLEGAL_STOP_TIME);

        this.startTime = startTime;
        this.stopTime = stopTime;
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
     * Retries the time on which this period ends.
     *
     * @return The Time containing the end of this period.
     *
     */
    public LocalTime getStopTime() {
        return (LocalTime) stopTime;
    }

    /**
     * Checks if the give times is during this period
     * 
     * @param time
     *            The time to check
     * @return Whether it is or not
     */
    public boolean isDuring(LocalTime time) {
        if (time == null) { throw new IllegalArgumentException(ERROR_NULL_DURING_TIME); }
        return !time.isBefore(this.getStartTime()) && !time.isAfter(this.getStopTime());
    }

    /**
     * Checks if the given period falls entirely inside this one If the given period falls (partly) outside this period, it'll return false.
     * 
     * @param period
     *            The period to check
     * @return Whether it does or not
     */
    public boolean isDuring(TimePeriod period) {
        if (period == null) { throw new IllegalArgumentException(ERROR_NULL_DURING_PERIOD); }
        return period.getStartTime().isAfter(this.getStartTime()) && period.getStopTime().isBefore(this.getStopTime());
    }

    /**
     * Checks if the given period is during this one.
     *
     * @param period
     *            The period to check
     * @return Whether of it does or not
     */
    public boolean isDuring(DateTimePeriod period) {
        if (period == null) { throw new IllegalArgumentException(ERROR_NULL_DURING_PERIOD); }
        LocalTime start = LocalTime.from(period.getStartTime());
        LocalTime stop = LocalTime.from(period.getStopTime());
        if (start.isBefore(stop) && ChronoUnit.DAYS.between(period.getStartTime(), period.getStopTime()) == 0) {
            return !this.getStartTime().isAfter(start) &&
                    !this.getStopTime().isBefore(stop);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + startTime.hashCode();
        result = prime * result + stopTime.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TimePeriod other = (TimePeriod) obj;
        if (!startTime.equals(other.startTime)) return false;
        if (!stopTime.equals(other.stopTime)) return false;
        return true;
    }

    private static final String ERROR_ILLEGAL_START_TIME = "Illegal start time for time span.";
    private static final String ERROR_ILLEGAL_STOP_TIME  = "Illegal stop time for time span.";
    private static final String ERROR_NULL_DURING_TIME   = "The time to check may not be null.";
    private static final String ERROR_NULL_DURING_PERIOD = "The period to check may not be null.";
}
