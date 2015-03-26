package be.kuleuven.cs.swop.domain;


import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.task.Task;

public class PlanningManager {

    private Set<TaskPlanning> plannings = new HashSet<TaskPlanning>();

    public PlanningManager() {}

    public Set<TaskPlanning> getTaskPlannings() {
        return ImmutableSet.copyOf(plannings);
    }

    protected boolean canHaveAsTaskPlanning(TaskPlanning planning){
        return planning != null;
    }
    public void addPlanning(TaskPlanning planning) {
        if (!canHaveAsTaskPlanning(planning)) throw new IllegalArgumentException(ERROR_ILLEGAL_TASK_PLANNING);
        plannings.add(planning);
    }

    public boolean isPlanned(Task task) {
        for (TaskPlanning planning : this.plannings)
        {
            if (task == planning.getTask())
                return true;
        }
        return false;
    }

    public boolean isUnplanned(Task task) {
        return !this.isPlanned(task);
    }

    public TaskPlanning getPlanningFor(Task task) {
        for (TaskPlanning planning : this.plannings)
        {
            if (task == planning.getTask())
                return planning;
        }
        return null;
    }

    private static String ERROR_ILLEGAL_TASK_PLANNING = "Illegal TaskPlanning in Planning manager.";
}
