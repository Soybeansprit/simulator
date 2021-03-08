package com.example.demo.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Action;
import com.example.demo.bean.GraphNode;
import com.example.demo.bean.GraphNodeArrow;
import com.example.demo.bean.Rule;
import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.TemplGraphNode;
import com.example.demo.bean.TemplTransition;
import com.example.demo.service.AnalyseIFD.RuleAndTriggerRules;
import com.example.demo.service.AnalyseIFD.RuleStyle;
import com.example.demo.service.AnalyseIFD.TriAndTriggerRule;
import com.example.demo.service.AnalyseIFD.TriggerStopRules;


//import com.simulate.GetRule.Rule;
//import com.simulate.GetTemplate.Template;
//import com.simulate.GraphNode.Arrow;
//import com.simulate.TGraphToDot.Action;
//import com.simulate.TemplateGraph.TemplGraph;
//import com.simulate.TemplateGraph.TemplGraphNode;
//import com.simulate.TemplateGraph.TemplTransition;
//import com.simulate.AnalyseIFD.RuleAndTriggerRules;
//import com.simulate.AnalyseIFD.RuleStyle;
//import com.simulate.AnalyseIFD.TriAndTriggerRule;
//import com.simulate.AnalyseIFD.TriggerStopRules;
@Service
public class SetParameter {

	public static void main(String[] args) throws DocumentException {
		// TODO Auto-generated method stub
//		String dotPath="D:\\g5.dot";
//		ToNode toNode=new ToNode();
//		List<GraphNode> graphNodes=new ArrayList<GraphNode>();
//		graphNodes=toNode.getNodes(dotPath);
//		String rulePath="D:\\rules.txt";
//		String path2="D:\\win21.xml";
//		TGraphToDot tDot = new TGraphToDot();
//		AnalyseIFD traverse=new AnalyseIFD();
//		GetRule rt=new GetRule();
//		List<Rule> rules=rt.getRules(rulePath);
//		
//		GetTemplate parse=new GetTemplate();
//		//parse.deletLine(path1, path2, 2);
//		List<Template> templates=parse.getTemplate(path2);
//		
//		TemplateGraph tGraph=new TemplateGraph();
//		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
////		List<TriAndTriggerRule> trTriggerRules=new ArrayList<TriAndTriggerRule>();
////		trTriggerRules=traverse.getTrTriggerRules(graphNodes);
//		SetParameter setParameter=new SetParameter();
//		
//		
//		for(Template template:templates) {
//			templGraphs.add(tGraph.getTemplGraph(template));
//		}
//		List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
//		for(TemplGraph templGraph:templGraphs) {
//			if(templGraph.declaration!=null) {
//				if(templGraph.declaration.indexOf("controlled_device")>=0) {
//					controlledDevices.add(templGraph);
//				}
//			}
//		}
//		List<Action> actions=tDot.getActions(rules,controlledDevices);
//		List<RuleAndTriggerRules> rulesAndTriggerRules=new ArrayList<RuleAndTriggerRules>();
//		List<GraphNode> ruleNodes=new ArrayList<GraphNode>();
//		for(GraphNode graphNode:graphNodes) {
//			if(graphNode.getShape().indexOf("hexagon")>=0) {
//				ruleNodes.add(graphNode);
//			}
//		}
//		rulesAndTriggerRules=traverse.getRulesAndTriggerRules(ruleNodes);
//		
//		for(RuleAndTriggerRules ruleAndTriggerRules:rulesAndTriggerRules) {
//			traverse.getAllTriggerRules(ruleAndTriggerRules, rulesAndTriggerRules);
//		}
//		
//		List<String> attributes=setParameter.getAttributesEffectedByDevice(actions);
//		List<RulesSameAttribute> rulesSameAttributes=setParameter.getRulesWithSameAttribute(rulesAndTriggerRules, attributes, rules);
////		List<String[]> allAttributeValue=setParameter.getAllAttributeValue(rulesSameAttributes);
//		List<RuleAndTriggerRules> chooseRules=new ArrayList<RuleAndTriggerRules>();
////		for(RulesSameAttribute ruleSameAttribute:rulesSameAttributes) {
////			RuleAndTriggerRules chooseRule=setParameter.getChooseRule(ruleSameAttribute);
////			chooseRules.add(chooseRule);
////			
////		}
		
		///////////////////////setParameters/////////////////////////////////////////////////
		//同Analyse IFD获得ruleAndTriggerRules以及triggerStopRules
		//获得所有actions  getAction方法获得
		//利用actions获得causal属性，getAttributesEffectedByDevice方法
		//进而获得涉及这些属性的rules-ruleSameAttributes，使用getRulesWithSameAttribute方法
		//选择相同属性rule中触发规则数最多的rule-chooseRules，使用getChooseRule方法
		//获得chooseRule后，可以对属性赋值  使用getAllAttributeValue方法，这里把chooseRule合并进去了
		/////////////////////////////////////////////////////////////////////////////////////
		
		
		
		
//		List<TriAndTriggerRule> maxTriRules=new ArrayList<TriAndTriggerRule>();
		
		
//		for(TriAndTriggerRule trTriggerRule:trTriggerRules) {
//			setParameter.getTriggerRuleNum(trTriggerRule);
//		}
//		maxTriRules=setParameter.getMaxRules(trTriggerRules);
//		
//		for(TriAndTriggerRule maxRule:maxTriRules) {
//			List<ParameterValue> parameterValues=setParameter.parameterSet(maxRule);
//			for(ParameterValue parameterValue:parameterValues) {
//				if(parameterValue.attribute.indexOf("temperature")>=0) {
//					System.out.println("temperature="+parameterValue.value);
//				}
//				if(parameterValue.attribute.indexOf("brightness")>=0) {
//					System.out.println("brightness="+parameterValue.value);
//				}
//				if(parameterValue.attribute.indexOf("co2ppm")>=0) {
//					System.out.println("co2ppm="+parameterValue.value);
//				}
//				if(parameterValue.attribute.indexOf("humidity")>=0) {
//					System.out.println("humidity="+parameterValue.value);
//				}
//				System.out.println(parameterValue.attribute+"="+parameterValue.value);
//				System.out.println();
//			}
//		}

	}
	
	//获得属性赋值
	public List<String[]> getAllAttributeValue(List<RulesSameAttribute> rulesSameAttributes,List<TriggerStopRules> triggersStopRules){
		
		TGraphToDot tDot = new TGraphToDot();
		List<String[]> allAttributeValue=new ArrayList<String[]>();
		for(RulesSameAttribute rulesSameAttribute:rulesSameAttributes) {
			System.out.println(rulesSameAttribute.attribute);
			
			RuleAndTriggerRules chooseRule=getChooseRule(rulesSameAttribute,triggersStopRules);
			
			GraphNode ruleNode=chooseRule.ruleNode;
			List<String[]> attrVals=new ArrayList<String[]>();
			for(GraphNodeArrow triggerArrow:ruleNode.getpNodeList()) {
				if(triggerArrow.getColor()==null) {
					GraphNode triggerNode=triggerArrow.getGraphNode();
					if(triggerNode.getLabel().indexOf(rulesSameAttribute.attribute)>=0) {
						String[] attrVal=tDot.getAttrVal(triggerNode.getLabel());
						attrVals.add(attrVal);
					}
				}
			}
			String[] attributeValue=setAttributeInitValue(attrVals);
			if(attributeValue!=null) {
				allAttributeValue.add(attributeValue);
			}
			
		}
		return allAttributeValue;
	}
	
	public RuleAndTriggerRules getChooseRule(RulesSameAttribute rulesSameAttribute,List<TriggerStopRules> triggersStopRules) {
		AnalyseIFD traverse=new AnalyseIFD();
		RuleAndTriggerRules chooseRule=traverse.new RuleAndTriggerRules();
		int maxNum=0;
		for(RuleAndTriggerRules ruleAndTriggerRules:rulesSameAttribute.ruleWithSameAttribute) {
			if(ruleAndTriggerRules.triggerRuleNodes.size()>maxNum) {
				boolean canTriggerByBiddable=false;
				for(TriggerStopRules triggerStopRules:triggersStopRules) {
					if(traverse.exitEqual(ruleAndTriggerRules.ruleNode, triggerStopRules.triggerRuleNodes)) {
						canTriggerByBiddable=true;
						break;
					}
				}
				if(!canTriggerByBiddable) {
					maxNum=ruleAndTriggerRules.triggerRuleNodes.size();
					chooseRule=ruleAndTriggerRules;
				}
				
			}
		}
		return chooseRule;
	}
	///////////////////////////////2020.12.27//////////////////////////////
	//////////////不随机，改成确定的值/////////////////////////////
	public String[] setAttributeInitValue(List<String[]> attrVals) {
		if(attrVals.size()==0) {
			return null;
		}
		String[] attributeValue=new String[2];
		attributeValue[0]=attrVals.get(0)[0];
		Double value=null;
		
		DecimalFormat df=new DecimalFormat("#.0");
		if(attrVals.size()==1) {
			String[] attrVal=attrVals.get(0);
			if(attrVal[1].equals(">")) {
				Double threshold=Double.parseDouble(attrVals.get(0)[2]);				
				value=threshold+1.0;
			}else if(attrVal[1].equals("<")) {
				Double threshold=Double.parseDouble(attrVals.get(0)[2]);				
				value=threshold-1.0;
				
			}			
		}else if(attrVals.size()>1) {
			List<String[]> belowAttrVal=new ArrayList<String[]>();
			List<String[]> aboveAttrVal=new ArrayList<String[]>();
			for(String[] attrVal:attrVals) {
				if(attrVal[1].equals(">")) {
					aboveAttrVal.add(attrVal);
				}else if(attrVal[1].equals("<")) {
					belowAttrVal.add(attrVal);
				}
			}
			Double aboveThreshold=null;
			Double belowThreshold=null;
			if(aboveAttrVal.size()>0) {
				aboveThreshold=Double.parseDouble(belowAttrVal.get(0)[2]);
				for(String[] attrVal:aboveAttrVal) {
					if(aboveThreshold<Double.parseDouble(attrVal[2])) {
						aboveThreshold=Double.parseDouble(attrVal[2]);
					}
				}
			}
			if(belowAttrVal.size()>0) {
				belowThreshold=Double.parseDouble(belowAttrVal.get(0)[2]);
				for(String[] attrVal:belowAttrVal) {
					if(belowThreshold>Double.parseDouble(attrVal[2])) {
						belowThreshold=Double.parseDouble(attrVal[2]);
					}
				}
			}
			if(aboveThreshold==null) {
				value=belowThreshold-1.0;
			}else if(belowThreshold==null) {
				value=aboveThreshold+1.0;
			}else if((belowThreshold-aboveThreshold)>0){
				value=aboveThreshold+(belowThreshold-aboveThreshold)/2;
			}
		}
		System.out.println(value);
		String valueStr=df.format(value);
		System.out.println(valueStr);
		attributeValue[1]=valueStr;
		return attributeValue;
	}
	
	///////////////////////////2021/1/12////////////////////////////////
	//根据sensor中的标记获得causal attributes
	public List<String> getCausalAttributes(List<TemplGraph> templGraphs){
		List<String> attributes=new ArrayList<String>();
		//根据sensor中的标记获得causal attributes
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration.indexOf("sensor")>=0) {
				if(templGraph.declaration.indexOf("causal")>=0) {
					for(TemplGraphNode node:templGraph.templGraphNodes) {
						for(TemplTransition inTransition:node.inTransitions) {
							if(inTransition.assignment!=null) {
								String[] assignments=inTransition.assignment.split(",");
								for(String assignment : assignments) {
									assignment=assignment.trim();
									if(assignment.indexOf("get()")>0) {
										String attribute=assignment.substring(0, assignment.indexOf("=")).trim();
										attributes.add(attribute);
									}
								}
							}
						}
					}
				}
			}
		}
		return attributes;
	}
	
	
	/////////////////////2020.12.26////////////////////////////////////////
	//根据templateGraph获得controlledDevice影响的属性，即获得casual attribute
	public List<String> getAttributesEffectedByDevice(List<Action> actions){
		List<String> attributes=new ArrayList<String>();
		for(Action action:actions) {
			for(String[] attrVal:action.attrVal) {
				if(!attrVal[0].equals("temp")) {
					boolean attributeExist=false;
					for(String attribute:attributes) {
						if(attribute.equals(attrVal[0])) {
							attributeExist=true;
							break;
						}
					}
					if(!attributeExist) {
						attributes.add(attrVal[0]);
					}
				}
				
			}
		}
		return attributes;
	}
	
	class RulesSameAttribute{
		String attribute=null;
		List<RuleAndTriggerRules> ruleWithSameAttribute=new ArrayList<RuleAndTriggerRules>();
	}
	
	//获得涉及casual attribute的rule
	public List<RulesSameAttribute> getRulesWithSameAttribute(List<RuleAndTriggerRules> rulesAndTriggerRules,List<String> casualAttributes,List<Rule> rules){
		List<RulesSameAttribute> rulesSameAttributes=new ArrayList<RulesSameAttribute>();
		
		for(String attribute:casualAttributes) {
			RulesSameAttribute rulesSameAttribute=new RulesSameAttribute();
			rulesSameAttribute.attribute=attribute;
			for(Rule rule:rules) {
				for(String trigger:rule.getTrigger()) {
					if(trigger.indexOf(attribute)>=0) {
						for(RuleAndTriggerRules ruleAndTriggerRules:rulesAndTriggerRules) {
							if(ruleAndTriggerRules.ruleNode.getName().equals(rule.getRuleName())) {
								rulesSameAttribute.ruleWithSameAttribute.add(ruleAndTriggerRules);								
								break;
							}
						}
						break;
					}
				}
			}
			if(rulesSameAttribute.ruleWithSameAttribute.size()>0) {
				rulesSameAttributes.add(rulesSameAttribute);
			}
			
		}
		return rulesSameAttributes;
	}
	
	//计算每个trTriggerRule的triggerRuleNum
	public void getTriggerRuleNum(TriAndTriggerRule trTriggerRule) {
		trTriggerRule.triggerRuleNum=0;
		for(int i=0;i<trTriggerRule.triggerRule.positive.size();i++) {
			trTriggerRule.triggerRuleNum++;
		}
		for(RuleStyle negRule:trTriggerRule.triggerRule.negative) {
			for(RuleStyle posRule:trTriggerRule.triggerRule.positive) {
				if(negRule.name.equals(posRule.name)&&posRule.weight==1) {
					trTriggerRule.triggerRuleNum--;
					trTriggerRule.triggerRule.positive.remove(posRule);
					break;
				}
			}
		}
		
	}
	

	
	
	

	
	///////////////////////////////对于2020.12.26很久以前///////////////////////
	//真正可能触发的规则数
	public int getTriRulNum(TriAndTriggerRule trTriggerRule) {
		int num=0;
		for(int i=0;i<trTriggerRule.triggerRule.positive.size();i++) {
			num++;
		}
		for(RuleStyle negRule:trTriggerRule.triggerRule.negative) {
			for(RuleStyle posRule:trTriggerRule.triggerRule.positive) {
				//如果posRule.weight=0则说明是初始trigger，肯定会发生，=2也是肯定会发生的
				if(negRule.name.equals(posRule.name)&&posRule.weight==1) {
					num--;
				}
			}
		}
		return num;
	}
	//trigger能触发的的rules
	public PostiveRuleTrigger getPostRule(TriAndTriggerRule trTriggerRule) {
		PostiveRuleTrigger postiveRuleTrigger=new PostiveRuleTrigger();
		for(RuleStyle posRule:trTriggerRule.triggerRule.positive) {
			int flag=0;
			for(RuleStyle negRule:trTriggerRule.triggerRule.negative) {
				if(negRule.name.equals(posRule.name)&&posRule.weight==1) {
					flag=1;
					break;
				}
			}
			if(flag==0) {
				postiveRuleTrigger.rules.add(posRule);
			}
		}
		return postiveRuleTrigger;
	}
	
	//在所有trigger中找能触发规则最多的trigger
	public List<TriAndTriggerRule> getMaxRules(List<TriAndTriggerRule> trTriggerRules){
		List<TriAndTriggerRule> maxTriRules=new ArrayList<TriAndTriggerRule>();
		for(TriAndTriggerRule trTriggerRule:trTriggerRules) {
			if(trTriggerRule.sensorsName!=null) {
				for(String sensorName:trTriggerRule.sensorsName) {
					if(sensorName.indexOf("person")<0 && sensorName.indexOf("distance")<0 &&sensorName.indexOf("rain")<0) {
						int maxNum=trTriggerRule.triggerRuleNum;
						TriAndTriggerRule maxTriRule=trTriggerRule;
						for(TriAndTriggerRule trTriggerRule2:trTriggerRules) {
							if(trTriggerRule2==trTriggerRule) {
								continue;
							}
							for(String sensorName2:trTriggerRule2.sensorsName) {
								if(sensorName.equals(sensorName2)) {
									int num=trTriggerRule2.triggerRuleNum;
									if(num>maxNum) {
										maxNum=num;
										maxTriRule=trTriggerRule2;
									}
								}
							}
						}
						int flag=0;
						for(int i=0;i<maxTriRules.size();i++) {
							if(maxTriRule==maxTriRules.get(i)) {
								flag=1;
								break;
							}
							for(String senName:maxTriRule.sensorsName) {
								if(senName.indexOf("person")<0 && senName.indexOf("distance")<0 &&senName.indexOf("rain")<0) {
									for(TriAndTriggerRule maxTr:maxTriRules) {
										for(String senName2:maxTr.sensorsName) {
											if(senName.equals(senName2)) {
												flag=1;
												break;
											}
										}
										if(flag==1) {
											break;
										}
									}
								}
								if(flag==1) {
									break;
								}
							}
						}
						if(flag==0) {
							maxTriRules.add(maxTriRule);
						}
						
					}
				}
			}
		}
		return maxTriRules;
	}
	//给每个causal属性设置初始参数值
	public List<ParameterValue> parameterSet(TriAndTriggerRule trTriggerRule) {
		List<ParameterValue> parameterValues=new ArrayList<ParameterValue>();
		ParameterValue parameterValue=new ParameterValue();
		String[] conditions=trTriggerRule.triggerContent.split("AND");
		DecimalFormat df=new DecimalFormat("#.0");
		for(String condition:conditions) {
			if(condition.indexOf("Person")<0 && condition.indexOf("rain")<0) {
				if(condition.indexOf(">")>0) {
					parameterValue.attribute=condition.substring(0, condition.indexOf(">"));
					double value=0;
					if(condition.indexOf("=")>0) {
						if(condition.indexOf("FOR")>0) {
							String valueStr=condition.substring(condition.indexOf("="), condition.indexOf("FOR")).substring("=".length());
							value=Double.parseDouble(valueStr);
							
						}else {
							String valueStr=condition.substring(condition.indexOf("=")).substring("=".length());
							value=Double.parseDouble(valueStr);
						}
					}else {
						if(condition.indexOf("FOR")>0) {
							String valueStr=condition.substring(condition.indexOf(">"), condition.indexOf("FOR")).substring("<".length());
							value=Double.parseDouble(valueStr);
							
						}else {
							String valueStr=condition.substring(condition.indexOf(">")).substring(">".length());
							value=Double.parseDouble(valueStr);
						}
					}
					
					Random random=new Random();
					double ranValue=random.nextDouble()*5+value+5.0;
					double pValue=Double.parseDouble(df.format(ranValue));
					parameterValue.value=pValue;
				}else if(condition.indexOf("<")>0) {
					parameterValue.attribute=condition.substring(0, condition.indexOf("<"));
					double value=0;
					if(condition.indexOf("=")>0) {
						if(condition.indexOf("FOR")>0) {
							String valueStr=condition.substring(condition.indexOf("="), condition.indexOf("FOR")).substring("=".length());
							value=Double.parseDouble(valueStr);
							
						}else {
							String valueStr=condition.substring(condition.indexOf("=")).substring("=".length());
							value=Double.parseDouble(valueStr);
						}
					}else {
						if(condition.indexOf("FOR")>0) {
							String valueStr=condition.substring(condition.indexOf("<"), condition.indexOf("FOR")).substring("<".length());
							value=Double.parseDouble(valueStr);
							
						}else {
							String valueStr=condition.substring(condition.indexOf("<")).substring("<".length());
							value=Double.parseDouble(valueStr);
						}
					}
					Random random=new Random();
					double ranValue=value-(random.nextDouble()*5+5.0);
					double pValue=Double.parseDouble(df.format(ranValue));
					parameterValue.value=pValue;
				}else if(condition.indexOf("=")>0){
					parameterValue.attribute=condition.substring(0, condition.indexOf("="));
					double value=0;
					if(condition.indexOf("FOR")>0) {
						String valueStr=condition.substring(condition.indexOf("="), condition.indexOf("FOR")).substring("=".length());
						value=Double.parseDouble(valueStr);
						
					}else {
						String valueStr=condition.substring(condition.indexOf("=")).substring("=".length());
						value=Double.parseDouble(valueStr);
					}
					
					double pValue=Double.parseDouble(df.format(value));
					parameterValue.value=pValue;
				}
				parameterValues.add(parameterValue);
			}
		}
		return parameterValues;
	}
	
	class ParameterValue{
		String attribute=null;
		double value=0;
	}
	
	class PostiveRuleTrigger{
		List<RuleStyle> rules=new ArrayList<RuleStyle>();
	}

}
