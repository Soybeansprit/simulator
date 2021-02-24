package com.example.demo.bean;



public class DeviceAnalysResult {
	public String deviceName=null;
	public DeviceConflict statesConflict=new DeviceConflict();
	public StatesChange statesChange=new StatesChange();
	public DeviceStateTime deviceStateLastTime=new DeviceStateTime();
	public DeviceCannotOff deviceCannotOff=null;
	public DeviceStateName deviceStateName=new DeviceStateName();
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public DeviceConflict getStatesConflict() {
		return statesConflict;
	}
	public void setStatesConflict(DeviceConflict statesConflict) {
		this.statesConflict = statesConflict;
	}
	
	public DeviceStateTime getDeviceStateLastTime() {
		return deviceStateLastTime;
	}
	public void setDeviceStateLastTime(DeviceStateTime deviceStateLastTime) {
		this.deviceStateLastTime = deviceStateLastTime;
	}
	public DeviceCannotOff getDeviceCannotOff() {
		return deviceCannotOff;
	}
	public void setDeviceCannotOff(DeviceCannotOff deviceCannotOff) {
		this.deviceCannotOff = deviceCannotOff;
	}
	public StatesChange getStatesChange() {
		return statesChange;
	}
	public void setStatesChange(StatesChange statesChange) {
		this.statesChange = statesChange;
	}
	public DeviceStateName getDeviceStateName() {
		return deviceStateName;
	}
	public void setDeviceStateName(DeviceStateName deviceStateName) {
		this.deviceStateName = deviceStateName;
	}

}
