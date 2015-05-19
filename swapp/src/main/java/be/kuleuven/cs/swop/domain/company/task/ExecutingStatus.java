package be.kuleuven.cs.swop.domain.company.task;


import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;


@SuppressWarnings("serial")
public class ExecutingStatus extends IncompleteStatus {

    ExecutingStatus(Task task, TaskPlanning planning) {
        super(task, planning);
    }

    @Override
    boolean isFinished() {
        return false;
    }

    @Override
    boolean isFailed() {
        return false;
    }
    
    @Override
    boolean isExecuting() {
    	return true;
    }


    @Override
    boolean canFinish() {
        return true;
    }

    @Override
    boolean canFail() {
        return true;
    }

    @Override
    boolean isFinal() {
        return false;
    }

    @Override
    void finish(DateTimePeriod period) {
        if (getTask().hasUnfinishedDependencies()) {
            throw new IllegalStateException(ERROR_FINISH);
        }
        else {
            goToStatus(new FinishedStatus(getTask(), getPlanning(), period));
        }
    }
    
    @Override
    void execute(){
        throw new IllegalStateException(ERROR_EXECUTE);
    }

    @Override
    Task getAlternative() {
        return null;
    }
    
	@Override
	boolean canExecute() {
		return false;
	}
	
    @Override
	void plan(TaskPlanning plan){
    	throw new IllegalStateException(ERROR_PLAN);
    }
    
    @Override
    void removePlanning(){
    	throw new IllegalStateException(ERROR_PLAN_REMOVE);
    }

    private static String ERROR_FINISH = "Can't finish a task with unfinished dependencies.";
    private static String ERROR_EXECUTE = "Can't execute a task that's already being executed.";
	private static String ERROR_PLAN                     = "Can't plan a task that's executing.";
	private static String ERROR_PLAN_REMOVE                     = "Can't remove the planning of a task that's executing.";

}
