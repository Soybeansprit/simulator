package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class DeviceAllSceneFastChangeRule {
	public String deviceName="";
	public List<StateCauseRuleCountSceneName> allFastChangeStateCauseRuleCountSceneName=new ArrayList<StateCauseRuleCountSceneName>();
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public List<StateCauseRuleCountSceneName> getAllFastChangeStateCauseRuleCountSceneName() {
		return allFastChangeStateCauseRuleCountSceneName;
	}
	public void setAllFastChangeStateCauseRuleCountSceneName(
			List<StateCauseRuleCountSceneName> allFastChangeStateCauseRuleCountSceneName) {
		this.allFastChangeStateCauseRuleCountSceneName = allFastChangeStateCauseRuleCountSceneName;
	}

}
