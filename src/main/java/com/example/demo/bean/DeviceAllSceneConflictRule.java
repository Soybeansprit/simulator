package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class DeviceAllSceneConflictRule {
	public String deviceName="";
	public List<CountStatesCauseRuleSceneName> allCountStateCauseRuleSceneName=new ArrayList<CountStatesCauseRuleSceneName>();
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public List<CountStatesCauseRuleSceneName> getAllCountStateCauseRuleSceneName() {
		return allCountStateCauseRuleSceneName;
	}
	public void setAllCountStateCauseRuleSceneName(List<CountStatesCauseRuleSceneName> allCountStateCauseRuleSceneName) {
		this.allCountStateCauseRuleSceneName = allCountStateCauseRuleSceneName;
	}

}
