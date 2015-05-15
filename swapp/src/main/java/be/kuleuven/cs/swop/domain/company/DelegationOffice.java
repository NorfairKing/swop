package be.kuleuven.cs.swop.domain.company;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.company.task.Task;


public class DelegationOffice {
    Set<Delegation> delegations = new HashSet<Delegation>();

    public DelegationOffice() {
        super();
    }
    
    
    protected boolean canDelegate(Task task, BranchOffice from, BranchOffice to){
        if(task == null) return false;
        if(from == null) return false;
        if(to == null) return false;
        if(!from.hasTask(task)) return false;
        //TODO: More checks?
        return true;
    }
    public Delegation createDelegation(Task task, BranchOffice from, BranchOffice to){        
    	if(canDelegate(task,from, to)){ 
            Task newTask = to.createDelegatedTask(task.getDescription(),
            		task.getEstimatedDuration(),
            		task.getAcceptableDeviation(),
            		task.getRequirements());
            Delegation del = new Delegation(newTask, from, to);
            delegations.add(del);
            task.delegate(del);
            return del;
        }
        return null;
    }

}
