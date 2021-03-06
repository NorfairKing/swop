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

    private Developer() { super(DEVELOPER_TYPE, "name"); }; //for automatic (de)-serialization

    public Developer(String name) {
        super(DEVELOPER_TYPE, name);
    }

    /**
     * Checks whether or not this resouce type is available during the given period.
     *
     * @param period The to be checked DateTimePeriod.
     * @return True if this is available then.
     */
    public boolean isAvailableDuring(DateTimePeriod period) {
        return WORKDAY.isDuring(period);
    }

    /**
     * Checks whether or not this resouce type is available during the given time.
     *
     * @param time The to be checked LocalTime.
     * @return True if this is available then.
     */
    public boolean isAvailableDuring(LocalTime time) {
        return WORKDAY.isDuring(time);
    }

    /**
     * Checks whether or not this resouce type is available during the given time.
     *
     * @param time The to be checked LocalDateTime.
     * @return True if this is available then.
     */
    public boolean isAvailableDuring(LocalDateTime time) {
        if (time == null) { throw new IllegalArgumentException(ERROR_NULL_DURING_TIME); }
        return WORKDAY.isDuring(LocalTime.from(time));
    }

    /**
     * Checks whether or not this developer can take a break during the given perdiod.
     *
     * @param period The to be checked DateTimePerdiod
     * @return True if the developer can take a break.
     */
    public boolean canTakeBreakDuring(DateTimePeriod period) {
        return BREAK_PERIOD.isDuring(period);
    }

    /**
     * A developer is also a resource, this method retrieves it's type.
     *
     * @return The developer ResourceType.
     */
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

    public static final int         WORKDAY_START          = 7;
    public static final int          BREAK_TIME             = 1;
    public static final int         BREAK_PERIOD_START     = 11;
    public static final int         BREAK_PERIOD_END       = 14;
    public static final int         WORKDAY_END            = 17;

    private static final TimePeriod  WORKDAY                = new TimePeriod(LocalTime.of(WORKDAY_START, 0), LocalTime.of(WORKDAY_END, 0));
    private static final TimePeriod  BREAK_PERIOD           = new TimePeriod(LocalTime.of(BREAK_PERIOD_START, 0), LocalTime.of(BREAK_PERIOD_END, 0));

    private static final String      ERROR_NULL_DURING_TIME = "The time to check may not be null.";

    public static final ResourceType DEVELOPER_TYPE         = new TimeConstrainedResourceType(
                                                                    "Developer", new HashSet<>(), new HashSet<>(), false, WORKDAY);

}
