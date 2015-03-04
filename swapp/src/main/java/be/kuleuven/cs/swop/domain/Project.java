package be.kuleuven.cs.swop.domain;

import be.kuleuven.cs.swop.TaskStatus;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

public class Project {
	
	private String title;
	private String description;
	private Date creationTime;
	private Date dueTime;
	private final Set<Task> tasks = new HashSet<Task>();
	
	/**
	 * Full constructor
	 * @param title The title for the project.
	 * @param description The description of the projects.
	 * @param status The starting status of this project.
	 * @param creationTime The date on which the project was created.
	 * @param dueTime The date on which the project should be finished.
	 * 
	 * @effect
	 * 		| setTitle(title);
	 * 		| setDescription(description);
	 * 		| setCreationTime(creationTime);
	 * 		| setDueTime(dueTime);
	 */
	public Project (String title, String description, Date creationTime, Date dueTime) {
		setTitle(title);
		setDescription(description);
		setCreationTime(creationTime);
		setDueTime(dueTime);
	}
	
	/**
	 * A constructor with only the really necessary information.
	 * @param title
	 * @param description
	 * @param dueTime
	 * 
	 * @effect
	 * 		| this(title, description, ProjectStatus.ONGOING, new Date(), dueTime);
	 */
	public Project(String title, String description, Date dueTime) {
		this(title, description, new Date(), dueTime);
	}
	
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
		return description != null;
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
	
	/**
	 * Get the current status of the projects.
	 * @return The current status of the project, based on tasks.
	 */
	public ProjectStatus getStatus() {
		// text said there had to be tasks for the project to be finished.
		if (tasks.isEmpty()) return ProjectStatus.ONGOING;
		
		for (Task task: tasks) {
			if (task.getStatus() != TaskStatus.FINISHED &&
				task.getStatus() != TaskStatus.FAILED) {
				return ProjectStatus.ONGOING;
			}
		}
		
		return ProjectStatus.FINISHED;
	}
	
	/**
	 * 
	 * @param creationTime
	 * @return Must be a valid time
	 * 		| creationTime != null;
	 */
	public static boolean canHaveAsCreationTime(Date creationTime) {
		return creationTime != null;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	private void setCreationTime(Date creationTime) {
		if (!canHaveAsCreationTime(creationTime)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_CREATIONTIME);
		}
		this.creationTime = creationTime;
	}
	
	/**
	 * 
	 * @param dueTime
	 * @return The dueTime must be after the creation of this project
	 * 		| dueTime != null && dueTime.after(getCreationTime()) 
	 */
	public boolean canHaveAsDueTime(Date dueTime) {
		return dueTime != null && dueTime.after(getCreationTime());
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
	
	/**
	 * Can this task be added to a project?
	 * @param task
	 * @return Any task that's not yet assigned to an other project can be added.
	 * 		| task != null && task.getProject() == null
	 */
	public static boolean canHaveAsTask(Task task) {
		return task != null && task.getProject() == null;
	}
	public void addTask(Task task) {
		if (!canHaveAsTask(task)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_TASK);
		}
		tasks.add(task);
		if(task.getProject() != this) {
			task.setProject(this);
		}
	}

	private static final String ERROR_ILLEGAL_TITLE = "Illegal title for project.";
	private static final String ERROR_ILLEGAL_DESCRIPTION = "Illegal description for project.";
	private static final String ERROR_ILLEGAL_CREATIONTIME = "Illegal creation time for project.";
	private static final String ERROR_ILLEGAL_DUETIME = "Illegal due time for project.";
	private static final String ERROR_ILLEGAL_TASK = "Illegal taks for project.";
	
}
