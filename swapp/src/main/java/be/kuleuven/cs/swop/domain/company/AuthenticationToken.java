package be.kuleuven.cs.swop.domain.company;

import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.domain.company.user.User;


public class AuthenticationToken {
    
    private final BranchOffice office;
    private final User user;
    
    AuthenticationToken(BranchOffice office, User user) {
        this.office = office;
        this.user = user;
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
    
}
