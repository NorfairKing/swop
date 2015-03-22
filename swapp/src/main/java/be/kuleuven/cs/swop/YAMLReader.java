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

import org.yaml.snakeyaml.Yaml;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.data.TaskStatusData;


public class YAMLReader {
    
    FacadeController facade;
    
    public YAMLReader(FacadeController facade) {
        this.facade = facade;
    }
    
    @SuppressWarnings("unchecked")
    public void read(String initFile) {
        try {
            System.out.println("Importing: " + initFile);
            
            // Setup
            InputStream input = new FileInputStream(new File(initFile));
            Yaml yaml = new Yaml();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Map<String, List<Map<String, Object>>> parsedFile = (Map<String, List<Map<String, Object>>>) yaml.load(input);
            
            // Remember variables in order
            List<ProjectWrapper> projects = new ArrayList<>();
            List<TaskWrapper> tasks = new ArrayList<>();
            
            // Read projects
            for (Map<String, Object> project : parsedFile.get("projects")) {
                ProjectData pData = new ProjectData(
                        (String) project.get("name"),
                        (String) project.get("description"),
                        format.parse((String) project.get("creationTime")),
                        format.parse((String) project.get("dueTime"))
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
                    Date startTime = format.parse((String) task.get("startTime"));
                    Date endTime = format.parse((String) task.get("endTime"));
                    boolean successful;
                    
                    switch (status) {
                        case "finished":
                            successful = true;
                            break;
                        case "failed":
                            successful = false;
                            break;
                        default:
                            throw new IllegalStateException("Unknown status.");
                    }
                    
                    TaskStatusData statusData = new TaskStatusData(startTime, endTime, successful);
                    facade.updateTaskStatusFor(t, statusData);
                }

                // Add to tracking list
                tasks.add(t);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't import data from file: Not found.");
        } catch (ParseException e) {
            System.out.println("Couldn't import data from file: Invalid date format");
            e.printStackTrace();
        }
    }
}
