package be.kuleuven.cs.swop.facade;

import be.kuleuven.cs.swop.domain.company.resource.ResourceType;

public class ResourceData {
	private String name;
	private ResourceType type;
	public ResourceData(String name, ResourceType type){
		this.setName(name);
		this.setType(type);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ResourceType getType() {
		return type;
	}
	public void setType(ResourceType type) {
		this.type = type;
	}

}
