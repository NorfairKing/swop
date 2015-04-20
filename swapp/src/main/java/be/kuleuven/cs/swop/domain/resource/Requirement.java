package be.kuleuven.cs.swop.domain.resource;


import java.io.Serializable;
import java.util.Set;


@SuppressWarnings("serial")
public class Requirement implements Serializable {

    private int          amount;
    private ResourceType type;

    public Requirement(int amount, ResourceType type) {
        setAmount(amount);
        setType(type);
    }

    public int getAmount() {
        return amount;
    }

    protected boolean canHaveAsAmount(int amount) {
        return amount > 0;
    }

    public void setAmount(int amount) {
        if (!canHaveAsAmount(amount)) { throw new IllegalArgumentException(ERROR_ILLEGAL_AMOUNT); }
        this.amount = amount;
    }

    public ResourceType getType() {
        return type;
    }

    protected boolean canHaveAsType(ResourceType type) {
        return type != null;
    }

    public void setType(ResourceType type) {
        if (!canHaveAsType(type)) { throw new IllegalArgumentException(ERROR_ILLEGAL_TYPE); }
        this.type = type;
    }

    public boolean isSatisfiedWith(Set<Resource> resources) {
        int counter = 0;
        for (Resource resource : resources) {
            if (resource.getType() == this.getType()) {
                counter++;
            }
            if (counter > this.getAmount()) {
                return true;
            }
        }
        return false;
    }

    private static String ERROR_ILLEGAL_AMOUNT = "Illegal amount for requirement";
    private static String ERROR_ILLEGAL_TYPE   = "Illegal type for requirement.";

}
