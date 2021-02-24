package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class StateNameRelativeRule {
	public String stateValue=null;
	public String stateName=null;
	public List<Rule> relativeRules=new ArrayList<Rule>();
	
	public String getStateValue() {
		return stateValue;
	}
	public void setStateValue(String stateValue) {
		this.stateValue = stateValue;
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
