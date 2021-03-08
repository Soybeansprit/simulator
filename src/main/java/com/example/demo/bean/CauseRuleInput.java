package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class CauseRuleInput {
	public ConflictTime conflictStateTime=new ConflictTime();
	public List<DataTimeValue> triggeredRulesName=new ArrayList<DataTimeValue>();
	public DeviceStateName deviceStateName=new DeviceStateName();
	public List<Rule> rules=new ArrayList<Rule>();
	
	public ConflictTime getConflictStateTime() {
		return conflictStateTime;
	}
	public void setConflictStateTime(ConflictTime conflictStateTime) {
		this.conflictStateTime = conflictStateTime;
	}
	public List<DataTimeValue> getTriggeredRulesName() {
		return triggeredRulesName;
	}
	public void setTriggeredRulesName(List<DataTimeValue> triggeredRulesName) {
		this.triggeredRulesName = triggeredRulesName;
	}
	public DeviceStateName getDeviceStateName() {
		return deviceStateName;
	}
	public void setDeviceStateName(DeviceStateName deviceStateName) {
		this.deviceStateName = deviceStateName;
	}
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
}
