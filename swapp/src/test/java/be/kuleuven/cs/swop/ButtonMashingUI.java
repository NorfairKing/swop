package be.kuleuven.cs.swop;


import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import be.kuleuven.cs.swop.data.ProjectData;
import be.kuleuven.cs.swop.data.TaskData;
import be.kuleuven.cs.swop.data.TaskStatusData;


public class ButtonMashingUI implements UserInterface {

    private static String VALID_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-0123456789&é\"\'(§è!çà)$^*¨µ][´~·/:;.,?><\\²³";
    private SessionController sessionController;
    private Random random;

    private static String ERROR_ILLEGAL_SESSION_CONTROLLER = "Illegal session controller for CLI.";

    public ButtonMashingUI() {
        random = new Random(8008135);
    }

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
    public void showProjects(Set<ProjectWrapper> projects) {}

    @Override
    public void showProject(ProjectWrapper project) {}

    @Override
    public void showTasks(Set<TaskWrapper> tasks) {}

    @Override
    public void showTask(TaskWrapper task) {}

    @Override
    public ProjectWrapper selectProject(Set<ProjectWrapper> projects) {
        return selectFromCollection(projects);
    }

    @Override
    public TaskWrapper selectTask(Set<TaskWrapper> tasks) {
        return selectFromCollection(tasks);
    }

    @Override
    public TaskWrapper selectTaskFromProjects(Set<ProjectWrapper> projects) {
        Set<TaskWrapper> tasks = new HashSet<TaskWrapper>();
        for (ProjectWrapper p : projects) {
            tasks.addAll(p.getTasks());
        }
        return selectFromCollection(tasks);
    }

    @Override
    public TaskData getTaskData() {
        if (random.nextBoolean()) return null;
        String description = randomString();
        boolean negative1 = random.nextBoolean();
        double duration = negative1 ? random.nextDouble() * 50.0 : random.nextDouble() * -50.0; // arbitrary
                                                                                                // constant
        boolean negative2 = random.nextBoolean();
        double deviation = negative2 ? random.nextDouble() * 2.0 : random.nextDouble() * -2.0; // arbitrary
                                                                                               // constant
        return new TaskData(description, duration, deviation);
    }

    @Override
    public TaskStatusData getUpdateStatusData() {
        if (random.nextBoolean()) return null;
        Date date1 = getTimeStamp();
        Date date2 = getTimeStamp();
        boolean success = random.nextBoolean();
        return new TaskStatusData(date1, date2, success);
    }

    @Override
    public ProjectData getProjectData() {
        if (random.nextBoolean()) return null;
        String title = randomString();
        String description = randomString();
        Date due = getTimeStamp();
        return new ProjectData(title, description, due);
    }

    @Override
    public Date getTimeStamp() {
        if (random.nextBoolean()) return null;
        return new Date(random.nextLong());
    }

    @Override
    public void showError(String error) {}

    private String randomString() {
        return randomString(120);
    }

    private String randomString(int lengthBound) {
        int characters = random.nextInt(lengthBound);
        char[] chars = new char[characters];
        for (int i = 0; i < characters; i++) {
            chars[i] = VALID_CHARACTERS.charAt(random.nextInt(VALID_CHARACTERS.length()));
        }
        return new String(chars);
    }

    private <O> O selectFromCollection(Iterable<O> objects) {
        Random r = new Random();
        double ch = 0;
        double c;
        O result = null;
        for (O o : objects) {
            c = r.nextDouble();
            if (c > ch) {
                ch = c;
                result = o;
            }
        }
        if (ch > 0.5) { return null; }// Arbitrary constant
        return result;
    }

    @Override
    public void start() {}

    public void performActions(int amount) {
        for (int i = 0; i < amount; i++) {
            performAction();
        }
    }

    public void performAction() {
        int useCases = 5;
        switch (random.nextInt(useCases)){
            case 0:
                getSessionController().startAdvanceTimeSession();
                break;
            case 1:
                getSessionController().startCreateProjectSession();
                break;
            case 2:
                getSessionController().startCreateTaskSession();
                break;
            case 3:
                getSessionController().startShowProjectsSession();
                break;
            case 4:
                getSessionController().startUpdateTaskStatusSession();
                break;
            default:
                throw new RuntimeException("Java should never get here");
        }
    }

}
