package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class CountStatesCauseRule {

	public int count=0;
	public List<StateCauseRule> statesCauseRule=new ArrayList<StateCauseRule>();
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<StateCauseRule> getStatesCauseRule() {
		return statesCauseRule;
	}
	public void setStatesCauseRule(List<StateCauseRule> statesCauseRule) {
		this.statesCauseRule = statesCauseRule;
	}
}
