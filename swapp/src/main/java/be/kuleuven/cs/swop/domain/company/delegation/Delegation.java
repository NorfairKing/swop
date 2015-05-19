package be.kuleuven.cs.swop.domain.company.delegation;

import be.kuleuven.cs.swop.domain.company.BranchOffice;
import be.kuleuven.cs.swop.domain.company.task.Task;


public class Delegation {
	private final Task delegatedTask;;
    private Task delegationTask = null;
    private final BranchOffice oldOffice;
    private final BranchOffice newOffice;
	
	public Delegation(Task delegatedTask, BranchOffice oldOffice, BranchOffice newOffice) {
        super();
        this.delegatedTask = delegatedTask;
        this.oldOffice = oldOffice;
        this.newOffice = newOffice;
    }
	
	void setDelegationTask(Task task){
		if(delegationTask != null){
			throw new IllegalStateException(ERROR_TASK_ALREADY_SET);
		}
		if(task == null){
			throw new IllegalArgumentException(ERROR_TASK_NULL);
		}
		delegationTask = task;
	}
    
	/**
	 * @return the delegatedTask
	 */
	public Task getDelegatedTask() {
		return delegatedTask;
	}
    /**
     * @return the delegatedTask
     */
    public Task getDelegationTask() {
        return delegationTask;
    }
    
    /**
     * @return the oldOffice
     */
    public BranchOffice getOldOffice() {
        return oldOffice;
    }
    
    /**
     * @return the newOffice
     */
    public BranchOffice getNewOffice() {
        return newOffice;
    }
    
    private String ERROR_TASK_ALREADY_SET = "A task has already been set for this Delegation";
    private String ERROR_TASK_NULL = "The Task for this Delegation may not be set to null";


    

}
