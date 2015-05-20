package be.kuleuven.cs.swop.domain.company.user;


import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.resource.TimeConstrainedResourceType;

import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.HashSet;


@SuppressWarnings("serial")
public class Developer extends Resource implements User {

    public Developer(String name) {
        super(DEVELOPER_TYPE, name);
    }

    public boolean isAvailableDuring(DateTimePeriod period) {
        return WORKDAY.isDuring(period);
    }

    public boolean isAvailableDuring(LocalTime time) {
        return WORKDAY.isDuring(time);
    }

    public boolean isAvailableDuring(LocalDateTime time) {
        if (time == null) { throw new IllegalArgumentException(ERROR_NULL_DURING_TIME); }
        return WORKDAY.isDuring(LocalTime.from(time));
    }

    public boolean canTakeBreakDuring(DateTimePeriod period) {
        return BREAK_PERIOD.isDuring(period);
    }
    
    public ResourceType getType() {
        return DEVELOPER_TYPE;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + getName().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Developer other = (Developer) obj;
        if (!getName().equals(other.getName())) return false;
        return true;
    }

    // FIXME, use better values when you figure out how to use them.
    // Also, change them to getters, in case we might want them to be
    // different for different developers.
    private static final int         WORKDAY_START          = 7;
    public static final int          BREAK_TIME             = 1;
    private static final int         BREAK_PERIOD_START     = 11;
    private static final int         BREAK_PERIOD_END       = 14;
    private static final int         WORKDAY_END            = 17;

    private static final TimePeriod  WORKDAY                = new TimePeriod(LocalTime.of(WORKDAY_START, 0), LocalTime.of(WORKDAY_END, 0));
    private static final TimePeriod  BREAK_PERIOD           = new TimePeriod(LocalTime.of(BREAK_PERIOD_START, 0), LocalTime.of(BREAK_PERIOD_END, 0));

    private static final String      ERROR_NULL_DURING_TIME = "The time to check may not be null.";

    public static final ResourceType DEVELOPER_TYPE         = new TimeConstrainedResourceType(
                                                                    "Developer", new HashSet<>(), new HashSet<>(), false, WORKDAY);

}
