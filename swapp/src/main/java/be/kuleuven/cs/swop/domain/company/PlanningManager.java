package be.kuleuven.cs.swop.domain.company;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.resource.Requirement;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.resource.TimeConstrainedResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.TimeCalculator;
import be.kuleuven.cs.swop.domain.TimePeriod;

/**
 * A class that handles all the planning of and working on tasks.
 */
@SuppressWarnings("serial")
public class PlanningManager implements Serializable {

    private Set<TaskPlanning> plannings = new HashSet<TaskPlanning>();
    private Set<Resource> resources = new HashSet<Resource>();
    private Set<ResourceType> resourceTypes = new HashSet<ResourceType>();
    private Set<Developer> developers = new HashSet<Developer>();

    public ImmutableSet<TaskPlanning> getTaskPlannings() {
        return ImmutableSet.copyOf(plannings);
    }

    protected boolean canHaveAsTaskPlanning(TaskPlanning planning){
        return planning != null;
    }
    public void addPlanning(TaskPlanning planning) {
        if (!canHaveAsTaskPlanning(planning)) throw new IllegalArgumentException(ERROR_ILLEGAL_TASK_PLANNING);
        plannings.add(planning);
    }

    /**
     * Checks to see if this task if planned
     * 
     * @param task The task to check
     * @return Whether or not it is planned
     */
    public boolean isPlanned(Task task) {
        return this.getPlanningFor(task) != null;
    }

    public boolean isUnplanned(Task task) {
        return !this.isPlanned(task);
    }
    
    /**
     * Is this developer available at this time
     * 
     * @param dev The developer for whom we are checking
     * @param task The task that you want to do
     * @param time The time on which you want to do it
     * @return Whether the developer is available to do this task at the given time
     */
    public boolean isAvailableFor(Developer dev, Task task, LocalDateTime time) {
        if (!dev.isAvailableDuring(time)) {
            return false;
        }
        for (TaskPlanning plan: plannings) {
            if (plan.getDevelopers().contains(dev) &&
                plan.getEstimatedOrRealPeriod().isDuring(time) &&
                plan.getTask() != task
                ) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Can the requiredment be satiesfied during the period?
     * Checks to see if no planning has reserved them
     * 
     * @param req The requirement to check
     * @param t The task of which the requirement is
     * @param period The period during which we want to use it
     * @return Whether or not enough resources are available at the time
     */
    private boolean canRequirementOfBeSatisfiedDuring(Requirement req, Task t, DateTimePeriod period) {
        ResourceType type = req.getType();
        
        Set<Resource> tempResources = new HashSet<>(resources);
        
        for (TaskPlanning plan: plannings) {
            if (plan.getEstimatedOrRealPeriod().overlaps(period) && plan.getTask() != t) {
                System.out.println("overlap: " + plan.getEstimatedOrRealPeriod().toString() + " <-> " + period.toString());
                tempResources.removeAll(plan.getReservations());
            }
        }
        
        int ofTypeLeft = 0;
        for (Resource res: tempResources) {
            System.out.println("res: " + res.getName());
            if (res.isOfType(type)) {
                ofTypeLeft += 1;
            }
        }
        
        System.out.println("Amount left: " + ofTypeLeft);
        return req.getAmount() <= ofTypeLeft;
    }
    
    
    /**
     * Check if the task is available.
     * This is the 'available' described in the second iteration.
     * Alternatively could be called 'canMoveToExecuting'
     * 
     * @param time The time to check for
     * @param dev The developer for whom the task might be available
     * @param task The task to check
     * @return Whether or not it is available
     */
    public boolean isTier2AvailableFor(LocalDateTime time, Developer dev,Task task){
        if (!task.isTier1Available()) {
            /*System.out.println("1: " + task.getDescription() + "; " + task.getDependencySet().size());
            for (Task dep: task.getDependencySet()) {
                System.out.println(dep.getDescription() + "; " + dep.isFinishedOrHasFinishedAlternative());
            }*/
            return false;
        }
        
        TaskPlanning planning = getPlanningFor(task);
        if (planning == null){
            //System.out.println("2");
            return false;
        }
        Set<Developer> devs = planning.getDevelopers();
        if (!devs.contains(dev)){
            //System.out.println("3");
            return false;
        }
        for (Developer d : devs ){
            if (!isAvailableFor(d, task, time)){
                //System.out.println("4");
                return false;
            }
        }
        if(planning.getEstimatedOrRealPeriod().isDuring(time)){
            // No problem, the planning already makes sure these reservations are in order.
        }else{
            for(Requirement req: task.getRecursiveRequirements()){//TODO does this have to be recursive or not?!
                DateTimePeriod startingNow = new DateTimePeriod(time, time.plusMinutes(task.getEstimatedDuration()));
                if (!canRequirementOfBeSatisfiedDuring(req, task, startingNow)){
                    //System.out.println("5: " + req.getType().getName());
                    return false;
                }
            }
        }
        
        return true;
    }

    /**
     * Returns the planning for a given task
     * 
     * @param task The task
     * @return The planning
     */
    public TaskPlanning getPlanningFor(Task task) {
        for (TaskPlanning planning : this.plannings)
        {
            if (task == planning.getTask())
                return planning;
        }
        return null;
    }
    
    /**
     * Retrieves the plannings for all the given tasks
     * 
     * @param tasks The list of tasks we want the plannings for
     * @return A set of all the planning
     */
    public Set<TaskPlanning> getPlanningsFor(Set<Task> tasks){
        Set<TaskPlanning> plans = new HashSet<TaskPlanning>();
        for(Task t : tasks){
            TaskPlanning pl = getPlanningFor(t);
            if(pl != null){
            plans.add(pl);
            }
        }
        return plans;
    }
    
    /**
     * Selects all unplanned tasks from the given set
     * @param tasks The set of tasks
     * @return Only the unplanned tasks from the set
     */
    public Set<Task> getUnplannedTasksFrom(Set<Task> tasks){
        Set<Task> unplannedTasks = new HashSet<Task>();
        for (Task t : tasks) {
            if (isUnplanned(t)) {
                unplannedTasks.add(t);
            }
        }
        return unplannedTasks;
    }
    
    /**
     * Selects all tasks that are assigned to the given dev
     * @param tasks The tasks to check
     * @param dev The dev to check
     * @return The tasks to which the dev is assigned
     */
    public Set<Task> getAssignedTasksOf(Set<Task> tasks, Developer dev){
        Set<TaskPlanning> allPlannings = getPlanningsFor(tasks);
        
        Set<Task> assignedTasks = new HashSet<Task>();
        for(TaskPlanning p : allPlannings){
            if(p.getDevelopers().contains(dev)){
                assignedTasks.add(p.getTask());
            }
        }
        
        return assignedTasks;
    }

    /**
     * Retrieves a number of times on which the given task could be planned
     * The search starts at the given time
     * 
     * @param task The task
     * @param n How many options you want
     * @param theTime The time to start the search on
     * @return A list of possible times
     */
    public List<LocalDateTime> getPlanningTimeOptions(Task task, int n, LocalDateTime theTime) {
        theTime = theTime.plusMinutes(60-theTime.getMinute());
        List<LocalDateTime> timeOptions = new ArrayList<LocalDateTime>();
        if (this.resources.isEmpty()) //safety checks
            return timeOptions;
        if (task.getRecursiveRequirements().stream().anyMatch(p -> !hasResourcesOfType(p.getType(),this.resources, p.getAmount())))
            return timeOptions;
        for (int i = 0; timeOptions.size() < n && i < 2000; i++) { //2000 iterations for safety, is this dirty? YES, fix it! (or at least use a constant)
            if (this.isValidTimeForTask(theTime,task))
                timeOptions.add(theTime);
            theTime = TimeCalculator.addWorkingMinutes(theTime,60);
        }
        return timeOptions;
    }

    /**
     * Checks to see if the task could be planned to start on this time
     */
    private boolean isValidTimeForTask(LocalDateTime time, Task task) {
        Set<Resource> usedResources = new HashSet<Resource>();
        Set<Developer> usedDevelopers = new HashSet<Developer>();
        DateTimePeriod period = new DateTimePeriod(time, task.getEstimatedOrRealFinishDate(time));
        for (TaskPlanning planning : this.plannings) {
            if (planning.getEstimatedOrRealPeriod().isDuring(period)) {
                usedResources.addAll(planning.getReservations());
                usedDevelopers.addAll(planning.getDevelopers());
            }
        }
        Set<Resource> availableResources = new HashSet<Resource>(this.resources.stream().filter(r -> r.getType().isAvailableDuring(period)).collect(Collectors.toSet()));
        availableResources.removeAll(usedResources);
        Set<Developer> availableDevelopers = new HashSet<Developer>(this.developers.stream().filter( d -> d.isAvailableDuring(period)).collect(Collectors.toSet()));
        availableDevelopers.removeAll(usedDevelopers);
        for (Requirement req : task.getRecursiveRequirements()) {
            if (!hasResourcesOfType(req.getType(), availableResources, req.getAmount()))
                return false;
        }
        if(availableDevelopers.isEmpty())
            return false;
        return true;
    }

    /**
     * Checks to see if the given set has enough resources of the given type
     * Enough is 'n or more'
     */
    private boolean hasResourcesOfType(ResourceType type, Set<Resource> resources, int number) {
        int counter = 0;
        for (Resource resource : resources) {
            if (resource.getType() == type) {
                counter++;
            }
            if (counter >= number)
                return true;
        }
        return false;
    }

    /**
     * Retrieves a list of options for each resource type needed by a task.
     * 
     * @param task The task for which you want the options
     * @param time The time on which you cant to use the resources
     * @return The list with options
     */
    public Map<ResourceType, List<Resource>> getPlanningResourceOptions(Task task, LocalDateTime time) {
        Set<Resource> usedResources = new HashSet<Resource>();
        DateTimePeriod period = new DateTimePeriod(time, task.getEstimatedOrRealFinishDate(time));
        for (TaskPlanning planning : this.plannings) {
            if (planning.getEstimatedOrRealPeriod().isDuring(period)) {
                usedResources.addAll(planning.getReservations());
            }
        }
        Set<Resource> availableResources = new HashSet<Resource>(this.resources);
        availableResources.removeAll(usedResources);
        availableResources = availableResources.stream().filter( p -> p.getType().isAvailableDuring(period)).collect(Collectors.toSet());
        Map<ResourceType,List<Resource>> map = new HashMap<ResourceType,List<Resource>>();
        for (Requirement req : task.getRecursiveRequirements()) {
            map.put(req.getType(), this.resources.stream().filter( p -> p.isOfType(req.getType())).collect(Collectors.toList()));
        }
        return map;
    }
    
    public ImmutableSet<Resource> getResources() {
        return ImmutableSet.copyOf(resources);
    }

    /**
     * Retrieves a list of developers that can work on the task on a given time
     * 
     * @param task The task for which you need developers
     * @param time The time on which you need developers
     * @return The possible developers
     */
    public Set<Developer> getPlanningDeveloperOptions(Task task, LocalDateTime time) {
        Set<Developer> usedDevelopers = new HashSet<Developer>();
        DateTimePeriod period = new DateTimePeriod(time, task.getEstimatedOrRealFinishDate(time));
        for (TaskPlanning planning : this.plannings) {
            if (planning.getEstimatedOrRealPeriod().isDuring(period)) {
                usedDevelopers.addAll(planning.getDevelopers());
            }
        }
        Set<Developer> availableDevelopers = new HashSet<Developer>(this.developers.stream().filter( d -> d.isAvailableDuring(period)).collect(Collectors.toSet()));
        availableDevelopers.removeAll(usedDevelopers);
        return availableDevelopers;
    }

    public void createPlanning(Task task, LocalDateTime estimatedStartTime, Set<Resource> resources, Set<Developer> devs) throws ConflictingPlanningException{
        if (this.getPlanningFor(task) != null) {
            throw new IllegalArgumentException(ERROR_TASK_ALREADY_PLANNED);
        }
        TaskPlanning newplanning = new TaskPlanning(devs, task, estimatedStartTime, resources);
        TaskPlanning conflict = getConflictIfExists(newplanning);
        if(conflict != null){
        	throw new ConflictingPlanningException(conflict);
        }
        this.plannings.add(newplanning);
    }
    
    public void removePlanning(TaskPlanning planning){
    	this.plannings.remove(planning);
    }
    
    private TaskPlanning getConflictIfExists(TaskPlanning newPlanning){
    	for (TaskPlanning plan: plannings) {
    		for(Resource res: newPlanning.getReservations()){
    			if (plan.getEstimatedOrRealPeriod().overlaps(newPlanning.getEstimatedOrRealPeriod())) {
    				if(plan.getReservations().contains(res)){
    					return plan;
    				}
    			}
    		}
    	}
    	return null;
    }
    
    public ImmutableSet<Developer> getDevelopers() {
        return ImmutableSet.copyOf(developers);
    }
    
    public Developer createDeveloper(String name){
        Developer dev = new Developer(name);
        developers.add(dev);
        return dev;
    }

    public Resource createResource(String name, ResourceType type){
        Resource resource = new Resource(type,name);
        resources.add(resource);
        return resource;
    }
    
    public ResourceType createResourceType(String name, Set<ResourceType> requires, Set<ResourceType> conflicts,boolean selfConflicting, TimePeriod availability){
    	ResourceType type;
    	if(availability != null){
        	type = new TimeConstrainedResourceType(name,requires,conflicts, selfConflicting, availability);
        }else{
        	type = new ResourceType(name,requires,conflicts, selfConflicting);
        }
        resourceTypes.add(type);
        return type;
    }
    
    public ImmutableSet<ResourceType> getResourceTypes(){
    	return ImmutableSet.copyOf(resourceTypes);
    }
    
    /**
     * Set that the task was finished during the given period
     * 
     * @param t The task to finish
     * @param period The period in which it was finished
     */
    public void finishTask(Task t, DateTimePeriod period){
        t.finish(period);
    }
    
    /**
     * Set that the task was failed during the given period
     * 
     * @param t The task that failed
     * @param period The period in which it failed
     */
    public void failTask(Task t,DateTimePeriod period){
        t.fail(period);
    }
    
    /**
     * Start the execution of a task is possible
     * 
     * @param t The task to execute
     * @param time The current time
     * @param dev The developer which indicated that work started
     * @throws IllegalStateException If the task can't start execution
     */
    public void startExecutingTask(Task t, LocalDateTime time, Developer dev){
        if (isTier2AvailableFor(time, dev, t)) {
            t.execute();
            // TODO Indien unplanning execution tell the system they are in use
            // this however isn't in the scope of part 2, so we'll leave it for a later date
        }
        else {
            throw new IllegalStateException(ERROR_ILLEGAL_EXECUTING_STATE);
        }
    }

    
    private static String ERROR_ILLEGAL_TASK_PLANNING = "Illegal TaskPlanning in Planning manager.";
    private static final String ERROR_ILLEGAL_EXECUTING_STATE = "Can't execute a task that isn't available.";
    private static final String ERROR_TASK_ALREADY_PLANNED = "The given Task already has a planning.";
    
}
