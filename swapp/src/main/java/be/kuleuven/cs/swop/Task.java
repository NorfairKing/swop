package be.kuleuven.cs.swop;

public class Task {

	private String description;
	private Project project;
	private Duration estimatedDuration;
	private TaskStatus status;

	public String getDescription() {
		return description;

	}

	public static boolean canHaveAsDescription(String description) {
		return description != null;
	}

	public void setDescription(String description) {
		if (!canHaveAsDescription(description)){ throw new IllegalArgumentException(ERROR_ILLEGAL_DESCRIPTION);}
		this.description = description;
	}

	public Project getProject() {
		return project;
	}

	public static boolean canHaveAsProject(Project project) {
		return project != null;
	}

	public void setProject(Project project) {
		if (!canHaveAsProject(project)){throw new IllegalArgumentException(ERROR_ILLEGAL_PROJECT);}
		this.project = project;
	}

	public Duration getEstimatedDuration() {
		return estimatedDuration;
	}

	public static boolean canHaveAsEstimatedDuration(Duration estimatedDuration) {
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

	private static boolean canHaveAsStatus(TaskStatus status) {
		return status != null;
	}

	public void setStatus(TaskStatus status) {
		if (!canHaveAsStatus(status)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_STATUS);
		}
		this.status = status;
	}

	private static final String ERROR_ILLEGAL_DESCRIPTION = "Illegal project for task.";
	private static final String ERROR_ILLEGAL_PROJECT = "Illegal project for task.";
	private static final String ERROR_ILLEGAL_DURATION = "Illegal estimated duration for task.";
	private static final String ERROR_ILLEGAL_STATUS = "Illegal status for task.";
}
