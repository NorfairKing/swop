package be.kuleuven.cs.swop.domain;


import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public final class TimeCalculator {

    private static int         workDayStart = 8;
    private static int         workDayEnd   = 16;
    private static DayOfWeek[] workDays     = new DayOfWeek[] { DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY };

    private static LocalDateTime startOfDay(LocalDateTime input) {
        return input.withSecond(0).withMinute(0).withHour(0);
    }

    private static LocalDateTime endOfDay(LocalDateTime input) {
        return startOfDay(input).plusDays(1);
    }

    // time1 and time2 need to be on the same day!!!w
    private static int timeInSingleDayBetween(LocalDateTime time1, LocalDateTime time2) {
        LocalDateTime startOfWorkDay = time1.withHour(workDayStart).withMinute(0).withSecond(0);
        LocalDateTime endOfWorkDay = time1.withHour(workDayEnd).withMinute(0).withSecond(0);
        LocalDateTime countStart;
        LocalDateTime countStop;
        if (!isWorkDay(time1)) { return 0; }
        if (startOfWorkDay.isBefore(time1)) {
            countStart = time1;
        } else {
            countStart = startOfWorkDay;
        }

        if (endOfWorkDay.isBefore(time2)) {
            countStop = endOfWorkDay;
        } else {
            countStop = time2;
        }

        int difference = (int) ChronoUnit.MINUTES.between(countStart, countStop);
        if (difference > 0) {
            return difference;
        } else {
            return 0;
        }
    }

    private static boolean isWorkDay(LocalDateTime time) {
        DayOfWeek day = time.getDayOfWeek();
        for (DayOfWeek d : workDays) {
            if (d == day) { return true; }
        }
        return false;
    }

    /**
     * This method calculates the working hours between two given Dates in minutes.
     * 
     * @param time1
     *            The starting Date for this calculation.
     * @param time2
     *            The end Ddate for this calculation.
     * @return Returns an integer containing the working hours between the two Dates in minutes.
     */
    public static int workingMinutesBetween(LocalDateTime start, LocalDateTime stop) {
        LocalDateTime endOfDay = endOfDay(start);
        int workMinutes = 0;

        while (endOfDay.isBefore(stop)) {
            workMinutes += timeInSingleDayBetween(start, endOfDay);

            start = endOfDay;
            endOfDay = endOfDay.plusDays(1);
        }

        workMinutes += timeInSingleDayBetween(start, stop);
        return workMinutes;
    }

    public static LocalDateTime addWorkingMinutes(LocalDateTime date, long minutes) {
        // Take care of weekends
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            date = date.plusDays(2).withHour(8).withMinute(0).withSecond(0);
        } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.plusDays(1).withHour(8).withMinute(0).withSecond(0);
        }

        if (date.getHour() < 8) {
            // Working day hasn't started. Reset date to start of this working day
            date = date.withHour(8).withMinute(0).withSecond(0);
        }

        LocalDateTime endOfCurrentWorkingDay = date.withHour(16).withMinute(0).withSecond(0);

        // Get minutes from date to endOfCurrentWorkingDay
        long minutesCovered = ChronoUnit.MINUTES.between(date, endOfCurrentWorkingDay);
        if (minutesCovered >= minutes) {
            // If minutesCovered covers the minutes value passed, that means result is the same working
            // day. Just add minutes and return
            return date.plusMinutes(minutes);
        } else {
            // Calculate remainingMinutes, and then recursively call this method with next working day
            long remainingMinutes = minutes - minutesCovered;
            return addWorkingMinutes(endOfCurrentWorkingDay.plusDays(1).withHour(8).withMinute(0).withSecond(0), remainingMinutes);
        }
    }

    public static long getDurationMinutes(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.MINUTES.between(start, end);
    }

}
