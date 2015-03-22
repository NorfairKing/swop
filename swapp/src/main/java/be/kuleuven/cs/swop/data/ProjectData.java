package be.kuleuven.cs.swop.data;

import java.time.LocalDateTime;

public class ProjectData {

    private String title;
    private String description;
    private LocalDateTime creationTime;
    private LocalDateTime dueTime;

    public ProjectData(String title, String description, LocalDateTime dueTime) {
        this.title = title;
        this.description = description;
        this.dueTime = dueTime;
    }
    
    public ProjectData(String title, String description, LocalDateTime creationTime, LocalDateTime dueTime) {
        this.title = title;
        this.description = description;
        this.creationTime = creationTime;
        this.dueTime = dueTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    
    public LocalDateTime getCreationTime() {
        return creationTime;
    }
    
    public LocalDateTime getDueTime() {
        return dueTime;
    }

}
