package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class StateCauseRulesAndRelativeRules {

	public String stateValue=null;
	public String stateName=null;
	public List<RuleAndRelativeRules> causeRules=new ArrayList<RuleAndRelativeRules>();
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
	public List<RuleAndRelativeRules> getCauseRules() {
		return causeRules;
	}
	public void setCauseRules(List<RuleAndRelativeRules> causeRules) {
		this.causeRules = causeRules;
	}
}
