package be.kuleuven.cs.swop.domain.company;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.Timekeeper;
import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;

import com.google.common.collect.ImmutableSet;


public class Company {

    private final Timekeeper timeKeeper = new Timekeeper();
    private final Set<BranchOffice> offices = new HashSet<BranchOffice>();
    
    public Company() {
        
    }
    
    public ImmutableSet<BranchOffice> getOffices() {
        return ImmutableSet.copyOf(offices);
    }
    
    public ImmutableSet<Developer> getDevelopers(AuthenticationToken at) {
        return at.getOffice().getDevelopers();
    }
    
    public ImmutableSet<Project> getProjects(AuthenticationToken at) {
        return at.getOffice().getProjects();
    }
    
    public ImmutableSet<ResourceType> getResourceTypes(AuthenticationToken at) {
        return at.getOffice().getResourceTypes();
    }
    
    public Set<Task> getUnplannedTasksOf(Project project, AuthenticationToken at) {
        return at.getOffice().getUnplannedTasksOf(project);
    }
    
    /**
     * Changes the program's system time.
     *
     * @param time
     *            The Date containing the new system time.
     *
     * @throws IllegalArgumentException
     *             If the given Date is invalid.
     *
     */
    public void updateSystemTime(LocalDateTime time) throws IllegalArgumentException {
        if (time == null) { throw new IllegalArgumentException("Null date for system time update"); }
        timeKeeper.setTime(time);
    }

    /**
     * Returns the current system time
     *
     * @return The current system time
     */
    public LocalDateTime getSystemTime() {
        return timeKeeper.getTime();
    }
    
}
