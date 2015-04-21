package be.kuleuven.cs.swop.domain.company.resource;

import java.util.HashSet;
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
	private Set<ResourceType> dependencies = new HashSet();
	private Set<ResourceType> conflictsWith = new HashSet();

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

	private void setDependencies(Set<ResourceType> dependencies) {
	    if (dependencies == null){
	        dependencies = new HashSet<>();
	    }
	    dependencies.forEach(d -> addDependency(d));
	}

	protected boolean canHaveAsDependency(ResourceType dependency) {
		return dependency != null;
	}
	
	private void addDependency(ResourceType dependency){
	    if (!canHaveAsDependency(dependency)) throw new IllegalArgumentException(ERROR_ILLEGAL_REQUIREMENTS);
	    this.dependencies.add(dependency);
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
	    if (conflictsWith == null)
	    {
	        conflictsWith = new HashSet<>();
	    }
	    conflictsWith.forEach(c -> addConflict(c));
		if(selfConflicting){
			addConflict(this);
		}
	}

	protected boolean canHaveAsConflict(ResourceType conflict) {
		return conflict != null;
	}
	private void addConflict(ResourceType conflict){
		if (!canHaveAsConflict(conflict)) throw new IllegalArgumentException(ERROR_ILLEGAL_CONFLICTS);
		this.conflictsWith.add(conflict);
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
