package be.kuleuven.cs.swop.domain.planning;


import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.resource.Resource;
import be.kuleuven.cs.swop.domain.task.Task;
import be.kuleuven.cs.swop.domain.user.Developer;


public class TaskPlanning implements Serializable {

    private Set<Developer> developers   = new HashSet<Developer>();
    private Task           task;
    private TimePeriod     period;
    private Set<Resource>  reservations = new HashSet<Resource>();

    public TaskPlanning(Set<Developer> developers, Task task, TimePeriod period, Set<Resource> reservations) {
        setDevelopers(developers);
        setTask(task);
        setPeriod(period);
        setReservations(reservations);
    }

    public ImmutableSet<Developer> getDevelopers() {
        return ImmutableSet.copyOf(developers);
    }

    private void setDevelopers(Set<Developer> developers) {
        if (!canHaveAsDevelopers(developers)) {
            throw new InvalidParameterException("Invalid developers set for planning.");
        }
        this.developers.clear();
        this.developers.addAll(developers);
    }

    protected boolean canHaveAsDevelopers(Set<Developer> developers) {
        return !developers.isEmpty();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    protected boolean canHaveAsTask(Task task) {
        return task != null;
    }

    public TimePeriod getPeriod() {
        return period;
    }

    public void setPeriod(TimePeriod period) {
        this.period = period;
    }

    protected boolean canHaveAsPeriod(TimePeriod period) {
        return period != null;
    }

    public ImmutableSet<Resource> getReservations() {
        return ImmutableSet.copyOf(reservations);
    }

    private void setReservations(Set<Resource> reservations) {
        if (!canHaveAsReservations(reservations)) {
            throw new InvalidParameterException("Invalid reservations set for planning.");
        }
        this.reservations.clear();
        this.reservations.addAll(reservations);
    }

    protected boolean canHaveAsReservations(Set<Resource> reservations) {
        return reservations != null;
    }

}
