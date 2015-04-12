package be.kuleuven.cs.swop.facade;

import java.time.LocalDateTime;

public class FinishedStatusData extends PerformedStatusData {

	public FinishedStatusData(LocalDateTime startTime, LocalDateTime endTime) {
		super(startTime, endTime);
	}

	@Override
	public boolean isSuccessful() {
		return true;
	}

}
