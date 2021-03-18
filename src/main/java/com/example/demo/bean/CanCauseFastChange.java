package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class CanCauseFastChange {
	public boolean canCause=false;
	public List<DeviceStateCounterRules> devicesStateCounterRules=new ArrayList<DeviceStateCounterRules>();
	public boolean isCanCause() {
		return canCause;
	}
	public void setCanCause(boolean canCause) {
		this.canCause = canCause;
	}
	public List<DeviceStateCounterRules> getDevicesStateCounterRules() {
		return devicesStateCounterRules;
	}
	public void setDevicesStateCounterRules(List<DeviceStateCounterRules> devicesStateCounterRules) {
		this.devicesStateCounterRules = devicesStateCounterRules;
	}
}
