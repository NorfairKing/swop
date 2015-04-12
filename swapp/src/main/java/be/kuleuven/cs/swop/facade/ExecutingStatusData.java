package be.kuleuven.cs.swop.facade;

public class ExecutingStatusData extends IncompleteStatusData {

	@Override
	public boolean isExecuting() {
		return true;
	}

}
