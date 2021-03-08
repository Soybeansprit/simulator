package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class TimeStateRelativeRules {
	public double time=0.0;
	public String stateName="";
	public List<Rule> relativeRules=new ArrayList<Rule>();
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public List<Rule> getRelativeRules() {
		return relativeRules;
	}
	public void setRelativeRules(List<Rule> relativeRules) {
		this.relativeRules = relativeRules;
	}

}
