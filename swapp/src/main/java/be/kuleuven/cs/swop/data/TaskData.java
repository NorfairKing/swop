package be.kuleuven.cs.swop.data;


public class TaskData {

    private String description;
    private double estimatedDuration;
    private double acceptableDeviation;

    public TaskData(String description, double estimatedDuration, double acceptableDeviation) {
        setDescription(description);
        setEstimatedDuration(estimatedDuration);
        setAcceptableDeviation(acceptableDeviation);
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public double getEstimatedDuration() {
        return estimatedDuration;
    }

    private void setEstimatedDuration(double estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public double getAcceptableDeviation() {
        return acceptableDeviation;
    }

    private void setAcceptableDeviation(double acceptableDeviation) {
        this.acceptableDeviation = acceptableDeviation;
    }

}
