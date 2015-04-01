package be.kuleuven.cs.swop.domain;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class Timekeeper implements Serializable {

    private LocalDateTime currentTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
    private static int workDayStart = 8;
    private static int workDayEnd = 16;
    private static DayOfWeek[] workDays = new DayOfWeek[]{DayOfWeek.MONDAY,DayOfWeek.TUESDAY,DayOfWeek.WEDNESDAY,DayOfWeek.THURSDAY,DayOfWeek.FRIDAY};

    /**
     * Return the current system time.
     * 
     * @return Returns the current system time.
     */
    public LocalDateTime getTime() {
        return (LocalDateTime) currentTime;
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

    
    
    // Time helper functions
    
    private static LocalDateTime startOfDay(LocalDateTime input){
        return input.withSecond(0).withMinute(0).withHour(0);
    }
    private static LocalDateTime endOfDay(LocalDateTime input){
        return startOfDay(input).plusDays(1);
    }
    
    // time1 and time2 need to be on the same day!!!w
    private static int timeInSingleDayBetween(LocalDateTime time1, LocalDateTime time2){
        LocalDateTime startOfWorkDay = time1.withHour(workDayStart).withMinute(0).withSecond(0);
        LocalDateTime endOfWorkDay = time1.withHour(workDayEnd).withMinute(0).withSecond(0);
        LocalDateTime countStart;
        LocalDateTime countStop;
        if(! isWorkDay(time1)){
            return 0;
        }
        if(startOfWorkDay.isBefore(time1)){ 
            countStart = time1;
        }else{
            countStart = startOfWorkDay;
        }

        if(endOfWorkDay.isBefore(time2)){
            countStop = endOfWorkDay;
        }else{
            countStop = time2;
        }

        int difference = (int) ChronoUnit.MINUTES.between(countStart,countStop);
        if(difference > 0){
            return difference;
        }else{
            return 0;
        }
    }
    
    private static boolean isWorkDay(LocalDateTime time){
        DayOfWeek day = time.getDayOfWeek();
        for(DayOfWeek d: workDays){
            if(d == day){
                return true;
            }
        }
        return false;
    }

    /**
     * This method calculates the working hours between two given Dates in minutes.
     * @param time1 The starting Date for this calculation.
     * @param time2 The end Ddate for this calculation.
     * @return Returns an integer containing the working hours between the two Dates in minutes.
     */
    public static int workingMinutesBetween(LocalDateTime start, LocalDateTime stop){
        LocalDateTime endOfDay = endOfDay(start);
        int workMinutes = 0;

        while(endOfDay.isBefore(stop)){
            workMinutes += timeInSingleDayBetween(start, endOfDay);

            start = endOfDay;
            endOfDay = endOfDay.plusDays(1);
        }

        workMinutes += timeInSingleDayBetween(start, stop);
        return workMinutes;
    }

}
