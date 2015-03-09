package be.kuleuven.cs.swop.domain.project;


import java.util.Date;
import java.util.Set;
import java.util.UUID;

import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.domain.task.Task;


public interface Project {

    public String getTitle();

    public static boolean canHaveAsTitle(String title) {
        return title != null && title.matches(TITLE_REGEX);
    }

    public void setTitle(String title);

    public String getDescription();

    public static boolean canHaveAsDescription(String description) {
        return description != null;
    }

    public void setDescription(String description);

    public boolean isOngoing();

    public boolean isFinished();

    public Date getCreationTime();

    public static boolean canHaveAsCreationTime(Date creationTime) {
        return creationTime != null;
    }

    public Date getDueTime();

    public void setDueTime(Date dueTime);

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


    public void addTask(Task task);

    public Task createTask(TaskData data);

    public Set<Task> getTasks();

    public UUID getId();

    public static final String TITLE_REGEX = "[\\w :]+$";

}
