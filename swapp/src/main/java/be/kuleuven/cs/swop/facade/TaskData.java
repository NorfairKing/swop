package be.kuleuven.cs.swop.facade;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;


public class TaskData {

    private String description;
    private long estimatedDuration;
    private double acceptableDeviation;
    private final Set<TaskWrapper> dependencies = new HashSet<>();

    public TaskData(String description, long estimatedDuration, double acceptableDeviation) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public long getEstimatedDuration() {
        return estimatedDuration;
    }

    private void setEstimatedDuration(long estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public double getAcceptableDeviation() {
        return acceptableDeviation;
    }

    private void setAcceptableDeviation(double acceptableDeviation) {
        this.acceptableDeviation = acceptableDeviation;
    }
    
    public void addDependency(TaskWrapper dependency) {
        dependencies.add(dependency);
    }
    
    public ImmutableSet<TaskWrapper> getDependencies() {
        return ImmutableSet.copyOf(dependencies);
    }

}
