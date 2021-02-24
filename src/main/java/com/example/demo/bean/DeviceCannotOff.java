package com.example.demo.bean;

public class DeviceCannotOff {
	public boolean cannotOff=false;
	public String reason=null;
	public boolean isCannotOff() {
		return cannotOff;
	}
	public void setCannotOff(boolean cannotOff) {
		this.cannotOff = cannotOff;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

}
