package com.example.demo.bean;

public class RuleAnalysis {

	public Rule rule=new Rule();
	public boolean canTriggered=true;
	public CanCauseStateConflict canCauseStateConflict=new CanCauseStateConflict();
	public CanCauseFastChange canCauseFastChange=new CanCauseFastChange();
	public Rule getRule() {
		return rule;
	}
	public void setRule(Rule rule) {
		this.rule = rule;
	}
	public boolean isCanTriggered() {
		return canTriggered;
	}
	public void setCanTriggered(boolean canTriggered) {
		this.canTriggered = canTriggered;
	}
	public CanCauseStateConflict getCanCauseStateConflict() {
		return canCauseStateConflict;
	}
	public void setCanCauseStateConflict(CanCauseStateConflict canCauseStateConflict) {
		this.canCauseStateConflict = canCauseStateConflict;
	}
	public CanCauseFastChange getCanCauseFastChange() {
		return canCauseFastChange;
	}
	public void setCanCauseFastChange(CanCauseFastChange canCauseFastChange) {
		this.canCauseFastChange = canCauseFastChange;
	}
	
}
