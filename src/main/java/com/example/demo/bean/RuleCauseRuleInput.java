package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class RuleCauseRuleInput {

	public List<Rule> causeRules=new ArrayList<Rule>();
	public List<Rule> rules=new ArrayList<Rule>();
	public List<Rule> getCauseRules() {
		return causeRules;
	}
	public void setCauseRules(List<Rule> causeRules) {
		this.causeRules = causeRules;
	}
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	
}
