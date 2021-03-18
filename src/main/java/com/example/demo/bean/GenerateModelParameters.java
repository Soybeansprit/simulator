package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class GenerateModelParameters {
	
	
//	public List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
	public List<String> attributes=new ArrayList<String>();
	public ScenesTree scenesTree=new ScenesTree(); 
	public List<Rule> rules=new ArrayList<Rule>();
	public List<Action> actions=new ArrayList<Action>();
	public int simulationDataNum=0;

//	public List<TemplGraph> getControlledDevices() {
//		return controlledDevices;
//	}
//	public void setControlledDevices(List<TemplGraph> controlledDevices) {
//		this.controlledDevices = controlledDevices;
//	}
	public List<String> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	public ScenesTree getScenesTree() {
		return scenesTree;
	}
	public void setScenesTree(ScenesTree scenesTree) {
		this.scenesTree = scenesTree;
	}
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	public int getSimulationDataNum() {
		return simulationDataNum;
	}
	public void setSimulationDataNum(int simulationDataNum) {
		this.simulationDataNum = simulationDataNum;
	}
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
}
