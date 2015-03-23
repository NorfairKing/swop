package be.kuleuven.cs.swop.domain.resource;


public class Resource {

    private ResourceType type;

    public Resource(ResourceType type) {
        setType(type);
    }

    public ResourceType getType() {
        return type;
    }

    protected boolean canHaveAsType(ResourceType type) {
        return type != null;
    }

    private void setType(ResourceType type) {
        if (!canHaveAsType(type)) { throw new IllegalArgumentException(ERROR_ILLEGAL_TYPE); }
        this.type = type;
    }

    private static final String ERROR_ILLEGAL_TYPE = "Illegal resource type for resource.";
}
