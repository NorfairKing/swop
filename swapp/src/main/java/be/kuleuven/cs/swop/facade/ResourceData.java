package be.kuleuven.cs.swop.facade;

public class ResourceData {
	private String name;
	private ResourceTypeWrapper type;
	public ResourceData(String name, ResourceTypeWrapper type){
		this.setName(name);
		this.setType(type);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ResourceTypeWrapper getType() {
		return type;
	}
	public void setType(ResourceTypeWrapper type) {
		this.type = type;
	}

}
