package be.kuleuven.cs.swop.facade;

import be.kuleuven.cs.swop.domain.company.BranchOffice;


public class BranchOfficeWrapper {
    
    private BranchOffice office;
    
    public BranchOfficeWrapper(BranchOffice office) {
        this.office = office;
    }
    
    BranchOffice getOffice() {
        return office;
    }
    
    public String getLocation() {
        return office.getLocation();
    }
    
}
