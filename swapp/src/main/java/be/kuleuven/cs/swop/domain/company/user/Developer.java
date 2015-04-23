package be.kuleuven.cs.swop.domain.company.user;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;

import java.time.LocalTime;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class Developer extends User {

    private TimePeriod workday = new TimePeriod(LocalTime.of(WORKDAY_START,0), LocalTime.of(WORKDAY_END,0)); //cannot be static :(

    private TimePeriod breakPeriod = new TimePeriod(LocalTime.of(BREAK_PERIOD_START,0), LocalTime.of(BREAK_PERIOD_END,0));

    public Developer(String name) {
        super(name);
    }

    public boolean isAvailableDuring(DateTimePeriod period) {
        return this.workday.isDuring(period);
    }

    public boolean isAvailableDuring(LocalTime time) {
        return workday.isDuring(time);
    }

    public boolean isAvailableDuring(LocalDateTime time) {
        return workday.isDuring(LocalTime.from(time));
    }

    public boolean canTakeBreakDuring(DateTimePeriod period) {
        return this.breakPeriod.isDuring(period);
    }

    // FIXME, use better values when you figure out how to use them.
    // Also, change them to getters, in case we might want them to be
    // different for different developers.
    private static final int WORKDAY_START      = 7;
    public  static final int BREAK_TIME         = 1;
    private static final int BREAK_PERIOD_START = 11;
    private static final int BREAK_PERIOD_END   = 14;
    private static final int WORKDAY_END        = 17;
}
