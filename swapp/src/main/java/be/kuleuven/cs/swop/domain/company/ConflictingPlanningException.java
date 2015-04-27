package be.kuleuven.cs.swop.domain.company;


import be.kuleuven.cs.swop.domain.company.planning.TaskPlanning;


@SuppressWarnings("serial")
public class ConflictingPlanningException extends Exception {

    private TaskPlanning planning;

    ConflictingPlanningException(TaskPlanning planning) {
        this.setPlanning(planning);
    }

    public TaskPlanning getPlanning() {
        return planning;
    }

    public void setPlanning(TaskPlanning planning) {
        this.planning = planning;
    }
}
