package be.kuleuven.cs.swop.facade;

import be.kuleuven.cs.swop.domain.company.resource.ResourceType;


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
    
    @Override
    public boolean equals(Object o){
    	if(!(o instanceof ResourceTypeWrapper)){
    		return false;
    	}
    	return this.getType().equals(((ResourceTypeWrapper) o).getType());
    }
    
    @Override
    public int hashCode(){
    	return this.type.hashCode() + "wrapper".hashCode();
    	
    }

    private static final String ERROR_ILLEGAL_TYPE = "Invalid resource type for resource type wrapper.";
}
