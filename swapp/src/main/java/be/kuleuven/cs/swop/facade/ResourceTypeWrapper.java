package be.kuleuven.cs.swop.facade;

import be.kuleuven.cs.swop.domain.resource.ResourceType;


public class ResourceTypeWrapper {
    private ResourceType type;
    
    public ResourceTypeWrapper(ResourceType resource){
        setType(resource);
    }

    ResourceType getType() {
        return type;
    }
    
    protected boolean canHaveAsType(ResourceType type){
        return type != null;
    }

    public void setType(ResourceType type) {
        if (!canHaveAsType(type)){throw new IllegalArgumentException(ERROR_ILLEGAL_TYPE);}
        this.type = type;
    }
    
    public String getName() {
        return type.getName();
    }

    private static final String ERROR_ILLEGAL_TYPE = "Invalid resource type for resource type wrapper.";
}
