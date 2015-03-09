package be.kuleuven.cs.swop.domain.task;


import java.util.Set;

import be.kuleuven.cs.swop.domain.TimePeriod;


public class TaskWrapper{

    private Task task;

    public TaskWrapper(Task task) {
        setTask(task);
    }

    public Task getTask() {
        return task;
    }

    protected boolean canHaveAsTask(Task task) {
        return task != null;
    }

    public void setTask(Task task) {
        if (!canHaveAsTask(task)) throw new IllegalArgumentException(ERROR_ILLEGAL_TASK);
        this.task = task;
    }

    public String getDescription() {
        return getTask().getDescription();
    }

    public void setDescription(String description) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    public double getEstimatedDuration() {
        return getTask().getEstimatedDuration();
    }

    public void setEstimatedDuration(double estimatedDuration) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    public void addDependency(Task dependency) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    public double getAcceptableDeviation() {
        return getTask().getAcceptableDeviation();
    }

    public void setAcceptableDeviation(double acceptableDeviation) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    public TaskWrapper getAlternative() {
        Task alt = getTask().getAlternative();
        if (alt == null) {
            return null;
        } else {
            return new TaskWrapper(getTask().getAlternative());
        }
    }

    public void setAlternative(Task alternative) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    public TimePeriod getPerformedDuring() {
        return getTask().getPerformedDuring();
    }

    public void performedDuring(TimePeriod timespan) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    public Set<Task> getDependencySet() {
        return getTask().getDependencySet();
    }

    public void finish() {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    public void fail() {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    private final String ERROR_ILLEGAL_ACCESS_TASK = "AA MOEDER JOENGE";
    private final String ERROR_ILLEGAL_TASK = "Illegal task for task proxy";

}
