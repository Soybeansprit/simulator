package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Rule;
import com.example.demo.bean.TemplGraph;
import com.example.demo.service.GetTemplate.Template;
import com.example.demo.bean.Action;
import com.example.demo.bean.GraphNode;
import com.example.demo.bean.GraphNodeArrow;

@Service
public class AnalyseIFD {

	public static void main(String[] args) throws DocumentException {
		// TODO Auto-generated method stub
		String dotPath="D:\\g5.dot";
		String rulePath="D:\\rules.txt";
		String path2="D:\\win21.xml";
		ToNode toNode=new ToNode();
		TGraphToDot tDot = new TGraphToDot();
		List<GraphNode> graphNodes=new ArrayList<GraphNode>();
		graphNodes=toNode.getNodes(dotPath);
		AnalyseIFD anaIFD=new AnalyseIFD();
		RuleService ruleService=new RuleService();
		List<Rule> rules=ruleService.getRuleListFromTxt(rulePath);
		
		GetTemplate parse=new GetTemplate();
		//parse.deletLine(path1, path2, 2);
		List<Template> templates=parse.getTemplate(path2);
		
		TemplGraphService tGraph=new TemplGraphService();
		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
		for(Template template:templates) {
			templGraphs.add(tGraph.getTemplGraph(template));
		}
		List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
		
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration!=null) {
				if(templGraph.declaration.indexOf("controlled_device")>=0) {
					controlledDevices.add(templGraph);
				}
			}
		}
////		List<TriggerRule> triggerRules=new ArrayList<TriggerRule>();
////		List<TriAndTriggerRule> trTriggerRules=new ArrayList<TriAndTriggerRule>();
////		List<TriAndTriggerRule> trTriggerRules0=new ArrayList<TriAndTriggerRule>();
////		trTriggerRules0=traverse.getTrTriggerRules(graphNodes);
////		
//		
//		List<Action> actions=tDot.getActions(rules,controlledDevices);
//		
//		for(GraphNode graphNode:graphNodes) {
//			if(graphNode.getShape().indexOf("hexagon")>=0) {
//				List<GraphNode> ruleTriggerRuleNodes=anaIFD.ruleTriggerRules(graphNode, actions);
//				System.out.println(graphNode.getName()+"can trigger:");
//				for(GraphNode ruleTriggerRuleNode:ruleTriggerRuleNodes) {					
//					System.out.println(ruleTriggerRuleNode.getName());
//				}				
//			}
//		}
//		
//		List<RuleAndTriggerRules> rulesAndTriggerRules=new ArrayList<RuleAndTriggerRules>();
////		for(GraphNode graphNode:graphNodes) {
////			if(graphNode.getShape().indexOf("hexagon")>=0) {
////				RuleAndTriggerRules ruleAndTriggerRules=traverse.new RuleAndTriggerRules();
////				ruleAndTriggerRules=traverse.getRuleTriggerRules(graphNode, actions);
////				rulesAndTriggerRules.add(ruleAndTriggerRules);
////			}
////		}
//		
//		List<GraphNode> ruleNodes=new ArrayList<GraphNode>();
//		for(GraphNode graphNode:graphNodes) {
//			if(graphNode.getShape().indexOf("hexagon")>=0) {
//				ruleNodes.add(graphNode);
//			}
//		}
//		rulesAndTriggerRules=anaIFD.getRulesAndTriggerRules(ruleNodes);
//		
//		for(RuleAndTriggerRules ruleAndTriggerRules:rulesAndTriggerRules) {
//			anaIFD.getAllTriggerRules(ruleAndTriggerRules, rulesAndTriggerRules);
//		}
//		
//		List<GraphNode> triggerNodes=new ArrayList<GraphNode>();
//		for(GraphNode graphNode:graphNodes) {
//			if(graphNode.getShape().indexOf("oval")>=0) {
//				triggerNodes.add(graphNode);
//			}
//		}
//		List<TriggerStopRules> triggersStopRules=traverse.getTriggerStopRules(triggerNodes);
//		for(TriggerStopRules triggerStopRules:triggersStopRules) {
//			System.out.println(triggerStopRules.triggerNode.getLabel());
//		}
//		
		
		
		///////////////////////////analyse IFD////////////////////////////////
		//获得IFD各节点graphNodes
		//先获得IFD中的ruleNode
		//用getRulesAndTriggerRules方法获得各ruleNode所能触发的其他规则
		//获得IFD中的triggerNode
		//用getTriggerStopRules方法获得可能引起其他rule无法发生的triggerNode
		/////////////////////////////////////////////////////////////////////
		
		
		List<GraphNode> ruleNodes=new ArrayList<GraphNode>();
		for(GraphNode graphNode:graphNodes) {
			if(graphNode.getShape().indexOf("hexagon")>=0) {
				ruleNodes.add(graphNode);
			}
		}
		List<RuleAndTriggerRules> rulesAndTriggerRules=new ArrayList<RuleAndTriggerRules>();
		rulesAndTriggerRules=anaIFD.getRulesAndTriggerRules(ruleNodes);
		for(RuleAndTriggerRules ruleAndTriggerRules:rulesAndTriggerRules) {
			anaIFD.getAllTriggerRules(ruleAndTriggerRules, rulesAndTriggerRules);
		}
		
		List<GraphNode> triggerNodes=new ArrayList<GraphNode>();
		for(GraphNode graphNode:graphNodes) {
			if(graphNode.getShape().indexOf("oval")>=0) {
				triggerNodes.add(graphNode);
			}
		}
		List<TriggerStopRules> triggersStopRules=anaIFD.getTriggerStopRules(triggerNodes);
		for(TriggerStopRules triggerStopRules:triggersStopRules) {
			System.out.println(triggerStopRules.triggerNode.getLabel());
		}
		
		
		
//		for(GraphNode graphNode:graphNodes) {
//			if(graphNode.getName().indexOf("sensor")>0) {
//				//从不是biddable的sensor开始遍历
//				if(graphNode.getName().indexOf("person")<0 && graphNode.getName().indexOf("distance")<0) {
//					for(Arrow cgraphNode:graphNode.cNodeList) {
//						TriggerRule triggerRule=traverse.new TriggerRule();
//						TriAndTriggerRule trTriggerRule=traverse.new TriAndTriggerRule();
//						triggerRule=traverse.getRuleList(cgraphNode.graphNode);
//						trTriggerRule.triggerRule=triggerRule;
//						trTriggerRule.triggerName=cgraphNode.graphNode.getName();
//						triggerRules.add(triggerRule);
//						trTriggerRules.add(trTriggerRule);
//					}
//				}
//			}
//		}
	}
	
	class TriAndTriggerRule{
		String triggerName=null;
		String triggerContent=null;
		int triggerRuleNum=0;
		TriggerRule triggerRule=new TriggerRule();
		List<String> sensorsName=new ArrayList<String>();
	}
	
	class RuleStyle{
		String name=null;
		int weight=0;  //0为初始触发的rule，1为可能发生，2为必然发生		
	}
	
	class TriggerRule{
		List<RuleStyle> positive=new ArrayList<RuleStyle>();
		List<RuleStyle> negative=new ArrayList<RuleStyle>();
	}
	
	class RuleAndTriggerRules{
		GraphNode ruleNode=new GraphNode();
		List<GraphNode> triggerRuleNodes=new ArrayList<GraphNode>();
		List<GraphNode> stopRuleNodes=new ArrayList<GraphNode>();
	}
	
	class TriggerStopRules{
		GraphNode triggerNode=new GraphNode();
		List<GraphNode> stopRuleNodes=new ArrayList<GraphNode>();
		List<GraphNode> triggerRuleNodes=new ArrayList<GraphNode>();
	}
	
	
//	////////////////////////////2020.12.22以前很久/////////////////////////////////////
//	//BFS,寻找某个节点所能影响的所有rules，最终选择
//	public TriggerRule getRuleListBFS(GraphNode triggerNode) {
//		TriggerRule triggerRule=new TriggerRule();
//		if(triggerNode==null) {
//			return triggerRule;
//		}
//		Stack<GraphNode> stack=new Stack<GraphNode>();
//		stack.push(triggerNode);
//		while(!stack.isEmpty()) {
//			GraphNode node=stack.pop();
//			node.flag=true;
//			for(Arrow arrow:node.cNodeList) {
//				if(((arrow.label!=null&&arrow.label.indexOf("+")>=0)||arrow.color==null)&&arrow.graphNode.flag==false) {
//					stack.push(arrow.graphNode);					
//				}
//				if(node==triggerNode&&arrow.graphNode.getShape()!=null&&arrow.graphNode.getShape().indexOf("hexagon")>=0) {
//					//第一个trigger的rule
//					RuleStyle ruleStyle=new RuleStyle();
//					ruleStyle.name=arrow.graphNode.getName();
//					ruleStyle.weight=0;
//					triggerRule.positive.add(ruleStyle);
//				}
//				if(arrow.label!=null&&arrow.label.indexOf("+")>=0){
//					//正影响，arrow.graphNode为trigger节点
//					for(Arrow carrow:arrow.graphNode.cNodeList) {
//						//找到该trigger节点对应的rule节点
//						if(carrow.graphNode.getShape()!=null&&carrow.graphNode.getShape().indexOf("hexagon")>=0) {
//							int flag=0;
//							for(RuleStyle positive:triggerRule.positive) {
//								if(positive.name.equals(carrow.graphNode.getName())) {
//									flag=1;
//									break;
//								}
//							}
//							if(flag==0) {
//								RuleStyle postive=new RuleStyle();
//								postive.name=carrow.graphNode.getName();
//								if(arrow.style==null) {
//									//直接的正影响
//									postive.weight=2;
//								}else if(arrow.style.indexOf("dashed")>=0) {
//									//可能的正影响
//									postive.weight=1;
//								}								
//								triggerRule.positive.add(postive);
//							}
//							break;
//						}
//					}
//					
//				}
//				if(arrow.label!=null&&arrow.label.indexOf("-")>=0) {
//					//负影响，arrow.graphNode为trigger节点
//					for(Arrow carrow:arrow.graphNode.cNodeList) {
//						//找到该trigger节点对应的rule节点
//						if(carrow.graphNode.getShape()!=null&&carrow.graphNode.getShape().indexOf("hexagon")>=0) {
//							int flag=0;
//							for(RuleStyle negative:triggerRule.negative) {
//								if(negative.name.equals(carrow.graphNode.getName())) {
//									flag=1;
//									if(arrow.style==null) {
//										negative.weight=2;
//									}
//									break;
//								}
//							}
//							if(flag==0) {
//								RuleStyle negative=new RuleStyle();
//								negative.name=carrow.graphNode.getName();
//								if(arrow.style==null) {
//									//直接的负影响
//									negative.weight=2;
//								}else if(arrow.style.indexOf("dashed")>=0) {
//									//可能的负影响
//									negative.weight=1;
//								}								
//								triggerRule.negative.add(negative);
//							}
//							break;
//						}
//					}
//				}
//			}
//			
//		}
//		return triggerRule;
//	}
//	
//	//DFS
//		public TriggerRule getRuleList(GraphNode root){
//			
//			TriggerRule triggerRule=new TriggerRule();
//			if(root==null) {
//				return triggerRule;
//			}
//			Stack<GraphNode> stack=new Stack<GraphNode>();
//			stack.push(root);
//			
//			while(!stack.isEmpty()) {
//				GraphNode graphNode=stack.pop();
//				graphNode.flag=true;			
//				if(graphNode.cNodeList!=null) {
//					for(Arrow arrow:graphNode.cNodeList) {
//						//不将设备节点添加到栈中，不将“-”的后继节点加入栈中
//						if(arrow.graphNode.getShape().indexOf("doubleoctagon")<0&&(arrow.label==null||arrow.label.indexOf("+")>0)&&arrow.graphNode.flag==false) {
//							stack.push(arrow.graphNode);
//						}					
//						//起始规则
//						if(graphNode==root && arrow.graphNode.getName().indexOf("rule")>=0 && arrow.graphNode.getName().indexOf("trigger")<0) {
//							int i=0;
//							for(;i<triggerRule.positive.size();i++) {
//								if(arrow.graphNode.getName().equals(triggerRule.positive.get(i).name)) {
//									break;
//								}
//							}
//							
//							//避免循环遍历
//							if(i==triggerRule.positive.size()) {
//								//初始rule
//								RuleStyle rule=new RuleStyle();
//								rule.name=arrow.graphNode.getName();
//								rule.weight=0;
//								triggerRule.positive.add(rule);
//								
//							}
//							
//						}
//						//正影响的规则
//						if(arrow.label!=null) {
//							if(arrow.label.indexOf("+")>=0) {
//								//positive
//								if(arrow.style==null) {
//									for(Arrow carrow:arrow.graphNode.cNodeList) {
//										if(carrow.graphNode.getName().indexOf("rule")>=0 && carrow.graphNode.getName().indexOf("trigger")<0) {
//											int i=0;
//											for(;i<triggerRule.positive.size();i++) {
//												if(carrow.graphNode.getName().equals(triggerRule.positive.get(i).name)) {
//													break;
//												}
//											}
//											
//											if(i==triggerRule.positive.size()) {
//												RuleStyle rule=new RuleStyle();
//												rule.name=carrow.graphNode.getName();
//												rule.weight=2;
//												triggerRule.positive.add(rule);
//												
//											}
//											
//										}
//									}
//								}else if(arrow.style.indexOf("dashed")>=0) {
//									for(Arrow carrow:arrow.graphNode.cNodeList) {
//										if(carrow.graphNode.getName().indexOf("rule")>=0 && carrow.graphNode.getName().indexOf("trigger")<0) {
//											int i=0;
//											for(;i<triggerRule.positive.size();i++) {
//												if(carrow.graphNode.getName().equals(triggerRule.positive.get(i).name)) {
//													break;
//												}
//											}
//											
//											if(i==triggerRule.positive.size()) {
//												RuleStyle rule=new RuleStyle();
//												rule.name=carrow.graphNode.getName();
//												rule.weight=1;
//												triggerRule.positive.add(rule);
//												
//											}
//										}
//									}
//								}
//							}
//							//负影响的规则
//							if(arrow.label.indexOf("-")>=0) {
//								//negative
//								if(arrow.style==null) {
//									for(Arrow carrow:arrow.graphNode.cNodeList) {
//										if(carrow.graphNode.getName().indexOf("rule")>=0 && carrow.graphNode.getName().indexOf("trigger")<0) {
//											int i=0;
//											for(;i<triggerRule.negative.size();i++) {
//												if(carrow.graphNode.getName().equals(triggerRule.negative.get(i).name)) {
//													break;
//												}
//											}
//											
//											if(i==triggerRule.negative.size()) {
//												RuleStyle rule=new RuleStyle();
//												rule.name=carrow.graphNode.getName();
//												rule.weight=2;
//												triggerRule.negative.add(rule);
//												
//											}
//											
//										}
//									}
//								}else if(arrow.style.indexOf("dashed")>=0) {
//									for(Arrow carrow:arrow.graphNode.cNodeList) {
//										if(carrow.graphNode.getName().indexOf("rule")>=0 && carrow.graphNode.getName().indexOf("trigger")<0) {
//											int i=0;
//											for(;i<triggerRule.negative.size();i++) {
//												if(carrow.graphNode.getName().equals(triggerRule.negative.get(i).name)) {
//													break;
//												}
//											}
//											
//											if(i==triggerRule.negative.size()) {
//												RuleStyle rule=new RuleStyle();
//												rule.name=carrow.graphNode.getName();
//												rule.weight=1;
//												triggerRule.negative.add(rule);
//												
//											}
//											
//										}
//									}
//								}
//								
//							}
//						}
//						
//						
//					}
//				}
//				
//			}
//			
//			return triggerRule;
//		}
//	
//	////////////////////////////2020.12.22以前很久/////////////////////////////////////
//	//获得每个trigger节点所能触发的所有规则以及引起的无法发生的规则
//	public List<TriAndTriggerRule> getTrTriggerRules(List<GraphNode> graphNodes){
//		List<GraphNode> triggerNodes=new ArrayList<GraphNode>();
//		List<TriAndTriggerRule> trTriggerRules=new ArrayList<TriAndTriggerRule>();
//		for(GraphNode graphNode:graphNodes) {
//			if(graphNode.getName().indexOf("trigger")>=0) {
//				triggerNodes.add(graphNode);
//			}
//		}
//		for(GraphNode triggerNode:triggerNodes) {
//			TriggerRule triggerRule=new TriggerRule();
//			TriAndTriggerRule trTriggerRule=new TriAndTriggerRule();
//			//寻找该节点所能影响的所有rules
//			triggerRule=getRuleListBFS(triggerNode);
//			resetFlag(triggerNode);
//			trTriggerRule.triggerRule=triggerRule;
//			trTriggerRule.triggerContent=triggerNode.getLabel();
//			trTriggerRule.triggerName=triggerNode.getName();
//			for(Arrow pNode:triggerNode.pNodeList) {
//				if(pNode.graphNode.getName().indexOf("sensor")>=0) {
//					trTriggerRule.sensorsName.add(pNode.graphNode.getName());
//				}
//			}
//			trTriggerRules.add(trTriggerRule);
//		}
//		return trTriggerRules;
//		
//		
//	}
//	//将遍历过的节点的flag重置为零
//	public void resetFlag(GraphNode root) {
//		Stack<GraphNode> stack=new Stack<GraphNode>();
//		stack.push(root);
//		while(!stack.isEmpty()) {
//			GraphNode graphNode=stack.pop();
//			graphNode.flag=false;
//			
//			if(graphNode.cNodeList!=null) {
//				for(Arrow arrow:graphNode.cNodeList) {					
//					if(((arrow.label!=null&&arrow.label.indexOf("+")>=0)||arrow.color==null)&&arrow.graphNode.flag==true) {
//						stack.push(arrow.graphNode);
//					}					
//				}
//		}
//	}
//	
//	
//
//	}
	
	
	

	
	/////////////////////////////////////2020.12.24/////////////////////////////////
	//某条规则发生的条件下，某个trigger能否满足？
	public boolean canTriggerSatisfied(GraphNode ruleNode,List<GraphNode> ruleTriggerNodes,List<GraphNode> ruleActionNodes,GraphNode otherTrigger) {
		//Ri，Ti，Ai，tj		
		if(exitEqual(otherTrigger, ruleTriggerNodes)) {
			//如果tj属于Ti
			return true;
		}else {
			boolean hasActionEffectTrigger=false;
			for(GraphNodeArrow pArrow:otherTrigger.getpNodeList()) {
				//实线
				if(pArrow.getStyle()==null && pArrow.getColor()!=null && pArrow.getColor().indexOf("red")>=0 && pArrow.getLabel()==null) {
					if(pArrow.getGraphNode().getShape().indexOf("oval")>=0) {
						//trigger节点
						GraphNode pTriggerNode=pArrow.getGraphNode();
						if(exitEqual(pTriggerNode, ruleTriggerNodes)) {
							//如果tj有红色实线的前驱trigger tk节点，tk属于Ti，则tj能满足
							return true;
						}
					}else if(pArrow.getGraphNode().getShape().indexOf("record")>=0) {
						//action节点
						GraphNode pActionNode=pArrow.getGraphNode();
						if(exitEqual(pActionNode, ruleActionNodes)) {
							//如果tj有红色实线的前驱action ak节点，ak属于Ai，则tj能满足
							return true;
						}
					}
				}else if(pArrow.getStyle()!=null) {
					//虚线
					if(pArrow.getGraphNode().getShape().indexOf("record")>=0) {
						//action节点
						GraphNode pActionNode=pArrow.getGraphNode();
						if(exitEqual(pActionNode, ruleActionNodes)) {
							// ak属于Ai，
							hasActionEffectTrigger=true;
							if(canActionTrigger(pActionNode, otherTrigger)) {
								return true;
							}
						}
					}
				}
			}
			if(hasActionEffectTrigger) {
				return false;
			}
			//如果tj和Ti无涉及相同设备（传感器和可控设备），则可满足
//			GraphNode triggerDeviceNode=getTriggerDevice(otherTrigger);
//			boolean triggerHasSameDevice=false;
//			for(GraphNode ruleTriggerNode:ruleTriggerNodes) {
//				GraphNode ruleTrggerDeviceNode=getTriggerDevice(ruleTriggerNode);
//				if(ruleTrggerDeviceNode==triggerDeviceNode) {
//					triggerHasSameDevice=true;
//				}
//			}
//			if(!triggerHasSameDevice) {
//				return true;
//			}
			return false;
		}
	}
	
	public GraphNode getTriggerDevice(GraphNode triggerNode) {
		GraphNode triggerDeviceNode=new GraphNode();
		for(GraphNodeArrow ptriggerArrow:triggerNode.getpNodeList()) {
			//找到triggerNode的device
			if(ptriggerArrow.getColor()!=null && ptriggerArrow.getColor().indexOf("lightpink")>=0) {
				triggerDeviceNode=ptriggerArrow.getGraphNode();
				break;
			}
		}
		return triggerDeviceNode;
	}
	
	public boolean canActionTrigger(GraphNode actionNode,GraphNode triggerNode) {
		GraphNode actionDeviceNode=new GraphNode();
		GraphNode triggerDeviceNode=getTriggerDevice(triggerNode);
		for(GraphNodeArrow actioncArrow:actionNode.getcNodeList()) {
			//找到actionNode的device
			if(actioncArrow.getColor()!=null && actioncArrow.getColor().indexOf("lemonchiffon3")>=0) {
				actionDeviceNode=actioncArrow.getGraphNode();
				break;
			}
		}
		for(GraphNodeArrow triggercArrow:triggerNode.getcNodeList()) {
			if(triggercArrow.getStyle()==null && triggercArrow.getLabel()==null && triggercArrow.getColor()!=null && triggercArrow.getColor().indexOf("red")>=0) {
				if(triggercArrow.getGraphNode().getShape().indexOf("oval")>=0) {
					//红色实线后继节点是trigger
					GraphNode triggercNode=triggercArrow.getGraphNode();
					GraphNode triggercDeviceNode=getTriggerDevice(triggercNode);
					if(triggerDeviceNode==triggercDeviceNode) {
						//两个trigger涉及同一属性
						//找到triggercNode所属ruleNode
						for(GraphNodeArrow triggerccArrow:triggercNode.getcNodeList()) {
							if(triggerccArrow.getColor()==null) {
								//rule节点
								GraphNode triggerccRuleNode=triggerccArrow.getGraphNode();
								for(GraphNodeArrow triggerccRulecArrow:triggerccRuleNode.getcNodeList()) {
									//action节点
									GraphNode triggerccRulecActionNode=triggerccRulecArrow.getGraphNode();
									GraphNode triggerccRulecActionDeviceNode=new GraphNode();
									for(GraphNodeArrow ccRuleActioncArrow:triggerccRulecActionNode.getcNodeList()) {
										//找到triggerccRulecActionNode的device
										if(ccRuleActioncArrow.getColor()!=null && ccRuleActioncArrow.getColor().indexOf("lemonchiffon3")>=0) {
											triggerccRulecActionDeviceNode=ccRuleActioncArrow.getGraphNode();
											break;
										}
									}
									//只有当am和ai属于同一设备，且am对tj无正影响时，tj才无法发生
									if(triggerccRulecActionDeviceNode==actionDeviceNode) {
										//该action am和原action ai属于同一设备
										//看aj对tj是否有正影响，如有，tj能发生
										//如无，则tj无法发生
										boolean hasPostEffectFlat=false;
										for(GraphNodeArrow ccRuleActioncTriggerArrow:triggerccRulecActionNode.getcNodeList()) {
											if(ccRuleActioncTriggerArrow.getColor()!=null && ccRuleActioncTriggerArrow.getColor().indexOf("red")>=0 && ccRuleActioncTriggerArrow.getLabel()==null) {
												//trigger tk=tj
												if(ccRuleActioncTriggerArrow.getGraphNode()==triggerNode) {
													hasPostEffectFlat=true;
													break;
												}
											}
										}
										if(!hasPostEffectFlat) {
											return false;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	///////////////////////////////////////2020.12.25////////////////////////////////////////	
	public boolean canRuleTriggersAllSatisfied(GraphNode ruleNode,GraphNode triggeredRuleNode,GraphNode oneTriggerNode,List<GraphNode> ruleTriggerNodes,List<GraphNode> ruleActionNodes) {
		//获得该rule的所有trigger节点，分别判断能否满足
		List<GraphNode> triggeredRuleTriggerNodes=getRuleTriggerNodes(triggeredRuleNode);
		for(GraphNode triggeredRuleTriggerNode:triggeredRuleTriggerNodes) {
			//遍历trigger，判断能否满足，如不能满足跳过，如能，继续判断，直到判断完
			if(triggeredRuleTriggerNode!=oneTriggerNode) {
				//oneTriggerNode本身满足，故不需要重复判断
				if(!canTriggerSatisfied(ruleNode, ruleTriggerNodes, ruleActionNodes, triggeredRuleTriggerNode)) {
					return false;
				}
			}			
		}
		return true;
	}
	///////////////////////////////////////2020.12.25////////////////////////////////////////
	//获得所有rule能触发的规则
	public List<RuleAndTriggerRules> getRulesAndTriggerRules(List<GraphNode> ruleNodes){
		List<RuleAndTriggerRules> rulesAndTriggerRules=new ArrayList<RuleAndTriggerRules>();
		for(GraphNode ruleNode:ruleNodes) {
			RuleAndTriggerRules ruleAndTriggerRules=getRuleAndTriggerRules(ruleNode);
			rulesAndTriggerRules.add(ruleAndTriggerRules);
		}
		return rulesAndTriggerRules;
	}
	
	///////////////////////////////////////2020.12.25////////////////////////////////////////
	public RuleAndTriggerRules getRuleAndTriggerRules(GraphNode ruleNode) {
		RuleAndTriggerRules ruleAndTriggerRules=new RuleAndTriggerRules();
		ruleAndTriggerRules.ruleNode=ruleNode;
		ruleAndTriggerRules.triggerRuleNodes.add(ruleNode);
		List<GraphNode> ruleTriggerNodes=getRuleTriggerNodes(ruleNode);
		List<GraphNode> ruleActionNodes=getRuleActionNodes(ruleNode);
		for(GraphNode ruleTriggerNode:ruleTriggerNodes) {
			//遍历Ti
			//如ti∈Ti存在黑色实线后继节点，即为rule Rj，获得Rj的所有trigger节点Tj，判断Tj在Ri的情况下能否满足
			//如ti∈Ti存在红色实线后继节点，即为trigger tj，获得tj的黑色实线后继节点rule Rj，获得Rj的所有trigger节点Tj，判断Tj在Ri的情况下能否满足

			for(GraphNodeArrow triggercArrow:ruleTriggerNode.getcNodeList()) {
				if(triggercArrow.getColor()==null) {
					//rule节点
					GraphNode triggercRuleNode=triggercArrow.getGraphNode();
					if(!exitEqual(triggercRuleNode, ruleAndTriggerRules.triggerRuleNodes)) {
						//如果triggerRuleNodes中没有这个rule，判断它能否发生
						//获得该rule的所有trigger节点，分别判断能否满足
						if(canRuleTriggersAllSatisfied(ruleNode, triggercRuleNode, ruleTriggerNode, ruleTriggerNodes, ruleActionNodes)) {
							ruleAndTriggerRules.triggerRuleNodes.add(triggercRuleNode);
						}
					}
				}else if(triggercArrow.getColor().indexOf("red")>=0 && triggercArrow.getLabel()==null && triggercArrow.getStyle()==null) {
					//triggerToTrigger
					GraphNode triggercTriggerNode=triggercArrow.getGraphNode();
					for(GraphNodeArrow triggercTriggercArrow:triggercTriggerNode.getcNodeList()) {
						if(triggercTriggercArrow.getColor()==null) {
							//rule节点
							GraphNode triggercTriggercRuleNode=triggercTriggercArrow.getGraphNode();
							if(!exitEqual(triggercTriggercRuleNode, ruleAndTriggerRules.triggerRuleNodes)) {
								//如果triggerRuleNodes中没有这个rule，判断它能否发生
								if(canRuleTriggersAllSatisfied(ruleNode, triggercTriggercRuleNode, triggercTriggerNode,ruleTriggerNodes, ruleActionNodes)) {
									ruleAndTriggerRules.triggerRuleNodes.add(triggercTriggercRuleNode);
									break;
								}
							}
						}
					}
				}else if(triggercArrow.getLabel()!=null) {
					//抑制作用
					GraphNode triggercNegaTriggerNode=triggercArrow.getGraphNode();
					boolean hasPostEffect=false;
					for(GraphNodeArrow triggercpTriggerArrow:triggercNegaTriggerNode.getpNodeList()) {
						if(triggercpTriggerArrow.getLabel()==null && triggercpTriggerArrow.getColor()!=null && triggercpTriggerArrow.getColor().indexOf("red")>=0) {
							hasPostEffect=true;
							break;
						}
					}
					if(!hasPostEffect) {
						for(GraphNodeArrow triggercTriggercArrow:triggercNegaTriggerNode.getcNodeList()) {
							if(triggercTriggercArrow.getColor()==null) {
								//rule节点
								GraphNode triggercNegaTriggercRuleNode=triggercTriggercArrow.getGraphNode();
								ruleAndTriggerRules.stopRuleNodes.add(triggercNegaTriggercRuleNode);
							}
						}
					}
					
				}
			}
		}
		for(GraphNode ruleActionNode:ruleActionNodes) {
			//遍历Ai
			//如ai∈Ai存在红色实线后继节点，即为trigger tj，获得tj的黑色实线后继节点rule Rj，获得Rj的所有trigger节点Tj，判断Tj在Ri的情况下能否满足
			//如ai∈Ai存在红色虚线后继节点，即为trigger tj，判断tj在Ri情况下能否满足，如能满足，获得tj的黑色实线后继节点rule Rj，获得Rj的所有trigger节点Tj，判断Tj在Ri的情况下能否满足

			for(GraphNodeArrow actioncArrow:ruleActionNode.getcNodeList()) {
				if(actioncArrow.getColor()!=null && actioncArrow.getColor().indexOf("red")>=0 && actioncArrow.getStyle()==null && actioncArrow.getLabel()==null) {
					//如ai∈Ai存在红色实线后继节点，即为trigger tj，获得tj的黑色实线后继节点rule Rj，获得Rj的所有trigger节点Tj，判断Tj在Ri的情况下能否满足
					GraphNode actioncTriggerNode=actioncArrow.getGraphNode();
					for(GraphNodeArrow actioncTriggercArrow:actioncTriggerNode.getcNodeList()) {
						if(actioncTriggercArrow.getColor()==null) {
							//rule节点
							GraphNode actioncTriggercRuleNode=actioncTriggercArrow.getGraphNode();
							if(!exitEqual(actioncTriggercRuleNode, ruleAndTriggerRules.triggerRuleNodes)) {
								if(canRuleTriggersAllSatisfied(ruleNode, actioncTriggercRuleNode, actioncTriggerNode, ruleTriggerNodes, ruleActionNodes)) {
									ruleAndTriggerRules.triggerRuleNodes.add(actioncTriggercRuleNode);
									break;
								}
							}
						}
					}
				}else if(actioncArrow.getStyle()!=null) {
					//如ai∈Ai存在红色虚线后继节点，即为trigger tj，判断tj在Ri情况下能否满足，如能满足，获得tj的黑色实线后继节点rule Rj，获得Rj的所有trigger节点Tj，判断Tj在Ri的情况下能否满足
					GraphNode actioncTriggerNode=actioncArrow.getGraphNode();
					if(canTriggerSatisfied(ruleNode, ruleTriggerNodes, ruleActionNodes, actioncTriggerNode)) {
						//如能满足，获得tj的黑色实线后继节点rule Rj，获得Rj的所有trigger节点Tj，判断Tj在Ri的情况下能否满足
						for(GraphNodeArrow actioncTriggercArrow:actioncTriggerNode.getcNodeList()) {
							if(actioncTriggercArrow.getColor()==null) {
								//rule节点
								GraphNode actioncTriggercRuleNode=actioncTriggercArrow.getGraphNode();
								if(!exitEqual(actioncTriggercRuleNode, ruleAndTriggerRules.triggerRuleNodes)) {
									if(canRuleTriggersAllSatisfied(ruleNode, actioncTriggercRuleNode, actioncTriggerNode, ruleTriggerNodes, ruleActionNodes)) {
										ruleAndTriggerRules.triggerRuleNodes.add(actioncTriggercRuleNode);
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		
		return ruleAndTriggerRules;
	}
	
	public List<TriggerStopRules> getTriggerStopRules(List<GraphNode> triggerNodes){
		//返回能trigger的triggerNodes和stop的RuleNodes
		List<TriggerStopRules> triggersStopRules=new ArrayList<TriggerStopRules>();
		for(GraphNode triggerNode:triggerNodes) {
			boolean canStopRule=false;
			TriggerStopRules triggerStopRules=new TriggerStopRules();
			triggerStopRules.triggerNode=triggerNode;
			for(GraphNodeArrow triggercArrow:triggerNode.getcNodeList()) {
				if(triggercArrow.getLabel()!=null) {
					canStopRule=true;
					GraphNode triggerNegaTriggerNode=triggercArrow.getGraphNode();
//					boolean hasPostEffect=false;
//					for(Arrow triggerNegaTriggerpArrow:triggerNegaTriggerNode.pNodeList) {
//						if(triggerNegaTriggerpArrow.label==null && triggerNegaTriggerpArrow.color!=null && triggerNegaTriggerpArrow.color.indexOf("red")>=0) {
//							hasPostEffect=true;
//							break;
//						}
//					}
//					if(!hasPostEffect) {
//						for(Arrow triggercTriggercArrow:triggerNegaTriggerNode.cNodeList) {
//							if(triggercTriggercArrow.color==null) {
//								GraphNode triggercNegaTriggercRuleNode=triggercTriggercArrow.graphNode;
//								if(!exitEqual(triggercNegaTriggercRuleNode, triggerStopRules.stopRuleNodes)) {
//									triggerStopRules.stopRuleNodes.add(triggercNegaTriggercRuleNode);
//								}
//							}
//						}
//					}
					for(GraphNodeArrow triggercTriggercArrow:triggerNegaTriggerNode.getcNodeList()) {
						if(triggercTriggercArrow.getColor()==null) {
							GraphNode triggercNegaTriggercRuleNode=triggercTriggercArrow.getGraphNode();
							if(!exitEqual(triggercNegaTriggercRuleNode, triggerStopRules.stopRuleNodes)) {
								triggerStopRules.stopRuleNodes.add(triggercNegaTriggercRuleNode);
							}
						}
					}
					
				}else if(triggercArrow.getLabel()==null && triggercArrow.getStyle()==null && triggercArrow.getColor()!=null && triggercArrow.getColor().indexOf("red")>=0) {
					GraphNode triggeredNode=triggercArrow.getGraphNode();
					for(GraphNodeArrow cArrow:triggeredNode.getcNodeList()) {
						if(cArrow.getColor()==null) {
							triggerStopRules.triggerRuleNodes.add(cArrow.getGraphNode());
						}
					}
				}
			}
			if(canStopRule) {
				triggersStopRules.add(triggerStopRules);
			}
		}
		return triggersStopRules;
	}
	
	
	///////////////////////////////////2020.12.22//////////////////////////////////
	public boolean canRuleTriggerRule(GraphNode ruleNode,List<GraphNode> ruleTriggerNodes,GraphNode otherRuleNode) {
		boolean triggerRuleFlag=true;
		//获得该rule的所有trigger节点
		List<GraphNode> otherRuleTriggerNodes=getRuleTriggerNodes(otherRuleNode);
		for(GraphNode otherRuleTriggerNode:otherRuleTriggerNodes) {
			//看有没有跟ruleNode的trigger节点重合或者pNode为ruleNode的trigger节点
			if(exitEqual(otherRuleTriggerNode, ruleTriggerNodes)) {
				//如果跟ruleNode的某个trigger相同
				continue;
			}else {
				//如果跟ruleNode的所有trigger都不相同，就看直接影响该trigger的trigger节点
				int flag=0;
				for(GraphNodeArrow triggerpArrow:otherRuleTriggerNode.getpNodeList()) {
					if(triggerpArrow.getLabel()!=null && triggerpArrow.getLabel().indexOf("+")>=0 && triggerpArrow.getStyle()==null) {
						if(triggerpArrow.getGraphNode().getShape().indexOf("oval")>=0) {
							if(exitEqual(triggerpArrow.getGraphNode(),ruleTriggerNodes)) {
								//存在直接影响该trigger的trigger节点是ruleNode的某个trigger
								flag=1;
								break;
							}
						}
						
					}					
				}
				if(flag==0) {
					//不存在直接影响该trigger的trigger节点是ruleNode的某个trigger
					triggerRuleFlag=false;
					break;
				}
			}
		}
		return triggerRuleFlag;
	}
	
//	//////////////////////////相对于2020.12.23很久以前的///////////////////
//	public List<GraphNode> triggerRules(GraphNode ruleNode,List<GraphNode> ruleTriggerNodes){
//		List<GraphNode> triggerRuleNodes=new ArrayList<GraphNode>();
//		
//		for(GraphNode triggerNode:ruleTriggerNodes) {
//			//trigger节点的后继节点
//			for(Arrow triggercArrow:triggerNode.cNodeList) {
//				if(triggercArrow.color==null) {
//					//为rule节点
//					GraphNode triggerRuleNode=triggercArrow.graphNode;
//					if(triggerRuleNode!=ruleNode) {
//						if(canRuleTriggerRule(ruleNode, ruleTriggerNodes, triggerRuleNode)) {
//							if(!exitEqual(triggerRuleNode, triggerRuleNodes)) {
//								triggerRuleNodes.add(triggerRuleNode);
//							}
//							
//						}
//					}
//				}
//				
//				if(triggercArrow.label!=null && triggercArrow.label.indexOf("+")>=0 && triggercArrow.style==null) {
//					//为trigger节点
//					GraphNode triggercNode=triggercArrow.graphNode;
//					for(Arrow triggercRuleArrow:triggercNode.cNodeList) {
//						if(triggercRuleArrow.color==null) {
//							//找到rule节点
//							GraphNode triggercRuleNode=triggercRuleArrow.graphNode;
//							if(triggercRuleNode!=ruleNode) {
//								if(canRuleTriggerRule(ruleNode, ruleTriggerNodes, triggercRuleNode)) {
//									if(!exitEqual(triggercRuleNode, triggerRuleNodes)) {
//										triggerRuleNodes.add(triggercRuleNode);
//									}
//									
//								}
//							}
//						}
//					}
//				}
//				
//			}
//		}
//		return triggerRuleNodes;
//	}
	
	//////////////////////////////2020.12.22/////////////////////
	//获得rule的所有trigger节点
	public List<GraphNode> getRuleTriggerNodes(GraphNode ruleNode){
		List<GraphNode> ruleTriggerNodes=new ArrayList<GraphNode>();
		for(GraphNodeArrow ruleTriggerArrow:ruleNode.getpNodeList()) {
			//获得该rule节点的所有trigger节点
			GraphNode triggerNode=ruleTriggerArrow.getGraphNode();
			ruleTriggerNodes.add(triggerNode);
		}
		return ruleTriggerNodes;
	}
	//某节点是否存在于某节点List中
	public boolean exitEqual(GraphNode otherNode,List<GraphNode> nodeLists) {
		for(GraphNode node:nodeLists) {
			if(otherNode==node) {
				return true;
			}
		}
		return false;
	}
	
	//////////////////////////////2020.12.22/////////////////////
	//获得rule的所有action节点
	public List<GraphNode> getRuleActionNodes(GraphNode ruleNode){
		List<GraphNode> ruleActionNodes=new ArrayList<GraphNode>();
		for(GraphNodeArrow ruleActionArrow:ruleNode.getcNodeList()) {
			GraphNode ruleActionNode=ruleActionArrow.getGraphNode();
			ruleActionNodes.add(ruleActionNode);
		}
		return ruleActionNodes;
	}
	
	//根据actionNode获得其对应的Action类型中的action
	public Action getActionNodeAction(GraphNode actionNode,List<Action> actions) {
		Action action=new Action();
		for(Action ac:actions) {
			if(ac.action.equals(actionNode.getName())) {
				action=ac;
				break;
			}
		}
		return action;
	}
	
	//////////////////////////2020.12.23/////////////////////////////
	//判断某个trigger能否被某个rule触发
	public boolean canTriggerbeTriggered(GraphNode triggerNode,GraphNode ruleNode,List<GraphNode> ruleTriggerNodes,List<GraphNode> ruleActionNodes,List<Action> actions) {

		if(exitEqual(triggerNode, ruleTriggerNodes)) {
			//trigger为ruleNode的trigger
			return true;
		}else {
			for(GraphNodeArrow pTriggerArrow:triggerNode.getpNodeList()) {
				if(pTriggerArrow.getLabel()!=null && pTriggerArrow.getLabel().indexOf("+")>=0) {
					if(pTriggerArrow.getStyle()==null) {
						if(pTriggerArrow.getGraphNode().getShape().indexOf("oval")>=0) {
							GraphNode pTriggerNode=pTriggerArrow.getGraphNode();
							//影响该trigger的是某个trigger
							if(exitEqual(pTriggerNode, ruleTriggerNodes)) {
								//此trigger是ruleNode的trigger
								return true;
							}
						}else if(pTriggerArrow.getGraphNode().getShape().indexOf("record")>=0) {
							GraphNode pActionNode=pTriggerArrow.getGraphNode();
							//影响该trigger的是某个action
							if(exitEqual(pActionNode, ruleActionNodes)) {
								//action是ruleNode的action
								return true;
							}
						}
					}else if(pTriggerArrow.getStyle()!=null) {
						if(pTriggerArrow.getGraphNode().getShape().indexOf("record")>=0) {
							GraphNode pPostActionNode=pTriggerArrow.getGraphNode();
							Action pPostAction=getActionNodeAction(pPostActionNode, actions);
							//影响该trigger的是某个action
							if(exitEqual(pPostActionNode, ruleActionNodes)) {
								//action是ruleNode的action
								boolean actionBeOffsetFlag=false;
								for(GraphNodeArrow pTriggerOtherArrow:triggerNode.getpNodeList()) {
									//看是否有负影响的
									if(pTriggerOtherArrow.getLabel()!=null && pTriggerOtherArrow.getLabel().indexOf("-")>=0) {
										if(pTriggerOtherArrow.getGraphNode().getShape().indexOf("record")>=0) {
											GraphNode pNegaActionNode=pTriggerOtherArrow.getGraphNode();
											Action pNegaAction=getActionNodeAction(pNegaActionNode, actions);
											if(pNegaAction.device.equals(pPostAction.device)) {
												actionBeOffsetFlag=true;
												break;
											}
											
										}
									}
								}
								if(!actionBeOffsetFlag) {
									return true;
								}
							}
						}
					}
					
				}
			}
		}
		return false;
	}
	
	
	
	/////////////////////////////////////2020.12.23（最新）///////////////////////////
	//获得某个rule能直观触发的一些rule
	public RuleAndTriggerRules getRuleTriggerRules(GraphNode ruleNode,List<Action> actions){
		RuleAndTriggerRules ruleAndTriggerRules=new RuleAndTriggerRules();
		List<GraphNode> ruleActionNodes=getRuleActionNodes(ruleNode);
		List<GraphNode> ruleTriggerNodes=getRuleTriggerNodes(ruleNode);
		ruleAndTriggerRules.ruleNode=ruleNode;
		ruleAndTriggerRules.triggerRuleNodes.add(ruleNode);
		for(GraphNode ruleActionNode:ruleActionNodes) {
			for(GraphNodeArrow cArrow:ruleActionNode.getcNodeList()) {
				if(cArrow.getLabel()!=null && cArrow.getLabel().indexOf("+")>=0) {
					//正影响
					GraphNode ctriggerNode=cArrow.getGraphNode();
					for(GraphNodeArrow triggercArrow:ctriggerNode.getcNodeList()) {
						if(triggercArrow.getColor()==null) {
							//rule节点
							GraphNode triggercRuleNode=triggercArrow.getGraphNode();
							if(!exitEqual(triggercRuleNode, ruleAndTriggerRules.triggerRuleNodes)) {
								//获得该rule的所有trigger
								List<GraphNode> cRuleTriggerNodes=getRuleTriggerNodes(triggercRuleNode);
								boolean canAllBeTriggeredFlag=false;
								for(GraphNode cRuleTriggerNode:cRuleTriggerNodes) {
									if(canTriggerbeTriggered(cRuleTriggerNode, ruleNode, ruleTriggerNodes, ruleActionNodes, actions)) {
										canAllBeTriggeredFlag=true;
									}else {
										canAllBeTriggeredFlag=false;
										break;
									}
								}
								if(canAllBeTriggeredFlag) {
									ruleAndTriggerRules.triggerRuleNodes.add(triggercRuleNode);
								}
							}
						}
					}
				}
			}
		}
		for(GraphNode ruleTriggerNode:ruleTriggerNodes) {
			//trigger节点的后继节点
			for(GraphNodeArrow cArrow:ruleTriggerNode.getcNodeList()) {
				if(cArrow.getColor()==null) {
					//为rule节点
					GraphNode triggerRuleNode=cArrow.getGraphNode();
					if(!exitEqual(triggerRuleNode, ruleAndTriggerRules.triggerRuleNodes)) {
						//获得all trigger节点
						List<GraphNode> cRuleTriggerNodes=getRuleTriggerNodes(triggerRuleNode);
						boolean canAllBeTriggeredFlag=false;
						for(GraphNode cRuleTriggerNode:cRuleTriggerNodes) {
							if(canTriggerbeTriggered(cRuleTriggerNode, ruleNode, ruleTriggerNodes, ruleActionNodes, actions)) {
								canAllBeTriggeredFlag=true;
							}else {
								canAllBeTriggeredFlag=false;
								break;
							}
						}
						if(canAllBeTriggeredFlag) {
							ruleAndTriggerRules.triggerRuleNodes.add(triggerRuleNode);
						}
					}
				}else if(cArrow.getLabel()!=null && cArrow.getLabel().indexOf("+")>=0 && cArrow.getStyle()==null) {
					//trigger节点
					GraphNode cTriggerNode=cArrow.getGraphNode();
					for(GraphNodeArrow cTriggercArrow:cTriggerNode.getcNodeList()) {
						if(cTriggercArrow.getColor()==null) {
							//rule节点
							GraphNode triggerRuleNode=cTriggercArrow.getGraphNode();
							if(!exitEqual(triggerRuleNode, ruleAndTriggerRules.triggerRuleNodes)) {
								//获得all trigger节点
								List<GraphNode> cRuleTriggerNodes=getRuleTriggerNodes(triggerRuleNode);
								boolean canAllBeTriggeredFlag=false;
								for(GraphNode cRuleTriggerNode:cRuleTriggerNodes) {
									if(canTriggerbeTriggered(cRuleTriggerNode, ruleNode, ruleTriggerNodes, ruleActionNodes, actions)) {
										canAllBeTriggeredFlag=true;
									}else {
										canAllBeTriggeredFlag=false;
										break;
									}
								}
								if(canAllBeTriggeredFlag) {
									ruleAndTriggerRules.triggerRuleNodes.add(triggerRuleNode);
								}
							}
						}
					}
				}else if(cArrow.getLabel()!=null && cArrow.getLabel().indexOf("-")>=0) {
					if(cArrow.getStyle()==null) {
						//对trigger有负影响
						boolean negaBeOffsetFlag=false;
						GraphNode cTriggerNode=cArrow.getGraphNode();
						for(GraphNodeArrow cTriggerpArrow:cTriggerNode.getpNodeList()) {
							if(cTriggerpArrow.getLabel()!=null && cTriggerpArrow.getLabel().indexOf("+")>=0) {
								//有正影响的话就抵消了负影响
								negaBeOffsetFlag=true;
								break;
							}
						}
						if(negaBeOffsetFlag==false) {
							//会使得该trigger没法发生
							for(GraphNodeArrow cTriggercArrow:cTriggerNode.getcNodeList()) {
								if(cTriggercArrow.getColor()==null) {
									GraphNode ruleStopRuleNode=cTriggercArrow.getGraphNode();
									if(!exitEqual(ruleStopRuleNode, ruleAndTriggerRules.stopRuleNodes)) {
										ruleAndTriggerRules.stopRuleNodes.add(ruleStopRuleNode);
									}
								}
							}
						}
					}
				}
				
			}
		}
		return ruleAndTriggerRules;
	}
	
	public void getAllTriggerRules(RuleAndTriggerRules ruleAndTriggerRules,List<RuleAndTriggerRules> rulesAndTriggerRules) {
		
		for(int i=0;i<ruleAndTriggerRules.triggerRuleNodes.size();i++) {
			GraphNode ruleNode=ruleAndTriggerRules.triggerRuleNodes.get(i);
			if(ruleNode!=ruleAndTriggerRules.ruleNode) {
				for(RuleAndTriggerRules otherRuleNode:rulesAndTriggerRules) {
					if(ruleNode==otherRuleNode.ruleNode) {
						for(GraphNode otherRuleTriggerNode:otherRuleNode.triggerRuleNodes) {
							if(!exitEqual(otherRuleTriggerNode, ruleAndTriggerRules.triggerRuleNodes)) {
								ruleAndTriggerRules.triggerRuleNodes.add(otherRuleTriggerNode);
							}
						}
						break;
					}
				}
			}
		}
		
	}
//public void getAllTriggerRules(RuleAndTriggerRules ruleAndTriggerRules,List<RuleAndTriggerRules> rulesAndTriggerRules) {
//		
//		ListIterator<GraphNode> triggerRuleNodes=ruleAndTriggerRules.triggerRuleNodes.listIterator();
//		while(triggerRuleNodes.hasNext()) {
//			GraphNode ruleNode=triggerRuleNodes.next();
//			if(ruleNode!=ruleAndTriggerRules.ruleNode) {
//				for(RuleAndTriggerRules otherRuleNode:rulesAndTriggerRules) {
//					if(ruleNode==otherRuleNode.ruleNode) {
//						for(GraphNode otherRuleTriggerNode:otherRuleNode.triggerRuleNodes) {
//							if(!exitEqual(otherRuleTriggerNode, ruleAndTriggerRules.triggerRuleNodes)) {
//								triggerRuleNodes.add(otherRuleTriggerNode);
//							}
//						}
//						break;
//					}
//				}
//			}
//		}
//		
//	}
	
	///////////////////////////////////////2020.12.22/////////////////////////////////
	public List<GraphNode> ruleTriggerRules(GraphNode ruleNode,List<Action> actions){
		List<GraphNode> ruleTriggerRuleNodes=new ArrayList<GraphNode>();
		List<GraphNode> ruleActionNodes=getRuleActionNodes(ruleNode);
		List<GraphNode> ruleTriggerNodes=getRuleTriggerNodes(ruleNode);
		ruleTriggerRuleNodes.add(ruleNode);
		for(GraphNode ruleActionNode:ruleActionNodes) {
			for(GraphNodeArrow cArrow:ruleActionNode.getcNodeList()) {
				if(cArrow.getLabel()!=null && cArrow.getLabel().indexOf("+")>=0) {
					//正影响
					GraphNode ctriggerNode=cArrow.getGraphNode();
					for(GraphNodeArrow triggercArrow:ctriggerNode.getcNodeList()) {
						if(triggercArrow.getColor()==null) {
							//rule节点
							int triggerFlag=0;
							GraphNode triggercRuleNode=triggercArrow.getGraphNode();
							if(!exitEqual(triggercRuleNode, ruleTriggerRuleNodes)) {
								//获得该rule的所有trigger
								List<GraphNode> cRuleTriggerNodes=getRuleTriggerNodes(triggercRuleNode);
								for(GraphNode cRuleTriggerNode:cRuleTriggerNodes) {
									if(exitEqual(cRuleTriggerNode, ruleTriggerNodes)) {
										//该trigger同时也是ruleNode的trigger
										continue;
									}else {
										int flag=0;
										for(GraphNodeArrow cRuleTriggerpArrow:cRuleTriggerNode.getpNodeList()) {
											if(cRuleTriggerpArrow.getLabel()!=null && cRuleTriggerpArrow.getLabel().indexOf("+")>=0 && cRuleTriggerpArrow.getStyle()==null) {
												GraphNode cRuleTriggerpNode=cRuleTriggerpArrow.getGraphNode();
												if(cRuleTriggerpNode.getShape().indexOf("oval")>=0) {
													//trigger
													if(exitEqual(cRuleTriggerpNode, ruleTriggerNodes)) {
														flag=1;
														//影响该trigger的trigger是ruleNode的trigger
														break;
													}
												}
											}
										}
										if(flag==0) {
											for(GraphNodeArrow postcRuleTriggerpArrow:cRuleTriggerNode.getpNodeList()) {
												if(postcRuleTriggerpArrow.getLabel()!=null && postcRuleTriggerpArrow.getLabel().indexOf("+")>=0 && postcRuleTriggerpArrow.getStyle()==null) {
													GraphNode cRuleTriggerpNode=postcRuleTriggerpArrow.getGraphNode();
													if(cRuleTriggerpNode.getShape().indexOf("record")>=0) {
														if(exitEqual(cRuleTriggerpNode, ruleActionNodes)) {
															flag=1;
															break;
														}
													}
												}
												if(postcRuleTriggerpArrow.getLabel()!=null && postcRuleTriggerpArrow.getLabel().indexOf("+")>=0 && postcRuleTriggerpArrow.getStyle()!=null) {
													GraphNode cRuleTriggerpNode=postcRuleTriggerpArrow.getGraphNode();
													if(cRuleTriggerpNode.getShape().indexOf("record")>=0) {
														//action
//														int isRuleActionFlag=0;
//														//这个action得要是ruleNode的action
//														for(Arrow actionRuleArrow:cRuleTriggerpNode.pNodeList) {
//															if(actionRuleArrow.graphNode==ruleNode) {
//																isRuleActionFlag=1;
//																break;
//															}
//														}
														if(exitEqual(cRuleTriggerpNode, ruleActionNodes)) {
															Action postTriggerpAction=getActionNodeAction(cRuleTriggerpNode, actions);
															int actionFlag=0;
															for(GraphNodeArrow negacRuleTriggerpArrow:cRuleTriggerNode.getpNodeList()) {
																if(negacRuleTriggerpArrow.getLabel()!=null && negacRuleTriggerpArrow.getLabel().indexOf("-")>=0) {
																	GraphNode negacRuleTriggerpNode=negacRuleTriggerpArrow.getGraphNode();
																	if(negacRuleTriggerpNode.getShape().indexOf("record")>=0) {
																		//action
																		Action negaTriggerpAction=getActionNodeAction(negacRuleTriggerpNode, actions);
																		if(postTriggerpAction.device.equals(negaTriggerpAction.device)) {
																			//同一设备不同影响，抵消正影响
																			actionFlag=1;
																			break;
																		}
																	}
																}
															}
															if(actionFlag==0) {
																flag=1;
																break;
															}
														}
														
													}
												}
											}
											
										}
										if(flag==0) {
											//ruleNode无法触发该rule
											triggerFlag=1;
											break;
										}
									}
								}
								
								if(triggerFlag==0) {
									ruleTriggerRuleNodes.add(triggercRuleNode);
								}
							}
							
						}
					}
				}
			}
		}
		for(GraphNode triggerNode:ruleTriggerNodes) {
			//trigger节点的后继节点
			for(GraphNodeArrow triggercArrow:triggerNode.getcNodeList()) {
				if(triggercArrow.getColor()==null) {
					//为rule节点
					GraphNode triggerRuleNode=triggercArrow.getGraphNode();
					if(!exitEqual(triggerRuleNode, ruleTriggerRuleNodes)) {
						if(triggerRuleNode!=ruleNode) {
							if(canRuleTriggerRule(ruleNode, ruleTriggerNodes, triggerRuleNode)) {
								ruleTriggerRuleNodes.add(triggerRuleNode);
								
							}
						}
					}
					
				}
				
				if(triggercArrow.getLabel()!=null && triggercArrow.getLabel().indexOf("+")>=0 && triggercArrow.getStyle()==null) {
					//为trigger节点
					GraphNode triggercNode=triggercArrow.getGraphNode();
					for(GraphNodeArrow triggercRuleArrow:triggercNode.getcNodeList()) {
						if(triggercRuleArrow.getColor()==null) {
							//找到rule节点
							GraphNode triggercRuleNode=triggercRuleArrow.getGraphNode();
							if(!exitEqual(triggercRuleNode, ruleTriggerRuleNodes)) {
								if(triggercRuleNode!=ruleNode) {
									if(canRuleTriggerRule(ruleNode, ruleTriggerNodes, triggercRuleNode)) {
										ruleTriggerRuleNodes.add(triggercRuleNode);									
										
									}
								}
							}
							
						}
					}
				}
				
			}
		}
		return ruleTriggerRuleNodes;
	}
	////////////////////////////////////2020.12.22//////////////////////////////////
	public List<GraphNode> actionRules(GraphNode ruleNode,List<GraphNode> ruleActionNodes,List<GraphNode> ruleTriggerNodes,List<Action> actions){
		List<GraphNode> actionRuleNodes=new ArrayList<GraphNode>();
		for(GraphNode ruleActionNode:ruleActionNodes) {
			for(GraphNodeArrow cArrow:ruleActionNode.getcNodeList()) {
				if(cArrow.getLabel()!=null && cArrow.getLabel().indexOf("+")>=0) {
					//正影响
					GraphNode ctriggerNode=cArrow.getGraphNode();
					for(GraphNodeArrow triggercArrow:ctriggerNode.getcNodeList()) {
						if(triggercArrow.getColor()==null) {
							//rule节点
							int triggerFlag=0;
							GraphNode triggercRuleNode=triggercArrow.getGraphNode();
							if(!exitEqual(triggercRuleNode, actionRuleNodes)) {
								List<GraphNode> cRuleTriggerNodes=getRuleTriggerNodes(triggercRuleNode);
								for(GraphNode cRuleTriggerNode:cRuleTriggerNodes) {
									if(exitEqual(cRuleTriggerNode, ruleTriggerNodes)) {
										//该trigger同时也是ruleNode的trigger
										continue;
									}else {
										int flag=0;
										for(GraphNodeArrow cRuleTriggerpArrow:cRuleTriggerNode.getpNodeList()) {
											if(cRuleTriggerpArrow.getLabel()!=null && cRuleTriggerpArrow.getLabel().indexOf("+")>=0 && cRuleTriggerpArrow.getStyle()==null) {
												GraphNode cRuleTriggerpNode=cRuleTriggerpArrow.getGraphNode();
												if(cRuleTriggerpNode.getShape().indexOf("oval")>=0) {
													//trigger
													if(exitEqual(cRuleTriggerpNode, ruleTriggerNodes)) {
														flag=1;
														//影响该trigger的trigger是ruleNode的trigger
														break;
													}
												}
											}
										}
										if(flag==0) {
											for(GraphNodeArrow postcRuleTriggerpArrow:cRuleTriggerNode.getpNodeList()) {
												if(postcRuleTriggerpArrow.getLabel()!=null && postcRuleTriggerpArrow.getLabel().indexOf("+")>=0 && postcRuleTriggerpArrow.getStyle()==null) {
													GraphNode cRuleTriggerpNode=postcRuleTriggerpArrow.getGraphNode();
													if(cRuleTriggerpNode.getShape().indexOf("record")>=0) {
														if(exitEqual(cRuleTriggerpNode, ruleActionNodes)) {
															flag=1;
															break;
														}
													}
												}
												if(postcRuleTriggerpArrow.getLabel()!=null && postcRuleTriggerpArrow.getLabel().indexOf("+")>=0 && postcRuleTriggerpArrow.getStyle()!=null) {
													GraphNode cRuleTriggerpNode=postcRuleTriggerpArrow.getGraphNode();
													if(cRuleTriggerpNode.getShape().indexOf("record")>=0) {
														//action
//														int isRuleActionFlag=0;
//														//这个action得要是ruleNode的action
//														for(Arrow actionRuleArrow:cRuleTriggerpNode.pNodeList) {
//															if(actionRuleArrow.graphNode==ruleNode) {
//																isRuleActionFlag=1;
//																break;
//															}
//														}
														if(exitEqual(cRuleTriggerpNode, ruleActionNodes)) {
															Action postTriggerpAction=getActionNodeAction(cRuleTriggerpNode, actions);
															int actionFlag=0;
															for(GraphNodeArrow negacRuleTriggerpArrow:cRuleTriggerNode.getpNodeList()) {
																if(negacRuleTriggerpArrow.getLabel()!=null && negacRuleTriggerpArrow.getLabel().indexOf("-")>=0) {
																	GraphNode negacRuleTriggerpNode=negacRuleTriggerpArrow.getGraphNode();
																	if(negacRuleTriggerpNode.getShape().indexOf("record")>=0) {
																		//action
																		Action negaTriggerpAction=getActionNodeAction(negacRuleTriggerpNode, actions);
																		if(postTriggerpAction.device.equals(negaTriggerpAction.device)) {
																			//同一设备不同影响，抵消正影响
																			actionFlag=1;
																			break;
																		}
																	}
																}
															}
															if(actionFlag==0) {
																flag=1;
																break;
															}
														}
														
													}
												}
											}
											
										}
										if(flag==0) {
											//ruleNode无法触发该rule
											triggerFlag=1;
											break;
										}
									}
								}
								
								if(triggerFlag==0) {
									actionRuleNodes.add(triggercRuleNode);
								}
							}
							
						}
					}
				}
			}
		}
		return actionRuleNodes;
	}
	
	
}
