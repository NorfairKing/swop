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
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

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
            List<ProjectWrapper> projects = new ArrayList<>();
            List<TaskWrapper> tasks = new ArrayList<>();
            List<ResourceTypeWrapper> resourceTypes = new ArrayList<>();
            List<ResourceWrapper> resources = new ArrayList<>();
            List<DeveloperWrapper> developers = new ArrayList<>();
            Map<Integer,TaskWrapper> taskPlannings = new HashMap<Integer,TaskWrapper>();

            // Set system time
            Object dateObject = parsedFile.get("systemTime");
            String dateString = dateObject.toString();
            facade.updateSystemTime(LocalDateTime.parse(dateString, format));

            // Read resourceTypes
            for (Map<String, Object> resourceType : parsedFile.get("resourceTypes")) {

                // Requirements
                List<ResourceTypeWrapper> requirements = new ArrayList<ResourceTypeWrapper>();
                List<Integer> reqs = (List<Integer>) resourceType.get("requires");
                if (reqs != null) {
                    for (int index : reqs) {
                        ResourceTypeWrapper req = resourceTypes.get(index);
                        requirements.add(req);                        
                    }
                }

                // Conflicts
                List<ResourceTypeWrapper> conflicts = new ArrayList<ResourceTypeWrapper>();
                List<Integer> confs = (List<Integer>) resourceType.get("requires");
                if (confs != null) {
                    for (int index : confs) {
                        ResourceTypeWrapper conf = resourceTypes.get(index);
                        conflicts.add(conf);                                                
                    }
                }

                String name = (String) resourceType.get("name");
                //TODO: add resource to facade, this is placeholder
                ResourceTypeWrapper newType = null;
                resourceTypes.add(newType);
            }

            // Add resources
            for (Map<String, Object> resource : parsedFile.get("resources")) {
                String name = (String) resource.get("name");
                int typeId = (int) resource.get("type");
                ResourceTypeWrapper type = resourceTypes.get(typeId);
                // TODO: add resource to facade
            }

            // Add developers
            for (Map<String, Object> developer : parsedFile.get("developers")) {
                String name = (String) developer.get("name");
                // TODO: add developer to facade
                DeveloperWrapper dev = null;
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
                        (double) (int) task.get("estimatedDuration"),
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
                    boolean needsPeriod = false;
                    switch (status) {
                        case "finished":
                            successful = true;
                            needsPeriod = true;
                            break;
                        case "failed":
                            successful = false;
                            needsPeriod = true;
                            break;
                        case "executing":
                            break;
                        default:
                            throw new IllegalStateException("Unknown status.");
                    }
                    if(needsPeriod){
                        LocalDateTime startTime = LocalDateTime.parse((String) task.get("startTime"), format);
                        LocalDateTime endTime = LocalDateTime.parse((String) task.get("endTime"), format);                        
                        TaskStatusData statusData = new TaskStatusData(startTime, endTime, successful);
                        facade.updateTaskStatusFor(t, statusData);   
                    }else{
                        // TODO: deal with executing
                    }
                }

                // Keep track for planning
                int planning = (int) task.get("planning");
                taskPlannings.put(planning, t);



                // Add to tracking list
                tasks.add(t);

            }

            // Add plannings
            int planningIndex = 0;
            for (Map<String, Object> planning : parsedFile.get("plannings")) {
                LocalDateTime startTime = LocalDateTime.parse((String) planning.get("plannedStartTime"), format);

                // Add developers
                List<Integer> devs = (List<Integer>) planning.get("developers");
                Set<DeveloperWrapper> currentDevs = new HashSet<DeveloperWrapper>();
                if (devs != null) {
                    for (int index : devs) {
                        DeveloperWrapper dev = developers.get(index);
                        currentDevs.add(dev);
                    }
                }
                List<Integer> planResources = (List<Integer>) planning.get("resources");
                Map<ResourceTypeWrapper, ResourceWrapper> currentResources = new HashMap<ResourceTypeWrapper, ResourceWrapper>();
                if (planResources != null) {
                    for (int index : planResources) {
                        ResourceWrapper rec = resources.get(index);
                        currentResources.add(rec);
                    }
                }

                facade.createPlanning(taskPlannings.get(planningIndex), startTime, currentResources, currentDevs);
                planningIndex++;
            }


        } catch (FileNotFoundException e) {
            System.out.println("Couldn't import data from file: Not found.");
        } catch (DateTimeParseException e) {
            System.out.println("Couldn't import data from file: Invalid date format");
            e.printStackTrace();
        }
    }
}
