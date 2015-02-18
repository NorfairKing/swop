package be.kuleuven.cs.swop;

public class Duration {
	private int minutes;

	public Duration(int minutes){
		setMinutes(minutes);
	}

	private void setMinutes(int minutes){
		this.minutes = minutes;
	}
	public int getMinutes(){
		return minutes;
	}

	public static boolean canHaveAsMinutes(int input){
		return (input > 0);
	}
}
