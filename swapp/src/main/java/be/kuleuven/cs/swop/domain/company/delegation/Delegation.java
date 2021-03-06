package be.kuleuven.cs.swop.domain.company.delegation;

import java.io.Serializable;

import be.kuleuven.cs.swop.domain.company.BranchOffice;
import be.kuleuven.cs.swop.domain.company.task.Task;


@SuppressWarnings("serial")
public class Delegation implements Serializable {
	private final Task delegatedTask;
    private Task delegationTask = null;
    private final BranchOffice oldOffice;
    private final BranchOffice newOffice;

    /**
     * Constructor, creates a new delegation of a task from and office to another office.
     *
     * @param delegatedTask The Task that will be delegated.
     * @param oldOffice The BranchOffice where the Task was originally meant to be
     * finished.
     * @param newOffice The BranchOffice that will now do the given Task.
     */
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

    /**
     * Checks whether or not this delegation has a Task
     *
     * @return True if this has a Task.
     */
    public boolean hasDelegation(){
        return delegationTask != null;
    }

    private static final String ERROR_TASK_ALREADY_SET = "A task has already been set for this Delegation";
    private static final String ERROR_TASK_NULL = "The Task for this Delegation may not be set to null";




}
