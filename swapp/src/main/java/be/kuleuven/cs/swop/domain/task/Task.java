package be.kuleuven.cs.swop.domain.task;


import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.resource.Requirement;

import com.google.common.collect.ImmutableSet;


/**
 *
 *
 */
public class Task implements Serializable {

    private String           description;
    private double           estimatedDuration;
    private double           acceptableDeviation;
    private Set<Task>        dependencies = new HashSet<Task>();
    private Task             alternative;
    private TimePeriod       performedDuring;
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
    public Task(String description, double estimatedDuration, double acceptableDeviation) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setStatus(new OngoingStatus(this));
    }

    public Task(String description, double estimatedDuration, double acceptableDeviation, Set<Requirement> requirements) {
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
    public double getEstimatedDuration() {
        return estimatedDuration;
    }

    /**
     * Retrieves the best estimated finish time or, if this Task has finished, the real time when the Task finished.
     *
     * @return Returns a Date containing the estimated or real time when the Task should be finished.
     */
    public LocalDateTime getEstimatedOrRealFinishDate(LocalDateTime currentDate) {
        if (isFinished()) return getPerformedDuring().getStopTime();
        if (isFailed()) {
            if (getAlternative() == null) {
                // Makes no sense but the assignment said so...
                return getPerformedDuring().getStopTime();
            }
            else {
                return getAlternative().getEstimatedOrRealFinishDate(currentDate);
            }
        }

        LocalDateTime lastOfDependencies = getLatestEstimatedOrRealFinishDateOfDependencies(currentDate);
        LocalDateTime now = currentDate;
        if (lastOfDependencies.isBefore(now)) {
            lastOfDependencies = now;
        }

        return addWorkingMinutes(lastOfDependencies, (long) getEstimatedDuration());
    }

    private static LocalDateTime addWorkingMinutes(LocalDateTime date, long minutes) {
        // Take care of weekends
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            date = date.plusDays(2).withHour(8).withMinute(0).withSecond(0);
        } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.plusDays(1).withHour(8).withMinute(0).withSecond(0);
        }

        if (date.getHour() < 8) {
            // Working day hasn't started. Reset date to start of this working day
            date = date.withHour(8).withMinute(0).withSecond(0);
        }

        LocalDateTime endOfCurrentWorkingDay = date.withHour(16).withMinute(0).withSecond(0);

        // Get minutes from date to endOfCurrentWorkingDay
        long minutesCovered = ChronoUnit.MINUTES.between(date, endOfCurrentWorkingDay);
        if (minutesCovered >= minutes) {
            // If minutesCovered covers the minutes value passed, that means result is the same working
            // day. Just add minutes and return
            return date.plusMinutes(minutes);
        } else {
            // Calculate remainingMinutes, and then recursively call this method with next working day
            long remainingMinutes = minutes - minutesCovered;
            return addWorkingMinutes(endOfCurrentWorkingDay.plusDays(1).withHour(8).withMinute(0).withSecond(0), remainingMinutes);
        }
    }

    private LocalDateTime getLatestEstimatedOrRealFinishDateOfDependencies(LocalDateTime currentDate) {
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
    public void setEstimatedDuration(double estimatedDuration) {
        if (!canHaveAsEstimatedDuration(estimatedDuration)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DURATION); }
        this.estimatedDuration = estimatedDuration;
    }

    private boolean containsDependency(Task dependency) {
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
     * Retrieves the alternative Task for this Task for when this Task has failed.
     *
     * @return The alternative Task.
     */
    public Task getAlternative() {
        return alternative;
    }

    /**
     * Checks whether of not the given Task is a valid alternative for this Task, this Task can't have an alternative Task when the new alternative is null, when this Task already has a alternative
     * and when the new alternative Task doesn't create a dependency loop when the new alternative is replaced by this Task, this Task has to be failed before you can set an alternative.
     *
     * @param alternative
     *            The Task to be checked as possible alternative for this Task.
     * @return Returns true if the given Task can be an alternative for this Task.
     */
    public boolean canHaveAsAlternative(Task alternative) {
        if (alternative == null) { return false; }
        if (this.alternative != null) { return false; }
        if (!status.canHaveAsAlternative(alternative)) { return false; }
        if (alternative.containsDependency(this)) { return false; }
        if (this == alternative) { return false; }
        return true;
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

    void setAlternative(Task alternative) {
        if (!canHaveAsAlternative(alternative)) { throw new IllegalArgumentException(ERROR_ILLEGAL_ALTERNATIVE); }
        this.alternative = alternative;
    }

    /**
     * Retrieves the TimePeriod for when this Task was performed.
     *
     * @return Returns a TimePeriod for when the Task was performed or null if the Task isn't failed of finished yet.
     */
    public TimePeriod getPerformedDuring() {
        return performedDuring;
    }

    /**
     * Checks whether or not this Task can be performed during the given timespan.
     *
     * @param timespan
     *            The TimePeriod that has to be checked.
     * @return Returns true if this Task could be performed during the given TimePeriod.
     */
    protected boolean canHaveBeenPerfomedDuring(TimePeriod timespan) {
        return timespan != null && performedDuring == null;
    }

    /**
     * Sets the timespan for this Task for when it was performed.
     *
     * @param timespan
     *            The TimePeriod for when during the Task was performed.
     */
    void performedDuring(TimePeriod timespan) {
        this.performedDuring = timespan;
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

    /**
     * Checks whether or not this Task can finish.
     *
     * @return Returns true if this Task's can be finished.
     */
    public boolean canFinish() {
        return status.canFinish();
    }

    /**
     * Checks if this Task is finished or if the Task has failed, if it's alternative has finished.
     *
     * @return Returns true if this task or it's alternative is finished.
     */
    public boolean isFinishedOrHasFinishedAlternative() {
        if (isFinished()) return true;

        if (getAlternative() == null) return false;

        return getAlternative().isFinishedOrHasFinishedAlternative();
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
    public void finish(TimePeriod period) {
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
    public void fail(TimePeriod period) {
        status.fail(period);
    }

    private long getRealDurationMs() {
        return ChronoUnit.MILLIS.between(
                getPerformedDuring().getStartTime(),
                getPerformedDuring().getStopTime()
                );
    }

    protected double getRealDuration() {
        return (double) getRealDurationMs() / 1000 / 60;
    }

    protected double getBestDuration() {
        return getEstimatedDuration() - getEstimatedDuration() * getAcceptableDeviation();
    }

    protected double getWorstDuration() {
        return getEstimatedDuration() + getEstimatedDuration() * getAcceptableDeviation();
    }

    /**
     * Checks whether or not this Task was finished before the estimated duration minus the acceptable deviation.
     *
     * @return Returns true if the Task has finished before the estimated duration minus the acceptable deviation. Otherwise it returns false, also when it hasn't finished yet.
     */
    public boolean wasFinishedEarly() {
        if (!isFinished()) return false;

        return getRealDuration() < getBestDuration();
    }

    /**
     * Checks whether or not this Task was finished within the acceptable deviation.
     *
     * @return Returns true if this Task has finished within the acceptable deviation. Otherwise it returns false, also when it hasn't finished yet.
     */
    public boolean wasFinishedOnTime() {
        if (!isFinished()) return false;

        double realDuration = getRealDuration();
        return realDuration >= getBestDuration() && realDuration <= getWorstDuration();
    }

    /**
     * Checks whether or not this Task was finished after the estimated duration plus the acceptable deviation.
     *
     * @return Returns true if the Task has finished before after the estimated duration plus the acceptable deviation. Otherwise it returns false, also when it hasn't finished yet.
     */
    public boolean wasFinishedLate() {
        if (!isFinished()) return false;

        return getRealDuration() > getWorstDuration();
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

    private static final String ERROR_ILLEGAL_DESCRIPTION  = "Illegal project for task.";
    private static final String ERROR_ILLEGAL_DEVIATION    = "Illegal acceptable deviation for task.";
    private static final String ERROR_ILLEGAL_DURATION     = "Illegal estimated duration for task.";
    private static final String ERROR_ILLEGAL_STATUS       = "Illegal status for task.";
    private static final String ERROR_ILLEGAL_ALTERNATIVE  = "Illegal original for task.";
    private static final String ERROR_ILLEGAL_DEPENDENCY   = "Illegal dependency set for task.";
    private static final String ERROR_ILLEGAL_REQUIREMENTS = "Illegal requirement set for task.";
}
