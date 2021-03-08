package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class RulesSceneSimulationTime {

	public List<Rule> rules=new ArrayList<Rule>();
	public Scene scene=new Scene();
	public String simulationTime="";
	
	
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	public Scene getScene() {
		return scene;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	public String getSimulationTime() {
		return simulationTime;
	}
	public void setSimulationTime(String simulationTime) {
		this.simulationTime = simulationTime;
	}
	
}
