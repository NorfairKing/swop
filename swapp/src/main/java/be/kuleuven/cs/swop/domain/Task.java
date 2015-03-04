package be.kuleuven.cs.swop.domain;


import java.util.HashSet;
import java.util.Set;


public class Task {

    private String description;
    private Project project;
    private Duration estimatedDuration;
    private double acceptableDeviation;
    private Set<Task> dependencies = new HashSet<Task>();
    private Task original;
    
    public Task(String description, Duration estimatedDuration, Project project){
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setProject(project);
    }

    public String getDescription() {
        return description;
    }

    protected static boolean canHaveAsDescription(String description) {
        return description != null;
    }

    private void setDescription(String description) {
        if (!canHaveAsDescription(description)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DESCRIPTION); }
        this.description = description;
    }

    public Project getProject() {
        return project;
    }

    protected static boolean canHaveAsProject(Project project) {
        return project != null;
    }

    public void setProject(Project project) {
        if (!canHaveAsProject(project)) { throw new IllegalArgumentException(ERROR_ILLEGAL_PROJECT); }
        this.project = project;
    }

    public Duration getEstimatedDuration() {
        return estimatedDuration;
    }

    protected static boolean canHaveAsEstimatedDuration(Duration estimatedDuration) {
        return estimatedDuration != null;
    }

    public void setEstimatedDuration(Duration estimatedDuration) {
        if (!canHaveAsEstimatedDuration(estimatedDuration)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DURATION); }
        this.estimatedDuration = estimatedDuration;
    }

    public Task getOriginal() {
        return original;
    }

    protected static boolean canHaveAsOriginal(Task original) {
        return true;
    }

    public void setOriginal(Task original) {
        if (!canHaveAsOriginal(original)) { throw new IllegalArgumentException(ERROR_ILLEGAL_ORIGINAL); }
        this.original = original;
    }

    public static boolean canHaveAsDependency(Task dependency) {
        return dependency != null;
    }

    public void addDependency(Task dependency) {
        if (!canHaveAsDependency(dependency)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DEPENDENCY); }
        this.dependencies.add(dependency);
    }

    public double getAcceptableDeviation() {
        return acceptableDeviation;
    }
    
    public static boolean canHaveAsDeviation(double deviation){
        if(Double.isNaN(deviation)){return false;}
        if(Double.isInfinite(deviation)){return false;}
        if(deviation < 0){return false;}
        return true;
    }

    public void setAcceptableDeviation(double acceptableDeviation) {
        if (!canHaveAsDeviation(acceptableDeviation))throw new IllegalArgumentException(ERROR_ILLEGAL_DEVIATION);
        this.acceptableDeviation = acceptableDeviation;
    }

    private static final String ERROR_ILLEGAL_DESCRIPTION = "Illegal project for task.";
    private static final String ERROR_ILLEGAL_PROJECT = "Illegal project for task.";
    private static final String ERROR_ILLEGAL_DEVIATION = "Illegal acceptable deviation for task.";
    private static final String ERROR_ILLEGAL_DURATION = "Illegal estimated duration for task.";
    private static final String ERROR_ILLEGAL_STATUS = "Illegal status for task.";
    private static final String ERROR_ILLEGAL_ORIGINAL = "Illegal original for task.";
    private static final String ERROR_ILLEGAL_DEPENDENCY = "Illegal dependency set for task.";
    private static final String ERROR_ILLEGAL_DEPENDENCY_SET = "Illegal dependency for task.";

}
