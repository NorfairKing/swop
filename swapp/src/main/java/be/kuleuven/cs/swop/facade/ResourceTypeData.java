package be.kuleuven.cs.swop.facade;

import java.time.LocalTime;
import java.util.Set;

import be.kuleuven.cs.swop.domain.company.resource.ResourceType;

public class ResourceTypeData {
	
	private String name;
	private Set<ResourceType> requires;
	private Set<ResourceType> conflicts;
	private boolean selfConflicting;
	private  LocalTime[] availibility;
	public ResourceTypeData(String name, Set<ResourceType> requires, Set<ResourceType> conflicts, boolean selfConflicting, LocalTime[] availibility ){
		this.setName(name);
		this.setRequires(requires);
		this.setConflicts(conflicts);
		this.setSelfConflicting(selfConflicting);
		this.setAvailibility(availibility);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<ResourceType> getRequires() {
		return requires;
	}
	public void setRequires(Set<ResourceType> requires) {
		this.requires = requires;
	}
	public Set<ResourceType> getConflicts() {
		return conflicts;
	}
	public void setConflicts(Set<ResourceType> conflicts) {
		this.conflicts = conflicts;
	}
	public boolean isSelfConflicting() {
		return selfConflicting;
	}
	public void setSelfConflicting(boolean selfConflicting) {
		this.selfConflicting = selfConflicting;
	}
	public LocalTime[] getAvailibility() {
		return availibility;
	}
	public void setAvailibility(LocalTime[] availibility) {
		this.availibility = availibility;
	}

}
