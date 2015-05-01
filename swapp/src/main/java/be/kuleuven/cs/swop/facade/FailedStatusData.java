package be.kuleuven.cs.swop.facade;

import java.time.LocalDateTime;

public class FailedStatusData extends PerformedStatusData {

	public FailedStatusData(LocalDateTime startTime, LocalDateTime endTime) {
		super(startTime, endTime);
	}

	@Override
	public boolean isSuccessful() {
		return false;
	}

}
