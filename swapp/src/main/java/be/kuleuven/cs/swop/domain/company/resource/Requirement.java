package be.kuleuven.cs.swop.domain.company.resource;


import java.io.Serializable;
import java.util.Set;


@SuppressWarnings("serial")
public class Requirement implements Serializable {

    private final int          amount;
    private final ResourceType type;

    public Requirement(int amount, ResourceType type) {
        if (type == null) throw new IllegalArgumentException(ERROR_ILLEGAL_TYPE);
        if (amount <= 0) throw new IllegalArgumentException(ERROR_ILLEGAL_AMOUNT);
        
        this.amount = amount;
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public ResourceType getType() {
        return type;
    }

    public boolean isSatisfiedWith(Set<Resource> resources) {
        int counter = 0;
        for (Resource resource : resources) {
            if (resource.getType() == this.getType()) {
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
