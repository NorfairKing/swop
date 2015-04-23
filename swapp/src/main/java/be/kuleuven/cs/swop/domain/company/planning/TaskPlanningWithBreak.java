package be.kuleuven.cs.swop.domain.company.planning;


import java.time.LocalDateTime;
import java.util.Set;

import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.domain.DateTimePeriod;

/**
 * A class that represents the reservation of a task.
 * It also includes the reservation of all needed resources and the developers assigned to do it.
 * It ofcourse also includes a planned starting time.
 * This special type of planning also includes a break for the developers.
 * 
 */
@SuppressWarnings("serial")
public class TaskPlanningWithBreak extends TaskPlanning {

    public TaskPlanningWithBreak(Set<Developer> developers, Task task, LocalDateTime plannedStartTime){
        super(developers,task,plannedStartTime,null);
    }

    public TaskPlanningWithBreak(Set<Developer> developers, Task task, LocalDateTime plannedStartTime, Set<Resource> resources) {
        super(developers, task, plannedStartTime,resources);
    }

    @Override
    public DateTimePeriod getEstimatedOrRealPeriod() {
        if (getTask().isFailed() || getTask().isFinished()) {
            return getTask().getPerformedDuring();
        } else {
            long taskDur = getTask().getEstimatedDuration() + 60 * Developer.BREAK_TIME;
            LocalDateTime estimatedEndTime = getPlannedStartTime().plusMinutes(taskDur);
            return new DateTimePeriod(getPlannedStartTime(), estimatedEndTime);
        }
    }

    @Override
    public boolean includesBreak() {
        return true;
    }

}
