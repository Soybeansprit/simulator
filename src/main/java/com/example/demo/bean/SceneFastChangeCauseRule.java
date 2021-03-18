package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class SceneFastChangeCauseRule {
	public String sceneName="";
	public List<StateCauseRuleCount> fastChangeStateCauseRuleCountList=new ArrayList<StateCauseRuleCount>();
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	public List<StateCauseRuleCount> getFastChangeStateCauseRuleCountList() {
		return fastChangeStateCauseRuleCountList;
	}
	public void setFastChangeStateCauseRuleCountList(List<StateCauseRuleCount> fastChangeStateCauseRuleCountList) {
		this.fastChangeStateCauseRuleCountList = fastChangeStateCauseRuleCountList;
	}
}
