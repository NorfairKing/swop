package be.kuleuven.cs.swop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

import be.kuleuven.cs.swop.facade.DeveloperData;
import be.kuleuven.cs.swop.facade.ExecutingStatusData;
import be.kuleuven.cs.swop.facade.FailedStatusData;
import be.kuleuven.cs.swop.facade.FinishedStatusData;
import be.kuleuven.cs.swop.facade.ResourceData;
import be.kuleuven.cs.swop.facade.ResourceTypeData;
import be.kuleuven.cs.swop.facade.TaskMan;
import be.kuleuven.cs.swop.facade.DeveloperWrapper;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.ResourceTypeWrapper;
import be.kuleuven.cs.swop.facade.ResourceWrapper;
import be.kuleuven.cs.swop.facade.TaskData;
import be.kuleuven.cs.swop.facade.TaskStatusData;
import be.kuleuven.cs.swop.facade.TaskWrapper;


public class YAMLReader {

	TaskMan facade;

	public YAMLReader(TaskMan facade) {
		this.facade = facade;
	}

	@SuppressWarnings("unchecked")
	public void read(String initFile) {
		try {
			// Setup
			File f = new File(initFile);
			InputStream input = new FileInputStream(f);
			Yaml yaml = new Yaml();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			Map<String, List<Map<String, Object>>> parsedFile = (Map<String, List<Map<String, Object>>>) yaml.load(input);

			// Remember variables in order
			List<ProjectWrapper> projects = new ArrayList<ProjectWrapper>();
			List<TaskWrapper> tasks = new ArrayList<TaskWrapper>();
			List<ResourceTypeWrapper> resourceTypes = new ArrayList<ResourceTypeWrapper>();
			List<ResourceWrapper> resources = new ArrayList<ResourceWrapper>();
			List<DeveloperWrapper> developers = new ArrayList<DeveloperWrapper>();
			Map<TaskWrapper, Integer> planningTasks = new HashMap<TaskWrapper, Integer>();
			Map<Integer,Set<DeveloperWrapper>> planningDevelopers = new HashMap<Integer,Set<DeveloperWrapper>>();
			Map<Integer,LocalDateTime> planningStartDates = new HashMap<Integer,LocalDateTime>();
			Map<TaskWrapper, Set<ResourceWrapper>> taskResources = new HashMap<TaskWrapper, Set<ResourceWrapper>>();

			// Set system time
			Object dateObject = parsedFile.get("systemTime");
			String dateString = dateObject.toString();
			facade.updateSystemTime(LocalDateTime.parse(dateString, format));

			// Read resourceTypes
			for (Map<String, Object> resourceType : parsedFile.get("resourceTypes")) {

				// Requirements
				Set<ResourceTypeWrapper> requirements = new HashSet<ResourceTypeWrapper>();
				List<Integer> reqs = (List<Integer>) resourceType.get("requires");
				if (reqs != null) {
					for (int index : reqs) {
						ResourceTypeWrapper req = resourceTypes.get(index);
						requirements.add(req);                        
					}
				}

				// Conflicts
				Set<ResourceTypeWrapper> conflicts = new HashSet<ResourceTypeWrapper>();
				List<Integer> confs = (List<Integer>) resourceType.get("conflictsWith");
				boolean selfConflicting = false; // Resourcetypes can conflict with themselves.
				if (confs != null) {
					for (int index : confs) {
						if(index == resourceTypes.size()){
							selfConflicting = true;
							continue;
						}
						ResourceTypeWrapper conf = resourceTypes.get(index);
						conflicts.add(conf);                                                
					}
				}
				
				//TODO: deal with dailyAvailability

				String name = (String) resourceType.get("name");
				ResourceTypeWrapper newType = facade.createResourceType(new ResourceTypeData(name, requirements, conflicts, selfConflicting));
				
				resourceTypes.add(newType);
			}

			// Add resources
			for (Map<String, Object> resource : parsedFile.get("resources")) {
				String name = (String) resource.get("name");
				int typeId = (int) resource.get("type");
				ResourceTypeWrapper type = resourceTypes.get(typeId);
				ResourceWrapper newRes = facade.createResource(new ResourceData(name, type));
				resources.add(newRes);
			}

			// Add developers
			for (Map<String, Object> developer : parsedFile.get("developers")) {
				String name = (String) developer.get("name");
				DeveloperWrapper dev = facade.createDeveloper(new DeveloperData(name));
				developers.add(dev);
			}



			// Read projects
			for (Map<String, Object> project : parsedFile.get("projects")) {
				ProjectData pData = new ProjectData(
						(String) project.get("name"),
						(String) project.get("description"),
						LocalDateTime.parse((String) project.get("creationTime"), format),
						LocalDateTime.parse((String) project.get("dueTime"), format)
						);
				ProjectWrapper p = facade.createProject(pData);
				projects.add(p);
			}

			// Read tasks
			for (Map<String, Object> task : parsedFile.get("tasks")) {
				// Get project of this task
				int projectIndex = (int) task.get("project");
				ProjectWrapper project = projects.get(projectIndex);
				// Create task
				TaskData tData = new TaskData(
						(String) task.get("description"),
						(long) (int) task.get("estimatedDuration"),
						(double) (int) task.get("acceptableDeviation")/100
						);
				TaskWrapper t = facade.createTaskFor(project, tData);

				// Add dependencies
				List<Integer> prerequisites = (List<Integer>) task.get("prerequisiteTasks");
				if (prerequisites != null) {
					for (int index : prerequisites) {
						TaskWrapper prereq = tasks.get(index);
						facade.addDependencyTo(t, prereq);
					}
				}

				// Add as alternative to other(s)
				Object alter = task.get("alternativeFor");
				if (alter != null) {
					TaskWrapper other = tasks.get((int) alter);
					facade.setAlternativeFor(other, t);
				}

				// Finish/fail task if set
				String status = (String) task.get("status");
				if (status != null) {
					boolean successful = false;
					boolean isFinal = false;
					boolean executing = false;
					switch (status) {
					case "finished":
						successful = true;
						isFinal = true;
						break;
					case "failed":
						successful = false;
						isFinal = true;
						break;
					case "executing":
						executing = true;
						break;
					default:
						throw new IllegalStateException("Unknown status.");
					}
					TaskStatusData statusData = null;
					if(isFinal){
						// Need to start executing the task before we can finish it
						facade.updateTaskStatusFor(t, new ExecutingStatusData());   
						LocalDateTime startTime = LocalDateTime.parse((String) task.get("startTime"), format);
						LocalDateTime endTime = LocalDateTime.parse((String) task.get("endTime"), format);
						
						if(successful){
							statusData = new FinishedStatusData(startTime, endTime);
						}else{
							statusData = new FailedStatusData(startTime, endTime);
						}
					}else{
						if(executing){
							statusData = new ExecutingStatusData();
						}
					}
					facade.updateTaskStatusFor(t, statusData);   

				}

				// Keep track for planning
				Object planning = task.get("planning");
				if(planning != null){
					planningTasks.put(t, (Integer) planning);
				}

				taskResources.put(t, new HashSet<ResourceWrapper>());


				// Add to tracking list
				tasks.add(t);

			}

			// Add developers to plannings
			int planningIndex = 0;
			for (Map<String, Object> planning : parsedFile.get("plannings")) {
				LocalDateTime startTime = LocalDateTime.parse((String) planning.get("plannedStartTime"), format);
				planningStartDates.put(planningIndex, startTime);
				// Add developers
				List<Integer> devs = (List<Integer>) planning.get("developers");
				Set<DeveloperWrapper> currentDevs = new HashSet<DeveloperWrapper>();
				if (devs != null) {
					for (int index : devs) {
						DeveloperWrapper dev = developers.get(index);
						currentDevs.add(dev);
					}
				}
				planningDevelopers.put(planningIndex, currentDevs);
				planningIndex++;
			}



			// Add reservations
			for (Map<String, Object> reservation : parsedFile.get("reservations")) {
				int resourceId = (Integer) reservation.get("resource");
				int taskId = (Integer) reservation.get("task");
				TaskWrapper task = tasks.get(taskId);
				ResourceWrapper resource = resources.get(resourceId);
				taskResources.get(task).add(resource);
			}

			// Save all the plannings
			for(TaskWrapper task : tasks){
				if(planningTasks.containsKey(task)){
					int planningId = planningTasks.get(task);
					Set<DeveloperWrapper> devs = planningDevelopers.get(planningId);
					LocalDateTime startTime = planningStartDates.get(planningId);
					Set<ResourceWrapper> resourceSet = taskResources.get(task);
					facade.createPlanning(task, startTime, resourceSet, devs);
				}
			}


			System.out.println("Imported.");

		} catch (FileNotFoundException e) {
			System.out.println("Couldn't import data from file: Not found.");
		} catch (DateTimeParseException e) {
			System.out.println("Couldn't import data from file: Invalid date format.");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Couldn't import data from file: Missing fields. This probably means you're importing an old file.");
		}
	}
}
