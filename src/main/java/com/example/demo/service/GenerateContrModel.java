package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

import com.example.demo.bean.Rule;
import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.TemplGraphNode;
import com.example.demo.bean.TemplTransition;
@Service
public class GenerateContrModel {

	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		GenerateContrModel gcModel=new GenerateContrModel();
		RuleService ruleService=new RuleService();
		String xmlPath1="D:\\try.xml";
		String xmlPath="D:\\newTry.xml";
		GetTemplate parse=new GetTemplate();
		parse.deletLine(xmlPath1, xmlPath, 2);
		gcModel.deleteRuleNode(xmlPath);
 		String rulePath="D:\\rules.txt";
 		List<Rule> rules=ruleService.getRuleListFromTxt(rulePath);
 		
 		
 		
 		//////////生成控制器模型///////////////
 		/////////先获得Rule类型的rules
 		/////////然后生成规则模型
 		///////////////////////////////////////

	}
	
	/////删除控制器模型
	@SuppressWarnings("unchecked")
	public void deleteRuleNode(String xmlPath) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(xmlPath));
		Element rootElement=document.getRootElement();
		List<Element> templateElements=rootElement.elements("template");
		for(Element templateElement:templateElements) {
			Element nameElement=templateElement.element("name");
			if(nameElement.getTextTrim().indexOf("Rule")>=0) {
				rootElement.remove(templateElement);
			}
		}
		OutputStream os=new FileOutputStream(xmlPath);
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		format.setTrimText(false);
        format.setNewlines(false);
		XMLWriter writer=new XMLWriter(os,format);
		writer.write(document);
		writer.close();
		os.close();
	}
	
	/////////////////////////////////////////2020.12.29/////////////////////////////
	//生成控制器模型
	@SuppressWarnings("unchecked")
	public void generateContrModel(String xmlPath,List<Rule> rules,List<TemplGraph> templGraphs) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(xmlPath));
		Element rootElement=document.getRootElement();
		List<Element> templateElements=rootElement.elements("template");
		for(Rule rule:rules) {
			//创建rule模型
			//xml中排版顺序为
			//<template>
			//	<name><name/>
			//	<declaration><declaration/>
			//	<location>
			//		<label><label/>
			//	<location/>
			//	<init/>
			//	<transition>
			//		<source/>
			//		<target/>
			//		<label><label/>
			//	<transition/>
			//<template/>
			Element ruleElement=DocumentHelper.createElement("template");
			templateElements.add(0,ruleElement);
			Element nameElement=ruleElement.addElement("name");
			//模型名为：Rule num
			nameElement.setText("Rule"+rule.getRuleName().substring("rule".length()));
			//模型的声明declaration
			Element declarationElement=ruleElement.addElement("declaration");
			declarationElement.setText("clock t;");
			//初始节点
			Element startElement=ruleElement.addElement("location");
			startElement.addAttribute("id", "id0");
			//初始节点位置
			startElement.addAttribute("x", "-300");
			startElement.addAttribute("y", "0");
			//初始节点具有不变式t<=3
			Element labelElement0=startElement.addElement("label");
			labelElement0.addAttribute("kind", "invariant");
			labelElement0.setText("t<=1");
			//第一个节点为初始节点
			Element initElement=ruleElement.addElement("init");
			initElement.addAttribute("ref", "id0");
			List<Element> locationElements=ruleElement.elements("location");
			//最后一个节点
			Element endElement=DocumentHelper.createElement("location");
			endElement.addAttribute("id", "id1");
			//end节点位置
			endElement.addAttribute("x", ""+(-300+150));
			endElement.addAttribute("y", "-100");
			//end节点具有不变式t<=1
			Element labelElement1=endElement.addElement("label");
			labelElement1.addAttribute("kind", "invariant");
			labelElement1.setText("t<=0");
			locationElements.add(0,endElement);
			//end->start transition
			Element transitionElement0=ruleElement.addElement("transition");
			Element sourceElement0=transitionElement0.addElement("source");
			Element targetElement0=transitionElement0.addElement("target");
			sourceElement0.addAttribute("ref", "id1");
			targetElement0.addAttribute("ref","id0");
			//end->start条件为t>=0
			Element guardElement0=transitionElement0.addElement("label");
			guardElement0.addAttribute("kind", "guard");
			guardElement0.addAttribute("x", ""+(-300+10));
			guardElement0.addAttribute("y", "-120");
			guardElement0.setText("t>=0");
			//同时assignment t=0
			Element assignmentElement0=transitionElement0.addElement("label");
			assignmentElement0.addAttribute("kind", "assignment");
			assignmentElement0.addAttribute("x", ""+(-300+10));
			assignmentElement0.addAttribute("y", "-90");
			assignmentElement0.setText("t=0");
			//nail为了美观
			Element nailElement0=transitionElement0.addElement("nail");
			nailElement0.addAttribute("x", "-300");
			nailElement0.addAttribute("y", "-100");
			List<Element> transitionElements=ruleElement.elements("transition");
			//中间节点
			int count=1; //节点计数
			//判断条件的节点
			for(int i=0;i<rule.getTrigger().size();i++) {
				//条件判断节点
				Element locationElement=DocumentHelper.createElement("location");
				locationElement.addAttribute("id", "id"+(1+count));
				locationElements.add(0,locationElement);
				//location位置
				locationElement.addAttribute("x", ""+(-300+count*150));
				locationElement.addAttribute("y", "0");
				//满足条件的transition
				Element satTransitionElement=DocumentHelper.createElement("transition");
				Element sourceElement=satTransitionElement.addElement("source");
				Element targetElement=satTransitionElement.addElement("target");
				sourceElement.addAttribute("ref", "id"+(1+count));
				targetElement.addAttribute("ref", "id"+(2+count));
				//不满足条件的transition，从该节点指向初始节点
				Element unsatTransitionElement=DocumentHelper.createElement("transition");
				Element unsatSourceElement=unsatTransitionElement.addElement("source");
				Element unsatTargetElement=unsatTransitionElement.addElement("target");
				unsatSourceElement.addAttribute("ref", "id"+(1+count));
				unsatTargetElement.addAttribute("ref", "id0");
				Element committedElement=DocumentHelper.createElement("committed");
				locationElement.add(committedElement);
				Element unsatGuardElement=unsatTransitionElement.addElement("label");
				unsatGuardElement.addAttribute("kind", "guard");
				Element satGuardElement=satTransitionElement.addElement("label");
				satGuardElement.addAttribute("kind", "guard");
				List<String> conAndReverseCon=new ArrayList<String>();
				if(rule.getTrigger().get(i).indexOf("=")<0&&
						rule.getTrigger().get(i).indexOf("<")<0&&
						rule.getTrigger().get(i).indexOf(">")<0) {
					//如果节点为设备相关，则该节点类型为committed
					//不满足条件的guard为条件的反转
					conAndReverseCon=getConAndReverseCon(rule.getTrigger().get(i),templGraphs);
				}else {
					//如果节点为属性相关，则该节点类型为committed
					//不满足条件的guard为条件的反转
					conAndReverseCon=getConAndReverseConAttr(rule.getTrigger().get(i),templGraphs);
				}
				System.out.println(conAndReverseCon.get(1));
				unsatGuardElement.setText(conAndReverseCon.get(1));
				satGuardElement.setText(conAndReverseCon.get(0));
				//label位置
				unsatGuardElement.addAttribute("x", ""+(-300+10));
				unsatGuardElement.addAttribute("y", ""+(count*90+10));
				//不满足条件则将rule.num赋值为0，表示规则不触发
				//同时将t重新赋值为0
				Element unsatAssElement=unsatTransitionElement.addElement("label");
				unsatAssElement.addAttribute("kind", "assignment");
				//assignment label的位置
				unsatAssElement.addAttribute("x", ""+(-300+10));
				unsatAssElement.addAttribute("y", ""+(count*90-20));
				unsatAssElement.setText("t=0,"+rule.getRuleName()+"=0");
				transitionElements.add(0,unsatTransitionElement);
				//增加两个nail
				Element unsatNailElement0=unsatTransitionElement.addElement("nail");
				unsatNailElement0.addAttribute("x", ""+(-300+(i+1)*150));
				unsatNailElement0.addAttribute("y", ""+count*90);
				Element unsatNailElement1=unsatTransitionElement.addElement("nail");
				unsatNailElement1.addAttribute("x", "-300");
				unsatNailElement1.addAttribute("y", ""+count*90);
				if(i==0) {
					//如果为第一个trigger判断节点，
					//则从初始节点到该节点有个transition
					//transition guard为t>=3
					Element firstTransitionElement=DocumentHelper.createElement("transition");
					Element firstSourceElement=firstTransitionElement.addElement("source");
					Element firstTargetElement=firstTransitionElement.addElement("target");
					firstSourceElement.addAttribute("ref", "id0");
					firstTargetElement.addAttribute("ref", "id"+(1+count));
					Element guardElement=firstTransitionElement.addElement("label");
					guardElement.addAttribute("kind", "guard");
					guardElement.setText("t>=1");
					transitionElements.add(0,firstTransitionElement);
				}
				if(i==rule.getTrigger().size()-1) {
					//最后一个trigger满足
					Element assignmentElement=satTransitionElement.addElement("label");
					assignmentElement.addAttribute("kind", "assignment");
					assignmentElement.setText("t=0,"+rule.getRuleName()+"=1");
				}				
				transitionElements.add(0,satTransitionElement);				
				count++;
			}
			
			//action的节点  
			for(int i=0;i<rule.getAction().size();i++) {
				//action节点
				Element locationElement=DocumentHelper.createElement("location");
				locationElement.addAttribute("id", "id"+(1+count));
				locationElements.add(0,locationElement);
				//位置
				locationElement.addAttribute("x", ""+(-300+count*150));
				locationElement.addAttribute("y", "0");
				//节点类型均为committed
				Element committedElement=DocumentHelper.createElement("committed");
				locationElement.add(committedElement);
				//该节点到下一节点的transition
				Element transitionElement=DocumentHelper.createElement("transition");
				Element sourceElement=transitionElement.addElement("source");
				Element targetElement=transitionElement.addElement("target");
				String[] actionTime=rule.getAction().get(i).split("for");
				actionTime[0]=actionTime[0].trim();
				if(rule.getAction().get(i).indexOf("for")>0) {
					actionTime[1]=actionTime[1].trim();
				}
				
				
				if(i<rule.getAction().size()-1) {
					//action 可能包含for表示经过多长时间后进行下一个
					//without for
					sourceElement.addAttribute("ref", "id"+(1+count));
					targetElement.addAttribute("ref", "id"+(2+count));
					Element actionSynElement=transitionElement.addElement("label");
					actionSynElement.addAttribute("kind", "synchronisation");
					
					actionSynElement.setText(actionTime[0]+"!");
					
					if(rule.getAction().get(i).indexOf("for")>0) {
						//for time 节点
						Element nextLocationElement=DocumentHelper.createElement("location");
						nextLocationElement.addAttribute("id", "id"+(2+count));
						locationElements.add(0,nextLocationElement);
						//位置
						nextLocationElement.addAttribute("x", ""+(-300+(count+1)*150));
						nextLocationElement.addAttribute("y", "0");
						//该节点到下一节点的transition
						Element nextTransitionElement=DocumentHelper.createElement("transition");
						Element nextSourceElement=nextTransitionElement.addElement("source");
						Element nextTargetElement=nextTransitionElement.addElement("target");
						nextSourceElement.addAttribute("ref", "id"+(2+count));
						nextTargetElement.addAttribute("ref", "id"+(3+count));
						String time=actionTime[1].substring(0, actionTime[1].indexOf("s"));
						Element invariantElement=nextLocationElement.addElement("label");
						invariantElement.addAttribute("kind", "invariant");
						invariantElement.setText("t<="+time);
						Element assignmentElement =nextTransitionElement.addElement("label");
						assignmentElement.addAttribute("kind", "assignment");
						assignmentElement.setText("t=0");
						Element guardElement=nextTransitionElement.addElement("label");
						guardElement.addAttribute("kind", "guard");
						guardElement.setText("t>="+time);
						transitionElements.add(0,nextTransitionElement);
						count++;
					}
				}
				if(i==rule.getAction().size()-1) {
					//最后一个action节点与end连接
					sourceElement.addAttribute("ref", "id"+(1+count));
					targetElement.addAttribute("ref", "id1");
					Element actionSynElement=transitionElement.addElement("label");
					actionSynElement.addAttribute("kind", "synchronisation");
					actionSynElement.addAttribute("x", ""+(-150+50));
					actionSynElement.addAttribute("y", "-90");
					actionSynElement.setText(actionTime[0]+"!");
					Element finalNailElement=transitionElement.addElement("nail");
					finalNailElement.addAttribute("x", ""+(-300+count*150));
					finalNailElement.addAttribute("y", "-100");
				}
				
				
				transitionElements.add(0,transitionElement);
				count++;
			}
			
			
		}
			
		
		OutputStream os=new FileOutputStream(xmlPath);
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		format.setTrimText(false);
		format.setNewlines(true);
		XMLWriter writer=new XMLWriter(os,format);
		writer.write(document);
		writer.close();
		os.close();
	}
	
	
	/////////////////////////////////////2020.12.29///////////////////////////////
	//获得属性条件和反转条件
	//////String[0]-----condition String[1]-----reverseCondition
	public List<String> getConAndReverseConAttr(String trigger,List<TemplGraph> templGraphs){
		List<String> conAndReverseCon=new ArrayList<String>();
		String reverseCondition=null;
		String condition=trigger;
		if(trigger.indexOf("FOR")>=0) {
			condition=trigger.substring(0, trigger.indexOf("FOR")).trim();
		}
		if(trigger.indexOf(".")>0) {
			condition=condition.substring(condition.indexOf(".")).substring(".".length());
		}
		if(condition.indexOf(">=")>0) {
			reverseCondition=condition.replace(">=", "<");
		}else if(condition.indexOf(">")>0) {
			reverseCondition=condition.replace(">", "<=");
		}else if(condition.indexOf("<=")>0) {
			reverseCondition=condition.replace("<=", ">");
		}else if(condition.indexOf("<")>0) {
			reverseCondition=condition.replace("<", ">=");
		}else if(condition.indexOf("=")>0) {
			reverseCondition=condition.replace("=", "!=");
			condition=condition.replace("=", "==");
		}
		//距离感应
		if(reverseCondition.indexOf("distanceFrom")>=0) {
			for(TemplGraph templGraph:templGraphs) {
				if(templGraph.declaration!=null && templGraph.declaration.indexOf("sensor")>=0 && templGraph.declaration.indexOf("return distanceFrom")>0) {
					for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
						for(TemplTransition inTransition:templGraphNode.inTransitions) {
							if(inTransition.assignment!=null) {
								String[] assignments=inTransition.assignment.split(",");
								for(String assignment:assignments) {
									if(assignment.indexOf("get()")>0) {
										assignment=assignment.trim();
										String attribute=assignment.substring(0, assignment.indexOf("=")).trim();
										if(reverseCondition.indexOf(">")>0) {
											String originAttribute=reverseCondition.substring(0, reverseCondition.indexOf(">")).trim();
											
											reverseCondition=reverseCondition.replace(originAttribute, attribute);
										}
										if(reverseCondition.indexOf("<")>0) {
											String originAttribute=reverseCondition.substring(0, reverseCondition.indexOf("<")).trim();
											
											reverseCondition=reverseCondition.replace(originAttribute, attribute);
										}
									}
								}
							}
						}
					}
				}
			}
			
		}
		if(condition.indexOf("distanceFrom")>=0) {
			for(TemplGraph templGraph:templGraphs) {
				if(templGraph.declaration!=null && templGraph.declaration.indexOf("sensor")>=0 && templGraph.declaration.indexOf("return distanceFrom")>0) {
					for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
						for(TemplTransition inTransition:templGraphNode.inTransitions) {
							if(inTransition.assignment!=null) {
								String[] assignments=inTransition.assignment.split(",");
								for(String assignment:assignments) {
									if(assignment.indexOf("get()")>0) {
										assignment=assignment.trim();
										String attribute=assignment.substring(0, assignment.indexOf("=")).trim();
										if(condition.indexOf(">")>0) {
											String originAttribute=condition.substring(0, condition.indexOf(">")).trim();
											
											condition=condition.replace(originAttribute, attribute);
										}
										if(condition.indexOf("<")>0) {
											String originAttribute=condition.substring(0, condition.indexOf("<")).trim();
											
											condition=condition.replace(originAttribute, attribute);
										}
									}
								}
							}
						}
					}
				}
			}
			
		}
		System.out.println(condition);
		System.out.println(reverseCondition);
		
		conAndReverseCon.add(condition);
		conAndReverseCon.add(reverseCondition);
		return conAndReverseCon;
		
	}
	//获得设备状态条件和反转条件
	//////String[0]-----condition String[1]-----reverseCondition
	public List<String> getConAndReverseCon(String trigger,List<TemplGraph> templGraphs){
		List<String> conAndReverseCon=new ArrayList<String>();
		String reverseCondition=null;
		String condition=null;
		String device=null;
		String state=null;
		if(trigger.indexOf(".")>0) {
			state=trigger.substring(trigger.indexOf(".")).substring(".".length());
			device=trigger.substring(0, trigger.indexOf("."));
		}
		//找到该设备状态对应的值/condition
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.name.equals(device)) {
				for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
					if(templGraphNode.name.equals(state)) {
						if(templGraphNode.inTransitions!=null) {
							if(templGraphNode.inTransitions.get(0).assignment!=null) {
								String[] assignments=templGraphNode.inTransitions.get(0).assignment.split(",");
								//设备参数，首字母小写
								String deviceParameter=device.substring(0, 1).toLowerCase()+device.substring(1);
								for(String assignment:assignments) {
									if(assignment.indexOf(deviceParameter)>=0) {
										condition=assignment;
										break;
									}
								}
							}
						}						
						break;
					}
				}				
				break;
			}
		}
		if(condition!=null && condition.indexOf("=")>0) {
			reverseCondition=condition.replace("=", "!=");
			condition=condition.replace("=", "==");
		}
		conAndReverseCon.add(condition);
		conAndReverseCon.add(reverseCondition);
		return conAndReverseCon;
	}
	

}
