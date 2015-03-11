package be.kuleuven.cs.swop.domain.task;


import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.task.status.AvailableStatus;
import be.kuleuven.cs.swop.domain.task.status.FailedStatus;
import be.kuleuven.cs.swop.domain.task.status.FinishedStatus;
import be.kuleuven.cs.swop.domain.task.status.TaskStatus;
import be.kuleuven.cs.swop.domain.task.status.UnavailableStatus;


public class Task {

    private String description;
    private double estimatedDuration;
    private double acceptableDeviation;
    private Set<Task> dependencies = new HashSet<Task>();
    private Task alternative;
    private TimePeriod performedDuring;
    private TaskStatus status;

    public Task(String description, double estimatedDuration, double acceptableDeviation) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setAlternative(null);
        setStatus(new AvailableStatus(this));
    }

    public String getDescription() {
        return description;
    }

    public boolean canHaveAsDescription(String description) {
        return description != null;
    }

    public void setDescription(String description) {
        if (!canHaveAsDescription(description)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DESCRIPTION); }
        this.description = description;
    }

    public boolean canHaveAsEstimatedDuration(double estimatedDuration) {
        return estimatedDuration > 0;
    }

    public double getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(double estimatedDuration) {
        if (!canHaveAsEstimatedDuration(estimatedDuration)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DURATION); }
        this.estimatedDuration = estimatedDuration;
    }

    /**
     * Check whether this task contains a given task as a dependency.
     * 
     * @param dependency
     *            The given possible dependency
     * @return whether the given task is a dependency of this task
     */
    private boolean containsDependency(Task dependency) {
        if (dependency == null) { return true; }
        for (Task subDep : this.getDependencySet()) {
            if (subDep == dependency) { return true; }
            if (subDep.containsDependency(dependency)) { return true; }
        }
        return false;
    }

    public boolean canHaveAsDependency(Task dependency) {
        if (dependency == null) { return false; }
        return (dependency != this) && !dependency.containsDependency(this);
    }

    public void addDependency(Task dependency) {
        if (!canHaveAsDependency(dependency)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DEPENDENCY); }
        if (this.dependencies.contains(dependency)) { return; }
        this.dependencies.add(dependency);
    }

    public double getAcceptableDeviation() {
        return acceptableDeviation;
    }

    public boolean canHaveAsDeviation(double deviation) {
        if (Double.isNaN(deviation)) { return false; }
        if (Double.isInfinite(deviation)) { return false; }
        if (deviation < 0) { return false; }
        return true;
    }

    public void setAcceptableDeviation(double acceptableDeviation) {
        if (!canHaveAsDeviation(acceptableDeviation)) throw new IllegalArgumentException(ERROR_ILLEGAL_DEVIATION);
        this.acceptableDeviation = acceptableDeviation;
    }

    public Task getAlternative() {
        return alternative;
    }

    public boolean canHaveAsAlternative(Task alternative) {
        return true;
    }

    public void setAlternative(Task alternative) {
    	if(this.alternative != null) throw new IllegalArgumentException(ERROR_ALTERNATIVE_ALREADY_SET);
        if (!canHaveAsAlternative(alternative)) throw new IllegalArgumentException(ERROR_ILLEGAL_ALTERNATIVE);
        this.alternative = alternative;
    }

    public TimePeriod getPerformedDuring() {
        return performedDuring;
    }

    protected boolean canHaveBeenPerfomedDuring(TimePeriod timespan) {
        return timespan != null && performedDuring == null;
    }

    public void performedDuring(TimePeriod timespan) {
        this.performedDuring = timespan;
    }

    public Set<Task> getDependencySet() {
        return ImmutableSet.copyOf(this.dependencies);
    }

    private TaskStatus getStatus() {
        if (this.status.isFinal()) {
            return this.status;
        } else {
            if (this.hasUnfinishedDependencies()) {
                return new UnavailableStatus(this);
            } else {
                return new AvailableStatus(this);
            }
        }
    }

    protected boolean canHaveAsStatus(TaskStatus status) {
        return status != null;
    }

    private void setStatus(TaskStatus status) {
        if (!canHaveAsStatus(status)) throw new IllegalArgumentException(ERROR_ILLEGAL_STATUS);
        this.status = status;
    }

    public boolean isFinished() {
        return getStatus().isFinished();
    }

    public boolean isFailed() {
        return getStatus().isFailed();
    }

    public boolean isFinishedOrHasFinishedAlternative() {
        if (isFinished()) return true;

        if (getAlternative() == null) return false;

        return getAlternative().isFinishedOrHasFinishedAlternative();
    }

    public void finish() {
        if (getStatus().canFinish()) {
            setStatus(new FinishedStatus(this));
        } else {
            throw new IllegalStateException("Can't finish when status is " + getStatus().toString());
        }
    }

    public void fail() {
        if (getStatus().canFail()) {
            setStatus(new FailedStatus(this));
        } else {
            throw new IllegalStateException();
        }
    }

    private double getRealDuration() {
        long diffMillies = getPerformedDuring().getStopTime().getTime() - getPerformedDuring().getStartTime().getTime();
        return (double) diffMillies / 1000 / 60;
    }

    private double getBestDuration() {
        return getEstimatedDuration() - getEstimatedDuration() * getAcceptableDeviation();
    }

    private double getWorstDuration() {
        return getEstimatedDuration() + getEstimatedDuration() * getAcceptableDeviation();
    }

    public boolean wasFinishedEarly() {
        if (!isFinished()) return false;

        return getRealDuration() < getBestDuration();
    }

    public boolean wasFinishedOnTime() {
        if (!isFinished()) return false;

        double realDuration = getRealDuration();
        return realDuration >= getBestDuration() && realDuration <= getWorstDuration();
    }

    public boolean wasFinishedLate() {
        if (!isFinished()) return false;

        return getRealDuration() > getWorstDuration();
    }

    private boolean hasUnfinishedDependencies() {
        if (dependencies.isEmpty()) { return false; }
        for (Task t : dependencies) {
            if (!t.isFinished()) { return true; }
        }
        return false;
    }

    private static final String ERROR_ILLEGAL_DESCRIPTION = "Illegal project for task.";
    private static final String ERROR_ILLEGAL_DEVIATION = "Illegal acceptable deviation for task.";
    private static final String ERROR_ILLEGAL_DURATION = "Illegal estimated duration for task.";
    private static final String ERROR_ILLEGAL_STATUS = "Illegal status for task.";
    private static final String ERROR_ILLEGAL_ALTERNATIVE = "Illegal original for task.";
    private static final String ERROR_ALTERNATIVE_ALREADY_SET = "This task already has an alternative.";
    private static final String ERROR_ILLEGAL_DEPENDENCY = "Illegal dependency set for task.";
}
