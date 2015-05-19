package be.kuleuven.cs.swop.domain.company.task;

import java.time.LocalDateTime;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.delegation.Delegation;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;

@SuppressWarnings("serial")
public class DelegatedStatus extends TaskStatus {
	
	private Delegation del;
	
	DelegatedStatus(Task task, Delegation del) {
	    super(task, null);
		this.del = del;
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
		return false;
	}

	@Override
	boolean canFinish() {
		return false;
	}

	@Override
	boolean canFail() {
		return false;
	}

	@Override
	boolean canExecute() {
		return false;
	}

	@Override
	boolean isFinal() {
		return false;
	}

	@Override
	void finish(DateTimePeriod period) {
		throw new IllegalStateException(ERROR_FINISH);
		
	}

	@Override
	void fail(DateTimePeriod period) {
		throw new IllegalStateException(ERROR_FAIL);
		
	}

	@Override
	void execute() {
		throw new IllegalStateException(ERROR_EXECUTE);
		
	}

	@Override
	LocalDateTime getEstimatedOrRealFinishDate(LocalDateTime currentDate) {
		return null;
	}

	@Override
	boolean isFinishedOrHasFinishedAlternative() {
		return false;
	}

	@Override
	Task getAlternative() {
		return null;
	}

	@Override
	void setAlternative(Task alternative) {
        throw new IllegalStateException(ERROR_SET_ALTERNATIVE_ERROR);		
	}

	@Override
	DateTimePeriod getPerformedDuring() {
		return null;
	}

	@Override
	boolean wasFinishedOnTime() {
		return false;
	}

	@Override
	boolean wasFinishedEarly() {
		return false;
	}

	@Override
	boolean wasFinishedLate() {
		return false;
	}
	
	@Override
	void delegate(Delegation del) {
        throw new IllegalStateException(ERROR_DELEGATE);		
		
	}
	
	@Override
	Delegation getDelegation(){
		return del;
	}
	
    @Override
	void plan(TaskPlanning plan){
    	throw new IllegalStateException(ERROR_PLAN);
    }
    
    @Override
    void removePlanning(){
    	throw new IllegalStateException(ERROR_PLAN_REMOVE);
    }
	
    private static String ERROR_SET_ALTERNATIVE_ERROR = "Can't set an alternative for a delegated task.";
	private static String ERROR_FINISH                   = "Can't finish a delegated task.";
	private static String ERROR_FAIL                     = "Can't fail a delegated task.";
	private static String ERROR_EXECUTE                     = "Can't execute a delegated task.";
	private static String ERROR_DELEGATE                     = "Can't delegate a task that's already delegated.";
	private static String ERROR_PLAN                     = "Can't plan a task that's delegated.";
	private static String ERROR_PLAN_REMOVE                     = "Can't remove the planning of a task that's delegated.";

    @Override
    public DateTimePeriod getEstimatedOrPlanningPeriod() {
        return del.getDelegatedTask().getEstimatedOrPlanningPeriod();
    }

}
