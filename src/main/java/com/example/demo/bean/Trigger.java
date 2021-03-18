package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class Trigger {
	public String trigger=null;
	public String triggerNum=null;
	public String[] attrVal=new String[3];
	public List<Rule> rules=new ArrayList<Rule>();
	public String getTrigger() {
		return trigger;
	}
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
	public String getTriggerNum() {
		return triggerNum;
	}
	public void setTriggerNum(String triggerNum) {
		this.triggerNum = triggerNum;
	}
	public String[] getAttrVal() {
		return attrVal;
	}
	public void setAttrVal(String[] attrVal) {
		this.attrVal = attrVal;
	}
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}	
}
