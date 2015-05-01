package be.kuleuven.cs.swop.facade;


import be.kuleuven.cs.swop.domain.company.user.Developer;
import be.kuleuven.cs.swop.domain.DateTimePeriod;


public class DeveloperWrapper {

    private Developer developer;

    public DeveloperWrapper(Developer developer) {
        setDeveloper(developer);
    }

    Developer getDeveloper() {
        return developer;
    }

    protected boolean canHaveAsDeveloper(Developer developer) {
        return developer != null;
    }

    private void setDeveloper(Developer developer) {
        if (!canHaveAsDeveloper(developer)) { throw new IllegalArgumentException(ERROR_ILLEGAL_DEVELOPER); }
        this.developer = developer;
    }
    
    public String getName() {
        return developer.getName();
    }
    
    public UserWrapper getAsUser() {
        return new UserWrapper(developer);
    }

    public boolean canTakeBreakDuring(DateTimePeriod period) {
        return this.developer.canTakeBreakDuring(period);
    }
    
    @Override
    public boolean equals(Object o){
    	if(!(o instanceof DeveloperWrapper)){
    		return false;
    	}
    	return this.getDeveloper().equals(((DeveloperWrapper) o).getDeveloper());
    	
    }
    
    @Override
    public int hashCode(){
    	return this.developer.hashCode() + "wrapper".hashCode();
    	
    }

    private static final String ERROR_ILLEGAL_DEVELOPER = "Invalid developer for developer wrapper";
}
