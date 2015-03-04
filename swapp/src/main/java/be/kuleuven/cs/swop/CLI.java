package be.kuleuven.cs.swop;

import be.kuleuven.cs.swop.domain.Project;
import be.kuleuven.cs.swop.domain.Task;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * A simple command line interface
 */
public class CLI implements UserInterface {

	private String[] args;
	private Scanner scanner;

	public CLI(String[] args) {
		this.setArgs(args);
		this.scanner = new Scanner(System.in);
	}

	public void mainLoop() {
		String command;
		while (true) {
			command = this.selectCommand();
			execute(command);
		}
	}

	private String selectCommand() {
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
				System.err.println("Command not recognised.");
				break;
		}
	}

	protected String[] getArgs() {
		return args;
	}

	protected static boolean canHaveAsArgs(String[] args) {
		return args != null;
	}

	protected void setArgs(String[] args) {
		if (!canHaveAsArgs(args)) {
			throw new IllegalArgumentException(ERROR_ILLEGAL_ARGUMENT_LIST);
		}
		this.args = args;
	}

	private static String ERROR_ILLEGAL_ARGUMENT_LIST = "Illegal argument list.";

    @Override
    public void showProjects(List<Project> projects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void showProject(Project project) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void showTasks(List<Task> tasks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Project selectProject(List<Project> projects) {
        throw new UnsupportedOperationException("Not supported yet.");
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
