package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;



public class StateLastTime {
	public String state=null;
	public String stateName=null;
	public double lastTime=0;
	public List<Rule> relativeRules=new ArrayList<Rule>();
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public double getLastTime() {
		return lastTime;
	}
	public void setLastTime(double lastTime) {
		this.lastTime = lastTime;
	}
	public List<Rule> getRelativeRules() {
		return relativeRules;
	}
	public void setRelativeRules(List<Rule> relativeRules) {
		this.relativeRules = relativeRules;
	}
}
