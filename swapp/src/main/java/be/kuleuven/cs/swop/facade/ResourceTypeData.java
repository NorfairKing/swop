package be.kuleuven.cs.swop.facade;

import java.util.Set;

public class ResourceTypeData {
	
	private String name;
	private Set<ResourceTypeWrapper> requires;
	private Set<ResourceTypeWrapper> conflicts;
	private boolean selfConflicting;
	public ResourceTypeData(String name, Set<ResourceTypeWrapper> requires, Set<ResourceTypeWrapper> conflicts, boolean selfConflicting ){
		this.setName(name);
		this.setRequires(requires);
		this.setConflicts(conflicts);
		this.setSelfConflicting(selfConflicting);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<ResourceTypeWrapper> getRequires() {
		return requires;
	}
	public void setRequires(Set<ResourceTypeWrapper> requires) {
		this.requires = requires;
	}
	public Set<ResourceTypeWrapper> getConflicts() {
		return conflicts;
	}
	public void setConflicts(Set<ResourceTypeWrapper> conflicts) {
		this.conflicts = conflicts;
	}
	public boolean isSelfConflicting() {
		return selfConflicting;
	}
	public void setSelfConflicting(boolean selfConflicting) {
		this.selfConflicting = selfConflicting;
	}

}
