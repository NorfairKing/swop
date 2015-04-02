package be.kuleuven.cs.swop.facade;

import java.util.Set;

public class ResourceTypeData {
	
	private String name;
	private Set<ResourceTypeWrapper> requires;
	private Set<ResourceTypeWrapper> conflicts;
	public ResourceTypeData(String name, Set<ResourceTypeWrapper> requires, Set<ResourceTypeWrapper> conflicts ){
		this.setName(name);
		this.setRequires(requires);
		this.setConflicts(conflicts);
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

}
