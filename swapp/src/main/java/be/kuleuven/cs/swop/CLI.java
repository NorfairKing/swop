package be.kuleuven.cs.swop;

import be.kuleuven.cs.swop.domain.project.Project;
import be.kuleuven.cs.swop.domain.task.Task;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
				break;
			case "project":
			case "p":
				System.out.println("Create a new project.");
				break;
			case "task":
			case "t":
				System.out.println("Create a new task.");
				break;
			case "update":
			case "u":
				System.out.println("Update a task.");
				break;
			case "clock":
			case "c":
				System.out.println("Update the system clock.");
				break;
			default:
				System.out.println("Command not recognised.");
				break;
		}
	}

	private static String ERROR_ILLEGAL_ARGUMENT_LIST = "Illegal argument list.";

    @Override
    public void showProjects(List<Project> projects) {
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
    public void showTasks(List<Task> tasks) {
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
    public Project selectProject(List<Project> projects) {
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
    public Task selectTask(List<Task> tasks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, String> provideInfo(Map<String, String> requirements) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date selectTimeStamp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
