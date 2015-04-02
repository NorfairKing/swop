package be.kuleuven.cs.swop.domain.task;


import be.kuleuven.cs.swop.domain.TimePeriod;


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
    void finish(TimePeriod period) {
        if (getTask().hasUnfinishedDependencies()) {
            throw new IllegalStateException(ERROR_FINISH);
        }
        else {
            goToStatus(new FinishedStatus(getTask(), period));
        }
    }

    @Override
    Task getAlternative() {
        return null;
    }

    private static String ERROR_FINISH = "Can't finish a task with unfinished dependencies.";
}
