package be.kuleuven.cs.swop.domain.task;


import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.task.status.TaskStatus;
import be.kuleuven.cs.swop.domain.task.status.AvailableStatus;
import be.kuleuven.cs.swop.domain.task.status.UnavailableStatus;


public class Task{

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
        updateAvailability();
    }

    public String getDescription() {
        return description;
    }

    
    public static boolean canHaveAsDescription(String description) {
        return description != null;
    }
    
    public void setDescription(String description) {
        if (!Task.canHaveAsDescription(description)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DESCRIPTION); }
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

    public static boolean canHaveAsEstimatedDuration(double estimatedDuration) {
        return estimatedDuration > 0;
    }
    
    public double getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(double estimatedDuration) {
        if (!Task.canHaveAsEstimatedDuration(estimatedDuration)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DURATION); }
        this.estimatedDuration = estimatedDuration;
    }

    public static boolean canHaveAsDependency(Task dependency) {
        return dependency != null;
    }
    
    public void addDependency(Task dependency) {
        if (!Task.canHaveAsDependency(dependency)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DEPENDENCY); }
        this.dependencies.add(dependency);
        updateAvailability();
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
        if (!Task.canHaveAsDeviation(acceptableDeviation)) throw new IllegalArgumentException(ERROR_ILLEGAL_DEVIATION);
        this.acceptableDeviation = acceptableDeviation;
    }

    public static boolean canHaveAsAlternative(Task alternative) {
        return true;
    }
    
    public Task getAlternative() {
        return alternative;
    }

    public void setAlternative(Task alternative) {
        if (!Task.canHaveAsAlternative(alternative)) throw new IllegalArgumentException(ERROR_ILLEGAL_ALTERNATIVE);
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
        return this.dependencies;
    }

    private TaskStatus getStatus() {
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

    private void updateAvailability() {
        TaskStatus status;
        if (hasUnfinishedDependencies()) {
            status = new UnavailableStatus(this);
        } else {
            status = new AvailableStatus(this);
        }
        setStatus(status);
    }

    private boolean hasUnfinishedDependencies() {
        if (dependencies.isEmpty()) { return false; }
        for (Task current : dependencies) {
            if (!current.getStatus().isFinished()) { return true; }
        }
        return false;
    }

    private static final String ERROR_ILLEGAL_DESCRIPTION = "Illegal project for task.";
    private static final String ERROR_ILLEGAL_DEVIATION = "Illegal acceptable deviation for task.";
    private static final String ERROR_ILLEGAL_DURATION = "Illegal estimated duration for task.";
    private static final String ERROR_ILLEGAL_STATUS = "Illegal status for task.";
    private static final String ERROR_ILLEGAL_ALTERNATIVE = "Illegal original for task.";
    private static final String ERROR_ILLEGAL_DEPENDENCY = "Illegal dependency set for task.";
}
