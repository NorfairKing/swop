package be.kuleuven.cs.swop.domain.company.task;


import java.time.LocalDateTime;
import be.kuleuven.cs.swop.domain.company.delegation.Delegation;


@SuppressWarnings("serial")
public abstract class CompletedStatus extends TaskStatus {

    protected CompletedStatus() {super();} //for automatic (de)-serialization
    CompletedStatus(Task task) {
        super(task);
    }

    /**
     * Checks whether the project containing this status can finish.
     *
     * @return Returns false.
     */
    @Override
    boolean canFinish() {
        return false;
    }

    /**
     * Checks whether the project containing this status can fail.
     *
     * @return Returns false.
     */
    @Override
    boolean canFail() {
        return false;
    }

    /**
     * Checks whether this status is final and therefore can't be changed.
     *
     * @return Returns true.
     */
    @Override
    boolean isFinal() {
        return true;
    }

    @Override
    boolean isExecuting() {
        return false;
    }

    @Override
    void fail() {
        throw new IllegalStateException(ERROR_FAIL);
    }

    @Override
    void finish() {
        throw new IllegalStateException(ERROR_FINISH);
    }

    @Override
    void execute() {
        throw new IllegalStateException(ERROR_EXECUTE);
    }

    @Override
    void delegate(Delegation del) {
        throw new IllegalStateException(ERROR_DELEGATE);
    }

    abstract LocalDateTime getEstimatedOrRealFinishDate(java.time.LocalDateTime currentDate);

    @Override
    boolean canExecute() {
        return false;
    }
    
    @Override
    boolean canPlan() {
        return false;
    }
    
    private static final String ERROR_FINISH                   = "Can't finish a performed task.";
    private static final String ERROR_FAIL                     = "Can't fail a performed task.";
    private static final String ERROR_EXECUTE                  = "Can't execute a performed task.";
    private static final String ERROR_DELEGATE                 = "Can't delegate a performed task.";

}
