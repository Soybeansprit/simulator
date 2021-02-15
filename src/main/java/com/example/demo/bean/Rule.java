package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class Rule {

	private String ruleName=null;
	private String ruleContent=null;
	private String triggers=null;
	private List<String> trigger=new ArrayList<String>();
	private List<String> action=new ArrayList<String>();
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getRuleContent() {
		return ruleContent;
	}
	public void setRuleContent(String ruleContent) {
		this.ruleContent = ruleContent;
	}
	public List<String> getTrigger() {
		return trigger;
	}
	public void setTrigger(List<String> trigger) {
		this.trigger = trigger;
	}
	public List<String> getAction() {
		return action;
	}
	public void setAction(List<String> action) {
		this.action = action;
	}
	public String getTriggers() {
		return triggers;
	}
	public void setTriggers(String triggers) {
		this.triggers = triggers;
	}
}
