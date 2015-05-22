package be.kuleuven.cs.swop.domain.company;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Requirements;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimeCalculator;


/**
 * A class that handles all the planning of and working on tasks.
 */
@SuppressWarnings("serial")
public class PlanningDepartment implements Serializable {

    private final BranchOffice office;

    public PlanningDepartment(BranchOffice office) {
        this.office = office;
    }

    // PASSTHROUGH GETTERS
    public Set<TaskPlanning> getTaskPlannings() {
        return office.getTaskPlannings();
    }

    public Set<Developer> getDevelopers() {
        return office.getDevelopers();
    }

    public Set<Resource> getResources() {
        return office.getResources();
    }

    public Set<ResourceType> getResourceTypes() {
        return office.getResourceTypes();
    }

    public Task getTaskFor(TaskPlanning planning) {
        return office.getTaskFor(planning);
    }

    // ADVANCED GETTERS
    public DateTimePeriod getEstimatedOrPlanningPeriod(TaskPlanning plan) {
        return getTaskFor(plan).getEstimatedOrPlanningPeriod();
    }

    /**
     * Retrieves the plannings for all the given tasks
     *
     * @param tasks
     *            The list of tasks we want the plannings for
     * @return A set of all the planning
     */
    public Set<TaskPlanning> getPlanningsFor(Set<Task> tasks) {
        Set<TaskPlanning> plans = new HashSet<TaskPlanning>();
        for (Task t : tasks) {
            TaskPlanning pl = t.getPlanning();
            if (pl != null) {
                plans.add(pl);
            }
        }
        return plans;
    }

    /**
     * Selects all unplanned tasks from the given set
     * 
     * @param tasks
     *            The set of tasks
     * @return Only the unplanned tasks from the set
     */
    public Set<Task> getUnplannedTasksFrom(Set<Task> tasks) {
        Set<Task> unplannedTasks = new HashSet<Task>();
        for (Task t : tasks) {
            if (!t.isPlanned()) {
                unplannedTasks.add(t);
            }
        }
        return unplannedTasks;
    }

    /**
     * Selects all tasks that are assigned to the given dev
     * 
     * @param tasks
     *            The tasks to check
     * @param dev
     *            The dev to check
     * @return The tasks to which the dev is assigned
     */
    public Set<Task> getAssignedTasksOf(Set<Task> tasks, Developer dev) {
        Set<TaskPlanning> allPlannings = getPlanningsFor(tasks);

        Set<Task> assignedTasks = new HashSet<Task>();
        for (TaskPlanning p : allPlannings) {
            if (p.getDevelopers().contains(dev)) {
                assignedTasks.add(getTaskFor(p));
            }
        }

        return assignedTasks;
    }

    /**
     * Retrieves a number of times on which the given task could be planned The search starts at the given time
     *
     * @param task
     *            The task
     * @param n
     *            How many options you want
     * @param theTime
     *            The time to start the search on
     * @return A list of possible times
     */
    public List<LocalDateTime> getPlanningTimeOptions(Task task, int n, LocalDateTime theTime) {
        theTime = theTime.plusMinutes(60 - theTime.getMinute());
        List<LocalDateTime> timeOptions = new ArrayList<LocalDateTime>();
        if (getResources().isEmpty() && !task.getRequirements().isEmpty()) // safety checks
            return timeOptions;
        if (task.getRecursiveRequirements().stream().anyMatch(p -> !hasResourcesOfType(p.getType(), getResources(), p.getAmount())))
            return timeOptions;
        for (int i = 0; timeOptions.size() < n && i < 2000; i++) { // 2000 iterations for safety.
            if (this.isValidTimeForTask(theTime, task))
                timeOptions.add(theTime);
            theTime = TimeCalculator.addWorkingMinutes(theTime, 60);
        }
        return timeOptions;
    }

    /**
     * Retrieves a list of options for each resource type needed by a task.
     *
     * @param task
     *            The task for which you want the options
     * @param time
     *            The time on which you cant to use the resources
     * @return The list with options
     */
    public Map<ResourceType, List<Resource>> getPlanningResourceOptions(Task task, LocalDateTime time) {
        Set<Resource> usedResources = new HashSet<Resource>();
        DateTimePeriod period = new DateTimePeriod(time, time.plusMinutes(task.getEstimatedDuration()));
        for (TaskPlanning planning : getTaskPlannings()) {
            if (getEstimatedOrPlanningPeriod(planning).isDuring(period)) {
                usedResources.addAll(planning.getReservations());
            }
        }
        Set<Resource> availableResources = new HashSet<Resource>(getResources());
        availableResources.removeAll(usedResources);
        availableResources = availableResources.stream().filter(p -> p.getType().isAvailableDuring(period)).collect(Collectors.toSet());
        Map<ResourceType, List<Resource>> map = new HashMap<ResourceType, List<Resource>>();
        for (Requirement req : task.getRecursiveRequirements()) {
            map.put(req.getType(), getResources().stream().filter(p -> p.isOfType(req.getType())).collect(Collectors.toList()));
        }
        return map;
    }

    /**
     * Retrieves a list of developers that can work on the task on a given time
     *
     * @param task
     *            The task for which you need developers
     * @param time
     *            The time on which you need developers
     * @return The possible developers
     */
    public Set<Developer> getPlanningDeveloperOptions(Task task, LocalDateTime time) {
        Set<Developer> usedDevelopers = new HashSet<Developer>();
        DateTimePeriod period = new DateTimePeriod(time, time.plusMinutes(task.getEstimatedDuration()));
        for (TaskPlanning planning : getTaskPlannings()) {
            if (getEstimatedOrPlanningPeriod(planning).isDuring(period)) {
                usedDevelopers.addAll(planning.getDevelopers());
            }
        }
        Set<Developer> availableDevelopers = new HashSet<Developer>(getDevelopers().stream().filter(d -> d.isAvailableDuring(period)).collect(Collectors.toSet()));
        availableDevelopers.removeAll(usedDevelopers);
        return availableDevelopers;
    }

    // VALIDATORS

    /**
     * Removes a planning, freeing everything that it reserved.
     *
     * @param planning
     *            The planning that will be removed
     */
    public void removePlanning(TaskPlanning planning) {
        getTaskFor(planning).removePlanning();
    }

    /**
     * Is this developer available at this time
     *
     * @param dev
     *            The developer for whom we are checking
     * @param task
     *            The task that you want to do
     * @param time
     *            The time on which you want to do it
     * @return Whether the developer is available to do this task at the given time
     */
    public boolean isAvailableFor(Developer dev, Task task, LocalDateTime time) {
        DateTimePeriod period = new DateTimePeriod(time, time.plusMinutes(task.getEstimatedDuration()));
        if (!dev.isAvailableDuring(time)) { return false; }
        if(getConflictIfExists(task, period, new HashSet<Resource>(Arrays.asList((Resource) dev))) != null) { return false; }
        return isTier2Available(period, task);
    }

    /**
     * Can the requiredment be satiesfied during the period? Checks to see if no planning has reserved them
     *
     * @param req
     *            The requirement to check
     * @param t
     *            The task of which the requirement is
     * @param period
     *            The period during which we want to use it
     * @return Whether or not enough resources are available at the time
     */
    private boolean canRequirementOfBeSatisfiedDuring(Requirement req, Task t, DateTimePeriod period) {
        ResourceType type = req.getType();

        Set<Resource> tempResources = new HashSet<>(getResources());

        for (TaskPlanning plan : getTaskPlannings()) {
            if (getEstimatedOrPlanningPeriod(plan).overlaps(period) && getTaskFor(plan) != t) {
                // System.out.println("overlap: " + getEstimatedOrPlanningPeriod(plan).toString() + " <-> " + period.toString());
                tempResources.removeAll(plan.getReservations());
            }
        }

        int ofTypeLeft = 0;
        for (Resource res : tempResources) {
            // System.out.println("res: " + res.getName());
            if (res.isOfType(type)) {
                ofTypeLeft += 1;
            }
        }

        // System.out.println("Amount left: " + ofTypeLeft);
        return req.getAmount() <= ofTypeLeft;
    }

    /**
     * Check if the task is available. This is the 'available' described in the second iteration. Alternatively could be called 'canMoveToExecuting'
     *
     * @param time
     *            The time to check for
     * @param task
     *            The task to check
     * @return Whether or not it is available
     */
    public boolean isTier2Available(DateTimePeriod period, Task task) {
        if (!task.isTier1Available()) {
            /*
             * System.out.println("1: " + task.getDescription() + "; " + task.getDependencySet().size()); for (Task dep: task.getDependencySet()) { System.out.println(dep.getDescription() + "; " +
             * dep.isFinishedOrHasFinishedAlternative()); }
             */
            return false;
        }
        for (Requirement req : task.getRecursiveRequirements()) {
            if (!canRequirementOfBeSatisfiedDuring(req, task, period)) {
                // System.out.println("5: " + req.getType().getName());
                return false;
            }
        }

        return true;
    }

    /**
     * Checks to see if the task could be planned to start on this time
     */
    private boolean isValidTimeForTask(LocalDateTime time, Task task) {
        Set<Resource> usedResources = new HashSet<Resource>();
        Set<Developer> usedDevelopers = new HashSet<Developer>();
        DateTimePeriod period = new DateTimePeriod(time, time.plusMinutes(task.getEstimatedDuration()));
        for (TaskPlanning planning : getTaskPlannings()) {
            if (getEstimatedOrPlanningPeriod(planning).isDuring(period)) {
                usedResources.addAll(planning.getReservations());
                usedDevelopers.addAll(planning.getDevelopers());
            }
        }
        Set<Resource> availableResources = new HashSet<Resource>(getResources().stream().filter(r -> r.getType().isAvailableDuring(period)).collect(Collectors.toSet()));
        availableResources.removeAll(usedResources);
        Set<Developer> availableDevelopers = new HashSet<Developer>(getDevelopers().stream().filter(d -> d.isAvailableDuring(period)).collect(Collectors.toSet()));
        availableDevelopers.removeAll(usedDevelopers);
        for (Requirement req : task.getRecursiveRequirements()) {
            if (!hasResourcesOfType(req.getType(), availableResources, req.getAmount()))
                return false;
        }
        if (availableDevelopers.isEmpty())
            return false;
        return true;
    }

    /**
     * Checks to see if the given set has enough resources of the given type Enough is 'n or more'
     */
    private boolean hasResourcesOfType(ResourceType type, Set<Resource> resources, int number) {
        int counter = 0;
        for (Resource resource : resources) {
            if (resource.getType().equals(type)) {
                counter++;
            }
            if (counter >= number)
                return true;
        }
        return false;
    }

    private void checkPlanningParameters(Task task, DateTimePeriod period, Set<Resource> resources) throws ConflictingPlannedTaskException {
        if (task == null) { throw new IllegalArgumentException(ERROR_ILLEGAL_TASK); }

        if (period == null) { throw new IllegalArgumentException(ERROR_ILLEGAL_DATETIME); }

        if (resources == null) { throw new IllegalArgumentException(ERROR_ILLEGAL_RESOURCE_SET_NULL); }

        
        for (Resource res : resources) {
            if (res == null) { throw new IllegalArgumentException(ERROR_ILLEGAL_RESOURCE); }
        }

        // Check if the task is already planned
        if (task.isPlanned()) { throw new IllegalArgumentException(ERROR_TASK_ALREADY_PLANNED); }

        // Check if the set of resources is consistent
        if (!Requirements.isPossibleResourceSet(resources)) { throw new IllegalArgumentException(ERROR_ILLEGAL_RESOURCE_SET); }

        // Check if the resources are available during the planning
        if (!areResourcesAvailableDuring(resources, period)) { throw new IllegalArgumentException(
                ERROR_RESOURCE_NOT_AVAILABLE); }
        
        // Check if all needed resources are present
        if (!task.getTaskInfo().getRequirements().isSatisfiedWith(resources)) { throw new IllegalArgumentException(ERROR_ILLEGAL_RESOURCE_SET); }


        // Check if the resources aren't already planned for another task
        TaskPlanning conflict = getConflictIfExists(task, period, resources);
        if (conflict != null) { throw new ConflictingPlannedTaskException(getTaskFor(conflict)); }
    }

    private TaskPlanning getConflictIfExists(Task task, DateTimePeriod period, Set<Resource> resources) {
        for (TaskPlanning plan : getTaskPlannings()) {
            for (Resource res : resources) {
                if (getEstimatedOrPlanningPeriod(plan).overlaps(period)) {
                    if (plan.getReservations().contains(res)) { return plan; }
                }
            }
        }
        return null;
    }

    private boolean areResourcesAvailableDuring(Set<Resource> resources, DateTimePeriod period) {
        for (Resource res : resources) {
            if (!res.getType().isAvailableDuring(period)) { return false; }
        }
        return true;
    }

    // ACTIONS

    /**
     * Creates a planning and keeps track of it.
     *
     * @param task
     *            The task this new planning will be for
     * @param period
     *            The planned period for this task
     * @param resources
     *            The resources that have been reserved for the task
     * @throws ConflictingPlannedTaskException
     *             If the created planning will result in a conflict.
     */
    public void createPlanning(Task task, DateTimePeriod period, Set<Resource> resources) throws ConflictingPlannedTaskException {
        createPlanning(task, period, resources, false);
    }

    /**
     * Creates a planning and keeps track of it, the created planning will include a break for the developers.
     *
     * @param task
     *            The task this new planning will be for
     * @param period
     *            The planned period for this task
     * @param resources
     *            The resources that have been reserved for the task
     * @param withBreak
     * 				Whether or not to include a break in this planning. Breaks are 1 hour long.
     * @throws ConflictingPlannedTaskException
     *             If the created planning will result in a conflict.
     */
    public void createPlanning(Task task, DateTimePeriod period, Set<Resource> resources, boolean withBreak) throws ConflictingPlannedTaskException {
        if(withBreak){
            period = new DateTimePeriod(period.getStartTime(), period.getStopTime().plusMinutes(Developer.BREAK_TIME));
        }
        checkPlanningParameters(task, period, resources);
        TaskPlanning newplanning = new TaskPlanning(period, resources);
        task.plan(newplanning);
    }

    /**
     * Set that the task was finished during the given period
     *
     * @param t
     *            The task to finish
     * @param endTime
     *            The time it was finished
     * @throws ConflictingPlannedTaskException When it conflicts with another task.
     */
    public void finishTask(Task t, LocalDateTime endTime) throws ConflictingPlannedTaskException {
        DateTimePeriod period = new DateTimePeriod(t.getPlanning().getPlannedStartTime(), endTime);
        updatePlanningForState(t,period, t.getPlanning().getReservations());
        t.finish();
    }

    /**
     * Set that the task was failed during the given period
     *
     * @param t
     *            The task that failed
     * @param endTime
     *            The time it failed
     * @throws ConflictingPlannedTaskException When it conflicts with another task.
     */
    public void failTask(Task t, LocalDateTime endTime) throws ConflictingPlannedTaskException {
        if(t.isExecuting()){
            DateTimePeriod period = new DateTimePeriod(t.getPlanning().getPlannedStartTime(), endTime);
            updatePlanningForState(t,period, t.getPlanning().getReservations());
        }else{
            t.removePlanning();
        }
        t.fail();
    }
    
    private void updatePlanningForState(Task t, DateTimePeriod period, Set<Resource> reservations) throws ConflictingPlannedTaskException{
        TaskPlanning plan = t.getPlanning();
        t.removePlanning();
        try{
        createPlanning(t, period, reservations);
        } catch (ConflictingPlannedTaskException e) {
            t.plan(plan);
            throw e;
        }
    }

    /**
     * Start the execution of a task is possible
     *
     * @param t
     *            The task to execute
     * @param time
     *            The current time
     * @param resources
     *            The used resources
     * @throws ConflictingPlannedTaskException 
     * 			When it conflicts with another task.
     * @throws IllegalStateException
     *             If the task can't start execution
     */
    public void startExecutingTask(Task t, LocalDateTime time, Set<Resource> resources) throws ConflictingPlannedTaskException {
        if (t.isTier1Available()) {
            DateTimePeriod period = new DateTimePeriod(time, time.plusMinutes(t.getEstimatedDuration()));
            updatePlanningForState(t,period, resources);
            t.execute();
        }
        else {
            throw new IllegalStateException(ERROR_ILLEGAL_EXECUTING_STATE);
        }
    }

    private static final String ERROR_ILLEGAL_EXECUTING_STATE = "Can't execute a task with unfinished dependencies.";
    private static final String ERROR_ILLEGAL_TASK            = "Illegal task provided.";
    private static final String ERROR_ILLEGAL_DATETIME        = "Illegal date provided.";
    private static final String ERROR_ILLEGAL_RESOURCE        = "Illegal resource provided.";
    private static final String ERROR_ILLEGAL_RESOURCE_SET_NULL    = "The given set of resources is null.";
    private static final String ERROR_ILLEGAL_RESOURCE_SET    = "The given set of resources is not possible.";
    private static final String ERROR_RESOURCE_NOT_AVAILABLE  = "A resource is not available at that time.";
    private static final String ERROR_TASK_ALREADY_PLANNED    = "The given Task already has a planning.";

}
