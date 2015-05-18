package be.kuleuven.cs.swop.domain.company.user;

import java.io.Serializable;


@SuppressWarnings("serial")
public abstract class User implements Serializable{
    private final String name;
    
    public User(String name){
        if (name == null) throw new IllegalArgumentException(ERROR_ILLEGAL_NAME);
        
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    private static final String ERROR_ILLEGAL_NAME = "Illegal user for name";
    
}
