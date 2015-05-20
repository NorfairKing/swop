package be.kuleuven.cs.swop.domain.company.resource;


import java.time.LocalTime;
import java.util.Set;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;


@SuppressWarnings("serial")
public class TimeConstrainedResourceType extends ResourceType {

    private final TimePeriod dailyAvailability;

    @SuppressWarnings("unused")
    private TimeConstrainedResourceType() { dailyAvailability = null; } //for automatic (de)-serialization
    
    public TimeConstrainedResourceType(String name, Set<ResourceType> requirements, Set<ResourceType> conflicts, boolean selfConflicting, TimePeriod dailyAvailability) {
        super(name, requirements, conflicts, selfConflicting);
        if (!canHaveAsAvailability(dailyAvailability)) { throw new IllegalArgumentException(ERROR_ILLEGAL_AVAILABILITY); }
        this.dailyAvailability = dailyAvailability;
    }

    protected boolean canHaveAsAvailability(TimePeriod availability) {
        return availability != null;
    }

    private TimePeriod getDailyAvailability() {
        return this.dailyAvailability;
    }

    public boolean isAvailableDuring(LocalTime time) {
        return this.getDailyAvailability().isDuring(time);
    }

    public boolean isAvailableDuring(DateTimePeriod period) {
        return this.getDailyAvailability().isDuring(period);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((dailyAvailability == null) ? 0 : dailyAvailability.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        TimeConstrainedResourceType other = (TimeConstrainedResourceType) obj;
        if (dailyAvailability == null) {
            if (other.dailyAvailability != null) return false;
        } else if (!dailyAvailability.equals(other.dailyAvailability)) return false;
        return true;
    }

    private static final String ERROR_ILLEGAL_AVAILABILITY = "Illegal daily availability TimePeriod for resource type.";
}
