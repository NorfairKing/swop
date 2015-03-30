package be.kuleuven.cs.swop.domain.resource;


import java.io.Serializable;
import java.util.Set;

import com.google.common.collect.ImmutableSet;


public class ResourceType implements Serializable {

    private String            name;
    private Set<ResourceType> requirements;
    private Set<ResourceType> conflictsWith;

    public ResourceType(String name, Set<ResourceType> requirements, Set<ResourceType> conflicts) {
        this.setName(name);
        this.setRequirements(requirements);
        this.setConflictsWith(conflicts);
    }

    public Set<ResourceType> getRequirements() {
        return ImmutableSet.copyOf(this.requirements);
    }

    private void setRequirements(Set<ResourceType> requirements) {
        if (!canHaveAsRequirements(requirements)) throw new IllegalArgumentException(ERROR_ILLEGAL_REQUIREMENTS);
        this.requirements = requirements;
    }

    protected boolean canHaveAsRequirements(Set<ResourceType> requirements) {
        return requirements != null;
    }

    public Set<ResourceType> getConflictsWith() {
        return ImmutableSet.copyOf(this.conflictsWith);
    }

    private void setConflictsWith(Set<ResourceType> conflictsWith) {
        if (!canHaveAsConflictsWith(conflictsWith)) throw new IllegalArgumentException(ERROR_ILLEGAL_CONFLICTS);
        this.conflictsWith = conflictsWith;
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

    private static final String ERROR_ILLEGAL_REQUIREMENTS = "Illegal requirement set for resource type.";
    private static final String ERROR_ILLEGAL_CONFLICTS    = "Illegal conflict set for resource type.";
    private static final String ERROR_ILLEGAL_NAME         = "Illegal name for resource type.";
}
