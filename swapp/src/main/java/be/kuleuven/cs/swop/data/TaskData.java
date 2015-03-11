package be.kuleuven.cs.swop.data;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.TaskWrapper;

import com.google.common.collect.ImmutableSet;


public class TaskData {

    private String description;
    private double estimatedDuration;
    private double acceptableDeviation;
    private final Set<TaskWrapper> dependencies = new HashSet<>();

    public TaskData(String description, double estimatedDuration, double acceptableDeviation) {
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

    public double getEstimatedDuration() {
        return estimatedDuration;
    }

    private void setEstimatedDuration(double estimatedDuration) {
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
