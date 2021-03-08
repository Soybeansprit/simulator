package com.example.demo.bean;

public class StateChangeFast {
	public double[] startTimeValue=new double[2];
	public double[] middleTimeValue=new double[2];
	public double[] endTimeValue=new double[2];
	/////////[0]是time [1]是stateValue
	public double[] getStartTimeValue() {
		return startTimeValue;
	}
	public void setStartTimeValue(double[] startTimeValue) {
		this.startTimeValue = startTimeValue;
	}
	public double[] getMiddleTimeValue() {
		return middleTimeValue;
	}
	public void setMiddleTimeValue(double[] middleTimeValue) {
		this.middleTimeValue = middleTimeValue;
	}
	public double[] getEndTimeValue() {
		return endTimeValue;
	}
	public void setEndTimeValue(double[] endTimeValue) {
		this.endTimeValue = endTimeValue;
	}
}
