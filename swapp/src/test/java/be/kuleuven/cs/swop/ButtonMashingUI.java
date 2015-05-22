package be.kuleuven.cs.swop;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.user.User;
import be.kuleuven.cs.swop.facade.BranchOfficeWrapper;
import be.kuleuven.cs.swop.facade.ProjectData;
import be.kuleuven.cs.swop.facade.ProjectWrapper;
import be.kuleuven.cs.swop.facade.SessionController;
import be.kuleuven.cs.swop.facade.SimulationStepData;
import be.kuleuven.cs.swop.facade.TaskData;
import be.kuleuven.cs.swop.facade.TaskWrapper;


public class ButtonMashingUI implements UserInterface {

    private static String        VALID_CHARACTERS                 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-0123456789";
    private SessionController    sessionController;
    private Random               random;

    private static String        ERROR_ILLEGAL_SESSION_CONTROLLER = "Illegal session controller for CLI.";

    private Map<String, Integer> errorCount                       = new HashMap<>();

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

    public Map<String, Integer> getErrorCount() {
        return errorCount;
    }

    @Override
    public User selectUser(Set<User> users) {
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
        for (TaskWrapper t : tasks) {
            showTask(t);
        }
    }

    @Override
    public void showTask(TaskWrapper task) {
        task.getDescription().toString();
        task.getDependencySet().size();
        task.getEstimatedDuration();
        task.getAcceptableDeviation();

        if (task.isFinished()) {
            task.wasFinishedEarly();
            task.wasFinishedLate();
            task.wasFinishedOnTime();
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
    public TaskWrapper selectTaskFromProjects(Map<ProjectWrapper, Set<TaskWrapper>> projectMap) {
        ProjectWrapper p = selectFromCollection(projectMap.keySet());
        if (p == null) {
            return null;
        } else {
            return selectFromCollection(projectMap.get(p));
        }
    }

    @Override
    public LocalDateTime selectTime(List<LocalDateTime> options) {
        return selectFromCollection(options);
    }

    @Override
    public SimulationStepData getSimulationStepData() {
        return new SimulationStepData(random.nextBoolean(), random.nextBoolean());
    }

    @Override
    public void showTaskPlanningContext(TaskWrapper task) {
        task.canFinish();
        task.getAcceptableDeviation();
        task.getAlternative();
        task.getDependencySet();
        task.getDescription();
        task.getEstimatedDuration();
        task.getRequirements();
        task.getRecursiveRequirements();
        task.isExecuting();
        task.isFailed();
        task.isFinal();
        task.isFinished();
        task.wasFinishedEarly();
        task.wasFinishedLate();
        task.wasFinishedOnTime();
    }

    @Override
    public Set<Resource> selectResourcesFor(Map<ResourceType, List<Resource>> options, Set<Requirement> requirements) {
        int n = random.nextInt(options.size() + 1);
        Set<Resource> result = new HashSet<>();
        for (int i = 0; i < n; i++) {
            ResourceType t = selectFromCollection(options.keySet());
            Resource res = selectFromCollection(options.get(t));
            result.add(res);
            
        }
        return result;
    }

    @Override
    public TaskData getTaskData(Set<ResourceType> types) {
        if (random.nextDouble() < 0.1) return null;
        String description = randomString();
        long duration = (long) random.nextInt(480); // arbitrary
                                                    // constant
        boolean negative2 = random.nextBoolean();
        double deviation = negative2 ? random.nextDouble() * 2.0 : random.nextDouble() * -2.0; // arbitrary
                                                                                               // constant
        return new TaskData(description, duration, deviation);
    }

    @Override
    public ProjectData getProjectData() {
        if (random.nextDouble() < 0.1) return null;
        String title = randomString();
        String description = randomString();
        LocalDateTime due = getTimeStamp();
        return new ProjectData(title, description, due);
    }

    @Override
    public LocalDateTime getTimeStamp() {
        if (random.nextDouble() < 0.1) return null;
        return LocalDateTime.of(
                random.nextInt(10000), random.nextInt(12) + 1, random.nextInt(28) + 1,
                random.nextInt(24), random.nextInt(60), random.nextInt(60));
    }

    @Override
    public void showError(String error) {
        System.out.println(error);
        if (errorCount.containsKey(error)) {
            int cur = errorCount.get(error);
            errorCount.put(error, cur + 1);
        }
        else {
            errorCount.put(error, 1);
        }
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
        double ch = 0;
        double c;
        O result = null;
        for (O o : objects) {
            c = random.nextDouble();
            if (c > ch) {
                ch = c;
                result = o;
            }
        }
        return result;
    }

    @Override
    public boolean start() {
        return random.nextBoolean();
    }

    public void performActions(int amount) {
        for (int i = 0; i < amount; i++) {
            performAction();
        }
    }

    public void performAction() {

        List<Runnable> sessions = new ArrayList<>();
        sessions.add(() -> getSessionController().startAdvanceTimeSession());
        sessions.add(() -> getSessionController().startCreateProjectSession());
        sessions.add(() -> getSessionController().startCreateTaskSession());
        sessions.add(() -> getSessionController().startShowProjectsSession());
        sessions.add(() -> getSessionController().startUpdateTaskStatusSession());
        sessions.add(() -> getSessionController().startPlanTaskSession());
        sessions.add(() -> getSessionController().startRunSimulationSession());
        sessions.add(() -> getSessionController().startSelectUserSession());
        sessions.add(() -> getSessionController().startUpdateTaskStatusSession());
        sessions.add(() -> getSessionController().startDelegateTaskSession());

        int toCall = random.nextInt(sessions.size());
        sessions.get(toCall).run();
    }

    @Override
    public boolean askToAddBreak() {
        return random.nextBoolean();
    }

    @Override
    public BranchOfficeWrapper selectOffice(Set<BranchOfficeWrapper> offices) {
        return selectFromCollection(offices);
    }

    @Override
    public String getFileName() {
        return selectFromCollection(Arrays.asList("../save_files/test.json", "blablabla.doesnexist"));
    }

    @Override
    public boolean askExecute() throws ExitEvent {
        return random.nextBoolean();
    }

    @Override
    public boolean askFinish() throws ExitEvent {
        return random.nextBoolean();
    }

    @Override
    public Set<Resource> askSelectnewResources(Set<Resource> resources,
            Map<ResourceType, List<Resource>> resourceOptions,
            Set<Requirement> reqs) throws ExitEvent {
        return resources;
    }

}
