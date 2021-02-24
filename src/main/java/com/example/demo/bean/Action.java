package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class Action {
	public String action=null;
	public String device=null;
	public String toState=null;
	public List<String[]> attrVal=new ArrayList<String[]>();
	public List<Rule> rules=new ArrayList<Rule>();
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getToState() {
		return toState;
	}
	public void setToState(String toState) {
		this.toState = toState;
	}
	public List<String[]> getAttrVal() {
		return attrVal;
	}
	public void setAttrVal(List<String[]> attrVal) {
		this.attrVal = attrVal;
	}
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
}
