package com.example.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.bean.Action;
import com.example.demo.bean.AllCauseRuleInput;
import com.example.demo.bean.AllRuleAnalysisResult;
import com.example.demo.bean.AllScenesAnalysisInput;
import com.example.demo.bean.CauseRuleInput;
import com.example.demo.bean.ConflictTime;
import com.example.demo.bean.DataTimeValue;
import com.example.demo.bean.DeviceAnalysResult;
import com.example.demo.bean.DeviceStateName;
import com.example.demo.bean.GenerateModelParameters;
import com.example.demo.bean.GenerateModelParametersAndScene;
import com.example.demo.bean.GraphNode;
import com.example.demo.bean.Rule;
import com.example.demo.bean.RuleAndCause;
import com.example.demo.bean.RuleCauseRuleInput;
import com.example.demo.bean.RuleText;
import com.example.demo.bean.RulesAllScenesSimulationTime;
import com.example.demo.bean.RulesSceneSimulationTime;
import com.example.demo.bean.Scene;
import com.example.demo.bean.ScenesTree;
import com.example.demo.bean.StateAndRuleAndCauseRule;
import com.example.demo.bean.StateCauseRulesAndRelativeRules;
import com.example.demo.bean.StateChangeCauseRuleInput;
import com.example.demo.bean.StateChangeCauseRules;
import com.example.demo.bean.StateChangeFast;
import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.WholeAndCurrentChangeCauseRule;
import com.example.demo.service.GenerateSysDeclaration;
import com.example.demo.service.RuleAnalysisService;
import com.example.demo.service.RuleService;
import com.example.demo.service.SceneService;
import com.example.demo.service.SceneTreeService;
import com.example.demo.service.SimulateResultService;
import com.example.demo.service.TGraphToDot;
import com.example.demo.service.TemplGraphService;
import com.example.demo.service.ToNode;

@CrossOrigin
@RestController
//@Controller
@RequestMapping("/str")
public class TxtStrController {

	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		TxtStrController txtController=new TxtStrController();
		SceneTreeService sceneTreeService=new SceneTreeService();
		RuleService ruleService=new RuleService();	
		String ruleText=" IF Person.number>0 AND Air.temperature<=10.0 THEN M.wclosePulse\r\n" + 
				"\r\n" + 
				" IF Person.number>0 AND Air.temperature>=35.0 THEN M.wopenPulse\r\n" + 
				"\r\n" + 
				" IF Person.number>0 AND Air.temperature>=25.0 THEN M.coldPulse\r\n" + 
				" \r\n" + 
				" IF Person.number>0 AND Air.temperature<=21.0 THEN M.hotPulse\r\n" + 
				" \r\n" + 
				
				" \r\n" + 
				" IF Person.number>0 AND Air.temperature>30.0 THEN M.fonPulse\r\n" + 
				"\r\n" + 
				
				"\r\n" + 
				" IF AirConditioner.hotOn THEN M.wclosePulse\r\n" + 
				"\r\n" + 
				" IF AirConditioner.coldOn THEN M.wclosePulse\r\n" + 
				" \r\n" + 
				" IF Person.distanceFromMc<=2 THEN M.monPulse\r\n" + 
				" \r\n" + 
				" IF Person.distanceFromPro<=2 THEN M.ponPulse\r\n" + 
				" \r\n" + 
				" IF Person.number>0 AND rain=0 THEN M.wopenPulse,M.bopenPulse\r\n" + 
				"\r\n" + 
				" IF Projector.pon THEN M.bclosePulse,M.wclosePulse\r\n" + 
				" \r\n" + 
				" IF Person.number>0 AND Air.co2ppm>800.0 THEN M.afonPulse\r\n" + 
				" \r\n" + 
				" IF Person.number>0 AND Air.humidity<30.0 THEN M.ahonPulse\r\n" + 
				" \r\n" + 
				" IF Person.distanceFromPro>2 THEN M.poffPulse\r\n" + 
				" \r\n" + 
				" IF Air.co2ppm<=400.0 THEN M.afoffPulse\r\n" + 
				" \r\n" + 
				" IF Person.distanceFromMc>2 THEN M.moffPulse\r\n" + 
				" \r\n" + 
				" IF Air.humidity>=30.0 THEN M.ahoffPulse\r\n" + 
				" \r\n" + 
				" IF Person.number=0 FOR 30m THEN M.poffPulse,M.wclosePulse,M.bclosePulse,M.aoffPulse,M.afoffPulse,M.boffPulse,M.moffPulse,M.ahoffPulse,M.foffPulse\r\n" + 
				" \r\n" + 
				" IF Air.humidity>60.0 THEN M.wclosePulse \r\n" + 
				" \r\n" + 
				" IF rain=1 THEN M.wclosePulse";
		String initModelName="window22.xml";
		String simulationTime="300";
		ruleText=ruleText.replace("\r\n", "\n");
		List<Rule> rules=ruleService.getRuleList(ruleText);
		System.out.println(rules);
		
		GenerateModelParameters generateModelParameters=sceneTreeService.getAllSimulationModels(rules, "D:\\example", initModelName, simulationTime);
		System.out.println(generateModelParameters);
		

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
	
	
	@RequestMapping("/upload")
	@ResponseBody
	public void uploadFile(@RequestParam("file") MultipartFile uploadedFile) throws DocumentException, IOException {
		//////////////上传的环境本体文件，存储在D:\\workspace位置
		if (uploadedFile == null) {
            System.out.println("上传失败，无法找到文件！");
        }
        // BMP、JPG、JPEG、PNG、GIF
        String initFileName = uploadedFile.getOriginalFilename();
        String filePath="D:\\workspace"+"\\"+initFileName;
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));

        outputStream.write(uploadedFile.getBytes());
        outputStream.flush();
        outputStream.close();
        //逻辑处理
        System.out.println(initFileName + "上传成功");
	}
	
	
//	@RequestMapping(value="generateAllModels",method = RequestMethod.GET)
//	@ResponseBody
//	public List<Rule> generateAllModels(String ruleText,String initModelName,String simulationTime) throws DocumentException, IOException {
//		SceneTreeService sceneTreeService=new SceneTreeService();
//		RuleService ruleService=new RuleService();		
////		String test="test";
//		List<Rule> rules=ruleService.getRuleList(ruleText);
////		GenerateModelParameters generateModelParameters=sceneTreeService.getAllSimulationModels(rules, "D:\\workspace", initModelName, simulationTime);
//		return rules;
//	}

	@RequestMapping(value="/generateAllModels",method = RequestMethod.POST)
	@ResponseBody
	public GenerateModelParameters generateAllModels(@RequestBody List<String> ruleTextLines,String initModelName,String simulationTime) throws DocumentException, IOException {
		SceneTreeService sceneTreeService=new SceneTreeService();
		RuleService ruleService=new RuleService();		
//		String test="test";
		List<Rule> rules=ruleService.getRuleList(ruleTextLines);
		
		GenerateModelParameters generateModelParameters=sceneTreeService.getAllSimulationModels(rules, "D:\\workspace", initModelName, simulationTime);
		generateModelParameters.rules=rules;
		return generateModelParameters;
	}
	
	@RequestMapping(value="/simulateAllModels",method = RequestMethod.POST)
	@ResponseBody
	public List<Scene> simulateAllModels(@RequestBody GenerateModelParameters generateModelParameters,String initModelName) throws DocumentException, IOException {

		SceneService sceneService=new SceneService();
		List<Scene> scenes=new ArrayList<Scene>();
		int simulationDataNum=generateModelParameters.simulationDataNum;
		ScenesTree scenesTree=generateModelParameters.scenesTree;
		scenes=sceneService.getAllSimulationDataTimeValue(scenesTree, "D:\\workspace", initModelName, "D:\\tools\\uppaal-4.1.24\\uppaal-4.1.24\\bin-Windows", simulationDataNum);
		
		return scenes;
	}
	@RequestMapping(value="/simulateModels",method = RequestMethod.POST)
	@ResponseBody
	public GenerateModelParametersAndScene simulateModel(@RequestBody GenerateModelParameters generateModelParameters,String sceneName,String initModelName) throws DocumentException, IOException {
		////////////////////TODO 同时返回Actions	
		SceneService sceneService=new SceneService();
		GenerateModelParametersAndScene generateModelParametersAndScene=new GenerateModelParametersAndScene();
		
		
		int simulationDataNum=generateModelParameters.simulationDataNum;
		Scene scene=sceneService.getSimulationDataTimeValue(sceneName, "D:\\workspace", initModelName, "D:\\tools\\uppaal-4.1.24\\uppaal-4.1.24\\bin-Windows", simulationDataNum);
		generateModelParametersAndScene.generateModelParameters=generateModelParameters;
		generateModelParametersAndScene.scene=scene;
		return generateModelParametersAndScene;
	}
	
	@RequestMapping(value="/getDeviceAnalysisResult",method = RequestMethod.POST)
	@ResponseBody
	public Scene getDeviceAnalysisResult(@RequestBody RulesSceneSimulationTime rulesSceneSimulationTime,String initModelName,String equivalentTime,String intervalTime) throws DocumentException, IOException {
			
		SceneService sceneService=new SceneService();
		
		Scene scene=rulesSceneSimulationTime.scene;
		List<Rule> rules=rulesSceneSimulationTime.rules;
		String simulationTime=rulesSceneSimulationTime.simulationTime;
		scene=sceneService.getDeviceAnalysisResult(scene, rules, simulationTime,"D:\\workspace", initModelName, equivalentTime, intervalTime);

		return scene;
	}
	
	@RequestMapping(value="/getAllDeviceAnalysisResult",method = RequestMethod.POST)
	@ResponseBody
	public List<Scene> getAllDeviceAnalysisResult(@RequestBody RulesAllScenesSimulationTime rulesAllScenesSimulationTime,String initModelName,String equivalentTime,String intervalTime) throws DocumentException, IOException {
			
		SceneService sceneService=new SceneService();
		List<Scene> newScenes=new ArrayList<Scene>();
		List<Scene> scenes=rulesAllScenesSimulationTime.scenes;
		List<Rule> rules=rulesAllScenesSimulationTime.rules;
		String simulationTime=rulesAllScenesSimulationTime.simulationTime;
		for(Scene scene:scenes) {
			scene=sceneService.getDeviceAnalysisResult(scene, rules, simulationTime,"D:\\workspace", initModelName, equivalentTime, intervalTime);
			newScenes.add(scene);
		}
		

		return newScenes;
	}
	
//	@RequestMapping(value="/getConflictCauseAnalysisResult",method = RequestMethod.POST)
//	@ResponseBody
//	public List<StateCauseRulesAndRelativeRules> getConflictCauseAnalysisResult(@RequestBody CauseRuleInput causeRuleInput,String initModelName) throws DocumentException, IOException {
//			
//		SceneService sceneService=new SceneService();
//		ToNode toNode=new ToNode();
//		List<StateCauseRulesAndRelativeRules> stateCauseRulesAndRelativeRules=new ArrayList<StateCauseRulesAndRelativeRules>();
//		ConflictTime conflictStateTime=causeRuleInput.conflictStateTime;
//		List<DataTimeValue> triggeredRulesName=causeRuleInput.triggeredRulesName;
//		DeviceStateName deviceStateName=causeRuleInput.deviceStateName;
//		List<Rule> rules=causeRuleInput.rules;
//		List<GraphNode> graphNodes=new ArrayList<GraphNode>();
//		
//		graphNodes=toNode.getGraphNodes("D:\\workspace", initModelName);
//		stateCauseRulesAndRelativeRules=sceneService.getConflictCauseRules(conflictStateTime, triggeredRulesName, deviceStateName, rules, graphNodes);
//		return stateCauseRulesAndRelativeRules;
//	}
	
	@RequestMapping(value="/getConflictCauseAnalysisResult",method = RequestMethod.POST)
	@ResponseBody
	public List<StateAndRuleAndCauseRule> getConflictCauseAnalysisResult(@RequestBody CauseRuleInput causeRuleInput,String initModelName) throws DocumentException, IOException {
			
		SceneService sceneService=new SceneService();
		ToNode toNode=new ToNode();
		List<StateAndRuleAndCauseRule> stateAndRuleAndCauseRules=new ArrayList<StateAndRuleAndCauseRule>();
		ConflictTime conflictStateTime=causeRuleInput.conflictStateTime;
		List<DataTimeValue> triggeredRulesName=causeRuleInput.triggeredRulesName;
		DeviceStateName deviceStateName=causeRuleInput.deviceStateName;
		List<Rule> rules=causeRuleInput.rules;
		List<GraphNode> graphNodes=new ArrayList<GraphNode>();
		
		graphNodes=toNode.getGraphNodes("D:\\workspace", initModelName);
		stateAndRuleAndCauseRules=sceneService.getDeviceConflictCauseRules(conflictStateTime, triggeredRulesName, deviceStateName, rules, graphNodes);
		return stateAndRuleAndCauseRules;
	}
	
	@RequestMapping(value="/getAllConflictCauseAnalysisResult",method = RequestMethod.POST)
	@ResponseBody
	public List<List<StateAndRuleAndCauseRule>> getAllConflictCauseAnalysisResult(@RequestBody AllCauseRuleInput allCauseRuleInput,String initModelName) throws DocumentException, IOException {
			
		SceneService sceneService=new SceneService();
		ToNode toNode=new ToNode();
		
		List<List<StateAndRuleAndCauseRule>> stateAndRuleAndCauseRulesList=new ArrayList<List<StateAndRuleAndCauseRule>>();
		for(int i=0;i<allCauseRuleInput.conflictStateTimes.size();i++) {
			List<StateAndRuleAndCauseRule> stateAndRuleAndCauseRules=new ArrayList<StateAndRuleAndCauseRule>();
			CauseRuleInput causeRuleInput=new CauseRuleInput();
			causeRuleInput.conflictStateTime=allCauseRuleInput.conflictStateTimes.get(i);
			causeRuleInput.deviceStateName=allCauseRuleInput.deviceStateName;
			causeRuleInput.triggeredRulesName=allCauseRuleInput.triggeredRulesName;
			causeRuleInput.rules=allCauseRuleInput.rules;
//			ConflictTime conflictStateTime=causeRuleInput.conflictStateTime;
//			List<DataTimeValue> triggeredRulesName=causeRuleInput.triggeredRulesName;
//			DeviceStateName deviceStateName=causeRuleInput.deviceStateName;
//			List<Rule> rules=causeRuleInput.rules;
//			stateAndRuleAndCauseRules=sceneService.getDeviceConflictCauseRules(conflictStateTime, triggeredRulesName, deviceStateName, rules, graphNodes);
			stateAndRuleAndCauseRules=getConflictCauseAnalysisResult(causeRuleInput, initModelName);
			stateAndRuleAndCauseRulesList.add(stateAndRuleAndCauseRules);
		}
		
		
		
		
		return stateAndRuleAndCauseRulesList;
	}
	
	@RequestMapping(value="/getAllScenesConflictCauseAnalysisResult",method = RequestMethod.POST)
	@ResponseBody
	public List<List<StateAndRuleAndCauseRule>> getAllScenesConflictCauseAnalysisResult(@RequestBody AllScenesAnalysisInput allScenesConflictInput,String deviceName,String initModelName) throws DocumentException, IOException {
			
		SceneService sceneService=new SceneService();
		ToNode toNode=new ToNode();
		List<GraphNode> graphNodes=new ArrayList<GraphNode>();
		List<Rule> rules=new ArrayList<Rule>();
		List<Scene> scenes=new ArrayList<Scene>();
		List<List<StateAndRuleAndCauseRule>> stateAndRuleAndCauseRulesList=new ArrayList<List<StateAndRuleAndCauseRule>>();
		graphNodes=toNode.getGraphNodes("D:\\workspace", initModelName);
		rules=allScenesConflictInput.rules;
		scenes=allScenesConflictInput.scenes;
		
		for(Scene scene:scenes) {
			List<StateAndRuleAndCauseRule> stateAndRuleAndCauseRules=new ArrayList<StateAndRuleAndCauseRule>();
			for(DeviceAnalysResult deviceAnalysResult:scene.getDevicesAnalysResults()) {
				if(deviceAnalysResult.deviceName.equals(deviceName)) {
					if(deviceAnalysResult.statesConflict.hasConflict) {
						
						List<DataTimeValue> triggeredRulesName=scene.getTriggeredRulesName();
						DeviceStateName deviceStateName=deviceAnalysResult.deviceStateName;
						for(ConflictTime conflictTime:deviceAnalysResult.statesConflict.conflictTimes) {
							stateAndRuleAndCauseRules=sceneService.getDeviceConflictCauseRules(conflictTime, triggeredRulesName, deviceStateName, rules, graphNodes);
							
						}
						break;
					}
				}
			}
			stateAndRuleAndCauseRulesList.add(stateAndRuleAndCauseRules);
		}
		
		return stateAndRuleAndCauseRulesList;
	}
	
	@RequestMapping(value="/getStateChangeFastCauseAnalysisResult",method = RequestMethod.POST)
	@ResponseBody
	public WholeAndCurrentChangeCauseRule getStateChangeFastCauseAnalysisResult(@RequestBody StateChangeCauseRuleInput stateChangeCauseRuleInput) throws DocumentException, IOException {
			
		SceneService sceneService=new SceneService();
		
		WholeAndCurrentChangeCauseRule wholeAndCurrentChangeCauseRule=new WholeAndCurrentChangeCauseRule();
		List<DataTimeValue> triggeredRulesName=stateChangeCauseRuleInput.triggeredRulesName;
		DeviceStateName deviceStateName=stateChangeCauseRuleInput.deviceStateName;
		List<StateChangeFast> stateChangeFasts=stateChangeCauseRuleInput.stateChangeFasts;
		StateChangeFast stateChangeFast=stateChangeCauseRuleInput.stateChangeFast;
		wholeAndCurrentChangeCauseRule=sceneService.getStatesChangeFastCauseRules(stateChangeFasts, stateChangeFast, triggeredRulesName, deviceStateName);
		
		
		return wholeAndCurrentChangeCauseRule;
	}
	
	@RequestMapping(value="/getAllScenesFastChangeCauseAnalysisResult",method = RequestMethod.POST)
	@ResponseBody
	public List<List<StateChangeCauseRules>> getAllScenesChangeFastCauseAnalysisResult(@RequestBody List<Scene> scenes,String deviceName) throws DocumentException, IOException {
			
		SceneService sceneService=new SceneService();
		
		List<List<StateChangeCauseRules>> stateChangeCauseRulesList=sceneService.getAllScenesFastChangeCauseRules(scenes, deviceName);
		
		return stateChangeCauseRulesList;
	}
	
	
	@RequestMapping(value="/getAllScenesRulesAnalysisResult",method = RequestMethod.POST)
	@ResponseBody
	public AllRuleAnalysisResult getAllScenesRulesAnalysisResult(@RequestBody AllScenesAnalysisInput allScenesAnalysisInput,String initFileName,String simulationTime,String equivalentTime,String intervalTime) throws DocumentException, IOException {
			
		
		RuleAnalysisService ruleAnalysisService=new RuleAnalysisService();
		List<Scene> scenes=new ArrayList<Scene>();
		List<Rule> rules=new ArrayList<Rule>();
		AllRuleAnalysisResult allRuleAnalysisResult=new AllRuleAnalysisResult();
		scenes=allScenesAnalysisInput.scenes;
		rules=allScenesAnalysisInput.rules;
		System.out.println(" initFileName:"+initFileName+" simulationTime:"+simulationTime+" equivalentTime:"+equivalentTime+" intervalTime:"+intervalTime);
		allRuleAnalysisResult=ruleAnalysisService.getAllRuleAnalysis(scenes, rules, "D:\\workspace", initFileName, simulationTime, equivalentTime, intervalTime);
		
		
		return allRuleAnalysisResult;
	}
	
	@RequestMapping(value="/getRuleCauseRule",method = RequestMethod.POST)
	@ResponseBody
	public List<RuleAndCause> getRuleCauseRule(@RequestBody RuleCauseRuleInput ruleCauseRuleInput,String initFileName) throws DocumentException, IOException {
			
		
		SceneService sceneService=new SceneService();
		ToNode toNode=new ToNode();
		List<GraphNode> graphNodes=new ArrayList<GraphNode>();
		List<Rule> causeRules=ruleCauseRuleInput.causeRules;
		List<Rule> rules=ruleCauseRuleInput.rules;
		List<RuleAndCause> rulesAndCause=new ArrayList<RuleAndCause>();
		graphNodes=toNode.getGraphNodes("D:\\workspace", initFileName);
		for(Rule rule:causeRules) {
			RuleAndCause ruleAndCause=sceneService.getRuleCauseRulesfromIFDGraph(graphNodes, rules, rule.getRuleName());
			rulesAndCause.add(ruleAndCause);
			for(GraphNode graphNode:graphNodes) {
				graphNode.flag=false;
			}
		}
		
		
		
		return rulesAndCause;
	}


}
