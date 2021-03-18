package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class RuleCountSceneName {
	public List<String> sceneNames=new ArrayList<String>();
	public RuleCount ruleCount=new RuleCount();
	public List<String> getSceneNames() {
		return sceneNames;
	}
	public void setSceneNames(List<String> sceneNames) {
		this.sceneNames = sceneNames;
	}
	public RuleCount getRuleCount() {
		return ruleCount;
	}
	public void setRuleCount(RuleCount ruleCount) {
		this.ruleCount = ruleCount;
	}
}
