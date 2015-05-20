package be.kuleuven.cs.swop.domain.company.resource;


import java.io.Serializable;

import be.kuleuven.cs.swop.domain.DateTimePeriod;


@SuppressWarnings("serial")
public class Resource implements Serializable {

    private final ResourceType type;
    private final String name;

    public Resource(ResourceType type, String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException(ERROR_ILLEGAL_NAME);
        if (type == null) throw new IllegalArgumentException(ERROR_ILLEGAL_TYPE);

        this.type = type;
        this.name = name;
    }

    public ResourceType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isOfType(ResourceType type) {
        return type == this.getType();
    }
    
    public boolean canTakeBreakDuring(DateTimePeriod period) {
        return false;
    }

    private static final String ERROR_ILLEGAL_TYPE = "Illegal resource type for resource.";
    private static final String ERROR_ILLEGAL_NAME = "Illegal name for resource.";
}
