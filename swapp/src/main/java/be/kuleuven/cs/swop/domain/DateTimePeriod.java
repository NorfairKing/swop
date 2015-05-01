package be.kuleuven.cs.swop.domain;


import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A period between two dates
 */
@SuppressWarnings("serial")
public class DateTimePeriod implements Serializable {

    private LocalDateTime startTime;
    private LocalDateTime stopTime;

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
    public DateTimePeriod(LocalDateTime start, LocalDateTime stop) {
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
     * Checks whether or not the given time is a valid beginning for this period, it's valid when the Date isn't null.
     *
     * @param startTime
     *            The Date containing the time to be checked if it is a valid beginning for this perdiod.
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
     * Checks whether or not the given time is a valid ending for this period, it's valid when the Date isn't null.
     *
     * @param stopTime
     *            The Date containing the time to be checked if it is a valid ending for this perdiod.
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

    /**
     * Checks if the given date time falls in this period
     * 
     * @param time The time to check
     * @return Yes or no
     */
    public boolean isDuring(LocalDateTime time) {
        return !time.isBefore(this.getStartTime()) && !time.isAfter(this.getStopTime());
    }
    
    /**
     * Checks if the given date time falls in this period
     * This however ignores the extremes, ie the exact start and end time.
     * 
     * @param time The time to check
     * @return Yes or no
     */
    public boolean isDuringExcludeExtremes(LocalDateTime time) {
        return time.isAfter(this.getStartTime()) && time.isBefore(this.getStopTime());
    }

    /**
     * Check if the given period fall entirely inside this one
     * This is not the same as overlapping, the given one has to fall entirely in this one.
     * 
     * @param period The period to check
     * @return Whether it falls entirely inside this period
     */
    public boolean isDuring(DateTimePeriod period) {
        return !period.getStartTime().isBefore(this.getStartTime()) && !period.getStopTime().isAfter(this.getStopTime());
    }
    
    /**
     * Checks to see if the given period overlaps with this one.
     * 
     * @param period The period to check
     * @return Whether the two periods overlap
     */
    public boolean overlaps(DateTimePeriod period) {
        if (this.isDuringExcludeExtremes(period.startTime)) {
            return true;
        }
        if (this.isDuringExcludeExtremes(period.stopTime)) {
            return true;
        }
        if (period.isDuringExcludeExtremes(startTime)) {
            return true;
        }
        if (period.isDuringExcludeExtremes(stopTime)) {
            return true;
        }
        if(period.startTime.equals(startTime) && period.stopTime.equals(stopTime)){
        	return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "DateTimePeriod [startTime=" + startTime + ", stopTime=" + stopTime + "]";
    }
    
    @Override
    public boolean equals(Object o){
    	if(!(o instanceof DateTimePeriod)){
    		return false;
    	}
    	return this.startTime.equals(((DateTimePeriod) o).getStartTime()) && this.stopTime.equals(((DateTimePeriod) o).getStopTime());
    }

    private static final String ERROR_ILLEGAL_START_TIME = "Illegal start time for time span.";
    private static final String ERROR_ILLEGAL_STOP_TIME  = "Illegal stop time for time span.";
}
