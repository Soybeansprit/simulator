package com.example.demo.bean;

public class RuleCount {
	public RuleAndCause causeRule=new RuleAndCause();
	public int count=0;

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public RuleAndCause getCauseRule() {
		return causeRule;
	}
	public void setCauseRule(RuleAndCause causeRule) {
		this.causeRule = causeRule;
	}
}
