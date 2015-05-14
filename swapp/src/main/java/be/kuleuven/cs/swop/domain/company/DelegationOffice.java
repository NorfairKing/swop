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
        //TODO: More checks?
        return true;
    }
    public Delegation createDelegation(Task task, BranchOffice from, BranchOffice to){
        Delegation del = new Delegation(task,from, to);
        
        if(isValidDelegation(del)){
            delegations.add(del);
            return del;
        }
        return null;
    }

}
