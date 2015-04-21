package be.kuleuven.cs.swop.domain.company.user;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;

import java.time.LocalTime;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class Developer extends User {

    private TimePeriod workday = new TimePeriod(LocalTime.of(WORKDAY_START,0), LocalTime.of(WORKDAY_END,0)); //cannot be static :(

    public Developer(String name) {
        super(name);
    }

    public boolean isAvailableDuring(DateTimePeriod period) {
        LocalTime start = LocalTime.from(period.getStartTime());
        LocalTime stop = LocalTime.from(period.getStopTime());
        if (start.isBefore(stop)) {
            return !workday.getStartTime().isAfter(start) &&
                !workday.getStopTime().isBefore(stop);
        } else {
            return false;
        }
    }

    public boolean isAvailableDuring(LocalTime time) {
        return workday.isDuring(time);
    }

    public boolean isAvailableDuring(LocalDateTime time) {
        return workday.isDuring(LocalTime.from(time));
    }

    // FIXME, use better values when you figure out how to use them.
    // Also, change them to getters, in case we might want them to be
    // different for different developers.
    private static final int WORKDAY_START      = 7;
    private static final int BREAK_TIME         = 1;
    private static final int BREAK_PERIOD_START = 11;
    private static final int BREAK_PERIOD_END   = 14;
    private static final int WORKDAY_END        = 17;
}
