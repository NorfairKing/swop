package be.kuleuven.cs.swop.domain.company.task;


import be.kuleuven.cs.swop.domain.DateTimePeriod;


@SuppressWarnings("serial")
public class ExecutingStatus extends IncompleteStatus {

    ExecutingStatus(Task task) {
        super(task);
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
            goToStatus(new FinishedStatus(getTask(), period));
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

    private static String ERROR_FINISH = "Can't finish a task with unfinished dependencies.";
    private static String ERROR_EXECUTE = "Can't execute a task that's already being executed.";
}
