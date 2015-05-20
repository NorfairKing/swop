package be.kuleuven.cs.swop.domain.company.user;


@SuppressWarnings("serial")
public class Manager implements User {
    private final String name;

    public Manager(String name) {
        if (name == null) throw new IllegalArgumentException(ERROR_ILLEGAL_NAME);
        
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    private static final String ERROR_ILLEGAL_NAME = "Illegal name for Manager";

}
