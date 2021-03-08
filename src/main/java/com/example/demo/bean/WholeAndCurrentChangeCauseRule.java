package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class WholeAndCurrentChangeCauseRule {

	public List<StateChangeCauseRules> wholeStateChangesCauseRules=new ArrayList<StateChangeCauseRules>();
	public StateChangeCauseRules currentStateChangeCauseRules=new StateChangeCauseRules();
	public List<StateChangeCauseRules> getWholeStateChangesCauseRules() {
		return wholeStateChangesCauseRules;
	}
	public void setWholeStateChangesCauseRules(List<StateChangeCauseRules> wholeStateChangesCauseRules) {
		this.wholeStateChangesCauseRules = wholeStateChangesCauseRules;
	}
	public StateChangeCauseRules getCurrentStateChangeCauseRules() {
		return currentStateChangeCauseRules;
	}
	public void setCurrentStateChangeCauseRules(StateChangeCauseRules currentStateChangeCauseRules) {
		this.currentStateChangeCauseRules = currentStateChangeCauseRules;
	}
	
}
