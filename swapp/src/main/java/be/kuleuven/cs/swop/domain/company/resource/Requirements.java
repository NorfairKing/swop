package be.kuleuven.cs.swop.domain.company.resource;


import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

import com.google.common.collect.ImmutableSet;


@SuppressWarnings("serial")
public class Requirements implements Serializable {

    private final Set<Requirement> reqs = new HashSet<Requirement>();

    public Requirements() {
        this(new HashSet<Requirement>());
    }

    public Requirements(Set<Requirement> requirements) {
        if (requirements != null) {
            for (Requirement req : requirements) {
                reqs.add(req);
            }
        }
    }

    /**
     * Retrieves the Set of Requirements
     *
     * @return An ImmutableSet containing the Requirements.
     */
    public ImmutableSet<Requirement> getRequirementSet() {
        return ImmutableSet.copyOf(this.reqs);
    }

    /**
     * Gets a set of all requirements recursively. So including the requirements of requirements.
     *
     * @return The expanded set of requirements including those found recursivly
     */
    public Requirements getRecursiveRequirements() {
        Set<ResourceType> types = getRecursiveResourceTypes(reqs);
        Set<Requirement> response = new HashSet<Requirement>();
        for (ResourceType type : types) {
            boolean found = false;
            for (Requirement req : reqs) {
                if (type == req.getType()) {
                    response.add(req);
                    found = true;
                    break;
                }
            }
            if (!found) {
                response.add(new Requirement(1, type));
            }
        }
        return new Requirements(response);
    }

    /**
     * Gets als of the resourcesTypes of the requirements including their dependencies.
     *
     * @param reqs The Set of Requirements.
     * @return A Set containing all of the ResourceTypes.
     */
    public static Set<ResourceType> getRecursiveResourceTypes(Set<Requirement> reqs) {
        Set<ResourceType> types = new HashSet<ResourceType>();
        reqs.stream().map(req -> req.getType()).forEach(type -> type.addThisAndDependenciesRecursiveTo(types));
        return types;
    }

    /**
     * Checks to see if in this list there are requirements are conflict with each other
     *
     * @return Whether any conflict
     */
    public boolean hasConflictingRequirements() {
        ImmutableSet<Requirement> recReqs = this.getRecursiveRequirements().getRequirementSet();
        for (Requirement req : recReqs) {
            for (ResourceType conflictType : req.getType().getConflictsWith()) {
                if (conflictType == req.getType()) {
                    if (req.getAmount() > 1) { return true; }
                } else {
                    for (Requirement req2 : recReqs) {
                        if (conflictType == req2.getType()) { return true; }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks the given resources for conflicts and satisfied dependencies.
     *
     * @return True if the dependencies are satisfied and there are no conflicts.
     */
    public static boolean isPossibleResourceSet(Set<Resource> resources) {

        Set<ResourceType> stillRequired = new HashSet<ResourceType>();
        Set<ResourceType> conflicts = new HashSet<ResourceType>();
        Set<ResourceType> typesInSet = new HashSet<ResourceType>();
        for (Resource res : resources) {
            ResourceType type = res.getType();

            // Check if it conflicts with a previous resource
            if (conflicts.contains(type)) { return false; }

            // Check if one of the previous resources conflicts with this one (except self-conflicting resources)
            for (ResourceType con : type.getConflictsWith()) {
                if (typesInSet.contains(con)) {
                    if (con == type) {
                        continue;
                    }
                    return false;
                }
            }

            // Add conflicts for this resource
            conflicts.addAll(type.getConflictsWith());

            // Add requirements for this resource that aren't fulfilled yet
            for (ResourceType req : type.getRequirements()) {
                if (!typesInSet.contains(req)) {
                    stillRequired.add(req);
                }
            }

            // Remove this from the still required resources
            stillRequired.remove(type);

            // Add to the current set of resources
            typesInSet.add(type);

        }

        // Are all the requirements met?
        return stillRequired.isEmpty();
    }

    /**
     * Checks whether or not this is satisfied with the given resources.
     *
     * @param resources A Set of Resources that will be checked.
     * @return True if the resources will satisfy this.
     */
    public boolean isSatisfiedWith(Set<Resource> resources) {
    	for (Requirement req: reqs) {
    		if (!req.isSatisfiedWith(resources)) {
    			return false;
    		}
    	}
    	return true;
    }

    /**
     * Checks to see if in this set of requirements there are two or more with the same type
     *
     * @return Whether there are doubles
     */
    public boolean hasDoubleTypesRequirements() {
        for (Requirement req1 : reqs) {
            for (Requirement req2 : reqs) {
                if (req1 != req2 && req1.getType() == req2.getType()) { return true; }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((reqs == null) ? 0 : reqs.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Requirements other = (Requirements) obj;
        if (reqs == null) {
            if (other.reqs != null) return false;
        } else if (!reqs.equals(other.reqs)) return false;
        return true;
    }
}
