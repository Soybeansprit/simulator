package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class CanCauseStateConflict {

	public boolean canCauseStateConflict=false;
	public List<DeviceStateCounterRules> devicesStateCounterRules=new ArrayList<DeviceStateCounterRules>();
	public boolean isCanCauseStateConflict() {
		return canCauseStateConflict;
	}
	public void setCanCauseStateConflict(boolean canCauseStateConflict) {
		this.canCauseStateConflict = canCauseStateConflict;
	}
	public List<DeviceStateCounterRules> getDevicesStateCounterRules() {
		return devicesStateCounterRules;
	}
	public void setDevicesStateCounterRules(List<DeviceStateCounterRules> devicesStateCounterRules) {
		this.devicesStateCounterRules = devicesStateCounterRules;
	}
	
}
