package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class DeviceSceneConflictCauseRule {
	public String deviceName="";
	public List<SceneConflictStateCauseRule> scenesConflcitStateCasueRule=new ArrayList<SceneConflictStateCauseRule>();
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public List<SceneConflictStateCauseRule> getScenesConflcitStateCasueRule() {
		return scenesConflcitStateCasueRule;
	}
	public void setScenesConflcitStateCasueRule(List<SceneConflictStateCauseRule> scenesConflcitStateCasueRule) {
		this.scenesConflcitStateCasueRule = scenesConflcitStateCasueRule;
	}
}
