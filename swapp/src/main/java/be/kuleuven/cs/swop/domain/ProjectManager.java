package be.kuleuven.cs.swop.domain;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

import be.kuleuven.cs.swop.domain.project.Project;
import be.kuleuven.cs.swop.domain.task.Task;


public class ProjectManager {

    private final Set<Project> projects = new HashSet<Project>();
    private Calendar currentTime;
    
    public ProjectManager() {
    	currentTime = Calendar.getInstance();
    	currentTime.setTimeInMillis(0);
    }
    
    public ProjectManager(String initialisationFilePath) {
    	this();
    	initialiseWith(initialisationFilePath);
    }

    public Set<Project> getProjects() {
        return projects;
    }

    protected boolean canHaveAsProject(Project project) {
        return project != null;
    }
    
    private void addProject(Project project){
        if (!canHaveAsProject(project) ) throw new IllegalArgumentException(ERROR_ILLEGAL_PROJECT);
        projects.add(project);
    }
    
    public Project createProject(String title, String description, Date dueTime) {
    	Date creationTime = currentTime.getTime();
    	return createProject(title, description, creationTime, dueTime);
    }
    
    protected boolean canHaveAsTime(Date time) {
    	return time != null;
    }
    
    public void setTime(Date time) {
    	if (!canHaveAsTime(time)) {
    		throw new IllegalArgumentException("Invalid time for the system.");
    	}
    	currentTime.setTime(time);
    }
    
    
    // Manual setting of creationTime and returning the created project is needed for the importer
    public Project createProject(String title, String description, Date creationTime, Date dueTime){
    	Project project = new Project(title, description, creationTime, dueTime);
    	addProject(project);
    	return project;
    }
    
	@SuppressWarnings("unchecked")
	private void initialiseWith(String initFile){
		try {
			System.out.println("Importing: " + initFile);
			InputStream input = new FileInputStream(new File(initFile));
			Yaml yaml = new Yaml();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Map<String, List<Map<String,Object>>> parsedFile = (Map<String, List<Map<String,Object>>>) yaml.load(input);
			List<Project> projects = new ArrayList<Project>();
			List<Task> tasks = new ArrayList<Task>();
			for(Map<String,Object> project : parsedFile.get("projects")){
				Project p = createProject(
						(String) project.get("name"),
						(String) project.get("description"),
						format.parse((String) project.get("creationTime")),
						format.parse((String) project.get("dueTime"))
						);
				projects.add(p);
			}
			for(Map<String,Object> task : parsedFile.get("tasks")){
				Task temp = new Task(
						(String) task.get("description"),
						(double) (int) task.get("estimatedDuration"),
						(double) (int) task.get("acceptableDeviation")
						);


				// Set Status
				String status = (String) task.get("status");
				if(status != null){
					Date startTime = format.parse((String)task.get("startTime"));
					Date endTime = format.parse((String)task.get("endTime"));
					TimePeriod timePeriod = new TimePeriod(startTime, endTime);
					
					switch(status){
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


				// Add to project
				int projectIndex = (int) task.get("project");
				projects.get(projectIndex).addTask(temp);



				// Add prerequisites
				List<Integer> prerequisites = (List<Integer>) task.get("prerequisiteTasks");
				if(prerequisites != null){
					for(int prereq : prerequisites){
						temp.addDependency(tasks.get(prereq));
					}
				}


				// Add as alternative
				Object alter = task.get("alternativeFor");
				if( alter != null){
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
