package be.kuleuven.cs.swop.domain;


import java.util.Calendar;
import java.util.Date;


public final class Timekeeper {

    private static Calendar currentTime = Calendar.getInstance();

    /**
     * Return the current system time.
     * 
     * @return Returns the current system time.
     */
    public static Date getTime() {
        return currentTime.getTime();
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
    protected static boolean canHaveAsTime(Date time) {
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
    public static void setTime(Date time) {
        if (!canHaveAsTime(time)) { throw new IllegalArgumentException("Invalid time for the system."); }
        currentTime.setTime(time);
    }

}
