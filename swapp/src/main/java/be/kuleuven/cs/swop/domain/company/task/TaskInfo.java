package be.kuleuven.cs.swop.domain.company.task;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.company.resource.Requirements;

import com.google.common.collect.ImmutableSet;


/**
 *
 *
 */
@SuppressWarnings("serial")
public class TaskInfo implements Serializable {

    private final String       description;
    private final long         estimatedDuration;
    private final double       acceptableDeviation;
    private final Set<Task>    dependencies;
    private final Requirements requirements;

    @SuppressWarnings("unused")
    private TaskInfo() {
        description = null;
        estimatedDuration = 0;
        acceptableDeviation = 0;
        dependencies = null;
        requirements = null;
    } // for automatic (de)-serialization

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
    public TaskInfo(String description, long estimatedDuration, double acceptableDeviation, Requirements requirements, Set<Task> dependencies) {
        if (!canHaveAsDescription(description)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DESCRIPTION); }
        this.description = description;
        if (!canHaveAsEstimatedDuration(estimatedDuration)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DURATION); }
        this.estimatedDuration = estimatedDuration;
        if (!canHaveAsDeviation(acceptableDeviation)) throw new IllegalArgumentException(ERROR_ILLEGAL_DEVIATION);
        this.acceptableDeviation = acceptableDeviation;
        if (!canHaveAsRequirements(requirements)) throw new IllegalArgumentException(ERROR_ILLEGAL_REQUIREMENTS);
        this.requirements = requirements;
        if (dependencies.stream().anyMatch(t -> !canHaveAsDependency(t))) throw new IllegalArgumentException(ERROR_ILLEGAL_DEPENDENCY);
        this.dependencies = new HashSet<Task>(dependencies);
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
        return description != null && !description.isEmpty();
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
     * Checks if this task depends on the given task
     *
     * @param dependency
     *            The possible dependency
     * @return Whether or not it actually is
     */
    boolean containsDependency(Task dependency) {
        if (dependency == null) { return false; }
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
        return dependency != null;
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
     * Retrieves the dependencies of this Task.
     *
     * @return Returns a Set containing the Tasks which are dependencies of this Task.
     */
    public ImmutableSet<Task> getDependencySet() {
        return ImmutableSet.copyOf(this.dependencies);
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

    public Requirements getRequirements() {
        return this.requirements;
    }

    protected boolean canHaveAsRequirements(Requirements requirements) {
        return requirements != null
                && !requirements.hasDoubleTypesRequirements()
                && !requirements.hasConflictingRequirements();
    }

    /**
     * Gets all requirements of this task recursively. So doesn't just return the direct requirements of the task, but also the requirements of requirements
     * 
     * @return All requirements
     */
    public Requirements getRecursiveRequirements() {
        return this.getRequirements().getRecursiveRequirements();
    }

    public TaskInfo withoutDependencies() {
        return new TaskInfo(this.getDescription(), this.getEstimatedDuration(), this.getAcceptableDeviation(), this.getRequirements(), new HashSet<Task>());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(acceptableDeviation);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((dependencies == null) ? 0 : dependencies.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + (int) (estimatedDuration ^ (estimatedDuration >>> 32));
        result = prime * result + ((requirements == null) ? 0 : requirements.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TaskInfo other = (TaskInfo) obj;
        if (Double.doubleToLongBits(acceptableDeviation) != Double.doubleToLongBits(other.acceptableDeviation)) return false;
        if (dependencies == null) {
            if (other.dependencies != null) return false;
        } else if (!dependencies.equals(other.dependencies)) return false;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (estimatedDuration != other.estimatedDuration) return false;
        if (requirements == null) {
            if (other.requirements != null) return false;
        } else if (!requirements.equals(other.requirements)) return false;
        return true;
    }

    private static final String ERROR_ILLEGAL_DESCRIPTION  = "Illegal project for task.";
    private static final String ERROR_ILLEGAL_DEVIATION    = "Illegal acceptable deviation for task.";
    private static final String ERROR_ILLEGAL_DURATION     = "Illegal estimated duration for task.";
    private static final String ERROR_ILLEGAL_DEPENDENCY   = "Illegal dependency set for task.";
    private static final String ERROR_ILLEGAL_REQUIREMENTS = "Illegal requirement set for task.";

}
