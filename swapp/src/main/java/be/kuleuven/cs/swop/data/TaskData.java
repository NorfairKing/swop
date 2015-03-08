package be.kuleuven.cs.swop.data;

public class TaskData {
	private String description;
    private double estimatedDuration;
    private double acceptableDeviation;
    
    public TaskData() {
    	
    }
    
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public double getEstimatedDuration() {
		return estimatedDuration;
	}
	public void setEstimatedDuration(double estimatedDuration) {
		this.estimatedDuration = estimatedDuration;
	}
	
	public double getAcceptableDeviation() {
		return acceptableDeviation;
	}
	public void setAcceptableDeviation(double acceptableDeviation) {
		this.acceptableDeviation = acceptableDeviation;
	}
    
    
}
