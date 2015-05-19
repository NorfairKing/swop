package be.kuleuven.cs.swop.domain.company.project;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.task.TaskInfo;

import com.google.common.collect.ImmutableSet;


@SuppressWarnings("serial")
public class Project implements Serializable {

    private String          title;
    private String          description;
    private LocalDateTime   creationTime;
    private LocalDateTime   dueTime;
    private final Set<Task> tasks = new HashSet<Task>();

    Project() { }
    /**
     * Full constructor
     *
     * @param title
     *            The title for the project.
     * @param description
     *            The description for the project.
     * @param creationTime The date on which the project was created.
     * @param dueTime
     *            The date on which the project should be finished.
     *
     */
    public Project(String title, String description, LocalDateTime creationTime, LocalDateTime dueTime) {
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
     * @param title
     *            The title to be checked for validity.
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
     * @param title
     *            The new title.
     *
     * @throws IllegalArgumentException
     *             If the new title isn't valid.
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
     * @param description
     *            The description for the project.
     *
     * @return Any description will do. | true
     */
    protected boolean canHaveAsDescription(String description) {
        return description != null;
    }

    /**
     * Changes this project's description to the given description.
     *
     * @param description
     *            The new description.
     *
     * @throws IllegalArgumentException
     *             If the given description isn't valid.
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
    public boolean isOngoing() {
        return !isFinished();
    }

    /**
     * Checks if the project is finished.
     *
     * @return Returns true if the project is finished.
     *
     */
    public boolean isFinished() {
        if (getTasks().isEmpty()) return false;

        for (Task task : getTasks()) {
            if (!task.isFinishedOrHasFinishedAlternative()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieves the project's date of creation.
     *
     * @return The creation Date
     *
     */
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    /**
     *
     * @param creationTime
     *            The creation time to be checked for validity.
     * @return Must be a valid time | creationTime != null;
     */
    protected boolean canHaveAsCreationTime(LocalDateTime creationTime) {
        return creationTime != null;
    }

    private void setCreationTime(LocalDateTime creationTime) {
        if (!canHaveAsCreationTime(creationTime)) { throw new IllegalArgumentException(ERROR_ILLEGAL_CREATIONTIME); }
        this.creationTime = creationTime;
    }

    /**
     * Retrieves the date of when the project is due to be finished.
     *
     * @return The project's due Date
     *
     */
    public LocalDateTime getDueTime() {
        return dueTime;
    }

    /**
     * Checks whether or not the given dueTime is valid,
     * this means it can't be null and it has to be after the project's creation time.
     *
     * @param dueTime
     *            The Date to be checked for validity.
     * @return The dueTime must be after the creation of this project.
     *
     */
    protected boolean canHaveAsDueTime(LocalDateTime dueTime) {
        return dueTime != null && dueTime.isAfter(getCreationTime());
    }

    /**
     * Changes the project's Date to the given Date.
     *
     * @param dueTime
     *            The new Date.
     *
     * @throws IllegalArgumentException
     *             If the new Date isn't valid.
     *
     */
    public void setDueTime(LocalDateTime dueTime) {
        if (!canHaveAsDueTime(dueTime)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DUETIME); }
        this.dueTime = dueTime;
    }

    /**
     * Can this task be added to a project?
     *
     * @param task
     *            The Task to be checked for validity.
     * @return Any task that's not yet assigned to an other project can be added.
     */
    public static boolean canHaveAsTask(Task task) {
        return task != null;
    }

    /**
     * Adds the given Task to this Project.
     *
     * @param task
     *            The Task to be added to this Project
     *
     */
    private void addTask(Task task) {
        if (!Project.canHaveAsTask(task)) { throw new IllegalArgumentException(ERROR_ILLEGAL_TASK); }
        tasks.add(task);
    }

    /**
     * Creates a new Task and adds it do the Project.
     *
     * @param description
     *            The description for the new Task.
     *
     * @param estimatedDuration
     *            The estimated duration it will take to complete the Task.
     *
     * @param acceptableDeviation
     *            The acceptable deviation of time for completing the Task.
     *
     * @return The newly created Task.
     *
     */
    public Task createTask(String description, long estimatedDuration, double acceptableDeviation) {
        return createTask(description, estimatedDuration, acceptableDeviation, new Requirements(new HashSet<Requirement>()));
    }
    
    public Task createTask(String description, long estimatedDuration, double acceptableDeviation, Requirements requirements) {
        return createTask(description, estimatedDuration, acceptableDeviation,new HashSet<Task>(), requirements);
    }
    
    public Task createTask(String description, long estimatedDuration, double acceptableDeviation, Set<Task> dependencies, Requirements requirements) {
        TaskInfo info = new TaskInfo(description, estimatedDuration, acceptableDeviation, requirements, dependencies);
    	Task newTask = new Task(info);
        addTask(newTask);
        return newTask;
    }

    /**
     * Retrieves the a Set containing every Task belonging to this Project.
     *
     * @return Returns the Set of Tasks.
     *
     */
    public ImmutableSet<Task> getTasks() {
        return ImmutableSet.copyOf(tasks);
    }

    /**
     * Checks whether or not this Project contains the given Task.
     *
     * @param task The Task that will be checked of this Project contains it.
     *
     * @return Returns true of this Project contains the given Task.
     *
     */
    public boolean containsTask(Task task) {
        return getTasks().contains(task);
    }

    /**
     * Check whether or not this Project has finished on time,
     * or when all Tasks haven't finished yet, whether or not it probably will
     * finish on time.
     *
     * @param currentDate The current system time
     * @return Returns true if this project is on time.
     *
     */
    public boolean isOnTime(LocalDateTime currentDate) {
        return !getDueTime().isBefore(estimatedFinishTime(currentDate));
    }
    
    /**
     * Calculated the estimated finish time for this project.
     * @param currentDate The current system time on which to base the estimation.
     * @return The estimated finish time.
     */
    public LocalDateTime estimatedFinishTime(LocalDateTime currentDate){
        LocalDateTime lastTime = LocalDateTime.MIN;
        for (Task task: getTasks()) {
            LocalDateTime lastTimeOfThis = task.getEstimatedOrRealFinishDate(currentDate);
            if (lastTimeOfThis.isAfter(lastTime)) {
                lastTime = lastTimeOfThis;
            }
        }
        return lastTime;
    }

    /**
     * Check whether or not this Project isn't on time.
     *
     * @param currentDate The current system time
     * @return Returns false when this Project is on time.
     *
     */
    public boolean isOverTime(LocalDateTime currentDate) {
        return !isOnTime(currentDate);
    }

    private static final String ERROR_ILLEGAL_TITLE        = "Illegal title for project.";
    private static final String ERROR_ILLEGAL_DESCRIPTION  = "Illegal description for project.";
    private static final String ERROR_ILLEGAL_CREATIONTIME = "Illegal creation time for project.";
    private static final String ERROR_ILLEGAL_DUETIME      = "Illegal due time for project.";
    private static final String ERROR_ILLEGAL_TASK         = "Illegal task for project.";

    public static final String  TITLE_REGEX                = "[\\w :]+$";

}
