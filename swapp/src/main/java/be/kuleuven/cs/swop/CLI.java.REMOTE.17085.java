package be.kuleuven.cs.swop;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;


/**
 * A simple command line interface
 */
public class CLI implements UserInterface {

    private Scanner scanner;
    private SessionController sessionController;

    public CLI() {
        this.scanner = new Scanner(System.in);
    }

    public int start() {
        System.out.println("Welcome to TaskMan");
        System.out.println("Enter \"h\" for help.");
        String command;
        while (true) {
            command = this.selectCommand();
            execute(command);
        }
    }

    private String selectCommand() {
        System.out.print("> ");
        String line = this.scanner.nextLine();
        return line;
    }

    private void execute(String command) {
        switch (command) {
            case "quit":
            case "q":
                System.exit(0);
                break;
            case "help":
            case "h":
                System.out.println("list    / l:   list all projects");
                System.out.println("project / p:   create project");
                System.out.println("task    / t:   create task");
                System.out.println("update  / u:   update task");
                System.out.println("clock   / c:   update clock");
                System.out.println("quit    / q:   quit taskman");
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
    }

    private static String ERROR_ILLEGAL_SESSION_CONTROLLER = "Illegal session controller for CLI.";

    public SessionController getSessionController() {
        return this.sessionController;
    }

    protected boolean canHaveAsSessionController(SessionController session) {
        return session != null;
    }

    public void setSessionController(SessionController session) {
        if (!canHaveAsSessionController(session)) throw new IllegalArgumentException(ERROR_ILLEGAL_SESSION_CONTROLLER);
        this.sessionController = session;
    }

    @Override
    public void showProjects(Set<ProjectWrapper> projectSet) {
        List<ProjectWrapper> projects = new ArrayList<ProjectWrapper>(projectSet);
        System.out.println("PROJECTS\n########");
        for (ProjectWrapper p : projects) {
            System.out.println("" + "# " + p.getTitle() + "\n" + "#   " + p.getDescription() + "\n" + "#   " + p.getCreationTime().toString() + "\n" + "#   " + p.getDueTime().toString() + "\n"
                    + "# ----------------------------------");
        }
    }

    @Override
    public void showProject(ProjectWrapper project) {
        System.out.println("PROJECT\n########");
        System.out.println("" + "# " + project.getTitle() + "\n" + "#   " + project.getDescription() + "\n" + "#   " + project.getCreationTime().toString() + "\n" + "#   "
                + project.getDueTime().toString());
    }

    @Override
    public void showTasks(Set<TaskWrapper> taskSet) {
        List<TaskWrapper> tasks = new ArrayList<TaskWrapper>(taskSet);
        System.out.println("TASKS\n########");
        for (TaskWrapper t : tasks) {
            System.out.println("" + "# Description: " + t.getDescription() + "\n" + "#   Dependencies: " + t.getDependencySet().size() + "\n" + "#   " + t.getEstimatedDuration() + "\n"
                    + "# ----------------------------------");
        }
    }

    @Override
    public ProjectWrapper selectProject(Set<ProjectWrapper> projectSet) {
        if (projectSet.isEmpty()) {
            System.out.println("No projects to select.");
            return null;
        }

        List<ProjectWrapper> projects = new ArrayList<ProjectWrapper>(projectSet);
        System.out.println("SELECT TASK\n########");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println("# " + (i + 1) + ") " + projects.get(i).getTitle());
        }
        System.out.println("\n# ----------------------------------");

        boolean validInput;
        int inputIndex = 0;
        do {
            System.out.print("Choose a project (number) " + "[1-" + projects.size() + "] (0 to quit): ");
            try {
                inputIndex = Integer.parseInt(this.scanner.nextLine());
                validInput = (inputIndex >= 0 && inputIndex <= projects.size());
            } catch (NumberFormatException e) {
                validInput = false;
            }
            if (!validInput) {
                System.out.println("Invalid input, try again!");
            }
        } while (!validInput);
        if (inputIndex == 0) {
            return null;
        } else {
            return projects.get(inputIndex - 1);
        }
    }

    @Override
    public TaskWrapper selectTask(Set<TaskWrapper> taskSet) {
        if (taskSet.isEmpty()) {
            System.out.println("No tasks to select.");
            return null;
        }

        List<TaskWrapper> tasks = new ArrayList<TaskWrapper>(taskSet);
        System.out.println("SELECT TASK\n########");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("# " + (i + 1) + ") " + tasks.get(i).getDescription());
        }
        System.out.println("\n# ----------------------------------");
        boolean validInput;
        int inputIndex = 0;
        do {
            System.out.print("Choose a task (number) " + "[1-" + tasks.size() + "] (0 to quit): ");
            try {
                inputIndex = Integer.parseInt(this.scanner.nextLine());
                validInput = (inputIndex >= 0 && inputIndex <= tasks.size());
            } catch (NumberFormatException e) {
                validInput = false;
            }
            if (!validInput) {
                System.out.println("Invalid input, try again!");
            }
        } while (!validInput);
        if (inputIndex == 0) {
            return null;
        } else {
            return tasks.get(inputIndex - 1);
        }
    }

    @Override
    public void showTask(TaskWrapper task) {
        System.out.println("TASK\n########");
        System.out.println("" + "# " + task.getDescription() + "\n" + "#   Dependencies: " + task.getDependencySet().size() + "\n" + "#   Estimated Duration: " + task.getEstimatedDuration());
    }

    @Override
    public ProjectData getProjectData() {
        System.out.println("CREATING PROJECT\n########");
        System.out.print("# Title: ");
        String title = this.scanner.nextLine();
        System.out.print("# Description: ");
        String description = this.scanner.nextLine();
        Date dueTime;
        while (true) {
            try {
                System.out.print("# Due Date: ");
                String dueTimeText = this.scanner.nextLine();
                dueTime = format.parse(dueTimeText);
                break;
            } catch (ParseException e) {
                System.out.println("# ERROR: Invalid Date Format. Needs to be like 2015-11-25 23:30");
            }
        }
        return new ProjectData(title, description, dueTime);

    }

    public static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public TaskData getTaskDate() {
        TaskData data = new TaskData();

        System.out.println("CREATING TASK\n########");

        System.out.print("# Description: ");
        String description = this.scanner.nextLine();
        data.setDescription(description);

        System.out.print("# Estimated Duration (double): ");
        while (true) {
            try {
                data.setEstimatedDuration(Double.parseDouble(this.scanner.nextLine()));
                break;
            } catch (NumberFormatException e) {
                System.out.print("# ERROR: Invalid duration. Please provide a double");
            }
        }

        System.out.print("# Acceptable Deviation (%, double): ");
        while (true) {
            try {
                data.setAcceptableDeviation(Double.parseDouble(this.scanner.nextLine()));
                break;
            } catch (NumberFormatException e) {
                System.out.print("# ERROR: Invalid deviation. Please provide a double");
            }
        }

        return data;
    }

    @Override
    public void showError(String error) {
        System.out.println("ERROR\n########");
        System.out.println(error);
    }

    @Override
    public TaskWrapper selectTaskFromProjects(Set<ProjectWrapper> projects) {
        // TODO Auto-generated method stub
        return null;
    }
}
