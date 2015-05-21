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

	private final String            name;
	private final Set<ResourceType> dependencies = new HashSet<ResourceType>();
	private final Set<ResourceType> conflictsWith = new HashSet<ResourceType>();

    protected ResourceType() { this(" "); }
	/**
	 * Simple constructor for a resource type that doesn't have dependencies and doesn't conflict with anything
	 * @param name The name for this resource type
	 */
	public ResourceType(String name){
	    this(name,false);
	}

    /**
     * Simple constructor for a resource type that might conflict with itself, but nothing else.
     * It also doesn't have any dependencies.
     * 
     * Selfconlfiction is used because, as we say in Belgium,
     *     "als ge nog niet bestaat kuntge uzelf nie toevoegen aan de set van conflicterende types he"
     * 
     * @param name The name for the resource type
     * @param selfConflicting Whether it conflicts with itself
     */
    public ResourceType(String name, boolean selfConflicting){
        this(name,null,null,selfConflicting);
    }
    
	/**
	 * Constructor with all possible info
	 * 
	 * @param name The resource's name
	 * @param dependencies Dependencies
	 * @param conflicts Conflicts
	 * @param selfConflicting Whether it conflicts with itself
	 */
	public ResourceType(String name, Set<ResourceType> dependencies, Set<ResourceType> conflicts, boolean selfConflicting) {
		if (name == null || name.isEmpty()) throw new IllegalArgumentException(ERROR_ILLEGAL_NAME);
        
		this.name = name;
		this.setDependencies(dependencies);
		this.setConflictsWith(conflicts, selfConflicting);
	}

	public ImmutableSet<ResourceType> getRequirements() {
		return ImmutableSet.copyOf(this.dependencies);
	}

	private void setDependencies(Set<ResourceType> dependencies) {
	    if (dependencies != null){
	        dependencies.forEach(d -> addDependency(d));
	    }
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

	public String getName() {
		return this.name;
	}

	
	public boolean isAvailableDuring(LocalTime time) {
    	if(time == null){
    		throw new IllegalArgumentException(ERROR_NULL_DURING_TIME);
    	}
        return true;
	}
    public boolean isAvailableDuring(LocalDateTime date){
    	if(date == null){
    		throw new IllegalArgumentException(ERROR_NULL_DURING_TIME);
    	}
		return isAvailableDuring(LocalTime.from(date));
    }
	public boolean isAvailableDuring(TimePeriod period) {
    	if(period == null){
    		throw new IllegalArgumentException(ERROR_NULL_DURING_PERIOD);
    	}
	    return true;
	}

	public boolean isAvailableDuring(DateTimePeriod period){
    	if(period == null){
    		throw new IllegalArgumentException(ERROR_NULL_DURING_PERIOD);
    	}
        return true;
	}

    @Override
    public int hashCode() {
        Set<ResourceType> conflictsWithWithoutThis = new HashSet<ResourceType>();
        
        if (conflictsWith != null) {
            for(ResourceType type : conflictsWith){
                if(type != this){
                    conflictsWithWithoutThis.add(type);
                }
            }
        }
        
        final int prime = 31;
        int result = 1;
        result = prime * result + ((conflictsWithWithoutThis == null) ? 0 : conflictsWithWithoutThis.hashCode());
        result = prime * result + ((dependencies == null) ? 0 : dependencies.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ResourceType other = (ResourceType) obj;
        if (conflictsWith == null) {
            if (other.conflictsWith != null) return false;
        } else if (!conflictsWith.equals(other.conflictsWith)) return false;
        if (dependencies == null) {
            if (other.dependencies != null) return false;
        } else if (!dependencies.equals(other.dependencies)) return false;
        if (!name.equals(other.name)) return false;
        return true;
    }

	private static final String ERROR_ILLEGAL_REQUIREMENTS = "Illegal requirement set for resource type.";
	private static final String ERROR_ILLEGAL_CONFLICTS    = "Illegal conflict set for resource type.";
	private static final String ERROR_ILLEGAL_NAME         = "Illegal name for resource type.";
	
    private static final String ERROR_NULL_DURING_TIME  = "The time to check may not be null.";
    private static final String ERROR_NULL_DURING_PERIOD  = "The period to check may not be null.";


}
