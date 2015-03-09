package be.kuleuven.cs.swop;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.data.TaskStatusData;


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
    public void showError(String error) {
        System.out.println("ERROR\n########");
        System.out.println(error);
    }

	@Override
	public ProjectData getProjectData() {
		System.out.println("CREATING PROJECT\n########");
		System.out.print("# Title: ");
		String title = this.scanner.nextLine();
		System.out.print("# Description: ");
		String description = this.scanner.nextLine();
		System.out.print("# Due Date: ");
		Date dueTime = getDate();
		return new ProjectData(title, description, dueTime);
	}

	
	@Override
	public TaskData getTaskData() {
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
			}
			catch (NumberFormatException e) {
				System.out.print("# ERROR: Invalid duration. Please provide a double");
			}
		}
		
		System.out.print("# Acceptable Deviation (%, double): ");
		while (true) {
			try {
				data.setAcceptableDeviation(Double.parseDouble(this.scanner.nextLine()));
				break;
			}
			catch (NumberFormatException e) {
				System.out.print("# ERROR: Invalid deviation. Please provide a double");
			}
		}
		
		return data;
	}

	@Override
	public TaskWrapper selectTaskFromProjects(Set<ProjectWrapper> projectSet) {
		List<ProjectWrapper> projects = new ArrayList<>(projectSet);
		
		List<TaskWrapper> allTasks = new ArrayList<TaskWrapper>();
		for (ProjectWrapper project: projects) {
			allTasks.addAll(project.getTasks());
		}
        System.out.println("SELECT TASK\n########");
        
        int taskId = 0;
        for (int p = 0; p < projects.size(); ++p) {
        	System.out.println("# " + projects.get(p).getTitle());
        	List<TaskWrapper> tasks = new ArrayList<>(projects.get(p).getTasks());
        	for (int t = 0; t < tasks.size(); ++t) {
        		System.out.println("    # " + (taskId + 1) + ") " + tasks.get(t).getDescription());
        		++taskId;
        	}
        }
        
        System.out.println("\n# ----------------------------------");
        while (true) {
            System.out.print("Choose a task (number) " + "[1-" + allTasks.size() + "] : ");
            int input = Integer.parseInt(this.scanner.nextLine());
            if (input > 0 && input <= allTasks.size()) return allTasks.get(input - 1);
            else System.out.println("You entered a wrong task number");
        }
    }

	@Override
	public TaskStatusData getUpdateStatusData() {
		System.out.println("UPDATE TASK STATUS\n########");

		System.out.print("# Start Date: ");
		Date startTime = getDate();
		
		System.out.print("# End Date: ");
		Date endTime = getDate();
		
		System.out.print("# Was is successful (or did it fail? :o) (finish/fail): ");
		boolean successful;
		do {
			String success = this.scanner.nextLine();
			if (success.equalsIgnoreCase("finish")) {
				successful = true;
				break;
			}
			else if (success.equalsIgnoreCase("fail")) {
				successful = false;
				break;
			}
			else {
				System.out.print("# Please type \"finish\" or \"fail\": ");
			}
		} while (true);
		
		return new TaskStatusData(startTime, endTime, successful);
	}
	
	@Override
	public Date getTimeStamp() {
		System.out.println("TIME STAMP\n########");
		
		System.out.print("# Time: ");
		Date time = getDate();
		
		return time;
	}
	
	private Date getDate() {
		while(true){
			try {
				String dueTimeText = this.scanner.nextLine();
				return format.parse(dueTimeText);
			} catch (ParseException e) {
				System.out.println("# ERROR: Invalid Date Format. Needs to be like 2015-11-25 23:30");
			}
		}
	}
	
	public static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
}
