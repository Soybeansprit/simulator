package com.example.demo.bean;



public class DeviceAnalysResult {
	public String deviceName=null;
	public DeviceConflict statesConflict=new DeviceConflict();
	public int stateChangeRate=0;
	public DeviceStateTime deviceStateLastTime=new DeviceStateTime();
	public boolean finallyOff=false;
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
	public int getStateChangeRate() {
		return stateChangeRate;
	}
	public void setStateChangeRate(int stateChangeRate) {
		this.stateChangeRate = stateChangeRate;
	}
	public DeviceStateTime getDeviceStateLastTime() {
		return deviceStateLastTime;
	}
	public void setDeviceStateLastTime(DeviceStateTime deviceStateLastTime) {
		this.deviceStateLastTime = deviceStateLastTime;
	}
	public boolean isFinallyOff() {
		return finallyOff;
	}
	public void setFinallyOff(boolean finallyOff) {
		this.finallyOff = finallyOff;
	}
}
