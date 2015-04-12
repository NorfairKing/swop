package be.kuleuven.cs.swop;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.facade.DeveloperWrapper;
import be.kuleuven.cs.swop.facade.FailedStatusData;
import be.kuleuven.cs.swop.facade.FinishedStatusData;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.ResourceTypeWrapper;
import be.kuleuven.cs.swop.facade.ResourceWrapper;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.SimulationStepData;
import be.kuleuven.cs.swop.facade.TaskData;
import be.kuleuven.cs.swop.facade.TaskStatusData;
import be.kuleuven.cs.swop.facade.TaskWrapper;
import be.kuleuven.cs.swop.facade.UserWrapper;


/**
 * A simple command line interface
 */
public class CLI implements UserInterface {

    private Scanner           scanner;
    private SessionController sessionController;

    public CLI() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public boolean start() {
        System.out.println("Welcome to TaskMan");
        System.out.println("Enter \"h\" for help.");
        System.out.println("Don't forget to set the initial date (\"c\").");
        String command;
        boolean stop = false;
        while (!stop) {
            command = this.selectCommand();
            stop = execute(command);
        }
        return true;
    }

    private String selectCommand() {
        System.out.print("> ");
        String line = this.getScanner().nextLine();
        return line;
    }

    private boolean execute(String command) {
        switch (command) {
            case "quit":
            case "q":
                return true;
            case "help":
            case "h":
                System.out.println("list    / l:   list all projects");
                System.out.println("user       :   select the current user");
                System.out.println("project / p:   create project");
                System.out.println("task    / t:   create task");
                System.out.println("update  / u:   update task");
                System.out.println("plan       :   plan task");
                System.out.println("simulation :   simulation");
                System.out.println("clock   / c:   update clock");
                System.out.println("quit    / q:   quit taskman");
                break;
            case "user":
                getSessionController().startSelectUserSession();
                break;
            case "list":
            case "l":
                getSessionController().startShowProjectsSession();
                break;
            case "project":
            case "p":
                getSessionController().startCreateProjectSession();
                break;
            case "task":
            case "t":
                getSessionController().startCreateTaskSession();
                break;
            case "update":
            case "u":
                getSessionController().startUpdateTaskStatusSession();
                break;
            case "plan":
                getSessionController().startPlanTaskSession();
                break;
            case "simulation":
                getSessionController().startRunSimulationSession();
                break;
            case "clock":
            case "c":
                getSessionController().startAdvanceTimeSession();
                break;
            case "":
                break;
            default:
                System.out.println("Command not recognised.");
                break;
        }
        return false;
    }

    // Data methods
    
    @Override
    public SessionController getSessionController() {
        return this.sessionController;
    }

    protected boolean canHaveAsSessionController(SessionController session) {
        return session != null;
    }

    @Override
    public void setSessionController(SessionController session) {
        if (!canHaveAsSessionController(session)) throw new IllegalArgumentException(ERROR_ILLEGAL_SESSION_CONTROLLER);
        this.sessionController = session;
    }

    public Scanner getScanner() {
        return scanner;
    }
    
    // Interface methods
    
    @Override
    public UserWrapper selectUser(Set<UserWrapper> usersSet) {
        return selectFromCollection(usersSet, "users", u -> u.getName());
    }

    @Override
    public void showProjects(Set<ProjectWrapper> projectSet) {
        List<ProjectWrapper> projects = new ArrayList<ProjectWrapper>(projectSet);
        projects.sort((p1, p2) -> p1.getTitle().compareTo(p2.getTitle()));

        System.out.println("PROJECTS\n########");
        for (ProjectWrapper p : projects) {
            printProject(p);
            printDelimiter();
        }
    }

    @Override
    public void showProject(ProjectWrapper project) {
        System.out.println("PROJECT\n########\n");
        printProject(project);
        printDelimiter();
    }
    
    private void printProject(ProjectWrapper project) {
        LocalDateTime currentTime = sessionController.getTaskMan().getSystemTime();
        
        System.out.println("" 
                + "# " + project.getTitle() + "\n" 
                + "# Desc:    " + project.getDescription() + "\n" 
                + "# Created: " + formatDate(project.getCreationTime()) + "\n" 
                + "# Due:     " + formatDate(project.getDueTime()) + "\n"
                + "# ETA:     " + formatDate(project.estimatedFinishTime(currentTime))
        );
        
        if (project.isFinished()) {
            System.out.println("#   Is finished");
        }
    }
    
    @Override
    public ProjectWrapper selectProject(Set<ProjectWrapper> projectSet) {
        return selectFromCollection(projectSet, "projects", p -> p.getTitle());
    }
    
    @Override
    public ProjectData getProjectData() {
        System.out.println("CREATING PROJECT\n########");
        System.out.print("# Title: ");
        String title = promptString();
        System.out.print("# Description: ");
        String description = promptString();
        System.out.print("# Due Date: ");
        LocalDateTime dueTime = promptDate();
        return new ProjectData(title, description, dueTime);
    }

    @Override
    public void showTasks(Set<TaskWrapper> taskSet) {
        List<TaskWrapper> tasks = new ArrayList<TaskWrapper>(taskSet);
        tasks.sort((t1, t2) -> t1.getDescription().compareTo(t2.getDescription()));

        System.out.println("TASKS\n########");
        for (TaskWrapper t : tasks) {
            printTask(t);
            printDelimiter();
        }
    }

    @Override
    public void showTask(TaskWrapper task) {
        System.out.println("TASK\n########\n");
        printTask(task);
        printDelimiter();
    }
    
    private void printTask(TaskWrapper task) {
        LocalDateTime currentTime = sessionController.getTaskMan().getSystemTime();
        
        System.out.println(""
                + "# " + task.getDescription() + "\n" 
                + "#   Dependencies: " + task.getDependencySet().size() + "\n" 
                + "#   Estimated Duration: " + formatDuration(task.getEstimatedDuration()) + "\n" 
                + "#   Estimated finsih date: " + formatDate(task.getEstimatedOrRealFinishDate(currentTime)) + "\n"
                + "#   Acceptable Deviation: " + formatPercentage(task.getAcceptableDeviation()));

        if (task.isFinished()) {
            String timeString;
            if (task.wasFinishedEarly()) timeString = "early";
            else if (task.wasFinishedOnTime()) timeString = "on time";
            else timeString = "late";

            System.out.println(""
                    + "#   Is Finished\n" 
                    + "#   Performed During: " + formatPeriod(task.getPerformedDuring()) + "\n" 
                    + "#   Was finished " + timeString + "\n");
        } else if (task.isFailed()) {
            System.out.println(""
                    + "#   Has Failed\n" 
                    + "#   Performed During: " + formatPeriod(task.getPerformedDuring()) + "\n");
        } else {
            System.out.println("#   Still needs work");
        }
    }

    @Override
    public TaskWrapper selectTask(Set<TaskWrapper> taskSet) {
        return selectFromCollection(taskSet, "tasks", p -> p.getDescription());
    }

    @Override
    public TaskData getTaskData() {
        System.out.println("CREATING TASK\n########");

        System.out.print("# Description: ");
        String description = promptString();

        System.out.print("# Estimated Duration (minutes): ");
        long estimatedDuration = promptLong();
        
        System.out.print("# Acceptable Deviation (%): ");
        double acceptableDeviation = promptPercentageAsDouble();

        return new TaskData(description, estimatedDuration, acceptableDeviation);

    }

    @Override
    public TaskWrapper selectTaskFromProjects(Set<ProjectWrapper> projectSet) {
        Map<ProjectWrapper, Set<TaskWrapper>> projectMap = new HashMap<>();
        for(ProjectWrapper p: projectSet) {
            projectMap.put(p, p.getTasks());
        }
        return selectTaskFromProjects(projectMap);
    }
    
    /**
     * Select task from project, but shows only a selection of the tasks of each project.
     * @param projectMap The selection of tasks for each project.
     * @return The selected task.
     */
    @Override
    public TaskWrapper selectTaskFromProjects(Map<ProjectWrapper, Set<TaskWrapper>> projectMap) {
        List<Entry<ProjectWrapper, Set<TaskWrapper>>> tuples = new ArrayList<>(projectMap.entrySet());
        // sort tuples on project title
        tuples.sort((p1, p2) -> p1.getKey().getTitle().compareTo(p2.getKey().getTitle()));

        // gather all tasks in one list
        List<TaskWrapper> allTasks = new ArrayList<TaskWrapper>();
        for (Entry<ProjectWrapper, Set<TaskWrapper>> e : tuples) {
            List<TaskWrapper> projectTasks = new ArrayList<TaskWrapper>(e.getValue());
            projectTasks.sort((t1, t2) -> t1.getDescription().compareTo(t2.getDescription()));
            allTasks.addAll(projectTasks);
        }
        
        // print selection info
        System.out.println("SELECT TASK\n########");
        int taskId = 0;
        for (Entry<ProjectWrapper, Set<TaskWrapper>> e : tuples) {
            System.out.println("# " + e.getKey().getTitle());
            int taskCountForThisProject = e.getValue().size();
            for (int t = 0; t < taskCountForThisProject; ++t) {
                System.out.println("    # " + (taskId + 1) + ") " + allTasks.get(taskId).getDescription());
                taskId++;
            }
        }

        System.out.println("\n# ----------------------------------");
        int index = promptNumber(0, allTasks.size());
        if (index == 0) {
            return null;
        } else {
            return allTasks.get(index - 1);
        }
    }
    
    @Override
    public TaskStatusData getUpdateStatusData() {
        System.out.println("UPDATE TASK STATUS\n########");

        System.out.print("# Start Date: ");
        LocalDateTime startTime = promptDate();

        System.out.print("# End Date: ");
        LocalDateTime endTime = promptDate();

        System.out.print("# Was is successful (finish/fail): ");
        boolean successful = promptBoolean("finish", "fail");
        if(successful){
        	return new FinishedStatusData(startTime, endTime);
        }else{
        	return new FailedStatusData(startTime, endTime);
        }
    }
    
    @Override
    public LocalDateTime getTimeStamp() {
        System.out.println("TIME STAMP\n########");

        System.out.print("# Time: ");
        LocalDateTime time = promptDate();

        return time;
    }

    @Override
    public LocalDateTime selectTime(List<LocalDateTime> options) {
        return selectFromCollection(options, "time", o -> formatDate(o));
    }

    @Override
    public Set<ResourceWrapper> selectResourcesFor(Map<ResourceTypeWrapper, List<ResourceWrapper>> options) {
        List<Entry<ResourceTypeWrapper, List<ResourceWrapper>>> tuples = new ArrayList<>(options.entrySet());
        // sort tuples on project title
        tuples.sort((p1, p2) -> p1.getKey().getName().compareTo(p2.getKey().getName()));

        Set<ResourceWrapper> selected = new HashSet<>();
        for (Entry<ResourceTypeWrapper, List<ResourceWrapper>> e : tuples) {
            selected.add(selectResourceFor(e.getKey(), e.getValue()));
        }
        
        return selected;
    }
    
    private ResourceWrapper selectResourceFor(ResourceTypeWrapper type, List<ResourceWrapper> resources) {
        if (resources.isEmpty()) {
            System.out.println("No resource to select.");
            return null;
        }
        
        resources.sort((t1, t2) -> t1.getName().compareTo(t2.getName()));
        System.out.println("SELECT RESOURCE FOR " + type.getName() + "\n########");
        for (int i = 0; i < resources.size(); i++) {
            System.out.println("# " + (i + 1) + ") " + resources.get(i).getName());
        }
        printDelimiter();

        int index = promptNumber(0, resources.size());
        if (index == 0) {
            return null;
        } else {
            return resources.get(index - 1);
        }
    }

    @Override
    public Set<DeveloperWrapper> selectDevelopers(Set<DeveloperWrapper> developerOptions) {
        if (developerOptions == null || developerOptions.size() == 0) {
            System.out.println("No developers to select.");
        }
        
        List<DeveloperWrapper> developers = new ArrayList<>(developerOptions);
        System.out.println("SELECT DEVELOPERS\n########");
        for (int i = 0; i < developers.size(); i++) {
            System.out.println("# " + (i + 1) + ") " + developers.get(i).getName());
        }
        printDelimiter();
        
        Set<DeveloperWrapper> selectedDevelopers = new HashSet<>();
        do {
            int index = promptNumber(0, developers.size());
            if (index == 0) {
                break;
            } else {
                DeveloperWrapper selected = developers.get(index - 1);
                if (selectedDevelopers.contains(selected)) {
                    System.out.println("That developer was already selected...");
                } else {
                    System.out.println("Developer added, select another one? (0 to stop): ");
                    selectedDevelopers.add(selected);
                }
            }
        } while (true);
        
        return null;
    }
    
    @Override
    public void showError(String error) {
        System.out.println("ERROR\n########");
        System.out.println(error);
        printDelimiter();
    }
    
    public SimulationStepData getSimulationStepData() {
        System.out.println("Continue the simulation? (\"continue\", \"realize\" or \"cancel\")");
        
        do {
            String reply = this.getScanner().nextLine();
            if (reply.equalsIgnoreCase("continue")) {
                return new SimulationStepData(true, false);
            } else if (reply.equalsIgnoreCase("realize")) {
                return new SimulationStepData(false, true);
            } else if (reply.equalsIgnoreCase("cancel")) {
                return new SimulationStepData(false, false);
            } else {
                System.out.print("# Please type \"continue\", \"realize\" or \"cancel\": ");
            }
        } while (true);
    }
    
    // Format methods
    
    private String formatDate(LocalDateTime date){
        return date.format(printFormat);
    }

    private String formatPeriod(TimePeriod period){
        return period.getStartTime().format(periodDateFormat) + " --> " + period.getStopTime().format(periodDateFormat);
    }

    private String formatPercentage(double input){
        return (int) (input * 100) + "%";
    }

    private String formatDuration(double input){
        int hours = ((int) input)/60;
        int minutes = (int) (input % 60);
        String hoursString;
        String minutesString;
        String sep = ", ";
        switch(hours){
            case 0:
                hoursString = "";
                sep = "";
                break;
            case 1:
                hoursString = "1 hour";
                break;
            default:
                hoursString = hours + " hours";
                break;
        }
        switch(minutes){
            case 0:
                minutesString = "";
                sep = "";
                break;
            case 1:
                minutesString = "1 minute";
                break;
            default:
                minutesString = minutes + " minutes";
                break;
        }
        return hoursString + sep + minutesString;
    }

    // Prompt Methods

    /**
     * Prompt for a number between lo and hi inclusive.
     * 
     * @param lo
     *            The lower bound
     * @param hi
     *            THe upper bound
     * @return an interger between lo and hi inclusive
     */
    private int promptNumber(int lo, int hi) {
        boolean validInput;
        int inputIndex = 0;
        do {
            System.out.print("Please pick a number " + "[" + lo + "-" + hi + "] (0 to quit): ");
            try {
                inputIndex = Integer.parseInt(this.getScanner().nextLine());
                validInput = (inputIndex >= lo && inputIndex <= hi);
            } catch (NumberFormatException e) {
                validInput = false;
            }
            if (!validInput) {
                System.out.println("Invalid input, try again!");
            }
        } while (!validInput);
        return inputIndex;
    }
    
    private String promptString() {
        return this.getScanner().nextLine();
    }
    
    private LocalDateTime promptDate() {
        while (true) {
            try {
                String inputText = this.getScanner().nextLine();
                if("now".equals(inputText)){
                    return LocalDateTime.now();
                }else{
                    return LocalDateTime.parse(inputText, parseFormat);
                }
            } catch (DateTimeParseException e) {
                System.out.println("# ERROR: Invalid Date Format. Needs to be like 2015-11-25 23:30 or use \"now\"");
            }
        }
    }
    
    private boolean promptBoolean(String trueString, String falseString) {
        boolean successful;
        
        do {
            String success = this.getScanner().nextLine();
            if (success.equalsIgnoreCase(trueString)) {
                successful = true;
                break;
            } else if (success.equalsIgnoreCase(falseString)) {
                successful = false;
                break;
            } else {
                System.out.print("# Please type \"" + trueString + "\" or \"" + falseString + "\": ");
            }
        } while (true);
        
        return successful;
    }
    
    /**
     * Asks the user to give a number in percentage.
     * @return A double representing this percentage (1 = 100%, 0 = 0%, 0.5 = 50%, ...)
     */
    private double promptPercentageAsDouble() {
        double percentage = promptDouble();
        return percentage / 100.0;
    }
    
    private double promptDouble() {
        do {
            try {
                return Double.parseDouble(getScanner().nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, try again: ");
            }
        } while (true);
    }
    
    private long promptLong() {
        do {
            try {
                return Long.parseLong(getScanner().nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, try again: ");
            }
        } while (true);
    }
    
    private <T> T selectFromCollection(Collection<T> collection, String heading, Function<T, String> toString) {
        if (collection.isEmpty()) {
            System.out.println("No " + heading.toLowerCase() + " to select.");
            return null;
        }

        List<T> list = new ArrayList<>(collection);
        list.sort((u1, u2) -> toString.apply(u1).compareTo(toString.apply(u2)));
        System.out.println("SELECT " + heading.toUpperCase() + "\n########");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("# " + (i + 1) + ") " + toString.apply(list.get(i)));
        }
        printDelimiter();

        int index = promptNumber(0, list.size());
        if (index == 0) {
            return null;
        } else {
            return list.get(index - 1);
        }
    }
    
    // Other helper functions
    
    private void printDelimiter() {
        System.out.println("# ----------------------------------");
    }
    
    // Constants
    public static final DateTimeFormatter parseFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter printFormat = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy - HH:mm");
    public static final DateTimeFormatter periodDateFormat = DateTimeFormatter.ofPattern("E dd/MM/yyyy HH:mm");
    
    private static String ERROR_ILLEGAL_SESSION_CONTROLLER = "Illegal session controller for CLI.";
}
