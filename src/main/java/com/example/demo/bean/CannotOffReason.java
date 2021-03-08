package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class CannotOffReason {

	public String reason="";
	public List<Rule> cannotTriggeredRules=new ArrayList<Rule>();
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public List<Rule> getCaonnotTriggeredRules() {
		return cannotTriggeredRules;
	}
	public void setCaonnotTriggeredRules(List<Rule> caonnotTriggeredRules) {
		this.cannotTriggeredRules = caonnotTriggeredRules;
	}

}
