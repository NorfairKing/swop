package be.kuleuven.cs.swop.domain.planning;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.resource.Resource;
import be.kuleuven.cs.swop.domain.task.Task;
import be.kuleuven.cs.swop.domain.user.Developer;


public class TaskPlanning {
    private Set<Developer> developers = new HashSet<Developer>();
    private Task task;
    private TimePeriod period;
    private Set<Resource> reservations = new HashSet<Resource>();

    public TaskPlanning(Set<Developer> developers, Task task, TimePeriod period, Set<Resource> reservations){
        setDevelopers(developers);
        setTask(task);
        setPeriod(period);
        setReservations(reservations);
    }

    public Set<Developer> getDevelopers() {
        return developers;
    }

    public void setDevelopers(Set<Developer> developers) {
        this.developers = developers;
    }
    protected boolean canHaveAsDevelopers(Set<Developer> developers){
        return !developers.isEmpty();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
    protected boolean canHaveAsTask(Task task){
        return task != null;
    }

    public TimePeriod getPeriod() {
        return period;
    }

    public void setPeriod(TimePeriod period) {
        this.period = period;
    }
    protected boolean canHaveAsPeriod(TimePeriod period){
        return period != null;
    }

    public Set<Resource> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Resource> reservations) {
        this.reservations = reservations;
    }
    protected boolean canHaveAsReservations(Set<Resource> reservations){
        return !reservations.isEmpty();
    }    

}
