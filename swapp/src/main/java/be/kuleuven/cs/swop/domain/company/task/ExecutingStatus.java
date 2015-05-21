package be.kuleuven.cs.swop.domain.company.task;


@SuppressWarnings("serial")
public class ExecutingStatus extends IncompleteStatus {
    
    @SuppressWarnings("unused")
    private ExecutingStatus() {super();} //for automatic (de)-serialization

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
    void finish() {
        if (getTask().hasUnfinishedDependencies()) {
            throw new IllegalStateException(ERROR_FINISH);
        }
        else {
            goToStatus(new FinishedStatus(getTask()));
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
    boolean canPlan() {
        return true;
    }

    private static final String ERROR_FINISH = "Can't finish a task with unfinished dependencies.";
    private static final String ERROR_EXECUTE = "Can't execute a task that's already being executed.";

}
