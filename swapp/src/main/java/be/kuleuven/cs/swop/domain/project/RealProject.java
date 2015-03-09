package be.kuleuven.cs.swop.domain.project;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.domain.task.Task;


public class RealProject implements Project {

    private String title;
    private String description;
    private Date creationTime;
    private Date dueTime;
    private final Set<Task> tasks = new HashSet<Task>();
    private UUID id;

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
    public RealProject(String title, String description, Date creationTime, Date dueTime) {
        setTitle(title);
        setDescription(description);
        setCreationTime(creationTime);
        setDueTime(dueTime);
        setId(UUID.randomUUID());
    }

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        if (!Project.canHaveAsTitle(title)) { throw new IllegalArgumentException(ERROR_ILLEGAL_TITLE); }
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
    public static boolean canHaveAsDescription(String description) {
        return description != null;
    }

    public void setDescription(String description) {
        if (!canHaveAsDescription(description)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DESCRIPTION); }
        this.description = description;
    }
    
    public boolean isOngoing(){
        if (getTasks().isEmpty()) return true;
        
        for (Task t : getTasks()){
            if (true){
                return false; // FIXME, work this out as soon as task is finished.
            }
        }
        
        return false;
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
    public static boolean canHaveAsCreationTime(Date creationTime) {
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

    public void addTask(Task task) {
        if (!Project.canHaveAsTask(task)) { throw new IllegalArgumentException(ERROR_ILLEGAL_TASK); }
        getTasks().add(task);
    }
    
    public Task createTask(TaskData data) {
    	Task newTask = new Task(data.getDescription(), data.getEstimatedDuration(), data.getAcceptableDeviation());
    	addTask(newTask);
    	return newTask;
    }

    public Set<Task> getTasks() {
		return tasks;
	}

	public UUID getId() {
        return id;
    }
	
	protected boolean canHaveAsID(UUID id){
	    return id != null;
	}

    private void setId(UUID id) {
        if (!canHaveAsID(id)) throw new IllegalArgumentException(ERROR_ILLEGAL_ID);
        this.id = id;
    }

    private static final String ERROR_ILLEGAL_TITLE = "Illegal title for project.";
    private static final String ERROR_ILLEGAL_DESCRIPTION = "Illegal description for project.";
    private static final String ERROR_ILLEGAL_CREATIONTIME = "Illegal creation time for project.";
    private static final String ERROR_ILLEGAL_DUETIME = "Illegal due time for project.";
    private static final String ERROR_ILLEGAL_TASK = "Illegal task for project.";
    private static final String ERROR_ILLEGAL_ID = "Illegal UUID for project";
    

}
