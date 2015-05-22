package be.kuleuven.cs.swop.domain.company.user;

import java.io.Serializable;

public interface User extends Serializable{

    /**
     * Retrieves the name of this manager.
     *
     * @return A String with it's name.
     */
    public String getName();

}
