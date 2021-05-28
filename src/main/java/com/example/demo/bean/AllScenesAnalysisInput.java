package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class AllScenesAnalysisInput {

	public List<Scene> scenes=new ArrayList<Scene>();
	public List<Rule> rules=new ArrayList<Rule>();
	public List<String> properties=new ArrayList<String>(); 
	
	public List<Scene> getScenes() {
		return scenes;
	}
	public void setScenes(List<Scene> scenes) {
		this.scenes = scenes;
	}
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	public List<String> getProperties() {
		return properties;
	}
	public void setProperties(List<String> properties) {
		this.properties = properties;
	}
}
