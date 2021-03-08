package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class RuleAndCause {

	public Rule selfRule=new Rule();
	public List<RuleAndCause> causeRules=new ArrayList<RuleAndCause>();
	
	public Rule getSelfRule() {
		return selfRule;
	}
	public void setSelfRule(Rule selfRule) {
		this.selfRule = selfRule;
	}
	public List<RuleAndCause> getCauseRules() {
		return causeRules;
	}
	public void setCauseRules(List<RuleAndCause> causeRules) {
		this.causeRules = causeRules;
	}
}
