package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.bean.Action;
import com.example.demo.bean.Rule;
import com.example.demo.bean.Scene;
import com.example.demo.bean.ScenesTree;
import com.example.demo.bean.TemplGraph;
import com.example.demo.service.GenerateSysDeclaration;
import com.example.demo.service.RuleService;
import com.example.demo.service.SceneService;
import com.example.demo.service.SceneTreeService;
import com.example.demo.service.SimulateResultService;
import com.example.demo.service.TGraphToDot;
import com.example.demo.service.TemplGraphService;
import com.example.demo.service.GetTemplate.Template;

@CrossOrigin
@RestController
//@Controller
@RequestMapping("/file")
public class Controller {
	public static void main(String[] args) throws DocumentException, IOException {
		Controller controller=new Controller();
		ScenesTree scenesTree=controller.getScenesTree();
		System.out.println(scenesTree);
		List<Scene> scenes=controller.getAllSceneAnalysisResult("D:\\exp", "exp0108", 84);
		System.out.println(scenes);
	}

	@Autowired
	SceneTreeService sceneTreeService;
	@Autowired
	SimulateResultService simulateResultService;
	@Autowired
	GenerateSysDeclaration gSysDeclaration;
	@Autowired
	SceneService sceneService;
	@Autowired
	TemplGraphService templGraphService;
	@Autowired
	RuleService ruleService;
	@Autowired
	TGraphToDot tDot;
	
	
	
	


	
	@RequestMapping(value="getScenesTree",method = RequestMethod.GET)
	@ResponseBody
	public ScenesTree getScenesTree() throws DocumentException, IOException {
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

		return scenesTree;
	}
	
	
	@RequestMapping(value="getAllSimulateResult",method = RequestMethod.GET)
	@ResponseBody
	public void getAllSimulateResult(String filePath,String initModelName,int treeSize,String uppaalBinPath) throws IOException, DocumentException {
		SimulateResultService simulateResultService=new SimulateResultService();
		GenerateSysDeclaration gSysDeclaration=new GenerateSysDeclaration();
		
		List<String> modelNames=new ArrayList<String>();
		modelNames.add(initModelName+"-final-ifd-scene");
		for(int i=0;i<treeSize;i++) {
			modelNames.add(initModelName+"-final-random-scene"+i);
		}
		int dataNum=gSysDeclaration.getSimulateDataNum(filePath+"\\"+initModelName+"-final-ifd-scene");
		simulateResultService.getAllSimulateResultFile(filePath, modelNames, uppaalBinPath,dataNum);
		
	}
	
	@RequestMapping(value="getSimulateResult",method = RequestMethod.GET)
	@ResponseBody
	public void getSimulateResult(String filePath,String initModelName,String sceneName,String uppaalBinPath) throws IOException, DocumentException {
		SimulateResultService simulateResultService=new SimulateResultService();
		GenerateSysDeclaration gSysDeclaration=new GenerateSysDeclaration();
		
		String modelName=initModelName+"-final-"+sceneName;
		int dataNum=gSysDeclaration.getSimulateDataNum(filePath+"\\"+initModelName+"-final-"+sceneName+".xml");
		simulateResultService.getSimulateResultFile(filePath, modelName, uppaalBinPath,dataNum);
	}
	
	@RequestMapping(value="getSceneAnalysisResult",method = RequestMethod.GET)
	@ResponseBody
	public Scene getSceneAnalysisResult(String filePath,String initModelName,String sceneName) throws IOException, DocumentException {
		TemplGraphService templGraphService=new TemplGraphService();
		TGraphToDot tDot=new TGraphToDot();
		RuleService ruleService=new RuleService();
		SceneService sceneService=new SceneService();
		
		filePath=filePath.replace("%5C", "\\");
		Scene scene=new Scene();
		sceneName=sceneName.trim();
		String modelName=initModelName+"-final-"+sceneName;
		String uppaalBinPath="D:\\tools\\uppaal-4.1.24\\uppaal-4.1.24\\bin-Windows";
		String rulePath="D:\\rules0105.txt";
		String allTime="300.0099999998758";
		String equivalentTime="24";
		String intervalTime="300";
		
		List<Rule> rules=ruleService.getRuleListFromTxt(rulePath);
		
		getSimulateResult(filePath, initModelName, sceneName, uppaalBinPath);
		
		List<TemplGraph> templGraphs=templGraphService.getTemplGraphs(filePath+"\\"+modelName+".xml");
		
		List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration.indexOf("controlled_device")>=0) {
				controlledDevices.add(templGraph);
			}
		}
		List<Action> actions=tDot.getActions(rules,controlledDevices);
		
		
		scene=sceneService.getSceneAnalysis(filePath, modelName, controlledDevices, actions, allTime, equivalentTime, intervalTime);
		return scene;
	}
	
	@RequestMapping(value="getAnalysisResult",method = RequestMethod.GET)
	@ResponseBody
	public Scene getAnalysisResult() throws IOException, DocumentException {
		String filePath="D:\\exp";
		String initModelName="exp0108";
		String sceneName="random-scene2";
		Scene scene=getSceneAnalysisResult(filePath, initModelName, sceneName);
		return scene;
	}
	
	@RequestMapping(value="getResult",method = RequestMethod.GET)
	@ResponseBody
	public String getResult() throws IOException, DocumentException {
		String filePath="D:\\exp";
		String initModelName="exp0108";
		String sceneName="random-scene2";
		Scene scene=getSceneAnalysisResult(filePath, initModelName, sceneName);
		return scene.getSceneName();
	}
	
	@RequestMapping(value="getAllSceneAnalysisResult",method = RequestMethod.GET)
	@ResponseBody
	public List<Scene> getAllSceneAnalysisResult(String filePath,String initModelName,int treeSize) throws IOException, DocumentException {
		List<Scene> scenes=new ArrayList<Scene>();
		TemplGraphService templGraphService=new TemplGraphService();
		TGraphToDot tDot=new TGraphToDot();
		RuleService ruleService=new RuleService();
		SceneService sceneService=new SceneService();
		
		filePath=filePath.replace("%5C", "\\");
		String uppaalBinPath="D:\\tools\\uppaal-4.1.24\\uppaal-4.1.24\\bin-Windows";
		String rulePath="D:\\rules0105.txt";
		String allTime="300.0099999998758";
		String equivalentTime="24";
		String intervalTime="300";
		String modelName=initModelName+"-final-"+"random-scene0";
		List<Rule> rules=ruleService.getRuleListFromTxt(rulePath);
		getAllSimulateResult(filePath, initModelName, treeSize, uppaalBinPath);
		List<TemplGraph> templGraphs=templGraphService.getTemplGraphs(filePath+"\\"+modelName+".xml");
		List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration.indexOf("controlled_device")>=0) {
				controlledDevices.add(templGraph);
			}
		}
		List<Action> actions=tDot.getActions(rules,controlledDevices);
		for(int i=0;i<treeSize;i++) {
			String modeName=initModelName+"-final-"+"random-scene"+i;
			scenes.add(sceneService.getSceneAnalysis(filePath, modeName, controlledDevices, actions, allTime, equivalentTime, intervalTime));
		}
		
		return scenes;
	}


	
}
