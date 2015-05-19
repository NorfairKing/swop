package be.kuleuven.cs.swop.domain.company.task;


import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;


@SuppressWarnings("serial")
public class UnstartedStatus extends IncompleteStatus {

    UnstartedStatus(Task task, TaskPlanning planning) {
        super(task, planning);
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
    	goToStatus(new ExecutingStatus(getTask(), getPlanning()));
    }

    @Override
    Task getAlternative() {
        return null;
    }
    
    @Override
	void plan(TaskPlanning plan){
    	if(isPlanned()){
    		throw new IllegalStateException(ERROR_ALREADY_PLANNED);
    	}
    	if(plan == null){
    		throw new IllegalArgumentException(ERROR_ILLEGAL_PLAN);
    	}
    	goToStatus(new UnstartedStatus(getTask(), plan));
    }
    
    @Override
    void removePlanning(){
    	goToStatus(new UnstartedStatus(getTask(), null));
    }
	@Override
	boolean canExecute() {
		return true;
	}
	

    private static final String ERROR_FINISH = "Can't finish a task that has not been started.";
    private static final String ERROR_ILLEGAL_PLAN    = "Illegal plan for task.";


}
