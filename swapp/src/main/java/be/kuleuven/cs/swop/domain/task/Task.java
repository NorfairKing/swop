package be.kuleuven.cs.swop.domain.task;


import java.util.Set;
import java.util.UUID;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.task.status.TaskStatus;


public interface Task {

    public String getDescription();

    public static boolean canHaveAsDescription(String description) {
        return description != null;
    }

    public void setDescription(String description);

    public double getEstimatedDuration();

    public static boolean canHaveAsEstimatedDuration(double estimatedDuration) {
        return estimatedDuration > 0;
    }

    public void setEstimatedDuration(double estimatedDuration);

    public static boolean canHaveAsDependency(RealTask dependency) {
        return dependency != null;
    }

    public void addDependency(RealTask dependency);

    public double getAcceptableDeviation();

    public static boolean canHaveAsDeviation(double deviation) {
        if (Double.isNaN(deviation)) { return false; }
        if (Double.isInfinite(deviation)) { return false; }
        if (deviation < 0) { return false; }
        return true;
    }

    public void setAcceptableDeviation(double acceptableDeviation);

    public Task getAlternative();

    public static boolean canHaveAsAlternative(RealTask alternative) {
        return true;
    }

    public void setAlternative(RealTask alternative);

    public TimePeriod getPerformedDuring();

    public void performedDuring(TimePeriod timespan);

    public Set<RealTask> getDependencySet();

    public void finish();

    public void fail();

    public UUID getId();

    public static boolean canHaveAsID(UUID id) {
        return id != null;
    }

}
