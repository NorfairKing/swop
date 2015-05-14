package be.kuleuven.cs.swop.domain.company.resource;

import java.time.LocalTime;
import java.util.Set;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;


@SuppressWarnings("serial")
public class TimeConstrainedResourceType extends ResourceType {

    private TimePeriod        dailyAvailability;
    
    @SuppressWarnings("unused")
    private TimeConstrainedResourceType() { super(); }
    
    public TimeConstrainedResourceType(String name, Set<ResourceType> requirements, Set<ResourceType> conflicts, boolean selfConflicting, TimePeriod dailyAvailability) {
        super(name, requirements, conflicts, selfConflicting);
        setDailyAvailability(dailyAvailability);
    }

	private void setDailyAvailability(TimePeriod availability) {
		if(!canHaveAsAvailability(availability)) throw new IllegalArgumentException(ERROR_ILLEGAL_AVAILABILITY);
		dailyAvailability = availability;
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

    private static final String ERROR_ILLEGAL_AVAILABILITY = "Illegal daily availability TimePeriod for resource type.";
}
