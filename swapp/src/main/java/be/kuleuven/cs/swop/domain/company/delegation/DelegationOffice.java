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

    /**
     * Constructor for the delegation office.
     *
     * @param company The Company of this DelegationOffice.
     */
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

    /**
     * Creates a new delegation of a Task.
     *
     * @param task The Task that will be delegated.
     * @param from The BranchOffice from where the Task will be delegated.
     * @param to The BranchOffice to where the Task will be delegated.
     * @return The resulting Delegation.
     */
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

    /**
     * Does all delegations that are currently still in the buffer.
     */
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

    /**
     * Undo all delegation that were the result of a simulation.
     */
    public void rollbackSimulation(BranchOffice office){
        Set<Delegation> toRemove = new HashSet<Delegation>();
        for(Delegation del : delegationBuffer ){
            if(office == del.getOldOffice()){
                toRemove.add(del);
            }
        }
        delegationBuffer.removeAll(toRemove);
        processBuffer();

    }



}
