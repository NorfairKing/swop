package be.kuleuven.cs.swop.domain.company.resource;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Resource implements Serializable{

    private ResourceType type;
    private String       name;

    public Resource(ResourceType type, String name) {
        setType(type);
        setName(name);
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

    public String getName() {
        return name;
    }

    protected boolean canHaveAsName(String name) {
        return name != null && !name.isEmpty();
    }

    private void setName(String name) {
        if (!canHaveAsName(name)) { throw new IllegalArgumentException(ERROR_ILLEGAL_NAME); }
        this.name = name;
    }

    public boolean isOfType(ResourceType type) {
        return type == this.getType();
    }

    private static final String ERROR_ILLEGAL_TYPE = "Illegal resource type for resource.";
    private static final String ERROR_ILLEGAL_NAME = "Illegal name for resource.";
}
