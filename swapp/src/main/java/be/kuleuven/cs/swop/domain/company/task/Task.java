package be.kuleuven.cs.swop.domain.company.task;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.delegation.Delegation;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;

import com.google.common.collect.ImmutableSet;


/**
 *
 *
 */
@SuppressWarnings("serial")
public class Task implements Serializable {

    private TaskStatus   status;
    private TaskInfo     info;
    private TaskPlanning planning;

    Task() {}

    /**
     * Full constructor
     *
     * @param info All info for this task.
     */
    public Task(TaskInfo info) {
        setTaskInfo(info);
        setStatus(new UnstartedStatus(this));
    }

    /**
     * Retrieve this tasks info.
     *
     * @return The TaskInfo.
     */
    public TaskInfo getTaskInfo() {
        return this.info;
    }

    private void setTaskInfo(TaskInfo info) {
        if (!canHaveAsTaskInfo(info)) throw new IllegalArgumentException(ERROR_ILLEGAL_TASK_INFO);
        this.info = info;
    }

    public boolean canHaveAsTaskInfo(TaskInfo info) {
        return info != null;
    }

    /**
     * Retrieves the best estimated finish time or, if this Task has finished, the real time when the Task finished.
     *
     * @param currentDate
     *            The current system time
     * @return Returns a Date containing the estimated or real time when the Task should be finished.
     */
    public LocalDateTime getEstimatedOrRealFinishDate(LocalDateTime currentDate) {
        return status.getEstimatedOrRealFinishDate(currentDate);
    }

    LocalDateTime getLatestEstimatedOrRealFinishDateOfDependencies(LocalDateTime currentDate) {
        return info.getLatestEstimatedOrRealFinishDateOfDependencies(currentDate);
    }

    public DateTimePeriod getEstimatedOrPlanningPeriod() {
        return status.getEstimatedOrPlanningPeriod();
    }


    /**
     * Sets an alternative Task for this Task for when this Task failed, the alternative can't be null, can't create a dependency loop and this Task has to be failed.
     *
     * @param alternative
     *            The Task to be set as alternative for this Task.
     * @throws IllegalArgumentException
     *             If the Task can't have the given Task as alternative.
     */
    public void setAlternative(Task alternative) {
        status.setAlternative(alternative);
    }

    /**
     * Checks if the given status is a valid status for this Task.
     *
     * @param status
     *            The TaskStatus to be checked for validity.
     * @return Returns true if this status is valid and therefore isn't null.
     */
    protected boolean canHaveAsStatus(TaskStatus status) {
        return status != null && status.getTask() == this;
    }

    void setStatus(TaskStatus status) {
        if (!canHaveAsStatus(status)) throw new IllegalArgumentException(ERROR_ILLEGAL_STATUS);
        this.status = status;
    }

    /**
     * Checks whether or not this Task has finished.
     *
     * @return Returns true if this Task's status is finished.
     */
    public boolean isFinished() {
        return status.isFinished();
    }

    /**
     * Checks whether or not this Task has failed.
     *
     * @return Returns true if this Task's status is failed.
     */
    public boolean isFailed() {
        return status.isFailed();
    }

    /**
     * Checks whether or not this Task is executing.
     *
     * @return Returns true if this Task's status is executing.
     */
    public boolean isExecuting() {
        return status.isExecuting();
    }

    /**
     * Checks whether or not this Task is final.
     *
     * @return Returns true if this Task's status is final.
     */
    public boolean isFinal() {
        return status.isFinal();
    }

    /**
     * Checks whether or not this Task can finish.
     *
     * @return Returns true if this Task's can be finished.
     */
    public boolean canFinish() {
        return status.canFinish();
    }

    /**
     * Changes this Task's status to finished if possible, otherwise it throws an exception.
     *
     * @throws IllegalStateException
     *             If this Task can't finish with the current status.
     */
    public void finish() {
        status.finish();
    }

    /**
     * Changes this Task's status to failed if possible, otherwise it throws an exception.
     *
     * @throws IllegalStateException
     *             If this Task can't fail with the current status.
     */
    public void fail() {
        status.fail();
    }

    /**
     * Set this task to executing. Only basic tests if it can are done here, the main testing is done by the planningManager. Because of this, this method should only be called by the planningManager.
     * We can't enforce this in java, but that's a limitation we'll have to live with. Note: for convenience it is used in some tests directly, however there are also tests to see if the checking is
     * done properly in the planningManager.
     */
    public void execute() {
        status.execute();
    }

    /**
     * The 'available' from the first iteration. This is just a basic check on the task level No deeper checks are done here.
     * 
     * @return Whether it is Tier1Available.
     */
    public boolean isTier1Available() {
        return !getTaskInfo().hasUnfinishedDependencies() && status.canExecute();
    }

    /**
     * Check whether or not this task was finished on time.
     *
     * @return True if it was finished on time.
     */
    public boolean wasFinishedOnTime() {
        return status.wasFinishedOnTime();
    }

    /**
     * Check whether or not this task was finished early.
     *
     * @return True if it was finished early.
     */
    public boolean wasFinishedEarly() {
        return status.wasFinishedEarly();
    }

    /**
     * Check whether or not this task was finished late.
     *
     * @return True if it was finished late.
     */
    public boolean wasFinishedLate() {
        return status.wasFinishedLate();
    }

    /**
     * Check whether or not this task was finished or it's alternative has finished.
     *
     * @return True if it was finished.
     */
    public boolean isFinishedOrHasFinishedAlternative() {
        return status.isFinishedOrHasFinishedAlternative();
    }

    /**
     * Retrieve the tasks alternative task.
     *
     * @return The alternative Task.
     */
    public Task getAlternative() {
        return status.getAlternative();
    }

    /**
     * Delegate this task.
     *
     * @param del The Delegation object.
     */
    public void delegate(Delegation del) {
        status.delegate(del);
    }

    /**
     * Retrieve this tasks delegation.
     *
     * @return The Delegation object.
     */
    public Delegation getDelegation() {
        return status.getDelegation();
    }

    /**
     * Get this tasks planning.
     *
     * @return The TaskPlanning.
     */
    public TaskPlanning getPlanning() {
        return this.planning;
    }

    /**
     * Get this tasks description.
     *
     * @return The String with the description.
     */
    public String getDescription() {
        return info.getDescription();
    }

    /**
     * Get this task estimated duration in minutes.
     *
     * @return A long with the estimated duration in minutes.
     */
    public long getEstimatedDuration() {
        return info.getEstimatedDuration();
    }

    /**
     * Check if this task has the given task as dependency.
     *
     * @param dependency The to be checked Task.
     * @return True if this task has it as a dependency.
     */
    boolean containsDependency(Task dependency) {
        return info.containsDependency(dependency);
    }

    /**
     * Retrieve the acceptable deviation.
     *
     * @return A double with the acceptable deviantion percentage.
     */
    public double getAcceptableDeviation() {
        return info.getAcceptableDeviation();
    }

    /**
     * Retrieve the dependencies of this task (dependant tasks)
     *
     * @return An Immutable Set containing the Tasks.
     */
    public ImmutableSet<Task> getDependencySet() {
        return info.getDependencySet();
    }

    /**
     * Check whether or not this task has unfinished dependencies.
     *
     * @return True if this has unfinished dependencies
     */
    boolean hasUnfinishedDependencies() {
        return info.hasUnfinishedDependencies();
    }

    /**
     * Retrieve the set of requirements.
     *
     * @return A Set containing the requirements of this Task.
     */
    public Set<Requirement> getRequirements() {
        return info.getRequirements().getRequirementSet();
    }

    /**
     * Retrieve the set of requirements including their dependencies.
     *
     * @return A Set containing the requirements of this Task.
     */
    public Set<Requirement> getRecursiveRequirements() {
        return info.getRecursiveRequirements().getRequirementSet();
    }

    protected long getBestDuration() {
        return info.getBestDuration();
    }

    protected long getWorstDuration() {
        return info.getWorstDuration();
    }

    /**
     * Check whether of not this task is already planned
     *
     * @return True if it's planned.
     */
    public boolean isPlanned() {
        return this.planning != null;
    }

    /**
     * Check whether or not this task can be planned.
     *
     * @return True if this Task can be planned.
     */
    public boolean canPlan(){
        return this.status.canPlan();
    }

    /**
     * Plan this task.
     *
     * @param plan The TaskPlanning for this Task.
     */
    public void plan(TaskPlanning plan) {
        if (!this.status.canPlan()){
            throw new IllegalStateException(ERROR_PLAN +" " + this.status.getClass().toString());
        }
        this.planning = plan;
    }

    /**
     * Undo this task's planning
     */
    public void removePlanning() {
        this.planning = null;
    }

    private static final String ERROR_ILLEGAL_TASK_INFO = "Illegal info for task.";
    private static final String ERROR_ILLEGAL_STATUS    = "Illegal status for task.";
    private static final String ERROR_PLAN  = "Task is in the wrong state to plan.";

    public boolean isDelegated() {
        return status.isDelegated();
    }

}
