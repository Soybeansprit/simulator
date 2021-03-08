package com.example.demo.bean;

public class DeviceCannotOff {
	public boolean cannotOff=false;
	public CannotOffReason cannotOffReason=new CannotOffReason();
	
	public boolean isCannotOff() {
		return cannotOff;
	}
	public void setCannotOff(boolean cannotOff) {
		this.cannotOff = cannotOff;
	}
	public CannotOffReason getReason() {
		return cannotOffReason;
	}
	public void setReason(CannotOffReason reason) {
		this.cannotOffReason = reason;
	}

}
