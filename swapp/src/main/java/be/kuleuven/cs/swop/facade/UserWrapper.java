package be.kuleuven.cs.swop.facade;

import be.kuleuven.cs.swop.domain.company.user.User;


public class UserWrapper {

    private User user;

    public UserWrapper(User user) {
        setUser(user);
    }

    User getUser() {
        return user;
    }

    protected boolean canHaveAsUser(User user) {
        return user != null;
    }

    private void setUser(User user) {
        if (!canHaveAsUser(user)) { throw new IllegalArgumentException(ERROR_ILLEGAL_USER); }
        this.user = user;
    }
    
    public String getName() {
        return user.getName();
    }

    private static final String ERROR_ILLEGAL_USER = "Invalid user for user wrapper";
    
}
