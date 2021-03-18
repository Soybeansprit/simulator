package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class CounterRule {
	public Rule rule=new Rule();
	public String deviceState="";
	public List<Scene> occurScenes=new ArrayList<Scene>();
	public Rule getRule() {
		return rule;
	}
	public void setRule(Rule rule) {
		this.rule = rule;
	}
	public String getDeviceState() {
		return deviceState;
	}
	public void setDeviceState(String deviceState) {
		this.deviceState = deviceState;
	}
	public List<Scene> getOccurScenes() {
		return occurScenes;
	}
	public void setOccurScenes(List<Scene> occurScenes) {
		this.occurScenes = occurScenes;
	}
}
