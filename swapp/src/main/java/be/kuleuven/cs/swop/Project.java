package be.kuleuven.cs.swop;

import java.util.HashSet;
import java.util.Set;
import java.util.Date;

public class Project {
	
	private String title;
	private String description;
	private ProjectStatus status;
	private Date creationTime;
	private Date dueTime;
	private final Set<Task> tasks = new HashSet<Task>();
	
	/**
	 * Is the given string a proper title for a project?
	 * @param title
	 * @return Any string is allowed if not empty nor null.
	 */
	public static boolean canHaveAsTitle(String title) {
		return title != null && !title.isEmpty();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		if (!canHaveAsTitle(title)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_TITLE);
		}
		this.title = title;
	}
	
	/**
	 * Is the given string a proper description for a project?
	 * @param description
	 * @return Any description will do.
	 * 		| true
	 */
	public static boolean canHaveAsDescription(String description) {
		return true;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		if (!canHaveAsDescription(description)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_DESCRIPTION);
		}
		this.description = description;
	}
	
	public static boolean canHaveAsStatus(ProjectStatus status) {
		return true;
	}
	public ProjectStatus getStatus() {
		return status;
	}
	public void setStatus(ProjectStatus status) {
		if (!canHaveAsStatus(status)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_STATUS);
		}
		this.status = status;
	}
	
	public static boolean canHaveAsCreationTime(Date creationTime) {
		return true;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		if (!canHaveAsCreationTime(creationTime)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_CREATIONTIME);
		}
		this.creationTime = creationTime;
	}
	
	public static boolean canHaveAsDueTime(Date dueTime) {
		return true;
	}
	public Date getDueTime() {
		return dueTime;
	}
	public void setDueTime(Date dueTime) {
		if (!canHaveAsDueTime(dueTime)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_DUETIME);
		}
		this.dueTime = dueTime;
	}
	
	public static boolean canHaveAsTask(Task task) {
		return task != null && task.getProject() == null;
	}
	public void addTask(Task task) {
		if (!canHaveAsTask(task)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_TASK);
		}
		tasks.add(task);
	}

	private static final String ERROR_ILLEGAL_TITLE = "Illegal title for project.";
	private static final String ERROR_ILLEGAL_DESCRIPTION = "Illegal description for project.";
	private static final String ERROR_ILLEGAL_STATUS = "Illegal status for project.";
	private static final String ERROR_ILLEGAL_CREATIONTIME = "Illegal creation time for project.";
	private static final String ERROR_ILLEGAL_DUETIME = "Illegal due time for project.";
	private static final String ERROR_ILLEGAL_TASK = "Illegal taks for project.";
	
}
