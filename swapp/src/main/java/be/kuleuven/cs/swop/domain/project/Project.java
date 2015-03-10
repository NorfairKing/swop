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
     *            The description of the projects.
     * @param status
     *            The starting status of this project.
     * @param creationTime
     *            The date on which the project was created.
     * @param dueTime
     *            The date on which the project should be finished.
     * 
     * @effect | setTitle(title); | setDescription(description); |
     *         setCreationTime(creationTime); | setDueTime(dueTime);
     */
    public Project(String title, String description, Date creationTime, Date dueTime) {
        setTitle(title);
        setDescription(description);
        setCreationTime(creationTime);
        setDueTime(dueTime);
    }

    public String getTitle() {
        return title;
    }
    
    protected boolean canHaveAsTitle(String title) {
        return title != null && title.matches(TITLE_REGEX);
    }
    
    public void setTitle(String title) {
        if (!canHaveAsTitle(title)) { throw new IllegalArgumentException(ERROR_ILLEGAL_TITLE); }
        this.title = title;
    }


    public String getDescription() {
        return description;
    }
    
    /**
     * Is the given string a proper description for a project?
     * 
     * @param description
     * @return Any description will do. | true
     */
    protected boolean canHaveAsDescription(String description) {
        return description != null;
    }

    public void setDescription(String description) {
        if (!canHaveAsDescription(description)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DESCRIPTION); }
        this.description = description;
    }
    
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
    
    public boolean isFinished(){
        return !isOngoing();
    }

    public Date getCreationTime() {
        return creationTime;
    }
    /**
     * 
     * @param creationTime
     * @return Must be a valid time | creationTime != null;
     */
    protected boolean canHaveAsCreationTime(Date creationTime) {
        return creationTime != null;
    }

    private void setCreationTime(Date creationTime) {
        if (!canHaveAsCreationTime(creationTime)) { throw new IllegalArgumentException(ERROR_ILLEGAL_CREATIONTIME); }
        this.creationTime = creationTime;
    }


    public Date getDueTime() {
        return (Date) dueTime.clone();
    }
    
    /**
     * 
     * @param dueTime
     * @return The dueTime must be after the creation of this project | dueTime
     *         != null && dueTime.after(getCreationTime())
     */
    protected boolean canHaveAsDueTime(Date dueTime) { 
        return dueTime != null && dueTime.after(getCreationTime());
    }

    public void setDueTime(Date dueTime) {
        if (!canHaveAsDueTime(dueTime)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DUETIME); }
        this.dueTime = dueTime;
    }
    
    /**
     * Can this task be added to a project?
     * 
     * @param task
     * @return Any task that's not yet assigned to an other project can be
     *         added. | task != null && task.getProject() == null
     */
    public static boolean canHaveAsTask(Task task) {
        return task != null;
    }

    public void addTask(Task task) {
        if (!Project.canHaveAsTask(task)) { throw new IllegalArgumentException(ERROR_ILLEGAL_TASK); }
        tasks.add(task);
    }
    
    public void createTask(String description, double estimatedDuration, double acceptableDeviation) {
    	Task newTask = new Task(description, estimatedDuration, acceptableDeviation);
    	addTask(newTask);
    }

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
