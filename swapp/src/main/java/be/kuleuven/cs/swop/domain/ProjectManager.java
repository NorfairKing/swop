package be.kuleuven.cs.swop.domain;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.project.Project;
import be.kuleuven.cs.swop.domain.task.Task;


public class ProjectManager {

    private final Set<Project> projects = new HashSet<Project>();

    /**
     * Full constructor
     */
    public ProjectManager() {}

    /**
     * Full constructor
     *
     * @param initialisationFilePath
     *            This is the String containing the file path for the yaml file which contains the initial situation for the Project Manager.
     */
    public ProjectManager(String initialisationFilePath) {
        this();
        initialiseWith(initialisationFilePath);
    }

    /**
     * Retrieves the all the projects this program has to manage.
     *
     * @return Returns a Set containing the project's this manager manages.
     */
    public Set<Project> getProjects() {
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
     * Creates and returns a new Project with the given arguments.
     *
     * @param title
     *            A String containing the title for the new Project.
     *
     * @param description
     *            A String containing the description for the new Project.
     *
     * @param dueTime
     *            A Date containing the time for when the Project is due to be completed.
     *
     * @return Returns the newly created Project.
     */
    public Project createProject(String title, String description, Date dueTime) {
        Date creationTime = Timekeeper.getTime();
        return createProject(title, description, creationTime, dueTime);
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
    public Project createProject(String title, String description, Date creationTime, Date dueTime) {
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

    @SuppressWarnings("unchecked")
    private void initialiseWith(String initFile) {
        try {
            System.out.println("Importing: " + initFile);
            InputStream input = new FileInputStream(new File(initFile));
            Yaml yaml = new Yaml();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Map<String, List<Map<String, Object>>> parsedFile = (Map<String, List<Map<String, Object>>>) yaml.load(input);
            List<Project> projects = new ArrayList<Project>();
            List<Task> tasks = new ArrayList<Task>();
            for (Map<String, Object> project : parsedFile.get("projects")) {
                Project p = createProject((String) project.get("name"), (String) project.get("description"), format.parse((String) project.get("creationTime")),
                        format.parse((String) project.get("dueTime")));
                projects.add(p);
            }
            for (Map<String, Object> task : parsedFile.get("tasks")) {
                // Get Project and add the task
                int projectIndex = (int) task.get("project");
                Task temp = projects.get(projectIndex).createTask((String) task.get("description"), (double) (int) task.get("estimatedDuration"), (double) (int) task.get("acceptableDeviation")/100);

                // Set Status
                String status = (String) task.get("status");
                if (status != null) {
                    Date startTime = format.parse((String) task.get("startTime"));
                    Date endTime = format.parse((String) task.get("endTime"));
                    TimePeriod timePeriod = new TimePeriod(startTime, endTime);

                    switch (status) {
                        case "finished":
                            temp.finish(timePeriod);
                            break;
                        case "failed":
                            temp.fail(timePeriod);
                            break;
                        default:
                            break;
                    }
                }

                // Add prerequisites
                List<Integer> prerequisites = (List<Integer>) task.get("prerequisiteTasks");
                if (prerequisites != null) {
                    for (int prereq : prerequisites) {
                        temp.addDependency(tasks.get(prereq));
                    }
                }

                // Add as alternative
                Object alter = task.get("alternativeFor");
                if (alter != null) {
                    tasks.get((int) alter).setAlternative(temp);
                }

                // Add to tracking list
                tasks.add(temp);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't import data from file: Not found.");
        } catch (ParseException e) {
            System.out.println("Couldn't import data from file: Invalid date format");
            e.printStackTrace();
        }
    }

    private static final String ERROR_ILLEGAL_PROJECT = "Invalid project for project manager";
}
