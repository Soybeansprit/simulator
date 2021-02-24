package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class ConflictTime {
	public List<String[]> conflictStates=new ArrayList<String[]>();
	public String conflictTime=null;
	public List<String[]> getConflictStates() {
		return conflictStates;
	}
	public void setConflictStates(List<String[]> conflictStates) {
		this.conflictStates = conflictStates;
	}
	public String getConflictTime() {
		return conflictTime;
	}
	public void setConflictTime(String conflictTime) {
		this.conflictTime = conflictTime;
	}
}
