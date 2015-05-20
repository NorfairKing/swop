package be.kuleuven.cs.swop.domain.company.task;


import java.time.LocalDateTime;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.delegation.Delegation;


@SuppressWarnings("serial")
public class DelegatedStatus extends TaskStatus {

    private final Delegation del;

    DelegatedStatus(Task task, Delegation del) {
        super(task);
        if(!canHaveAsDelegation(del)){
            throw new IllegalArgumentException(ERROR_ILLEGAL_DELEGATION);
        }
        this.del = del;
    }

    protected boolean canHaveAsDelegation(Delegation del) {
        return del != null;
    }

    @Override
    boolean isFinished() {
        return getDelegation().getDelegationTask().isFinished();
    }

    @Override
    boolean isFailed() {
        return getDelegation().getDelegationTask().isFailed();
    }

    @Override
    boolean isExecuting() {
        return getDelegation().getDelegationTask().isExecuting();
    }

    @Override
    boolean canFinish() {
        return false; //the delegation task might be finishable, but this one can't anymore.
        // It can however that the isFinished() call returns true, where before it returned false,
        // even though it this status won't allow changing to the finished status. This happens
        // when the delegation task is finished.
    }

    @Override
    boolean canFail() {
        return false; //see canFinish
    }

    @Override
    boolean canExecute() {
        return false; //see canFinish
    }

    @Override
    boolean isFinal() {
        return getDelegation().getDelegationTask().isFinal();
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
        return getDelegation().getDelegationTask().getEstimatedOrRealFinishDate(currentDate);
    }

    @Override
    boolean isFinishedOrHasFinishedAlternative() {
        // we consider the delegation task to be the alternative here.
        return getDelegation().getDelegationTask().isFinishedOrHasFinishedAlternative();
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
        return getDelegation().getDelegationTask().getPerformedDuring();
    }

    @Override
    boolean wasFinishedOnTime() {
        return getDelegation().getDelegationTask().wasFinishedOnTime();
    }

    @Override
    boolean wasFinishedEarly() {
        return getDelegation().getDelegationTask().wasFinishedEarly();
    }

    @Override
    boolean wasFinishedLate() {
        return getDelegation().getDelegationTask().wasFinishedLate();
    }

    @Override
    void delegate(Delegation del) {
        throw new IllegalStateException(ERROR_DELEGATE);
    }

    @Override
    Delegation getDelegation() {
        return del;
    }

    @Override
    boolean canPlan() {
        return false;
    }

    private static final String ERROR_SET_ALTERNATIVE_ERROR = "Can't set an alternative for a delegated task.";
    private static final String ERROR_FINISH                = "Can't finish a delegated task.";
    private static final String ERROR_FAIL                  = "Can't fail a delegated task.";
    private static final String ERROR_EXECUTE               = "Can't execute a delegated task.";
    private static final String ERROR_DELEGATE              = "Can't delegate a task that's already delegated.";
    private static final String ERROR_ILLEGAL_DELEGATION    = "Illegal delegation for delegated status.";

    @Override
    public DateTimePeriod getEstimatedOrPlanningPeriod() {
        return null;
    }



}
