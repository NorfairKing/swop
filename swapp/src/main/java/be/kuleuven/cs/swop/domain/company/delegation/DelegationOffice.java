package be.kuleuven.cs.swop.domain.company.delegation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.company.BranchOffice;
import be.kuleuven.cs.swop.domain.company.Company;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.task.Task;


@SuppressWarnings("serial")
public class DelegationOffice implements Serializable {
	
    private final Set<Delegation> delegations;
    private final Company company;
    private final Set<Delegation> delegationBuffer;

    public DelegationOffice(Company company) {
        this.company = company;
        this.delegationBuffer = new HashSet<Delegation>();
        this.delegations = new HashSet<Delegation>();
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
            Delegation del = new Delegation(task, from, to);
            task.delegate(del);
            if(company.isInASimulationFor(from) || company.isInASimulationFor(to)){
            	delegationBuffer.add(del);
            }else{
            	commitDelegation(del);
            }
            return del;
        }
        return null;
    }
    
    private void commitDelegation(Delegation del){
        Task task = del.getDelegatedTask();
        Task newTask = del.getNewOffice().createDelegationTask(task.getDescription(),
        		task.getEstimatedDuration(),
        		task.getAcceptableDeviation(),
        		new Requirements(task.getRequirements()));
        del.setDelegationTask(newTask);
        delegations.add(del);

    }
    
    public void processBuffer(){
        Set<Delegation> toRemove = new HashSet<Delegation>();
    	for(Delegation del : delegationBuffer ){
    	    if(!company.isInASimulationFor(del.getOldOffice()) && !company.isInASimulationFor(del.getNewOffice())){
    	           commitDelegation(del);
    	           toRemove.add(del);
    	    }
    	}
    	delegationBuffer.removeAll(toRemove);
    }
    
    public void rollbackSimulation(BranchOffice office){
        for(Delegation del : delegationBuffer ){
            if(office == del.getOldOffice()){
                delegationBuffer.remove(del);
            }
        }
        processBuffer();
        
    }
    
    

}
