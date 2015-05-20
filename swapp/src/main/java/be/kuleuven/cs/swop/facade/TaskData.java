package be.kuleuven.cs.swop.facade;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.swop.domain.company.resource.ResourceType;

import com.google.common.collect.ImmutableSet;


public class TaskData {

    private String description;
    private long estimatedDuration;
    private double acceptableDeviation;
    private final Set<TaskWrapper> dependencies = new HashSet<>();
    private Map<ResourceType, Integer> requirements;

    public TaskData(String description, long estimatedDuration, double acceptableDeviation, Map<ResourceType, Integer> requirements) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
        setRequirements(requirements);
    }
    public TaskData(String description, long estimatedDuration, double acceptableDeviation) {
    	this(description, estimatedDuration, acceptableDeviation, null);

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

	public Map<ResourceType, Integer> getRequirements() {
		return requirements;
	}

	public void setRequirements(Map<ResourceType, Integer> requirements) {
		this.requirements = requirements;
	}

}
