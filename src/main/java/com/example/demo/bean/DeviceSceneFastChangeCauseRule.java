package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class DeviceSceneFastChangeCauseRule {
	public String deviceName="";
	public List<SceneFastChangeCauseRule> scenesFastChangeCauseRule=new ArrayList<SceneFastChangeCauseRule>();
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public List<SceneFastChangeCauseRule> getScenesFastChangeCauseRule() {
		return scenesFastChangeCauseRule;
	}
	public void setScenesFastChangeCauseRule(List<SceneFastChangeCauseRule> scenesFastChangeCauseRule) {
		this.scenesFastChangeCauseRule = scenesFastChangeCauseRule;
	}
}
