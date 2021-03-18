package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class DeviceStateCounterRules {

	public String deviceName="";
	public String deviceState="";
	public List<CounterRule> counterRules=new ArrayList<CounterRule>();
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceState() {
		return deviceState;
	}
	public void setDeviceState(String deviceState) {
		this.deviceState = deviceState;
	}
	public List<CounterRule> getCounterRules() {
		return counterRules;
	}
	public void setCounterRules(List<CounterRule> counterRules) {
		this.counterRules = counterRules;
	}
}
