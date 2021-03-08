package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class StateChangeCauseRuleInput {

	public List<DataTimeValue> triggeredRulesName=new ArrayList<DataTimeValue>();
	public DeviceStateName deviceStateName=new DeviceStateName();
	public List<StateChangeFast> stateChangeFasts=new ArrayList<StateChangeFast>();
	public StateChangeFast stateChangeFast=new StateChangeFast();
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
	public List<StateChangeFast> getStateChangeFasts() {
		return stateChangeFasts;
	}
	public void setStateChangeFasts(List<StateChangeFast> stateChangeFasts) {
		this.stateChangeFasts = stateChangeFasts;
	}
	public StateChangeFast getStateChangeFast() {
		return stateChangeFast;
	}
	public void setStateChangeFast(StateChangeFast stateChangeFast) {
		this.stateChangeFast = stateChangeFast;
	}
}
