package be.kuleuven.cs.swop.domain.company;

import be.kuleuven.cs.swop.domain.company.user.User;
import be.kuleuven.cs.swop.domain.company.user.Developer;


public class Authenticator {

    /**
     * Authenticates a user and creates a AuthenticationToken.
     *
     * @param office The BranchOffice for which the user will be authenticated.
     * @param user The User that will be authenticated.
     *
     * @throws IllegalArgumentException If the user doesn't belong to the BranchOffice.
     * @return The AuthenticationToken.
     */
    public AuthenticationToken createFor(BranchOffice office, User user) {
        if (user instanceof Developer && !office.getDevelopers().contains((Developer) user)) throw new IllegalArgumentException(ERROR_ILLEGAL_USER);
        return new AuthenticationToken(office, user);
    }

    private static final String ERROR_ILLEGAL_USER = "The User does not belong to the BranchOffice";

}
