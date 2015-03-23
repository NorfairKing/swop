package be.kuleuven.cs.swop.domain.resource;

import be.kuleuven.cs.swop.domain.TimePeriod;
import be.kuleuven.cs.swop.domain.task.Task;


public class Reservation {
    private Resource resource;
    private TimePeriod reservedDuring;
    private Task owner;


    public Reservation(Resource resource, TimePeriod reservedDuring,Task owner){
        this.setResource(resource);
        this.setReservedDuring(reservedDuring);
        this.setOwner(owner);
    }
    protected boolean canHaveAsResource(Resource resource){
        return resource != null;
    }


    protected boolean canHaveAsTimePerioid(TimePeriod period){
        return period != null;
    }


    protected boolean canHaveAsOwner(Task owner){
        return owner != null;
    }
    public Resource getResource() {
        return resource;
    }
    private void setResource(Resource resource) {
        this.resource = resource;
    }
    public TimePeriod getReservedDuring() {
        return reservedDuring;
    }
    private void setReservedDuring(TimePeriod reservedDuring) {
        this.reservedDuring = reservedDuring;
    }
    public Task getOwner() {
        return owner;
    }
    private void setOwner(Task owner) {
        this.owner = owner;
    }
}
