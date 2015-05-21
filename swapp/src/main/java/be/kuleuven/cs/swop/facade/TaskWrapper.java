package be.kuleuven.cs.swop.facade;


import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.task.Task;


public class TaskWrapper {

    private Task task;

    /**
     * Full contstructor
     *
     * @param task The Task this wrapper has to contain.
     *
     */
    public TaskWrapper(Task task) {
        setTask(task);
    }

    /**
     * Retrieves the the Task contained by this wrapper.
     *
     * @return Returns the Task contained by this TaskWrapper.
     *
     */
    Task getTask() {
        return task;
    }

    /**
     * Checks whether or not this wrapper can wrap around the given Task,
     * the TaskWrapper can't have the given Task if it's null.
     *
     * @param task The Task to be checked for validity.
     *
     * @return Returns true if the given Task is valid.
     *
     */
    protected boolean canHaveAsTask(Task task) {
        return task != null;
    }

    private void setTask(Task task) {
        if (!canHaveAsTask(task)) throw new IllegalArgumentException(ERROR_ILLEGAL_TASK);
        this.task = task;
    }

    /**
     * Retrieve the description of the Task contained by this wrapper.
     *
     * @return Returns a String containing the Task's description.
     *
     */
    public String getDescription() {
        return getTask().getDescription();
    }

    /**
     * Retrieve the contained Task's estimated duration in minutes.
     *
     * @return Returns a double which represents the Task's estimated duration in minutes.
     *
     */
    public double getEstimatedDuration() {
        return getTask().getEstimatedDuration();
    }

    /**
     * Retrieve the contained Task's acceptable deviation in minutes.
     *
     * @return Returns a double which represents the Task's acceptable deviation in minutes.
     *
     */
    public double getAcceptableDeviation() {
        return getTask().getAcceptableDeviation();
    }

    /**
     * Retrieves the containing Task's alternative Task for when the Task has failed.
     *
     * @return Returns a TaskWrapper containing the alternative Task.
     *         Returns null when the containing Task doesn't have an alternative.
     *
     */
    public TaskWrapper getAlternative() {
        Task alt = getTask().getAlternative();
        if (alt == null) {
            return null;
        } else {
            return new TaskWrapper(getTask().getAlternative());
        }
    }

    /**
     * Retrieves the containing Task's dependencies.
     *
     * @return Returns a Set containing the TaskWrappers which contain the  dependencies of this Task.
     *
     */
    public Set<TaskWrapper> getDependencySet() {
        Set<TaskWrapper> result = new HashSet<TaskWrapper>();
        for (Task realTask : getTask().getDependencySet()) {
            result.add(new TaskWrapper(realTask));
        }
        return result;
    }
    
    public Set<Requirement> getRequirements(){
        return getTask().getRequirements();
    }
    
    public Set<Requirement> getRecursiveRequirements(){
    	return getTask().getRecursiveRequirements();
    }

    /**
     * Checks whether or not the containing Task is finished.
     *
     * @return Returns true if the containing Task is finished.
     *
     */
    public boolean isFinished() {
        return getTask().isFinished();
    }

    /**
     * Checks whether or not the containing Task is failed.
     *
     * @return Returns true if the containing Task is failed.
     *
     */
    public boolean isFailed() {
        return getTask().isFailed();
    }
    
    public boolean isExecuting(){
    	return getTask().isExecuting();
    }
    
    public boolean isFinal(){
    	return getTask().isFinal();
    }

    /**
     * Checks whether or not the containing Task can finish.
     *
     * @return Returns true if the containing Task can finish.
     *
     */
    public boolean canFinish(){
        return getTask().canFinish();
    }

    /**
     * Checks whether or not the containing Task was finished early.
     *
     * @return Returns true if the containing Task was finished early.
     *
     */
    public boolean wasFinishedEarly() {
        return task.wasFinishedEarly();
    }

    /**
     * Checks whether or not the containing Task was finished on time.
     *
     * @return Returns true if the containing Task was finished on time.
     *
     */
    public boolean wasFinishedOnTime() {
        return getTask().wasFinishedOnTime();
    }

    /**
     * Checks whether or not the containing Task was finished late.
     *
     * @return Returns true if the containing Task was finished late.
     *
     */
    public boolean wasFinishedLate() {
        return getTask().wasFinishedLate();
    }
    
    @Override
    public boolean equals(Object o){
    	if(!(o instanceof TaskWrapper)){
    		return false;
    	}
    	return this.getTask().equals(((TaskWrapper) o).getTask());
    }
    
    @Override
    public int hashCode(){
    	return this.task.hashCode() + "wrapper".hashCode();
    	
    }
    
    public TaskPlanning getPlanning(){
    	return getTask().getPlanning();
    }
    public DateTimePeriod getEstimatedOrPlanningPeriod(){
        return task.getEstimatedOrPlanningPeriod();
    }

    
    
    public LocalDateTime getEstimatedOrRealFinishDate(LocalDateTime currentTime) {
        return getTask().getEstimatedOrRealFinishDate(currentTime);
    }
    private final String ERROR_ILLEGAL_TASK = "Illegal task for task wrapper";

}
