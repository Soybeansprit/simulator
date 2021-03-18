package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.dom4j.DocumentException;

import com.example.demo.bean.AllRuleAnalysisResult;
import com.example.demo.bean.ConflictTime;
import com.example.demo.bean.CountStatesCauseRule;
import com.example.demo.bean.CountStatesCauseRuleSceneName;
import com.example.demo.bean.DataTimeValue;
import com.example.demo.bean.DeviceAllSceneConflictRule;
import com.example.demo.bean.DeviceAllSceneFastChangeRule;
import com.example.demo.bean.DeviceAnalysResult;
import com.example.demo.bean.DeviceSceneConflictCauseRule;
import com.example.demo.bean.DeviceSceneFastChangeCauseRule;
import com.example.demo.bean.GenerateModelParameters;
import com.example.demo.bean.GraphNode;
import com.example.demo.bean.Rule;
import com.example.demo.bean.RuleAndCause;
import com.example.demo.bean.RuleCount;
import com.example.demo.bean.RuleCountSceneName;
import com.example.demo.bean.Scene;
import com.example.demo.bean.SceneConflictStateCauseRule;
import com.example.demo.bean.SceneFastChangeCauseRule;
import com.example.demo.bean.ScenesTree;
import com.example.demo.bean.StateCauseRule;
import com.example.demo.bean.StateCauseRuleCount;
import com.example.demo.bean.StateCauseRuleCountSceneName;
import com.example.demo.bean.StateChangeCauseRules;
import com.example.demo.bean.StateChangeFast;
import com.example.demo.bean.TimeStateRelativeRules;

public class RuleAnalysisService {

	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		SceneTreeService sceneTreeService=new SceneTreeService();
		RuleService ruleService=new RuleService();	
		SceneService sceneService=new SceneService();
		RuleAnalysisService ruleAnalysisService=new RuleAnalysisService();
		String ruleText="1. IF SmartHomeSecurity.homeMode AND temperature<=15 THEN M.heatonPulse\r\n" + 
				"\r\n" + 
				"2. IF SmartHomeSecurity.homeMode AND temperature>=30 THEN M.accoolPulse\r\n" + 
				"\r\n" + 
				"3. IF SmartHomeSecurity.homeMode AND humidity<20 THEN M.honPulse\r\n" + 
				"\r\n" + 
				"4. IF SmartHomeSecurity.homeMode AND humidity>=45 THEN M.hoffPulse\r\n" + 
				"\r\n" + 
				"5. IF SmartHomeSecurity.homeMode AND humidity>65 THEN M.fonPulse\r\n" + 
				"\r\n" + 
				"6. IF SmartHomeSecurity.homeMode AND temperature>28 THEN M.fonPulse\r\n" + 
				"\r\n" + 
				"7. IF SmartHomeSecurity.homeMode AND temperature<20 THEN M.foffPulse\r\n" + 
				"\r\n" + 
				"8. IF SmartHomeSecurity.homeMode AND rain=1 THEN M.phlwhitePulse\r\n" + 
				"\r\n" + 
				"9. IF SmartHomeSecurity.homeMode AND temperature<=10 THEN M.phlbluePulse\r\n" + 
				"\r\n" + 
				"10. IF SmartHomeSecurity.homeMode AND leak=1 THEN M.phlbluePulse\r\n" + 
				"\r\n" + 
				"11. IF SmartHomeSecurity.awayMode THEN M.phloffPulse\r\n" + 
				"\r\n" + 
				"12. IF SmartHomeSecurity.homeMode THEN M.phlwhitePulse\r\n" + 
				"\r\n" + 
				"13. IF Door.dopen THEN M.phlwhitePulse\r\n" + 
				"\r\n" + 
				"14. IF SmartHomeSecurity.homeMode AND co2ppm>=1000 THEN M.phlredPulse\r\n" + 
				"\r\n" + 
				"15. IF SmartHomeSecurity.awayMode THEN M.foffPulse\r\n" + 
				"\r\n" + 
				"16. IF Door.dopen THEN M.heatoffPulse\r\n" + 
				"\r\n" + 
				"17. IF Window.wopen THEN M.heatoffPulse\r\n" + 
				"\r\n" + 
				"18. IF SmartHomeSecurity.awayMode THEN M.heatoffPulse,M.acoffPulse,M.foffPulse,M.bclosePulse,M.boffPulse\r\n" + 
				"\r\n" + 
				"19. IF SmartHomeSecurity.homeMode AND temperature<18 THEN M.acheatPulse\r\n" + 
				"\r\n" + 
				"20. IF SmartHomeSecurity.homeMode AND temperature>30 THEN M.accoolPulse\r\n" + 
				"\r\n" + 
				"21. IF SmartHomeSecurity.homeMode THEN M.rdockPulse\r\n" + 
				"\r\n" + 
				"22. IF SmartHomeSecurity.awayMode THEN M.rstartPulse\r\n" + 
				"\r\n" + 
				"23. IF SmartHomeSecurity.awayMode THEN M.wclosePulse,M.dclosePulse\r\n" + 
				"\r\n" + 
				"24. IF number>0 THEN M.homeModePulse\r\n" + 
				"\r\n" + 
				"25. IF number=0 THEN M.awayModePulse\r\n" + 
				"\r\n" + 
				"26. IF SmartHomeSecurity.homeMode AND temperature>28 THEN M.bopenPulse\r\n" + 
				"\r\n" + 
				"27. IF SmartHomeSecurity.homeMode THEN M.bonPulse\r\n" + 
				"\r\n" + 
				"28. IF SmartHomeSecurity.homeMode AND co2ppm>=800 THEN M.fonPulse,M.wopenPulse\r\n" + 
				"\r\n" + 
				"29. IF AirConditioner.cool THEN M.wclosePulse\r\n" + 
				"\r\n" + 
				"30. IF AirConditioner.heat THEN M.wclosePulse";
		String initModelName="exp0108.xml";
		String simulationTime="300";
		List<Rule> rules=ruleService.getRuleList(ruleText);
		System.out.println(rules);
		
		GenerateModelParameters generateModelParameters=sceneTreeService.getAllSimulationModels(rules,"D:\\workspace", initModelName, simulationTime);
		List<Scene> scenes=new ArrayList<Scene>();
		int simulationDataNum=generateModelParameters.simulationDataNum;
		ScenesTree scenesTree=generateModelParameters.scenesTree;
		scenes=sceneService.getAllSimulationDataTimeValue(scenesTree, "D:\\workspace", initModelName, "D:\\tools\\uppaal-4.1.24\\uppaal-4.1.24\\bin-Windows", simulationDataNum);
		ruleAnalysisService.getAllRuleAnalysis(scenes, rules, "D:\\workspace", initModelName, simulationTime, "24", "300");

	}
	
	
	public AllRuleAnalysisResult getAllRuleAnalysis(List<Scene> scenes,List<Rule> rules,String filePath,String initFileName,String simulationTime,String equivalentTime,String intervalTime) throws DocumentException {
		///////////////////////先进行deviceAnalysisResult
		SceneService sceneService=new SceneService();
		List<Scene> newScenes=new ArrayList<Scene>();
		AllRuleAnalysisResult allRuleAnalysisResult=new AllRuleAnalysisResult();
		ToNode toNode=new ToNode();
		List<GraphNode> graphNodes=new ArrayList<GraphNode>();
		graphNodes=toNode.getGraphNodes("D:\\workspace", initFileName);
		System.out.println("filePath:"+filePath+" initFileName:"+initFileName+" simulationTime:"+simulationTime+" equivalentTime:"+equivalentTime+" intervalTime:"+intervalTime);
		for(Scene scene:scenes) {
			scene=sceneService.getDeviceAnalysisResult(scene, rules, simulationTime,"D:\\workspace", initFileName, equivalentTime, intervalTime);
			newScenes.add(scene);
		}
		
		List<RuleAndCause> rulesNeverTriggered=getRulesNeverTriggered(newScenes, rules,graphNodes);
		List<String> devicesWithConflict=getDevicesWithConflict(newScenes);
		List<String> devicesWithFastChange=getDevicesWithFastChange(newScenes);
		List<DeviceSceneConflictCauseRule> devicesSceneConflictCauseRule=new ArrayList<DeviceSceneConflictCauseRule>();
		List<DeviceSceneFastChangeCauseRule> devicesSceneFastChangeCauseRule=new ArrayList<DeviceSceneFastChangeCauseRule>();
		for(String deviceWithConflict:devicesWithConflict) {
			DeviceSceneConflictCauseRule deviceSceneConflictCauseRule=new DeviceSceneConflictCauseRule();
			deviceSceneConflictCauseRule.deviceName=deviceWithConflict;
			List<SceneConflictStateCauseRule> scenesConflcitStateCasueRule=getDeviceAllScenesConflictCauseRules(newScenes, deviceWithConflict, rules,graphNodes);
			deviceSceneConflictCauseRule.scenesConflcitStateCasueRule=scenesConflcitStateCasueRule;
			devicesSceneConflictCauseRule.add(deviceSceneConflictCauseRule);
		}
		for(String deviceWithFastChagne:devicesWithFastChange) {
			DeviceSceneFastChangeCauseRule deviceSceneFastChangeCauseRule=new DeviceSceneFastChangeCauseRule();
			deviceSceneFastChangeCauseRule.deviceName=deviceWithFastChagne;
			List<SceneFastChangeCauseRule> scenesFastChangeCauseRule=getDeviceAllScenesFastChangeCauseRules(newScenes, deviceWithFastChagne, rules,graphNodes);
			deviceSceneFastChangeCauseRule.scenesFastChangeCauseRule=scenesFastChangeCauseRule;
			devicesSceneFastChangeCauseRule.add(deviceSceneFastChangeCauseRule);
		}
		
		System.out.println();
		
		////////////////////////对所有场景进行整合//////////////////////////////
		List<DeviceAllSceneConflictRule> devicesAllSceneConflictRule=new ArrayList<DeviceAllSceneConflictRule>();
		List<DeviceAllSceneFastChangeRule> devicesAllSceneFastChangeRule=new ArrayList<DeviceAllSceneFastChangeRule>();
		for(DeviceSceneConflictCauseRule deviceSceneConflictCauseRule:devicesSceneConflictCauseRule) {
			DeviceAllSceneConflictRule deviceAllSceneConflictRule=new DeviceAllSceneConflictRule();
			List<CountStatesCauseRuleSceneName> allCountStateCauseRuleSceneName=new ArrayList<CountStatesCauseRuleSceneName>();
			for(SceneConflictStateCauseRule sceneConflictStateCauseRule:deviceSceneConflictCauseRule.scenesConflcitStateCasueRule) {
				for(CountStatesCauseRule sceneCountStatesCauseRule:sceneConflictStateCauseRule.conflictCauseRuleStatistic) {
//					Iterator<StateCauseRule> stateCauseRulesIterator=sceneCountStatesCauseRule.statesCauseRule.iterator();
//					while(stateCauseRulesIterator.hasNext()) {
//						StateCauseRule stateCauseRule=stateCauseRulesIterator.next();
//						if(stateCauseRule.causeRules.size()==0) {
//							stateCauseRulesIterator.remove();
//						}
//					}
					boolean noCauseRule=false;
					for(StateCauseRule stateCauseRule:sceneCountStatesCauseRule.statesCauseRule) {
						if(stateCauseRule.causeRules.size()==0) {
							noCauseRule=true;
							break;
						}
					}
					if(noCauseRule) {
						continue;
					}
					boolean exist=false;
					for(CountStatesCauseRuleSceneName countStateCauseRuleSceneName:allCountStateCauseRuleSceneName) {
						if(statesCauseRuleEqual(countStateCauseRuleSceneName.countStatesCauseRule.statesCauseRule, sceneCountStatesCauseRule.statesCauseRule)) {
							exist=true;
							countStateCauseRuleSceneName.countStatesCauseRule.count+=sceneCountStatesCauseRule.count;
							countStateCauseRuleSceneName.sceneNames.add(sceneConflictStateCauseRule.sceneName);
							break;
						}
					}
					if(!exist) {
						
						CountStatesCauseRuleSceneName countStateCauseRuleSceneName=new CountStatesCauseRuleSceneName();
						countStateCauseRuleSceneName.countStatesCauseRule.count=sceneCountStatesCauseRule.count;
						countStateCauseRuleSceneName.countStatesCauseRule.statesCauseRule=sceneCountStatesCauseRule.statesCauseRule;
						countStateCauseRuleSceneName.sceneNames.add(sceneConflictStateCauseRule.sceneName);
						allCountStateCauseRuleSceneName.add(countStateCauseRuleSceneName);
					}
				}
			}
			deviceAllSceneConflictRule.allCountStateCauseRuleSceneName=allCountStateCauseRuleSceneName;
			deviceAllSceneConflictRule.deviceName=deviceSceneConflictCauseRule.deviceName;
			devicesAllSceneConflictRule.add(deviceAllSceneConflictRule);
		}
		
		for(DeviceSceneFastChangeCauseRule deviceSceneFastChangeCauseRule:devicesSceneFastChangeCauseRule) {
			DeviceAllSceneFastChangeRule deviceAllSceneFastChangeRule=new DeviceAllSceneFastChangeRule();
			List<StateCauseRuleCountSceneName> allFastChangeStateCauseRuleCountSceneName=new ArrayList<StateCauseRuleCountSceneName>();
			for(SceneFastChangeCauseRule sceneFastChangeCauseRule:deviceSceneFastChangeCauseRule.scenesFastChangeCauseRule) {
				for(StateCauseRuleCount sceneStateCauseRuleCount:sceneFastChangeCauseRule.fastChangeStateCauseRuleCountList) {
					boolean exist=false;
					for(StateCauseRuleCountSceneName stateCauseRuleCountSceneName:allFastChangeStateCauseRuleCountSceneName) {
						if(stateCauseRuleCountSceneName.stateName.equals(sceneStateCauseRuleCount.stateName)) {
							exist=true;
							for(RuleCount ruleCount:sceneStateCauseRuleCount.rulesCount) {
								boolean existRule=false;
								for(RuleCountSceneName ruleCountExist:stateCauseRuleCountSceneName.rulesCountSceneName) {
									if(ruleCount.causeRule.selfRule.getRuleName().equals(ruleCountExist.ruleCount.causeRule.selfRule.getRuleName())) {
										existRule=true;
										ruleCountExist.ruleCount.count+=ruleCount.count;
										ruleCountExist.sceneNames.add(sceneFastChangeCauseRule.sceneName);
										break;
									}
								}
								if(!existRule) {
									RuleCountSceneName ruleCountExist=new RuleCountSceneName();
									ruleCountExist.ruleCount=ruleCount;
									ruleCountExist.sceneNames.add(sceneFastChangeCauseRule.sceneName);
									stateCauseRuleCountSceneName.rulesCountSceneName.add(ruleCountExist);
								}
							}
						}
					}
					if(!exist) {
						StateCauseRuleCountSceneName stateCauseRuleCountSceneName=new StateCauseRuleCountSceneName();
						stateCauseRuleCountSceneName.stateName=sceneStateCauseRuleCount.stateName;
						for(RuleCount ruleCount:sceneStateCauseRuleCount.rulesCount) {
							RuleCountSceneName ruleCountExist=new RuleCountSceneName();
							ruleCountExist.ruleCount=ruleCount;
							ruleCountExist.sceneNames.add(sceneFastChangeCauseRule.sceneName);
							stateCauseRuleCountSceneName.rulesCountSceneName.add(ruleCountExist);
						}
						
						
						
						allFastChangeStateCauseRuleCountSceneName.add(stateCauseRuleCountSceneName);
					}
				}
			}
			deviceAllSceneFastChangeRule.allFastChangeStateCauseRuleCountSceneName=allFastChangeStateCauseRuleCountSceneName;
			deviceAllSceneFastChangeRule.deviceName=deviceSceneFastChangeCauseRule.deviceName;
			devicesAllSceneFastChangeRule.add(deviceAllSceneFastChangeRule);
		}
		allRuleAnalysisResult.scenes=newScenes;
		allRuleAnalysisResult.rulesNeverTriggered=rulesNeverTriggered;
		allRuleAnalysisResult.devicesAllSceneConflictRule=devicesAllSceneConflictRule;
		allRuleAnalysisResult.devicesAllSceneFastChangeRule=devicesAllSceneFastChangeRule;
		allRuleAnalysisResult.devicesSceneConflictCauseRule=devicesSceneConflictCauseRule;
		allRuleAnalysisResult.devicesSceneFastChangeCauseRule=devicesSceneFastChangeCauseRule;
//		for(DeviceSceneConflictCauseRule deviceSceneConflictCauseRule:devicesSceneConflictCauseRule) {
//			DeviceAllSceneConflictRule deviceAllSceneConflictRule=new DeviceAllSceneConflictRule();
//			List<CountStatesCauseRule> allCountStateCauseRule=new ArrayList<CountStatesCauseRule>();
//			for(SceneConflictStateCauseRule sceneConflictStateCauseRule:deviceSceneConflictCauseRule.scenesConflcitStateCasueRule) {
//				for(CountStatesCauseRule sceneCountStatesCauseRule:sceneConflictStateCauseRule.conflictCauseRuleStatistic) {
//					boolean exist=false;
//					for(CountStatesCauseRule countStateCauseRule:allCountStateCauseRule) {
//						if(statesCauseRuleEqual(countStateCauseRule.statesCauseRule, sceneCountStatesCauseRule.statesCauseRule)) {
//							exist=true;
//							countStateCauseRule.count+=sceneCountStatesCauseRule.count;
//							break;
//						}
//					}
//					if(!exist) {
//						CountStatesCauseRule countStateCauseRule=new CountStatesCauseRule();
//						countStateCauseRule.count=sceneCountStatesCauseRule.count;
//						countStateCauseRule.statesCauseRule=sceneCountStatesCauseRule.statesCauseRule;
//						allCountStateCauseRule.add(countStateCauseRule);
//					}
//				}
//			}
//			deviceAllSceneConflictRule.allCountStateCauseRule=allCountStateCauseRule;
//			deviceAllSceneConflictRule.deviceName=deviceSceneConflictCauseRule.deviceName;
//			devicesAllSceneConflictRule.add(deviceAllSceneConflictRule);
//		}
		
//		for(DeviceSceneFastChangeCauseRule deviceSceneFastChangeCauseRule:devicesSceneFastChangeCauseRule) {
//			DeviceAllSceneFastChangeRule deviceAllSceneFastChangeRule=new DeviceAllSceneFastChangeRule();
//			List<StateCauseRuleCount> allFastChangeStateCauseRuleCountList=new ArrayList<StateCauseRuleCount>();
//			for(SceneFastChangeCauseRule sceneFastChangeCauseRule:deviceSceneFastChangeCauseRule.scenesFastChangeCauseRule) {
//				for(StateCauseRuleCount sceneStateCauseRuleCount:sceneFastChangeCauseRule.fastChangeStateCauseRuleCountList) {
//					boolean exist=false;
//					for(StateCauseRuleCount stateCauseRuleCount:allFastChangeStateCauseRuleCountList) {
//						if(stateCauseRuleCount.stateName.equals(sceneStateCauseRuleCount.stateName)) {
//							exist=true;
//							for(RuleCount ruleCount:sceneStateCauseRuleCount.rulesCount) {
//								boolean existRule=false;
//								for(RuleCount ruleCountExist:stateCauseRuleCount.rulesCount) {
//									if(ruleCount.rule.getRuleName().equals(ruleCountExist.rule.getRuleName())) {
//										existRule=true;
//										ruleCountExist.count+=ruleCount.count;
//										break;
//									}
//								}
//								if(!existRule) {
//									stateCauseRuleCount.rulesCount.add(ruleCount);
//								}
//							}
//						}
//					}
//					if(!exist) {
//						allFastChangeStateCauseRuleCountList.add(sceneStateCauseRuleCount);
//					}
//				}
//			}
//			deviceAllSceneFastChangeRule.allFastChangeStateCauseRuleCountList=allFastChangeStateCauseRuleCountList;
//			deviceAllSceneFastChangeRule.deviceName=deviceSceneFastChangeCauseRule.deviceName;
//			devicesAllSceneFastChangeRule.add(deviceAllSceneFastChangeRule);
//		}
		System.out.println();
		return allRuleAnalysisResult;
		
	}
	
	/////////////////////////RulesNerverTriggered//////////
	public List<RuleAndCause> getRulesNeverTriggered(List<Scene> scenes,List<Rule> rules,List<GraphNode> graphNodes){
		List<RuleAndCause> rulesNeverTriggered=new ArrayList<RuleAndCause>();
		SceneService sceneService=new SceneService();
		for(Rule rule:rules) {
			boolean canTriggered=false;
			second:
			for(Scene scene:scenes) {
				for(DataTimeValue triggeredRuleName:scene.getTriggeredRulesName()) {
					if(rule.getRuleName().equals(triggeredRuleName.name)) {
						canTriggered=true;
						break second;
					}
				}
			}
			if(!canTriggered) {
				RuleAndCause ruleAndCause=sceneService.getRuleCauseRulesfromIFDGraph(graphNodes, rules, rule.getRuleName());
				rulesNeverTriggered.add(ruleAndCause);
			}
				
		}
		return rulesNeverTriggered;
	}
	 /////////////////////////DevicesWithConflict///////////////
	public List<String> getDevicesWithConflict(List<Scene> scenes){
		List<String> devicesWithConflict=new ArrayList<String>();
		for(DeviceAnalysResult deviceAnalysisResult:scenes.get(0).getDevicesAnalysResults()) {
			String deviceName=deviceAnalysisResult.deviceName;
			boolean existConflict=false;
			second:
			for(Scene scene:scenes) {
				third:
				for(DeviceAnalysResult deviceAnalysResult:scene.getDevicesAnalysResults()) {
					if(deviceAnalysResult.deviceName.equals(deviceName)) {
						if(deviceAnalysResult.statesConflict.hasConflict) {
							existConflict=true;
							break second;
						}
						break third;
					}
				}
			}
			if(existConflict) {
				devicesWithConflict.add(deviceName);
			}
		}
		return devicesWithConflict;
	}
	//////////////////////////DevicesWithFastChange//////////////////////
	public List<String> getDevicesWithFastChange(List<Scene> scenes){
		List<String> devicesWithFastChange=new ArrayList<String>();
		for(DeviceAnalysResult deviceAnalysisResult:scenes.get(0).getDevicesAnalysResults()) {
			String deviceName=deviceAnalysisResult.deviceName;
			boolean existFastChange=false;
			second:
			for(Scene scene:scenes) {
				third:
				for(DeviceAnalysResult deviceAnalysResult:scene.getDevicesAnalysResults()) {
					if(deviceAnalysResult.deviceName.equals(deviceName)) {
						if(deviceAnalysResult.statesChange.stateChangeFasts.size()>0) {
							existFastChange=true;
							break second;
						}
						break third;
					}
					
				}
			}
			if(existFastChange) {
				devicesWithFastChange.add(deviceName);
			}
		}
		return devicesWithFastChange;
	}
	
	//////////////////DeviceConflictCauseRulesAnalysis///////////////////////////
	public List<SceneConflictStateCauseRule> getDeviceAllScenesConflictCauseRules(List<Scene> scenes,String deviceName,List<Rule> rules,List<GraphNode> graphNodes) {
		/////////////先分别把每个场景所有冲突的原因找出///////////////////////
		/////////////再对每个场景的所有冲突原因进行统计分析///////////////////////
		SceneService sceneService=new SceneService();
		List<SceneConflictStateCauseRule> scenesConflictStateCauseRule=new ArrayList<SceneConflictStateCauseRule>();
		for(Scene scene:scenes) {
			SceneConflictStateCauseRule sceneConflictStateCauseRule=new SceneConflictStateCauseRule();			
			String sceneName=scene.getSceneName();
			sceneConflictStateCauseRule.sceneName=sceneName;
			second:
			for(DeviceAnalysResult deviceAnalysResult:scene.getDevicesAnalysResults()) {
				if(deviceAnalysResult.deviceName.equals(deviceName)) {
					if(deviceAnalysResult.statesConflict.hasConflict) {
						List<List<StateCauseRule>> sceneConflictsStatesCauseRule=new ArrayList<List<StateCauseRule>>();
						/////////////先分别把每个场景所有冲突的原因找出///////////////////////
						for(ConflictTime conflictTime:deviceAnalysResult.statesConflict.conflictTimes) {
							List<StateCauseRule> conflictStatesCauseRule=sceneService.getStateCauseRules(conflictTime, scene.getTriggeredRulesName(), deviceAnalysResult.deviceStateName, rules,graphNodes);
							sceneConflictsStatesCauseRule.add(conflictStatesCauseRule);
						}
						/////////////再对每个场景的所有冲突原因进行统计分析///////////////////////
						List<CountStatesCauseRule> conflictCauseRuleStatistic=getDeviceConflictCauseRuleStatistic(sceneConflictsStatesCauseRule);
						sceneConflictStateCauseRule.conflictCauseRuleStatistic=conflictCauseRuleStatistic;
					}
					break second;
				}
			}
			System.out.println(sceneName);
			scenesConflictStateCauseRule.add(sceneConflictStateCauseRule);
		}
		return scenesConflictStateCauseRule;
	}
	
	///////////////////统计计数
	public List<CountStatesCauseRule> getDeviceConflictCauseRuleStatistic(List<List<StateCauseRule>> sceneConflictsStatesCauseRule) {
		List<CountStatesCauseRule> conflictCauseRuleStatistic=new ArrayList<CountStatesCauseRule>();
		for(List<StateCauseRule> stateCauseRules:sceneConflictsStatesCauseRule) {
			List<StateCauseRule> newStateCauseRules=removeContraRules(stateCauseRules);
			if(newStateCauseRules.size()<=1) {
				continue;
			}
			boolean exist=false;
			for(CountStatesCauseRule conflictCauseRule:conflictCauseRuleStatistic) {
				if(statesCauseRuleEqual(conflictCauseRule.statesCauseRule, newStateCauseRules)) {
					conflictCauseRule.count++;
					exist=true;
					break;
				}
			}
			if(!exist) {
				CountStatesCauseRule conflictCauseRule=new CountStatesCauseRule();
				conflictCauseRule.count=1;
				conflictCauseRule.statesCauseRule=newStateCauseRules;
				conflictCauseRuleStatistic.add(conflictCauseRule);
			}
		}
		return conflictCauseRuleStatistic;
	}
	
	public List<StateCauseRule> removeContraRules(List<StateCauseRule> stateCauseRules){
		List<StateCauseRule> newStateCauseRules=stateCauseRules;
		StateCauseRule stateCauseRule=new StateCauseRule();
		boolean assign=false;
		for(StateCauseRule stateCauseRule1:newStateCauseRules) {
			if(stateCauseRule1.causeRules.size()==1) {
				stateCauseRule=stateCauseRule1;
				assign=true;
				break;
			}
		}
		if(!assign) {
			for(StateCauseRule stateCauseRule1:newStateCauseRules) {
				boolean existContra=false;
				forth:
				for(int n=0;n<stateCauseRule1.causeRules.size();n++) {
					for(int m=0;m<stateCauseRule1.causeRules.size();m++) {
						if(m!=n) {
							if(ruleExistContra(stateCauseRule1.causeRules.get(n), stateCauseRule1.causeRules.get(m))) {
								existContra=true;
								break forth;
							}
						}
					}
				}
				if(!existContra) {
					stateCauseRule=stateCauseRule1;
					assign=true;
					break;
				}
			}
		}
		if(assign) {
			Iterator<StateCauseRule> stateCauseRulesIterator=newStateCauseRules.iterator();
			while(stateCauseRulesIterator.hasNext()) {
				StateCauseRule stateCauseRule1=stateCauseRulesIterator.next();
				if(!stateCauseRule1.stateName.equals(stateCauseRule.stateName)) {
					Iterator<RuleAndCause> causeRules=stateCauseRule1.causeRules.iterator();
					while(causeRules.hasNext()) {
						RuleAndCause rule2=causeRules.next();
						for(RuleAndCause rule1:stateCauseRule.causeRules) {
							if(ruleExistContra(rule1,rule2)) {
								////////////remove
								System.out.println("exsitContra");
								System.out.println(rule1.selfRule.getRuleName()+"existContra");
								causeRules.remove();
								if(stateCauseRule1.causeRules.size()==0) {
									stateCauseRulesIterator.remove();
								}
								break;
							}
						}
						
					}
				}
			}
			
		}
		return newStateCauseRules;
		
	}
	
	public List<SceneFastChangeCauseRule> getDeviceAllScenesFastChangeCauseRules(List<Scene> scenes,String deviceName,List<Rule> rules,List<GraphNode> graphNodes) {
		/////////////////先分别把每个场景的所有频繁变化原因找出///////////////////////
		///////////////////再对每个场景的所有频繁变化进行统计分析///////////////////////
		SceneService sceneService=new SceneService();
		List<SceneFastChangeCauseRule> scenesFastChangeCauseRule=new ArrayList<SceneFastChangeCauseRule>();
		for(Scene scene:scenes) {
			SceneFastChangeCauseRule sceneFastChangeCauseRule=new SceneFastChangeCauseRule();
			sceneFastChangeCauseRule.sceneName=scene.getSceneName();
			second:
			for(DeviceAnalysResult deviceAnalysResult:scene.getDevicesAnalysResults()) {
				if(deviceAnalysResult.deviceName.equals(deviceName)) {
					if(deviceAnalysResult.statesChange.stateChangeFasts.size()>0) {
						List<StateChangeCauseRules> stateChangesCauseRules=new ArrayList<StateChangeCauseRules>();
						/////////////////先分别把每个场景的所有频繁变化原因找出///////////////////////
						for(StateChangeFast stateChangeFast:deviceAnalysResult.statesChange.stateChangeFasts) {
							StateChangeCauseRules stateChangeCauseRules=new StateChangeCauseRules();
							stateChangeCauseRules=sceneService.getStateChangeCauseRules(stateChangeFast, scene.getTriggeredRulesName(), deviceAnalysResult.deviceStateName);
							stateChangesCauseRules.add(stateChangeCauseRules);
						}
						///////////////////再对每个场景的所有频繁变化进行统计分析///////////////////////
						List<List<TimeStateRelativeRules>> timeStateRelativeRulesListList=new ArrayList<List<TimeStateRelativeRules>>();
						for(int i=0;i<stateChangesCauseRules.size();) {
							StateChangeCauseRules stateChangeCauseRules=stateChangesCauseRules.get(i);
							List<TimeStateRelativeRules> timeStateRelativeRulesList=new ArrayList<TimeStateRelativeRules>();
							timeStateRelativeRulesList.add(stateChangeCauseRules.start);
							timeStateRelativeRulesList.add(stateChangeCauseRules.middle);
							timeStateRelativeRulesList.add(stateChangeCauseRules.end);
							if(i<stateChangesCauseRules.size()-1) {
								int j=i+1;
								for(;j<stateChangesCauseRules.size();j++) {
									StateChangeCauseRules nextStateChangeCauseRules=stateChangesCauseRules.get(j);
									i=j;
									if(nextStateChangeCauseRules.start.time<stateChangeCauseRules.end.time) {
										timeStateRelativeRulesList.add(nextStateChangeCauseRules.end);
										stateChangeCauseRules=nextStateChangeCauseRules;
									}else {
										timeStateRelativeRulesListList.add(timeStateRelativeRulesList);
										break;
									}
								}
								if(j==stateChangesCauseRules.size()) {
									timeStateRelativeRulesListList.add(timeStateRelativeRulesList);
									i=j;
								}
							}else {
								timeStateRelativeRulesListList.add(timeStateRelativeRulesList);
								i++;
							}
						}
						////////////////////对timeStateRelativeRulesListList统计分析
						List<StateCauseRuleCount> fastChangeStateCauseRuleCountList=getDeviceFastChagneCauseRuleStatetistic(timeStateRelativeRulesListList,graphNodes,rules);
						sceneFastChangeCauseRule.fastChangeStateCauseRuleCountList=fastChangeStateCauseRuleCountList;
					}
					break second;
				}
			}
			scenesFastChangeCauseRule.add(sceneFastChangeCauseRule);
		}
		return scenesFastChangeCauseRule;
		
	}
	
	public List<StateCauseRuleCount> getDeviceFastChagneCauseRuleStatetistic(List<List<TimeStateRelativeRules>> timeStateRelativeRulesListList,List<GraphNode> graphNodes,List<Rule> rules){
		/////////////////对每条引起状态频繁变化的规则计数
		SceneService sceneService=new SceneService();
		List<StateCauseRuleCount> fastChangeStateCauseRuleCountList=new ArrayList<StateCauseRuleCount>();
		for(List<TimeStateRelativeRules> timeStateRelativeRulesList:timeStateRelativeRulesListList) {
			for(int i=0;i<timeStateRelativeRulesList.size();i++) {
				if(i==0) {
					continue;
				}
				TimeStateRelativeRules timeStateRelativeRules=timeStateRelativeRulesList.get(i);
				boolean ruleExistContra=false;
				third:
				for(int n=0;n<timeStateRelativeRules.relativeRules.size();n++) {
					for(int m=0;m<timeStateRelativeRules.relativeRules.size();m++) {
						if(n!=m) {
							if(ruleExistContra(timeStateRelativeRules.relativeRules.get(n), timeStateRelativeRules.relativeRules.get(m))) {
								ruleExistContra=true;
								break third;
							}
						}
					}
				}
				if(ruleExistContra) {
					continue;
				}
				boolean exist=false;
				for(StateCauseRuleCount stateCauseRuleCount:fastChangeStateCauseRuleCountList) {
					if(stateCauseRuleCount.stateName.equals(timeStateRelativeRules.stateName)) {
						/////////////如果已经有了再看规则//////////
						exist=true;
//						boolean ruleExistContra=false;
						for(Rule rule:timeStateRelativeRules.relativeRules) {
							boolean ruleExist=false;
							for(RuleCount ruleCount:stateCauseRuleCount.rulesCount) {
								if(rule.getRuleName().equals(ruleCount.causeRule.selfRule.getRuleName())) {
									ruleExist=true;
									ruleCount.count++;
									break;
								}
							}
							if(!ruleExist) {
								RuleCount ruleCount=new RuleCount();
								RuleAndCause ruleAndCause=sceneService.getRuleCauseRulesfromIFDGraph(graphNodes, rules, rule.getRuleName());
								
								for(GraphNode graphNode:graphNodes) {
									graphNode.flag=false;
								}
								ruleCount.causeRule=ruleAndCause;
								ruleCount.count=1;
								stateCauseRuleCount.rulesCount.add(ruleCount);
							}
						}
						break;
					}
				}
				if(!exist) {
					StateCauseRuleCount stateCauseRuleCount=new StateCauseRuleCount();
					stateCauseRuleCount.stateName=timeStateRelativeRules.stateName;
					for(Rule rule:timeStateRelativeRules.relativeRules) {
						boolean ruleExist=false;
						for(RuleCount ruleCount:stateCauseRuleCount.rulesCount) {
							if(rule.getRuleName().equals(ruleCount.causeRule.selfRule.getRuleName())) {
								ruleExist=true;
								ruleCount.count++;
								break;
							}
						}
						if(!ruleExist) {
							RuleCount ruleCount=new RuleCount();
							RuleAndCause ruleAndCause=sceneService.getRuleCauseRulesfromIFDGraph(graphNodes, rules, rule.getRuleName());
							
							for(GraphNode graphNode:graphNodes) {
								graphNode.flag=false;
							}
							ruleCount.causeRule=ruleAndCause;
							ruleCount.count=1;
							stateCauseRuleCount.rulesCount.add(ruleCount);
						}
					}
//					for(int j=i;j<timeStateRelativeRulesList.size();j++) {
//						TimeStateRelativeRules newTimeStateRelativeRules=timeStateRelativeRulesList.get(j);
//						if(newTimeStateRelativeRules.stateName.equals(stateCauseRuleCount.stateName)) {
////							boolean ruleExistContra=false;
////							forth:
////							for(int n=0;n<newTimeStateRelativeRules.relativeRules.size();n++) {
////								for(int m=0;m<newTimeStateRelativeRules.relativeRules.size();m++) {
////									if(n!=m) {
////										if(ruleExistContra(newTimeStateRelativeRules.relativeRules.get(n), newTimeStateRelativeRules.relativeRules.get(m))) {
////											ruleExistContra=true;
////											break forth;
////										}
////									}
////								}
////							}
////							if(ruleExistContra) {
////								continue;
////							}
//							for(Rule rule:newTimeStateRelativeRules.relativeRules) {
//								boolean  ruleExist=false;
//								for(RuleCount ruleCount:stateCauseRuleCount.rulesCount) {
//									if(rule.getRuleName().equals(ruleCount.rule.getRuleName())) {
//										ruleExist=true;
//										ruleCount.count++;
//										break;
//									}
//								}
//								if(!ruleExist) {
//									RuleCount ruleCount=new RuleCount();
//									ruleCount.rule=rule;
//									ruleCount.count=1;
//									stateCauseRuleCount.rulesCount.add(ruleCount);
//								}
//							}
//						}
//					}
					fastChangeStateCauseRuleCountList.add(stateCauseRuleCount);
				}
			}
		}
		return fastChangeStateCauseRuleCountList;
	}
	
//////////////////////////判断当前冲突的各个状态是否都有相应规则导致，如无则不显示
	public boolean conflictStatesHaveCauseRules(List<StateCauseRule> stateCauseRules) {
		for(StateCauseRule stateCauseRule:stateCauseRules) {
			if(stateCauseRule.causeRules.size()==0) {
				return false;
			}
		}
		return true;
	}
	
	public boolean statesCauseRuleEqual(List<StateCauseRule> stateCauseRules1,List<StateCauseRule> stateCauseRules2) {
		if(stateCauseRules1.size()!=stateCauseRules2.size()) {
			return false;
		}
		for(StateCauseRule stateCauseRule1:stateCauseRules1) {
			boolean exsit=false;
			for(StateCauseRule stateCauseRule2:stateCauseRules2) {
				if(stateCauseRule1.stateName.equals(stateCauseRule2.stateName)) {
					if(causeRuleEqual(stateCauseRule1.causeRules, stateCauseRule2.causeRules)) {
						exsit=true;
						break;
					}

				}
			}
			if(!exsit) {
				return false;
			}
		}
		return true;
	}
	
	public boolean causeRuleEqual(List<RuleAndCause> rules1,List<RuleAndCause> rules2) {
		if(rules1.size()!=rules2.size()) {
			return false;
		}
		for(RuleAndCause rule1:rules1) {
			boolean exist=false;
			for(RuleAndCause rule2:rules2) {
				if(rule1.selfRule.getRuleName().equals(rule2.selfRule.getRuleName())) {
					exist=true;
					break;
				}
			}
			if(!exist) {
				return false;
			}
		}
		return true;
	}
	
	public boolean ruleEqual(List<Rule> rules1,List<Rule> rules2) {
		if(rules1.size()!=rules2.size()) {
			return false;
		}
		for(Rule rule1:rules1) {
			boolean exist=false;
			for(Rule rule2:rules2) {
				if(rule1.getRuleName().equals(rule2.getRuleName())) {
					exist=true;
					break;
				}
			}
			if(!exist) {
				return false;
			}
		}
		return true;
	}
	
	public boolean ruleExistContra(RuleAndCause rule1,RuleAndCause rule2) {
		TGraphToDot toDot=new TGraphToDot();
		List<String> triggers1=rule1.selfRule.getTrigger();
		List<String> triggers2=rule2.selfRule.getTrigger();
		List<String[]> attrVals1=new ArrayList<String[]>();
		for(String trigger:triggers1) {
			attrVals1.add(toDot.getAttrVal(trigger));
		}
		for(String trigger:triggers2) {
			String[] attrVal2=toDot.getAttrVal(trigger);
			for(String[] attrVal1:attrVals1) {
				if(triggerExistContra(attrVal1, attrVal2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean ruleExistContra(Rule rule1,Rule rule2) {
		TGraphToDot toDot=new TGraphToDot();
		List<String> triggers1=rule1.getTrigger();
		List<String> triggers2=rule2.getTrigger();
		List<String[]> attrVals1=new ArrayList<String[]>();
		for(String trigger:triggers1) {
			attrVals1.add(toDot.getAttrVal(trigger));
		}
		for(String trigger:triggers2) {
			String[] attrVal2=toDot.getAttrVal(trigger);
			for(String[] attrVal1:attrVals1) {
				if(triggerExistContra(attrVal1, attrVal2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	////////////////////////判断rule的trigger是否矛盾
	public boolean triggerExistContra(String[] attrVal1,String[] attrVal2) {
		if(attrVal1[0].equals(attrVal2[0])) {
			if(attrVal1[1].equals(".")) {
				if(!attrVal1[2].equals(attrVal2[2])) {
			          /////////////////////都是同一设备状态，但状态不同
					return true;
				}
			}else {
				double val1=Double.parseDouble(attrVal1[2]);
				double val2=Double.parseDouble(attrVal2[2]);
				if(attrVal1[1].equals("=")) {
					if(attrVal2[1].equals("=")) {
						if(!attrVal1[2].equals(attrVal2[2])) {
							return true;
						}
						
					}
					if(attrVal2[1].equals(">")) {
						if(val2>=val1) {
							return true;
						}
					}
					if(attrVal2[1].equals("<")) {
						if(val2<=val1) {
							return true;
						}
					}
				}
				if(attrVal2[1].equals("=")) {
					if(attrVal1[1].equals("<")) {
						if(val2>=val1) {
							return true;
						}
					}
					if(attrVal1[1].equals(">")) {
						if(val2<=val1) {
							return true;
						}
					}
				}
				if(attrVal1[1].equals(">")) {
					if(attrVal2[1].equals("<")) {
						if(val1>=val2) {
							return true;
						}
					}
				}
				if(attrVal2[1].equals(">")) {
					if(attrVal1[1].equals("<")) {
						if(val1<=val2) {
							return true;
						}
						
					}
				}
			}
		}
		return false;
	}

}
