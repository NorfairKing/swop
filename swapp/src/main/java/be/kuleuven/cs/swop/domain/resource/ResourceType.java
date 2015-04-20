package be.kuleuven.cs.swop.domain.resource;

import java.util.Set;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimePeriod;

@SuppressWarnings("serial")
public class ResourceType implements Serializable {

	private String            name;
	private Set<ResourceType> dependencies;
	private Set<ResourceType> conflictsWith;

	/**
	 * 
	 * @param name
	 * @param dependencies
	 * @param conflicts
	 * @param selfConflicting want alsge nog niet bestaat kuntge uzelf nie toevoegen aan de set van conflicterende types
	 */
	public ResourceType(String name, Set<ResourceType> dependencies, Set<ResourceType> conflicts, boolean selfConflicting) {
		this.setName(name);
		this.setDependencies(dependencies);
		this.setConflictsWith(conflicts, selfConflicting);
	}

	public ImmutableSet<ResourceType> getRequirements() {
		return ImmutableSet.copyOf(this.dependencies);
	}

	private void setDependencies(Set<ResourceType> dependenciess) {
		if (!canHaveAsDependencies(dependenciess)) throw new IllegalArgumentException(ERROR_ILLEGAL_REQUIREMENTS);
		this.dependencies = dependenciess;
	}

	protected boolean canHaveAsDependencies(Set<ResourceType> dependencies) {
		return dependencies != null;
	}

    public void addThisAndDependenciesRecursiveTo(Set<ResourceType> resourceSet) {
        resourceSet.add(this);
        this.getRequirements().stream().filter( req -> !resourceSet.contains(req))
                                       .forEach( req -> req.addThisAndDependenciesRecursiveTo(resourceSet));
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

	
	public boolean isAvailableDuring(LocalTime time) {
        return true;
	}
	
    public boolean isAvailableDuring(LocalDateTime date){
		return isAvailableDuring(LocalTime.from(date));
    }
	public boolean isAvailableDuring(TimePeriod period) {
	    return true;
	}

	public boolean isAvailableDuring(DateTimePeriod period){
        return true;
	}

	private static final String ERROR_ILLEGAL_REQUIREMENTS = "Illegal requirement set for resource type.";
	private static final String ERROR_ILLEGAL_CONFLICTS    = "Illegal conflict set for resource type.";
	private static final String ERROR_ILLEGAL_NAME         = "Illegal name for resource type.";
}
