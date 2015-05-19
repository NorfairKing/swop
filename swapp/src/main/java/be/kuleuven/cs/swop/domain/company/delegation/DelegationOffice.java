package be.kuleuven.cs.swop.domain.company.delegation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.domain.company.BranchOffice;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.task.Task;


public class DelegationOffice {
    private final Set<Delegation> delegations = new HashSet<Delegation>();
    private BranchOffice simulatingOffice = null;
    private final Map<Task,Delegation> delegationBuffer = new HashMap<Task, Delegation>();

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
            Delegation del = new Delegation(from, to);
            task.delegate(del);
            if(isSimulating() && from == simulatingOffice){
            	delegationBuffer.put(task, del);
            }else{
            	commitDelegation(task, del);
            }
            return del;
        }
        return null;
    }
    
    public boolean isSimulating(){
    	return simulatingOffice != null;
    }
    
    public void startSimulation(BranchOffice office){
    	if(simulatingOffice != null){
    		throw new IllegalStateException(ERROR_ALREADY_SIMULATING);
    	}
    	simulatingOffice = office;
    }
    
    private void commitDelegation(Task task, Delegation del){
        Task newTask = del.getNewOffice().createDelegationTask(task.getDescription(),
        		task.getEstimatedDuration(),
        		task.getAcceptableDeviation(),
        		new Requirements(task.getRequirements()));
        del.setTask(newTask);
        delegations.add(del);

    }
    
    public void commitSimulation(){
    	if(simulatingOffice == null){
    		throw new IllegalStateException(ERROR_NOT_SIMULATING);
    	}
    	
    	for(Map.Entry<Task, Delegation> entry : delegationBuffer.entrySet() ){
    		commitDelegation(entry.getKey(), entry.getValue());
    	}
    	delegationBuffer.clear();
    	simulatingOffice = null;
    	
    }
    
    public void rollbackSimulation(){
    	if(simulatingOffice == null){
    		throw new IllegalStateException(ERROR_NOT_SIMULATING);
    	}
    	delegationBuffer.clear();
    	simulatingOffice = null;
    }
    
    private String ERROR_ALREADY_SIMULATING = "Can't start a simulation when there is already a simulation running.";
    private String ERROR_NOT_SIMULATING = "Can't end a simulation when there is no simulation running.";
    
    

}
