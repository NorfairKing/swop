package be.kuleuven.cs.swop.facade;

import java.util.Set;

import be.kuleuven.cs.swop.domain.company.BranchOffice;
import be.kuleuven.cs.swop.domain.company.user.User;


public class BranchOfficeWrapper {

    private final BranchOffice office;
    
    public BranchOfficeWrapper(BranchOffice office) {
        this.office = office;
    }
    
    BranchOffice getOffice() {
        return office;
    }
    
    public String getLocation() {
        return office.getLocation();
    }
    
    public Set<User> getUsers() {
        return office.getUsers();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((office == null) ? 0 : office.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BranchOfficeWrapper other = (BranchOfficeWrapper) obj;
        if (office == null) {
            if (other.office != null) return false;
        } else if (!office.equals(other.office)) return false;
        return true;
    }

    
}
