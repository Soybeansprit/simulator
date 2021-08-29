package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.Iterator;
import java.util.LinkedList;

import org.dom4j.DocumentException;

import com.example.demo.bean.Action;
import com.example.demo.bean.AllRuleAnalysisResult;
import com.example.demo.bean.ConflictTime;
import com.example.demo.bean.CountStatesCauseRule;
import com.example.demo.bean.CountStatesCauseRuleSceneName;
import com.example.demo.bean.DataTimeValue;
import com.example.demo.bean.Device;
import com.example.demo.bean.DeviceAllSceneConflictRule;
import com.example.demo.bean.DeviceAllSceneFastChangeRule;
import com.example.demo.bean.DeviceAnalysResult;
import com.example.demo.bean.DeviceSceneConflictCauseRule;
import com.example.demo.bean.DeviceSceneFastChangeCauseRule;
import com.example.demo.bean.ErrorReason;
import com.example.demo.bean.GenerateModelParameters;
import com.example.demo.bean.GraphNode;
import com.example.demo.bean.GraphNodeArrow;
import com.example.demo.bean.PropertyAnalysis;
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
import com.example.demo.bean.StaticAnalysisResult;
import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.TemplGraphNode;
import com.example.demo.bean.TemplTransition;
import com.example.demo.bean.TimeStateRelativeRules;

public class RuleAnalysisService {

	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		SceneTreeService sceneTreeService=new SceneTreeService();
		RuleService ruleService=new RuleService();	
		SceneService sceneService=new SceneService();
		RuleAnalysisService ruleAnalysisService=new RuleAnalysisService();
		String ruleText="1. IF SmartHomeSecurity.homeMode AND temperature<=15 THEN Heater.turn_heat_on\r\n" + 
				"\r\n" + 
				"2. IF SmartHomeSecurity.homeMode AND temperature>=30 THEN AirConditioner.turn_ac_cool\r\n" + 
				"\r\n" + 
				"3. IF SmartHomeSecurity.homeMode AND humidity<20 THEN Humidifier.turn_hum_on\r\n" + 
				"\r\n" + 
				"4. IF SmartHomeSecurity.homeMode AND humidity>=45 THEN Humidifier.turn_hum_off\r\n" + 
				"\r\n" + 
				"5. IF SmartHomeSecurity.homeMode AND humidity>65 THEN Fan.turn_fan_on\r\n" + 
				"\r\n" + 
				"6. IF SmartHomeSecurity.homeMode AND temperature>28 THEN Fan.turn_fan_on\r\n" + 
				"\r\n" + 
				"7. IF SmartHomeSecurity.homeMode AND temperature<20 THEN Fan.turn_fan_off\r\n" + 
				"\r\n" + 
				"8. IF SmartHomeSecurity.homeMode AND rain=1 THEN PhilipsHueLight.turn_phl_white\r\n" + 
				"\r\n" + 
				"9. IF SmartHomeSecurity.homeMode AND temperature<=10 THEN PhilipsHueLight.turn_phl_blue\r\n" + 
				"\r\n" + 
				"10. IF SmartHomeSecurity.homeMode AND leak=1 THEN PhilipsHueLight.turn_phl_blue\r\n" + 
				"\r\n" + 
				"11. IF SmartHomeSecurity.awayMode THEN PhilipsHueLight.turn_phl_off\r\n" + 
				"\r\n" + 
				"12. IF SmartHomeSecurity.homeMode THEN PhilipsHueLight.turn_phl_white\r\n" + 
				"\r\n" + 
				"13. IF Door.dopen THEN PhilipsHueLight.turn_phl_white\r\n" + 
				"\r\n" + 
				"14. IF SmartHomeSecurity.homeMode AND co2ppm>=1000 THEN PhilipsHueLight.turn_phl_red\r\n" + 
				"\r\n" + 
				"15. IF SmartHomeSecurity.awayMode THEN Fan.turn_fan_on\r\n" + 
				"\r\n" + 
				"16. IF Door.dopen THEN Heater.turn_heat_off\r\n" + 
				"\r\n" + 
				"17. IF Window.wopen THEN Heater.turn_heat_off\r\n" + 
				"\r\n" + 
				"18. IF SmartHomeSecurity.awayMode THEN Heater.turn_heat_off,AirConditioner.turn_ac_off,Fan.turn_fan_off,Blind.close_blind,Bulb.turn_bulb_off\r\n" + 
				"\r\n" + 
				"19. IF SmartHomeSecurity.homeMode AND temperature<18 THEN AirConditioner.turn_ac_heat\r\n" + 
				"\r\n" + 
				"20. IF SmartHomeSecurity.homeMode AND temperature>30 THEN AirConditioner.turn_ac_cool\r\n" + 
				"\r\n" + 
				"21. IF SmartHomeSecurity.homeMode THEN Robot.dock_robot\r\n" + 
				"\r\n" + 
				"22. IF SmartHomeSecurity.awayMode THEN Robot.start_robot\r\n" + 
				"\r\n" + 
				"23. IF SmartHomeSecurity.awayMode THEN Window.close_window,Door.close_door\r\n" + 
				"\r\n" + 
				"24. IF number>0 THEN SmartHomeSecurity.turn_sms_home\r\n" + 
				"\r\n" + 
				"25. IF number=0 THEN SmartHomeSecurity.turn_sms_away\r\n" + 
				"\r\n" + 
				"26. IF SmartHomeSecurity.homeMode AND temperature>28 THEN Blind.open_blind\r\n" + 
				"\r\n" + 
				"27. IF SmartHomeSecurity.homeMode THEN Bulb.turn_bulb_on\r\n" + 
				"\r\n" + 
				"28. IF SmartHomeSecurity.homeMode AND co2ppm>=800 THEN Fan.turn_fan_on,Window.open_window\r\n" + 
				"\r\n" + 
				"29. IF AirConditioner.cool THEN Window.close_window\r\n" + 
				"\r\n" + 
				"30. IF AirConditioner.heat THEN Window.close_window\r\n"+
				"\r\n" +
				"19. IF SmartHomeSecurity.homeMode AND temperature<18 THEN AirConditioner.turn_ac_heat\r\n"+ 
				"\r\n" + 
				"19. IF temperature>20 AND temperature<18 THEN AirConditioner.turn_heat_ac\r\n" + 
				"\r\n" + 
				"19. IF SmartHomeSecurity.homeMode AND SamrtHomeSecurity.awayMode THEN AirConditioner.turn_ac_heat\r\n"+ 
				"\r\n" + 
				"19. IF number=0 AND number>0 THEN AirConditioner.turn_ac_heat\r\n"+ 
				"\r\n" + 
				"19. IF temperature<18 AND temperature>21 THEN AirConditioner.turn_ac_heat\r\n"+
				"\r\n"+
				"IF temperature>18 THEN Window.open_window,AirConditioner.turn_ac_heat\r\n"+
				"\r\n"+
				"IF temperature>18 THEN Window.open_window\r\n"+
				"\r\n"+
				"IF temperature>18 THEN Fan.turn_fan_on\r\n"+
				"\r\n"+
				"IF Fan.fon THEN AirConditioner.turn_ac_heat\r\n";
		String initModelName="exp0108.xml";
		String simulationTime="300";
		List<Rule> rules=ruleService.getRuleList(ruleText);
		System.out.println(rules);
		StaticAnalysisResult staticAnalysisResult=getRequirementError(rules, "exp0108-person-dif-new.xml","D:\\workspace");
		List<Rule> newRules=deleteRepeat(rules);
		GenerateModelParameters generateModelParameters=sceneTreeService.getAllSimulationModels(rules,"D:\\workspace", initModelName, simulationTime);
		List<Scene> scenes=new ArrayList<Scene>();
		int simulationDataNum=generateModelParameters.simulationDataNum;
		ScenesTree scenesTree=generateModelParameters.scenesTree;
		scenes=sceneService.getAllSimulationDataTimeValue(scenesTree, "D:\\workspace", initModelName, AddressService.UPPAAL_PATH, simulationDataNum);
//		ruleAnalysisService.getAllRuleAnalysis(scenes, rules, "D:\\workspace", initModelName, simulationTime, "24", "300");

	}
	
	
	public static StaticAnalysisResult getRequirementError(List<Rule> rules, String initFileName,String filePath) throws DocumentException, IOException {
		GetTemplate getTemplate=new GetTemplate();
		////返回可用的rules和各种错误
		HashMap<String,Rule> mapRules=new HashMap<String,Rule>();
		for(Rule rule:rules) {
			mapRules.put(rule.getRuleName(), rule);
		}
		TemplGraphService templGraphService=new TemplGraphService();
		TGraphToDot tDot=new TGraphToDot();
		ToNode toNode=new ToNode();
		//转成可解析xml文件
		String initFileModelName=initFileName.replace(".xml", "");
		String middleChangedModelFileName=initFileModelName+"-change";
		getTemplate.deletLine(filePath+"\\"+initFileName, filePath+"\\"+middleChangedModelFileName+".xml", 2);
		
		List<TemplGraph> templGraphs=templGraphService.getTemplGraphs(filePath+"\\"+middleChangedModelFileName+".xml");
		List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration.indexOf("controlled_device")>=0) {
				controlledDevices.add(templGraph);
			}
		}
		List<Device> devices=templGraphService.getDevice(controlledDevices);
		/////移除incorrect rules
//		List<Rule> incorrectRules=getIncorrect(rules, templGraphs);
		List<ErrorReason> incorrectRules=getIncorrect(rules, devices);
		
		
		Iterator<Rule> iteratorRules=rules.iterator();
		while(iteratorRules.hasNext()) {
			Rule rule=iteratorRules.next();			
			for(ErrorReason er:incorrectRules) {
				if(rule.getRuleName().equals(er.rule.getRuleName())) {
					iteratorRules.remove();
				}
			}
		}
		///////////////////删除重复的规则
		List<Rule> newRules=deleteRepeat(rules);
		
		tDot.getNewIFD(templGraphs, newRules, filePath+"\\"+initFileName+".dot");
		List<GraphNode> nodes=toNode.getNodes(filePath+"\\"+initFileName+".dot");
		List<GraphNode> ruleNodes=new ArrayList<GraphNode>();
		
		for(GraphNode node:nodes) {
			if(node.getShape().equals("hexagon")) {
				ruleNodes.add(node);
			}
		}
		///////////获得unused
		List<ErrorReason> unusedRules=getUnused(ruleNodes, devices,mapRules);
		System.out.println(unusedRules);
		Iterator<Rule> iteratorNewRules=newRules.iterator();
		//////////删掉unused
		while(iteratorNewRules.hasNext()) {
			Rule rule=iteratorNewRules.next();
			for(ErrorReason unusedRule:unusedRules) {
				if(unusedRule.rule.getRuleName().equals(rule.getRuleName())) {
					iteratorNewRules.remove();
					break;
				}
			}
		}
		tDot.getNewIFD(templGraphs, newRules, filePath+"\\"+initFileName+".dot");
		ruleNodes.clear();
		nodes.clear();
		nodes=toNode.getNodes(filePath+"\\"+initFileName+".dot");
		for(GraphNode node:nodes) {
			if(node.getShape().equals("hexagon")) {
				ruleNodes.add(node);
			}
		}

		//////获得redundant rules
		List<List<GraphNode>> redundantRuleNodes=new ArrayList<List<GraphNode>>();
		for(GraphNode ruleNode:ruleNodes) {
			redundantRuleNodes.add(getRedundant(ruleNode,nodes));
		}
		
		List<List<Rule>> redundantRules=new ArrayList<List<Rule>>();
		for(List<GraphNode> redundant:redundantRuleNodes) {
			List<Rule> reRules=new ArrayList<Rule>();
			for(GraphNode node:redundant) {
				Rule rule=mapRules.get(node.getName());
				reRules.add(rule);
			}
			if(reRules.size()>1) {
				redundantRules.add(reRules);
			}
		}
		System.out.println(redundantRuleNodes);
		List<Action> actions=tDot.getNewActions(newRules,templGraphs);
		List<Device> cannotOffDevices=getIncompleteness(devices, actions);
		List<String> incompleteness=new ArrayList<String>();
		for(Device device:cannotOffDevices) {
			for(String[] stateActionValue:device.stateActionValues) {
				if(Integer.parseInt(stateActionValue[2])==0) {
					String incomplete="Missing rule to turn off "+device.name;
					incompleteness.add(incomplete);
					break;
				}
			}
		}
		
		StaticAnalysisResult staticAnalysisResult=new StaticAnalysisResult();
		staticAnalysisResult.incorrectRules=incorrectRules;
		staticAnalysisResult.unusedRules=unusedRules;
		staticAnalysisResult.redundantRules=redundantRules;
		staticAnalysisResult.incompleteness=incompleteness;
		staticAnalysisResult.usableRules=newRules;
		return staticAnalysisResult;
		
	}
	//////////////////获得incompleteness
	public static List<Device> getIncompleteness(List<Device> devices,List<Action> actions) {
		List<Device> cannotOffDevices=new ArrayList<Device>();
		for(Device device:devices) {
			boolean canOn=false;
			String offAction="";
			for(String[] stateActionValue:device.stateActionValues) {
				if(Integer.parseInt(stateActionValue[2])>0) {
					String action=stateActionValue[1];
					for(Action act:actions) {
						if(act.action.equals(action)) {
							canOn=true;
							break;
						}
					}
				}else {
					offAction=stateActionValue[1];
				}
			}
			if(canOn) {
				boolean canOff=false;
				for(Action act:actions) {
					if(act.action.equals(offAction)) {
						canOff=true;
						break;
					}
				}
				if(!canOff) {
					cannotOffDevices.add(device);
				}
			}
		}
		return cannotOffDevices;
	}
	
	///////////////////获得unused
	public static List<ErrorReason> getUnused(List<GraphNode> ruleNodes,List<Device> devices,HashMap<String,Rule> mapRules){
//		List<GraphNode> unusedRules=new ArrayList<GraphNode>();
		List<ErrorReason> unusedRules=new ArrayList<ErrorReason>();
		for(GraphNode ruleNode:ruleNodes) {
			if(!isUnused(ruleNode, devices).equals("")) {
//				unusedRules.add(ruleNode);
				String reason=isUnused(ruleNode,devices);
				Rule unusedRule=mapRules.get(ruleNode.getName());
				ErrorReason er=new ErrorReason();
				er.reason=reason;
				er.rule=unusedRule;
				unusedRules.add(er);
			}
		}
		return unusedRules;
	}
	public static String isUnused(GraphNode ruleNode,List<Device> devices) {
		TGraphToDot tDot=new TGraphToDot();
		boolean isUnused=false;
		boolean correct=true;
		String reason="";
		List<GraphNode> triggerNodes=new ArrayList<GraphNode>();
		for(GraphNodeArrow pArrow:ruleNode.getpNodeList()) {
			GraphNode triggerNode=pArrow.getGraphNode();
			boolean legal=false;
			for(GraphNodeArrow ppArrow:triggerNode.getpNodeList()) {
				if(ppArrow.getColor()!=null&&ppArrow.getColor().indexOf("lightpink")>=0) {
					legal=true;
					break;
				}
			}
			if(!legal) {
				//////////trigger不合法
				correct=false;
				reason="Trigger: "+triggerNode.getLabel()+" illegal.";
				System.out.println(reason);
				isUnused=true;
				return reason;
			}
			triggerNodes.add(triggerNode);
		}
		///////////trigger之间矛盾
		for(int i=0;i<triggerNodes.size();i++) {
			GraphNode triggerNode1=triggerNodes.get(i);
			for(int j=i+1;j<triggerNodes.size();j++) {
				GraphNode triggerNode2=triggerNodes.get(j);
				if(isContra(triggerNode1, triggerNode2)) {
					isUnused=true;
					reason="Trigger: "+triggerNode1.getLabel()+" "+triggerNode2.getLabel()+" has a logical contradiction.";
					System.out.println(reason);
					return reason;
				}
			}
		}
		////////////trigger是state，但是没有条件触发
		for(GraphNode triggerNode:triggerNodes) {
			String[] attrVal=tDot.getAttrVal(triggerNode.getLabel());
			if(attrVal[1].equals(".")) {
				boolean hasDevice=false;
				for(Device device:devices) {
					if(device.name.equals(attrVal[0])) {
						hasDevice=true;
						for(String[] stateActionValue:device.stateActionValues) {
							if(stateActionValue[0].equals(attrVal[2])) {
								if(!stateActionValue[2].equals("0")) {
									boolean hasPreAction=false;
									for(GraphNodeArrow pArrow:triggerNode.getpNodeList()) {
										if(pArrow.getGraphNode().getName().equals(stateActionValue[1])) {
											hasPreAction=true;
											//////////看这个action的rule有没有能触发的
											GraphNode actionNode=pArrow.getGraphNode();
											boolean cantriggered=false;
											for(GraphNodeArrow ppArrow:actionNode.getpNodeList()) {
												if(ppArrow.getGraphNode().getShape().equals("hexagon")) {
													GraphNode pruleNode=ppArrow.getGraphNode();
													if(isUnused(pruleNode, devices).equals("")) {
														cantriggered=true;
														break;
													}
												}
											}
											if(!cantriggered) {
												isUnused=true;
												reason="No rule can satisfy "+triggerNode.getLabel()+".";
												System.out.println(reason);
												return reason;
											}
											break;
										}
									}
									if(!hasPreAction) {
										isUnused=true;
										reason="No rule can satisfy "+triggerNode.getLabel()+".";
										System.out.println(reason);
										return reason;
									}
								}
								break;
							}
						}
						
						break;
					}
				}
//				if(!hasDevice) {
//					isUnused=false;
//					return isUnused;
//				}
				

				
				
			}
		}
		
		
		
		
		return reason;
	}
	
	public static boolean isContra(GraphNode triggerNode1,GraphNode triggerNode2) {
		TGraphToDot tDot=new TGraphToDot();
		String trigger1=triggerNode1.getLabel();
		String trigger2=triggerNode2.getLabel();
		String[] attrVal1=tDot.getAttrVal(trigger1);
		String[] attrVal2=tDot.getAttrVal(trigger2); 
		if(attrVal1[0].equals(attrVal2[0])) {
			if(attrVal1[1].equals(".")) {
				if(attrVal2[2].equals(attrVal1[2])) {
					return true;
				}
			}else {
				double val1=Double.parseDouble(attrVal1[2]);
				double val2=Double.parseDouble(attrVal2[2]);
				if(attrVal1[1].equals("=")) {
					if(attrVal2[1].equals("=")) {
						if(!attrVal2[2].equals(attrVal1[2])) {
							return true;
						}
					}else if(attrVal2[1].equals(">")) {
						if(val2>=val1) {
							return true;
						}
					}else if(attrVal2[1].equals("<")) {
						if(val2<=val1) {
							return true;
						}
					}
				}else if(attrVal2[1].equals("=")) {
					if(attrVal1[1].equals(">")) {
						if(val1>=val2) {
							return true;
						}
					}else if(attrVal1[1].equals("<")) {
						if(val1<=val2) {
							return true;
						}
					}
				}else if(attrVal1[1].equals(">")) {
					if(attrVal2[1].equals("<")) {
						if(val1>=val2) {
							return true;
						}
					}
				}else if(attrVal1[1].equals("<")) {
					if(attrVal2[1].equals(">")) {
						if(val1<=val2) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	////////////////////获得冗余的规则
	public static List<GraphNode> getRedundant(GraphNode ruleNode,List<GraphNode> graphNodes) {
		List<GraphNode> redundantNodes=new ArrayList<GraphNode>();
		List<GraphNode> otherCauseRuleNodes=new ArrayList<GraphNode>();
		redundantNodes.add(ruleNode);
		boolean allActionHasOtherRule=true;
		first:
		for(GraphNodeArrow cArrow:ruleNode.getcNodeList()) {
			GraphNode actionNode=cArrow.getGraphNode();
			boolean existOtherRule=false;
			for(GraphNodeArrow pArrow:actionNode.getpNodeList()) {
				/////////还有其他规则能发起该action		
				if(!pArrow.getGraphNode().getShape().equals("hexagon")) {
					continue;
				}
				if(!pArrow.getGraphNode().getName().equals(ruleNode.getName())) {
					existOtherRule=true;
					GraphNode otherRuleNode=pArrow.getGraphNode();
					otherCauseRuleNodes.add(otherRuleNode);
					if(containActionNode(ruleNode, otherRuleNode)) {
						/////////////otherRule包含rule的所有action
						List<GraphNode> pathRuleLists=canTraceBack(ruleNode, otherRuleNode,graphNodes);
						if(pathRuleLists!=null) {
							/////otherRuleNode的trigger总能指向ruleNode的trigger
							/////说明是冗余的
							redundantNodes.addAll(pathRuleLists);							
							break first;
						}
					}
				}				
			}
			if(!existOtherRule) {
				//////////////如果没有其他规则能发起该rule那这条规则不会冗余
				allActionHasOtherRule=false;
				break;
			}
		}
		if(allActionHasOtherRule&&redundantNodes.size()==1) {
			for(GraphNode otherRuleNode:otherCauseRuleNodes) {
				List<GraphNode> pathRuleList=canTraceBack(ruleNode, otherRuleNode,graphNodes);
				if(pathRuleList!=null) {
					redundantNodes.addAll(pathRuleList);
				}
			}
		}
		if(redundantNodes.size()>1) {
			for(GraphNodeArrow cArrow:ruleNode.getcNodeList()) {
				boolean existActionNode=false;
				second:
				for(int i=1;i<redundantNodes.size();i++) {
					for(GraphNodeArrow recArrow:redundantNodes.get(i).getcNodeList()) {
						if(cArrow.getGraphNode().getName().equals(recArrow.getGraphNode().getName())) {
							existActionNode=true;
							break second;
						}
					}
				}
				if(!existActionNode) {
					redundantNodes.clear();
					redundantNodes.add(ruleNode);
					break;
				}
			}
			
		}
		return redundantNodes;
	}
	

	
	public static List<GraphNode> canTraceBack(GraphNode ruleNode,GraphNode otherRuleNode,List<GraphNode> graphNodes){
		List<GraphNode> ruleList=new ArrayList<GraphNode>();
		List<GraphNode> triggerNodes=new ArrayList<GraphNode>();
		for(GraphNodeArrow pArrow:ruleNode.getpNodeList()) {
			triggerNodes.add(pArrow.getGraphNode());
		}
		for(GraphNode node:graphNodes) {
			if(node.flag) {
				node.flag=false;
			}
		}
		for(GraphNodeArrow pArrow:otherRuleNode.getpNodeList()) {
			GraphNode triggerNode=pArrow.getGraphNode();
			Queue<GraphNode> nodeQueue=new LinkedList<GraphNode>();
			nodeQueue.add(triggerNode);
			triggerNode.flag=true;
			boolean canTraceTo=false;
			while(!nodeQueue.isEmpty()) {
				GraphNode node=nodeQueue.poll();
				if(triggerNodes.contains(node)) {
					canTraceTo=true;
					break;
				}
				for(GraphNodeArrow nodepArrow:node.getpNodeList()) {
					GraphNode pNode=nodepArrow.getGraphNode();
					if(nodepArrow.getStyle()==null&&pNode.getShape().indexOf("doubleoctagon")<0&&!pNode.flag) {
						if(pNode.getShape().indexOf("hexagon")>=0 && !pNode.getName().equals(ruleNode.getName())) {
							List<GraphNode> pRuleList=new ArrayList<GraphNode>();
							
							if((pRuleList=canTraceBack(ruleNode, pNode, graphNodes))!=null) {
								ruleList.addAll(pRuleList);
							}
						}else {
							nodeQueue.add(pNode);
							pNode.flag=true;
						}
					}
				}
				
			}
			if(!canTraceTo&&ruleList.size()==0) {
				return null;
			}
			
		}
		
		
		ruleList.add(otherRuleNode);
		
		return ruleList;
	}
	

	
	

	
	///////////otherRule的action是否包含rule的action
	public static boolean containActionNode(GraphNode ruleNode,GraphNode otherRuleNode) {
		boolean contain=true;
		for(GraphNodeArrow cArrow:ruleNode.getcNodeList()) {
			GraphNode actionNode=cArrow.getGraphNode();
			boolean exist=false;
			for(GraphNodeArrow othercArrow:otherRuleNode.getcNodeList()) {
				if(othercArrow.getGraphNode().getName().equals(actionNode.getName())) {
					exist=true;
					break;
				}
			}
			if(!exist) {
				contain=false;
			}
		}
		return contain;
	}
	

	
	
	
	///////////////////删除重复的规则
	
	public static List<Rule> deleteRepeat(List<Rule> rules){
		List<Rule> newRules=new ArrayList<Rule>();
//		for(int i=rules.size()-1;i>=0;i--) {
//			for(int j=0;j<i;j++) {
//				if(rules.get(i).contentEquals(rules.get(j))) {
//					rules.remove(i);
//					break;
//				}
//			}
//		}
		for(Rule rule:rules) {
			boolean equal=false;
			for(Rule newRule:newRules) {
				if(newRule.contentEquals(rule)) {
					equal=true;
					break;
				}
			}
			if(!equal) {
				newRules.add(rule);
			}
		}
		return newRules;
	}
//	public static List<Rule> deleteRepeat(List<Rule> rules){
//		List<Rule> newRules=new ArrayList<Rule>();
//		for(Rule rule:rules) {
//			boolean exist=false;
//			second:
//			for(Rule newRule:newRules) {
//				if(rule.getRuleContent().equals(newRule.getRuleContent())) {
//					exist=true;
//					break;
//				}
//				boolean actionSame=true;
//				boolean triggerSame=true;
//				if(newRule.getTrigger().size()==rule.getTrigger().size()) {
//					for(String tri:rule.getTrigger()) {
//						boolean triExist=false;
//						for(String newTri:newRule.getTrigger()) {
//							if(tri.equals(newTri)) {
//								triExist=true;
//								break;
//							}
//						}
//						if(!triExist) {
//							triggerSame=false;
//							continue second;
//						}
//					}
//				}else {
//					triggerSame=false;
//					continue;
//				}
//				if(newRule.getAction().size()==rule.getAction().size()) {
//					for(String act:rule.getAction()) {
//						boolean actExist=false;
//						for(String newAct:newRule.getAction()) {
//							if(act.equals(newAct)) {
//								actExist=true;
//								break;
//							}
//						}
//						if(!actExist) {
//							actionSame=false;
//							continue second;
//						}
//					}
//				}else {
//					actionSame=false;
//					continue;
//				}
//				
//				if(actionSame&&triggerSame) {
//					exist=true;
//					break;
//				}
//				
//			}
//			if(!exist) {
//				newRules.add(rule);
//			}
//		}
//		return newRules;
//	}
	
	
	////////////////////获得不正确的规则
	public static List<ErrorReason> getIncorrect(List<Rule> rules,List<Device> devices){
		List<ErrorReason> incorrectReason=new ArrayList<ErrorReason>();
		List<Rule> incorrectRules=new ArrayList<Rule>();
		for(Rule rule:rules) {
			String reason="";
			boolean incorrect=false;
			for(String action:rule.getAction()) {
				
				if(action.indexOf(".")>0)
				action=action.substring(action.indexOf(".")).substring(1);
				boolean existAction=false;
				device:
				for(Device device:devices) {
					for(String[] stateActionValue:device.stateActionValues) {
						if(stateActionValue[1].trim().equals(action.trim())) {
							existAction=true;
							break device;
						}
					}
				}
				if(!existAction) {
					incorrect=true;	
					reason+=action.trim()+" ";
				}
			}
			if(incorrect) {
				ErrorReason er=new ErrorReason();
				er.rule=rule;
				er.reason=reason+"cannot be executed!";
				incorrectReason.add(er);
				
				incorrectRules.add(rule);
				System.out.println(reason);
			}
			
		}
		return incorrectReason;
	}
	
	
//	public static List<Rule> getIncorrect(List<Rule> rules,List<TemplGraph> templGraphs){
//		TGraphToDot tDot=new TGraphToDot();
//		List<Action> actions=tDot.getActions(rules,templGraphs);
//		///////判断action是否有效
//		List<Rule> incorrectRules=new ArrayList<Rule>();
//		for(Action action:actions) {
//			if(action.getToState()==null) {
//				for(Rule rule:action.getRules()) {
//					boolean exist=false;
//					for(Rule inrule:incorrectRules) {
//						if(inrule.getRuleName().equals(rule.getRuleName())) {
//							exist=true;
//							break;
//						}
//					}
//					if(!exist) {
//						incorrectRules.add(rule);
//					}
//				}
//			}
//		}
//		
//		return incorrectRules;
//	}
	

	
	
	public AllRuleAnalysisResult getAllRuleAnalysis(List<Scene> scenes,List<Rule> rules,List<String> properties,String filePath,String initFileName,String simulationTime,String equivalentTime,String intervalTime) throws DocumentException {
		///////////////////////先进行deviceAnalysisResult
		SceneService sceneService=new SceneService();
		List<Scene> newScenes=new ArrayList<Scene>();
		PropertyAnalysisService propertyAnalysisService=new PropertyAnalysisService();
		AllRuleAnalysisResult allRuleAnalysisResult=new AllRuleAnalysisResult();
		ToNode toNode=new ToNode();
		List<GraphNode> graphNodes=new ArrayList<GraphNode>();
		graphNodes=toNode.getGraphNodes("D:\\workspace", initFileName);
		System.out.println("filePath:"+filePath+" initFileName:"+initFileName+" simulationTime:"+simulationTime+" equivalentTime:"+equivalentTime+" intervalTime:"+intervalTime);
		for(Scene scene:scenes) {
			scene=sceneService.getDeviceAnalysisResult(scene, rules, simulationTime,"D:\\workspace", initFileName, equivalentTime, intervalTime);
			newScenes.add(scene);
		}
		List<PropertyAnalysis> propertyAnalysis=new ArrayList<PropertyAnalysis>();
		if(properties.size()>0) {
			for(String property:properties) {
				propertyAnalysis.add(propertyAnalysisService.analyzeProperty(property, newScenes));
				
			}
		}
		allRuleAnalysisResult.propertyAnalysis=propertyAnalysis;
		
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
					
					if(noCauseRule ) {
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
								if(ruleCount.count<=1) {
									//////////////////不考虑次数小于等于1的causeRule
									continue;
								}
								boolean existRule=false;
								for(RuleCountSceneName ruleCountExist:stateCauseRuleCountSceneName.rulesCountSceneName) {
									///////////////////////////////count<=1的不去考虑  TODO
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
							if(ruleCount.count<=1) {
								//////////////////不考虑次数小于等于1的causeRule
								continue;
							}
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
