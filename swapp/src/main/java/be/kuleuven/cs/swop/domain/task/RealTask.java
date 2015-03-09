package be.kuleuven.cs.swop.domain.task;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.task.status.TaskStatus;
import be.kuleuven.cs.swop.domain.task.status.AvailableStatus;
import be.kuleuven.cs.swop.domain.task.status.UnavailableStatus;


public class RealTask implements Task {

    private String description;
    private double estimatedDuration;
    private double acceptableDeviation;
    private Set<RealTask> dependencies = new HashSet<RealTask>();
    private RealTask alternative;
    private TimePeriod performedDuring;
    private TaskStatus status;
    private UUID id;

    public RealTask(String description, double estimatedDuration, double acceptableDeviation) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setAlternative(null);
        updateAvailability();
        setId(UUID.randomUUID());
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
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

    @Override
    public double getEstimatedDuration() {
        return estimatedDuration;
    }

    @Override
    public void setEstimatedDuration(double estimatedDuration) {
        if (!Task.canHaveAsEstimatedDuration(estimatedDuration)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DURATION); }
        this.estimatedDuration = estimatedDuration;
    }

    @Override
    public void addDependency(RealTask dependency) {
        if (!Task.canHaveAsDependency(dependency)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DEPENDENCY); }
        this.dependencies.add(dependency);
        updateAvailability();
    }

    @Override
    public double getAcceptableDeviation() {
        return acceptableDeviation;
    }

    @Override
    public void setAcceptableDeviation(double acceptableDeviation) {
        if (!Task.canHaveAsDeviation(acceptableDeviation)) throw new IllegalArgumentException(ERROR_ILLEGAL_DEVIATION);
        this.acceptableDeviation = acceptableDeviation;
    }

    @Override
    public Task getAlternative() {
        return alternative;
    }

    @Override
    public void setAlternative(RealTask alternative) {
        if (!Task.canHaveAsAlternative(alternative)) throw new IllegalArgumentException(ERROR_ILLEGAL_ALTERNATIVE);
        this.alternative = alternative;
    }

    @Override
    public TimePeriod getPerformedDuring() {
        return performedDuring;
    }

    protected boolean canHaveBeenPerfomedDuring(TimePeriod timespan) {
        return timespan != null && performedDuring == null;
    }

    @Override
    public void performedDuring(TimePeriod timespan) {
        this.performedDuring = timespan;
    }

    @Override
    public Set<RealTask> getDependencySet() {
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

    @Override
    public void finish() {
        TaskStatus status = this.status.finish();
        setStatus(status);
    }

    @Override
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
        for (RealTask current : dependencies) {
            if (!current.getStatus().isFinished()) { return true; }
        }
        return false;
    }

    @Override
    public UUID getId() {
        return id;
    }

    protected boolean canHaveAsID(UUID id) {
        return id != null;
    }

    private void setId(UUID id) {
        if (!canHaveAsID(id)) throw new IllegalArgumentException(ERROR_ILLEGAL_ID);
        this.id = id;
    }

    private static final String ERROR_ILLEGAL_DESCRIPTION = "Illegal project for task.";
    private static final String ERROR_ILLEGAL_DEVIATION = "Illegal acceptable deviation for task.";
    private static final String ERROR_ILLEGAL_DURATION = "Illegal estimated duration for task.";
    private static final String ERROR_ILLEGAL_STATUS = "Illegal status for task.";
    private static final String ERROR_ILLEGAL_ALTERNATIVE = "Illegal original for task.";
    private static final String ERROR_ILLEGAL_DEPENDENCY = "Illegal dependency set for task.";
    private static final String ERROR_ILLEGAL_ID = "Illegal UUID for task";

}
