package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.example.demo.bean.AttributeValue;
import com.example.demo.bean.Parameter;
import com.example.demo.bean.Rule;
import com.example.demo.bean.SceneChild;
import com.example.demo.bean.ScenesTree;
import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.Trigger;
import com.example.demo.service.RandomWay.AttrPiecewise;
import com.example.demo.service.GenerateSysDeclaration;
import com.example.demo.service.GetTemplate.Template;
import com.example.demo.service.IFDSceneService.ParameterInstances;

@Service
public class SceneTreeService {

	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		
		SceneTreeService sceneTreeService=new SceneTreeService();
		RuleService ruleService=new RuleService();
		int allTime=300;
		String initModelFileName="exp0108";	
		String initModelPath="D:\\";
		String targetPath="D:\\exp\\";
		String rulePath="D:\\rules0105.txt";
		String dotPath="D:\\exp\\ifd.dot";
		List<Rule> rules=ruleService.getRuleListFromTxt(rulePath);
		String parentName="SmartHome";
		
		ScenesTree scenesTree=sceneTreeService.getAllModels(allTime, initModelFileName, initModelPath, targetPath, dotPath, rules, parentName);

		System.out.println(scenesTree);
	}
	
	/////////////////////////////获得场景树的数据并生成所有场景仿真模型/////////////////////////////////
	public ScenesTree getScenesTreeAndRandomAllModels(List<String> attributes,List<Trigger> triggers,List<Parameter> parameters,String parentName,String finalPath,
			String changedModelFilePathName,List<TemplGraph> templGraphs,List<String> instances,String allTime) throws DocumentException, IOException {
		ScenesTree scenesTree=new ScenesTree();
		RandomWay rWay=new RandomWay();
		GenerateSysDeclaration gSysDeclar=new GenerateSysDeclaration();
		List<AttrPiecewise> attrPiecewises=rWay.getAttrPiecewises(attributes, triggers);
		int allPieceNum=rWay.getAttrPieceNum(attrPiecewises);
		scenesTree.setName(parentName);
		for(int i=0;i<allPieceNum;i++) {
			SceneChild sceneChild=new SceneChild();
			sceneChild.setName("random-scene"+i);
			List<String[]> allAttributeValueRandom=rWay.getAttrValues(attrPiecewises, i);
			for(String[] attrValue:allAttributeValueRandom) {
				for(Parameter parameter:parameters) {
					if(parameter.getName().equals(attrValue[0])) {
						
						parameter.setInitValue(attrValue[1]);
						
						break;
					}
				}
				AttributeValue attributeValue=new AttributeValue();
				attributeValue.setName(attrValue[0]);
				attributeValue.setValue(Double.parseDouble(attrValue[1]));
				sceneChild.addChildren(attributeValue);
				
			}
			AttributeValue attributeValue=new AttributeValue();
			attributeValue.setName("random-scene"+i+" result");
			sceneChild.addChildren(attributeValue);
			scenesTree.addChildren(sceneChild);
			String finalModelPathNameRandom=finalPath+"-random-scene"+i+".xml";
			File finalModelRandomFile=new File(finalModelPathNameRandom);
			if(!finalModelRandomFile.exists()) {
				gSysDeclar.globalDeclaration(changedModelFilePathName,finalModelPathNameRandom, parameters);
				gSysDeclar.modelDeclaration(finalModelPathNameRandom,finalModelPathNameRandom, templGraphs,instances);		
				gSysDeclar.setQuery(finalModelPathNameRandom,finalModelPathNameRandom, parameters,String.valueOf(allTime));
			}
			
		}
		
		return scenesTree;
	}
	
	
	public ScenesTree getAllModels(int allTime,String initModelFileName,String initModelPath,String targetPath,String dotPath,
		List<Rule> rules,String parentName) throws DocumentException, IOException {
		IFDSceneService ifdSceneService=new IFDSceneService();
		GetTemplate getTemplate=new GetTemplate();
		TemplGraphService tGraph=new TemplGraphService();
		GenerateContrModel gcModel=new GenerateContrModel();
		SetParameter setParameter=new SetParameter();
		TGraphToDot tDot=new TGraphToDot();
		GetTemplate gTemplate=new GetTemplate();
		
		ScenesTree scenesTree=new ScenesTree();
		
		String changedModelFileName=initModelFileName+"-change";
		String finalModelFileName=initModelFileName+"-final";
		String initModelFilePathName=initModelPath+initModelFileName+".xml";
		String changedModelFilePathName=targetPath+changedModelFileName+".xml";
		String finalModelPath=targetPath+finalModelFileName;
		//转成可解析xml文件
		getTemplate.deletLine(initModelFilePathName, changedModelFilePathName, 2);
		//将xml文件中的template解析
		List<Template> templates=getTemplate.getTemplate(changedModelFilePathName);
		//获得templGraph
		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
		for(Template template:templates) {
			templGraphs.add(tGraph.getTemplGraph(template));
		}
		
 		/////////////////////////////生成控制器模型///////////////
 		/////////先获得Rule类型的rules
 		/////////然后生成规则模型
 		//////////////////////////////////////////////////////////		
		
		gcModel.deleteRuleNode(changedModelFilePathName);
		gcModel.generateContrModel(changedModelFilePathName, rules, templGraphs);
		
		/////////////////////生成环境模型//////////////////////////////
		//将模型转成Template类型，再确定模型determine方法（有branchpoint则是不确定的）
		//获得biddable类型的多个确定模型后，确定最终的确定模型finalDetermine
		//最终的确定模型，写入xml文件中
		//删除最初的不确定模型
		///////////////////////////////////////////////////////////////
		ifdSceneService.getDetermineEnvModel(templGraphs, changedModelFilePathName);
		
		/////////////////////////生成IFD///////////////////////////////
		//getIFD方法
		/////根据解析出的Rule类型rules画出trigger->rule->action
		/////根据环境模型（templGraphs）获得sensor/device->trigger以及action->device,
		/////还有最后action->trigger和trigger->trigger
		///////////////////////////////////////////////////////////////
		
		templates.clear();
		templGraphs.clear();
		templates=gTemplate.getTemplate(changedModelFilePathName);
		for(Template template:templates) {
			templGraphs.add(tGraph.getTemplGraph(template));
		}
		
		ifdSceneService.getIFDGraphviz(templates, templGraphs, changedModelFilePathName, rules, dotPath);
		
		///////////////////////////analyse IFD////////////////////////////////
		//获得IFD各节点graphNodes
		//先获得IFD中的ruleNode
		//用getRulesAndTriggerRules方法获得各ruleNode所能触发的其他规则
		//获得IFD中的triggerNode
		//用getTriggerStopRules方法获得可能引起其他rule无法发生的triggerNode
		/////////////////////////////////////////////////////////////////////
		List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
		List<TemplGraph> biddables=new ArrayList<TemplGraph>();
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
		
		ParameterInstances paramInst=ifdSceneService.new ParameterInstances();
		///////////////////////生成ifd分析获得的模型///////////////////////////
		paramInst=ifdSceneService.ifdToSceneModel(changedModelFilePathName, dotPath, finalModelPath, templGraphs, rules, controlledDevices, biddables,  allTime);
		List<Parameter> parameters=paramInst.parameters;
		List<String> instances=paramInst.instances;
		///////////////////////所有随机场景模型/////////////////////
		List<String> attributes=setParameter.getCausalAttributes(templGraphs);
		List<Trigger> triggers=tDot.getTriggers(rules);
		scenesTree=getScenesTreeAndRandomAllModels(attributes, triggers, parameters, parentName, finalModelPath, changedModelFilePathName, templGraphs, instances, String.valueOf(allTime));
		return scenesTree;
	}
	
	
	

}
