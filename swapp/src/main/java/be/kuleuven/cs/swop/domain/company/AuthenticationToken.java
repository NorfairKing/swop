package be.kuleuven.cs.swop.domain.company;

import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.domain.company.user.User;


public class AuthenticationToken {
    
    private final BranchOffice office;
    private final User user;
    
    AuthenticationToken(BranchOffice office, User user) {
        if (!canHaveAsOffice(office)){throw new IllegalArgumentException(ERROR_ILLEGAL_OFFICE);}
        if (!canHaveAsUser(user)){throw new IllegalArgumentException(ERROR_ILLEGAL_USER);}
        this.office = office;
        this.user = user;
    }
    
    private boolean canHaveAsUser(User user) {
        return user != null;
    }

    private boolean canHaveAsOffice(BranchOffice office) {
        return office != null;
    }

    BranchOffice getOffice() {
        return office;
    }
    
    User getUser() {
        return user;
    }
    
    Developer getAsDeveloper() {
        return (Developer)user;
    }
    
    public boolean isDeveloper() {
        return user instanceof Developer;
    }
    
    private static final String ERROR_ILLEGAL_OFFICE = "Invalid office for authentication token.";
    private static final String ERROR_ILLEGAL_USER = "Invalid user for authentication token.";
}
