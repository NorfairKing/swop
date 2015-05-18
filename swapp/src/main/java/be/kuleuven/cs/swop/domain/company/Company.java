package be.kuleuven.cs.swop.domain.company;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.swop.domain.company.project.Project;
import be.kuleuven.cs.swop.domain.company.resource.ResourceType;
import be.kuleuven.cs.swop.domain.company.task.Task;
import be.kuleuven.cs.swop.domain.company.user.Developer;

import com.google.common.collect.ImmutableSet;


public class Company {

    private LocalDateTime time = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
    private final Set<BranchOffice> offices = new HashSet<BranchOffice>();
    private final DelegationOffice delegationOffice = new DelegationOffice();
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

    private DelegationOffice getDelegationOffice() {
        return delegationOffice;
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
        this.time = time;
    }

    /**
     * Returns the current system time
     *
     * @return The current system time
     */
    public LocalDateTime getSystemTime() {
        return time;
    }
    
    public void delegate(Task oldTask, BranchOffice newOffice){
        
    }
    
}
