package be.kuleuven.cs.swop.facade;


public abstract class OngoingStatusData extends IncompleteStatusData {

    public OngoingStatusData() {
    }

	@Override
	public boolean isExecuting() {
		return false;
	}
}
