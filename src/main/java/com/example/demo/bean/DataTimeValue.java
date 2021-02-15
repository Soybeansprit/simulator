package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class DataTimeValue {
	public String name=null;
	public List<double[]> timeValue=new ArrayList<double[]>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<double[]> getTimeValue() {
		return timeValue;
	}
	public void setTimeValue(List<double[]> timeValue) {
		this.timeValue = timeValue;
	}
}
