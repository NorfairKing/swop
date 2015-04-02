package be.kuleuven.cs.swop.facade;


import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.resource.Resource;
import be.kuleuven.cs.swop.domain.task.Task;
import be.kuleuven.cs.swop.domain.user.Developer;


public class TaskPlanningWrapper {

    private TaskPlanning planning;

    public TaskPlanningWrapper(TaskPlanning planning) {
        setPlanning(planning);
    }

    TaskPlanning getPlanning() {
        return planning;
    }

    protected boolean canHaveAsPlanning(TaskPlanning planning) {
        return planning != null;
    }

    private void setPlanning(TaskPlanning planning) {
        if (!canHaveAsPlanning(planning)) { throw new IllegalArgumentException(ERROR_ILLEGAL_PLANNING); }
        this.planning = planning;
    }

    public Set<Developer> getDevelopers() {
        return ImmutableSet.copyOf(planning.getDevelopers());
    }

    public TaskWrapper getTask() {
        return new TaskWrapper(planning.getTask());
    }

    public TimePeriod getPeriod() {
        return planning.getPeriod();
    }

    public Set<Resource> getReservations() {
        return ImmutableSet.copyOf(planning.getReservations());
    }

    private static final String ERROR_ILLEGAL_PLANNING = "Illegal planning for planning wrapper.";

}
