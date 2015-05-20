package be.kuleuven.cs.swop.facade;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.resource.Resource;

public class ResourceWrapper {
    
    private Resource resource;
    
    public ResourceWrapper(Resource resource){
        setResource(resource);
    }

    Resource getResource() {
        return resource;
    }
    
    protected boolean canHaveAsResource(Resource resource){
        return resource != null;
    }

    public void setResource(Resource resource) {
        if (!canHaveAsResource(resource)){throw new IllegalArgumentException(ERROR_ILLEGAL_RESOURCE);}
        this.resource = resource;
    }

    public ResourceTypeWrapper getType() {
        return new ResourceTypeWrapper(resource.getType());
    }

    public String getName() {
        return resource.getName();
    }
    
    public boolean canTakeBreakDuring(DateTimePeriod period) {
        return resource.canTakeBreakDuring(period);
    }
    
    @Override
    public boolean equals(Object o){
    	if(!(o instanceof ResourceWrapper)){
    		return false;
    	}
    	return this.getResource().equals(((ResourceWrapper) o).getResource());
    }
    
    @Override
    public int hashCode(){
    	return this.resource.hashCode() + "wrapper".hashCode();
    	
    }
    
    private static final String ERROR_ILLEGAL_RESOURCE = "Invalid resource for resource wrapper.";

}
