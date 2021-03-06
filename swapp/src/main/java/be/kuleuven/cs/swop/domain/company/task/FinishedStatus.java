package be.kuleuven.cs.swop.domain.company.task;


import java.time.LocalDateTime;


@SuppressWarnings("serial")
public class FinishedStatus extends CompletedStatus {

    @SuppressWarnings("unused")
    private FinishedStatus() {super();} //for automatic (de)-serialization
    FinishedStatus(Task task) {
        super(task);
    }

    @Override
    boolean isFinished() {
        return true;
    }

    @Override
    boolean isFailed() {
        return false;
    }

    @Override
    void setAlternative(Task alternative) {
        throw new IllegalStateException(ERROR_SET_ALTERNATIVE);
    }

    @Override
    boolean isFinishedOrHasFinishedAlternative() {
        return true;
    }

    /**
     * Checks whether or not this Task was finished within the acceptable deviation.
     *
     * @return Returns true if this Task has finished within the acceptable deviation. Otherwise it returns false, also when it hasn't finished yet.
     */
    boolean wasFinishedOnTime() {
        double realDuration = getTask().getPlanning().getTaskDuration();
        return realDuration >= getTask().getBestDuration() && realDuration <= getTask().getWorstDuration();
    }

    /**
     * Checks whether or not this Task was finished after the estimated duration plus the acceptable deviation.
     *
     * @return Returns true if the Task has finished before after the estimated duration plus the acceptable deviation. Otherwise it returns false, also when it hasn't finished yet.
     */
    boolean wasFinishedLate() {
        return getTask().getPlanning().getTaskDuration() > getTask().getWorstDuration();
    }

    @Override
    LocalDateTime getEstimatedOrRealFinishDate(LocalDateTime currentDate) {
        return getTask().getPlanning().getPlannedEndTime();
    }

    @Override
    Task getAlternative() {
        return null;
    }

    @Override
    boolean wasFinishedEarly() {
        return getTask().getPlanning().getTaskDuration() < getTask().getBestDuration();
    }

    private static String ERROR_SET_ALTERNATIVE = "Can't set an alternative for a finished task.";

}
