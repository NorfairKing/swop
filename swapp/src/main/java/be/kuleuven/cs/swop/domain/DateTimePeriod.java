package be.kuleuven.cs.swop.domain;


import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * A period between two dates
 */
@SuppressWarnings("serial")
public class DateTimePeriod implements Serializable {

    private final LocalDateTime startTime;
    private final LocalDateTime stopTime;

    /**
     * Full Constructor
     *
     * @param start
     *            The Date containing the start of this peroid.
     *
     * @param stop
     *            The Date containing the end of this peroid.
     *
     */
    @SuppressWarnings("unused") // used by automatic (de)serialization
	private DateTimePeriod(){startTime = null;stopTime = null;}
    
    public DateTimePeriod(LocalDateTime start, LocalDateTime stop) {
        if (start == null) throw new IllegalArgumentException(ERROR_ILLEGAL_TIMES);
        if (stop == null) throw new IllegalArgumentException(ERROR_ILLEGAL_TIMES);
        if (start.isAfter(stop)) throw new IllegalArgumentException(ERROR_ILLEGAL_TIMES);

        this.startTime = start;
        this.stopTime = stop;
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
     * Retries the time on which this period ends.
     *
     * @return The Date containing the end of this period.
     *
     */
    public LocalDateTime getStopTime() {
        return (LocalDateTime) stopTime;
    }

    /**
     * Checks if the given date time falls in this period
     * 
     * @param time
     *            The time to check
     * @return Yes or no
     */
    public boolean isDuring(LocalDateTime time) {
        return !time.isBefore(this.getStartTime()) && !time.isAfter(this.getStopTime());
    }

    /**
     * Checks if the given date time falls in this period This however ignores the extremes, ie the exact start and end time.
     * 
     * @param time
     *            The time to check
     * @return Yes or no
     */
    public boolean isDuringExcludeExtremes(LocalDateTime time) {
        return time.isAfter(this.getStartTime()) && time.isBefore(this.getStopTime());
    }

    /**
     * Check if the given period fall entirely inside this one This is not the same as overlapping, the given one has to fall entirely in this one.
     * 
     * @param period
     *            The period to check
     * @return Whether it falls entirely inside this period
     */
    public boolean isDuring(DateTimePeriod period) {
        return !period.getStartTime().isBefore(this.getStartTime()) && !period.getStopTime().isAfter(this.getStopTime());
    }

    /**
     * Checks to see if the given period overlaps with this one.
     * 
     * @param period
     *            The period to check
     * @return Whether the two periods overlap
     */
    public boolean overlaps(DateTimePeriod period) {
        if (this.isDuringExcludeExtremes(period.startTime)) { return true; }
        if (this.isDuringExcludeExtremes(period.stopTime)) { return true; }
        if (period.isDuringExcludeExtremes(startTime)) { return true; }
        if (period.isDuringExcludeExtremes(stopTime)) { return true; }
        if (period.startTime.equals(startTime) && period.stopTime.equals(stopTime)) { return true; }
        return false;
    }

    @Override
    public String toString() {
        return "DateTimePeriod [startTime=" + startTime + ", stopTime=" + stopTime + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result + ((stopTime == null) ? 0 : stopTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DateTimePeriod other = (DateTimePeriod) obj;
        if (!startTime.equals(other.startTime)) return false;
        if (!stopTime.equals(other.stopTime)) return false;
        return true;
    }

    private static final String ERROR_ILLEGAL_TIMES = "Illegal start or stop time for time span.";
}
