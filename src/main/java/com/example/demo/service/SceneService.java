package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Action;
import com.example.demo.bean.ConflictTime;
import com.example.demo.bean.DataFunction;
import com.example.demo.bean.DataTimeValue;
import com.example.demo.bean.DeviceAnalysResult;
import com.example.demo.bean.DeviceConflict;
import com.example.demo.bean.DeviceStateName;
import com.example.demo.bean.NameDataFunction;
import com.example.demo.bean.Rule;
import com.example.demo.bean.Scene;
import com.example.demo.bean.StateChangeFast;
import com.example.demo.bean.StateNameRelativeRule;
import com.example.demo.bean.StatesChange;
import com.example.demo.bean.TemplGraph;
import com.example.demo.service.GetTemplate.Template;

@Service
public class SceneService {

	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		SceneService sceneService=new SceneService();
		GetTemplate getTemplate=new GetTemplate();
		TemplGraphService tGraph=new TemplGraphService();
		TGraphToDot tDot=new TGraphToDot();
		RuleService ruleService=new RuleService();
		
		String rulePath="D:\\rules0105.txt";
		String filePath="D:\\exp";
		String modelName="exp0108-final-random-scene2";
		String modelPathStr=filePath+"\\"+modelName+".xml";
		List<Rule> rules=ruleService.getRuleListFromTxt(rulePath);
		List<Template> templates=getTemplate.getTemplate(modelPathStr);
		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
		for(Template template:templates) {
			templGraphs.add(tGraph.getTemplGraph(template));
		}
		List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration.indexOf("controlled_device")>=0) {
				controlledDevices.add(templGraph);
			}
		}
		List<Action> actions=tDot.getActions(rules,controlledDevices);
		
		String allTime="300.0099999998758";
		String equivalentTime="24";
		String intervalTime="300";
		
		Scene scene=sceneService.getSceneAnalysis(filePath, modelName, controlledDevices, actions, 
				allTime, equivalentTime, intervalTime);
		System.out.println(scene);
		for(TemplGraph controlledDevice:controlledDevices) {
			String deviceName=controlledDevice.name.substring(0,1).toLowerCase()+controlledDevice.name.substring(1);
			List<List<StateNameRelativeRule>> allConflictCauseRules=new ArrayList<List<StateNameRelativeRule>>();
			List<List<StateNameRelativeRule>> allFreqStatesChangeCauseRules=new ArrayList<List<StateNameRelativeRule>>();

			
			DeviceAnalysResult deviceAnalysResult=sceneService.getDeviceAnalysResult(deviceName, scene);
			DeviceConflict deviceConflict=deviceAnalysResult.statesConflict;
			StatesChange statesChange=deviceAnalysResult.statesChange;
			DeviceStateName deviceStateName=deviceAnalysResult.deviceStateName;
			
			allConflictCauseRules=sceneService.getDeviceAllConflictCauseRules(deviceConflict, scene.getTriggeredRulesName(), deviceStateName);
			allFreqStatesChangeCauseRules=sceneService.getDeviceAllFreqStatesChangeCauseRules(statesChange, scene.getTriggeredRulesName(), deviceStateName);
			System.out.println(allConflictCauseRules);
			System.out.println(allFreqStatesChangeCauseRules);
			
		}

	}
	
	////////////////////////////////场景分析///////////////////////////////
	public Scene getSceneAnalysis(String filePath,String modelName,List<TemplGraph> controlledDevices,List<Action> actions
			,String allTime,String equivalentTime,String intervalTime) throws IOException {
		DataAnalysisService dataAnalysisService=new DataAnalysisService();
		Scene scene=new Scene();
		String modelPathStr=filePath+"\\"+modelName+".txt";
		List<DataTimeValue> datasTimeValue=dataAnalysisService.getDataTimes(modelPathStr);
		scene.setSceneName(modelName);
		scene.setDatasTimeValue(datasTimeValue);
		List<DataTimeValue> rulesTimeValue=new ArrayList<DataTimeValue>();
		for(DataTimeValue dataTimeValue:datasTimeValue) {
			if(dataTimeValue.name.indexOf("rule")>=0) {
				rulesTimeValue.add(dataTimeValue);
			}
			
		}
		List<NameDataFunction> nameDataFunctions=dataAnalysisService.getSceneNameDataFunctions(datasTimeValue);
		scene.setNameDataFunctions(nameDataFunctions);
		List<DataTimeValue> triggeredRulesName=dataAnalysisService.getSceneTriggeredRules(rulesTimeValue);
		scene.setTriggeredRulesName(triggeredRulesName);
		List<String> cannotTriggeredRulesName=dataAnalysisService.getSceneCannotTriggeredRules(rulesTimeValue);
		scene.setCannotTriggeredRulesName(cannotTriggeredRulesName);
		List<DeviceAnalysResult> devicesAnalysResults=new ArrayList<DeviceAnalysResult>();
		for(DataTimeValue dataTimeValue:datasTimeValue) {
			if(dataTimeValue.name.indexOf("rule")>=0) {
				continue;
			}else {
				for(TemplGraph controlledDevice:controlledDevices) {
					String deviceName=controlledDevice.name.substring(0,1).toLowerCase()+controlledDevice.name.substring(1);
					if(deviceName.equals(dataTimeValue.name)) {
						DeviceAnalysResult deviceAnalysResult=dataAnalysisService.getSceneDeviceAnalysisResult(dataTimeValue, controlledDevice, actions, rulesTimeValue, 
								allTime, equivalentTime, intervalTime);
						devicesAnalysResults.add(deviceAnalysResult);
						break;
					}
				}
			}
		}
		scene.setDevicesAnalysResults(devicesAnalysResults);
		
		
		return scene;
	}
	
	/////////////////////////返回所有导致状态冲突的规则/////////////////////////////
	public List<List<StateNameRelativeRule>> getDeviceAllConflictCauseRules(DeviceConflict deviceConflict,List<DataTimeValue> triggeredRulesName,DeviceStateName deviceStateName){
		List<List<StateNameRelativeRule>> allConflictCauseRules=new ArrayList<List<StateNameRelativeRule>>();
		for(ConflictTime conflictStateTime:deviceConflict.conflictTimes) {
			List<StateNameRelativeRule> conflictCauseRules=getConflictCauseRules(conflictStateTime, triggeredRulesName, deviceStateName);
			allConflictCauseRules.add(conflictCauseRules);
		}
		
		return allConflictCauseRules;
		
	}
	
	///////////////////////////////导致状态冲突的规则/////////////////////////////////////
	public List<StateNameRelativeRule> getConflictCauseRules(ConflictTime conflictStateTime,List<DataTimeValue> triggeredRulesName,DeviceStateName deviceStateName) {
		DataAnalysisService dataAnalysisService=new DataAnalysisService();
		
		List<StateNameRelativeRule> conflictCauseRules=new ArrayList<StateNameRelativeRule>();		
		double conflictTime=Double.parseDouble(conflictStateTime.conflictTime);
		for(String[] conflictState:conflictStateTime.conflictStates) {
			StateNameRelativeRule stateNameRelativeRule=dataAnalysisService.getStateNameRelativeRule(deviceStateName, conflictState[0]);
			StateNameRelativeRule conflictCaueseRule=new StateNameRelativeRule();
			conflictCaueseRule.stateValue=stateNameRelativeRule.stateValue;
			conflictCaueseRule.stateName=stateNameRelativeRule.stateName;
			for(Rule rule:stateNameRelativeRule.relativeRules) {
				for(DataTimeValue ruleTimeValue:triggeredRulesName) {
					if(ruleTimeValue.name.equals(rule.getRuleName())) {
						NameDataFunction ruleDataFunction=dataAnalysisService.getNameDataFunction(ruleTimeValue);
						for(DataFunction dataFunction:ruleDataFunction.dataFunctions) {
							double downValue=dataFunction.downValue;
							double upValue=dataFunction.upValue;
							if(downValue==1||upValue==1) {
								if(dataFunction.downTime<=conflictTime && dataFunction.upTime>=conflictTime) {
									conflictCaueseRule.relativeRules.add(rule);
									break;
								}
							}
							
						}
						break;
					}
				}
			}
			conflictCauseRules.add(conflictCaueseRule);
		}
		return conflictCauseRules;
	}
	
	public List<List<StateNameRelativeRule>> getDeviceAllFreqStatesChangeCauseRules(StatesChange statesChange,List<DataTimeValue> triggeredRulesName,DeviceStateName deviceStateName){
		List<List<StateNameRelativeRule>> allFreqStatesChangeCauseRules=new ArrayList<List<StateNameRelativeRule>>();	
		for(StateChangeFast stateChangeFast:statesChange.stateChangeFasts) {
			List<StateNameRelativeRule> freqStatesChangeCauseRules=new ArrayList<StateNameRelativeRule>();
			freqStatesChangeCauseRules=getFrequentStatesChangeCauseRules(stateChangeFast, triggeredRulesName, deviceStateName);
			allFreqStatesChangeCauseRules.add(freqStatesChangeCauseRules);
		}
		return allFreqStatesChangeCauseRules;
	}
	
	
	//////////////////////////////////获得影响快速变化的规则/////////////////////////////
	public List<StateNameRelativeRule> getFrequentStatesChangeCauseRules(StateChangeFast stateChangeFast,List<DataTimeValue> triggeredRulesName,DeviceStateName deviceStateName){
		DataAnalysisService dataAnalysisService=new DataAnalysisService();
		List<StateNameRelativeRule> causeRules=new ArrayList<StateNameRelativeRule>();
		StateNameRelativeRule startStateRelativeRule=dataAnalysisService.getStateNameRelativeRule(deviceStateName, stateChangeFast.startTimeValue[1]+"");
		StateNameRelativeRule middleStateRelativeRule=dataAnalysisService.getStateNameRelativeRule(deviceStateName, stateChangeFast.middleTimeValue[1]+"");
		StateNameRelativeRule endStateRelativeRule=dataAnalysisService.getStateNameRelativeRule(deviceStateName, stateChangeFast.endTimeValue[1]+"");
		StateNameRelativeRule startCaueseRule=new StateNameRelativeRule();
		startCaueseRule.stateName=startStateRelativeRule.stateName;
		startCaueseRule.stateValue=startStateRelativeRule.stateValue;
		for(Rule rule:startStateRelativeRule.relativeRules) {
			for(DataTimeValue ruleTimeValue:triggeredRulesName) {
				if(ruleTimeValue.name.equals(rule.getRuleName())) {
					NameDataFunction ruleDataFunction=dataAnalysisService.getNameDataFunction(ruleTimeValue);
					for(DataFunction dataFunction:ruleDataFunction.dataFunctions) {
						double downValue=dataFunction.downValue;
						double upValue=dataFunction.upValue;
						if(downValue==1||upValue==1) {
							if(dataFunction.downTime<=stateChangeFast.startTimeValue[0] && dataFunction.upTime>=stateChangeFast.startTimeValue[0]) {
								startCaueseRule.relativeRules.add(rule);
								break;
							}
						}
						
					}
					break;
				}
			}
		}
		
		StateNameRelativeRule middleCaueseRule=new StateNameRelativeRule();
		middleCaueseRule.stateName=middleStateRelativeRule.stateName;
		middleCaueseRule.stateValue=middleStateRelativeRule.stateValue;
		for(Rule rule:middleStateRelativeRule.relativeRules) {
			for(DataTimeValue ruleTimeValue:triggeredRulesName) {
				if(ruleTimeValue.name.equals(rule.getRuleName())) {
					NameDataFunction ruleDataFunction=dataAnalysisService.getNameDataFunction(ruleTimeValue);
					for(DataFunction dataFunction:ruleDataFunction.dataFunctions) {
						double downValue=dataFunction.downValue;
						double upValue=dataFunction.upValue;
						if(downValue==1||upValue==1) {
							if(dataFunction.downTime<=stateChangeFast.middleTimeValue[0] && dataFunction.upTime>=stateChangeFast.middleTimeValue[0]) {
								middleCaueseRule.relativeRules.add(rule);
								break;
							}
						}
						
					}
					break;
				}
			}
		}
		
		StateNameRelativeRule endCaueseRule=new StateNameRelativeRule();
		endCaueseRule.stateName=endStateRelativeRule.stateName;
		endCaueseRule.stateValue=endStateRelativeRule.stateValue;
		for(Rule rule:endStateRelativeRule.relativeRules) {
			for(DataTimeValue ruleTimeValue:triggeredRulesName) {
				if(ruleTimeValue.name.equals(rule.getRuleName())) {
					NameDataFunction ruleDataFunction=dataAnalysisService.getNameDataFunction(ruleTimeValue);
					for(DataFunction dataFunction:ruleDataFunction.dataFunctions) {
						double downValue=dataFunction.downValue;
						double upValue=dataFunction.upValue;
						if(downValue==1||upValue==1) {
							if(dataFunction.downTime<=stateChangeFast.endTimeValue[0] && dataFunction.upTime>=stateChangeFast.endTimeValue[0]) {
								endCaueseRule.relativeRules.add(rule);
								break;
							}
						}
						
					}
					break;
				}
			}
		}
		
		causeRules.add(startCaueseRule);
		causeRules.add(middleCaueseRule);
		causeRules.add(endCaueseRule);
		
		
		return causeRules;
	}
	
	////////////////////////////////获得某场景某设备的状态冲突情况//////////////////////////////////
	public DeviceConflict getDeviceConflict(String deviceName,Scene scene) {
		DeviceConflict deviceConflict=new DeviceConflict();
		List<DeviceAnalysResult> devicesAnalysResults=scene.getDevicesAnalysResults();
		for(DeviceAnalysResult deviceAnalysResult:devicesAnalysResults) {
			if(deviceAnalysResult.deviceName.equals(deviceName)) {
				deviceConflict=deviceAnalysResult.statesConflict;
				break;
			}
		}
		
		return deviceConflict;
	}
	
	 
	
	
	///////////////////////////////获得某场景某设备状态变化情况///////////////////////////////////////////
	public StatesChange getDeviceStatesChange(String deviceName,Scene scene) {
		StatesChange statesChange=new StatesChange();
		List<DeviceAnalysResult> devicesAnalysResults=scene.getDevicesAnalysResults();
		for(DeviceAnalysResult deviceAnalysResult:devicesAnalysResults) {
			if(deviceAnalysResult.deviceName.equals(deviceName)) {
				statesChange=deviceAnalysResult.statesChange;
				break;
			}
		}
		return statesChange;
	}
	
	///////////////////////////获得某个设备的分析结果//////////////////////////////
	public DeviceAnalysResult getDeviceAnalysResult(String deviceName,Scene scene) {
		DeviceAnalysResult device=new DeviceAnalysResult();
		List<DeviceAnalysResult> devicesAnalysResults=scene.getDevicesAnalysResults();
		for(DeviceAnalysResult deviceAnalysResult:devicesAnalysResults) {
			if(deviceAnalysResult.deviceName.equals(deviceName)) {
				device=deviceAnalysResult;
				break;
			}
		}
		return device;
	}

}
