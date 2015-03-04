package be.kuleuven.cs.swop;

import be.kuleuven.cs.swop.domain.project.Project;
import be.kuleuven.cs.swop.domain.task.Task;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * A simple command line interface
 */
public class CLI implements UserInterface {

	private Scanner scanner;

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
			default:
				System.out.println("Command not recognised.");
				break;
		}
	}

	private static String ERROR_ILLEGAL_ARGUMENT_LIST = "Illegal argument list.";

    public void setSessionController(SessionController session)
    {
        throw new UnsupportedOperationException("");
    }

    public SessionController getSessionController() { throw new UnsupportedOperationException(""); }

    @Override
    public void showProjects(Set<Project> projectSet) {
        List<Project> projects = new ArrayList<Project>(projectSet);
        System.out.println("PROJECTS\n########");
        for (Project p : projects) {
            System.out.println("# " + p.getTitle() +
                             "\n#   " + p.getDescription() +
                             "\n#   " + p.getCreationTime().toString() +
                             "\n#   " + p.getDueTime().toString() +
                             "\n# ----------------------------------");
        }
    }

    @Override
    public void showProject(Project project) {
        System.out.println("PROJECT\n########");
        System.out.println("# " + project.getTitle() +
                         "\n#   " + project.getDescription() +
                         "\n#   " + project.getCreationTime().toString() +
                         "\n#   " + project.getDueTime().toString());
    }

    @Override
    public void showTasks(Set<Task> taskSet) {
        List<Task> tasks = new ArrayList<Task>(taskSet);
        System.out.println("TASKS\n########");
        for (Task t : tasks) {
            System.out.println("# Description: " + t.getDescription() +
                             "\n#   Dependencies: " + t.getDependencySet().size() +
                             "\n#   " + t.getProject().toString() +
                             "\n#   " + t.getEstimatedDuration() +
                             "\n# ----------------------------------");
        }
    }

    @Override
    public Project selectProject(Set<Project> projectSet) {
        List<Project> projects = new ArrayList<Project>(projectSet);
        System.out.println("SELECT TASK\n########");
        for (int i = 0; i < projects.size(); i++) {
            System.out.println("# " + (i+1) + ") " + projects.get(i).getTitle());
        }
        System.out.println("\n# ----------------------------------");
        for(;;) {
            System.out.print("Choose a project (number) " + "[1-" + projects.size() + "] : ");
            int input = this.scanner.nextInt();
            if (input > 0 && input < projects.size())
                return projects.get(input-1);
            else
                System.out.println("You entered a wrong project number");
        }
    }

    @Override
    public Task selectTask(Set<Task> taskSet) {
        List<Task> tasks = new ArrayList<Task>(taskSet);
        System.out.println("SELECT TASK\n########");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("# " + (i+1) + ") " + tasks.get(i).getDescription());
        }
        System.out.println("\n# ----------------------------------");
        for(;;) {
            System.out.print("Choose a task (number) " + "[1-" + tasks.size() + "] : ");
            int input = this.scanner.nextInt();
            if (input > 0 && input < tasks.size())
                return tasks.get(input-1);
            else
                System.out.println("You entered a wrong task number");
        }
    }

    @Override
    public Map<String, String> provideInfo(Map<String, String> requirements) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date selectTimeStamp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

	@Override
	public void showTask(Task task) {
		// TODO Auto-generated method stub
		
	}
}
