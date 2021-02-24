package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;

import com.example.demo.bean.GraphNode;
import com.example.demo.bean.Parameter;
import com.example.demo.bean.Rule;
import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.TemplGraphNode;
import com.example.demo.service.AnalyseIFD.RuleAndTriggerRules;
import com.example.demo.service.AnalyseIFD.TriggerStopRules;
import com.example.demo.service.GetTemplate.Template;
import com.example.demo.service.RandomWay.BiddableTimeValue;
import com.example.demo.service.RandomWay.TimeValue;
import com.example.demo.service.SetParameter.RulesSameAttribute;










public class IFDSceneService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	

	
	/////////////////////生成环境模型//////////////////////////////
	//将模型转成Template类型，再确定模型determine方法（有branchpoint则是不确定的）
	//获得biddable类型的多个确定模型后，确定最终的确定模型finalDetermine
	//最终的确定模型，写入xml文件中
	//删除最初的不确定模型
	///////////////////////////////////////////////////////////////
	public void getDetermineEnvModel(List<TemplGraph> templGraphs,String changedModelFilePathName) throws DocumentException, IOException {
		GenerateEnvModel geModel=new GenerateEnvModel();
		List<TemplGraph> determineTemplGraphs=new ArrayList<TemplGraph>();
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration.indexOf("biddable")>=0) {
				List<List<TemplGraph>> allbpGraphs=new ArrayList<List<TemplGraph>>();
				for(TemplGraphNode node:templGraph.templGraphNodes) {
					if(node.style.equals("branchpoint")) {
						allbpGraphs.add(geModel.bpTemplGraphs(node, templGraph));
					}
				}
				List<TemplGraph> determinGraphs=geModel.stitchGraphs(allbpGraphs,templGraph);
				if(determinGraphs.size()==0) {
					//如果是确定的模型，不变化
				}else if(determinGraphs.size()==1) {
					//一个
					determineTemplGraphs.add(determinGraphs.get(0));
				}else {
					//多个确定模型
					if(templGraph.parameter==null) {
						geModel.finalDetermine(determinGraphs);
						for(TemplGraph deterGraph:determinGraphs) {
							determineTemplGraphs.add(deterGraph);
						}
					}else {
						geModel.finalDetermine(determinGraphs);
						for(TemplGraph deterGraph:determinGraphs) {
							determineTemplGraphs.add(deterGraph);
						}
					}
				}
			}
		}
		
		//确定化自治模型
		for(int i=0;i<determineTemplGraphs.size();i++) {
			geModel.generateBiddableModel(determineTemplGraphs.get(i), changedModelFilePathName, changedModelFilePathName, i);
		}
				
		//删除原本的不确定模型
		for(int i=0;i<determineTemplGraphs.size();i++) {
			geModel.deleteModel(changedModelFilePathName, determineTemplGraphs.get(i).name);
		}
	}
	
	
	public void getIFDGraphviz(List<Template> templates,List<TemplGraph> templGraphs,String changedModelFilePathName,List<Rule> rules,String dotPath) throws DocumentException, IOException {
		GetTemplate gTemplate=new GetTemplate();
		TGraphToDot tDot=new TGraphToDot();
		TemplGraphService tGraph=new TemplGraphService();
		templates.clear();
		templGraphs.clear();
		templates=gTemplate.getTemplate(changedModelFilePathName);
		for(Template template:templates) {
			templGraphs.add(tGraph.getTemplGraph(template));
		}
		tDot.getIFD(templGraphs, rules, dotPath);
	}
	
	public ParameterInstances ifdToSceneModel(String changedModelFilePathName,String dotPath,String finalModelPath,List<TemplGraph> templGraphs,List<Rule> rules,
			List<TemplGraph> controlledDevices,List<TemplGraph> biddables,int allTime) throws DocumentException, IOException {
		ToNode toNode=new ToNode();
		AnalyseIFD anaIFD=new AnalyseIFD();
		SetParameter setParameter=new SetParameter();
		ModifyContrAndEnvModel modifyModel=new ModifyContrAndEnvModel();
		GenerateSysDeclaration gSysDeclar=new GenerateSysDeclaration();
		
		ParameterInstances pamaInst=new ParameterInstances();

		List<Parameter> parameters=new ArrayList<Parameter>();
		List<String> instances=new ArrayList<String>();
		/////////////////////get IFD node//////////////////////////
		//从IFD中获得GraphNode类型的node
		///////////////////////////////////////////////////////////
		
		List<GraphNode> graphNodes=new ArrayList<GraphNode>();
		graphNodes=toNode.getNodes(dotPath);
		
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
		
		///////////////////////setParameters/////////////////////////////////////////////////
		//同Analyse IFD获得ruleAndTriggerRules以及triggerStopRules
		//获得所有actions  getAction方法获得
		//利用actions获得causal属性，getAttributesEffectedByDevice方法
		//进而获得涉及这些属性的rules-ruleSameAttributes，使用getRulesWithSameAttribute方法
		//选择相同属性rule中触发规则数最多的rule-chooseRules，使用getChooseRule方法
		//获得chooseRule后，可以对属性赋值  使用getAllAttributeValue方法，这里把chooseRule合并进去了
		/////////////////////////////////////////////////////////////////////////////////////
		
//		List<Action> actions=tDot.getActions(rules,controlledDevices);
//		List<Trigger> triggers=tDot.getTriggers(rules);
		//获得casual attributes
		List<String> attributes=setParameter.getCausalAttributes(templGraphs);
		List<RulesSameAttribute> rulesSameAttributes=setParameter.getRulesWithSameAttribute(rulesAndTriggerRules, attributes, rules);
		List<String[]> allAttributeValue=setParameter.getAllAttributeValue(rulesSameAttributes,triggersStopRules);
		List<RuleAndTriggerRules> chooseRules=new ArrayList<RuleAndTriggerRules>();
		for(RulesSameAttribute ruleSameAttribute:rulesSameAttributes) {
			RuleAndTriggerRules chooseRule=setParameter.getChooseRule(ruleSameAttribute,triggersStopRules);
			chooseRules.add(chooseRule);			
		}

		////////////////////////////////modify model//////////////////////////////////////
		//主要是修改biddable类型的model，一般除了person相关的model
		//这是在获得triggerStopRules获得之后进行修改的，biddable模型的进行前提是某些rule发生后
		//其实这里可以删除rulesAndTriggerRules的吧
		//使用modifyContrEnvModel方法，
		//////首先保证“某些rule”确实能被触发，只要看是否在chooseRules以及chooseRules触发的rules中即可
		//////修改模型时暂时只考虑了stop one rule的情况
		//////////////////////////////////////////////////////////////////////////////////
		
		
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration!=null) {
				if(templGraph.declaration.indexOf("controlled_device")>=0) {
					controlledDevices.add(templGraph);
				}
				if(templGraph.declaration.indexOf("biddable")>=0&& templGraph.declaration.indexOf("sensor")<0) {
					biddables.add(templGraph);
				}
			}
		}

		//outPath1作为利用IFD的结果
		String finalModelPathNameIFD=finalModelPath+"-ifd-scene.xml";
		File finalModelIFD=new File(finalModelPathNameIFD);
		if(!finalModelIFD.exists()) {

			modifyModel.modifyContrEnvModel(changedModelFilePathName, finalModelPathNameIFD,biddables, rulesAndTriggerRules, chooseRules, triggersStopRules);
			
			
			/////////////////////////system declaration/////////////////////////////////////////////////
			//先全局声明，需要获得涉及到的所有parameters，使用getParameters方法
			//这之前需要前面先获得causal属性的initValue
			//更改parameters：对于含有distance的parameter，其value值设置为10.0，含[rid]的为数组，distance相关，同样赋值
			//根据parameters对xml文件进行全局声明，使用globalDeclaration方法
			//模型声明，先对person相关进行参数实例化，再对所有模型实例化
			//验证器验证仿真参数生成，直接根据parameters获得，不考虑synchronisation
			/////////////////////////////////////////////////////////////////////////////////////////////

			parameters=gSysDeclar.getParameters(templGraphs, allAttributeValue);
			//打印到控制板用
			for(Parameter parameter:parameters) {
				System.out.println(parameter.getStyle()+": ");
				System.out.println("  "+parameter.getName());
				System.out.println("  "+parameter.getInitValue());
			}
			
			for(Parameter parameter:parameters) {
				if(parameter.getInitValue()==null) {
					if(parameter.getName().indexOf("distance")>=0) {
						if(parameter.getName().indexOf("[rid]")>=0) {
							String name=parameter.getName().replace("[rid]", "[5]");
							parameter.setName(name);
							parameter.setInitValue("{10.0,10.0,10.0,10.0,10.0}");
						}else {
							parameter.setInitValue("10.0");
						}
					}
					
				}
			}
			rulesSameAttributes=setParameter.getRulesWithSameAttribute(rulesAndTriggerRules, attributes, rules);
			allAttributeValue=setParameter.getAllAttributeValue(rulesSameAttributes,triggersStopRules);
			for(String[] attributeValue:allAttributeValue) {
				for(Parameter parameter:parameters) {
					if(attributeValue[0].equals(parameter.getName())) {
						parameter.setInitValue(attributeValue[1]);
					}
				}
			}
			//打印到控制板用
			for(Parameter parameter:parameters) {
				System.out.println(parameter.getStyle()+": ");
				System.out.println("  "+parameter.getName());
				System.out.println("  "+parameter.getInitValue());
			}
			
			////////////////////////确定自治模型每个时间点的值//////////////////////////////
			/////////////////////////这个是通用的/////////////////////////////
			instances=getDeclareInstance(templGraphs, allTime);
			

			gSysDeclar.globalDeclaration(finalModelPathNameIFD, finalModelPathNameIFD,parameters);
			gSysDeclar.modelDeclaration(finalModelPathNameIFD,finalModelPathNameIFD, templGraphs,instances);		
			gSysDeclar.setQuery(finalModelPathNameIFD,finalModelPathNameIFD, parameters,String.valueOf(allTime));

		}
		

		pamaInst.parameters=parameters;
		pamaInst.instances=instances;
		return pamaInst;
	}
	
	////////////////////////确定自治模型每个时间点的值//////////////////////////////
	/////////////////////////这个是通用的/////////////////////////////
	public List<String> getDeclareInstance(List<TemplGraph> templGraphs,int allTime){
		RandomWay rWay=new RandomWay();
		List<String> instances =new ArrayList<String>();
		List<BiddableTimeValue> biddablesTimeValue=rWay.getBiddabelsTimeValue(templGraphs);
		
//		for(BiddableTimeValue biddableTimeValue:biddablesTimeValue) {
//			System.out.println(biddableTimeValue.name);
//			rWay.setTimeValues(biddableTimeValue.timeValues, 300);
//		}
		//设置仿真时间 allTime

		rWay.setAllBiddablesTimeValue(biddablesTimeValue,allTime);
		
		for(BiddableTimeValue biddableTimeValue:biddablesTimeValue) {
			System.out.println(biddableTimeValue.name);
			for(TimeValue timeValue:biddableTimeValue.timeValues) {
				System.out.println(timeValue.timeName);
				System.out.println(timeValue.timeValue);
			}
		}
		///////////对biddable模型的实例化(根据ifd图规划的场景模型)///////////////////
		instances=rWay.getInstancesString(biddablesTimeValue);
		return instances;
	}
	
	public class ParameterInstances{
		public List<Parameter> parameters;
		public List<String> instances;
	}

}
