package be.kuleuven.cs.swop.facade;

import java.util.HashSet;
import java.util.Set;





import be.kuleuven.cs.swop.domain.DateTimePeriod;
import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;
import be.kuleuven.cs.swop.domain.company.resource.Resource;
import be.kuleuven.cs.swop.domain.company.user.Developer;


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

    public Set<DeveloperWrapper> getDevelopers() {
    	Set<DeveloperWrapper> result = new HashSet<DeveloperWrapper>();
    	for (Developer realDev : planning.getDevelopers()) {
    		result.add(new DeveloperWrapper(realDev));
    	}
    	return result;
    }

    public Set<ResourceWrapper> getReservations() {
    	Set<ResourceWrapper> result = new HashSet<ResourceWrapper>();
    	for (Resource realRes : planning.getReservations()) {
    		result.add(new ResourceWrapper(realRes));
    	}
    	return result;    }
    
    @Override
    public boolean equals(Object o){
    	if(!(o instanceof TaskPlanningWrapper)){
    		return false;
    	}
    	return this.getPlanning().equals(((TaskPlanningWrapper) o).getPlanning());
    }
    
    @Override
    public int hashCode(){
    	return this.planning.hashCode() + "wrapper".hashCode();
    	
    }

    private static final String ERROR_ILLEGAL_PLANNING = "Illegal planning for planning wrapper.";

}
