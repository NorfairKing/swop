package be.kuleuven.cs.swop.domain.company;

import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.domain.company.user.User;


public class AuthenticationToken {

    private final BranchOffice office;
    private final User user;

    /**
     * Constructor, creates a token authenticating the given user to the given office.
     *
     * @param office The BranchOffice for which the given user is authenticated.
     * @param user The User that is authenticated.
     */
    AuthenticationToken(BranchOffice office, User user) {
        if (!canHaveAsOffice(office)) throw new IllegalArgumentException(ERROR_ILLEGAL_OFFICE);
        if (!canHaveAsUser(user)) throw new IllegalArgumentException(ERROR_ILLEGAL_USER);
        this.office = office;
        this.user = user;
    }

    private boolean canHaveAsUser(User user) {
        return user != null;
    }

    private boolean canHaveAsOffice(BranchOffice office) {
        return office != null;
    }

    /**
     * Retrieves the office for which this token authenticates.
     *
     * @return The BranchOffice for which this token authenticates.
     */
    BranchOffice getOffice() {
        return office;
    }

    /**
     * Retrieves the authenticated user.
     *
     * @return the Authenticated User.
     */
    User getUser() {
        return user;
    }

    /**
     * Retrieves the authenticated developer.
     *
     * @return the Authenticated Developer.
     */
    Developer getAsDeveloper() {
        return (Developer)user;
    }

    /**
     * Checks whether of not the authtenticated user is a developer
     *
     * @return True if the user is a developer.
     */
    public boolean isDeveloper() {
        return user instanceof Developer;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + office.hashCode();
        result = prime * result + user.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AuthenticationToken other = (AuthenticationToken) obj;
        if (!office.equals(other.office)) return false;
        if (!user.equals(other.user)) return false;
        return true;
    }

    private static final String ERROR_ILLEGAL_OFFICE = "Invalid office for authentication token.";
    private static final String ERROR_ILLEGAL_USER = "Invalid user for authentication token.";
}
