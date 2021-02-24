package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;



public class Scene {
	private String sceneName=null;
	
	private List<DataTimeValue> datasTimeValue=new ArrayList<DataTimeValue>();
	private List<NameDataFunction> nameDataFunctions=new ArrayList<NameDataFunction>();
	private List<DeviceAnalysResult> devicesAnalysResults=new ArrayList<DeviceAnalysResult>();
	private List<DataTimeValue> triggeredRulesName=new ArrayList<DataTimeValue>();
	private List<String> cannotTriggeredRulesName=new ArrayList<String>();
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}
	public List<NameDataFunction> getNameDataFunctions() {
		return nameDataFunctions;
	}
	public void setNameDataFunctions(List<NameDataFunction> nameDataFunctions) {
		this.nameDataFunctions = nameDataFunctions;
	}
	public List<DeviceAnalysResult> getDevicesAnalysResults() {
		return devicesAnalysResults;
	}
	public void setDevicesAnalysResults(List<DeviceAnalysResult> devicesAnalysResults) {
		this.devicesAnalysResults = devicesAnalysResults;
	}
	public List<DataTimeValue> getTriggeredRulesName() {
		return triggeredRulesName;
	}
	public void setTriggeredRulesName(List<DataTimeValue> triggeredRulesName) {
		this.triggeredRulesName = triggeredRulesName;
	}
	public List<String> getCannotTriggeredRulesName() {
		return cannotTriggeredRulesName;
	}
	public void setCannotTriggeredRulesName(List<String> cannotTriggeredRulesName) {
		this.cannotTriggeredRulesName = cannotTriggeredRulesName;
	}
	
	public List<DataTimeValue> getDatasTimeValue() {
		return datasTimeValue;
	}
	public void setDatasTimeValue(List<DataTimeValue> datasTimeValue) {
		this.datasTimeValue = datasTimeValue;
	}
}
