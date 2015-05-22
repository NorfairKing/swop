package be.kuleuven.cs.swop.domain.company.task;


import java.time.LocalDateTime;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.delegation.Delegation;


@SuppressWarnings("serial")
public class DelegatedStatus extends TaskStatus {

    private final Delegation del;
    
    @SuppressWarnings("unused")
    private DelegatedStatus() {super(); del = null;} //for automatic (de)-serialization
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
        if(del.hasDelegation()){
            return getDelegation().getDelegationTask().isFinished();
        }else{
            return false;
        }
    }

    @Override
    boolean isFailed() {
        if(del.hasDelegation()){
            return getDelegation().getDelegationTask().isFailed();
        }else{
            return false;
        }
    }

    @Override
    boolean isExecuting() {
        if(del.hasDelegation()){
            return getDelegation().getDelegationTask().isExecuting();
        }else{
            return false;
        }
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
        if(del.hasDelegation()){
            return getDelegation().getDelegationTask().isFinal();
        }else{
            return false;
        }
    }

    @Override
    void finish() {
        throw new IllegalStateException(ERROR_FINISH);

    }

    @Override
    void fail() {
        throw new IllegalStateException(ERROR_FAIL);

    }

    @Override
    void execute() {
        throw new IllegalStateException(ERROR_EXECUTE);

    }

    @Override
    LocalDateTime getEstimatedOrRealFinishDate(LocalDateTime currentDate) {
        if(del.hasDelegation()){
            return getDelegation().getDelegationTask().getEstimatedOrRealFinishDate(currentDate);
        }else{
            return currentDate.plusMinutes(del.getDelegatedTask().getEstimatedDuration());
        }
    }

    @Override
    boolean isFinishedOrHasFinishedAlternative() {
        // we consider the delegation task to be the alternative here.
        if(del.hasDelegation()){
            return getDelegation().getDelegationTask().isFinishedOrHasFinishedAlternative();
        }else{
            return false;
        }
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
    boolean wasFinishedOnTime() {
        if(del.hasDelegation()){
            return getDelegation().getDelegationTask().wasFinishedOnTime();
        }else{
            return false;
        }
    }

    @Override
    boolean wasFinishedEarly() {
        if(del.hasDelegation()){
            return getDelegation().getDelegationTask().wasFinishedEarly();
        }else{
            return false;
        }
    }

    @Override
    boolean wasFinishedLate() {
        if(del.hasDelegation()){
            return getDelegation().getDelegationTask().wasFinishedLate();
        }else{
            return false;
        }
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
        if(del.hasDelegation()){
            return getDelegation().getDelegationTask().getEstimatedOrPlanningPeriod();
        }else{
            return null;
        }
    }
    
    @Override
    public boolean isDelegated(){
        return true;
    }



}
