package be.kuleuven.cs.swop.domain;


import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.planning.TaskPlanning;

public class PlanningManager {

    private Set<TaskPlanning> plannings = new HashSet<TaskPlanning>();

    public PlanningManager() {}

    public Set<TaskPlanning> getTaskPlannings() {
        return ImmutableSet.copyOf(plannings);
    }

    protected boolean canHaveAsTaskPlanning(TaskPlanning planning){
        return planning != null;
    }
    public void addProject(TaskPlanning Planning) {
        if (!canHaveAsTaskPlanning(Planning)) throw new IllegalArgumentException(ERROR_ILLEGAL_Task_Planning);
        plannings.add(Planning);
    }

    private static String ERROR_ILLEGAL_Task_Planning = "Illegal TaskPlanning in Planning manager.";
}
