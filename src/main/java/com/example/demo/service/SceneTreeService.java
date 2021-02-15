package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.dom4j.DocumentException;

import com.example.demo.bean.AttributeValue;
import com.example.demo.bean.Parameter;
import com.example.demo.bean.SceneChild;
import com.example.demo.bean.ScenesTree;

import com.example.demo.service.RandomWay.AttrPiecewise;
import com.example.demo.service.TGraphToDot.Trigger;
import com.example.demo.service.TemplateGraph.TemplGraph;
//import com.simulate.TGraphToDot.Trigger;
//import com.simulate.RandomWay.AttrPiecewise;
//import com.simulate.GenerateSysDeclaration.Parameter;
import com.example.demo.service.GenerateSysDeclaration;

public class SceneTreeService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/////////////////////////////获得场景树的数据并生成所有场景仿真模型/////////////////////////////////
	public ScenesTree getScenesTreeAndAllModels(List<String> attributes,List<Trigger> triggers,List<Parameter> parameters,String parentName,String finalPath,
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
						AttributeValue attributeValue=new AttributeValue();
						attributeValue.setName(attrValue[0]);
						parameter.setInitValue(attrValue[1]);
						attributeValue.setValue(Double.parseDouble(attrValue[1]));
						sceneChild.addChildren(attributeValue);
						break;
					}
				}
			}
			AttributeValue attributeValue=new AttributeValue();
			attributeValue.setName("random-scene"+i+"\n result");
			sceneChild.addChildren(attributeValue);
			scenesTree.addChildren(sceneChild);
			String finalModelPathNameRandom=finalPath+"-random-scene"+i+".xml";
			gSysDeclar.globalDeclaration(changedModelFilePathName,finalModelPathNameRandom, parameters);
			gSysDeclar.modelDeclaration(finalModelPathNameRandom,finalModelPathNameRandom, templGraphs,instances);		
			gSysDeclar.setQuery(finalModelPathNameRandom,finalModelPathNameRandom, parameters,String.valueOf(allTime));
		}
		
		return scenesTree;
	}
	
	
	

}
