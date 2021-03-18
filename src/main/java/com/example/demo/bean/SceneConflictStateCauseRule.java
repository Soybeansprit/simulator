package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class SceneConflictStateCauseRule {
	public String sceneName="";
	public List<CountStatesCauseRule> conflictCauseRuleStatistic=new ArrayList<CountStatesCauseRule>();
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	public List<CountStatesCauseRule> getConflictCauseRuleStatistic() {
		return conflictCauseRuleStatistic;
	}
	public void setConflictCauseRuleStatistic(List<CountStatesCauseRule> conflictCauseRuleStatistic) {
		this.conflictCauseRuleStatistic = conflictCauseRuleStatistic;
	}
}
