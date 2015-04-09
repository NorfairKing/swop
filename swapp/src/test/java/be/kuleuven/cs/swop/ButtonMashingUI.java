package be.kuleuven.cs.swop;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import be.kuleuven.cs.swop.facade.DeveloperWrapper;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.ResourceTypeWrapper;
import be.kuleuven.cs.swop.facade.ResourceWrapper;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.TaskData;
import be.kuleuven.cs.swop.facade.TaskStatusData;
import be.kuleuven.cs.swop.facade.TaskWrapper;
import be.kuleuven.cs.swop.facade.UserWrapper;


public class ButtonMashingUI implements UserInterface {

    private static String     VALID_CHARACTERS                 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-0123456789&é\"\'(§è!çà)$^*¨µ][´~·/:;.,?><\\²³";
    private SessionController sessionController;
    private Random            random;

    private static String     ERROR_ILLEGAL_SESSION_CONTROLLER = "Illegal session controller for CLI.";

    public ButtonMashingUI() {
        random = new Random(8008135);
    }

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
    
    @Override
    public UserWrapper selectUser(Set<UserWrapper> users) {
        return selectFromCollection(users);
    }

    @Override
    public void showProjects(Set<ProjectWrapper> projects) {
        for (ProjectWrapper p : projects) {
            showProject(p);
        }

    }

    @Override
    public void showProject(ProjectWrapper project) {
        project.getTitle().toString();
        project.getDescription().toString();
        project.getCreationTime().toString();
        project.getDueTime().toString();
        project.getTasks().toString();
        project.isFinished();
    }

    @Override
    public void showTasks(Set<TaskWrapper> tasks) {
        for (TaskWrapper t : tasks){
            showTask(t);}
    }

    @Override
    public void showTask(TaskWrapper task) {
        task.getDescription().toString();
        task.getDependencySet().size();
        task.getEstimatedDuration();
        task.getAcceptableDeviation();
        
        if (task.isFinished()){
            task.wasFinishedEarly();
            task.wasFinishedLate();
            task.wasFinishedOnTime();
            task.getPerformedDuring();
            task.getDependencySet().size();
        }
    }

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
        long duration = negative1 ? random.nextLong() * 50 : random.nextLong() * -50;           // arbitrary
                                                                                                // constant
        boolean negative2 = random.nextBoolean();
        double deviation = negative2 ? random.nextDouble() * 2.0 : random.nextDouble() * -2.0; // arbitrary
                                                                                               // constant
        return new TaskData(description, duration, deviation);
    }

    @Override
    public TaskStatusData getUpdateStatusData() {
        if (random.nextBoolean()) return null;
        LocalDateTime date1 = getTimeStamp();
        LocalDateTime date2 = getTimeStamp();
        boolean success = random.nextBoolean();
        return new TaskStatusData(date1, date2, success);
    }

    @Override
    public ProjectData getProjectData() {
        if (random.nextBoolean()) return null;
        String title = randomString();
        String description = randomString();
        LocalDateTime due = getTimeStamp();
        return new ProjectData(title, description, due);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        if (random.nextBoolean()) return null;
        return LocalDateTime.of(
                random.nextInt(10000), random.nextInt(12) + 1, random.nextInt(28) + 1,
                random.nextInt(24), random.nextInt(60), random.nextInt(60));
    }

    @Override
    public void showError(String error) {
        error.toString();
    }

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
    public boolean start() {
        return random.nextDouble() < 0.5;
    }

    public void performActions(int amount) {
        for (int i = 0; i < amount; i++) {
            performAction();
        }
    }

    public void performAction() {
        int useCases = 5;
        switch (random.nextInt(useCases)) {
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

    @Override
    public TaskWrapper selectTaskFromProjects(Map<ProjectWrapper, Set<TaskWrapper>> projectMap) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LocalDateTime selectTime(List<LocalDateTime> options) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<ResourceWrapper> selectResourcesFor(Map<ResourceTypeWrapper, List<ResourceWrapper>> options) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<DeveloperWrapper> selectDevelopers(Set<DeveloperWrapper> developerOptions) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserInterface getSimulationUI() {
        return this;
    }

}
