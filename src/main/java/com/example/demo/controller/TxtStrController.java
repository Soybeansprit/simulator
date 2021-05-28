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
import com.example.demo.bean.StaticAnalysisResult;
import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.WholeAndCurrentChangeCauseRule;
import com.example.demo.service.AddressService;
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
		String ruleText="1. IF SmartHomeSecurity.homeMode AND temperature<=15 THEN M.heatonPulse\r\n" + 
				"\r\n" + 
				"2. IF SmartHomeSecurity.homeMode AND temperature>=30 THEN M.accoolPulse\r\n" + 
				"\r\n" + 
				"3. IF SmartHomeSecurity.homeMode AND humidity<20 THEN M.honPulse\r\n" + 
				"\r\n" + 
				"4. IF SmartHomeSecurity.homeMode AND humidity>=45 THEN M.hoffPulse\r\n" + 
				"\r\n" + 
				"5. IF SmartHomeSecurity.homeMode AND humidity>65 THEN M.fonPulse\r\n" + 
				"\r\n" + 
				"6. IF SmartHomeSecurity.homeMode AND temperature>28 THEN M.fonPulse\r\n" + 
				"\r\n" + 
				"7. IF SmartHomeSecurity.homeMode AND temperature<20 THEN M.foffPulse\r\n" + 
				"\r\n" + 
				"8. IF SmartHomeSecurity.homeMode AND rain=1 THEN M.phlwhitePulse\r\n" + 
				"\r\n" + 
				"9. IF SmartHomeSecurity.homeMode AND temperature<=10 THEN M.phlbluePulse\r\n" + 
				"\r\n" + 
				"10. IF SmartHomeSecurity.homeMode AND leak=1 THEN M.phlbluePulse\r\n" + 
				"\r\n" + 
				"11. IF SmartHomeSecurity.awayMode THEN M.phloffPulse\r\n" + 
				"\r\n" + 
				"12. IF SmartHomeSecurity.homeMode THEN M.phlwhitePulse\r\n" + 
				"\r\n" + 
				"13. IF Door.dopen THEN M.phlwhitePulse\r\n" + 
				"\r\n" + 
				"14. IF SmartHomeSecurity.homeMode AND co2ppm>=1000 THEN M.phlredPulse\r\n" + 
				"\r\n" + 
				"15. IF SmartHomeSecurity.awayMode THEN M.foffPulse\r\n" + 
				"\r\n" + 
				"16. IF Door.dopen THEN M.heatoffPulse\r\n" + 
				"\r\n" + 
				"17. IF Window.wopen THEN M.heatoffPulse\r\n" + 
				"\r\n" + 
				"18. IF SmartHomeSecurity.awayMode THEN M.heatoffPulse,M.acoffPulse,M.foffPulse,M.bclosePulse,M.boffPulse\r\n" + 
				"\r\n" + 
				"19. IF SmartHomeSecurity.homeMode AND temperature<18 THEN M.acheatPulse\r\n" + 
				"\r\n" + 
				"20. IF SmartHomeSecurity.homeMode AND temperature>30 THEN M.accoolPulse\r\n" + 
				"\r\n" + 
				"21. IF SmartHomeSecurity.homeMode THEN M.rdockPulse\r\n" + 
				"\r\n" + 
				"22. IF SmartHomeSecurity.awayMode THEN M.rstartPulse\r\n" + 
				"\r\n" + 
				"23. IF SmartHomeSecurity.awayMode THEN M.wclosePulse,M.dclosePulse\r\n" + 
				"\r\n" + 
				"24. IF number>0 THEN M.homeModePulse\r\n" + 
				"\r\n" + 
				"25. IF number=0 THEN M.awayModePulse\r\n" + 
				"\r\n" + 
				"26. IF SmartHomeSecurity.homeMode AND temperature>28 THEN M.bopenPulse\r\n" + 
				"\r\n" + 
				"27. IF SmartHomeSecurity.homeMode THEN M.bonPulse\r\n" + 
				"\r\n" + 
				"28. IF SmartHomeSecurity.homeMode AND co2ppm>=800 THEN M.fonPulse,M.wopenPulse\r\n" + 
				"\r\n" + 
				"29. IF AirConditioner.cool THEN M.wclosePulse\r\n" + 
				"\r\n" + 
				"30. IF AirConditioner.heat THEN M.wclosePulse";
		String initModelName="exp0108-person-dif.xml";
		String simulationTime="300";
		String equivalentTime="24";
		String intervalTime="300";
		ruleText=ruleText.replace("\r\n", "\n");
		List<Rule> rules=ruleService.getRuleList(ruleText);
		System.out.println(rules);
		
		GenerateModelParameters generateModelParameters=sceneTreeService.getAllSimulationModels(rules, "D:\\example", initModelName, simulationTime);
		System.out.println(generateModelParameters);
		List<Scene> scenes=txtController.simulateAllModels(generateModelParameters, initModelName);
		AllScenesAnalysisInput allScenesAnalysisInput=new AllScenesAnalysisInput();
		allScenesAnalysisInput.rules=rules;
		allScenesAnalysisInput.scenes=scenes;
		allScenesAnalysisInput.properties.add("Humidifier.hoff &  AirConditioner.cool");
		AllRuleAnalysisResult allRuleAnalysisResult=txtController.getAllScenesRulesAnalysisResult(allScenesAnalysisInput, initModelName, simulationTime, equivalentTime, intervalTime);
		System.out.println(allRuleAnalysisResult);
		

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
        String filePath=AddressService.MODEL_FILE_PATH+"\\"+initFileName;
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
	
	@RequestMapping(value="/getStaticAnalysisResult",method = RequestMethod.POST)
	@ResponseBody
	public StaticAnalysisResult getStaticAnalysisResult(@RequestBody List<String> ruleTextLines,String initFileName) throws DocumentException, IOException {
		RuleService ruleService=new RuleService();		
		List<Rule> rules=ruleService.getRuleList(ruleTextLines);
		StaticAnalysisResult staticAnalysisResult=RuleAnalysisService.getRequirementError(rules, initFileName,AddressService.MODEL_FILE_PATH);
		return staticAnalysisResult;
	}
	
	@RequestMapping(value="/generateAllModels",method = RequestMethod.POST)
	@ResponseBody
	public GenerateModelParameters generateAllModels(@RequestBody List<Rule> rules,String initModelName,String simulationTime) throws DocumentException, IOException {
		SceneTreeService sceneTreeService=new SceneTreeService();		
		GenerateModelParameters generateModelParameters=sceneTreeService.getAllSimulationModels(rules, AddressService.MODEL_FILE_PATH, initModelName, simulationTime);
		generateModelParameters.rules=rules;
		return generateModelParameters;
	}

//	@RequestMapping(value="/generateAllModels",method = RequestMethod.POST)
//	@ResponseBody
//	public GenerateModelParameters generateAllModels(@RequestBody List<String> ruleTextLines,String initModelName,String simulationTime) throws DocumentException, IOException {
//		SceneTreeService sceneTreeService=new SceneTreeService();
//		RuleService ruleService=new RuleService();		
//
//		List<Rule> rules=ruleService.getRuleList(ruleTextLines);
//		
//		GenerateModelParameters generateModelParameters=sceneTreeService.getAllSimulationModels(rules, "D:\\workspace", initModelName, simulationTime);
//		generateModelParameters.rules=rules;
//		return generateModelParameters;
//	}
	
	@RequestMapping(value="/simulateAllModels",method = RequestMethod.POST)
	@ResponseBody
	public List<Scene> simulateAllModels(@RequestBody GenerateModelParameters generateModelParameters,String initModelName) throws DocumentException, IOException {

		SceneService sceneService=new SceneService();
		List<Scene> scenes=new ArrayList<Scene>();
		int simulationDataNum=generateModelParameters.simulationDataNum;
		ScenesTree scenesTree=generateModelParameters.scenesTree;
		scenes=sceneService.getAllSimulationDataTimeValue(scenesTree, AddressService.MODEL_FILE_PATH, initModelName, AddressService.UPPAAL_PATH, simulationDataNum);
		return scenes;
	}
	
	@RequestMapping(value="/simulateModels",method = RequestMethod.POST)
	@ResponseBody
	public GenerateModelParametersAndScene simulateModel(@RequestBody GenerateModelParameters generateModelParameters,String sceneName,String initModelName) throws DocumentException, IOException {
		////////////////////TODO 同时返回Actions	
		SceneService sceneService=new SceneService();
		GenerateModelParametersAndScene generateModelParametersAndScene=new GenerateModelParametersAndScene();
		
		
		int simulationDataNum=generateModelParameters.simulationDataNum;
		Scene scene=sceneService.getSimulationDataTimeValue(sceneName, AddressService.MODEL_FILE_PATH, initModelName, AddressService.UPPAAL_PATH, simulationDataNum);
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
		scene=sceneService.getDeviceAnalysisResult(scene, rules, simulationTime,AddressService.MODEL_FILE_PATH, initModelName, equivalentTime, intervalTime);

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
			scene=sceneService.getDeviceAnalysisResult(scene, rules, simulationTime,AddressService.MODEL_FILE_PATH, initModelName, equivalentTime, intervalTime);
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
		
		graphNodes=toNode.getGraphNodes(AddressService.MODEL_FILE_PATH, initModelName);
		stateAndRuleAndCauseRules=sceneService.getDeviceConflictCauseRules(conflictStateTime, triggeredRulesName, deviceStateName, rules, graphNodes);
		return stateAndRuleAndCauseRules;
	}
	
	@RequestMapping(value="/getAllConflictCauseAnalysisResult",method = RequestMethod.POST)
	@ResponseBody
	public List<List<StateAndRuleAndCauseRule>> getAllConflictCauseAnalysisResult(@RequestBody AllCauseRuleInput allCauseRuleInput,String initModelName) throws DocumentException, IOException {
			

		
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
		graphNodes=toNode.getGraphNodes(AddressService.MODEL_FILE_PATH, initModelName);
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
		List<String> properties=new ArrayList<String>();
		AllRuleAnalysisResult allRuleAnalysisResult=new AllRuleAnalysisResult();
		scenes=allScenesAnalysisInput.scenes;
		rules=allScenesAnalysisInput.rules;
		properties= allScenesAnalysisInput.properties;
		System.out.println(" initFileName:"+initFileName+" simulationTime:"+simulationTime+" equivalentTime:"+equivalentTime+" intervalTime:"+intervalTime);
		
		allRuleAnalysisResult=ruleAnalysisService.getAllRuleAnalysis(scenes, rules,properties, AddressService.MODEL_FILE_PATH, initFileName, simulationTime, equivalentTime, intervalTime);
		
		
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
		graphNodes=toNode.getGraphNodes(AddressService.MODEL_FILE_PATH, initFileName);
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
