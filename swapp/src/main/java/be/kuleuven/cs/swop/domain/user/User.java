package be.kuleuven.cs.swop.domain.user;


public abstract class User {
    private String name;
    
    public User(String name){
        setName(name);
    }

    public String getName() {
        return name;
    }
    
    protected boolean canHaveAsName(String name){
        return name != null;
    }

    private void setName(String name) {
        if (!canHaveAsName(name)){ throw new IllegalArgumentException(ERROR_ILLEGAL_NAME);}
        this.name = name;
    }
    
    private static final String ERROR_ILLEGAL_NAME = "Illegal user for name";
    
}