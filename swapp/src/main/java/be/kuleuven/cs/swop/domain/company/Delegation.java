package be.kuleuven.cs.swop.domain.company;

import be.kuleuven.cs.swop.domain.company.task.Task;


public class Delegation {
    public Delegation(Task delegatedTask, BranchOffice oldOffice, BranchOffice newOffice) {
        super();
        this.delegatedTask = delegatedTask;
        this.oldOffice = oldOffice;
        this.newOffice = newOffice;
    }

    private Task delegatedTask;
    private BranchOffice oldOffice;
    private BranchOffice newOffice;
    
    /**
     * @return the delegatedTask
     */
    public Task getDelegatedTask() {
        return delegatedTask;
    }
    
    /**
     * @return the oldOffice
     */
    public BranchOffice getOldOffice() {
        return oldOffice;
    }
    
    /**
     * @return the newOffice
     */
    public BranchOffice getNewOffice() {
        return newOffice;
    }

}
