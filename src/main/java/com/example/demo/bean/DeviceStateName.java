package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;



public class DeviceStateName {
	public String deviceName=null;
	public List<StateNameRelativeRule> stateNames=new ArrayList<StateNameRelativeRule>();
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public List<StateNameRelativeRule> getStateNames() {
		return stateNames;
	}
	public void setStateNames(List<StateNameRelativeRule> stateNames) {
		this.stateNames = stateNames;
	}
}
