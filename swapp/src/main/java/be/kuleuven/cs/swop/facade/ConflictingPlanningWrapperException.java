package be.kuleuven.cs.swop.facade;


@SuppressWarnings("serial")
public class ConflictingPlanningWrapperException extends Exception{
	
	private TaskPlanningWrapper planning;
	ConflictingPlanningWrapperException(TaskPlanningWrapper planning){
		this.setPlanning(planning);
	}
	public TaskPlanningWrapper getPlanning() {
		return planning;
	}
	public void setPlanning(TaskPlanningWrapper planning) {
		this.planning = planning;
	}
}