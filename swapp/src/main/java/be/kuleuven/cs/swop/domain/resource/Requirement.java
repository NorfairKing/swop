package be.kuleuven.cs.swop.domain.resource;


import java.io.Serializable;


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

    private static String ERROR_ILLEGAL_AMOUNT = "Illegal amount for requirement";
    private static String ERROR_ILLEGAL_TYPE   = "Illegal type for requirement.";

}
