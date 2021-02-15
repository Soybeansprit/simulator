package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;





public class DeviceConflict {
	public String name=null;
	public boolean hasConflict=false;
	public List<ConflictTime> conflictTimes=new ArrayList<ConflictTime>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isHasConflict() {
		return hasConflict;
	}
	public void setHasConflict(boolean hasConflict) {
		this.hasConflict = hasConflict;
	}
	public List<ConflictTime> getConflictTimes() {
		return conflictTimes;
	}
	public void setConflictTimes(List<ConflictTime> conflictTimes) {
		this.conflictTimes = conflictTimes;
	}
}
