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
        return del.getDelegationTask().getEstimatedOrPlanningPeriod();
    }



}
