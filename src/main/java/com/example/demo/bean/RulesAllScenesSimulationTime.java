package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class RulesAllScenesSimulationTime {

	public List<Rule> rules=new ArrayList<Rule>();
	public List<Scene> scenes=new ArrayList<Scene>();
	public String simulationTime="";
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	public List<Scene> getScenes() {
		return scenes;
	}
	public void setScenes(List<Scene> scenes) {
		this.scenes = scenes;
	}
	public String getSimulationTime() {
		return simulationTime;
	}
	public void setSimulationTime(String simulationTime) {
		this.simulationTime = simulationTime;
	}
}
