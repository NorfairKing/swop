package be.kuleuven.cs.swop.domain.company.resource;


import java.io.Serializable;
import java.util.Set;


@SuppressWarnings("serial")
public class Requirement implements Serializable {

    private final int          amount;
    private final ResourceType type;

    @SuppressWarnings("unused")
    private Requirement(){amount = 0; type = null;} //for automatic (de)-serialization
    public Requirement(int amount, ResourceType type) {
        if (type == null) throw new IllegalArgumentException(ERROR_ILLEGAL_TYPE);
        if (amount <= 0) throw new IllegalArgumentException(ERROR_ILLEGAL_AMOUNT);

        this.amount = amount;
        this.type = type;
    }

    /**
     * Retrieve the amount that is required of the type.
     *
     * @return The amount as an integer.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Retrieve the type of resource that is required.
     *
     * @return The ResourceType that is required.
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Checks whether or not the given set of resources satisfies this single requirement.
     *
     * @param resources A Set of Resources to be checked for satisfaction for this
     * Requirement
     * @return True if this Requirement is satisfied.
     */
    public boolean isSatisfiedWith(Set<Resource> resources) {
        int counter = 0;
        for (Resource resource : resources) {
            if (resource.getType().equals(this.getType())) {
                counter++;
            }
            if (counter >= this.getAmount()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + amount;
        result = prime * result + type.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Requirement other = (Requirement) obj;
        if (amount != other.amount) return false;
        if (!type.equals(other.type)) return false;
        return true;
    }

    private static final String ERROR_ILLEGAL_AMOUNT = "Illegal amount for requirement";
    private static final String ERROR_ILLEGAL_TYPE   = "Illegal type for requirement.";

}
