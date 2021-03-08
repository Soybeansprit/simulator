package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Action;
import com.example.demo.bean.ConflictTime;
import com.example.demo.bean.DataFunction;
import com.example.demo.bean.DataTimeValue;
import com.example.demo.bean.DeviceAnalysResult;
import com.example.demo.bean.DeviceConflict;
import com.example.demo.bean.DeviceStateName;
import com.example.demo.bean.GraphNode;
import com.example.demo.bean.GraphNodeArrow;
import com.example.demo.bean.NameDataFunction;
import com.example.demo.bean.Rule;
import com.example.demo.bean.RuleAndCause;
import com.example.demo.bean.RuleAndRelativeRules;
import com.example.demo.bean.Scene;
import com.example.demo.bean.SceneChild;
import com.example.demo.bean.ScenesTree;
import com.example.demo.bean.StateAndRuleAndCauseRule;
import com.example.demo.bean.StateCauseRulesAndRelativeRules;
import com.example.demo.bean.StateChangeCauseRules;
import com.example.demo.bean.StateChangeFast;
import com.example.demo.bean.StateNameRelativeRule;
import com.example.demo.bean.StatesChange;
import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.TimeStateRelativeRules;
import com.example.demo.bean.WholeAndCurrentChangeCauseRule;
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
		ToNode toNode=new ToNode();
		
		String rulePath="D:\\rules0105.txt";
		String filePath="D:\\exp";
		String modelName="exp0108-final-random-scene2";
		String modelPathStr=filePath+"\\"+modelName+".xml";
		List<Rule> rules=ruleService.getRuleListFromTxt(rulePath);
		List<GraphNode> graphNodes=new ArrayList<GraphNode>();
		graphNodes=toNode.getNodes("D:\\workspace\\exp0108-ifd.dot");
		List<Rule> relativeRules1=sceneService.getRulesfromIFDGraph(graphNodes, rules, "rule7");
		List<Rule> relativeRules2=sceneService.getRulesfromIFDGraph(graphNodes, rules, "rule5");
		
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
			List<List<StateCauseRulesAndRelativeRules>> allStateCauseRules=new ArrayList<List<StateCauseRulesAndRelativeRules>>();
			List<List<StateAndRuleAndCauseRule>> allConflictRuleCauseRules=new ArrayList<List<StateAndRuleAndCauseRule>>();
			List<WholeAndCurrentChangeCauseRule> allFastStatesChangeCauseRules=new ArrayList<WholeAndCurrentChangeCauseRule>();
			
			DeviceAnalysResult deviceAnalysResult=sceneService.getDeviceAnalysResult(deviceName, scene);
			DeviceConflict deviceConflict=deviceAnalysResult.statesConflict;
			StatesChange statesChange=deviceAnalysResult.statesChange;
			DeviceStateName deviceStateName=deviceAnalysResult.deviceStateName;
			
			allFastStatesChangeCauseRules=sceneService.getAllStatesChangeFastCauseRules(statesChange, scene.getTriggeredRulesName(), deviceStateName);
			allConflictRuleCauseRules=sceneService.getAllDeviceConflictCauseRules(deviceConflict, scene.getTriggeredRulesName(), deviceStateName, rules, graphNodes);
			allStateCauseRules=sceneService.getAllConflcitCauseRules(deviceConflict, scene.getTriggeredRulesName(), deviceStateName, rules, graphNodes);
			allConflictCauseRules=sceneService.getDeviceAllConflictCauseRules(deviceConflict, scene.getTriggeredRulesName(), deviceStateName);
			allFreqStatesChangeCauseRules=sceneService.getDeviceAllFreqStatesChangeCauseRules(statesChange, scene.getTriggeredRulesName(), deviceStateName);
			System.out.println(allConflictCauseRules);
			System.out.println(allFreqStatesChangeCauseRules);
			
		}

	}
	
	////////////////////////////获得仿真文件的同时直接生成Scene的dataTimeValue以及部分内容/////////////////////////
	public List<Scene> getAllSimulationDataTimeValue(ScenesTree scenesTree,String filePath,String initFileName,String uppaalBinPath,int dataNum) throws IOException {
		CMD cmd=new CMD();
		DataAnalysisService dataAnalysisService=new DataAnalysisService();
		List<Scene> scenes=new ArrayList<Scene>();
		String initFileModelName=initFileName.replace(".xml", "");
		String finalModelNameSame=initFileModelName+"-final";
		for(SceneChild sceneChild:scenesTree.getChildren()) {
			String finalRandomSameModelName=finalModelNameSame+"-"+sceneChild.getName()+".xml";
			cmd.gCsvFile(uppaalBinPath, filePath+"\\"+finalRandomSameModelName);
			String csvFileSameName=finalRandomSameModelName+"-q0-e";
			Scene scene=new Scene();
			scene.setSceneName(sceneChild.getName());
			List<DataTimeValue> dataTimeValues=new ArrayList<DataTimeValue>();
			List<NameDataFunction> nameDataFunctions=new ArrayList<NameDataFunction>();
//			List<String> csvFilePathList=new ArrayList<String>();
			for(int i=0;i<dataNum;i++) {
//				csvFilePathList.add(filePath+"\\"+csvFileSameName+i+".csv");
				///////////////读取数据//////////////////
				DataTimeValue dataTimeValue=dataAnalysisService.getDataTimeValue(filePath+"\\"+csvFileSameName+i+".csv");
				dataTimeValues.add(dataTimeValue);
				NameDataFunction nameDataFunction=dataAnalysisService.getNameDataFunction(dataTimeValue);
				nameDataFunctions.add(nameDataFunction);
			}
			List<DataTimeValue> triggeredRules=dataAnalysisService.getSceneTriggeredRules(dataTimeValues);
			List<String> cannotTriggeredRules=dataAnalysisService.getSceneCannotTriggeredRules(dataTimeValues);
			scene.setDatasTimeValue(dataTimeValues);
			scene.setNameDataFunctions(nameDataFunctions);
			scene.setTriggeredRulesName(triggeredRules);
			scene.setCannotTriggeredRulesName(cannotTriggeredRules);
			
			scenes.add(scene);
			
		}
		
		return scenes;		
	}
	
	public Scene getSimulationDataTimeValue(String sceneName,String filePath,String initFileName,String uppaalBinPath,int dataNum) throws IOException {
		Scene scene=new Scene();
		CMD cmd=new CMD();
		DataAnalysisService dataAnalysisService=new DataAnalysisService();
		String initFileModelName=initFileName.replace(".xml", "");
		String finalModelName=initFileModelName+"-final-"+sceneName+".xml";
		cmd.gCsvFile(uppaalBinPath, filePath+"\\"+finalModelName);
		String csvFileSameName=finalModelName+"-q0-e";
		List<DataTimeValue> dataTimeValues=new ArrayList<DataTimeValue>();
		List<NameDataFunction> nameDataFunctions=new ArrayList<NameDataFunction>();
		for(int i=0;i<dataNum;i++) {
//			csvFilePathList.add(filePath+"\\"+csvFileSameName+i+".csv");
			///////////////读取数据//////////////////
			DataTimeValue dataTimeValue=dataAnalysisService.getDataTimeValue(filePath+"\\"+csvFileSameName+i+".csv");
			dataTimeValues.add(dataTimeValue);
			NameDataFunction nameDataFunction=dataAnalysisService.getNameDataFunction(dataTimeValue);
			nameDataFunctions.add(nameDataFunction);
		}
		List<DataTimeValue> triggeredRules=dataAnalysisService.getSceneTriggeredRules(dataTimeValues);
		List<String> cannotTriggeredRules=dataAnalysisService.getSceneCannotTriggeredRules(dataTimeValues);
		scene.setDatasTimeValue(dataTimeValues);
		scene.setNameDataFunctions(nameDataFunctions);
		scene.setTriggeredRulesName(triggeredRules);
		scene.setCannotTriggeredRulesName(cannotTriggeredRules);
		return scene;
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
	

	////////////////////////////////////////分析场景的device分析结果
	public Scene getDeviceAnalysisResult(Scene scene,List<Rule> rules,String simulationTime,String filePath,String initFileName,String equivalentTime,String intervalTime) throws DocumentException {
		TemplGraphService templGraphService=new TemplGraphService();
		TGraphToDot tDot=new TGraphToDot();
		DataAnalysisService dataAnalysisService=new DataAnalysisService();
		
		String initFileModelName=initFileName.replace(".xml", "");
		String finalModelNameSame=initFileModelName+"-final";
		String finalModelName=finalModelNameSame+"-"+scene.getSceneName()+".xml";
		List<TemplGraph> templGraphs=templGraphService.getTemplGraphs(filePath+"\\"+finalModelName);
		List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration.indexOf("controlled_device")>=0) {
				controlledDevices.add(templGraph);
			}
		}
		List<Action> actions=tDot.getActions(rules,controlledDevices);
		List<DeviceAnalysResult> devicesAnalysResults=new ArrayList<DeviceAnalysResult>();
		List<DataTimeValue> rulesTimeValue=new ArrayList<DataTimeValue>();
		for(DataTimeValue dataTimeValue:scene.getDatasTimeValue()) {
			if(dataTimeValue.name.indexOf("rule")>=0) {
				rulesTimeValue.add(dataTimeValue);
			}
			
		}
		for(DataTimeValue dataTimeValue:scene.getDatasTimeValue()) {
			if(dataTimeValue.name.indexOf("rule")>=0) {
				continue;
			}else {
				for(TemplGraph controlledDevice:controlledDevices) {
					String deviceName=controlledDevice.name.substring(0,1).toLowerCase()+controlledDevice.name.substring(1);
					if(deviceName.equals(dataTimeValue.name)) {
						DeviceAnalysResult deviceAnalysResult=dataAnalysisService.getSceneDeviceAnalysisResult(dataTimeValue, controlledDevice, actions, rulesTimeValue, 
								simulationTime, equivalentTime, intervalTime);
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
	/////////////////////////返回所有导致状态冲突的规则/利用IFD////////////////////////////
	public List<List<StateCauseRulesAndRelativeRules>> getAllConflcitCauseRules(DeviceConflict deviceConflict,List<DataTimeValue> triggeredRulesName,DeviceStateName deviceStateName,List<Rule> rules,List<GraphNode> graphNodes){
		List<List<StateCauseRulesAndRelativeRules>> allConflictCauseRules=new ArrayList<List<StateCauseRulesAndRelativeRules>>();
		for(ConflictTime conflictStateTime:deviceConflict.conflictTimes) {
			List<StateCauseRulesAndRelativeRules> conflictCauseRules=getConflictCauseRules(conflictStateTime, triggeredRulesName, deviceStateName, rules, graphNodes);
			allConflictCauseRules.add(conflictCauseRules);
		}
		return allConflictCauseRules;
	}
	
	/////////////////////////返回所有导致状态冲突的规则/利用IFD////ruleAndCauseRules////////////////////////
	public List<List<StateAndRuleAndCauseRule>> getAllDeviceConflictCauseRules(DeviceConflict deviceConflict,List<DataTimeValue> triggeredRulesName,DeviceStateName deviceStateName,List<Rule> rules,List<GraphNode> graphNodes){
		List<List<StateAndRuleAndCauseRule>> allConflictCauseRules=new ArrayList<List<StateAndRuleAndCauseRule>>();
		for(ConflictTime conflictStateTime:deviceConflict.conflictTimes) {
			List<StateAndRuleAndCauseRule> conflictCauseRules=getDeviceConflictCauseRules(conflictStateTime, triggeredRulesName, deviceStateName, rules, graphNodes);
			allConflictCauseRules.add(conflictCauseRules);
		}
		return allConflictCauseRules;
	}	
	
	//////////////////////////导致状态冲突的规则/利用IFD////ruleAndCauseRules////////////////
	public List<StateAndRuleAndCauseRule> getDeviceConflictCauseRules(ConflictTime conflictStateTime,List<DataTimeValue> triggeredRulesName,DeviceStateName deviceStateName,List<Rule> rules,List<GraphNode> graphNodes) {
		DataAnalysisService dataAnalysisService=new DataAnalysisService();
		List<StateAndRuleAndCauseRule> stateAndRuleAndCauseRules=new ArrayList<StateAndRuleAndCauseRule>();
		double conflictTime=Double.parseDouble(conflictStateTime.conflictTime);
		for(String[] conflictState:conflictStateTime.conflictStates) {
			//////////////////所有可能导致冲突的规则///////////
			List<Rule> possibleCauseRules=dataAnalysisService.getStatePossibleCauseRules(deviceStateName, conflictState[0]);
			StateAndRuleAndCauseRule stateAndRuleAndCauseRule=new StateAndRuleAndCauseRule();
			stateAndRuleAndCauseRule.stateValue=conflictState[0];
			stateAndRuleAndCauseRule.stateName=conflictState[1];
			////////////////////在这个时刻导致冲突的规则//////////////////
			List<Rule> causeRules=new ArrayList<Rule>();
			for(Rule rule:possibleCauseRules) {
				for(DataTimeValue ruleTimeValue:triggeredRulesName) {
					if(ruleTimeValue.name.equals(rule.getRuleName())) {
						NameDataFunction ruleDataFunction=dataAnalysisService.getNameDataFunction(ruleTimeValue);
						for(DataFunction dataFunction:ruleDataFunction.dataFunctions) {
							double downValue=dataFunction.downValue;
							double upValue=dataFunction.upValue;
							if(downValue==1||upValue==1) {
								if(dataFunction.downTime<=conflictTime && dataFunction.upTime>=conflictTime) {
									/////////////规则在这个时刻发生，指会造成冲突的规则//////////////
									causeRules.add(rule);
									break;
								}
							}
							
						}
						break;
					}
				}
			}
			////////////////////触发该规则的rules////////////////
			for(Rule rule:causeRules) {
				RuleAndCause ruleAndCause=getRuleCauseRulesfromIFDGraph(graphNodes, rules, rule.getRuleName(),triggeredRulesName);
				stateAndRuleAndCauseRule.rulesAndCauseRules.add(ruleAndCause);
				for(GraphNode graphNode:graphNodes) {
					graphNode.flag=false;
				}
			}
			stateAndRuleAndCauseRules.add(stateAndRuleAndCauseRule);
			
		}
		
		return stateAndRuleAndCauseRules;
		
	}
	
	///////////////////////////////导致状态冲突的规则/同时利用IFD////////////////////////////////////
	public List<StateCauseRulesAndRelativeRules> getConflictCauseRules(ConflictTime conflictStateTime,List<DataTimeValue> triggeredRulesName,DeviceStateName deviceStateName,List<Rule> rules,List<GraphNode> graphNodes){
		DataAnalysisService dataAnalysisService=new DataAnalysisService();
		
 		List<StateCauseRulesAndRelativeRules> conflictCauseRules=new ArrayList<StateCauseRulesAndRelativeRules>();	

		double conflictTime=Double.parseDouble(conflictStateTime.conflictTime);
		for(String[] conflictState:conflictStateTime.conflictStates) {
			//////////////////所有可能导致冲突的规则///////////
			StateCauseRulesAndRelativeRules stateCauseRules=dataAnalysisService.getStateCauseRules(deviceStateName, conflictState[0]);
			////////////////////在这个时刻导致冲突的规则//////////////////
			StateCauseRulesAndRelativeRules conflictCaueseRule=new StateCauseRulesAndRelativeRules();
			conflictCaueseRule.stateValue=stateCauseRules.stateValue;
			conflictCaueseRule.stateName=stateCauseRules.stateName;
			for(RuleAndRelativeRules causeRule:stateCauseRules.causeRules) {
				Rule rule=causeRule.rule;
				for(DataTimeValue ruleTimeValue:triggeredRulesName) {
					if(ruleTimeValue.name.equals(rule.getRuleName())) {
						NameDataFunction ruleDataFunction=dataAnalysisService.getNameDataFunction(ruleTimeValue);
						for(DataFunction dataFunction:ruleDataFunction.dataFunctions) {
							double downValue=dataFunction.downValue;
							double upValue=dataFunction.upValue;
							if(downValue==1||upValue==1) {
								if(dataFunction.downTime<=conflictTime && dataFunction.upTime>=conflictTime) {
									/////////////规则在这个时刻发生//////////////
									RuleAndRelativeRules causeRuleAndRelativeRules=new RuleAndRelativeRules();
									causeRuleAndRelativeRules.rule=rule;
									conflictCaueseRule.causeRules.add(causeRuleAndRelativeRules);
									break;
								}
							}
							
						}
						break;
					}
				}
			}
			for(RuleAndRelativeRules causeRule:conflictCaueseRule.causeRules) {
				Rule rule=causeRule.rule;
				String ruleName=rule.getRuleName();
				List<Rule> relativeRules=getRulesfromIFDGraph(graphNodes, rules, ruleName);
				causeRule.relativeRules=relativeRules;
			}
			conflictCauseRules.add(conflictCaueseRule);
			
		}
		return conflictCauseRules;
	}
	
	/////////////////////////给定rule节点，获得该节点的causeRules///////////////////////////
	public RuleAndCause getRuleCauseRulesfromIFDGraph(List<GraphNode> graphNodes,List<Rule> rules,String ruleName,List<DataTimeValue> triggeredRulesName) {
		RuleAndCause ruleAndeCause=new RuleAndCause();
		for(DataTimeValue triggeredRule:triggeredRulesName) {
			if(triggeredRule.name.equals(ruleName)) {
				for(Rule rule:rules) {
					if(rule.getRuleName().equals(ruleName)) {
						ruleAndeCause.selfRule=rule;
						break;
					}
				}
				break;
			}
		}

		GraphNode ruleStartNode=new GraphNode();
		for(GraphNode graphNode:graphNodes) {
			if(graphNode.getName().equals(ruleName)) {
				ruleStartNode=graphNode;
				break;
			}
		}
		Stack<GraphNode> stack=new Stack<GraphNode>();
		if(!ruleStartNode.flag) {
			stack.push(ruleStartNode);
			ruleStartNode.flag=true;
		}
		while(!stack.isEmpty()) {
			GraphNode graphNode=stack.pop();
			if(graphNode.getShape().indexOf("hexagon")>=0) {
				////////////如果当前节点是rule节点
				
				
				for(GraphNodeArrow pArrow:graphNode.getpNodeList()) {
					//////////p节点为trigger，全push进去
					GraphNode pGraphNode=pArrow.getGraphNode();
					stack.push(pGraphNode);
				}
			}else if(graphNode.getShape().indexOf("oval")>=0) {
				//////////////如果当前节点是trigger节点
				for(GraphNodeArrow pArrow:graphNode.getpNodeList()) {
					//////////////专门push p节点中直接影响的action节点
					if(pArrow.getColor()!=null && pArrow.getColor().indexOf("red")>=0 && pArrow.getStyle()==null) {
						//////////红色实线
						GraphNode pGraphNode=pArrow.getGraphNode();	
						if(pGraphNode.getShape().indexOf("record")>=0) {
							stack.push(pGraphNode);
						}
						
					}

					
				}
				
			}else if(graphNode.getShape().indexOf("record")>=0) {
				//////////////如果当前节点是action节点
				for(GraphNodeArrow pArrow:graphNode.getpNodeList()) {
					/////////////p节点都是rule节点，就不push了
					GraphNode pGraphNode=pArrow.getGraphNode();
//					stack.push(pGraphNode);
					String pRuleName=pGraphNode.getName();
					ruleAndeCause.causeRules.add(getRuleCauseRulesfromIFDGraph(graphNodes, rules, pRuleName,triggeredRulesName));
				}
			}
		}
		
		return ruleAndeCause;
	}
	
	
	public List<Rule> getRulesfromIFDGraph(List<GraphNode> graphNodes,List<Rule> rules,String ruleName) {
		List<Rule> relativeRules=new ArrayList<Rule>();
		List<String> rulesName=new ArrayList<String>();
		GraphNode ruleStartNode=new GraphNode();
		for(GraphNode graphNode:graphNodes) {
			if(graphNode.getName().equals(ruleName)) {
				ruleStartNode=graphNode;
				break;
			}
		}
		
		Stack<GraphNode> stack=new Stack<GraphNode>();
		stack.push(ruleStartNode);
		while(!stack.isEmpty()) {
			GraphNode graphNode=stack.pop();
			if(graphNode.getShape().indexOf("hexagon")>=0) {
				////////////如果当前节点是rule节点
				String currentRuleName=graphNode.getName();
				boolean existRule=false;
				for(String rName:rulesName) {
					if(currentRuleName.equals(rName)) {
						existRule=true;
						break;
					}
				}
				if(!existRule) {
					rulesName.add(currentRuleName);
				}
				for(GraphNodeArrow pArrow:graphNode.getpNodeList()) {
					//////////p节点为trigger，全push进去
					GraphNode pGraphNode=pArrow.getGraphNode();
					stack.push(pGraphNode);
				}
			}else if(graphNode.getShape().indexOf("oval")>=0) {
				//////////////如果当前节点是trigger节点
				for(GraphNodeArrow pArrow:graphNode.getpNodeList()) {
					//////////////专门push p节点中直接影响的action节点
					if(pArrow.getColor()!=null && pArrow.getColor().indexOf("red")>=0 && pArrow.getStyle()==null) {
						//////////红色实线
						GraphNode pGraphNode=pArrow.getGraphNode();	
						if(pGraphNode.getShape().indexOf("record")>=0) {
							stack.push(pGraphNode);
						}
						
					}

					
				}
				
			}else if(graphNode.getShape().indexOf("record")>=0) {
				//////////////如果当前节点是action节点
				for(GraphNodeArrow pArrow:graphNode.getpNodeList()) {
					/////////////p节点都是rule节点，全push进去
					GraphNode pGraphNode=pArrow.getGraphNode();
					stack.push(pGraphNode);
				}
			}

		}
		
		for(String rName:rulesName) {
			for(Rule rule:rules) {
				if(rName.equals(rule.getRuleName())) {
					relativeRules.add(rule);
					break;
				}
			}
		}
		
		return relativeRules;
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
	
	public List<WholeAndCurrentChangeCauseRule> getAllStatesChangeFastCauseRules(StatesChange statesChange,List<DataTimeValue> triggeredRulesName,DeviceStateName deviceStateName){
		List<WholeAndCurrentChangeCauseRule> wholeAndCurrentChangeCauseRules=new ArrayList<WholeAndCurrentChangeCauseRule>();
		for(StateChangeFast stateChangeFast:statesChange.stateChangeFasts) {
			WholeAndCurrentChangeCauseRule wholeAndCurrentChangeCauseRule=getStatesChangeFastCauseRules(statesChange.stateChangeFasts, stateChangeFast, triggeredRulesName, deviceStateName);
			wholeAndCurrentChangeCauseRules.add(wholeAndCurrentChangeCauseRule);
		}
		return wholeAndCurrentChangeCauseRules;
	}
	
	//////////////////////////////////获得影响快速变化的规则/////////////////////////////
	public WholeAndCurrentChangeCauseRule getStatesChangeFastCauseRules(List<StateChangeFast> stateChangeFasts,StateChangeFast stateChangeFast,List<DataTimeValue> triggeredRulesName,DeviceStateName deviceStateName){
		
		
		WholeAndCurrentChangeCauseRule wholeAndCurrentChangeCauseRule=new WholeAndCurrentChangeCauseRule();
		List<StateChangeCauseRules> stateChangesCauseRules=new ArrayList<StateChangeCauseRules>();
		for(StateChangeFast stateChange:stateChangeFasts) {
			StateChangeCauseRules stateChangeCauseRules=new StateChangeCauseRules();
			stateChangeCauseRules=getStateChangeCauseRules(stateChange, triggeredRulesName, deviceStateName);
			stateChangesCauseRules.add(stateChangeCauseRules);
		}
		wholeAndCurrentChangeCauseRule.wholeStateChangesCauseRules=stateChangesCauseRules;
		StateChangeCauseRules currentChangeCauseRules=new StateChangeCauseRules();
		currentChangeCauseRules=getStateChangeCauseRules(stateChangeFast, triggeredRulesName, deviceStateName);
		wholeAndCurrentChangeCauseRule.currentStateChangeCauseRules=currentChangeCauseRules;
		
		
		
		
		return wholeAndCurrentChangeCauseRule;
	}
	
	public StateChangeCauseRules getStateChangeCauseRules(StateChangeFast stateChange,List<DataTimeValue> triggeredRulesName,DeviceStateName deviceStateName) {
		DataAnalysisService dataAnalysisService=new DataAnalysisService();
		StateChangeCauseRules stateChangeCauseRules=new StateChangeCauseRules();
		StateNameRelativeRule startStateRelativeRule=dataAnalysisService.getStateNameRelativeRule(deviceStateName, stateChange.startTimeValue[1]+"");
		StateNameRelativeRule middleStateRelativeRule=dataAnalysisService.getStateNameRelativeRule(deviceStateName, stateChange.middleTimeValue[1]+"");
		StateNameRelativeRule endStateRelativeRule=dataAnalysisService.getStateNameRelativeRule(deviceStateName, stateChange.endTimeValue[1]+"");
		TimeStateRelativeRules startCaueseRule=new TimeStateRelativeRules();
		startCaueseRule.stateName=startStateRelativeRule.stateName;
		startCaueseRule.time=stateChange.startTimeValue[0];
		for(Rule rule:startStateRelativeRule.relativeRules) {
			for(DataTimeValue ruleTimeValue:triggeredRulesName) {
				if(ruleTimeValue.name.equals(rule.getRuleName())) {
					NameDataFunction ruleDataFunction=dataAnalysisService.getNameDataFunction(ruleTimeValue);
					for(DataFunction dataFunction:ruleDataFunction.dataFunctions) {
						double downValue=dataFunction.downValue;
						double upValue=dataFunction.upValue;
						if(downValue==1||upValue==1) {
							if(dataFunction.downTime<=stateChange.startTimeValue[0] && dataFunction.upTime>=stateChange.startTimeValue[0]) {
								startCaueseRule.relativeRules.add(rule);
								break;
							}
						}
						
					}
					break;
				}
			}
		}
		
		TimeStateRelativeRules middleCaueseRule=new TimeStateRelativeRules();
		middleCaueseRule.stateName=middleStateRelativeRule.stateName;
		middleCaueseRule.time=stateChange.middleTimeValue[0];
		for(Rule rule:middleStateRelativeRule.relativeRules) {
			for(DataTimeValue ruleTimeValue:triggeredRulesName) {
				if(ruleTimeValue.name.equals(rule.getRuleName())) {
					NameDataFunction ruleDataFunction=dataAnalysisService.getNameDataFunction(ruleTimeValue);
					for(DataFunction dataFunction:ruleDataFunction.dataFunctions) {
						double downValue=dataFunction.downValue;
						double upValue=dataFunction.upValue;
						if(downValue==1||upValue==1) {
							if(dataFunction.downTime<=stateChange.middleTimeValue[0] && dataFunction.upTime>=stateChange.middleTimeValue[0]) {
								middleCaueseRule.relativeRules.add(rule);
								break;
							}
						}
						
					}
					break;
				}
			}
		}
		
		TimeStateRelativeRules endCaueseRule=new TimeStateRelativeRules();
		endCaueseRule.stateName=endStateRelativeRule.stateName;
		endCaueseRule.time=stateChange.endTimeValue[0];
		for(Rule rule:endStateRelativeRule.relativeRules) {
			for(DataTimeValue ruleTimeValue:triggeredRulesName) {
				if(ruleTimeValue.name.equals(rule.getRuleName())) {
					NameDataFunction ruleDataFunction=dataAnalysisService.getNameDataFunction(ruleTimeValue);
					for(DataFunction dataFunction:ruleDataFunction.dataFunctions) {
						double downValue=dataFunction.downValue;
						double upValue=dataFunction.upValue;
						if(downValue==1||upValue==1) {
							if(dataFunction.downTime<=stateChange.endTimeValue[0] && dataFunction.upTime>=stateChange.endTimeValue[0]) {
								endCaueseRule.relativeRules.add(rule);
								break;
							}
						}
						
					}
					break;
				}
			}
		}
		
		stateChangeCauseRules.start=startCaueseRule;
		stateChangeCauseRules.middle=middleCaueseRule;
		stateChangeCauseRules.end=endCaueseRule;
		return stateChangeCauseRules;
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
