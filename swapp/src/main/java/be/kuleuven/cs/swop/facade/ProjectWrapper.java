package be.kuleuven.cs.swop.facade;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.project.Project;
import be.kuleuven.cs.swop.domain.task.Task;


public class ProjectWrapper {

    private Project project;

    /**
     * Full contstructor
     *
     * @param project The Project this wrapper has to contain.
     *
     */
    public ProjectWrapper(Project project) {
        setProject(project);
    }

    /**
     * Retrieves the the Project contained by this wrapper.
     *
     * @return Returns the Project contained by this ProjectWrapper.
     *
     */
    Project getProject() {
        return project;
    }

    /**
     * Checks whether or not this wrapper can wrap around the given Project,
     * the ProjectWrapper can't have the given Project if it's null.
     *
     * @param project The Project to be checked for validity.
     *
     * @return Returns true if the given Project is valid.
     *
     */
    protected boolean canHaveAsProject(Project project) {
        return project != null;
    }

    private void setProject(Project project) {
        if (!canHaveAsProject(project)) throw new IllegalArgumentException(ERROR_ILLEGAL_PROJECT);
        this.project = project;
    }

    /**
     * Retrieve the title of the Project contained by this wrapper.
     *
     * @return Returns a String containing the Project's title.
     *
     */
    public String getTitle() {
        return getProject().getTitle();
    }

    /**
     * Retrieve the description of the Project contained by this wrapper.
     *
     * @return Returns a String containing the Project's description.
     *
     */
    public String getDescription() {
        return getProject().getDescription();
    }

    /**
     * Checks whether or not the contained Project is ongoing.
     *
     * @return Returns true if the containing Project is ongoing.
     *
     */
    public boolean isOngoing() {
        return getProject().isOngoing();
    }

    /**
     * Checks whether or not the contained Project is finished.
     *
     * @return Returns true if the containing Project is finished.
     *
     */
    public boolean isFinished() {
        return getProject().isFinished();
    }

    /**
     * Retrieve the creation time of the Project contained by this wrapper.
     *
     * @return Returns the Date containing the Project's creation time.
     *
     */
    public LocalDateTime getCreationTime() {
        return getProject().getCreationTime();
    }

    /**
     * Retrieve the due time of the Project contained by this wrapper.
     *
     * @return Returns the Date containing the Project's due time.
     *
     */
    public LocalDateTime getDueTime() {
        return getProject().getDueTime();
    }

    /**
     * Checks whether or not the contained Project is on time.
     *
     * @return Returns true if the containing Project is on time.
     *
     */
    public boolean isOnTime(LocalDateTime currentDate){
        return getProject().isOnTime(currentDate);
    }

    /**
     * Checks whether or not the contained Project isn't on time.
     *
     * @return Returns true if the containing Project isn't on time.
     *
     */
    public boolean isOverTime(LocalDateTime currentDate){
        return getProject().isOverTime(currentDate);
    }

    /**
     * Retrieve the Tasks of the Project contained by this wrapper.
     *
     * @return Returns a Set containing TaskWrappers of the Tasks from the Project
     * contained by this wrapper.
     *
     */
    public Set<TaskWrapper> getTasks() {
        Set<TaskWrapper> result = new HashSet<TaskWrapper>();
        for (Task realTask : project.getTasks()) {
            result.add(new TaskWrapper(realTask));
        }
        return result;
    }
    
    /**
     * Get the estimated finish time for this project
     * @return The estimated finish time.
     */
    public LocalDateTime estimatedFinishTime(LocalDateTime currentTime){
        return project.estimatedFinishTime(currentTime);
    }

    private final String ERROR_ILLEGAL_PROJECT = "Illegal project for project wrapper";
}
