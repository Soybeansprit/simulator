package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class RuleSpecificError {
	public List<Rule> incorrectRules=new ArrayList<Rule>();
	public List<Rule> unusedRules=new ArrayList<Rule>();
	public List<List<Rule>> redundantRules=new ArrayList<List<Rule>>();
	public List<String> ruleIncompeleteness=new ArrayList<String>();
	public List<Rule> getIncorrectRules() {
		return incorrectRules;
	}
	public void setIncorrectRules(List<Rule> incorrectRules) {
		this.incorrectRules = incorrectRules;
	}
	public List<Rule> getUnusedRules() {
		return unusedRules;
	}
	public void setUnusedRules(List<Rule> unusedRules) {
		this.unusedRules = unusedRules;
	}
	public List<List<Rule>> getRedundantRules() {
		return redundantRules;
	}
	public void setRedundantRules(List<List<Rule>> redundantRules) {
		this.redundantRules = redundantRules;
	}
	public List<String> getRuleIncompeleteness() {
		return ruleIncompeleteness;
	}
	public void setRuleIncompeleteness(List<String> ruleIncompeleteness) {
		this.ruleIncompeleteness = ruleIncompeleteness;
	}
	
}
