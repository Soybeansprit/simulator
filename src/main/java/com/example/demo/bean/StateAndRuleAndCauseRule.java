package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class StateAndRuleAndCauseRule {

	public String stateValue="";
	public String stateName="";
	public List<RuleAndCause> rulesAndCauseRules=new ArrayList<RuleAndCause>();
	
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
	public List<RuleAndCause> getRulesAndCauseRules() {
		return rulesAndCauseRules;
	}
	public void setRulesAndCauseRules(List<RuleAndCause> rulesAndCauseRules) {
		this.rulesAndCauseRules = rulesAndCauseRules;
	}
}
