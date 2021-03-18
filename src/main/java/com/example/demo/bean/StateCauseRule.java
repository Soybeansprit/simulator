package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class StateCauseRule {

	public String stateValue="";
	public String stateName="";
	public List<RuleAndCause> causeRules=new ArrayList<RuleAndCause>();
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getStateValue() {
		return stateValue;
	}
	public void setStateValue(String stateValue) {
		this.stateValue = stateValue;
	}
	public List<RuleAndCause> getCauseRules() {
		return causeRules;
	}
	public void setCauseRules(List<RuleAndCause> causeRules) {
		this.causeRules = causeRules;
	}

}
