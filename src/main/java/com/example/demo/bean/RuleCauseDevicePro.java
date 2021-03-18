package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class RuleCauseDevicePro {
	public Rule rule=new Rule();
	public List<DeviceStateCounterRules> devicesStateCounterRules=new ArrayList<DeviceStateCounterRules>();
	public Rule getRule() {
		return rule;
	}
	public void setRule(Rule rule) {
		this.rule = rule;
	}
	public List<DeviceStateCounterRules> getDevicesStateCounterRules() {
		return devicesStateCounterRules;
	}
	public void setDevicesStateCounterRules(List<DeviceStateCounterRules> devicesStateCounterRules) {
		this.devicesStateCounterRules = devicesStateCounterRules;
	}
}
