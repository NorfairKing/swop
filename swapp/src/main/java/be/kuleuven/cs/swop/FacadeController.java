package be.kuleuven.cs.swop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.Yaml;
import be.kuleuven.cs.swop.domain.ProjectManager;
import be.kuleuven.cs.swop.domain.project.Project;
import be.kuleuven.cs.swop.domain.task.Task;

public class FacadeController {
	ProjectManager projectManager;

	public FacadeController(){
		projectManager = new ProjectManager();
	}

	public Set<Project> getProjects() {
		return projectManager.getProjects();
	}
	
	public void createProject(String title, String description, Date dueTime){
		projectManager.createProject(title, description, dueTime);
	}

	@SuppressWarnings("unchecked")
	public void initialiseWith(String initFile){
		try {
			System.out.println("Importing: " + initFile);
			InputStream input = new FileInputStream(new File(initFile));
			Yaml yaml = new Yaml();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Map<String, List<Map<String,Object>>> parsedFile = (Map<String, List<Map<String,Object>>>) yaml.load(input);
			List<Project> projects = new ArrayList<Project>();
			List<Task> tasks = new ArrayList<Task>();
			for(Map<String,Object> project : parsedFile.get("projects")){
				Project p = projectManager.createProject(
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
					switch(status){
					case "finished":
						temp.finish();
						break;
					case "failed":
						temp.fail();
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
}
