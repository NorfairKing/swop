package be.kuleuven.cs.swop.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@SuppressWarnings("serial")
public class Timekeeper implements Serializable {

    private LocalDateTime currentTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
    
    /**
     * Return the current system time.
     * 
     * @return Returns the current system time.
     */
    public LocalDateTime getTime() {
        return currentTime;
    }

    /**
     * Checks whether or not this ProjectManager can use the given time as system time. *
     * 
     * @param time
     *            The Date containing the new system time.
     *
     * @return Returns true if the given Date isn't null.
     *
     */
    protected boolean canHaveAsTime(LocalDateTime time) {
        return time != null;
    }

    /**
     * Changed the system time to the given time.
     *
     * @param time
     *            The Date containing the new system time.
     *
     * @throws IllegalArgumentException
     *             If the given Date is invalid, which means that it's null.
     *
     */
    public void setTime(LocalDateTime time) {
        if (!canHaveAsTime(time)) { throw new IllegalArgumentException("Invalid time for the system."); }
        currentTime = (LocalDateTime) time;
    }

}
