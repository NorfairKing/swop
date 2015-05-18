package be.kuleuven.cs.swop.domain.company;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.company.task.Task;


public class DelegationOffice {
    Set<Delegation> delegations = new HashSet<Delegation>();

    public DelegationOffice() {
        super();
    }
    
    
    protected boolean isValidDelegation(Delegation del){
        if(del == null){
            return false;
        }
        return true;
        //TODO: More checks?
    }
    public Delegation createDelegation(BranchOffice from, BranchOffice to){
        Delegation del = new Delegation(null,from, to);
        if(isValidDelegation(del)){
            delegations.add(del);
        }
        return del;
    }

}
