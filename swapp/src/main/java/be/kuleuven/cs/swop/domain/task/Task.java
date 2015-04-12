package be.kuleuven.cs.swop.domain.task;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.TimeCalculator;
import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.resource.Requirement;

import com.google.common.collect.ImmutableSet;


/**
 *
 *
 */
@SuppressWarnings("serial")
public class Task implements Serializable {

    private String           description;
    private long             estimatedDuration;
    private double           acceptableDeviation;
    private Set<Task>        dependencies = new HashSet<Task>();
    private TaskStatus       status;
    private Set<Requirement> requirements = new HashSet<Requirement>();

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
    public Task(String description, long estimatedDuration, double acceptableDeviation) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setStatus(new OngoingStatus(this));
    }

    public Task(String description, long estimatedDuration, double acceptableDeviation, Set<Requirement> requirements) {
        this(description, estimatedDuration, acceptableDeviation);
        this.setRequirements(requirements);
    }

    /**
     * Retrieves this task's description.
     *
     * @return Returns a String containing the task's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks whether or not the given description is valid.
     *
     * @param description
     *            A String containing a possible description for this Task.
     * @return Returns true if the given description is valid.
     */
    public boolean canHaveAsDescription(String description) {
        return description != null;
    }

    /**
     * Changes the Task's description to the given String
     *
     * @param description
     *            The String containing the Task's new description.
     * @throws IllegalArgumentException
     *             If the given description is invalid.
     */
    public void setDescription(String description) {
        if (!canHaveAsDescription(description)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DESCRIPTION); }
        this.description = description;
    }

    /**
     * Checks whether or not the given duration is a valid new estimated duration.
     *
     * @param estimatedDuration
     *            The double containing the possible new estimated duration for the Task in minutes.
     * @return Returns true if the given duration is a valid duration for this task. Which means that it's greater than zero.
     */
    public boolean canHaveAsEstimatedDuration(double estimatedDuration) {
        return Double.isFinite(estimatedDuration) && estimatedDuration > 0;
    }

    /**
     * Retrieves the Task's estimated duration.
     *
     * @return Returns a double containing the estimated duration in minutes.
     */
    public long getEstimatedDuration() {
        return estimatedDuration;
    }

    /**
     * Retrieves the best estimated finish time or, if this Task has finished, the real time when the Task finished.
     *
     * @return Returns a Date containing the estimated or real time when the Task should be finished.
     */
    public LocalDateTime getEstimatedOrRealFinishDate(LocalDateTime currentDate) {
        return status.getEstimatedOrRealFinishDate(currentDate);
    }

    LocalDateTime getLatestEstimatedOrRealFinishDateOfDependencies(LocalDateTime currentDate) {
        LocalDateTime lastTime = LocalDateTime.MIN;
        for (Task dependency : getDependencySet()) {
            LocalDateTime lastTimeOfThis = dependency.getEstimatedOrRealFinishDate(currentDate);
            if (lastTimeOfThis.isAfter(lastTime)) {
                lastTime = lastTimeOfThis;
            }
        }

        return lastTime;
    }

    /**
     * Changes the Task's estimated duration to the given duration.
     *
     * @param estimatedDuration
     *            The double containing the new estimated duration in minutes.
     * @throws IllegalArgumentException
     *             If the given duration is not valid.
     */
    public void setEstimatedDuration(long estimatedDuration) {
        if (!canHaveAsEstimatedDuration(estimatedDuration)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DURATION); }
        this.estimatedDuration = estimatedDuration;
    }

    boolean containsDependency(Task dependency) {
        if (dependency == null) { return true; }
        for (Task subDep : this.getDependencySet()) {
            if (subDep == dependency) { return true; }
            if (subDep.containsDependency(dependency)) { return true; }
        }
        return false;
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
     * Adds the given Task as dependency of this Task, this throws an exception when the new Task creates a dependency loop.
     *
     * @param dependency
     *            The Task to be added as dependency of this Task.
     * @throws IllegalArgumentException
     *             If the new dependency is invalid, this means that it can't be null or create a dependency loop.
     */
    public void addDependency(Task dependency) {
        if (!canHaveAsDependency(dependency)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DEPENDENCY); }
        if (this.dependencies.contains(dependency)) { return; }
        this.dependencies.add(dependency);
    }

    /**
     * Retrieves the acceptable deviation for competing this Task.
     *
     * @return Returns a double containing the acceptable deviation for completing this task.
     */
    public double getAcceptableDeviation() {
        return acceptableDeviation;
    }

    /**
     * Checks whether or not the Task can have the given deviation as acceptable deviation.
     *
     * @param deviation
     *            The double containing the deviation to be checked.
     * @return Returns true if the given double is a valid deviation.
     */
    public boolean canHaveAsDeviation(double deviation) {
        if (Double.isNaN(deviation)) { return false; }
        if (Double.isInfinite(deviation)) { return false; }
        if (deviation < 0) { return false; }
        return true;
    }

    /**
     * Changes the Task's acceptable deviation for the time completing the Task to the given deviation.
     *
     * @param acceptableDeviation
     *            The double containing the new deviation.
     * @throws IllegalArgumentException
     *             If the given deviation is not valid.
     */
    public void setAcceptableDeviation(double acceptableDeviation) {
        if (!canHaveAsDeviation(acceptableDeviation)) throw new IllegalArgumentException(ERROR_ILLEGAL_DEVIATION);
        this.acceptableDeviation = acceptableDeviation;
    }

    /**
     * Sets an alternative Task for this Task for when this Task failed, the alternative can't be null, can't create a dependency loop and this Task has to be failed.
     *
     * @param alternative
     *            The Task to be set as alternative for this Task.
     * @throws IllegalArgumentException
     *             If the Task can't have the given Task as alternative.
     */
    public void addAlternative(Task alternative) {
        status.setAlternative(alternative);
    }

    /**
     * Retrieves the dependencies of this Task.
     *
     * @return Returns a Set containing the Tasks which are dependencies of this Task.
     */
    public ImmutableSet<Task> getDependencySet() {
        return ImmutableSet.copyOf(this.dependencies);
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
    
    public boolean isExecuting(){
    	return status.isExecuting();
    }
    
    public boolean isFinal(){
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
    public void finish(DateTimePeriod period) {
        status.finish(period);
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
    public void fail(DateTimePeriod period) {
        status.fail(period);
    }
    
    public void execute(){
    	status.execute();
    }

    protected long getRealDuration() {
        return TimeCalculator.getDurationMinutes(
                getPerformedDuring().getStartTime(),
                getPerformedDuring().getStopTime());
    }

    protected long getBestDuration() {
        return getEstimatedDuration() - (long) ((double) getEstimatedDuration() * getAcceptableDeviation());
    }

    protected long getWorstDuration() {
        return getEstimatedDuration() + (long) ((double) getEstimatedDuration() * getAcceptableDeviation());
    }

    boolean hasUnfinishedDependencies() {
        if (dependencies.isEmpty()) { return false; }
        for (Task t : dependencies) {
            if (!t.isFinishedOrHasFinishedAlternative()) { return true; }
        }
        return false;
    }

    public ImmutableSet<Requirement> getRequirements() {
        return ImmutableSet.copyOf(this.requirements);
    }

    private void setRequirements(Set<Requirement> requirements) {
        if (!canHaveAsRequirements(requirements)) throw new IllegalArgumentException(ERROR_ILLEGAL_REQUIREMENTS);
        this.requirements = requirements;
    }

    protected boolean canHaveAsRequirements(Set<Requirement> requirements) {
        return requirements != null;
    }

    public DateTimePeriod getPerformedDuring() {
        return status.getPerformedDuring();
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

    private static final String ERROR_ILLEGAL_DESCRIPTION  = "Illegal project for task.";
    private static final String ERROR_ILLEGAL_DEVIATION    = "Illegal acceptable deviation for task.";
    private static final String ERROR_ILLEGAL_DURATION     = "Illegal estimated duration for task.";
    private static final String ERROR_ILLEGAL_STATUS       = "Illegal status for task.";
    private static final String ERROR_ILLEGAL_DEPENDENCY   = "Illegal dependency set for task.";
    private static final String ERROR_ILLEGAL_REQUIREMENTS = "Illegal requirement set for task.";

}
