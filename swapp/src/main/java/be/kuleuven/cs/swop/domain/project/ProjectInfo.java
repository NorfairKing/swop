package be.kuleuven.cs.swop.domain.project;

import java.util.Date;

public class ProjectInfo {
	
    private String title;
    private String description;
    private Date creationTime;
    private Date dueTime;
    
	private String getTitle() {
		return title;
	}
	private void setTitle(String title) {
		this.title = title;
	}
	
	private String getDescription() {
		return description;
	}
	private void setDescription(String description) {
		this.description = description;
	}
	
	private Date getCreationTime() {
		return creationTime;
	}
	private void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	
	private Date getDueTime() {
		return dueTime;
	}
	private void setDueTime(Date dueTime) {
		this.dueTime = dueTime;
	}

}
