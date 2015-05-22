package be.kuleuven.cs.swop.domain.company.resource;


import java.io.Serializable;

import be.kuleuven.cs.swop.domain.DateTimePeriod;


@SuppressWarnings("serial")
public class Resource implements Serializable {

    private final ResourceType type;
    private final String name;

    @SuppressWarnings("unused")
    private Resource() { type = null; name = null; } //for automatic (de)-serialization

    public Resource(ResourceType type, String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException(ERROR_ILLEGAL_NAME);
        if (type == null) throw new IllegalArgumentException(ERROR_ILLEGAL_TYPE);

        this.type = type;
        this.name = name;
    }

    /**
     * Retrieve the type of this resource.
     *
     * @return The ResourceType of this Resource.
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Retrieve the name of this resource.
     *
     * @return A String that is the name of this Resource.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks whether or not this resource is of the given type.
     *
     * @param type The to be checked ResouceType.
     * @return True if this Resource is of the given ResourceType.
     */
    public boolean isOfType(ResourceType type) {
        return this.getType().equals(type);
    }

    /**
     * Checks if this resource can take a break during the given period.
     * A normal non developer resource will never be able to take a break.
     *
     * @param period The DateTimePeriod that will be checked.
     * @return True if the period satisfies.
     */
    public boolean canTakeBreakDuring(DateTimePeriod period) {
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name != null) ? name.hashCode() : 0 );
        result = prime * result + ((type != null) ? type.hashCode() : 0 );
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Resource other = (Resource) obj;
        if (!name.equals(other.name)) return false;
        if (!type.equals(other.type)) return false;
        return true;
    }

    private static final String ERROR_ILLEGAL_TYPE = "Illegal resource type for resource.";
    private static final String ERROR_ILLEGAL_NAME = "Illegal name for resource.";
}
