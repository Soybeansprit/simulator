package com.example.demo.bean;

public class StateChangeCauseRules {
	public TimeStateRelativeRules start=new TimeStateRelativeRules();
	public TimeStateRelativeRules middle=new TimeStateRelativeRules();
	public TimeStateRelativeRules end=new TimeStateRelativeRules();
	public TimeStateRelativeRules getStart() {
		return start;
	}
	public void setStart(TimeStateRelativeRules start) {
		this.start = start;
	}
	public TimeStateRelativeRules getMiddle() {
		return middle;
	}
	public void setMiddle(TimeStateRelativeRules middle) {
		this.middle = middle;
	}
	public TimeStateRelativeRules getEnd() {
		return end;
	}
	public void setEnd(TimeStateRelativeRules end) {
		this.end = end;
	}
	
}
