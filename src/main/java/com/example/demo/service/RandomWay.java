package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.TemplGraphNode;
import com.example.demo.bean.TemplTransition;
import com.example.demo.bean.Trigger;

//import com.simulate.GenerateSysDeclaration.Parameter;
//import com.simulate.GetRule.Rule;
//import com.simulate.GetTemplate.Template;
//import com.simulate.TGraphToDot.Action;
//import com.simulate.TGraphToDot.Trigger;
//import com.simulate.TemplateGraph.TemplGraph;
//import com.simulate.TemplateGraph.TemplGraphNode;
//import com.simulate.TemplateGraph.TemplTransition;

public class RandomWay {

	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		
		//get all triggers
//		GetTemplate parse=new GetTemplate();
//		GetRule rt=new GetRule();
//		GenerateEnvModel geModel=new GenerateEnvModel();
//		GenerateContrModel gcModel=new GenerateContrModel();
//		TemplateGraph tGraph=new TemplateGraph();
//		TGraphToDot tDot=new TGraphToDot();
//		
//		GenerateSysDeclaration gSysDeclar=new GenerateSysDeclaration();
//		RandomWay rWay=new RandomWay();
//		Random rd=new Random();
//		SetParameter setParameter=new SetParameter();	
//		String xmlPath1="D:\\exp0108.xml";
//		String xmlPath="D:\\exp01081.xml";
// 		String rulePath="D:\\rules0105.txt";
// 		
//		//转成可解析xml文件
//		parse.deletLine(xmlPath1, xmlPath, 2);
//		//将xml文件中的template解析
//		List<Template> templates=parse.getTemplate(xmlPath);
//		//获得templGraph
//		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
//		for(Template template:templates) {
//			templGraphs.add(tGraph.getTemplGraph(template));
//		}
//		
// 		/////////////////////////////生成控制器模型///////////////
// 		/////////先获得Rule类型的rules
// 		/////////然后生成规则模型
// 		//////////////////////////////////////////////////////////
//		List<Rule> rules=rt.getRules(rulePath);
//		gcModel.deleteRuleNode(xmlPath);
//		gcModel.generateContrModel(xmlPath, rules, templGraphs);
//		
//
//		/////////////////////生成环境模型//////////////////////////////
//		//将模型转成Template类型，再确定模型determine方法（有branchpoint则是不确定的）
//		//获得biddable类型的多个确定模型后，确定最终的确定模型finalDetermine
//		//最终的确定模型，写入xml文件中
//		//删除最初的不确定模型
//		///////////////////////////////////////////////////////////////
//		
//		List<TemplGraph> determineTemplGraphs=new ArrayList<TemplGraph>();
//		for(TemplGraph templGraph:templGraphs) {
//			if(templGraph.declaration.indexOf("biddable")>=0) {
//				List<List<TemplGraph>> allbpGraphs=new ArrayList<List<TemplGraph>>();
//				for(TemplGraphNode node:templGraph.templGraphNodes) {
//					if(node.style.equals("branchpoint")) {
//						allbpGraphs.add(geModel.bpTemplGraphs(node, templGraph));
//					}
//				}
//				List<TemplGraph> determinGraphs=geModel.stitchGraphs(allbpGraphs,templGraph);
//				if(determinGraphs.size()==0) {
//					//如果是确定的模型，不变化
//				}else if(determinGraphs.size()==1) {
//					//一个
//					determineTemplGraphs.add(determinGraphs.get(0));
//				}else {
//					//多个确定模型
//					if(templGraph.parameter==null) {
//						geModel.finalDetermine(determinGraphs);
//						for(TemplGraph deterGraph:determinGraphs) {
//							determineTemplGraphs.add(deterGraph);
//						}
//					}else {
//						geModel.finalDetermine(determinGraphs);
//						for(TemplGraph deterGraph:determinGraphs) {
//							determineTemplGraphs.add(deterGraph);
//						}
//					}
//				}
//			}
//		}
//		
//		//确定化自治模型
//		for(int i=0;i<determineTemplGraphs.size();i++) {
//			geModel.generateBiddableModel(determineTemplGraphs.get(i), xmlPath, xmlPath, i);
//		}
//				
//		//删除原本的不确定模型
//		for(int i=0;i<determineTemplGraphs.size();i++) {
//			geModel.deleteModel(xmlPath, determineTemplGraphs.get(i).name);
//		}
//		
//		templates.clear();
//		templGraphs.clear();
//		templates=parse.getTemplate(xmlPath);
//		for(Template template:templates) {
//			templGraphs.add(tGraph.getTemplGraph(template));
//		}
//
//		///////////////////////setParameters/////////////////////////////////////////////////
//		//获得causal attributes
//		//分别获得与causal attributes相关的triggers
//		//获得causal attributes取值的分界点
//		//随机给causal attributes赋值
//		/////////////////////////////////////////////////////////////////////////////////////
//		
////		List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
//		List<String> attributes=new ArrayList<String>();
////		
////		for(TemplGraph templGraph:templGraphs) {
////			if(templGraph.declaration!=null) {
////				if(templGraph.declaration.indexOf("controlled_device")>=0) {
////					controlledDevices.add(templGraph);
////				}
////			}
////		}
////		List<Action> actions=tDot.getActions(rules,controlledDevices);
//		List<Trigger> triggers=tDot.getTriggers(rules);
////		
////		
////		attributes=setParameter.getAttributesEffectedByDevice(actions);
////		for(String attribute:attributes) {
////			System.out.println(attribute);
////			List<Trigger> triggersWithSameAttribtue=rWay.getTriggersWithSameAttribute(attribute, triggers);
////			List<Double> piecewise=rWay.getPiecewise(triggersWithSameAttribtue);
////		}
//		attributes=setParameter.getCausalAttributes(templGraphs);
//		List<AttrPiecewise> attrPiecewises=rWay.getAttrPiecewises(attributes, triggers);
//		int allPieceNum=rWay.getAttrPieceNum(attrPiecewises);
//		System.out.println();
//		System.out.println(allPieceNum);
//		int num=rd.nextInt(allPieceNum-1);
//		System.out.println(num);
//		System.out.println();
////		List<AttrValue> attrValues=rWay.getAttrValues(attrPiecewises, num);
//		
//		///////////////////////////////////////setParameter(for instances)///////////////////
//		//获得带时间参数的biddable类型的各时间点
//		//根据总的仿真时间给每个时间点随机取值
//		//获得实例化字符串(用于模型声明)
//		/////////////////////////////////////////////////////////////////////////////////////
//		List<BiddableTimeValue> biddablesTimeValue=rWay.getBiddabelsTimeValue(templGraphs);
//		
////		for(BiddableTimeValue biddableTimeValue:biddablesTimeValue) {
////			System.out.println(biddableTimeValue.name);
////			rWay.setTimeValues(biddableTimeValue.timeValues, 300);
////		}
//		//设置仿真时间 allTime
//		int allTime=300;
//		rWay.setAllBiddablesTimeValue(biddablesTimeValue,allTime);
//		
//		for(BiddableTimeValue biddableTimeValue:biddablesTimeValue) {
//			System.out.println(biddableTimeValue.name);
//			for(TimeValue timeValue:biddableTimeValue.timeValues) {
//				System.out.println(timeValue.timeName);
//				System.out.println(timeValue.timeValue);
//			}
//		}
//		
//		List<String> instances=rWay.getInstancesString(biddablesTimeValue);
//		//从AttrValue格式转为String[] 的AttributeValue
//		List<String[]> allAttributeValue=rWay.getAttrValues(attrPiecewises, num);
////		for(AttrValue attrValue:attrValues) {
////			String[] attributeValue=new String[2];
////			attributeValue[0]=attrValue.name;
////			attributeValue[1]=attrValue.value.toString();
////			allAttributeValue.add(attributeValue);
////		}
//		
//		List<Parameter> parameters=gSysDeclar.getParameters(templGraphs, allAttributeValue);
//		//打印到控制板用
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
//		
//		//打印到控制板用
//		for(Parameter parameter:parameters) {
//			System.out.println(parameter.style+": ");
//			System.out.println("  "+parameter.name);
//			System.out.println("  "+parameter.initValue);
//		}
//		
//		gSysDeclar.globalDeclaration(xmlPath,xmlPath, parameters);
//		gSysDeclar.modelDeclaration(xmlPath,xmlPath, templGraphs, instances);
//		gSysDeclar.setQuery(xmlPath, xmlPath,parameters,String.valueOf(allTime));

	}
	
	class AttrPiecewise{
		String name=null; //attribute name
		List<Double> piecewise=new ArrayList<Double>();
	}
	
	class AttrValue{
		String name=null;
		Double value=null;
	}
	
	public List<AttrPiecewise> getAttrPiecewises(List<String> attributes,List<Trigger> triggers){
		List<AttrPiecewise> attrPiecewises=new ArrayList<AttrPiecewise>();
		//根据trigger给attribute取值分段
		for(String attribute:attributes) {
			System.out.println(attribute);
			AttrPiecewise attrPiecewise=new AttrPiecewise();
			attrPiecewise.name=attribute;
			List<Trigger> triggersWithSameAttribtue=getTriggersWithSameAttribute(attribute, triggers);
			attrPiecewise.piecewise=getPiecewise(triggersWithSameAttribtue);
			attrPiecewises.add(attrPiecewise);
		}
		
		return attrPiecewises;
	}
	
	//获得总的可能取值范围，共多少种取值可能性
	public int getAttrPieceNum(List<AttrPiecewise> attrPiecewises) {
		int attrPieceNum=1;
		for(int i=0;i<attrPiecewises.size();i++) {
			attrPieceNum=attrPieceNum*(attrPiecewises.get(i).piecewise.size()+1);
		}
		return attrPieceNum;
	}
	
//	//根据num给不同attribute赋值
//	public List<AttrValue> getAttrValues(List<AttrPiecewise> attrPiecewises,int num){
//		Random rd=new Random();
//		//num取值在0-attrPieceNum-1
//		List<AttrValue> attrValues=new ArrayList<AttrValue>();
//		for(int i=attrPiecewises.size()-1;i>=0;i--) {
//			AttrValue attrValue=new AttrValue();
//			//k选定piecewise第k段
//			int k=num % (attrPiecewises.get(i).piecewise.size()+1);
//			num=num/(attrPiecewises.get(i).piecewise.size()+1);
//			attrValue.name=attrPiecewises.get(i).name;
//			if(k==0) {
//				attrValue.value=attrPiecewises.get(i).piecewise.get(k)-1;
//			}else if(k==attrPiecewises.get(i).piecewise.size()+1-1){
//				attrValue.value=attrPiecewises.get(i).piecewise.get(k-1)+1;
//			}else {
//				//中间取随机值
//				attrValue.value=rd.nextDouble()*(attrPiecewises.get(i).piecewise.get(k)-attrPiecewises.get(i).piecewise.get(k-1))+attrPiecewises.get(i).piecewise.get(k-1);
//			}
//			attrValue.value=Double.parseDouble(String.format("%.1f", attrValue.value));
//			System.out.println(attrValue.name);
//			System.out.println(attrValue.value);
//			attrValues.add(attrValue);
//		}
//		
//		return attrValues;
//	}
	
	//根据num给不同attribute赋值
	public List<String[]> getAttrValues(List<AttrPiecewise> attrPiecewises,int num){
		Random rd=new Random();
		//num取值在0-attrPieceNum-1
		List<String[]> attrValues=new ArrayList<String[]>();
		for(int i=attrPiecewises.size()-1;i>=0;i--) {
			String[] attrValue=new String[2];
			//k选定piecewise第k段
			int k=num % (attrPiecewises.get(i).piecewise.size()+1);
			num=num/(attrPiecewises.get(i).piecewise.size()+1);
			attrValue[0]=attrPiecewises.get(i).name;
			if(k==0) {
				attrValue[1]=String.format("%.1f", attrPiecewises.get(i).piecewise.get(k)-1);
			}else if(k==attrPiecewises.get(i).piecewise.size()+1-1){
				attrValue[1]=String.format("%.1f", attrPiecewises.get(i).piecewise.get(k-1)+1);
			}else {
				//中间取随机值——>改成取中间值
				attrValue[1]=String.format("%.1f", (attrPiecewises.get(i).piecewise.get(k)-attrPiecewises.get(i).piecewise.get(k-1))/2+attrPiecewises.get(i).piecewise.get(k-1));
			}
			
			System.out.println(attrValue[0]);
			System.out.println(attrValue[1]);
			attrValues.add(attrValue);
		}
		
		return attrValues;
	}
	
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
	
	//获得涉及相同属性的trigger
	public List<Trigger> getTriggersWithSameAttribute(String attribute,List<Trigger> triggers){
		List<Trigger> triggersWithSameAttribute=new ArrayList<Trigger>();
		for(Trigger trigger:triggers) {
			if(trigger.attrVal[0].equals(attribute)) {
				triggersWithSameAttribute.add(trigger);
			}
		}
		return triggersWithSameAttribute;
	}
	//根据属性相同的trigger获得取值分段,每段分界点
	public List<Double> getPiecewise(List<Trigger> triggers){
		List<Double> piecewise=new ArrayList<Double>();
		for(Trigger trigger:triggers) {
			Double value=Double.parseDouble(trigger.attrVal[2]);
			boolean exist=false;
			for(Double pieceValue : piecewise) {
				if(Math.abs(pieceValue-value)<0.05) {
					exist=true;
					break;
				}
			}
			if(!exist) {
				piecewise.add(value);
			}
		}
		//sort
		Collections.sort(piecewise);
		for(Double pieceValue:piecewise) {
			System.out.println(pieceValue);
		}
		return piecewise;
	}
	
	class BiddableTimeValue{
		String name=null;
		boolean setFlag=false;
		List<TimeValue> timeValues=new ArrayList<TimeValue>();
	}
	
	class TimeValue{
		String timeName=null;
		Integer timeValue=null;
	}
	
	//biddable类型需要设置为随机时间的变量
	public List<TimeValue> getTimeValues(TemplGraph templGraph){
		List<TimeValue> timeValues=new ArrayList<TimeValue>();
		if(templGraph.declaration!=null &&templGraph.declaration.indexOf("biddable")>=0 && templGraph.declaration.indexOf("sensor")<0) {
			TemplGraphNode initNode=new TemplGraphNode();
			for(TemplGraphNode node:templGraph.templGraphNodes) {
				if(node.id.equals(templGraph.init)) {
					initNode=node;
					break;
				}
			}
			Stack<TemplGraphNode> stack=new Stack<TemplGraphNode>();
			stack.push(initNode);
			while(!stack.isEmpty()) {
				TemplGraphNode node=stack.pop();
				node.flag=true;
				if(node.outTransitions.size()>0) {
					TemplTransition outTransition=node.outTransitions.get(0);
					if(!outTransition.node.flag) {
						stack.push(outTransition.node);
					}				
					if(outTransition.guard!=null) {
						String[] guards=outTransition.guard.split("&&");
						for(String guard:guards) {
							guard=guard.trim();
							if(guard.indexOf("time")>=0) {
								TimeValue timeValue=new TimeValue();
								timeValue.timeName=guard.substring(guard.indexOf("=")).substring("=".length());
								timeValues.add(timeValue);
								break;
							}
						}
					}
				}
				
			}
		}
		return timeValues;
	}
	//设置biddable类型的行为时间
	public void setTimeValues(List<TimeValue> timeValues,int allTime) {
		Random rd=new Random();
		int interval=allTime/timeValues.size();
		for(int i=0;i<timeValues.size();i++) {
			//i*interval-(i+1)*interval
			if(timeValues.size()==1) {
				timeValues.get(i).timeValue=allTime/2;
			}else {
				if(i==0) {
					//0-interval
					timeValues.get(i).timeValue=rd.nextInt(interval);
				}else if(i<timeValues.size()-1) {
					int tempInterval=(interval*(i+1)-timeValues.get(i-1).timeValue)/2;
					timeValues.get(i).timeValue=rd.nextInt(tempInterval)+tempInterval+timeValues.get(i-1).timeValue;
				}else{
					int tempInterval=(allTime-timeValues.get(i-1).timeValue)/2;
					timeValues.get(i).timeValue=rd.nextInt(tempInterval)+tempInterval+timeValues.get(i-1).timeValue;
				}
			}
			
			System.out.println(timeValues.get(i).timeName);
			System.out.println(timeValues.get(i).timeValue);
		}
	}
	//获得需要设置时间点的biddable的各时间分界点
	public List<BiddableTimeValue> getBiddabelsTimeValue(List<TemplGraph> templGraphs){
		List<BiddableTimeValue> biddablesTimeValue=new ArrayList<BiddableTimeValue>();
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.parameter!=null) {
				if(templGraph.declaration!=null && templGraph.declaration.indexOf("biddable")>=0 && 
						templGraph.declaration.indexOf("sensor")<=0) {
					BiddableTimeValue biddableTimeValue=new BiddableTimeValue();
					biddableTimeValue.name=templGraph.name;
					biddableTimeValue.timeValues=getTimeValues(templGraph);
					biddablesTimeValue.add(biddableTimeValue);
				}
			}
		}
		return biddablesTimeValue;
	}
	
	//随机设置biddable时间分界点的值
	public void setAllBiddablesTimeValue(List<BiddableTimeValue> biddablesTimeValue,int allTime) {
		for(BiddableTimeValue biddableTimeValue:biddablesTimeValue) {
			if(!biddableTimeValue.setFlag) {
				biddableTimeValue.setFlag=true;
				System.out.println(biddableTimeValue.name);
				setTimeValues(biddableTimeValue.timeValues, allTime);
				String biddableName=biddableTimeValue.name;
				if(biddableTimeValue.name.indexOf("Instance")>0) {
					biddableName=biddableTimeValue.name.substring(0, biddableTimeValue.name.indexOf("Instance"));
				}
				
				for(BiddableTimeValue otherBiddableTimeValue:biddablesTimeValue) {
					if(otherBiddableTimeValue.name.indexOf(biddableName)>=0) {
						for(TimeValue otherTimeValue:otherBiddableTimeValue.timeValues) {
							for(TimeValue timeValue:biddableTimeValue.timeValues) {
								if(otherTimeValue.timeName.equals(timeValue.timeName)) {
									otherTimeValue.timeValue=timeValue.timeValue;
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	public List<String> getInstancesString(List<BiddableTimeValue> biddablesTimeValue){
		List<String> instances=new ArrayList<String>();
		for(int i=0;i<biddablesTimeValue.size();i++) {
			String instance="inst"+i+"="+biddablesTimeValue.get(i).name+"(";
			for(int j=0;j<biddablesTimeValue.get(i).timeValues.size();j++) {
				TimeValue timeValue=biddablesTimeValue.get(i).timeValues.get(j);
				double value=timeValue.timeValue;
				if(j<biddablesTimeValue.get(i).timeValues.size()-1) {
					instance=instance+value+",";
					
				}else if(j==biddablesTimeValue.get(i).timeValues.size()-1) {
					instance=instance+value;
				}
				
			}
			if(biddablesTimeValue.get(i).name.indexOf("Person")>=0) {
				instance=instance+","+i;
			}
			
			instance=instance+");";
			instances.add(instance);
		}
		for(String instance:instances) {
			System.out.println(instance);
		}
		return instances;
	}
	
	public void modelDeclaration(String xmlPath,List<TemplGraph> templGraphs,List<String> instances) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(xmlPath));
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
		OutputStream os=new FileOutputStream(xmlPath);
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		format.setTrimText(false); //保留换行，但是出现空行
		format.setNewlines(false);
		XMLWriter writer=new XMLWriter(os,format);
		writer.write(document);
		writer.close();
		os.close();
	}

}
