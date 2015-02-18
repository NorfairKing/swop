package be.kuleuven.cs.swop;

import java.util.Set;

public class Task {

	private String description;
	private Project project;
	private Duration estimatedDuration;
	private TaskStatus status;
	private Set<Task> dependencies;
	private Task original;

	public String getDescription() {
		return description;
	}

	protected static boolean canHaveAsDescription(String description) {
		return description != null;
	}

	private void setDescription(String description) {
		if (!canHaveAsDescription(description)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_DESCRIPTION);
		}
		this.description = description;
	}

	public Project getProject() {
		return project;
	}

	protected static boolean canHaveAsProject(Project project) {
		return project != null;
	}

	public void setProject(Project project) {
		if (!canHaveAsProject(project)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_PROJECT);
		}
		this.project = project;
	}

	public Duration getEstimatedDuration() {
		return estimatedDuration;
	}

	protected static boolean canHaveAsEstimatedDuration(Duration estimatedDuration) {
		return estimatedDuration != null;
	}

	public void setEstimatedDuration(Duration estimatedDuration) {
		if (!canHaveAsEstimatedDuration(estimatedDuration)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_DURATION);
		}
		this.estimatedDuration = estimatedDuration;
	}

	public TaskStatus getStatus() {
		return status;
	}

	protected static boolean canHaveAsStatus(TaskStatus status) {
		return status != null;
	}

	public void setStatus(TaskStatus status) {
		if (!canHaveAsStatus(status)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_STATUS);
		}
		this.status = status;
	}

	public Task getOriginal() {
		return original;
	}

	protected static boolean canHaveAsOriginal(Task original) {
		return true;
	}

	public void setOriginal(Task original) {
		if (!canHaveAsOriginal(original)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_ORIGINAL);
		}
		this.original = original;
	}

	public Set<Task> getDependencySet() {
		return dependencies;
	}

	private static boolean canHaveAsDependencySet(Set<Task> dependencyList) {
		return dependencyList != null;
	}

	public void setDependencySet(Set<Task> dependencySet) {
		if (!canHaveAsDependencySet(dependencySet)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_DEPENDENCY_SET);
		}
		this.dependencies = dependencySet;
	}

	public static boolean canHaveAsDependency(Task dependency) {
		return dependency != null;
	}

	public void addDependency(Task dependency) {
		if (!canHaveAsDependency(dependency)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_DEPENDENCY);
		}
		this.dependencies.add(dependency);
	}

	private static final String ERROR_ILLEGAL_DESCRIPTION = "Illegal project for task.";
	private static final String ERROR_ILLEGAL_PROJECT = "Illegal project for task.";
	private static final String ERROR_ILLEGAL_DURATION = "Illegal estimated duration for task.";
	private static final String ERROR_ILLEGAL_STATUS = "Illegal status for task.";
	private static final String ERROR_ILLEGAL_ORIGINAL = "Illegal original for task.";
	private static final String ERROR_ILLEGAL_DEPENDENCY = "Illegal dependency set for task.";
	private static final String ERROR_ILLEGAL_DEPENDENCY_SET = "Illegal dependency for task.";

}
