package be.kuleuven.cs.swop;


import java.util.Set;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.task.Task;


public class TaskWrapper{

    private Task task;

    public TaskWrapper(Task task) {
        setTask(task);
    }

    Task getTask() {
        return task;
    }

    protected boolean canHaveAsTask(Task task) {
        return task != null;
    }

    private void setTask(Task task) {
        if (!canHaveAsTask(task)) throw new IllegalArgumentException(ERROR_ILLEGAL_TASK);
        this.task = task;
    }
    
    
    
    public String getDescription() {
        return getTask().getDescription();
    }

    public double getEstimatedDuration() {
        return getTask().getEstimatedDuration();
    }

    public double getAcceptableDeviation() {
        return getTask().getAcceptableDeviation();
    }

    public TaskWrapper getAlternative() {
        Task alt = getTask().getAlternative();
        if (alt == null) {
            return null;
        } else {
            return new TaskWrapper(getTask().getAlternative());
        }
    }

    public TimePeriod getPerformedDuring() {
        return getTask().getPerformedDuring();
    }

    public Set<Task> getDependencySet() {
        return getTask().getDependencySet();
    }
    
    public boolean isFinished() {
    	return getTask().isFinished();
    }
    
    public boolean isFailed() {
    	return getTask().isFailed();
    }
    
    public boolean wasFinishedEarly() {
    	return task.wasFinishedEarly();
    }
    
    public boolean wasFinishedOnTime() {
    	return getTask().wasFinishedOnTime();
    }
    
    public boolean wasFinishedLate() {
    	return getTask().wasFinishedLate();
    }

    private final String ERROR_ILLEGAL_TASK = "Illegal task for task wrapper";

}
