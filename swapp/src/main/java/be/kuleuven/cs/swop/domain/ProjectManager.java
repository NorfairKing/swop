package be.kuleuven.cs.swop.domain;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.project.Project;
import be.kuleuven.cs.swop.domain.task.Task;

import com.google.common.collect.ImmutableSet;


@SuppressWarnings("serial")
public class ProjectManager implements Serializable {

    private final Set<Project> projects = new HashSet<Project>();

    /**
     * Full constructor
     */
    public ProjectManager() {}

    /**
     * Retrieves the all the projects this program has to manage.
     *
     * @return Returns a Set containing the project's this manager manages.
     */
    public ImmutableSet<Project> getProjects() {
        return ImmutableSet.copyOf(projects);
    }

    /**
     * Checks whethor or not the given project can be managed by this manager, this means the Project can't be null.
     *
     * @param project
     *            The project to be checked for validity.
     *
     * @return Returns true if the given project is valid and therefore isn't null.
     *
     */
    protected boolean canHaveAsProject(Project project) {
        return project != null;
    }

    private void addProject(Project project) {
        if (!canHaveAsProject(project)) throw new IllegalArgumentException(ERROR_ILLEGAL_PROJECT);
        projects.add(project);
    }

    /**
     * Creates and returns a new Project with the given arguments, this method is used by the importer of the yaml file because the creationTime is specified.
     *
     * @param title
     *            A String containing the title for the new Project.
     *
     * @param description
     *            A String containing the description for the new Project.
     *
     * @param creationTime
     *            A Date containing the time when the new Project was created.
     *
     * @param dueTime
     *            A Date containing the time for when the Project is due to be completed.
     *
     * @return Returns the newly created Project.
     */
    public Project createProject(String title, String description, LocalDateTime creationTime, LocalDateTime dueTime) {
        Project project = new Project(title, description, creationTime, dueTime);
        addProject(project);
        return project;
    }

    public Project getProjectFor(Task task) {
        for (Project project : getProjects()) {
            if (project.containsTask(task)) { return project; }
        }

        return null;
    }

    private static final String ERROR_ILLEGAL_PROJECT = "Invalid project for project manager";
}
