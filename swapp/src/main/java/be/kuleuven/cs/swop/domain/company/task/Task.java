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
     * @param description
     *            The description of the new task.
     * @param estimatedDuration
     *            The estimated duration of the task in minutes.
     * @param acceptableDeviation
     *            The acceptable deviation of time in which the task can be completed.
     */
    public Task(TaskInfo info) {
        setTaskInfo(info);
        setStatus(new UnstartedStatus(this));
    }

    public TaskInfo getTaskInfo() {
        return this.info;
    }

    public void setTaskInfo(TaskInfo info) {
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
        return getTaskInfo().getLatestEstimatedOrRealFinishDateOfDependencies(currentDate);
    }

    public DateTimePeriod getEstimatedOrPlanningPeriod() {
        return status.getEstimatedOrPlanningPeriod();
    }

    /**
     * Check whether this Task can have the given task as a dependency, the given Task can't be null and cannnot create a dependency loop when it's added as dependency to tis Task.
     *
     * @param dependency
     *            The given possible dependency.
     * @return Returns true is the given Task isn't null or when it doesn't create a dependency loop when added as dependency.
     */
    public boolean canHaveAsDependency(Task dependency) {
        if (dependency == null) { return false; }
        return (dependency != this) && !dependency.containsDependency(this);
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

    public boolean isExecuting() {
        return status.isExecuting();
    }

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
     * @param period
     *            The TimePeriod for when there has been worked on this project.
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
     * @param period
     *            The TimePeriod for when there has been worked on this project.
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

    public boolean wasFinishedOnTime() {
        return status.wasFinishedOnTime();
    }

    public boolean wasFinishedEarly() {
        return status.wasFinishedEarly();
    }

    public boolean wasFinishedLate() {
        return status.wasFinishedLate();
    }

    public boolean isFinishedOrHasFinishedAlternative() {
        return status.isFinishedOrHasFinishedAlternative();
    }

    public Task getAlternative() {
        return status.getAlternative();
    }

    public void delegate(Delegation del) {
        status.delegate(del);
    }

    public Delegation getDelegation() {
        return status.getDelegation();
    }

    public TaskPlanning getPlanning() {
        return this.planning;
    }

    public String getDescription() {
        return info.getDescription();
    }

    public long getEstimatedDuration() {
        return info.getEstimatedDuration();
    }

    boolean containsDependency(Task dependency) {
        return info.containsDependency(dependency);
    }

    public double getAcceptableDeviation() {
        return info.getAcceptableDeviation();
    }

    public ImmutableSet<Task> getDependencySet() {
        return info.getDependencySet();
    }

    boolean hasUnfinishedDependencies() {
        return info.hasUnfinishedDependencies();
    }

    public Set<Requirement> getRequirements() {
        return info.getRequirements().getRequirementSet();
    }

    public Set<Requirement> getRecursiveRequirements() {
        return info.getRecursiveRequirements().getRequirementSet();
    }

    protected long getBestDuration() {
        return info.getBestDuration();
    }

    protected long getWorstDuration() {
        return info.getWorstDuration();
    }

    public boolean isPlanned() {
        return this.planning != null;
    }

    public void plan(TaskPlanning plan) {
        if (!this.status.canPlan()){
            throw new IllegalStateException(ERROR_PLAN +" " + this.status.getClass().toString());
        }
        this.planning = plan;
    }

    public void removePlanning() {
        this.planning = null;
    }

    private static final String ERROR_ILLEGAL_TASK_INFO = "Illegal info for task.";
    private static final String ERROR_ILLEGAL_STATUS    = "Illegal status for task.";
    private static final String ERROR_PLAN  = "Task is in the wrong state to plan.";

}
