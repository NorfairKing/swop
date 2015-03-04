package be.kuleuven.cs.swop.domain;

public class TimeDeviation {
	private double value;
	public TimeDeviation(double value){
		setValue(value);
	}
	private void setValue(double value){
		this.value = value;
	}
	public double getValue(){
		return value;
	}

	public static boolean canHaveAsValue(double value){
		if(Double.isNaN(value)){
			return false;
		}
		if(Double.isInfinite(value)){
			return false;
		}
		if(value < 0){
			return false;
		}
		return true;
	}

}
