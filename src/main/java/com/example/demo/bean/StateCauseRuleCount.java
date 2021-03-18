package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class StateCauseRuleCount {
	public String stateName="";
	public List<RuleCount> rulesCount=new ArrayList<RuleCount>();
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public List<RuleCount> getRulesCount() {
		return rulesCount;
	}
	public void setRulesCount(List<RuleCount> rulesCount) {
		this.rulesCount = rulesCount;
	}
}
