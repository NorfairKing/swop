package be.kuleuven.cs.swop.facade;


@SuppressWarnings("serial")
public class ConflictingPlannedTaskWrapperException extends Exception{
	
    private TaskWrapper task;
	ConflictingPlannedTaskWrapperException(TaskWrapper task){
        this.setTask(task);
    }

    public TaskWrapper getTask() {
        return task;
    }

    public void setTask(TaskWrapper task) {
        this.task = task;
    }
	
	
}
