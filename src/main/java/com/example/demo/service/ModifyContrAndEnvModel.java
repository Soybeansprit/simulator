package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;

import com.example.demo.bean.GraphNode;
import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.TemplGraphNode;
import com.example.demo.bean.TemplTransition;
import com.example.demo.service.AnalyseIFD.RuleAndTriggerRules;
import com.example.demo.service.AnalyseIFD.TriggerStopRules;

//import com.simulate.GetRule.Rule;
//import com.simulate.GetTemplate.Template;
//import com.simulate.GraphNode.Arrow;
//import com.simulate.SetParameter.RulesSameAttribute;
//import com.simulate.TGraphToDot.Action;
//import com.simulate.TemplateGraph.TemplGraph;
//import com.simulate.TemplateGraph.TemplGraphNode;
//import com.simulate.TemplateGraph.TemplTransition;
//import com.simulate.AnalyseIFD.RuleAndTriggerRules;
//import com.simulate.AnalyseIFD.RuleStyle;
//import com.simulate.AnalyseIFD.TriAndTriggerRule;
//import com.simulate.AnalyseIFD.TriggerStopRules;
@Service
public class ModifyContrAndEnvModel {

	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
//		String dotPath="D:\\g5.dot";
//		ToNode toNode=new ToNode();
//		ModifyContrAndEnvModel modifyModel=new ModifyContrAndEnvModel();
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
//		List<TemplGraph> biddables=new ArrayList<TemplGraph>();
//		for(TemplGraph templGraph:templGraphs) {
//			if(templGraph.declaration!=null) {
//				if(templGraph.declaration.indexOf("controlled_device")>=0) {
//					controlledDevices.add(templGraph);
//				}
//				if(templGraph.declaration.indexOf("biddable")>=0) {
//					biddables.add(templGraph);
//				}
//			}
//		}
//		
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
//		List<RuleAndTriggerRules> chooseRules=new ArrayList<RuleAndTriggerRules>();
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
//		for(RulesSameAttribute ruleSameAttribute:rulesSameAttributes) {
//			RuleAndTriggerRules chooseRule=setParameter.getChooseRule(ruleSameAttribute,triggersStopRules);
//			chooseRules.add(chooseRule);
//			
//		}
//		List<String[]> allAttributeValue=setParameter.getAllAttributeValue(rulesSameAttributes,triggersStopRules);
//		modifyModel.modifyContrEnvModel(path2,path2, biddables, rulesAndTriggerRules, chooseRules, triggersStopRules);
//		
		////////////////////////////////modify model//////////////////////////////////////
		//主要是修改biddable类型的model，一般除了person相关的model
		//这是在获得triggerStopRules获得之后进行修改的，biddable模型的进行前提是某些rule发生后
		//其实这里可以删除rulesAndTriggerRules的吧
		//使用modifyContrEnvModel方法，
		//////首先保证“某些rule”确实能被触发，只要看是否在chooseRules以及chooseRules触发的rules中即可
		//////修改模型时暂时只考虑了stop one rule的情况
		//////////////////////////////////////////////////////////////////////////////////
		

	}
	public void modifyContrEnvModel(String inputPath,String outputPath,List<TemplGraph> biddables,List<RuleAndTriggerRules> rulesAndTriggerRules,List<RuleAndTriggerRules> chooseRules,List<TriggerStopRules> triggersStopRules) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(inputPath));
		Element rootElement=document.getRootElement();
		List<Element> templateElements=rootElement.elements("template");
		//某条规则会使得其他规则没法发生，故该条规则需要在这些规则发生之后发生，但有些规则在选择的规则中如果无法发生则不能在这些规则之后发生
		for(RuleAndTriggerRules ruleAndTriggerRules:rulesAndTriggerRules) {
			if(ruleAndTriggerRules.stopRuleNodes.size()>0) {
				Iterator<GraphNode> stopRuleNodesIterator=ruleAndTriggerRules.stopRuleNodes.iterator();
				while(stopRuleNodesIterator.hasNext()) {
					GraphNode ruleNode=stopRuleNodesIterator.next();
					boolean canRuleHappen=false;
					for(RuleAndTriggerRules chooseRule:chooseRules) {
						for(GraphNode triggerRuleNode:chooseRule.triggerRuleNodes) {
							if(ruleNode==triggerRuleNode) {
								canRuleHappen=true;
								break;
							}
						}
						if(canRuleHappen) {
							break;
						}
					}
					if(!canRuleHappen) {
						stopRuleNodesIterator.remove();
					}
				}
			}
		}
		modifyTriggerStopRules(triggersStopRules, chooseRules);
		for(TriggerStopRules triggerStopRules:triggersStopRules) {
			if(triggerStopRules.stopRuleNodes.size()>0) {
				String attribute=triggerStopRules.triggerNode.getLabel();
				//寻找biddable类型属性相关的biddable实体
				for(TemplGraph biddable:biddables) {
					boolean flag=false;
					for(TemplGraphNode node:biddable.templGraphNodes) {
						if(node.invariant!=null && node.invariant.indexOf(attribute)>=0) {
							flag=true;
							break;
						}
						for(TemplTransition inTransition:node.inTransitions) {
							if(inTransition.assignment!=null && inTransition.assignment.indexOf(attribute)>=0) {
								flag=true;
								break;
							}
						}
						if(flag) {
							break;
						}
					}
					
					
					if(flag) {
						String biddableName=biddable.name.trim();
						for(Element templateElement:templateElements) {
							Element nameElement=templateElement.element("name");
							String name=nameElement.getTextTrim();
							
							if(name.equals(biddableName)) {
								//修改biddable类型实体
								List<Element> locationElements=templateElement.elements("location");
								List<Element> transitionEnvElements=templateElement.elements("transition");
								Element initElement=templateElement.element("init");
								Attribute initAttribute=initElement.attribute("ref");
								String initId=initAttribute.getValue();
								for(Element locationElement:locationElements) {
									//初始节点去掉urgent
									Attribute locationId=locationElement.attribute("id");
									String id=locationId.getValue();
									if(id.equals(initId)) {
										Element urgentElement=locationElement.element("urgent");
										if(urgentElement!=null) {
											locationElement.remove(urgentElement);
										}
										List<Element> labelElements=locationElement.elements("label");
										boolean hasInvariant=false;
										for(Element element:labelElements) {
											Attribute kind=element.attribute("kind");
											String kindStr=kind.getValue();
											if(kindStr.equals("invariant")) {
												hasInvariant=true;
												element.setText("t<=1");
											}
										}
										if(!hasInvariant) {
											Element labelElement=DocumentHelper.createElement("label");												
											labelElement.addAttribute("kind", "invariant");
											labelElement.setText("t<=1");
											labelElements.add(labelElement);
										}
										
										Element transitionElement=DocumentHelper.createElement("transition");
										Element sourceIdElement=transitionElement.addElement("source");
										Element targetIdElement=transitionElement.addElement("target");
										sourceIdElement.addAttribute("ref", initId);
										targetIdElement.addAttribute("ref", initId);
										List<Element> transitionLabelElements=transitionElement.elements("label");
										Element guardElement=DocumentHelper.createElement("label");
										guardElement.addAttribute("kind", "guard");
										guardElement.setText("t>=1 && "+triggerStopRules.stopRuleNodes.get(0).getName()+"==0");
										transitionLabelElements.add(guardElement);
										Element assignmentElement=DocumentHelper.createElement("label");
										assignmentElement.addAttribute("kind", "assignment");
										assignmentElement.setText("t=0");
										transitionLabelElements.add(assignmentElement);
										transitionEnvElements.add(transitionElement);
										break;
									}
									
									
								}
								//
								for(Element transitionElement:transitionEnvElements) {
									Element sourceIdElement=transitionElement.element("source");
									String sourceId=sourceIdElement.attribute("ref").getValue();
									if(sourceId.equals(initId)) {
										if(triggerStopRules.stopRuleNodes.size()==1) {						
											List<Element> labelElements=transitionElement.elements("label");
											boolean hasGuardLabel=false;
											boolean hasAssignmentLabel=false;
											for(Element element:labelElements) {
												Attribute kind=element.attribute("kind");
												String kindStr=kind.getValue();
												if(kindStr.equals("guard")) {
													hasGuardLabel=true;
													element.setText("t>=1 && "+triggerStopRules.stopRuleNodes.get(0).getName()+"==1");
												}
												if(kindStr.equals("assignment")) {
													hasAssignmentLabel=true;
													element.setText("t=0");
												}
											}
											if(!hasGuardLabel) {
												Element guardElement=DocumentHelper.createElement("label");	
												guardElement.addAttribute("kind", "guard");
												guardElement.setText("t>=1 && "+triggerStopRules.stopRuleNodes.get(0).getName()+"==1");
												labelElements.add(0,guardElement);
											}
											if(!hasAssignmentLabel) {
												Element assignmentElement=DocumentHelper.createElement("label");
												assignmentElement.addAttribute("kind", "assignment");
												assignmentElement.setText("t=0");
												labelElements.add(assignmentElement);
											}
											
											
											
										}
										
										break;
									}
								}
								
								
								
							}
						}
						break;
					}
				}
			}
		}
		OutputStream os=new FileOutputStream(outputPath);
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		format.setTrimText(false); //保留换行，但是出现空行
		format.setIndent(true);
		format.setNewlines(false);
		XMLWriter writer=new XMLWriter(os,format);
		writer.write(document);
		writer.close();
		os.close();
	}
	
	public void modifyTriggerStopRules(List<TriggerStopRules> triggersStopRules,List<RuleAndTriggerRules> chooseRules) {
		for(TriggerStopRules triggerStopRules:triggersStopRules) {
			if(triggerStopRules.stopRuleNodes.size()>0) {
				Iterator<GraphNode> stopRuleNodesIterator=triggerStopRules.stopRuleNodes.iterator();
				while(stopRuleNodesIterator.hasNext()) {
					GraphNode stopRuleNode=stopRuleNodesIterator.next();
					boolean canRuleHappen=false;
					for(RuleAndTriggerRules chooseRule:chooseRules) {
						for(GraphNode triggerRuleNode:chooseRule.triggerRuleNodes) {
							if(stopRuleNode==triggerRuleNode) {
								canRuleHappen=true;
								break;
							}
						}
						if(canRuleHappen) {
							break;
						}
					}
					if(!canRuleHappen) {
						stopRuleNodesIterator.remove();
					}
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	

}
