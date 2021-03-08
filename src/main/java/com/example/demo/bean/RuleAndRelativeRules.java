package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class RuleAndRelativeRules {

	public Rule rule=new Rule();
	public List<Rule> relativeRules=new ArrayList<Rule>();
	public Rule getRule() {
		return rule;
	}
	public void setRule(Rule rule) {
		this.rule = rule;
	}
	public List<Rule> getRelativeRules() {
		return relativeRules;
	}
	public void setRelativeRules(List<Rule> relativeRules) {
		this.relativeRules = relativeRules;
	}
}
