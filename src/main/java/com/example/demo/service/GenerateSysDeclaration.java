package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Parameter;
import com.example.demo.bean.Rule;
import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.TemplGraphNode;
import com.example.demo.bean.TemplTransition;
import com.example.demo.service.AnalyseIFD.RuleStyle;
import com.example.demo.service.AnalyseIFD.TriAndTriggerRule;
import com.example.demo.service.GetTemplate.Label;
import com.example.demo.service.GetTemplate.Location;
import com.example.demo.service.GetTemplate.Template;
import com.example.demo.service.GetTemplate.Transition;
import com.example.demo.service.SetParameter.ParameterValue;

import org.dom4j.Attribute;

//import com.simulate.SetParameter.ParameterValue;
//import com.simulate.SetParameter.RulesSameAttribute;
//import com.simulate.TGraphToDot.Action;
//import com.simulate.TemplateGraph.TemplGraph;
//import com.simulate.TemplateGraph.TemplGraphNode;
//import com.simulate.TemplateGraph.TemplTransition;
//import com.simulate.AnalyseIFD.RuleAndTriggerRules;
//import com.simulate.AnalyseIFD.RuleStyle;
//import com.simulate.AnalyseIFD.TriAndTriggerRule;
//import com.simulate.AnalyseIFD.TriggerStopRules;
//import com.simulate.GetTemplate.Label;
//import com.simulate.GetTemplate.Location;
//import com.simulate.GetTemplate.Template;
//import com.simulate.GetTemplate.Transition;
//import com.simulate.GetRule.Rule;
@Service
public class GenerateSysDeclaration {

	public static void main(String[] args) throws DocumentException, IOException {
//		// TODO Auto-generated method stub
////		String xmlPath="D:\\win18.xml";
////		String dotPath="D:\\g2.dot";
////		ToNode toNode=new ToNode();
////		List<GraphNode> graphNodes=new ArrayList<GraphNode>();
////		graphNodes=toNode.getNodes(dotPath);
////		Traverse traverse=new Traverse();
////		List<TriAndTriggerRule> trTriggerRules=new ArrayList<TriAndTriggerRule>();
////		trTriggerRules=traverse.getTrTriggerRules(graphNodes);
////		GetTemplate parse=new GetTemplate();
//		GenerateSysDeclaration gSysDeclar=new GenerateSysDeclaration();
////		List<Template> templates=parse.getTemplate(xmlPath);
////		String rulePath="D:\\rules.txt";
////		GetRule rt=new GetRule();
////		List<Rule> rules=rt.getRules(rulePath);
////		gSysDeclar.modifyModel(xmlPath, templates, trTriggerRules, rules);
////		templates=parse.getTemplate(xmlPath);
////		List<Parameter> parameters=new ArrayList<Parameter>();
////		parameters=gSysDeclar.getParameters(templates);
////		SetParameter setParameter=new SetParameter();
////		List<TriAndTriggerRule> maxTriRules=new ArrayList<TriAndTriggerRule>();
////		
////		
////		for(TriAndTriggerRule trTriggerRule:trTriggerRules) {
////			setParameter.getTriggerRuleNum(trTriggerRule);
////		}
////		maxTriRules=setParameter.getMaxRules(trTriggerRules);
////		
////		gSysDeclar.setInitValueOfParameters(maxTriRules, parameters);
////		//给各参数赋初值并写入xml文件中
////		gSysDeclar.systemDeclaration(xmlPath, parameters);
////		//模型声明
////		gSysDeclar.modelDeclaration(xmlPath, templates);
////		for(Parameter parameter:parameters) {
////		
////		
////		
////			if(parameter.name.equals("humidity")) {
////				
////			}
////		}
////		//验证器验证内容
////		gSysDeclar.setQuery(xmlPath);
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
//		List<Parameter> parameters=gSysDeclar.getParameters(templGraphs, allAttributeValue);
//		for(Parameter parameter:parameters) {
//			System.out.println(parameter.style+": ");
//			System.out.println("  "+parameter.name);
//			System.out.println("  "+parameter.initValue);
//		}
//		
//		for(Parameter parameter:parameters) {
//			if(parameter.initValue==null) {
//				if(parameter.name.indexOf("distance")>=0) {
//					if(parameter.name.indexOf("[rid]")>=0) {
//						parameter.name=parameter.name.replace("[rid]", "[5]");
//						parameter.initValue= "{10.0,10.0,10.0,10.0,10.0}";
//					}else {
//						parameter.initValue="10.0";
//					}
//				}
//				
//			}
//		}
//		for(Parameter parameter:parameters) {
//			System.out.println(parameter.style+": ");
//			System.out.println("  "+parameter.name);
//			System.out.println("  "+parameter.initValue);
//		}
//		List<String> instances=new ArrayList<String>();
//		String inst0="inst0=PersonDistanceInstance0(5.0,10.0,40.0,180.0,200.0,0,-0.5,-0.5);";
//		String inst1="inst1=PersonDistanceInstance0(5.0,10.0,40.0,180.0,200.0,1,-0.6,-0.6);";
//		String inst2="inst2=PersonDistanceInstance1(5.0,10.0,40.0,180.0,200.0,2,-0.6,-0.6);";
//		instances.add(inst0);
//		instances.add(inst1);
//		instances.add(inst2);
//		
//		gSysDeclar.globalDeclaration(path2, path2,parameters);
//		gSysDeclar.modelDeclaration(path2,path2, templGraphs,instances);
//		gSysDeclar.setQuery(path2,path2, parameters,"210");
		
		/////////////////////////system declaration/////////////////////////////////////////////////
		//先全局声明，需要获得涉及到的所有parameters，使用getParameters方法
		//这之前需要前面先获得causal属性的initValue
		//更改parameters：对于含有distance的parameter，其value值设置为10.0，含[rid]的为数组，distance相关，同样赋值
		//根据parameters对xml文件进行全局声明，使用globalDeclaration方法
		//模型声明，先对person相关进行参数实例化，再对所有模型实例化
		//验证器验证仿真参数生成，直接根据parameters获得，不考虑synchronisation
		/////////////////////////////////////////////////////////////////////////////////////////////

	}
	
	
	//参数声明格式
	
	
	public void setQuery(String inputPath,String outputPath,List<Parameter> parameters,String time) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(inputPath));
		Element rootElement=document.getRootElement();
		List<String> queries=new ArrayList<String>();
		
		for(Parameter parameter:parameters) {
			if(parameter.getStyle().indexOf("chan")<0 && parameter.getName().indexOf("[")<0) {
				queries.add(parameter.getName());
			}
		}
		
		Element queriesElement=rootElement.element("queries");
		Element queryElement=queriesElement.element("query");
		Element formulaElement=queryElement.element("formula");
		String formula="simulate[<="+time+"] {";
		for(int i=0;i<queries.size();i++) {
			if(i<queries.size()-1) {
				formula=formula+queries.get(i)+",";
			}
			if(i==queries.size()-1) {
				formula=formula+queries.get(i)+"}";
			}
		}
		formulaElement.setText(formula);
		
		
		OutputStream os=new FileOutputStream(outputPath);
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		format.setTrimText(false); //保留换行，但是出现空行
		format.setNewlines(false);
		XMLWriter writer=new XMLWriter(os,format);
		writer.write(document);
		writer.close();
		os.close();
	}
	/////////////////////////////获得仿真数据数/////////////////////////
	public int getSimulateDataNum(String modelFilePathName) throws DocumentException {
		
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(modelFilePathName));
		Element rootElement=document.getRootElement();
		Element queriesElement=rootElement.element("queries");
		Element queryElement=queriesElement.element("query");
		Element formulaElement=queryElement.element("formula");
		String formulaString=formulaElement.getTextTrim();
		String dataString=formulaString.substring(formulaString.indexOf("{"), formulaString.indexOf("}")).substring("{".length());
		String[] dataEles=dataString.split(",");
		return dataEles.length;
	}
	
	public void modelDeclaration(String inputPath,String outputPath,List<TemplGraph> templGraphs,List<String> instances) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(inputPath));
		Element rootElement=document.getRootElement();
		Element systemElement=rootElement.element("system");
		//实例化：
		
		String system="system ";
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.parameter==null) {
				if(templGraph.name.indexOf("sensor")>0) {
					if(templGraph.name.indexOf("distance")<0) {
						continue;
					}
				}
				system=system+templGraph.name+",";
			}
		}
		String text="";
		for(int i=0;i<instances.size();i++) {
			text=text+"\n"+instances.get(i);
			if(i<instances.size()-1) {
				system=system+instances.get(i).substring(0, instances.get(i).indexOf("="))+",";
			}
			if(i==instances.size()-1) {
				system=system+instances.get(i).substring(0, instances.get(i).indexOf("="))+";";
			}
		}
		text=text+"\n"+system;
		systemElement.setText(text);
		OutputStream os=new FileOutputStream(outputPath);
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		format.setTrimText(false); //保留换行，但是出现空行
		format.setNewlines(false);
		XMLWriter writer=new XMLWriter(os,format);
		writer.write(document);
		writer.close();
		os.close();
	}
	
	

	
	public Parameter getSameParameter(List<Parameter> parameters,String name) {
		for(Parameter parameter:parameters) {
			if(parameter.getName().equals(name)) {
				return parameter;
			}
		}
		return null;
	}
	
	public void globalDeclaration(String inputPath,String outputPath,List<Parameter> parameters) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(inputPath));
		Element rootElement=document.getRootElement();
		
		Element declarationElement=rootElement.element("declaration");
		String text="";
		for(Parameter parameter:parameters) {
			if(parameter.getInitValue()!=null) {
				text=text+parameter.getStyle() +" "+parameter.getName()+"="+parameter.getInitValue()+";\n";
			}else {
				text=text+parameter.getStyle()+" "+parameter.getName()+";\n";
			}
		}
		declarationElement.setText(text);
		OutputStream os=new FileOutputStream(outputPath);
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		format.setTrimText(false);
		format.setNewlines(true);
		XMLWriter writer=new XMLWriter(os,format);
		writer.write(document);
		writer.close();
		os.close();
	}
	
	
	
	public List<Parameter> getParameters(List<TemplGraph> templGraphs, List<String[]> allAttributeValue){
		List<Parameter> parameters=new ArrayList<Parameter>();
		for(String[] attributeValue:allAttributeValue) {
			Parameter parameter=new Parameter();
			parameter.setName(attributeValue[0]);
			parameter.setInitValue(attributeValue[1]);
			parameter.setStyle("double");
			parameters.add(parameter);
		}
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.name.indexOf("Rule")>=0) {
				//规则参数
				Parameter parameter=new Parameter();
				String pname=templGraph.name.substring(0, 1).toLowerCase()+templGraph.name.substring(1);
				parameter.setName(pname);
				parameter.setStyle("int[0,1]");
				parameter.setInitValue("0");
				parameters.add(parameter);
			}else if(templGraph.declaration!=null && templGraph.declaration.indexOf("controlled_device")>=0) {
				//设备状态参数
				Parameter parameter=new Parameter();
				String pname=templGraph.name.substring(0, 1).toLowerCase()+templGraph.name.substring(1);
				parameter.setName(pname);
				int stateNum=templGraph.templGraphNodes.size();
				parameter.setStyle("int[0,"+(stateNum-1)+"]");
				parameter.setInitValue("0");
				parameters.add(parameter);
				for(TemplGraphNode stateNode:templGraph.templGraphNodes) {
					//设备会改变causal 属性，定义causal属性参数，无临时变量
					if(stateNode.invariant!=null) {
						String[] invariants=stateNode.invariant.split("&&");
						for(String invariant:invariants) {
							invariant=invariant.trim();
							if(invariant.indexOf("'==")>0) {
								String name=invariant.substring(0, invariant.indexOf("'=="));
								Parameter sameParameter=getSameParameter(parameters, name);
								if(sameParameter==null) {
									Parameter biddableParameter=new Parameter();
									biddableParameter.setName(name);
									biddableParameter.setStyle("clock");
									parameters.add(biddableParameter);
								}else {
									if(!sameParameter.getStyle().equals("clock")) {
										sameParameter.setStyle("clock");
									}
								}
							}
						}
					}
					for(TemplTransition inTransition:stateNode.inTransitions) {
						if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
							String name=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
							if(getSameParameter(parameters, name)==null) {
								Parameter synchroParameter=new Parameter();
								synchroParameter.setStyle("urgent broadcast chan");
								synchroParameter.setName(name);
								parameters.add(synchroParameter);
							}
						}
					}
					
				}
			}else if(templGraph.declaration!=null && templGraph.declaration.indexOf("biddable")>=0 && 
					templGraph.declaration.indexOf("sensor")<0) {
				if(templGraph.name.indexOf("Person")<0) {
					//与人无关的biddable类型实体
					Parameter parameter=new Parameter();
					int stateNum=0;
					for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
						if(templGraphNode.name!=null) {
							stateNum++;
						}
					}
					parameter.setStyle("int[0,"+(stateNum-1)+"]");
					parameter.setInitValue("0");
					for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
						if(templGraphNode.name!=null) {
							for(TemplTransition inTransition:templGraphNode.inTransitions) {
								if(inTransition.assignment!=null) {
									String[] assignments=inTransition.assignment.split(",");
									for(String assignment:assignments) {
										assignment=assignment.trim();
										String name=assignment.substring(0, assignment.indexOf("="));
										if(!name.equals("t") && getSameParameter(parameters, name)==null) {
											parameter.setName(name);
											break;
										}
									}
									break;
								}
							}
							break;
						}
					}
					parameters.add(parameter);
				}else {
					for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
						if(templGraphNode.invariant!=null) {
							String[] invariants=templGraphNode.invariant.split("&&"); 
							for(String invariant:invariants) {
								invariant=invariant.trim();
								String name=null;
								if(invariant.indexOf("'==")>0) {
									name=invariant.substring(0, invariant.indexOf("'=="));
								}else if(invariant.indexOf(">")>0) {
									name=invariant.substring(0, invariant.indexOf(">"));
								}else if(invariant.indexOf("<")>=0) {
									name=invariant.substring(0, invariant.indexOf("<"));								
								}
								if(name.equals("t")) {
									continue;
								}else {
									Parameter sameParameter=getSameParameter(parameters, name);
									if(sameParameter==null) {
										Parameter biddableParameter=new Parameter();
										biddableParameter.setName(name);
										biddableParameter.setStyle("clock");
										parameters.add(biddableParameter);
									}else {
										if(!sameParameter.getStyle().equals("clock")) {
											sameParameter.setStyle("clock");
										}
									}
								}
							}
						}
						for(TemplTransition inTransition:templGraphNode.inTransitions) {
							if(inTransition.assignment!=null) {
								String[] assignments=inTransition.assignment.split(",");
								for(String assignment:assignments) {
									assignment=assignment.trim();
									if(assignment.indexOf("++")>0) {
										String name=assignment.substring(0, assignment.indexOf("++"));
										if(getSameParameter(parameters, name)==null) {
											Parameter biddableParameter=new Parameter();
											biddableParameter.setName(name);
											biddableParameter.setStyle("int");
											biddableParameter.setInitValue("0");
											parameters.add(biddableParameter);
										}
									}else if(assignment.indexOf("--")>0) {
										String name=assignment.substring(0, assignment.indexOf("--"));
										if(getSameParameter(parameters, name)==null) {
											Parameter biddableParameter=new Parameter();
											biddableParameter.setName(name);
											biddableParameter.setStyle("int");
											biddableParameter.setInitValue("0");
											parameters.add(biddableParameter);
										}
									}
								}
							}
						}
					}
				}
				
			}else if(templGraph.declaration!=null && templGraph.declaration.indexOf("sensor")>=0) {
				for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
					boolean hasInvariantVar=false;
					if(templGraphNode.invariant!=null) {
						String[] invariants=templGraphNode.invariant.split("&&");
						for(String invariant:invariants) {
							invariant=invariant.trim();
							String name=null;
							if(invariant.indexOf("'==")>0) {
								name=invariant.substring(0, invariant.indexOf("'=="));
								
							}else if(invariant.indexOf(">")>0) {
								name=invariant.substring(0, invariant.indexOf(">"));
							}else if(invariant.indexOf("<")>0) {
								name=invariant.substring(0, invariant.indexOf("<"));
							}
							if(!name.equals("t") && getSameParameter(parameters, name)==null) {
								hasInvariantVar=true;
								Parameter parameter=new Parameter();
								parameter.setName(name);
								parameter.setStyle("clock");
								parameters.add(parameter);
							}
						}
					}
					if(!hasInvariantVar) {
						String[] declarations=templGraph.declaration.split("\n");
						String getString=null;
						for(String declaration:declarations) {
							declaration=declaration.trim();
							if(declaration.indexOf("get()")>0) {
								getString=declaration;
								break;
							}
						}
						for(TemplTransition inTransition:templGraphNode.inTransitions) {
							if(inTransition.assignment!=null) {
								String[] assignments=inTransition.assignment.split(",");
								for(String assignment:assignments) {
									assignment=assignment.trim();
									if(assignment.indexOf("get()")>0) {
										String name=assignment.substring(0,assignment.indexOf("=")).trim();
										if(!name.equals("t")&& getSameParameter(parameters, name)==null) {
											String style=getString.substring(0, getString.indexOf(" ")).trim();
											Parameter parameter=new Parameter();
											parameter.setName(name);
											parameter.setStyle(style);
											parameters.add(parameter);
										}
									}
									
								}
							}
						}
					}
				}
			}
		}
		
		return parameters;
	}
	
	//模型检测内容
	public void setQuery(String xmlPath) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(xmlPath));
		Element rootElement=document.getRootElement();
		Element systemElement=rootElement.element("system");
		Element declarationElement=rootElement.element("declaration");
		String systemContent=systemElement.getTextTrim();
		String declarationContent=declarationElement.getTextTrim();
		String[] systems=systemContent.split(";");
		String[] declarations=declarationContent.split(";");
		
		List<String> queries=new ArrayList<String>();
		for(String declaration:declarations) {
			declaration=declaration.trim();
			if(declaration.indexOf("chan")>=0) {
				continue;
			}
			String[] parameters=declaration.split(" ");
			if(parameters[1].indexOf("[")<0) {
				String parameter="";
				if(parameters[1].indexOf("=")>0) {
					parameter=parameters[1].substring(0, parameters[1].indexOf("="));
				}else {
					parameter=parameters[1];
				}
				 
				queries.add(parameter);
			}
		}
		for(String system:systems) {
			if(system.indexOf("system")>=0) {
				int systemIndex=system.indexOf("system");
				system=system.substring(systemIndex).substring("system".length());
				String[] models=system.split(",");
				for(String model:models) {
					model=model.trim();
					if(model.indexOf("Rule")>=0) {
						String parameter=model+"."+model.substring(0, 1).toLowerCase()+model.substring(1);
						queries.add(parameter);
					}
				}
			}
		}
		Element queriesElement=rootElement.element("queries");
		Element queryElement=DocumentHelper.createElement("query");
		Element formulaElement=queryElement.addElement("formula");
		String formula="simulate[<=210] {";
		for(int i=0;i<queries.size();i++) {
			if(i<queries.size()-1) {
				formula=formula+queries.get(i)+",";
			}
			if(i==queries.size()-1) {
				formula=formula+queries.get(i)+"}";
			}
		}
		formulaElement.setText(formula);
		queryElement.addElement("comment");
		queriesElement.add(queryElement);
		OutputStream os=new FileOutputStream(xmlPath);
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
	
	//模型修改
	public void modifyModel(String xmlPath,List<Template> templates,List<TriAndTriggerRule> trTriggerRules,List<Rule> rules) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(xmlPath));
		Element rootElement=document.getRootElement();
		List<Element> templateElements=rootElement.elements("template");
		
		for(TriAndTriggerRule trTriggerRule:trTriggerRules) {
			for(RuleStyle negative:trTriggerRule.triggerRule.negative) {
				if(negative.weight==2) {
					String ruleName=trTriggerRule.triggerName.substring(0, trTriggerRule.triggerName.indexOf("trigger"));
					String templateName=null;
					for(Rule rule:rules) {
						if(ruleName.equals(rule.getRuleName())) {
							if(rule.getTriggers().indexOf("rain")>=0) {
								templateName="RainInstance";
								break;
							}
						}
					}
					String ruleTemplateName="Rule"+negative.name.substring(negative.name.indexOf("rule")).substring("rule".length());
					Element ruleTemplElement=null;
					Element envTemplElement=null;
					int flag=0;
					for(Element templateElement:templateElements) {
						Element nameElement=templateElement.element("name");
						String name=nameElement.getTextTrim();
						
						if(name.equals(ruleTemplateName)) {
							if(flag==0) {
								flag=1;
							}else if(flag==1) {
								flag=2;
							}
							ruleTemplElement=templateElement;
							
						}
						if(templateName!=null&&name.indexOf(templateName)>=0){
							if(flag==0) {
								flag=1;
							}else if(flag==1) {
								flag=2;
							}
							envTemplElement=templateElement;
							
						}
						if(flag==2) {
							List<Element> transitionRuleElements=ruleTemplElement.elements("transition");
							for(Element transitionElement:transitionRuleElements) {
								Element sourceIdElement=transitionElement.element("source");
								String sourceId=sourceIdElement.attribute("ref").getValue();
								Element targetIdElement=transitionElement.element("target");
								String targetId=targetIdElement.attribute("ref").getValue();
								if(sourceId.equals("id1")&&targetId.equals("id0")) {
									Element labelElement=DocumentHelper.createElement("label");
									labelElement.addAttribute("kind", "synchronisation");
									labelElement.setText(negative.name+"OK!");									
									List<Element> labelElements=transitionElement.elements("label");
									labelElements.add(0,labelElement);
									break;
								}
							}
							
							List<Element> locationElements=envTemplElement.elements("location");
							List<Element> transitionEnvElements=envTemplElement.elements("transition");
							Element initElement=envTemplElement.element("init");
							Attribute initAttribute=initElement.attribute("ref");
							String initId=initAttribute.getValue();
							for(Element locationElement:locationElements) {
								Attribute locationId=locationElement.attribute("id");
								String id=locationId.getValue();
								if(id.equals(initId)) {
									Element urgentElement=locationElement.element("urgent");
									if(urgentElement!=null) {
										locationElement.remove(urgentElement);
									}
									
									break;
								}
							}
							for(Element transitionElement:transitionEnvElements) {
								Element sourceIdElement=transitionElement.element("source");
								String sourceId=sourceIdElement.attribute("ref").getValue();
								if(sourceId.equals(initId)) {
									Element labelElement=DocumentHelper.createElement("label");
									labelElement.addAttribute("kind", "synchronisation");
									labelElement.setText(negative.name+"OK?");									
									List<Element> labelElements=transitionElement.elements("label");
									labelElements.add(0,labelElement);
									break;
								}
							}
							
							break;
						}
					}
				}
			}
		}
		
		OutputStream os=new FileOutputStream(xmlPath);
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
	
	//模型声明
	public void modelDeclaration(String xmlPath,List<Template> templates) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(xmlPath));
		Element rootElement=document.getRootElement();
		
		
		Element systemElement=rootElement.element("system");
		
		
		//实例化：
		List<String> instances=new ArrayList<String>();
		/////////不同的人的行为模型//////////////
		int flag=0;
		for(Template template:templates) {
			if(template.name.indexOf("PersonDistance")>=0) {
				flag=1;
				break;
			}
		}
		if(flag==1) {
			String pdi0="pdi0=PersonDistanceInstance0(5.0,10.0,40.0,180.0,200.0,4,1,0,-0.5,-0.5);";
			String pdi1="pdi1=PersonDistanceInstance0(5.0,10.0,40.0,180.0,200.0,4,1,1,-0.6,-0.6);";
			String pdi2="pdi2=PersonDistanceInstance1(5.0,10.0,40.0,180.0,200.0,4,1,2,-0.6,-0.6);";
			instances.add(pdi2);
			instances.add(pdi1);
			instances.add(pdi0);
		}else {
			String p0="p0=Person(0);";
			String p1="p1=Person(1);";
			instances.add(p1);
			instances.add(p0);
		}
		//////////////////////////////////////////
		String rain0="rain0=RainInstance0(8,2,65.0);";
		String bb="bb=Bulb(800.0);";
		String ac="ac=AirConditioner(-0.5,0.5);";
		String af="af=AirFreshener(-10.0);";
		String ah="ah=AirHumidifier(0.5);";
		
		instances.add(ah);
		instances.add(af);
		instances.add(ac);
		instances.add(bb);
		instances.add(rain0);
		
		String system="system ";
		for(Template template:templates) {
			if(template.parameter==null) {
				if(template.name.indexOf("sensor")>0) {
					if(template.name.indexOf("person")<0&&template.name.indexOf("distance")<0&&template.name.indexOf("rain")<0) {
						continue;
					}
				}
				system=system+template.name+",";
			}
		}
		String text="";
		for(int i=0;i<instances.size();i++) {
			text=text+"\n"+instances.get(i);
			if(i<instances.size()-1) {
				system=system+instances.get(i).substring(0, instances.get(i).indexOf("="))+",";
			}
			if(i==instances.size()-1) {
				system=system+instances.get(i).substring(0, instances.get(i).indexOf("="))+";";
			}
		}
		text=text+"\n"+system;
		systemElement.setText(text);
		
		
		
		OutputStream os=new FileOutputStream(xmlPath);
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
	
	
	//全局声明
	public void systemDeclaration(String xmlPath,List<Parameter> parameters) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(xmlPath));
		Element rootElement=document.getRootElement();
		
		Element declarationElement=rootElement.element("declaration");
		String text="";
		for(Parameter parameter:parameters) {
			String declaration=null;
			if(parameter.getInitValue()==null) {
				if(parameter.getStyle().equals("chan")) {
					declaration="urgent broadcast chan "+parameter.getName()+";";
				}
				if(parameter.getName().equals("time")){
					declaration=parameter.getStyle()+" "+parameter.getName()+";";
				}
				if(parameter.getName().equals("number")) {
					declaration=parameter.getStyle()+" "+parameter.getName()+"=0;";
				}
				if(parameter.getName().indexOf("distance")>=0) {
					if(parameter.getName().indexOf("[rid]")>0) {
						declaration=parameter.getStyle()+" "+parameter.getName().replace("rid", "5")+"={10.0,10.0,10.0,10.0,10.0};";
					}else {
						declaration=parameter.getStyle()+" "+parameter.getName()+"=10.0;";
					}
					
				}
				
			}else {
				declaration=parameter.getStyle()+" "+parameter.getName()+"="+parameter.getInitValue()+";";
			}
			
			text=text+"\n "+declaration;
		}
		declarationElement.setText(text);
		
		OutputStream os=new FileOutputStream(xmlPath);
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
	
	
	public void setInitValueOfParameters(List<TriAndTriggerRule> maxTriRules,List<Parameter> parameters) {
		for(TriAndTriggerRule maxRule:maxTriRules) {
			SetParameter setParameter=new SetParameter();
			List<ParameterValue> parameterValues=setParameter.parameterSet(maxRule);
			for(ParameterValue parameterValue:parameterValues) {
				if(parameterValue.attribute.indexOf("temperature")>=0) {
					System.out.println("temperature="+parameterValue.value);
					for(Parameter parameter:parameters) {
						if(parameter.getName().equals("temperature")) {
							NumberFormat nf=NumberFormat.getInstance();
							nf.setGroupingUsed(false);
							String value=nf.format(parameterValue.value);
							if(value.indexOf(".")<0) {
								value=value+".0";
							}
							parameter.setInitValue(value);
							break;
						}
					}
				}
				if(parameterValue.attribute.indexOf("brightness")>=0) {
					System.out.println("brightness="+parameterValue.value);
					for(Parameter parameter:parameters) {
						if(parameter.getName().equals("brightness")) {
							NumberFormat nf=NumberFormat.getInstance();
							nf.setGroupingUsed(false);
							String value=nf.format(parameterValue.value);
							if(value.indexOf(".")<0) {
								value=value+".0";
							}
							parameter.setInitValue(value);
							break;
						}
					}
				}
				if(parameterValue.attribute.indexOf("co2ppm")>=0) {
					System.out.println("co2ppm="+parameterValue.value);
					for(Parameter parameter:parameters) {
						if(parameter.getName().equals("co2ppm")) {
							NumberFormat nf=NumberFormat.getInstance();
							nf.setGroupingUsed(false);
							String value=nf.format(parameterValue.value);
							if(value.indexOf(".")<0) {
								value=value+".0";
							}
							parameter.setInitValue(value);
							break;
						}
					}
				}
				if(parameterValue.attribute.indexOf("humidity")>=0) {
					System.out.println("humidity="+parameterValue.value);
					for(Parameter parameter:parameters) {
						if(parameter.getName().equals("humidity")) {
							NumberFormat nf=NumberFormat.getInstance();
							nf.setGroupingUsed(false);
							String value=nf.format(parameterValue.value);
							if(value.indexOf(".")<0) {
								value=value+".0";
							}
							parameter.setInitValue(value);
							break;
						}
					}
				}
				System.out.println(parameterValue.attribute+"="+parameterValue.value);
				System.out.println();
			}
		}
	}
	
	
//	public List<Parameter> getParameters(List<Template> templates){
//		List<Parameter> parameters=new ArrayList<Parameter>();
//		for(Template template:templates) {
//			if(template.name.indexOf("Rule")<0) {
//				if(template.declaration!=null&&template.declaration.indexOf("controlled_device")>=0) {
//					//如果是controlled_device，需要一个对应变量表示该设备状态
//					Parameter parameter=new Parameter();
//					String nameAttr=template.name.substring(0,1).toLowerCase()+template.name.substring(1);
//					parameter.name=nameAttr;
//					if(nameAttr.equals("airConditioner")){
//						parameter.style="int[0,2]";
//					}else {
//						parameter.style="int[0,1]";
//					}
//					parameter.initValue="0";
//					parameters.add(parameter);
//				}
//				if(template.name.indexOf("Rain")>=0) {
//					//如果是Rain，也需要一个rain作为状态变量
//					Parameter parameter=new Parameter();
//					parameter.name="rain";
//					parameter.style="int[0,1]";
//					parameter.initValue="0";
//					parameters.add(parameter);
//				}
//				String[] declarations=null;
//				if(template.declaration!=null&&template.declaration.indexOf(";")>0) {
//					//用来看局部变量
//					declarations=template.declaration.split(";");
//				}				
//				for(Location location:template.locations) {
//					//location节点上的invariant为clock类型变量
//					String[] invariants=null;
//					if(location.invariant!=null) {
//						invariants=location.invariant.split("&&");
//					}
//					if(invariants!=null) {
//						for(String invariant:invariants) {
//							String attribute=getInvaAttribute(invariant);
////							String value=null;
////							//为数组
////							if(attribute.indexOf("[")>0) {
////								String para=attribute.substring(attribute.indexOf("["),attribute.indexOf("]")).substring("[".length());
////								String[] paras=template.parameter.split(",");
////								for(String pa:paras) {
////									pa=pa.trim();
////									if(pa.indexOf(para)>=0) {
////										if(pa.indexOf("[")>0) {
////											String length=pa.substring(pa.indexOf("["), pa.indexOf("]")).substring("[".length());
////											String[] index=length.split(",");
////											int len=Integer.parseInt(index[1])-Integer.parseInt(index[0])+1;
////											length=""+len;
////											attribute.replace(para, length);
////											if(attribute.indexOf("distance")>=0) {
////												value="{";
////												for(int i=0;i<len-1;i++) {
////													value=value+"10.0,";
////												}
////												value=value+"10.0}";
////											}
////										}
////									}
////								}
////							}
//							
//							int flag=0;
//							for(Parameter parameter:parameters) {
//								//看是否已存在参数列表中
//								if(parameter.name.equals(attribute)) {
//									flag=1;
//									if(parameter.style.indexOf("clock")<0){
//										parameter.style="clock";
//									}
//									break;
//									
//								}
//							}
//							if(flag==0) {
//								if(declarations!=null) {
//									for(String declaration:declarations) {
//										//看是否是局部变量
//										declaration=declaration.trim();
//										if(declaration.indexOf(" ")>=0) {
//											String localAttr=declaration.substring(declaration.lastIndexOf(" ")).substring(" ".length());
//											if(localAttr.equals(attribute)) {
//												flag=1;
//												break;
//											}
//										}
//										
//									}
//								}
//							}
//							
//							
//							if(flag==1) {
//								//如果parameters已经有该参数，或者该parameter的定义在该模型的声明中，则不添加到parameters中
//								continue;
//							}else {
//								Parameter parameter=new Parameter();
//								parameter.name=attribute;
//								parameter.style="clock";
////								parameter.initValue=value;
//								parameters.add(parameter);
//							}
//							
//						}
//					}
//					
//				}
//				for(Transition transition:template.transitions) {
//					for(Label label:transition.labels) {
//						String attribute=null;
//						String style=null;
////						String value=null;
//						if(label.kind.equals("synchronisation")) {
//							//chan
//							if(label.content.indexOf("?")>0) {
//								attribute=label.content.substring(0, label.content.indexOf("?")).trim();
//							}
//							if(label.content.indexOf("!")>0) {
//								attribute=label.content.substring(0,label.content.indexOf("!")).trim();
//							}
////							//为数组
////							if(attribute.indexOf("[")>0) {
////								String para=attribute.substring(attribute.indexOf("["),attribute.indexOf("]")).substring("[".length());
////								String[] paras=template.parameter.split(",");
////								for(String pa:paras) {
////									pa=pa.trim();
////									if(pa.indexOf(para)>=0) {
////										if(pa.indexOf("[")>0) {
////											String length=pa.substring(pa.indexOf("["), pa.indexOf("]")).substring("[".length());
////											String[] index=length.split(",");
////											int len=Integer.parseInt(index[1])-Integer.parseInt(index[0])+1;
////											length=""+len;
////											attribute.replace(para, length);
////											if(attribute.indexOf("distance")>=0) {
////												value="{";
////												for(int i=0;i<len-1;i++) {
////													value=value+"10.0,";
////												}
////												value=value+"10.0}";
////											}
////										}
////									}
////								}
////							}
//							style="chan";
//							int flag=0;
//							for(Parameter para:parameters) {
//								if(para.name.equals(attribute)) {
//									flag=1;
//									break;
//								}
//								
//							}
//							if(flag==0) {
//								Parameter parameter=new Parameter();
//								parameter.name=attribute;
//								parameter.style=style;
////								parameter.initValue=value;
//								parameters.add(parameter);
//							}
//							
//						}
//						if(label.kind.equals("assignment")) {
//							String[] assignments=label.content.split(",");
//							for(String assignment:assignments) {
//								assignment=assignment.trim();
//								if(assignment.indexOf("++")>0) {
//									style="int";
//									attribute=assignment.substring(0,assignment.indexOf("++"));
//									
//								}
//								if(assignment.indexOf("--")>0) {
//									style="int";
//									attribute=assignment.substring(0, assignment.indexOf("--"));
//								}
//								if(assignment.indexOf("=")>0) {
//									attribute=assignment.substring(0,assignment.indexOf("="));
//									style=getAssigStyle(assignment, template);
//								}
////								//为数组
////								if(attribute.indexOf("[")>0) {
////									String para=attribute.substring(attribute.indexOf("["),attribute.indexOf("]")).substring("[".length());
////									String[] paras=template.parameter.split(",");
////									for(String pa:paras) {
////										pa=pa.trim();
////										if(pa.indexOf(para)>=0) {
////											if(pa.indexOf("[")>0) {
////												String length=pa.substring(pa.indexOf("["), pa.indexOf("]")).substring("[".length());
////												String[] index=length.split(",");
////												int len=Integer.parseInt(index[1])-Integer.parseInt(index[0])+1;
////												length=""+len;
////												attribute.replace(para, length);
////												if(attribute.indexOf("distance")>=0) {
////													value="{";
////													for(int i=0;i<len-1;i++) {
////														value=value+"10.0,";
////													}
////													value=value+"10.0}";
////												}
////											}
////										}
////									}
////								}
//								int flag=0;
//								for(Parameter parameter:parameters) {
//									if(parameter.name.equals(attribute)) {
//										flag=1;
//										if(style!=null&&(style.indexOf(parameter.style)>=0||style.equals("clock"))){
//											parameter.style=style;
//										}
//										break;
//									}
//								}
//								if(flag==0) {
//									if(declarations!=null) {
//										for(String declaration:declarations) {
//											declaration=declaration.trim();
//											if(declaration.indexOf(" ")>=0) {
//												String localAttr=declaration.substring(declaration.lastIndexOf(" ")).substring(" ".length());
//												if(localAttr.equals(attribute)) {
//													flag=1;
//													break;
//												}
//											}
//											
//											
//										}
//									}
//								}
//								
//								
//								if(flag==1) {
//									//如果parameters已经有该参数，或者该parameter的定义在该模型的声明中，则不添加到parameters中
//									continue;
//								}else {
//									Parameter parameter=new Parameter();
//									parameter.name=attribute;
//									parameter.style=style;
////									parameter.initValue=value;
//									parameters.add(parameter);
//								}
//							}
//							
//						}
//					}
//				}
//			}
//			
//		}
//		
//		
//		return parameters;
//	}
	
	public String getInvaAttribute(String invariant) {
		String attribute=null;
		if(invariant.indexOf("<")>0) {
			attribute=invariant.substring(0, invariant.indexOf("<")).trim();
		}
		if(invariant.indexOf(">")>0) {
			attribute=invariant.substring(0,invariant.indexOf(">")).trim();
		}
		if(invariant.indexOf("'==")>0) {
			attribute=invariant.substring(0, invariant.indexOf("'")).trim();
		}
		return attribute;
	}
	
	public String getAssigStyle(String assignment,Template template) {
		String style=null;
		if(assignment.indexOf("++")>0||assignment.indexOf("--")>0) {
			style="int";
		}
		if(assignment.indexOf("=")>0) {
			String value=assignment.substring(assignment.indexOf("=")).substring("=".length());
			if(value.indexOf(".")>0) {
				style="double";
			}else if(value.indexOf("()")>0) {
				//value为函数
				String[] declarations=template.declaration.split(";");
				for(String declaration:declarations) {
					if(declaration.indexOf("()")>0) {
						declaration=declaration.trim();
						style=declaration.substring(0, declaration.indexOf(" "));
						break;
					}
				}
				
			}else if(isNumeric(value)) {
				style="int";
			}else {
				//value为参数
				String[] parameters=template.parameter.split(",");
				for(String parameter:parameters) {
					parameter=parameter.trim();
					String[] stylePara=parameter.split(" ");
					if(stylePara[1].equals(value)) {
						style=stylePara[0];
						break;
					}
				}
				//如果是局部变量
				if(style==null&&template.declaration!=null) {
					if(template.declaration.indexOf(";")>0) {
						String[] declarations=template.declaration.split(";");
						for(String declaration:declarations) {
							declaration=declaration.trim();
							String[] stylePara=declaration.split(" ");
							int size=stylePara.length;
							if(stylePara[size-1].equals(value)) {
								style=stylePara[size-2];
								break;
							}
						}
					}
				}
			}
			
		}
		return style;
	}
	
	public boolean isNumeric(String str) {
		for(int i=str.length()-1;i>=0;i--) {
			if(!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

}
