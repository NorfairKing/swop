package be.kuleuven.cs.swop;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.data.TaskStatusData;
import be.kuleuven.cs.swop.domain.ProjectManager;
import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.project.Project;
import be.kuleuven.cs.swop.domain.task.Task;


public class FacadeController {

    ProjectManager projectManager;

    public FacadeController() {
        projectManager = new ProjectManager();
    }

    public FacadeController(String initialisationFilePath) {
        projectManager = new ProjectManager(initialisationFilePath);
    }

    public Set<ProjectWrapper> getProjects() {
        Set<ProjectWrapper> result = new HashSet<ProjectWrapper>();
        for (Project realProject : projectManager.getProjects()) {
            result.add(new ProjectWrapper(realProject));
        }
        return result;
    }

    public Set<TaskWrapper> getTasksOf(ProjectWrapper project) {
        Set<TaskWrapper> tasks = new HashSet<TaskWrapper>();
        for (Project p : projectManager.getProjects()) {
            for (Task t : p.getTasks()) {
                tasks.add(new TaskWrapper(t));
            }
        }
        return tasks;
    }

    public ProjectWrapper createProject(ProjectData data) {
        if (data == null) { throw new IllegalArgumentException("Null projectdata for project creation"); }
        if (data.getDescription() == null) { throw new IllegalArgumentException("Null description for project creation"); }
        if (data.getTitle() == null) { throw new IllegalArgumentException("Null title for project creation"); }
        if (data.getDueTime() == null) { throw new IllegalArgumentException("Null due time for project creation"); }

        Project createdProject = projectManager.createProject(data.getTitle(), data.getDescription(), data.getDueTime());
        
        return new ProjectWrapper(createdProject);
    }

    public TaskWrapper createTaskFor(ProjectWrapper project, TaskData data) throws IllegalArgumentException {
        if (project == null) { throw new IllegalArgumentException("Null project for task creation"); }
        if (data == null) { throw new IllegalArgumentException("Null task data for task creation"); }

        if (data.getDescription() == null) { throw new IllegalArgumentException("Null description for task creation"); }

        Task createdTask = project.getProject().createTask(data.getDescription(), data.getEstimatedDuration(), data.getAcceptableDeviation());
        for (TaskWrapper dependency: data.getDependencies()) {
            createdTask.addDependency(dependency.getTask());
        }
        
        return new TaskWrapper(createdTask);
    }

    public void updateTaskStatusFor(TaskWrapper task, TaskStatusData statusData) throws IllegalArgumentException {
        if (task == null) { throw new IllegalArgumentException("Null task for status update"); }
        if (statusData == null) { throw new IllegalArgumentException("Null statusdata for status update"); }

        if (statusData.getStartTime() == null) { throw new IllegalArgumentException("Null start time for status update"); }
        if (statusData.getEndTime() == null) { throw new IllegalArgumentException("Null end time for status update"); }

        TimePeriod timePeriod = new TimePeriod((Date) statusData.getStartTime().clone(), (Date) statusData.getEndTime().clone());

        if (statusData.getSuccessful()) {
            task.getTask().finish(timePeriod);
        } else {
            task.getTask().fail(timePeriod);
        }
    }

    public void updateSystemTime(Date time) throws IllegalArgumentException {
        if (time == null) { throw new IllegalArgumentException("Null date for system time update"); }
        projectManager.setTime((Date) time.clone());
    }
}
