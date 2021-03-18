package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class StateCauseRuleCountSceneName {
	public String stateName="";
	public List<RuleCountSceneName> rulesCountSceneName=new ArrayList<RuleCountSceneName>();
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public List<RuleCountSceneName> getRulesCount() {
		return rulesCountSceneName;
	}
	public void setRulesCount(List<RuleCountSceneName> rulesCountSceneName) {
		this.rulesCountSceneName = rulesCountSceneName;
	}
}
