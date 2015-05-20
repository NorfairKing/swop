package be.kuleuven.cs.swop.domain.company.task;


import be.kuleuven.cs.swop.domain.DateTimePeriod;


@SuppressWarnings("serial")
public class UnstartedStatus extends IncompleteStatus {

    UnstartedStatus(Task task) {
        super(task);
    }

    /**
     * Checks whether the project containing this status is finished.
     *
     * @return Returns false.
     */
    @Override
    boolean isFinished() {
        return false;
    }

    /**
     * Checks whether the project containing this status is failed.
     *
     * @return Returns false.
     */
    @Override
    boolean isFailed() {
        return false;
    }

    /**
     * Checks whether this status is final and therefore can't be changed.
     *
     * @return Returns false.
     */
    @Override
    boolean isFinal() {
        return false;
    }
    
    @Override
    boolean isExecuting() {
    	return false;
    }

    /**
     * Checks whether the project containing this status can finish.
     *
     * @return Returns false.
     */
    @Override
    boolean canFinish() {
        return false; //!getTask().hasUnfinishedDependencies();
    }

    /**
     * Checks whether the project containing this status can fail.
     *
     * @return Returns true.
     */
    @Override
    boolean canFail() {
        return true;
    }

    @Override
    void finish(DateTimePeriod period) {
    	throw new IllegalStateException(ERROR_FINISH);
    }
    
    @Override
    void execute(){
    	goToStatus(new ExecutingStatus(getTask()));
    }

    @Override
    Task getAlternative() {
        return null;
    }
    
	@Override
	boolean canExecute() {
		return true;
	}
	
	boolean canPlan(){
	    return true;
	}

    private static final String ERROR_FINISH = "Can't finish a task that has not been started.";


}
