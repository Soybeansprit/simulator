package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class Device {
	public String name="";
	public List<String[]> stateActionValues=new ArrayList<String[]>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String[]> getStateActionValue() {
		return stateActionValues;
	}
	public void setStateActionValue(List<String[]> stateActionValues) {
		this.stateActionValues = stateActionValues;
	}
	
}
