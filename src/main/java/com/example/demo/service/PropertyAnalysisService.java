package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.example.demo.bean.DataFunction;
import com.example.demo.bean.DataTimeValue;
import com.example.demo.bean.DeviceAnalysResult;
import com.example.demo.bean.NameDataFunction;
import com.example.demo.bean.PropertyAnalysis;
import com.example.demo.bean.Scene;
import com.example.demo.bean.StateNameRelativeRule;

public class PropertyAnalysisService {
	public static void main(String[] args) {
		PropertyAnalysisService propertyAnalysisService=new PropertyAnalysisService();
		List<double[]> timeList=new ArrayList<double[]>();
		List<double[]> newTimeList=new ArrayList<double[]>();
		double[] time0= {10.2,22.5};
		double[] time1= {50.3,90.5};
		double[] time2= {100,150.6};
		double[] time3= {180,280.3};
		double[] time4= {5,8.0};
		double[] time5= {15,60};
		double[] time6= {110,140};
		timeList.add(time0);
		timeList.add(time1);
		timeList.add(time2);
		timeList.add(time3);
		newTimeList.add(time4);
		newTimeList.add(time5);
		newTimeList.add(time6);
		List<double[]> finalTimeList=propertyAnalysisService.getIntersaction(timeList, newTimeList);
		System.out.println(finalTimeList);
		
		
		
	}

	public PropertyAnalysis analyzeProperty(String property,List<Scene> scenes) {
		PropertyAnalysis propertyAnalysis=new PropertyAnalysis();
		propertyAnalysis.property=property;
		
		List<String> proConditions=Arrays.asList(property.split("&"));
		List<SceneTimeList> reachableSceneTimeList=new ArrayList<PropertyAnalysisService.SceneTimeList>();
		getReachableSceneTime(proConditions, 0, reachableSceneTimeList, scenes);
		if(reachableSceneTimeList.size()==0||reachableSceneTimeList==null) {
			propertyAnalysis.reachable=false;
		}else {
			propertyAnalysis.reachable=true;
			List<String> reachableScenes=new ArrayList<String>();
			for(SceneTimeList sceneTimeList:reachableSceneTimeList) {
				reachableScenes.add(sceneTimeList.scene);
			}
			propertyAnalysis.reachableScenes=reachableScenes;
		}
		return propertyAnalysis;
		
	}
	
	public List<SceneTimeList> getReachableSceneTime(List<String> proConditions,int index,List<SceneTimeList> sceneTimeLists,List<Scene> scenes){
		if(index>=proConditions.size()||sceneTimeLists==null) {
			return sceneTimeLists;
		}else {
			String proCondition=proConditions.get(index).trim();
			if(index==0) {
				for(Scene scene:scenes) {
					SceneTimeList sceneTimeList=new SceneTimeList();
					sceneTimeList.scene=scene.getSceneName();
					List<DataTimeValue> datasTimeValue=scene.getDatasTimeValue();
					double[] time=new double[2];
					time[0]=0;
					List<double[]> timeValue=datasTimeValue.get(0).timeValue;
					time[1]=timeValue.get(timeValue.size()-1)[0];
					sceneTimeList.timeList.add(time);
					sceneTimeLists.add(sceneTimeList);
				}
			}
			boolean existCon=false;
			if(proCondition.indexOf(">")>0) {
				String attribute="";
				if(proCondition.indexOf(".")>0) {
					attribute=proCondition.substring(proCondition.indexOf("."), proCondition.indexOf(">")).substring(".".length());
				}else {
					attribute=proCondition.substring(0, proCondition.indexOf(">"));
				}
				
				double value=0;
				if(proCondition.indexOf("=")>0) {
					value=Double.parseDouble(proCondition.substring(proCondition.indexOf("=")).substring("=".length()));
				}else {
					value=Double.parseDouble(proCondition.substring(proCondition.indexOf(">")).substring("=".length()));
				}
				Iterator<SceneTimeList> sceneTimeListsIterator=sceneTimeLists.iterator();
				while(sceneTimeListsIterator.hasNext()) {
					SceneTimeList sceneTimeList=sceneTimeListsIterator.next();
					List<double[]> timeList=sceneTimeList.timeList;
					for(Scene scene:scenes) {
						if(scene.getSceneName().equals(sceneTimeList.scene)) {
							//////找到对应场景
							List<NameDataFunction> nameDataFunctions=scene.getNameDataFunctions();
							for(NameDataFunction nameDataFunction:nameDataFunctions) {
								if(nameDataFunction.name.equals(attribute)) {
									existCon=true;
									///////找到对应属性仿真值
									List<double[]> newTimeList=new ArrayList<double[]>();
									for(DataFunction dataFunction:nameDataFunction.dataFunctions) {
										if(dataFunction.upValue>value) {
											double[] time=new double[2];
											double startTime=(value-dataFunction.function.a)/dataFunction.function.b;
											if(startTime<dataFunction.downTime||
													startTime>dataFunction.upTime) {
												time[0]=dataFunction.downTime;
											}else {
												time[0]=startTime;
											}
											time[1]=dataFunction.upTime;
											newTimeList.add(time);
										}else if(dataFunction.downTime>value) {
											double[] time=new double[2];
											double startTime=(value-dataFunction.function.a)/dataFunction.function.b;
											if(startTime>dataFunction.upTime) {
												time[1]=dataFunction.upTime;
											}else {
												time[1]=startTime;
											}
											time[0]=dataFunction.downTime;
											newTimeList.add(time);
										}
									}
									/////取交集
									List<double[]> finalTimeList=getIntersaction(timeList, newTimeList);
							
									if(finalTimeList.size()==0) {
										sceneTimeListsIterator.remove();
									}else {
										sceneTimeList.timeList=finalTimeList;
									}
									
									
									break;								
									
								}
							}
							
							
							break;
						}
						
						
					}
					
				}
				
				
			}else if(proCondition.indexOf("<")>0) {
				String attribute="";
				if(proCondition.indexOf(".")>0) {
					attribute=proCondition.substring(proCondition.indexOf("."), proCondition.indexOf("<")).substring(".".length());
				}else {
					attribute=proCondition.substring(0, proCondition.indexOf("<"));
				}
				
				double value=0;
				if(proCondition.indexOf("=")>0) {
					value=Double.parseDouble(proCondition.substring(proCondition.indexOf("=")).substring("=".length()));
				}else {
					value=Double.parseDouble(proCondition.substring(proCondition.indexOf("<")).substring("=".length()));
				}
				Iterator<SceneTimeList> sceneTimeListsIterator=sceneTimeLists.iterator();
				while(sceneTimeListsIterator.hasNext()) {
					SceneTimeList sceneTimeList=sceneTimeListsIterator.next();
					List<double[]> timeList=sceneTimeList.timeList;
					for(Scene scene:scenes) {
						if(scene.getSceneName().equals(sceneTimeList.scene)) {
							//////找到对应场景
							List<NameDataFunction> nameDataFunctions=scene.getNameDataFunctions();
							for(NameDataFunction nameDataFunction:nameDataFunctions) {
								if(nameDataFunction.name.equals(attribute)) {
									///////找到对应属性仿真值
									existCon=true;
									List<double[]> newTimeList=new ArrayList<double[]>();
									for(DataFunction dataFunction:nameDataFunction.dataFunctions) {
										if(dataFunction.upValue<value) {
											double[] time=new double[2];
											double startTime=(value-dataFunction.function.a)/dataFunction.function.b;
											if(startTime<dataFunction.downTime ||
													startTime>dataFunction.upTime) {
												time[0]=dataFunction.downTime;
											}else {
												time[0]=startTime;
											}
											time[1]=dataFunction.upTime;
											newTimeList.add(time);
										}else if(dataFunction.downTime<value) {
											double[] time=new double[2];
											double startTime=(value-dataFunction.function.a)/dataFunction.function.b;
											if(startTime>dataFunction.upTime) {
												time[1]=dataFunction.upTime;
											}else {
												time[1]=startTime;
											}
											time[0]=dataFunction.downTime;
											newTimeList.add(time);
										}
									}
									/////取交集
									List<double[]> finalTimeList=getIntersaction(timeList, newTimeList);
							
									if(finalTimeList.size()==0) {
										sceneTimeListsIterator.remove();
									}else {
										sceneTimeList.timeList=finalTimeList;
									}
									
									
									break;								
									
								}
							}
							
							
							break;
						}
						
						
					}
				}
			}else if(proCondition.indexOf("=")>0) {
				String attribute="";
				if(proCondition.indexOf(".")>0) {
					attribute=proCondition.substring(proCondition.indexOf("."), proCondition.indexOf("=")).substring(".".length());
				}else {
					attribute=proCondition.substring(0, proCondition.indexOf("="));
				}
				
				int value=Integer.parseInt(proCondition.substring(proCondition.indexOf("=")).substring("=".length()));
				Iterator<SceneTimeList> sceneTimeListsIterator=sceneTimeLists.iterator();
				while(sceneTimeListsIterator.hasNext()) {
					SceneTimeList sceneTimeList=sceneTimeListsIterator.next();
					List<double[]> timeList=sceneTimeList.timeList;
					for(Scene scene:scenes) {
						if(scene.getSceneName().equals(sceneTimeList.scene)) {
							//////找到对应场景
							List<NameDataFunction> nameDataFunctions=scene.getNameDataFunctions();
							for(NameDataFunction nameDataFunction:nameDataFunctions) {
								if(nameDataFunction.name.equals(attribute)) {
									///////找到对应属性仿真值
									existCon=true;
									List<double[]> newTimeList=new ArrayList<double[]>();
									for(DataFunction dataFunction:nameDataFunction.dataFunctions) {
										if(dataFunction.upValue.intValue()==value && dataFunction.downValue.intValue()==value) {
											double[] time=new double[2];
											time[0]=dataFunction.downTime;
											time[1]=dataFunction.upTime;
											newTimeList.add(time);
										}
									}
									/////取交集
									List<double[]> finalTimeList=getIntersaction(timeList, newTimeList);
							
									if(finalTimeList.size()==0) {
										sceneTimeListsIterator.remove();
									}else {
										sceneTimeList.timeList=finalTimeList;
									}
									
									
									break;								
									
								}
							}
							
							
							break;
						}
						
						
					}
				}
			}else if(proCondition.indexOf(".")>0){
				String device=proCondition.substring(0, proCondition.indexOf("."));
				String state=proCondition.substring(proCondition.indexOf(".")).substring(".".length());
				int stateValue=0;
				List<DeviceAnalysResult> devicesAnalysResults=scenes.get(0).getDevicesAnalysResults();
				for(DeviceAnalysResult deviceAnalysResult:devicesAnalysResults) { 
					if(deviceAnalysResult.deviceName.toLowerCase().equals(device.toLowerCase())) {
						//////找到同一个设备
						for(StateNameRelativeRule stateName:deviceAnalysResult.deviceStateName.stateNames) {
							if(stateName.stateName.equals(state)) {
								///////获得该设备状态的值
								existCon=true;
								stateValue=Integer.parseInt(stateName.stateValue);
								break;
							}
						}
						break;
					}
				}
				Iterator<SceneTimeList> sceneTimeListsIterator=sceneTimeLists.iterator();
				while(sceneTimeListsIterator.hasNext()) {
					SceneTimeList sceneTimeList=sceneTimeListsIterator.next();
					List<double[]> timeList=sceneTimeList.timeList;
					for(Scene scene:scenes) {
						if(scene.getSceneName().equals(sceneTimeList.scene)) {
							//////找到对应场景
							List<NameDataFunction> nameDataFunctions=scene.getNameDataFunctions();
							for(NameDataFunction nameDataFunction:nameDataFunctions) {
								if(nameDataFunction.name.toLowerCase().equals(device.toLowerCase())) {
									///////找到对应属性仿真值
									
									List<double[]> newTimeList=new ArrayList<double[]>();
									for(DataFunction dataFunction:nameDataFunction.dataFunctions) {
										if(dataFunction.upValue.intValue()==stateValue && dataFunction.downValue.intValue()==stateValue) {
											double[] time=new double[2];
											time[0]=dataFunction.downTime;
											time[1]=dataFunction.upTime;
											newTimeList.add(time);
										}
									}
									/////取交集
									List<double[]> finalTimeList=getIntersaction(timeList, newTimeList);
							
									if(finalTimeList.size()==0) {
										sceneTimeListsIterator.remove();
									}else {
										sceneTimeList.timeList=finalTimeList;
									}
									
									
									break;								
									
								}
							}
							
							
							break;
						}
						
						
					}
				}
			}
		/////递归求解
			if(existCon) {
				return getReachableSceneTime(proConditions, index+1, sceneTimeLists, scenes);
			}else {
				return null;
			}
			
		}
	}
	
	public List<double[]> getIntersaction(List<double[]> timeList,List<double[]> newTimeList){
		List<double[]> finalTimeList=new ArrayList<double[]>();
		for(int i=0,j=0;i<timeList.size()&&j<newTimeList.size();) {
			if(timeList.get(i)[0]>=newTimeList.get(j)[1]) {
				j++;
				continue;
			}else if(timeList.get(i)[1]<=newTimeList.get(j)[0]) {
				i++;
				continue;
			}
			if(timeList.get(i)[1]<newTimeList.get(j)[1]) {
				double[] time=new double[2];
				time[0]=Math.max(timeList.get(i)[0], newTimeList.get(j)[0]);
				time[1]=timeList.get(i)[1];
				if(time[1]-time[0]>1) {
					finalTimeList.add(time);
				}
				i++;
			}else {
				double[] time=new double[2];
				time[0]=Math.max(timeList.get(i)[0], newTimeList.get(j)[0]);
				time[1]=newTimeList.get(j)[1];
				if(time[1]-time[0]>1) {
					finalTimeList.add(time);
				}
				j++;
			}
					
		}
		return finalTimeList;
	}
	
	public class SceneTimeList{
		public String scene="";
		public List<double[]> timeList=new ArrayList<double[]>();
	}
	
	
}
