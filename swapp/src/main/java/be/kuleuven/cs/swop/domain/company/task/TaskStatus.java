package be.kuleuven.cs.swop.domain.company.task;


import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.delegation.Delegation;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;

import java.io.Serializable;
import java.time.LocalDateTime;


@SuppressWarnings("serial")
abstract class TaskStatus implements Serializable {

    private final Task task;
    
    protected TaskStatus() {task = null;} //for automatic (de)-serialization
    
    TaskStatus(Task task) {
        if (!canHaveAsTask(task)) { throw new IllegalArgumentException(ERROR_ILLEGAL_TASK); }
        this.task = task;    
    }

    /**
     * Checks whether the project containing this status is finished.
     *
     * @return Returns a true if Task containing this status is finished.
     */
    abstract boolean isFinished();

    /**
     * Checks whether the project containing this status is failed.
     *
     * @return Returns a true if Task containing this status is failed.
     */
    abstract boolean isFailed();
    
    abstract boolean isExecuting();

    /**
     * Checks whether the project containing this status can finish.
     *
     * @return Returns a true if Task containing this status can finish.
     */
    abstract boolean canFinish();

    /**
     * Checks whether the project containing this status can fail.
     *
     * @return Returns a true if Task containing this status can fail.
     */
    abstract boolean canFail();
    
    abstract boolean canExecute();

    /**
     * Checks whether this status is final and therefore can't be changed.
     *
     * @return Returns a true if this status is final.
     */
    abstract boolean isFinal();
    
    public boolean isDelegated(){
        return false;
    }

    protected Task getTask() {
        return task;
    }

    boolean canHaveAsTask(Task task) {
        return task != null;
    }

    abstract void finish();

    abstract void fail();
    
    abstract void execute();
    
    abstract void delegate(Delegation del);

    void goToStatus(TaskStatus status) {
        this.task.setStatus(status);
    }

    abstract LocalDateTime getEstimatedOrRealFinishDate(LocalDateTime currentDate);

    /**
     * Checks if this Task is finished or if the Task has failed, if it's alternative has finished.
     *
     * @return Returns true if this task or it's alternative is finished.
     */
    abstract boolean isFinishedOrHasFinishedAlternative();

    abstract Task getAlternative();

    abstract void setAlternative(Task alternative);

    /**
     * Checks whether or not this Task was finished within the acceptable deviation.
     *
     * @return Returns true if this Task has finished within the acceptable deviation. Otherwise it returns false, also when it hasn't finished yet.
     */
    abstract boolean wasFinishedOnTime();

    abstract boolean wasFinishedEarly();

    abstract boolean wasFinishedLate();
    
    Delegation getDelegation(){
    	return null;
    }
    
    abstract boolean canPlan();
    

    /**
     * Gives an indication of when this planning has taken, or should take place. If the task is already finished, the planning knows when it was, and returns that as the period it was done. If the
     * task isn't finished the planning will estimate a period based on the planned starting time and how long the task needs.
     *
     * @return An estimated period in which the task should be done, or the real period in which it was done.
     */
    public DateTimePeriod getEstimatedOrPlanningPeriod() {
        if(getTask().isPlanned()){
            TaskPlanning p = getTask().getPlanning();
            return p.getEstimatedPeriod();

        }else{
            return null;
        }
    }
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((task == null) ? 0 : task.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TaskStatus other = (TaskStatus) obj;
        if (task == null) {
            if (other.task != null) return false;
        } else if (!task.equals(other.task)) return false;
        return true;
    }

    private static final String ERROR_ILLEGAL_TASK = "Illegal task for status";
    protected static final String ERROR_ALREADY_PLANNED  = "This task is already planned.";


}
