package be.kuleuven.cs.swop.domain.resource;

import java.util.Set;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.TimePeriod;

@SuppressWarnings("serial")
public class ResourceType implements Serializable {

	private String            name;
	private Set<ResourceType> requirements;
	private Set<ResourceType> conflictsWith;
	private TimePeriod        dailyAvailability;
	private boolean           hasAvailabilityPeriod = false;

	public ResourceType(String name, Set<ResourceType> requirements, Set<ResourceType> conflicts, boolean selfConflicting) {
		this.setName(name);
		this.setRequirements(requirements);
		this.setConflictsWith(conflicts, selfConflicting);
	}

	public ResourceType(String name, Set<ResourceType> requirements, Set<ResourceType> conflicts, boolean selfConflicting, TimePeriod dailyAvailability) {
		this(name, requirements, conflicts, selfConflicting);
		this.setDailyAvailability(dailyAvailability);
	}

	public ImmutableSet<ResourceType> getRequirements() {
		return ImmutableSet.copyOf(this.requirements);
	}

	private void setRequirements(Set<ResourceType> requirements) {
		if (!canHaveAsRequirements(requirements)) throw new IllegalArgumentException(ERROR_ILLEGAL_REQUIREMENTS);
		this.requirements = requirements;
	}

	protected boolean canHaveAsRequirements(Set<ResourceType> requirements) {
		return requirements != null;
	}

    public void addThisAndRequirementsRecursiveTo(Set<ResourceType> resourceSet) {
        resourceSet.add(this);
        this.getRequirements().stream().filter( req -> !resourceSet.contains(req))
                                       .forEach( req -> req.addThisAndRequirementsRecursiveTo(resourceSet));
    }

	public ImmutableSet<ResourceType> getConflictsWith() {
		return ImmutableSet.copyOf(this.conflictsWith);
	}

	private void setConflictsWith(Set<ResourceType> conflictsWith, boolean selfConflicting) {
		if (!canHaveAsConflictsWith(conflictsWith)) throw new IllegalArgumentException(ERROR_ILLEGAL_CONFLICTS);
		this.conflictsWith = conflictsWith;
		if(selfConflicting){
			this.conflictsWith.add(this);
		}
	}

	private void setDailyAvailability(TimePeriod availability) {
		if(!canHaveAsAvailability(availability)) throw new IllegalArgumentException(ERROR_ILLEGAL_AVAILABILITY);
		this.dailyAvailability = availability;
		this.hasAvailabilityPeriod = true;
	}

	protected boolean canHaveAsAvailability(TimePeriod availability) {
		return availability != null;
	}

	protected boolean canHaveAsConflictsWith(Set<ResourceType> conflictsWith) {
		return conflictsWith != null;
	}

	private void setName(String name) {
		if (!canHaveAsName(name)) throw new IllegalArgumentException(ERROR_ILLEGAL_NAME);
		this.name = name;
	}

	protected boolean canHaveAsName(String name) {
		return name != null && !name.isEmpty();
	}

	public String getName() {
		return this.name;
	}

	private TimePeriod getDailyAvailability() {
		return this.dailyAvailability;
	}

	
	public boolean isAvailableDuring(LocalTime time) {
		if (this.hasAvailabilityPeriod) {
			return this.getDailyAvailability().isDuring(time);
		} else {
			return true;
		}
	}
	
	public boolean isAvailableDuring(LocalDateTime date){
		return isAvailableDuring(LocalTime.from(date));
	}

	private static final String ERROR_ILLEGAL_REQUIREMENTS = "Illegal requirement set for resource type.";
	private static final String ERROR_ILLEGAL_CONFLICTS    = "Illegal conflict set for resource type.";
	private static final String ERROR_ILLEGAL_NAME         = "Illegal name for resource type.";
	private static final String ERROR_ILLEGAL_AVAILABILITY = "Illegal daily availability TimePeriod for resource type.";
}
