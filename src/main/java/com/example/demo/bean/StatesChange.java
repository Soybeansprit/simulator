package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class StatesChange {
	public int statesChangeCount=0;
	public double statesChangeFrequence=0.0;
	public List<StateChangeFast> stateChangeFasts=new ArrayList<StateChangeFast>();
	
	
	public int getStatesChangeCount() {
		return statesChangeCount;
	}
	public void setStatesChangeCount(int statesChangeCount) {
		this.statesChangeCount = statesChangeCount;
	}
	public double getStatesChangeFrequence() {
		return statesChangeFrequence;
	}
	public void setStatesChangeFrequence(double statesChangeFrequence) {
		this.statesChangeFrequence = statesChangeFrequence;
	}
	public List<StateChangeFast> getStateChangeFasts() {
		return stateChangeFasts;
	}
	public void setStateChangeFasts(List<StateChangeFast> stateChangeFasts) {
		this.stateChangeFasts = stateChangeFasts;
	}
	

}
