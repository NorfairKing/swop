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
import be.kuleuven.cs.swop.domain.project.RealProject;
import be.kuleuven.cs.swop.domain.task.RealTask;


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
        String command;
        while (true) {
            command = this.selectCommand();
            execute(command);
        }
    }

    private String selectCommand() {
        System.out.print(">");
        String line = this.scanner.nextLine();
        return line;
    }

    private void execute(String command) {
        switch (command) {
            case "quit":
            case "q":
                System.exit(0);
                break;
            case "list":
            case "l":
                System.out.println("List all projects.");
                getSessionController().startShowProjectsSession();
                break;
            case "project":
            case "p":
                System.out.println("Create a new project.");
                getSessionController().startCreateProjectSession();
                break;
            case "task":
            case "t":
                System.out.println("Create a new task.");
                getSessionController().startCreateTaskSession();
                break;
            case "update":
            case "u":
                System.out.println("Update a task.");
                getSessionController().startUpdateTaskStatusSession();
                break;
            case "clock":
            case "c":
                System.out.println("Update the system clock.");
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
    public void showProjects(Set<RealProject> projectSet) {
        List<RealProject> projects = new ArrayList<RealProject>(projectSet);
        System.out.println("PROJECTS\n########");
        for (RealProject p : projects) {
            System.out.println(""
            		+ "# " + p.getTitle() + "\n"
            		+ "#   " + p.getDescription() + "\n"
    				+ "#   " + p.getCreationTime().toString() + "\n"
					+ "#   " + p.getDueTime().toString() + "\n"
					+ "# ----------------------------------");
        }
    }

    @Override
    public void showProject(RealProject project) {
        System.out.println("PROJECT\n########");
        System.out.println(""
        		+ "# " + project.getTitle() + "\n"
        		+ "#   " + project.getDescription() + "\n"
				+ "#   " + project.getCreationTime().toString() + "\n"
				+ "#   " + project.getDueTime().toString());
    }

    @Override
    public void showTasks(Set<RealTask> taskSet) {
        List<RealTask> tasks = new ArrayList<RealTask>(taskSet);
        System.out.println("TASKS\n########");
        for (RealTask t : tasks) {
            System.out.println(""
            		+ "# Description: " + t.getDescription() + "\n"
            		+ "#   Dependencies: " + t.getDependencySet().size() + "\n"
            		+ "#   " + t.getEstimatedDuration() + "\n"
            		+ "# ----------------------------------");
        }
    }

    @Override
    public RealProject selectProject(Set<RealProject> projectSet) {
        List<RealProject> projects = new ArrayList<RealProject>(projectSet);
        System.out.println("SELECT TASK\n########");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println("# " + (i + 1) + ") " + projects.get(i).getTitle());
        }
        System.out.println("\n# ----------------------------------");
        for (;;) {
            System.out.print("Choose a project (number) " + "[1-" + projects.size() + "] : ");
            int input = Integer.parseInt(this.scanner.nextLine());
            if (input > 0 && input <= projects.size()) return projects.get(input - 1);
            else System.out.println("You entered a wrong project number");
        }
    }

    @Override
    public RealTask selectTask(Set<RealTask> taskSet) {
        List<RealTask> tasks = new ArrayList<RealTask>(taskSet);
        System.out.println("SELECT TASK\n########");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("# " + (i + 1) + ") " + tasks.get(i).getDescription());
        }
        System.out.println("\n# ----------------------------------");
        for (;;) {
            System.out.print("Choose a task (number) " + "[1-" + tasks.size() + "] : ");
            int input = Integer.parseInt(this.scanner.nextLine());
            if (input > 0 && input <= tasks.size()) return tasks.get(input - 1);
            else System.out.println("You entered a wrong task number");
        }
    }

    @Override
    public Map<String, String> provideInfo(Map<String, String> requirements) {
        Map<String, String> info = new HashMap<String, String>();
        for (String requirement : requirements.keySet()) {
            System.out.println(requirement + ": ");
            for (boolean isIncorrect = true; isIncorrect;) {
                String input = this.scanner.nextLine();
                if (input.matches(requirements.get(requirement))) {
                    isIncorrect = false;
                    info.put(requirement, input);
                } else System.out.println("Invalid input, try again");
            }
        }
        return info;

    }

    @Override
    public Date selectTimeStamp() {
        throw new UnsupportedOperationException("Not supported yet.");

    }

    @Override
    public void showTask(RealTask task) {
        System.out.println("TASK\n########");
        System.out.println(""
        		+ "# " + task.getDescription() + "\n"
        		+ "#   Dependencies: " + task.getDependencySet().size() + "\n"
        		+ "#   Estimated Duration: " + task.getEstimatedDuration());
    }

	@Override
	public ProjectData getProjectData() {
		System.out.println("CREATING PROJECT\n########");
		System.out.print("# Title: ");
		String title = this.scanner.nextLine();
		System.out.print("# Description: ");
		String description = this.scanner.nextLine();
		Date dueTime;
		while(true){
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
	public void showError(String error) {
		System.out.println("ERROR\n########");
		System.out.println(error);	
	}
}
