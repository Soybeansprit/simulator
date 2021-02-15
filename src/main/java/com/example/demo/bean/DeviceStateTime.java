package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;



public class DeviceStateTime {
	public String name=null;
	public List<StateLastTime> statesTime=new ArrayList<StateLastTime>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<StateLastTime> getStatesTime() {
		return statesTime;
	}
	public void setStatesTime(List<StateLastTime> statesTime) {
		this.statesTime = statesTime;
	}
}
