package be.kuleuven.cs.swop.facade;

import be.kuleuven.cs.swop.domain.resource.Resource;

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
    
    private static final String ERROR_ILLEGAL_RESOURCE = "Invalid resource for resource wrapper.";

}
