package be.kuleuven.cs.swop.domain.task;


import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.task.status.TaskStatus;


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
    }

    public String getDescription() {
        return description;
    }

    protected static boolean canHaveAsDescription(String description) {
        return description != null;
    }

    private void setDescription(String description) {
        if (!canHaveAsDescription(description)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DESCRIPTION); }
        this.description = description;
    }

    /*
     * public Project getProject() { return project; }
     * 
     * protected static boolean canHaveAsProject(Project project) { return
     * project != null; }
     * 
     * public void setProject(Project project) { if (!canHaveAsProject(project))
     * { throw new IllegalArgumentException(ERROR_ILLEGAL_PROJECT); }
     * this.project = project; }
     */

    public double getEstimatedDuration() {
        return estimatedDuration;
    }

    protected static boolean canHaveAsEstimatedDuration(double estimatedDuration) {
        return estimatedDuration > 0;
    }

    public void setEstimatedDuration(double estimatedDuration) {
        if (!canHaveAsEstimatedDuration(estimatedDuration)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DURATION); }
        this.estimatedDuration = estimatedDuration;
    }

    public static boolean canHaveAsDependency(Task dependency) {
        return dependency != null;
    }

    public void addDependency(Task dependency) {
        if (!canHaveAsDependency(dependency)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DEPENDENCY); }
        this.dependencies.add(dependency);
    }

    public double getAcceptableDeviation() {
        return acceptableDeviation;
    }

    public static boolean canHaveAsDeviation(double deviation) {
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

    protected static boolean canHaveAsAlternative(Task alternative) {
        return true;
    }

    public void setAlternative(Task alternative) {
        if (!canHaveAsAlternative(alternative)) throw new IllegalArgumentException(ERROR_ILLEGAL_ALTERNATIVE);
        this.alternative = alternative;
    }

    private TimePeriod getPerformedDuring() {
        return performedDuring;
    }

    protected boolean canHaveBeenPerfomedDuring(TimePeriod timespan) {
        return timespan != null && performedDuring == null;
    }

    public void performedDuring(TimePeriod timespan) {
        this.performedDuring = timespan;
    }

    public Set<Task> getDependencySet() {
        return this.dependencies;
    }

    public TaskStatus getStatus() {
        return status;
    }

    protected boolean canHaveAsStatus(TaskStatus status) {
        return status != null;
    }

    private void setStatus(TaskStatus status) {
        if (!canHaveAsStatus(status)) throw new IllegalArgumentException(ERROR_ILLEGAL_STATUS);
        this.status = status;
    }

    public void finish() {
        TaskStatus status = this.status.finish();
        setStatus(status);
    }

    public void fail() {
        TaskStatus status = this.status.fail();
        setStatus(status);
    }

    private static final String ERROR_ILLEGAL_DESCRIPTION = "Illegal project for task.";
    private static final String ERROR_ILLEGAL_PROJECT = "Illegal project for task.";
    private static final String ERROR_ILLEGAL_DEVIATION = "Illegal acceptable deviation for task.";
    private static final String ERROR_ILLEGAL_DURATION = "Illegal estimated duration for task.";
    private static final String ERROR_ILLEGAL_STATUS = "Illegal status for task.";
    private static final String ERROR_ILLEGAL_ALTERNATIVE = "Illegal original for task.";
    private static final String ERROR_ILLEGAL_DEPENDENCY = "Illegal dependency set for task.";

}
