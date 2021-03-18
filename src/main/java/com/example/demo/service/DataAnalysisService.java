package com.example.demo.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.bean.Action;
import com.example.demo.bean.ConflictTime;
import com.example.demo.bean.DataFunction;
import com.example.demo.bean.DataTimeValue;
import com.example.demo.bean.DeviceAnalysResult;
import com.example.demo.bean.DeviceCannotOff;
import com.example.demo.bean.DeviceConflict;
import com.example.demo.bean.DeviceStateName;
import com.example.demo.bean.DeviceStateTime;
import com.example.demo.bean.Function;
import com.example.demo.bean.NameDataFunction;
import com.example.demo.bean.Rule;
import com.example.demo.bean.RuleAndRelativeRules;
import com.example.demo.bean.Scene;
import com.example.demo.bean.SceneChild;
import com.example.demo.bean.ScenesTree;
import com.example.demo.bean.StateCauseRulesAndRelativeRules;
import com.example.demo.bean.StateChangeFast;
import com.example.demo.bean.StateLastTime;
import com.example.demo.bean.StateNameRelativeRule;
import com.example.demo.bean.StatesChange;
import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.TemplGraphNode;
import com.example.demo.bean.TemplTransition;




@Service
public class DataAnalysisService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	

	
	///////////////////////////////从csv文件中读取某仿真数据的时间值DataTimeValue//////////////////
	public DataTimeValue getDataTimeValue(String csvFilePath) throws IOException{
		DataTimeValue dataTimeValue=new DataTimeValue();
		FileReader fr=new FileReader(csvFilePath);
		BufferedReader br=new BufferedReader(fr);
		String str=br.readLine();
		while(str!=null) {
			if(str!=null && str.indexOf("# time,")>=0) {
				String dataName=str.substring(str.indexOf("# time,")).substring("# time,".length()).trim();
				dataTimeValue.name=dataName;							
			}else if(str.indexOf(",")>0){
				String splitValue[]=str.split(",");
				double[] timeValue=new double[2];
				timeValue[0]=Double.parseDouble(splitValue[0]);
				timeValue[1]=Double.parseDouble(splitValue[1]);
				dataTimeValue.timeValue.add(timeValue);
			}
			str=br.readLine();	
		}
		br.close();
		fr.close();
		return dataTimeValue;
	}
	
	//获得输出文件中的各属性时间点和值
	public List<DataTimeValue> getDataTimes(String dataFilePath) throws IOException{
		List<DataTimeValue> datas=new ArrayList<DataTimeValue>();
		
		FileReader fr=new FileReader(dataFilePath);
		BufferedReader br=new BufferedReader(fr);
		String str=br.readLine();
		while(str!=null) {
			if(str!=null && str.indexOf("#1")>0 ) {
				DataTimeValue data=new DataTimeValue();
				String name=str.substring(str.indexOf("#"), str.indexOf("#1")).substring("#".length()).replace(" ", "");
				data.name=name.trim();
				str=br.readLine();
				while(str!=null && str!="" && str.indexOf("#1")<0) {
					String splitValue[]=str.split(" ");
					double[] timeValue=new double[2];
					timeValue[0]=Double.parseDouble(splitValue[0]);
					timeValue[1]=Double.parseDouble(splitValue[1]);
					data.timeValue.add(timeValue);
					str=br.readLine();
				}
				
				datas.add(data);
			} else if(str!=null){
				str=br.readLine();
			}
		}
		br.close();
		fr.close();
		
		return datas;
	}
	
	public List<DataTimeValue> getDataTimeValues(String dataString){
		List<DataTimeValue> datas=new ArrayList<DataTimeValue>();
		List<String> dataStrs=Arrays.asList(dataString.split("\r\n"));
		String dataStr="";
		for(int i=0;i<dataStrs.size();) {
			dataStr=dataStrs.get(i);
			if(dataStr.indexOf("#1")>0) {
				DataTimeValue data=new DataTimeValue();
				String name=dataStr.substring(dataStr.indexOf("#"), dataStr.indexOf("#1")).substring("#".length()).replace(" ", "");
				data.name=name.trim();
				i++;
				for(;i<dataStrs.size();) {
					dataStr=dataStrs.get(i);
					
					if(dataStr.indexOf("#1")<0) {
						i++;
						if(dataStr=="") {
							continue;
						}
						String splitValue[]=dataStr.split(" ");
						double[] timeValue=new double[2];
						timeValue[0]=Double.parseDouble(splitValue[0]);
						timeValue[1]=Double.parseDouble(splitValue[1]);
						data.timeValue.add(timeValue);
						
					}else {
						break;
					}
				}
			}
			
		}
		return datas;
	}
	
	
	//分析数据，获得某个模型下某个数据的函数
	public NameDataFunction getNameDataFunction(DataTimeValue data){
		NameDataFunction nameDataFunction=new NameDataFunction();
		nameDataFunction.name=data.name;
		for(int i=0;i<data.timeValue.size()-1;) {
			int icount=i;
			DataFunction dataFunction=new DataFunction();
			
			Double down=data.timeValue.get(i)[0];
			dataFunction.downTime=down;
			Double up=data.timeValue.get(i)[0];
			Double downValue=data.timeValue.get(i)[1];
			Double upValue=data.timeValue.get(i)[1];
			for(int j=i+1;j<data.timeValue.size();j++) {
				Double up1=data.timeValue.get(j)[0];
				Double upValue1=data.timeValue.get(j)[1];
				
				if(Math.abs((upValue1-downValue))<0.0005) {
					//相等
					up=up1;
					upValue=upValue1;
					icount=j;
					if(j==data.timeValue.size()-1) {
						Double a=0.0;
						Double b=upValue;
						dataFunction.downTime=down;
						dataFunction.upTime=up;
						dataFunction.downValue=downValue;
						dataFunction.upValue=upValue;
						Function function=new Function();
						function.a=a;
						function.b=b;
						dataFunction.function=function;
						nameDataFunction.dataFunctions.add(dataFunction);
						i=icount;
					}
				}else {					
					if(j==i+1) {
						up=up1;
						upValue=upValue1;
						icount=j;
					}
					Double a=null;
					Double b=null;
					dataFunction.downTime=down;
					dataFunction.upTime=up;
					dataFunction.downValue=downValue;
					dataFunction.upValue=upValue;
					if((up-down)>0.1) {
						a=(upValue-downValue)/(up-down);
						b=(downValue-a*down);
					}else {
						a=downValue;
						b=upValue;
					}
					Function function=new Function();
					function.a=a;
					function.b=b;
					dataFunction.function=function;
					nameDataFunction.dataFunctions.add(dataFunction);
					i=icount;
					break;
				}
			}
		}
		return nameDataFunction;
	}
	
	public List<NameDataFunction> getSceneNameDataFunctions(List<DataTimeValue> dataTimeValues){
		List<NameDataFunction> nameDataFunctions=new ArrayList<NameDataFunction>();
		for(DataTimeValue dataTimeValue:dataTimeValues) {
			NameDataFunction nameDataFunction=getNameDataFunction(dataTimeValue);
			nameDataFunctions.add(nameDataFunction);
		}
		return nameDataFunctions;
	}
	

	

	
	//////////////////////////设备分析//////////////////////////
	public DeviceAnalysResult getSceneDeviceAnalysisResult(DataTimeValue dataTimeValue,TemplGraph controlledDevice,List<Action> actions,
			List<DataTimeValue> rulesTimeValue,String allTime,String equivalentTime,String intervalTime) {
		DeviceAnalysResult deviceAnalysResult=new DeviceAnalysResult();
		deviceAnalysResult.deviceName=dataTimeValue.name;
		NameDataFunction nameDataFunction=getNameDataFunction(dataTimeValue);
		DeviceStateName deviceStateName=getDeviceStateName(controlledDevice, actions, rulesTimeValue);
		deviceAnalysResult.deviceStateName=deviceStateName;
		DeviceConflict deviceConflict=getStateConflict(dataTimeValue, deviceStateName);
		deviceAnalysResult.statesConflict=deviceConflict;
		DeviceStateTime deviceStateTime=getStateLastTime(nameDataFunction, deviceStateName, actions, rulesTimeValue);
		deviceAnalysResult.deviceStateLastTime=deviceStateTime;
		DeviceCannotOff deviceCannotOff=cannotOff(deviceStateTime.statesTime, actions, deviceStateName);
		deviceAnalysResult.deviceCannotOff=deviceCannotOff;
		StatesChange statesChange=getStateChangeRate(nameDataFunction, allTime, equivalentTime, intervalTime);
		deviceAnalysResult.statesChange=statesChange;
		return deviceAnalysResult;
	}
	
	////////////////////////可以被触发的规则//////////////////////
	public List<DataTimeValue> getSceneTriggeredRules(List<DataTimeValue> dataTimeValues){
		List<DataTimeValue> triggeredRules=new ArrayList<DataTimeValue>();
		for(DataTimeValue dataTimeValue:dataTimeValues) {
			if(dataTimeValue.name.indexOf("rule")>=0) {
				for(double[] timeValue:dataTimeValue.timeValue) {
					if(timeValue[1]>0) {
						triggeredRules.add(dataTimeValue);
						break;
					}
				}
			}
		}
		return triggeredRules;
	}
	
	///////////////////////不能被触发的规则////////////////////////
	public List<String> getSceneCannotTriggeredRules(List<DataTimeValue> dataTimeValues){
		List<String> cannotTriggeredRules=new ArrayList<String>();
		for(DataTimeValue dataTimeValue:dataTimeValues) {
			if(dataTimeValue.name.indexOf("rule")>=0) {
				boolean canTriggered=false;
				for(double[] timeValue:dataTimeValue.timeValue) {
					if(timeValue[1]>0) {
						canTriggered=true;
						break;
					}
				}
				if(!canTriggered) {
					cannotTriggeredRules.add(dataTimeValue.name);
				}
			}
		}
		return cannotTriggeredRules;
	}
	
	/////////////////////////////////设备状态变化频率如何//////////////////////
	////////////////////////////////选择仿真过程映射的时间长度////////////////////////////////
	////////////////////////////////在一定时间内一个状态转为另一个状态算较为频繁的变化//////////////////////////
	////////////////////////////////equivalentTime以小时为单位//////////////////////////////////
	///////////////////////////////intervalTime为多少秒改变状态是不合适的/////////////////////////
	public StatesChange getStateChangeRate(NameDataFunction deviceDataFunction,String allTime,String equivalentTime,String intervalTime) {
		StatesChange deviceStatesChange=new StatesChange();
		double equiTime=Double.parseDouble(equivalentTime);
		double alTime=Double.parseDouble(allTime);
		double intervTime=Double.parseDouble(intervalTime);
		double multiple=equiTime*3600/alTime;
		int stateChangeCount=0;
		for(int i=0;i<deviceDataFunction.dataFunctions.size();i++) {
			DataFunction dataFunction=deviceDataFunction.dataFunctions.get(i);
			if(!(dataFunction.downTime+"").equals(dataFunction.upTime+"") &&Math.abs(dataFunction.downValue-dataFunction.upValue)>0.5) {
				stateChangeCount++;
			}
			
		}
		deviceStatesChange.statesChangeCount=stateChangeCount;
		deviceStatesChange.statesChangeFrequence=stateChangeCount/equiTime;
		for(int i=0;i<deviceDataFunction.dataFunctions.size();) {
			DataFunction dataFunction=deviceDataFunction.dataFunctions.get(i);
			if(Math.abs(dataFunction.downValue-dataFunction.upValue)>0.5 &&
					(dataFunction.upTime-dataFunction.downTime)>0) {
				////状态变化///////////////////////////
				/////寻找下一次状态变化在什么时候/////////////
				boolean hasStateChangeNext=false;
				for(int j=i+1;j<deviceDataFunction.dataFunctions.size();j++) {
					DataFunction nextDataFunction=deviceDataFunction.dataFunctions.get(j);
					DataFunction beforeDataFunction=deviceDataFunction.dataFunctions.get(j-1);
					if((beforeDataFunction.downTime+"").equals(beforeDataFunction.upTime+"") && 
							Math.abs(beforeDataFunction.downValue-beforeDataFunction.upValue)>0.5) {
						//////表明上一个有冲突，跳过
						i=j;
						continue;
					}
					if(Math.abs(nextDataFunction.downValue-nextDataFunction.upValue)>0.5 &&
							(nextDataFunction.upTime-nextDataFunction.downTime)>0) {
						double startTime=dataFunction.upTime;
						double endTime=nextDataFunction.downTime;
						if((endTime-startTime)*multiple<=intervTime) {
							StateChangeFast stateChangeFast=new StateChangeFast();
							stateChangeFast.startTimeValue[0]=dataFunction.downTime;
							stateChangeFast.startTimeValue[1]=dataFunction.downValue;
							stateChangeFast.middleTimeValue[0]=dataFunction.upTime;
							stateChangeFast.middleTimeValue[1]=dataFunction.upValue;
							stateChangeFast.endTimeValue[0]=nextDataFunction.upTime;
							stateChangeFast.endTimeValue[1]=nextDataFunction.upValue;
							deviceStatesChange.stateChangeFasts.add(stateChangeFast);
						}
						hasStateChangeNext=true;
						i=j;
						break;
					}
					
				}
				if(!hasStateChangeNext) {
					i++;
				}
			}else {
				i++;
			}
			
		}
		
		
		return deviceStatesChange;
	}
	
	/////////////////////////////////设备状态以及持续时间////////////////////
	public DeviceStateTime getStateLastTime(NameDataFunction deviceDataFunction,DeviceStateName deviceStateName,List<Action> actions,List<DataTimeValue> triggeredRules){
		////获得状态持续时间
		DeviceStateTime deviceStateTime=new DeviceStateTime();
		deviceStateTime.name=deviceDataFunction.name;
		List<StateLastTime> statesTime=new ArrayList<StateLastTime>();
		for(DataFunction dataFunction:deviceDataFunction.dataFunctions) {
			if((dataFunction.upTime-dataFunction.downTime)>0.1) {
				int stateValue=dataFunction.downValue.intValue();
				String state=String.valueOf(stateValue);
				double time=dataFunction.upTime-dataFunction.downTime;
				boolean exist=false;
				for(StateLastTime stateTime:statesTime) {
					if(dataFunction.downValue==Double.parseDouble(stateTime.state)) {
						stateTime.lastTime=stateTime.lastTime+time;
						exist=true;
						break;
					}
				}
				if(!exist) {
					StateLastTime stateTime=new StateLastTime();
					stateTime.state=state;
					stateTime.lastTime=time;
					stateTime.stateName=getStateName(deviceStateName, state);
					
					statesTime.add(stateTime);
				}
				
			}
		}
		deviceStateTime.statesTime=statesTime;
		
		
		return deviceStateTime;
	}
	
	/////////////////////////////设备能不能关////////////////////////
	/////////////////////////////如果没有是因为没有相应规则呢/////////////////////
	////////////////////////////还是因为相应规则无法触发呢//////////////////////////
	

	
	////////////////////////////////////new 修改了DeivceCannotOff////////////////////////////////////
	public DeviceCannotOff cannotOff(List<StateLastTime> statesTime,List<Action> actions,DeviceStateName deviceStateName) {
		DeviceCannotOff deviceCannotOff=new DeviceCannotOff();
		if(statesTime.size()>1) {
			for(StateLastTime stateTime:statesTime) {
				for(StateNameRelativeRule stateName:deviceStateName.stateNames)
					if(stateTime.stateName.equals(stateName.stateName)) {
						if(stateName.relativeRules.size()==0) {
							//设备无法关闭
							deviceCannotOff.cannotOff=true;
							boolean existRule=false;
							String rulestr="";
							List<Rule> cannotTriggeredRules=new ArrayList<Rule>();
							for(Action action:actions) {
								if(action.toState.equals(stateTime.stateName)) {
									existRule=true;
									for(Rule rule:action.rules) {
										rulestr=rulestr+rule.getRuleName()+" ";
										cannotTriggeredRules.add(rule);
									}
									break;
								}
							}
							if(!existRule) {
								deviceCannotOff.cannotOffReason.reason="No rules to turn off the device.";
								//是因为没有相应规则
							}else {
								deviceCannotOff.cannotOffReason.reason=rulestr+"cannot be triggered in this scene.";
								deviceCannotOff.cannotOffReason.cannotTriggeredRules=cannotTriggeredRules;
								//是因为相应规则无法触发
							}
						}
					}
				
			}
		}
		return deviceCannotOff;
		
	}
	
	
	
///////////////////////设备状态是否存在冲突///会找到同一时刻冲突的所有状态/////////////
	public DeviceConflict getStateConflict(DataTimeValue deviceTimeValue,DeviceStateName deviceStateName) {
		DeviceConflict deviceConflict=new DeviceConflict();
		deviceConflict.name=deviceStateName.deviceName;
		for(int i=0;i<deviceTimeValue.timeValue.size();) {
			boolean existConflict=false;
			ConflictTime conflictTime=new ConflictTime();
			List<String[]> conflictStates=new ArrayList<String[]>();
			double[] timeValue1=deviceTimeValue.timeValue.get(i);
			String time1=timeValue1[0]+"";
			String value1=timeValue1[1]+"";
			
			int j=i+1;
			for(;j<deviceTimeValue.timeValue.size();) {
				double[] timeValue2=deviceTimeValue.timeValue.get(j);
				String time2=timeValue2[0]+"";
				String value2=timeValue2[1]+"";
				////往后面找time相等而value不等的
				if(time1.equals(time2) && !value1.equals(value2)) {
					existConflict=true;
					deviceConflict.hasConflict=true;
					String stateName1=getStateName(deviceStateName, value2);
					boolean exist=false;
					for(String[] stateName:conflictStates) {
						if(stateName[1].equals(stateName1)) {
							exist=true;
							break;
						}
					}
					if(!exist) {
						String[] state=new String[2];
						state[1]=stateName1;
						state[0]=value2;
						conflictStates.add(state);
					}
					value1=value2;
				}else if(!time1.equals(time2)) {
					break;
				}
				
				j++;
			}
			
			if(existConflict) {
				double[] finalTimeValue=deviceTimeValue.timeValue.get(j);
				conflictTime.conflictTime=time1;
				String[] state=new String[2];
				state[1]=getStateName(deviceStateName,finalTimeValue[1]+"");
				boolean exist=false;
				for(String[] stateName:conflictStates) {
					if(stateName[1].equals(state[1])) {
						exist=true;
						break;
					}
				}
				if(!exist) {
					state[0]=finalTimeValue[1]+"";
					conflictStates.add(state);
				}
				
				conflictTime.conflictStates=conflictStates;
				deviceConflict.conflictTimes.add(conflictTime);
			}
			i=j;
		}
		return deviceConflict;
	}
	

	
//	///////////////////////设备状态是否存在冲突////////////////
//	public DeviceConflict getDeviceConflict(NameDataFunction deviceDataFunction,DeviceStateName deviceStateName) {
//		DeviceConflict deviceConflict=new DeviceConflict();
//		boolean existConflict=false;
//		for(DataFunction dataFunction:deviceDataFunction.dataFunctions) {
//			String downTimeStr=dataFunction.downTime.toString();
//			String upTimeStr=dataFunction.upTime.toString();
//			if(downTimeStr.equals(upTimeStr)) {
//				existConflict=true;
//				deviceConflict.hasConflict=true;
//				
//				System.out.println("  conflict on"+downTimeStr);
//				ConflictTime conflictTime=new ConflictTime();
//				List<String> conflictStates=new ArrayList<String>();
//				conflictStates.add(dataFunction.downValue.toString());
//				conflictStates.add(dataFunction.upValue.toString());
//				conflictTime.conflictStates=conflictStates;
//				conflictTime.conflictTime=downTimeStr;
//				deviceConflict.conflictTimes.add(conflictTime);
//			}
//		}
//		if(existConflict) {
//			//找到对应stateName
//			if(deviceStateName.deviceName.equals(deviceConflict.name)) {
//				for(ConflictTime conflictTime:deviceConflict.conflictTimes) {
//					for(int i=0;i<conflictTime.conflictStates.size();i++) {
//						String state=conflictTime.conflictStates.get(i);
//						for(StateNameRelativeRule stateName:deviceStateName.stateNames) {
//							if(state.equals(stateName.stateValue)||state.equals(stateName.stateValue+".0")) {
//								conflictTime.conflictStates.set(i,stateName.stateName);
//								break;
//							}
//						}
//					}
//				}
//				
//			}
//		}
//		return deviceConflict;
//	}
	
	//设备状态值对应的状态以及相关的规则    2021/1/25
	public DeviceStateName getDeviceStateName(TemplGraph controlledDevice,List<Action> actions,List<DataTimeValue> triggeredRules) {
		DeviceStateName deviceStateName=new DeviceStateName();
		String name=controlledDevice.name.substring(0,1).toLowerCase()+controlledDevice.name.substring(1);
		deviceStateName.deviceName=name;
		for(TemplGraphNode stateNode:controlledDevice.templGraphNodes) {
			TemplTransition inTransition=stateNode.inTransitions.get(0);
			if(inTransition.assignment!=null) {
				String[] assignments=inTransition.assignment.split(",");
				for(String assignment:assignments) {
					assignment=assignment.trim();
					
					if(assignment.indexOf(name)>=0) {
						StateNameRelativeRule stateName=new StateNameRelativeRule();
						String stateNum=assignment.substring(assignment.indexOf("=")).substring("=".length());
						stateName.stateValue=stateNum.trim();
						stateName.stateName=stateNode.name;
						deviceStateName.stateNames.add(stateName);
						System.out.println(stateName.stateName);
						for(Action action:actions) {
							
							System.out.println("action.toState");
							System.out.println(action.toState);
							
							if(action.toState.equals(stateName.stateName)) {						
								for(Rule rule:action.rules) {
									boolean canTriggered=false;
									for(DataTimeValue triggeredRule:triggeredRules) {
										if(triggeredRule.name.equals(rule.getRuleName())) {
											canTriggered=true;
											break;
										}
									}
									if(canTriggered) {
										stateName.relativeRules.add(rule);
									}
								}
							}
						}
						break;
					}
				}
			}
		}
		return deviceStateName;
	}
	
	
	public String getStateName(DeviceStateName deviceStateName,String value) {
		String stateName=null;
		for(StateNameRelativeRule stateValueName:deviceStateName.stateNames) {
			if(Double.parseDouble(stateValueName.stateValue)==Double.parseDouble(value)) {
				stateName=stateValueName.stateName;
				break;
			}
		}
		return stateName;
	}
	
	//////////////返回与这个状态相关的value、state、relative rules/////////////////////////////
	public StateNameRelativeRule getStateNameRelativeRule(DeviceStateName deviceStateName,String value) {
		StateNameRelativeRule stateNameRelativeRule=new StateNameRelativeRule();
		for(StateNameRelativeRule stateValueName:deviceStateName.stateNames) {
			if(Double.parseDouble(stateValueName.stateValue)==Double.parseDouble(value)) {
				stateNameRelativeRule.stateName=stateValueName.stateName;
				stateNameRelativeRule.stateValue=stateValueName.stateValue;
				stateNameRelativeRule.relativeRules=stateValueName.relativeRules;
				break;
			}
		}
		return stateNameRelativeRule;
	}
	
	public StateCauseRulesAndRelativeRules getStateCauseRules(DeviceStateName deviceStateName,String value) {
		StateCauseRulesAndRelativeRules stateCauseRulesAndRelativeRules=new StateCauseRulesAndRelativeRules();
		for(StateNameRelativeRule stateValueName:deviceStateName.stateNames) {
			if(Double.parseDouble(stateValueName.stateValue)==Double.parseDouble(value)) {
				stateCauseRulesAndRelativeRules.stateName=stateValueName.stateName;
				stateCauseRulesAndRelativeRules.stateValue=stateValueName.stateValue;
				for(Rule rule:stateValueName.relativeRules) {
					RuleAndRelativeRules ruleAndRelativeRules=new RuleAndRelativeRules();
					ruleAndRelativeRules.rule=rule;
					stateCauseRulesAndRelativeRules.causeRules.add(ruleAndRelativeRules);
				}
				
			}
		}
		return stateCauseRulesAndRelativeRules;
	}
	
	/////////////////////////////////////返回直接造成该状态的rules
	public List<Rule> getStatePossibleCauseRules(DeviceStateName deviceStateName,String value){
		List<Rule> possibleCauseRules=new ArrayList<Rule>();
		for(StateNameRelativeRule stateValueName:deviceStateName.stateNames) {
			if(Double.parseDouble(stateValueName.stateValue)==Double.parseDouble(value)) {
				possibleCauseRules=stateValueName.relativeRules;
				break;
			}
		}
		
		return possibleCauseRules;
	}
	

}
