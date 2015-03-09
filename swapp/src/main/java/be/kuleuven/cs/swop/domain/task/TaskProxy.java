package be.kuleuven.cs.swop.domain.task;


import java.util.Set;
import java.util.UUID;

import be.kuleuven.cs.swop.domain.TimePeriod;


public class TaskProxy implements Task {

    private Task task;

    public TaskProxy(Task task) {
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

    @Override
    public String getDescription() {
        return getTask().getDescription();
    }

    @Override
    public void setDescription(String description) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    @Override
    public double getEstimatedDuration() {
        return getTask().getEstimatedDuration();
    }

    @Override
    public void setEstimatedDuration(double estimatedDuration) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    @Override
    public void addDependency(RealTask dependency) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    @Override
    public double getAcceptableDeviation() {
        return getTask().getAcceptableDeviation();
    }

    @Override
    public void setAcceptableDeviation(double acceptableDeviation) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    @Override
    public Task getAlternative() {
        Task alt = getTask().getAlternative();
        if (alt == null) {return null;}
        else {return new TaskProxy(getTask().getAlternative());}
    }

    @Override
    public void setAlternative(RealTask alternative) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    @Override
    public TimePeriod getPerformedDuring() {
        return getTask().getPerformedDuring();
    }

    @Override
    public void performedDuring(TimePeriod timespan) {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    @Override
    public Set<RealTask> getDependencySet() {
        return getTask().getDependencySet();
    }

    @Override
    public void finish() {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    @Override
    public void fail() {
        throw new IllegalAccessError(ERROR_ILLEGAL_ACCESS_TASK);
    }

    @Override
    public UUID getId() {
        return getTask().getId();
    }

    private final String ERROR_ILLEGAL_ACCESS_TASK = "AA MOEDER JOENGE";
    private final String ERROR_ILLEGAL_TASK = "Illegal task for task proxy";

}
