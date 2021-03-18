package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class AllRuleAnalysisResult {
	public List<Scene> scenes=new ArrayList<Scene>();
	public List<RuleAndCause> rulesNeverTriggered=new ArrayList<RuleAndCause>();
	public List<DeviceAllSceneConflictRule> devicesAllSceneConflictRule=new ArrayList<DeviceAllSceneConflictRule>();
	public List<DeviceAllSceneFastChangeRule> devicesAllSceneFastChangeRule=new ArrayList<DeviceAllSceneFastChangeRule>();
	public List<DeviceSceneConflictCauseRule> devicesSceneConflictCauseRule=new ArrayList<DeviceSceneConflictCauseRule>();
	public List<DeviceSceneFastChangeCauseRule> devicesSceneFastChangeCauseRule=new ArrayList<DeviceSceneFastChangeCauseRule>();

	public List<DeviceAllSceneConflictRule> getDevicesAllSceneConflictRule() {
		return devicesAllSceneConflictRule;
	}
	public void setDevicesAllSceneConflictRule(List<DeviceAllSceneConflictRule> devicesAllSceneConflictRule) {
		this.devicesAllSceneConflictRule = devicesAllSceneConflictRule;
	}
	public List<DeviceAllSceneFastChangeRule> getDevicesAllSceneFastChangeRule() {
		return devicesAllSceneFastChangeRule;
	}
	public void setDevicesAllSceneFastChangeRule(List<DeviceAllSceneFastChangeRule> devicesAllSceneFastChangeRule) {
		this.devicesAllSceneFastChangeRule = devicesAllSceneFastChangeRule;
	}
	public List<DeviceSceneConflictCauseRule> getDevicesSceneConflictCauseRule() {
		return devicesSceneConflictCauseRule;
	}
	public void setDevicesSceneConflictCauseRule(List<DeviceSceneConflictCauseRule> devicesSceneConflictCauseRule) {
		this.devicesSceneConflictCauseRule = devicesSceneConflictCauseRule;
	}
	public List<DeviceSceneFastChangeCauseRule> getDevicesSceneFastChangeCauseRule() {
		return devicesSceneFastChangeCauseRule;
	}
	public void setDevicesSceneFastChangeCauseRule(List<DeviceSceneFastChangeCauseRule> devicesSceneFastChangeCauseRule) {
		this.devicesSceneFastChangeCauseRule = devicesSceneFastChangeCauseRule;
	}
	public List<Scene> getScenes() {
		return scenes;
	}
	public void setScenes(List<Scene> scenes) {
		this.scenes = scenes;
	}
	public List<RuleAndCause> getRulesNeverTriggered() {
		return rulesNeverTriggered;
	}
	public void setRulesNeverTriggered(List<RuleAndCause> rulesNeverTriggered) {
		this.rulesNeverTriggered = rulesNeverTriggered;
	}
}
