package be.kuleuven.cs.swop.facade;

import be.kuleuven.cs.swop.domain.company.resource.Requirement;

public class RequirementWrapper {
	private Requirement requirement;
	
	public RequirementWrapper(Requirement req){
		this.setRequirement(req);
	}

	Requirement getRequirement() {
		return requirement;
	}

	private void setRequirement(Requirement req) {
		this.requirement = req;
	}
	
	public int getAmount(){
		return requirement.getAmount();
	}
	
	public ResourceTypeWrapper getType(){
		return new ResourceTypeWrapper(requirement.getType());
	}
	

}
