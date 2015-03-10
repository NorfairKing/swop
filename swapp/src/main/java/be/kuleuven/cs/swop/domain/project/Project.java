package be.kuleuven.cs.swop.domain.project;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.task.Task;


public class Project{

    private String title;
    private String description;
    private Date creationTime;
    private Date dueTime;
    private final Set<Task> tasks = new HashSet<Task>();

    /**
     * Full constructor
     *
     * @param title
     *            The title for the project.
     * @param description
     *            The description for the project.
     * @param creationTime
     *            The date on which the project was created.
     * @param dueTime
     *            The date on which the project should be finished.
     *
     */
    public Project(String title, String description, Date creationTime, Date dueTime) {
        setTitle(title);
        setDescription(description);
        setCreationTime(creationTime);
        setDueTime(dueTime);
    }

    /**
     * Return the title of this project.
     *
     * @return This projects title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Checks if this a title is valid as title for this project.
     *
     * @param title The title to be checked for validity.
     *
     * @return Returns true if the given title is valid.
     *
     */
    protected boolean canHaveAsTitle(String title) {
        return title != null && title.matches(TITLE_REGEX);
    }

    /**
     * Changes this project's title to the give title.
     *
     * @param title The new title.
     *
     * @throws IllegalArgumentException If the new title isn't valid.
     *
     */
    public void setTitle(String title) {
        if (!canHaveAsTitle(title)) { throw new IllegalArgumentException(ERROR_ILLEGAL_TITLE); }
        this.title = title;
    }

    /**
     * Returns the project's description.
     *
     * @return The project's description.
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Is the given string a proper description for a project?
     *
     * @param description The description for the project.
     *
     * @return Any description will do. | true
     */
    protected boolean canHaveAsDescription(String description) {
        return description != null;
    }

    /**
     *  Changes this project's description to the given description.
     *
     *  @param description The new description.
     *
     *  @throws IllegalArgumentException If the given description isn't valid.
     *
     */
    public void setDescription(String description) {
        if (!canHaveAsDescription(description)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DESCRIPTION); }
        this.description = description;
    }

    /**
     * Checks if the project is still ongoing and has unfinished work.
     *
     * @return Returns true if there is unfinished work.
     *
     */
    public boolean isOngoing(){
        if (getTasks().isEmpty()) return true;

        boolean stillUnfinishedWork = false;
        for (Task task : getTasks()){
            if (!task.isFinishedOrHasFinishedAlternative()){
            	stillUnfinishedWork = true;
            }
        }

        return stillUnfinishedWork;
    }

    /**
     * Checks if the project is finished.
     *
     * @return Returns true if the project is finished.
     *
     */
    public boolean isFinished(){
        return !isOngoing();
    }

    /**
     * Retrieves the project's date of creation.
     *
     * @return The creation Date
     *
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     *
     * @param creationTime The creation time to be checked for validity.
     * @return Must be a valid time | creationTime != null;
     */
    protected boolean canHaveAsCreationTime(Date creationTime) {
        return creationTime != null;
    }

    private void setCreationTime(Date creationTime) {
        if (!canHaveAsCreationTime(creationTime)) { throw new IllegalArgumentException(ERROR_ILLEGAL_CREATIONTIME); }
        this.creationTime = creationTime;
    }

    /**
     * Retrieves the date of when the project is due to be finished.
     *
     * @return The project's due Date
     *
     */
    public Date getDueTime() {
        return (Date) dueTime.clone();
    }

    /**
     *
     * @param dueTime
     * @return The dueTime must be after the creation of this project.
     *
     */
    protected boolean canHaveAsDueTime(Date dueTime) {
        return dueTime != null && dueTime.after(getCreationTime());
    }

    /**
     * Changes the project's Date to the given Date.
     *
     * @param dueTime The new Date.
     *
     * @throws IllegalArgumentException If the new Date isn't valid.
     *
     */
    public void setDueTime(Date dueTime) {
        if (!canHaveAsDueTime(dueTime)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DUETIME); }
        this.dueTime = dueTime;
    }

    /**
     * Can this task be added to a project?
     *
     * @param task The Task to be checked for validity.
     * @return Any task that's not yet assigned to an other project can be added.
     */
    public static boolean canHaveAsTask(Task task) {
        return task != null;
    }

    /**
     * Adds the given Task to this Project.
     *
     * @param task The Task to be added to this Project
     *
     */
    public void addTask(Task task) {
        if (!Project.canHaveAsTask(task)) { throw new IllegalArgumentException(ERROR_ILLEGAL_TASK); }
        tasks.add(task);
    }

    /**
     * Creates a new Task and adds it do the Project.
     *
     * @param description The description for the new Task.
     *
     * @param estimatedDuration The estimated duration it will take to complete the Task.
     *
     * @param acceptableDeviation The acceptable deviation of time for completing the Task.
     *
     */
    public void createTask(String description, double estimatedDuration, double acceptableDeviation) {
    	Task newTask = new Task(description, estimatedDuration, acceptableDeviation);
    	addTask(newTask);
    }

    /**
     * Retrieves the a Set containing every Task belonging to this Project.
     *
     * @return Returns the Set of Tasks.
     *
     */
    public Set<Task> getTasks(){
        return ImmutableSet.copyOf(tasks);
    }

    private static final String ERROR_ILLEGAL_TITLE = "Illegal title for project.";
    private static final String ERROR_ILLEGAL_DESCRIPTION = "Illegal description for project.";
    private static final String ERROR_ILLEGAL_CREATIONTIME = "Illegal creation time for project.";
    private static final String ERROR_ILLEGAL_DUETIME = "Illegal due time for project.";
    private static final String ERROR_ILLEGAL_TASK = "Illegal task for project.";

    public static final String TITLE_REGEX = "[\\w :]+$";

}
