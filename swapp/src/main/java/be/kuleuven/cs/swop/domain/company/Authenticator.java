package be.kuleuven.cs.swop.domain.company;

import be.kuleuven.cs.swop.domain.company.user.User;


public class Authenticator {
    
    public AuthenticationToken createFor(BranchOffice office, User user) {
        // TODO check if the user is from the office, otherwise throw IllegalArgumentException.
        return new AuthenticationToken(office, user);
    }
    
}
