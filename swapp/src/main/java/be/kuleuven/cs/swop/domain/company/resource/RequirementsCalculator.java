package be.kuleuven.cs.swop.domain.company.resource;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * A collection of methods to get information regarding requirements
 * They mostly work on sets of requirement where it made no sense to call it directly on a single requirement
 */
public final class RequirementsCalculator {
    
    /**
     * Gets a set of all requirements recursively. So including the requirements of requirements.
     * @param reqs The initial requirements
     * @return The expanded set of requirements including those found recursivly
     */
    public static ImmutableSet<Requirement> getRecursiveRequirements(Set<Requirement> reqs) {
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
        return ImmutableSet.copyOf(response);
    }
    
    public static Set<ResourceType> getRecursiveResourceTypes(Set<Requirement> reqs) {
        Set<ResourceType> types = new HashSet<ResourceType>();
        reqs.stream().map(req -> req.getType()).forEach(type -> type.addThisAndDependenciesRecursiveTo(types));
        return types;
    }

    /**
     * Checks to see if in this list there are requirements are conflict with each other
     * 
     * @param reqs The requirements to check
     * @return Whether any conflict
     */
    public static boolean hasConflictingRequirements(Set<Requirement> reqs) {
        Set<Requirement> recReqs = getRecursiveRequirements(reqs);
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
    
    public static boolean isPossibleResourceSet(Set<Resource> resources){
    	
    	Set<ResourceType> stillRequired = new HashSet<ResourceType>();
    	Set<ResourceType> conflicts = new HashSet<ResourceType>();    	
    	Set<ResourceType> typesInSet = new HashSet<ResourceType>();
    	for(Resource res : resources){
    		ResourceType type = res.getType();
    		
    		// Check if it conflicts with a previous resource
    		if(conflicts.contains(type)){
    			return false;
    		}
    		
    		// Check if one of the previous resources conflicts with this one (except self-conflicting resources)
    		for(ResourceType con : type.getConflictsWith()){
    			if(typesInSet.contains(con)){
    				if(con == type){
    					continue;
    				}
    				return false;
    			}
    		}
    		
    		// Add conflicts for this resource
    		conflicts.addAll(type.getConflictsWith());
    		
    		// Add requirements for this resource that aren't fulfilled yet
    		for(ResourceType req : type.getRequirements()){
    			if(!typesInSet.contains(req)){
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
     * Checks to see if in this set of requirements there are two or more with the same type
     * 
     * @param reqs Requirements to check
     * @return Whether there are doubles
     */
    public static boolean hasDoubleTypesRequirements(Set<Requirement> reqs) {
        for (Requirement req1 : reqs) {
            for (Requirement req2 : reqs) {
                if (req1 != req2 && req1.getType() == req2.getType()) { return true; }
            }
        }
        return false;
    }
    
}
