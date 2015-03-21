package be.kuleuven.cs.swop.data;


import java.util.Date;


public class ProjectData {

    private String title;
    private String description;
    private Date   dueTime;

    public ProjectData(String title, String description, Date dueTime) {
        this.title = title;
        this.description = description;
        this.dueTime = dueTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDueTime() {
        return dueTime;
    }

}
