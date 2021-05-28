package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Action;
import com.example.demo.bean.Rule;
import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.TemplGraphNode;
import com.example.demo.bean.TemplTransition;
import com.example.demo.bean.Trigger;
import com.example.demo.service.GetTemplate.Template;
@Service
public class TGraphToDot {

	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		
		//String path1="D:\\window17.xml";
		String path2="D:\\win21.xml";
		
		String rulePath="D:\\rules.txt";
		String dotPath="D:\\g5.dot";
		String dotPath1="D:\\g4.dot";
		TGraphToDot tDot = new TGraphToDot();
		RuleService ruleService=new RuleService();
		List<Rule> rules=ruleService.getRuleListFromTxt(rulePath);
		
		GetTemplate parse=new GetTemplate();
		//parse.deletLine(path1, path2, 2);
		List<Template> templates=parse.getTemplate(path2);
		
		TemplGraphService tGraph=new TemplGraphService();
		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
		for(Template template:templates) {
			templGraphs.add(tGraph.getTemplGraph(template));
		}
		
		tDot.getIFD(templGraphs, rules, dotPath);
		/////////////////////////生成IFD///////////////////////////////
		//getIFD方法
		/////根据解析出的Rule类型rules画出trigger->rule->action
		/////根据环境模型（templGraphs）获得sensor/device->trigger以及action->device,
		/////还有最后action->trigger和trigger->trigger
		///////////////////////////////////////////////////////////////
		
		
		
//		//实例化接口？///////////////////////
//		String AC="ac=AirConditioner(-0.5,0.5)"; 
//		String Ah="ah=AirHumidifier(0.5)";
//		String Af="af=AirFreshener(-0.5)";
//		String Bulb="bulb=Bulb(800.0)";
//		String Rain="rain=RainInstance0(8,2,65.0)";
//		////////////                                                                                     
//		tDot.getInstance(AC, templGraphs);
//		tDot.getInstance(Ah, templGraphs);
//		tDot.getInstance(Af, templGraphs);
//		tDot.getInstance(Bulb, templGraphs);
//		tDot.getInstance(Rain, templGraphs);
		
		
		tDot.getDot(templGraphs, rules, dotPath1);
		
		

	}
	
	////////////////////////////////////////////2020.12.22以前/////////////////////////////////
	public void getIFDDot(List<TemplGraph> templGraphs,List<Rule> rules,String dotPath) throws IOException {
		GetTemplate parse=new GetTemplate();
		TGraphToDot tDot = new TGraphToDot();
		TemplGraphService tGraph=new TemplGraphService();
		List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
		List<TemplGraph> sensors=new ArrayList<TemplGraph>();
		List<TemplGraph> biddables=new ArrayList<TemplGraph>();
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration!=null) {
				if(templGraph.declaration.indexOf("controlled_device")>=0) {
					controlledDevices.add(templGraph);
				}
				if(templGraph.declaration.indexOf("sensor")>=0) {
					sensors.add(templGraph);
				}				
			}
			if(templGraph.declaration.indexOf("biddable")>=0) {
				biddables.add(templGraph);
			}
		}
		//-------------------写dot文件-----------------------------------
		
		File graphvizFile=new File(dotPath);
		if(!graphvizFile.exists()) {
			try {
				graphvizFile.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		parse.write(graphvizFile, "", false);
		parse.write(graphvizFile, "digraph infoflow{", true);
		parse.write(graphvizFile, "rankdir=LR;", true);
		parse.write(graphvizFile,  "", true);
		///////////////////sensor//////////////////
		parse.write(graphvizFile, "///////////////sensors////////////////", true);
		for(TemplGraph sensor:sensors) {
			String sensorDot=sensor.name+"[shape=\"doubleoctagon\",style=\"filled\",fillcolor=\"azure3\"]";
			parse.write(graphvizFile, sensorDot, true);
		}
		///////////////////////////////////////////////////
		parse.write(graphvizFile, "", true);
		
		//////////////controlled devices////////////////
		parse.write(graphvizFile, "//////////////controlled devices//////////////", true);
		for(TemplGraph controlledDevice:controlledDevices) {
			//device
			String deviceDot=controlledDevice.name+"[shape=\"doubleoctagon\",style=\"filled\",fillcolor=\"darkseagreen1\"]";
			parse.write(graphvizFile, deviceDot, true);			
			List<String> actionList=new ArrayList<String>();
			for(TemplGraphNode state:controlledDevice.templGraphNodes) {
				int flag=0;
				for(TemplTransition outTransition:state.outTransitions) {
					String synchronisation=outTransition.synchronisation;
					if(synchronisation!=null && synchronisation.indexOf("?")>0) {
						String action=synchronisation.substring(0, synchronisation.indexOf("?"));
						for(String act:actionList) {
							if(synchronisation.indexOf(act)>=0) {
								flag=1;
							}
						}
						if(flag==0) {
							//action
							String actionDot=action+"[shape=\"record\",style=\"filled\",fillcolor=\"beige\"]";
							parse.write(graphvizFile, actionDot, true);
							//actionToDevice
							String actionToDeviceDot=action+"->"+controlledDevice.name+"[color=\"lemonchiffon3\"]";
							parse.write(graphvizFile, actionToDeviceDot, true);
							actionList.add(action);
						}
					}
				}
				
			}
			
			
			//actionToDevice
		}
		//////////////////////////////////////////////////////
		parse.write(graphvizFile, "", true);
		parse.write(graphvizFile, "", true);
		
		/////////////////////rules////////////////////
		parse.write(graphvizFile, "////////////////////rules/////////////////////", true);
		parse.write(graphvizFile, "", true);
		for(Rule rule:rules) {
			parse.write(graphvizFile, "///////////////////"+rule.getRuleName()+"////////////////////", true);
			//ruleNum节点
			String ruleDot=rule.getRuleName()+"[shape=\"hexagon\",style=\"filled\",fillcolor=\"lightskyblue\"]";
			parse.write(graphvizFile, ruleDot, true);
			//ruleToAction
			for(String action:rule.getAction()) {
				if(action.indexOf("for")>0) {
					action=action.substring(0, action.indexOf("for")).trim();
				}
				String ruleToActionDot=rule.getRuleName()+"->"+action;
				parse.write(graphvizFile, ruleToActionDot, true);
			}
			
			//trigger
			for(int i=0;i<rule.getTrigger().size();i++) {
				String trigger=rule.getTrigger().get(i);
				//trigger节点
				String triggerNum=rule.getRuleName()+"trigger"+i;
				String triggerDot=triggerNum+"[label=\""+rule.getTrigger()+"\",shape=\"oval\",style=\"filled\",fillcolor=\"lightpink\"]";
				parse.write(graphvizFile, triggerDot, true);
				//triggerToRule
				String triggerToRuleDot=triggerNum+"->"+rule.getRuleName();
				parse.write(graphvizFile, triggerToRuleDot, true);
				//sensorToTrigger
				//先找到trigger对应attribute
				if(trigger.indexOf(">")>0) {
					String attribute=null;
					String valStr=null;
					//attribute
					if(trigger.indexOf(".")>0) {
						attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf(">")).substring(".".length());
					}else {
						attribute=trigger.substring(0, trigger.indexOf(">"));
					}
					//valstr
					if(trigger.indexOf("=")>0) {
						valStr=trigger.substring(trigger.indexOf("=")).substring("=".length());
					}else {
						valStr=trigger.substring(trigger.indexOf(">")).substring(">".length());
					}
					
					//sensorToTrigger
					for(TemplGraph sensor:sensors) {
						if(sensor.declaration.indexOf(attribute)>=0) {
							String sensorToTriggerDot=sensor.name+"->"+triggerNum+"[color=\"lightpink\"]";
							parse.write(graphvizFile, sensorToTriggerDot, true);
							break;
						}
					}
					
					//其他设备状态对trigger的影响
					
					
				}else if(trigger.indexOf("<")>0) {
					String attribute=null;
					String valStr=null;
					//attribute
					if(trigger.indexOf(".")>0) {
						attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf("<")).substring(".".length());
					}else {
						attribute=trigger.substring(0, trigger.indexOf("<"));
					}
					//valstr
					if(trigger.indexOf("=")>0) {
						valStr=trigger.substring(trigger.indexOf("=")).substring("=".length());
					}else {
						valStr=trigger.substring(trigger.indexOf("<")).substring("<".length());
					}
					
					//sensorToTrigger
					for(TemplGraph sensor:sensors) {
						if(sensor.declaration.indexOf(attribute)>=0) {
							String sensorToTriggerDot=sensor.name+"->"+triggerNum+"[color=\"lightpink\"]";
							parse.write(graphvizFile, sensorToTriggerDot, true);
							break;
						}
					}
					//其他设备状态对trigger的影响
					
					
				}else if(trigger.indexOf("=")>0) {
					String attribute=null;
					//找到rule的trigger涉及的属性
					if(trigger.indexOf(".")>=0) {
						attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf("=")).substring(".".length());
					}else {
						attribute=trigger.substring(0, trigger.indexOf("="));
					}
					//sensorToTrigger
					for(TemplGraph sensor:sensors) {
						if(sensor.declaration.indexOf(attribute)>=0) {
							String sensorToTriggerDot=sensor.name+"->"+triggerNum+"[color=\"lightpink\"]";
							parse.write(graphvizFile, sensorToTriggerDot, true);
							break;
						}
					}
				}else {
					//trigger为设备状态
					String stateTrigger=trigger;
					String device=stateTrigger.substring(0, stateTrigger.indexOf("."));
					String state=stateTrigger.substring(stateTrigger.indexOf(".")).substring(".".length());
					//deviceToTrigger
					String deviceToTriggerDot=device+"->"+triggerNum+"[color=\"lightpink\"]";
					parse.write(graphvizFile, deviceToTriggerDot, true);
					//actionToTrigger, trigger为state
					for(TemplGraph contrDevice:controlledDevices) {
						if(device.equals(contrDevice.name)) {
							for(TemplGraphNode sta:contrDevice.templGraphNodes) {
								if(state.equals(sta.name)) {									
									for(TemplTransition inTransition:sta.inTransitions) {										
										if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
											String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
											String actionToStateDot=action+"->"+triggerNum+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
											parse.write(graphvizFile, actionToStateDot, true);
											break;
										}
										
									}
									break;
									
								}
							}
							break;
						}
					}
				}
				parse.write(graphvizFile, "", true);
			}
			parse.write(graphvizFile, "", true);
		}
		parse.write(graphvizFile, "", true);
		parse.write(graphvizFile, "}", true);
	}
	////////////////////////////////////////////2020.12.22以前/////////////////////////////////
	public void getDeviceEffect(File graphvizFile,String trigger,String triggerNum,String attribute,String compareStr,List<TemplGraph> controlledDevices) throws IOException {
		GetTemplate parse=new GetTemplate();
		String valStr=null;
		//valstr
		if(trigger.indexOf("=")>0) {
			valStr=trigger.substring(trigger.indexOf("=")).substring("=".length());
		}else {
			valStr=trigger.substring(trigger.indexOf(compareStr)).substring(compareStr.length());
		}
		////////////////////effect//////////////////////
		//对该trigger有正影响的device的action
		for(TemplGraph device:controlledDevices) {
			for(TemplGraphNode state:device.templGraphNodes) {
				if(state.invariant!=null && state.invariant.indexOf(attribute)>0) {
					String[] invariants=state.invariant.split("&&");
					String effectValueStr=null;
					for(String invariant:invariants) {
						if(invariant.indexOf(attribute)>=0) {
							effectValueStr=invariant.substring(invariant.indexOf("==")).substring("==".length());
							break;
						}
					}
					Double effectValue=Double.parseDouble(effectValueStr);
					if(effectValue>0) {
						
						//actionToTrigger
						for(TemplTransition inTransition:state.inTransitions) {
							if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
								if(compareStr.equals(">")) {
									//正影响
									String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
									String actionToTriggerDot=action+"->"+triggerNum+"[style=\"dashed\",color=\"red\",label=\"+\",fontsize=\"18\"]";
									parse.write(graphvizFile, actionToTriggerDot, true);
								}
								if(compareStr.equals("<")) {
									//负影响
									String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
									String actionToTriggerDot=action+"->"+triggerNum+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
									parse.write(graphvizFile, actionToTriggerDot, true);
								}
								
								break;
							}
						}
					}
					if(effectValue<0) {
						
						for(TemplTransition inTransition:state.inTransitions) {
							if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
								if(compareStr.equals("<")) {
									//正影响
									String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
									String actionToTriggerDot=action+"->"+triggerNum+"[style=\"dashed\",color=\"red\",label=\"+\",fontsize=\"18\"]";
									parse.write(graphvizFile, actionToTriggerDot, true);
								}
								if(compareStr.equals(">")) {
									//负影响
									String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
									String actionToTriggerDot=action+"->"+triggerNum+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
									parse.write(graphvizFile, actionToTriggerDot, true);
								}
							}
						}
					}
					
				}
				if(state.invariant==null) {
					for(TemplTransition inTransition:state.inTransitions) {
						if(inTransition.assignment!=null && inTransition.assignment.indexOf(attribute)>=0) {
							String[] assignments=inTransition.assignment.split(",");
							for(String assignment:assignments) {
								if(assignment.indexOf(attribute)>=0&&assignment.indexOf("temp")<0) {
									String effectValueStr=assignment.substring(assignment.indexOf("=")).substring("=".length());
									Double effectValue=Double.parseDouble(effectValueStr);
									Double value=Double.parseDouble(valStr);
									if(compareStr.equals(">")) {
										if(effectValue>value) {
											//正影响
											if(inTransition.synchronisation!=null&&inTransition.synchronisation.indexOf("?")>0) {
												String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
												String actionToTriggerDot=action+"->"+triggerNum+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
												parse.write(graphvizFile, actionToTriggerDot, true);
												break;
											}
										}
										if(effectValue<value) {
											//负影响
											if(inTransition.synchronisation!=null&&inTransition.synchronisation.indexOf("?")>0) {
												String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
												String actionToTriggerDot=action+"->"+triggerNum+"[color=\"red\",label=\"-\",fontsize=\"24\"]";
												parse.write(graphvizFile, actionToTriggerDot, true);
												break;
											}
										}
									}
									if(compareStr.equals("<")) {
										if(effectValue<value) {
											//正影响
											if(inTransition.synchronisation!=null&&inTransition.synchronisation.indexOf("?")>0) {
												String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
												String actionToTriggerDot=action+"->"+triggerNum+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
												parse.write(graphvizFile, actionToTriggerDot, true);
												break;
											}
										}
										if(effectValue>value) {
											//负影响
											if(inTransition.synchronisation!=null&&inTransition.synchronisation.indexOf("?")>0) {
												String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
												String actionToTriggerDot=action+"->"+triggerNum+"[color=\"red\",label=\"-\",fontsize=\"24\"]";
												parse.write(graphvizFile, actionToTriggerDot, true);
												break;
											}
										}
									}
									
									break;
								}
							}
							
							break;
						}
					}
				}
			}
		}
	}
	/////////////////////////////////////////////////2020.12.19/////////////////////////////////////////
	public void getBiddableEffect(File graphvizFile,String trigger,String triggerNum,String attribute,String compareStr,List<TemplGraph> biddables,List<Rule> rules) throws IOException {
		GetTemplate parse=new GetTemplate();
		String valStr=null;
		Double value=Double.parseDouble(valStr);
		//valstr
		if(trigger.indexOf("=")>0) {
			valStr=trigger.substring(trigger.indexOf("=")).substring("=".length());
		}else {
			valStr=trigger.substring(trigger.indexOf(compareStr)).substring(compareStr.length());
		}
		for(TemplGraph biddable:biddables) {
			for(TemplGraphNode state:biddable.templGraphNodes) {
				if(state.invariant!=null && state.invariant.indexOf(attribute)>=0) {
					String[] invariants=state.invariant.split("&&");
					String effectValueStr=null;
					for(String invariant:invariants) {
						invariant=invariant.trim();
						if(invariant.indexOf(attribute)>=0) {
							effectValueStr=invariant.substring(invariant.indexOf("==")).substring("==".length());
							break;
						}
						Double effectValue=Double.parseDouble(effectValueStr);
						if(effectValue>0) {
							
							//actionToTrigger
							for(TemplTransition inTransition:state.inTransitions) {
								if(inTransition.assignment!=null ) {
									/////////////////////////////////////////////////
									
									break;
								}
							}
						}
					}
				}
			}
		}
		if(compareStr.equals(">")) {
			
		}
		if(compareStr.equals("<")) {
			
		}
	}
	
	
	////////////////////////////////////////////////2020.12.20（新）/////////////////////////////////////////////////////////
	public void getBiddableEffect(File graphvizFile,Trigger trigger,List<Trigger> triggers,List<TemplGraph> biddables) throws IOException {
		if(!trigger.attrVal[1].equals(".") && !trigger.attrVal[1].equals("=")) {
			GetTemplate parse=new GetTemplate();
			String attribute=trigger.attrVal[0];
			String compareStr=trigger.attrVal[1];
			for(TemplGraph biddable :biddables) {
				if(biddable.name.indexOf("Person")<0) {
					for(TemplGraphNode stateNode:biddable.templGraphNodes) {
						if(stateNode.invariant!=null && stateNode.invariant.indexOf(attribute)>=0) {
							String[] invariants=stateNode.invariant.split("&&");
							for(String invariant:invariants) {
								if(invariant.indexOf(attribute)>=0) {
									invariant=invariant.trim();
									String effectValStr=invariant.substring(invariant.indexOf("==")).substring("==".length());
									Double effectVal=Double.parseDouble(effectValStr);
									if(compareStr.equals(">")) {
										if(effectVal>0) {
											//正影响
											for(TemplTransition inTransition:stateNode.inTransitions) {
												if(inTransition.assignment!=null) {
													for(Trigger otherTrigger:triggers) {
														if(otherTrigger.attrVal[1].equals("=")&&otherTrigger.attrVal[0].indexOf("number")<0) {
															if(inTransition.assignment.indexOf(otherTrigger.trigger)>=0) {
																String[] assignments=inTransition.assignment.split(",");
																for(String assignment:assignments) {
																	assignment=assignment.trim();
																	if(assignment.equals(otherTrigger.trigger)) {
																		String biddableTriggerToTriggerDot=otherTrigger.triggerNum+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",fontsize=\"18\"]";
																		parse.write(graphvizFile, biddableTriggerToTriggerDot, true);
																	}
																}
																break;
															}
														}
													}
													break;
												}
											}
										}
//										if(effectVal<0) {
//											//负影响
//											for(TemplTransition inTransition:stateNode.inTransitions) {
//												if(inTransition.assignment!=null) {
//													for(Trigger otherTrigger:triggers) {
//														if(otherTrigger.attrVal[1].equals("=")&&otherTrigger.attrVal[0].indexOf("number")<0) {
//															if(inTransition.assignment.indexOf(otherTrigger.trigger)>=0) {
//																String[] assignments=inTransition.assignment.split(",");
//																for(String assignment:assignments) {
//																	assignment=assignment.trim();
//																	if(assignment.equals(otherTrigger.trigger)) {
//																		String biddableTriggerToTriggerDot=otherTrigger.triggerNum+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
//																		parse.write(graphvizFile, biddableTriggerToTriggerDot, true);
//																	}
//																}
//																break;
//															}
//														}
//													}
//													break;
//												}
//											}
//										}
									}
								}
							}
						}else {
							for(TemplTransition inTransition:stateNode.inTransitions) {
								if(inTransition.assignment!=null && inTransition.assignment.indexOf(attribute)>=0) {
									String[] assignments=inTransition.assignment.split(",");
									for(String assignment:assignments) {
										assignment=assignment.trim();
										if(assignment.indexOf(attribute)>=0) {
											String effectValStr=assignment.substring(assignment.indexOf("=")).substring("=".length());
											Double effectVal=Double.parseDouble(effectValStr);
											Double value=Double.parseDouble(trigger.attrVal[2]);
											if(compareStr.equals(">")) {
												if(effectVal>value) {
													//正影响
													for(Trigger otherTrigger:triggers) {
														if(otherTrigger.attrVal[1].equals("=") && otherTrigger.attrVal[0].indexOf("number")<0) {
															for(String assign:assignments) {
																if(assign.equals(otherTrigger.trigger)) {
																	String biddableTriggerToTriggerDot=otherTrigger.triggerNum+"->"+trigger.triggerNum+"[color=\"red\",fontsize=\"18\"]";
																	parse.write(graphvizFile, biddableTriggerToTriggerDot, true);
																	break;
																}
															}
														}
													}
													break;
												}
												if(effectVal<value) {
													//负影响
													for(Trigger otherTrigger:triggers) {
														if(otherTrigger.attrVal[1].equals("=") && otherTrigger.attrVal[0].indexOf("number")<0) {
															for(String assign:assignments) {
																if(assign.equals(otherTrigger.trigger)) {
																	String biddableTriggerToTriggerDot=otherTrigger.triggerNum+"->"+trigger.triggerNum+"[color=\"red\",label=\"-\",fontsize=\"24\"]";
																	parse.write(graphvizFile, biddableTriggerToTriggerDot, true);
																	break;
																}
															}
														}
													}
													break;
												}
											}
											if(compareStr.equals("<")) {
												if(effectVal<value) {
													//正影响
													for(Trigger otherTrigger:triggers) {
														if(otherTrigger.attrVal[1].equals("=") && otherTrigger.attrVal[0].indexOf("number")<0) {
															for(String assign:assignments) {
																if(assign.equals(otherTrigger.trigger)) {
																	String biddableTriggerToTriggerDot=otherTrigger.triggerNum+"->"+trigger.triggerNum+"[color=\"red\",fontsize=\"18\"]";
																	parse.write(graphvizFile, biddableTriggerToTriggerDot, true);
																	break;
																}
															}
														}
													}
													break;
												}
												if(effectVal>value) {
													//负影响
													for(Trigger otherTrigger:triggers) {
														if(otherTrigger.attrVal[1].equals("=") && otherTrigger.attrVal[0].indexOf("number")<0) {
															for(String assign:assignments) {
																if(assign.equals(otherTrigger.trigger)) {
																	String biddableTriggerToTriggerDot=otherTrigger.triggerNum+"->"+trigger.triggerNum+"[color=\"red\",label=\"-\",fontsize=\"24\"]";
																	parse.write(graphvizFile, biddableTriggerToTriggerDot, true);
																	break;
																}
															}
														}
													}
													break;
												}
											}
											
										}
									}
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	

	
	

	
	///////////////////////////////////2020.12.20////////////////////////////////////
	//获得所有提到的condition节点
	public List<Trigger> getTriggers(List<Rule> rules){
		List<Trigger> triggers=new ArrayList<Trigger>();
		int count=0;
		for(Rule rule:rules) {
			for(String triggerStr:rule.getTrigger()) {
				triggerStr=triggerStr.trim();
				int flag=0;
				for(Trigger trigger:triggers) {
					if(triggerStr.equals(trigger.trigger)) {
						flag=1;
						trigger.rules.add(rule);
						break;
					}
				}
				if(flag==0) {
					Trigger trigger=new Trigger();
					trigger.trigger=triggerStr.trim();
					trigger.triggerNum="trigger"+count;
					trigger.rules.add(rule);
					//trigger对应的device
					trigger.attrVal=getAttrVal(trigger.trigger);
					triggers.add(trigger);
					count++;
				}
			}
		}
		return triggers;
	}
	
	
    ///////////////////////////////////2020.12.20////////////////////////////////////
	//解析trigger
	public String[] getAttrVal(String trigger){
		String[] attrVal=new String[3];
		if(trigger.indexOf("FOR")>0) {
			trigger=trigger.substring(0, trigger.indexOf("FOR"));
		}
		trigger=trigger.trim();
		if(trigger.indexOf(">")>0) {
			String attribute=null;
			String valStr=null;
			if(trigger.indexOf(".")>=0) {
				attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf(">")).substring(".".length());
			}else {
				attribute=trigger.substring(0, trigger.indexOf(">"));
			}
			//找到阈值
			if(trigger.indexOf("=")>0) {
				valStr=trigger.substring(trigger.indexOf("=")).substring("=".length());				
			}
			if(trigger.indexOf("=")<0) {
				valStr=trigger.substring(trigger.indexOf(">")).substring(">".length());
			}
			attrVal[0]=attribute.trim();
			attrVal[1]=">";
			attrVal[2]=valStr.trim();
		}else if(trigger.indexOf("<")>0) {
			String attribute=null;
			String valStr=null;
			if(trigger.indexOf(".")>=0) {
				attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf("<")).substring(".".length());
			}else {
				attribute=trigger.substring(0, trigger.indexOf("<"));
			}
			//找到阈值
			if(trigger.indexOf("=")>0) {
				valStr=trigger.substring(trigger.indexOf("=")).substring("=".length());
				
			}
			if(trigger.indexOf("=")<0) {
				valStr=trigger.substring(trigger.indexOf("<")).substring("<".length());
			}
			attrVal[0]=attribute.trim();
			attrVal[1]="<";
			attrVal[2]=valStr.trim();
		}else if(trigger.indexOf("=")>0) {
			String attribute=null;
			String valStr=null;
			if(trigger.indexOf(".")>=0) {
				attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf("=")).substring(".".length());
			}else {
				attribute=trigger.substring(0, trigger.indexOf("="));
			}
			valStr=trigger.substring(trigger.indexOf("=")).substring("=".length());
			attrVal[0]=attribute.trim();
			attrVal[1]="=";
			attrVal[2]=valStr.trim();
			
		}else {
			String device=null;
			String state=null;
			device=trigger.substring(0, trigger.indexOf("."));
			state=trigger.substring(trigger.indexOf(".")).substring(".".length());
			attrVal[0]=device.trim();
			attrVal[1]=".";
			attrVal[2]=state.trim();
		}
		return attrVal;
	}
	///////////////////////////////////////////2020.12.27///////////////////////
	//更改一下trigger，好像没太大必要
	public String[] getAttributeValue(String trigger) {
		String[] attrVal=new String[3];
		if(trigger.indexOf("FOR")>0) {
			trigger=trigger.substring(0, trigger.indexOf("FOR"));
		}
		trigger=trigger.trim();
		if(trigger.indexOf(">=")>0) {
			String attribute=null;
			String valStr=null;
			if(trigger.indexOf(".")>=0) {
				attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf(">")).substring(".".length());
			}else {
				attribute=trigger.substring(0, trigger.indexOf(">"));
			}
			//找到阈值
			valStr=trigger.substring(trigger.indexOf("=")).substring("=".length());
			attrVal[0]=attribute.trim();
			attrVal[1]=">=";
			attrVal[2]=valStr.trim();
		}else if(trigger.indexOf(">")>=0) {
			String attribute=null;
			String valStr=null;
			if(trigger.indexOf(".")>=0) {
				attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf(">")).substring(".".length());
			}else {
				attribute=trigger.substring(0, trigger.indexOf(">"));
			}
			//找到阈值
			valStr=trigger.substring(trigger.indexOf(">")).substring(">".length());
			attrVal[0]=attribute.trim();
			attrVal[1]=">";
			attrVal[2]=valStr.trim();
		}else if(trigger.indexOf("<=")>=0) {
			String attribute=null;
			String valStr=null;
			if(trigger.indexOf(".")>=0) {
				attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf("<")).substring(".".length());
			}else {
				attribute=trigger.substring(0, trigger.indexOf("<"));
			}
			//找到阈值
			valStr=trigger.substring(trigger.indexOf("=")).substring("=".length());
			attrVal[0]=attribute.trim();
			attrVal[1]="<=";
			attrVal[2]=valStr.trim();
		}else if(trigger.indexOf("<")>=0) {
			String attribute=null;
			String valStr=null;
			if(trigger.indexOf(".")>=0) {
				attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf("<")).substring(".".length());
			}else {
				attribute=trigger.substring(0, trigger.indexOf("<"));
			}
			//找到阈值
			valStr=trigger.substring(trigger.indexOf("<")).substring("<".length());
			attrVal[0]=attribute.trim();
			attrVal[1]="<";
			attrVal[2]=valStr.trim();
		}else if(trigger.indexOf("=")>=0) {
			String attribute=null;
			String valStr=null;
			if(trigger.indexOf(".")>=0) {
				attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf("=")).substring(".".length());
			}else {
				attribute=trigger.substring(0, trigger.indexOf("="));
			}
			valStr=trigger.substring(trigger.indexOf("=")).substring("=".length());
			attrVal[0]=attribute.trim();
			attrVal[1]="=";
			attrVal[2]=valStr.trim();
		}else {
			String device=null;
			String state=null;
			device=trigger.substring(0, trigger.indexOf("."));
			state=trigger.substring(trigger.indexOf(".")).substring(".".length());
			attrVal[0]=device.trim();
			attrVal[1]=".";
			attrVal[2]=state.trim();
		}
		return attrVal;
	}
	
	///////////////////////////////////2020.12.20////////////////////////////////////
	public List<Action> getActions(List<Rule> rules,List<TemplGraph> templGraphs){
		List<Action> actions=new ArrayList<Action>();
		for(Rule rule:rules) {
			for(String actionStr:rule.getAction()) {
				actionStr=actionStr.trim();
				if(actionStr.indexOf("for")>0) {
					actionStr=actionStr.substring(0, actionStr.indexOf("for")).trim();					
				}
				int flag=0;
				for(Action action:actions) {
					if(actionStr.equals(action.action)) {
						flag=1;
						action.rules.add(rule);
						break;
					}
				}
				if(flag==0) {
					Action action=new Action();
					action.action=actionStr.trim();
					action.rules.add(rule);
//					System.out.println("ruleName");
//					System.out.println(rule.getRuleName());
					action=getAttrVal(templGraphs,action);
					actions.add(action);
				}
			}
		}
		return actions;
	}
	//获得action影响以及对应的device
	public Action getAttrVal(List<TemplGraph> templGraphs,Action act){
		String action=act.action;
		int flag=0;
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration!=null && templGraph.declaration.indexOf("controlled_device")>=0) {
				for(TemplGraphNode stateNode:templGraph.templGraphNodes) {
					for(TemplTransition inTransition:stateNode.inTransitions) {
						if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf(action)>=0) {
							if(inTransition.synchronisation.indexOf("?")>0) {
								String synchronisation=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
								if(synchronisation.equals(action)) {
									//////////TODO///////action 
									act.device=templGraph.name;
									act.toState=stateNode.name;
									System.out.println(act.device);
									System.out.println("act.toState");
									System.out.println(act.toState);
									flag=1;
									//寻找该action对属性的影响
									if(stateNode.invariant!=null) {
										String[] invariants=stateNode.invariant.split("&&");
										for(String invariant:invariants) {
											invariant=invariant.trim();
											if(invariant.indexOf("'==")>0) {
												String[] attrVal=new String[3];
												String attribute=invariant.substring(0, invariant.indexOf("'=="));
												String kind="'";
												String value=invariant.substring(invariant.indexOf("==")).substring("==".length());
												attrVal[0]=attribute.trim();
												attrVal[1]=kind;
												attrVal[2]=value.trim();
												act.attrVal.add(attrVal);
											}
											
										}
									}
									if(inTransition.assignment!=null) {
										String[] assignments=inTransition.assignment.split(",");
										String deviceVal=templGraph.name.substring(0,1).toLowerCase()+templGraph.name.substring(1);
										for(String assignment:assignments) {
											assignment=assignment.trim();
											if(assignment.indexOf(deviceVal)>=0) {
												String val=assignment.substring(assignment.indexOf("=")).substring("=".length());
												act.value=val;
												continue;
											}
											String[] attrVal=new String[3];
											String attribute=assignment.substring(0, assignment.indexOf("="));
											String kind="=";
											String value=assignment.substring(assignment.indexOf("=")).substring("=".length());
											attrVal[0]=attribute.trim();
											attrVal[1]=kind;
											attrVal[2]=value.trim();
											act.attrVal.add(attrVal);
										}
									}
									break;
								}
							}
							
						}
					}
					if(flag==1) {
						break;
					}
				}
			}
			if(flag==1) {
				break;
			}
		}
		return act;
	}
	///////////////////////////////////////2020.12.20///////////////////////////////
	//判断trigger和action节点是不是属于同一个rule
	public boolean belongSameRule(Trigger trigger, Action action) {
		for(Rule triggerRule:trigger.rules) {
			for(Rule actionRule:action.rules) {
				if(triggerRule==actionRule) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/////////////////////////////////2020.12.20之后////////////////////////////////
	public void getIFD(List<TemplGraph> templGraphs,List<Rule> rules,String dotPath) throws IOException {
		GetTemplate parse=new GetTemplate();
		List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
		List<TemplGraph> sensors=new ArrayList<TemplGraph>();
		List<TemplGraph> biddables=new ArrayList<TemplGraph>();
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration!=null) {
				if(templGraph.declaration.indexOf("controlled_device")>=0) {
					controlledDevices.add(templGraph);
				}
				if(templGraph.declaration.indexOf("sensor")>=0) {
					sensors.add(templGraph);
				}
				if(templGraph.declaration.indexOf("biddable")>=0 && templGraph.declaration.indexOf("sensor")<0) {
					biddables.add(templGraph);
				}
			}
		}
		
		//-------------------写dot文件-----------------------------------
		
		File graphvizFile=new File(dotPath);
		if(!graphvizFile.exists()) {
			try {
				graphvizFile.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		parse.write(graphvizFile, "", false);
		parse.write(graphvizFile, "digraph infoflow{", true);
		parse.write(graphvizFile, "rankdir=LR;", true);
		parse.write(graphvizFile,  "", true);
		///////////////////sensor//////////////////
		parse.write(graphvizFile, "///////////////sensors////////////////", true);
		for(TemplGraph sensor:sensors) {
			String sensorDot=sensor.name+"[shape=\"doubleoctagon\",style=\"filled\",fillcolor=\"azure3\"]";
			parse.write(graphvizFile, sensorDot, true);
		}
		///////////////////////////////////////////////////
		parse.write(graphvizFile, "", true);
		
		
		//////////////controlled devices////////////////
		parse.write(graphvizFile, "//////////////controlled devices//////////////", true);		
		for(TemplGraph controlledDevice:controlledDevices) {
			String controlledDot=controlledDevice.name+"[shape=\"doubleoctagon\",style=\"filled\",fillcolor=\"darkseagreen1\"]";
			parse.write(graphvizFile, controlledDot, true);
//			List<String> actionList=new ArrayList<String>();
//			for(TemplGraphNode state:controlledDevice.templGraphNodes) {
//				int flag=0;
//				for(TemplTransition outTransition:state.outTransitions) {
//					String synchronisation=outTransition.synchronisation;
//					if(synchronisation!=null && synchronisation.indexOf("?")>0) {
//						String action=synchronisation.substring(0, synchronisation.indexOf("?"));
//						for(String act:actionList) {
//							if(synchronisation.indexOf(act)>=0) {
//								flag=1;
//							}
//						}
//						if(flag==0) {
//							//action
//							String actionDot=" "+action+"[shape=\"record\",style=\"filled\",fillcolor=\"beige\"]";
//							parse.write(graphvizFile, actionDot, true);
//							//actionToDevice
//							String actionToDeviceDot="  "+action+"->"+controlledDevice.name+"[color=\"lemonchiffon3\"]";
//							parse.write(graphvizFile, actionToDeviceDot, true);
//							actionList.add(action);
//						}
//					}
//				}
//				
//			}
		}
		
		//////////////////////////////////////////////////////
		parse.write(graphvizFile, "", true);
		parse.write(graphvizFile, "", true);
		
		/////////////////////rules////////////////////
		parse.write(graphvizFile, "////////////////////rulesNum/////////////////////", true);
		parse.write(graphvizFile, "", true);
		for(Rule rule:rules) {
			String ruleDot=rule.getRuleName()+"[shape=\"hexagon\",style=\"filled\",fillcolor=\"lightskyblue\"]";
			parse.write(graphvizFile, ruleDot, true);
		}
		//////////////////////////////////////////////////////
		parse.write(graphvizFile, "", true);
		parse.write(graphvizFile, "", true);
		
		
		//getTriggers
		List<Trigger> triggers=getTriggers(rules);
		List<Action> actions=getActions(rules,controlledDevices);
		parse.write(graphvizFile, "", true);
		parse.write(graphvizFile, "////////////////////actions/////////////////////", true);
		//////////////////////////////////////////////////////
		parse.write(graphvizFile, "", true);
		parse.write(graphvizFile, "", true);
		parse.write(graphvizFile, "////////////////////actions/////////////////////", true);
		for(Action action:actions) {
			//actionDot
			String actionDot=action.action+"[shape=\"record\",style=\"filled\",fillcolor=\"beige\"]";
			parse.write(graphvizFile, actionDot, true);
			for(Rule actRule:action.rules) {
				//ruleToActionDot
				String ruleToActionDot=actRule.getRuleName()+"->"+action.action;
				parse.write(graphvizFile, ruleToActionDot, true);
			}
			//actionToDeviceDot
			//2021/5/24 更改了action和device节点之间的关系
			String actionToDevice=action.device+"->"+action.action+"[color=\"lemonchiffon3\"]";
			parse.write(graphvizFile, actionToDevice, true);
		}
		
		parse.write(graphvizFile, "", true);
		parse.write(graphvizFile, "////////////////////triggers/////////////////////", true);
		for(Trigger trigger:triggers) {
			//triggerDot
			String triggerDot=trigger.triggerNum+"[label=\""+trigger.trigger+"\",shape=\"oval\",style=\"filled\",fillcolor=\"lightpink\"]";
			parse.write(graphvizFile, triggerDot, true);
			//triggerToRuleDot
			for(Rule triRule:trigger.rules) {
				String triggerToRuleDot=trigger.triggerNum+"->"+triRule.getRuleName();
				parse.write(graphvizFile, triggerToRuleDot, true);				
			}
			//sensorToTriggerDot
			if(trigger.attrVal[1].equals(".")) {
				//deviceToTriggerDot
				String deviceToTriggerDot=trigger.attrVal[0]+"->"+trigger.triggerNum+"[color=\"lightpink\"]";
				parse.write(graphvizFile, deviceToTriggerDot, true);
			}else {
				//deviceToTriggerDot
				for(TemplGraph sensor:sensors) {
					if(sensor.declaration!=null) {
						if(sensor.declaration.indexOf(trigger.attrVal[0])>=0) {
							String sensorToTriggerDot=sensor.name+"->"+trigger.triggerNum+"[color=\"lightpink\"]";
							parse.write(graphvizFile, sensorToTriggerDot, true);
							break;
						}
					}
				}
			}
			
			//trigger受到的影响
			//triggerToTrigger   相同属性
			if(trigger.attrVal[1].equals(".")) {
				//deviceState
				for(TemplGraph controlledDevice:controlledDevices) {
					if(controlledDevice.name.equals(trigger.attrVal[0])) {
						for(TemplGraphNode stateNode:controlledDevice.templGraphNodes) {
							if(stateNode.name.equals(trigger.attrVal[2])) {
								for(TemplTransition inTransition:stateNode.inTransitions) {
									if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
										//actionToTrigger
										String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
										boolean hasAction=false;
										for(Action act:actions) {
											if(action.equals(act.action)) {
												hasAction=true;
												break;
											}
										}
										//得要在rule中有这个action才行
										if(hasAction) {
											String actionToTriggerDot=action+"->"+trigger.triggerNum+"[color=\"red\",fontsize=\"18\"]";
											parse.write(graphvizFile, actionToTriggerDot, true);
										}										
										break;
									}
								}
								break;
							}
						}
						break;
					}					
				}
			}else {
				//attribute
				//action\trigger
				//actionToTrigger
				getActionEffect(graphvizFile, trigger, actions);
				//biddableTriggerToTrigger
				getBiddableEffect(graphvizFile, trigger, triggers, biddables);
				//triggerToTrigger
				for(Trigger otherTrigger:triggers) {
					if(otherTrigger!=trigger && !otherTrigger.attrVal[1].equals(".") && otherTrigger.attrVal[0].equals(trigger.attrVal[0])) {
						if(trigger.attrVal[1].equals(">")) {
							if(otherTrigger.attrVal[1].equals(">")) {
								//正影响
								Double triVal=Double.parseDouble(trigger.attrVal[2]);
								Double othTriVal=Double.parseDouble(otherTrigger.attrVal[2]);
								if(triVal<othTriVal) {
									//更改颜色为green2，同时删除“+”
									String triggerToTriggerDot=otherTrigger.triggerNum+"->"+trigger.triggerNum+"[color=\"red\",fontsize=\"18\"]";
									parse.write(graphvizFile, triggerToTriggerDot, true);
								}else if (trigger.attrVal[2].equals(otherTrigger.attrVal[2])) {
									if(otherTrigger.trigger.indexOf(">=")>0) {
										String triggerToTriggerDot=trigger.triggerNum+"->"+otherTrigger.triggerNum+"[color=\"red\",fontsize=\"18\"]";
										parse.write(graphvizFile, triggerToTriggerDot, true);
									}
								}
							}
//							if(otherTrigger.attrVal[1].equals("<")) {
//								Double triVal=Double.parseDouble(trigger.attrVal[2]);
//								Double othTriVal=Double.parseDouble(otherTrigger.attrVal[2]);
//								if(triVal>othTriVal) {
//									String triggerToTriggerDot=otherTrigger.triggerNum+"->"+trigger.triggerNum+"[color=\"red\",label=\"-\",fontsize=\"24\"]";
//									parse.write(graphvizFile, triggerToTriggerDot, true);
//								}
//							}
						}else
						if(trigger.attrVal[1].equals("<")) {
//							if(otherTrigger.attrVal[1].equals(">")) {
//								Double triVal=Double.parseDouble(trigger.attrVal[2]);
//								Double othTriVal=Double.parseDouble(otherTrigger.attrVal[2]);
//								if(triVal<othTriVal) {
//									String triggerToTriggerDot=otherTrigger.triggerNum+"->"+trigger.triggerNum+"[color=\"red\",label=\"-\",fontsize=\"24\"]";
//									parse.write(graphvizFile, triggerToTriggerDot, true);
//								}
//							}
							if(otherTrigger.attrVal[1].equals("<")) {
								Double triVal=Double.parseDouble(trigger.attrVal[2]);
								Double othTriVal=Double.parseDouble(otherTrigger.attrVal[2]);
								if(triVal>othTriVal) {
									String triggerToTriggerDot=otherTrigger.triggerNum+"->"+trigger.triggerNum+"[color=\"red\",fontsize=\"18\"]";
									parse.write(graphvizFile, triggerToTriggerDot, true);
								}else if(trigger.attrVal[2].equals(otherTrigger.attrVal[2])) {
									if(otherTrigger.trigger.indexOf("<=")>0) {
										String triggerToTriggerDot=trigger.triggerNum+"->"+otherTrigger.triggerNum+"[color=\"red\",fontsize=\"18\"]";
										parse.write(graphvizFile, triggerToTriggerDot, true);
									}
								}
							}
						}
					}
				}
				//biddableToTrigger
			}
			
			
			
		}
		
		
		/////////////////////////////////////////////
		
		parse.write(graphvizFile, "", true);
		parse.write(graphvizFile, "}", true);
	}
	
	////////////////////////////////////////////2020.12.21//////////////////////////////////
	public void getActionEffect(File graphvizFile,Trigger trigger,List<Action> actions) throws IOException {
		if(!trigger.attrVal[1].equals(".")&&!trigger.attrVal[1].equals("=")) {
			GetTemplate parse=new GetTemplate();
			String attribute=trigger.attrVal[0];
			String compareStr=trigger.attrVal[1];
			Double value=Double.parseDouble(trigger.attrVal[2]);
			for(Action action:actions) {
				for(String[] attrVal:action.attrVal) {
					Double effectVal=null;
					if(!attrVal[0].equals("temp")&&!attrVal[2].equals("temp")) {
						effectVal=Double.parseDouble(attrVal[2]);
						if(attribute.equals(attrVal[0])) {
							if(compareStr.equals(">")) {
								if(attrVal[1].equals("'")) {
									if(effectVal>0) {
										//正影响
										String actionToTriggerDot=action.action+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",fontsize=\"18\"]";
										parse.write(graphvizFile, actionToTriggerDot, true);
									}
//									if(effectVal<0) {
//										//负影响
//										String actionToTriggerDot=action.action+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
//										parse.write(graphvizFile, actionToTriggerDot, true);
//									}
									//2020.12.23晚更改负影响的线
//									if(effectVal<0||attrVal[2].equals("0")) {
//										//看是否会对该trigger有负影响
//										for(Trigger otherTrigger:triggers) {
//											if(otherTrigger!=trigger) {
//												if(otherTrigger.attrVal[0].equals(trigger.attrVal[0])) {
//													//关于相同属性
//													if(otherTrigger.attrVal[1].equals(compareStr)) {
//														//同为>
//														Double otherTriggerValue=Double.parseDouble(otherTrigger.attrVal[2]);
//														if(otherTriggerValue<value) {
//															//则先触发otherTrigger
//															if(belongSameRule(otherTrigger, action)) {
//																String actionToTriggerDot=action.action+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
//																parse.write(graphvizFile, actionToTriggerDot, true);
//															}
////															for(Rule triggerRule:otherTrigger.rules) {
////																for(Rule actionRule:action.rules) {
////																	if(triggerRule==actionRule) {
////																		String actionToTriggerDot=action.action+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
////																		parse.write(graphvizFile, actionToTriggerDot, true);
////																		break;
////																	}
////																}
////															}
//														}
//													}
//												}
//											}
//											
//										}
//									}
								}
								if(attrVal[1].equals("=")) {
									if(effectVal>value) {
										String actionToTriggerDot=action.action+"->"+trigger.triggerNum+"[color=\"red\",fontsize=\"18\"]";
										parse.write(graphvizFile, actionToTriggerDot, true);
									}
//									if(effectVal<value) {
//										String actionToTriggerDot=action.action+"->"+trigger.triggerNum+"[color=\"red\",label=\"-\",fontsize=\"24\"]";
//										parse.write(graphvizFile, actionToTriggerDot, true);
//									}
								}
							}
							if(compareStr.equals("<")) {
								if(attrVal[1].equals("'")) {
									if(effectVal<0) {
										//正影响
										String actionToTriggerDot=action.action+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",fontsize=\"18\"]";
										parse.write(graphvizFile, actionToTriggerDot, true);
									}
									
//									if(effectVal>0) {
//										//负影响
//										String actionToTriggerDot=action.action+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
//										parse.write(graphvizFile, actionToTriggerDot, true);
//									}
									
//									if(effectVal>0||attrVal[2].equals("0")) {
//										//看是否会对该trigger有负影响
//										for(Trigger otherTrigger:triggers) {
//											if(otherTrigger!=trigger) {
//												if(otherTrigger.attrVal[0].equals(trigger.attrVal[0])) {
//													//关于相同属性
//													if(otherTrigger.attrVal[1].equals(compareStr)) {
//														//同为>
//														Double otherTriggerValue=Double.parseDouble(otherTrigger.attrVal[2]);
//														if(otherTriggerValue>value) {
//															//则先触发otherTrigger
//															if(belongSameRule(otherTrigger, action)) {
//																String actionToTriggerDot=action.action+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
//																parse.write(graphvizFile, actionToTriggerDot, true);
//															}
////															for(Rule triggerRule:otherTrigger.rules) {
////																for(Rule actionRule:action.rules) {
////																	if(triggerRule==actionRule) {
////																		String actionToTriggerDot=action.action+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
////																		parse.write(graphvizFile, actionToTriggerDot, true);
////																	}
////																}
////															}
//														}
//													}
//												}
//											}
//											
//										}
//									}
									
								}
								if(attrVal[1].equals("=")) {
									if(effectVal<value) {
										String actionToTriggerDot=action.action+"->"+trigger.triggerNum+"[color=\"red\",fontsize=\"18\"]";
										parse.write(graphvizFile, actionToTriggerDot, true);
									}
									
								}
								
							}
							break;
						}
					}
					
					
				}
			}
		}
	}
	
	////////////////////////////////////////////2020.12.20//////////////////////////////////
	public void getDeviceEffect(File graphvizFile,Trigger trigger,List<TemplGraph> controlledDevices) throws IOException {
		if(!trigger.attrVal[1].equals(".")) {
			GetTemplate parse=new GetTemplate();
			String attribute=trigger.attrVal[0];
			String compareStr=trigger.attrVal[1];
			for(TemplGraph controlledDevice:controlledDevices) {
				for(TemplGraphNode stateNode:controlledDevice.templGraphNodes) {
					if(stateNode.invariant!=null && stateNode.invariant.indexOf(attribute)>=0) {
						String[] invariants=stateNode.invariant.split("&&");
						for(String invariant:invariants) {
							if(invariant.indexOf(attribute)>=0) {
								invariant=invariant.trim();
								String effectValStr=invariant.substring(invariant.indexOf("==")).substring("==".length()).trim();
								Double effectVal=Double.parseDouble(effectValStr);
								if(compareStr.equals(">")) {
									if(effectVal>0) {
										//正影响
										for(TemplTransition inTransition:stateNode.inTransitions) {
											if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
												String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?")).trim();
												String actionToTrigger=action+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",label=\"+\",fontsize=\"18\"]";
												parse.write(graphvizFile, actionToTrigger, true);
												break;
											}
										}
									}
									if(effectVal<0) {
										//负影响
										for(TemplTransition inTransition:stateNode.inTransitions) {
											if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
												String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?")).trim();
												String actionToTrigger=action+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
												parse.write(graphvizFile, actionToTrigger, true);
												break;
											}
										}
									}
								}
								if(compareStr.equals("<")) {
									if(effectVal>0) {
										//负影响
										for(TemplTransition inTransition:stateNode.inTransitions) {
											if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
												String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?")).trim();
												String actionToTrigger=action+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
												parse.write(graphvizFile, actionToTrigger, true);
												break;
											}
										}
									}
									if(effectVal<0) {
										//正影响
										for(TemplTransition inTransition:stateNode.inTransitions) {
											if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
												String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?")).trim();
												String actionToTrigger=action+"->"+trigger.triggerNum+"[style=\"dashed\",color=\"red\",label=\"+\",fontsize=\"18\"]";
												parse.write(graphvizFile, actionToTrigger, true);
												break;
											}
										}
									}
								}
							}
						}
					}else {
						for(TemplTransition inTransition:stateNode.inTransitions) {
							if(inTransition.assignment!=null && inTransition.assignment.indexOf(attribute)>=0) {
								String[] assignments=inTransition.assignment.split(",");
								for(String assignment:assignments) {
									if(assignment.indexOf(attribute)>=0 && assignment.indexOf("temp")<0) {
										assignment=assignment.trim();
										String effectValStr=assignment.substring(assignment.indexOf("=")).substring("=".length()).trim();
										Double effectVal=Double.parseDouble(effectValStr);
										Double val=Double.parseDouble(trigger.attrVal[2]);
										if(compareStr.equals(">")) {											
											if(effectVal>val) {
												//正影响
												if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
													String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?")).trim();
													String actionToTrigger=action+"->"+trigger.triggerNum+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
													parse.write(graphvizFile, actionToTrigger, true);
													break;
												}
											}
											
										}
										if(compareStr.equals("<")) {
											if(effectVal<val) {
												//正影响
												if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
													String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?")).trim();
													String actionToTrigger=action+"->"+trigger.triggerNum+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
													parse.write(graphvizFile, actionToTrigger, true);
													break;
												}
											}
										}
									}
								}
								break;
							}
						}
					}
				}
			}
		}
		
		
	}
	
	public void getDot(List<TemplGraph> templGraphs,List<Rule> rules,String dotPath) throws IOException {
		GetTemplate parse=new GetTemplate();
		TGraphToDot tDot = new TGraphToDot();
		List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
		List<TemplGraph> sensors=new ArrayList<TemplGraph>();
		TemplGraph rain=new TemplGraph();
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration!=null) {
				if(templGraph.declaration.indexOf("controlled_device")>=0) {
					controlledDevices.add(templGraph);
				}
				if(templGraph.declaration.indexOf("sensor")>=0) {
					sensors.add(templGraph);
				}				
			}
			if(templGraph.name.equals("RainInstance0")) {
				rain=templGraph;
			}
		}
		//-------------------写dot文件-----------------------------------
		
		File graphvizFile=new File(dotPath);
		if(!graphvizFile.exists()) {
			try {
				graphvizFile.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		parse.write(graphvizFile, "", false);
		parse.write(graphvizFile, "digraph infoflow{", true);
		parse.write(graphvizFile, "rankdir=LR;", true);
		parse.write(graphvizFile,  "", true);
		///////////////////sensor//////////////////
		parse.write(graphvizFile, "///////////////sensors////////////////", true);
		for(TemplGraph sensor:sensors) {
			String sensorDot=sensor.name+"[shape=\"doubleoctagon\",style=\"filled\",fillcolor=\"azure3\"]";
			parse.write(graphvizFile, sensorDot, true);
		}
		///////////////////////////////////////////////////
		parse.write(graphvizFile, "", true);
		
		//////////////controlled devices////////////////
		parse.write(graphvizFile, "//////////////controlled devices//////////////", true);
		for(TemplGraph controlledDevice:controlledDevices) {
			//device
			String deviceDot=controlledDevice.name+"[shape=\"doubleoctagon\",style=\"filled\",fillcolor=\"darkseagreen1\"]";
			parse.write(graphvizFile, deviceDot, true);			
			List<String> actionList=new ArrayList<String>();
			for(TemplGraphNode state:controlledDevice.templGraphNodes) {
				int flag=0;
				for(TemplTransition outTransition:state.outTransitions) {
					String synchronisation=outTransition.synchronisation;
					if(synchronisation!=null && synchronisation.indexOf("?")>0) {
						String action=synchronisation.substring(0, synchronisation.indexOf("?"));
						for(String act:actionList) {
							if(synchronisation.indexOf(act)>=0) {
								flag=1;
							}
						}
						if(flag==0) {
							//action
							String actionDot=action+"[shape=\"record\",style=\"filled\",fillcolor=\"beige\"]";
							parse.write(graphvizFile, actionDot, true);
							//actionToDevice
							String actionToDeviceDot=action+"->"+controlledDevice.name+"[color=\"lemonchiffon3\"]";
							parse.write(graphvizFile, actionToDeviceDot, true);
							actionList.add(action);
						}
					}
				}
				
			}
			
			
			//actionToDevice
		}
		//////////////////////////////////////////////////////
		parse.write(graphvizFile, "", true);
		parse.write(graphvizFile, "", true);
		
		/////////////////////rules////////////////////
		parse.write(graphvizFile, "////////////////////rules/////////////////////", true);
		parse.write(graphvizFile, "", true);
		for(Rule rule:rules) {
			parse.write(graphvizFile, "///////////////////"+rule.getRuleName()+"////////////////////", true);
			//ruleNum节点
			String ruleDot=rule.getRuleName()+"[shape=\"hexagon\",style=\"filled\",fillcolor=\"lightskyblue\"]";
			parse.write(graphvizFile, ruleDot, true);
			//trigger节点
			String triggerDot=rule.getRuleName()+"trigger"+"[label=\""+rule.getTrigger()+"\",shape=\"oval\",style=\"filled\",fillcolor=\"lightpink\"]";
			parse.write(graphvizFile, triggerDot, true);
			//triggerToRule
			String triggerToRuleDot=rule.getRuleName()+"trigger->"+rule.getRuleName();
			parse.write(graphvizFile, triggerToRuleDot, true);
			//ruleToAction
			for(String action:rule.getAction()) {
				if(action.indexOf("for")>0) {
					action=action.substring(0, action.indexOf("for")).trim();
				}
				String ruleToActionDot=rule.getRuleName()+"->"+action;
				parse.write(graphvizFile, ruleToActionDot, true);	
				
			}
			
			//sensorToTrigger or deviceToTrigger
			for(String trigger:rule.getTrigger()) {
				if(trigger.indexOf(">")>0) {
					
					String attribute=null;
					String valStr=null;
					//找到rule的trigger涉及的属性
					if(trigger.indexOf(".")>=0) {
						attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf(">")).substring(".".length());
					}else {
						attribute=trigger.substring(0, trigger.indexOf(">"));
					}
					//找到阈值
					if(trigger.indexOf("=")>0) {
						valStr=trigger.substring(trigger.indexOf("=")).substring("=".length());
						
					}
					if(trigger.indexOf("=")<0) {
						valStr=trigger.substring(trigger.indexOf(">")).substring(">".length());
					}
					//寻找检测该属性的sensor
					for(TemplGraph sensor:sensors) {
						if(sensor.declaration.indexOf(attribute)>=0) {
							//sensorToTrigger
							String sensorToTriggerDot=sensor.name+"->"+rule.getRuleName()+"trigger"+"[color=\"lightpink\"]";
							parse.write(graphvizFile, sensorToTriggerDot, true);
						}
					}
					//寻找设备引起的可能的影响
					tDot.getDeviceEffect(graphvizFile,attribute,valStr,  ">", sensors, controlledDevices, rule);
					//rain的影响
					tDot.getRainEffect(rain, rules, rule, attribute, valStr, ">", graphvizFile);
					//其他trigger的正影响
					for(Rule ru:rules) {
						if(ru.getTriggers().indexOf(attribute)>=0) {
							for(String tri:ru.getTrigger()) {
								if(tri.indexOf(attribute)>=0 && tri.indexOf(">")>0) {
									
									String vStr=null;
									if(tri.indexOf("=")>0) {
										vStr=tri.substring(tri.indexOf("=")).substring("=".length());
									}
									if(tri.indexOf("=")<0) {
										vStr=tri.substring(tri.indexOf(">")).substring(">".length());
									}
									if(Double.parseDouble(vStr)>Double.parseDouble(valStr)) {
										//triggerToTrigger
										String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
										parse.write(graphvizFile, triggerToTriggerDot, true);
									}
								}
							}
						}
					}
				}else if(trigger.indexOf("<")>0) {
					String attribute=null;
					String valStr=null;
					//找到rule的trigger涉及的属性
					if(trigger.indexOf(".")>=0) {
						attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf("<")).substring(".".length());
					}else {
						attribute=trigger.substring(0, trigger.indexOf("<"));
					}
					//找到阈值
					if(trigger.indexOf("=")>0) {
						valStr=trigger.substring(trigger.indexOf("=")).substring("=".length());
						
					}
					if(trigger.indexOf("=")<0) {
						valStr=trigger.substring(trigger.indexOf("<")).substring("<".length());
					}
					//寻找检测该属性的sensor
					for(TemplGraph sensor:sensors) {
						if(sensor.declaration.indexOf(attribute)>=0) {
							//sensorToTrigger
							String sensorToTriggerDot=sensor.name+"->"+rule.getRuleName()+"trigger"+"[color=\"lightpink\"]";
							parse.write(graphvizFile, sensorToTriggerDot, true);
							break;
						}
					}
					//寻找设备引起的可能的正影响
					tDot.getDeviceEffect(graphvizFile, attribute,valStr, "<", sensors, controlledDevices, rule);
					//rain的影响
					tDot.getRainEffect(rain, rules, rule, attribute, valStr, "<", graphvizFile);
					//其他trigger的正影响
					for(Rule ru:rules) {
						if(ru.getTriggers().indexOf(attribute)>=0) {
							for(String tri:ru.getTrigger()) {
								if(tri.indexOf(attribute)>=0 && tri.indexOf("<")>0) {
									
									String vStr=null;
									if(tri.indexOf("=")>0) {
										vStr=tri.substring(tri.indexOf("=")).substring("=".length());
									}
									if(tri.indexOf("=")<0) {
										vStr=tri.substring(tri.indexOf("<")).substring("<".length());
									}
									if(Double.parseDouble(vStr)<Double.parseDouble(valStr)) {
										//triggerToTrigger
										String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
										parse.write(graphvizFile, triggerToTriggerDot, true);
									}
								}
							}
						}
					}
				}else if(trigger.indexOf("=")>=0) {
					String attribute=null;
					//找到rule的trigger涉及的属性
					if(trigger.indexOf(".")>=0) {
						attribute=trigger.substring(trigger.indexOf("."), trigger.indexOf("=")).substring(".".length());
					}else {
						attribute=trigger.substring(0, trigger.indexOf("="));
					}
					//寻找检测该属性的sensor
					for(TemplGraph sensor:sensors) {
						if(sensor.declaration.indexOf(attribute)>=0) {
							//sensorToTrigger
							String sensorToTriggerDot=sensor.name+"->"+rule.getRuleName()+"trigger"+"[color=\"lightpink\"]";
							parse.write(graphvizFile, sensorToTriggerDot, true);
						}
					}
				}else {
					//trigger为设备状态
					String stateTrigger=trigger;
					String device=stateTrigger.substring(0, stateTrigger.indexOf("."));
					String state=stateTrigger.substring(stateTrigger.indexOf(".")).substring(".".length());
					//deviceToTrigger
					String deviceToTriggerDot=device+"->"+rule.getRuleName()+"trigger"+"[color=\"lightpink\"]";
					parse.write(graphvizFile, deviceToTriggerDot, true);
					//actionToTrigger, trigger为state
					for(TemplGraph contrDevice:controlledDevices) {
						if(device.equals(contrDevice.name)) {
							for(TemplGraphNode sta:contrDevice.templGraphNodes) {
								if(state.equals(sta.name)) {									
									for(TemplTransition inTransition:sta.inTransitions) {										
										if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
											String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
											String actionToStateDot=action+"->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
											parse.write(graphvizFile, actionToStateDot, true);
											break;
										}
										
									}
									break;
									
								}
							}
							break;
						}
					}
				}
			}
			parse.write(graphvizFile, "", true);
			
			
		}
		
		
	/////////////////////////////////////////////
			
	parse.write(graphvizFile, "", true);
	parse.write(graphvizFile, "}", true);
	}
	
	public String[] getParameters(String parameter) {
		String[] parameters=parameter.split(",");
		for(int i=0;i<parameters.length;i++) {
			parameters[i]=parameters[i].substring(parameters[i].indexOf(" ")).substring(" ".length());
		}
		return parameters;
	}
	
	
	
	
	public void getDeviceEffect(File graphvizFile,String attribute,String valStr,String compare,List<TemplGraph> sensors,List<TemplGraph> controlledDevices,Rule rule) throws IOException {
		GetTemplate parse=new GetTemplate();
		
		////////////////////effect//////////////////////
		//对该trigger有正影响的device的action
		for(TemplGraph device:controlledDevices) {
			for(TemplGraphNode state:device.templGraphNodes) {
				if(state.invariant!=null && state.invariant.indexOf(attribute)>=0) {
					String[] invariants=state.invariant.split("&&");
					String valueStr="";
					for(String invariant:invariants) {
						invariant=invariant.trim();
						if(invariant.indexOf(attribute)>=0) {
							valueStr=invariant.substring(invariant.indexOf("==")).substring("==".length());
							break;
						}
					}
					
					Double value=Double.parseDouble(valueStr);
					if(compare.equals(">")) {
						if(value>0) {
							//正影响
							//actionToTrigger
							for(TemplTransition inTransition:state.inTransitions) {
								if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
									String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
									String actionToTriggerDot=action+"->"+rule.getRuleName()+"trigger"+"[style=\"dashed\",color=\"red\",label=\"+\",fontsize=\"18\"]";
									parse.write(graphvizFile, actionToTriggerDot, true);
									break;
								}
							}
						}
						if(value<0) {
							//负影响
							for(TemplTransition inTransition:state.inTransitions) {
								if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
									String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
									int flag=0;
									for(String act:rule.getAction()) {
										if(act.indexOf("for")>0) {
											act=act.substring(0, act.indexOf("for")).trim();
										}
										if(act.equals(action)) {
											flag=1;
										}
									}
									if(flag==0) {
										String actionToTriggerDot=action+"->"+rule.getRuleName()+"trigger"+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
										parse.write(graphvizFile, actionToTriggerDot, true);
									}
								
									break;
								}
							}
						}
					}else if(compare.equals("<")) {
						if(value<0) {
							//actionToTrigger
							for(TemplTransition inTransition:state.inTransitions) {
								if(inTransition.synchronisation!=null&&inTransition.synchronisation.indexOf("?")>0) {
									String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
									String actionToTriggerDot=action+"->"+rule.getRuleName()+"trigger"+"[style=\"dashed\",color=\"red\",label=\"+\",fontsize=\"18\"]";
									parse.write(graphvizFile, actionToTriggerDot, true);
									break;
								}
							}
						}
						if(value>0) {
							//负影响
							for(TemplTransition inTransition:state.inTransitions) {
								if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
									String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
									int flag=0;
									for(String act:rule.getAction()) {
										if(act.indexOf("for")>0) {
											act=act.substring(0, act.indexOf("for")).trim();
										}
										if(act.equals(action)) {
											flag=1;
										}
									}
									if(flag==0) {
										String actionToTriggerDot=action+"->"+rule.getRuleName()+"trigger"+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
										parse.write(graphvizFile, actionToTriggerDot, true);
									}
								
									break;
								}
							}
						}
					}
					
				}
				if(state.invariant==null) {
					for(TemplTransition inTransition:state.inTransitions) {
						if(inTransition.assignment!=null) {
							String[] assignments=inTransition.assignment.split(",");
							for(String assignment:assignments) {
								if(assignment.indexOf(attribute)>=0&&assignment.indexOf("temp")<0) {
									String valueStr=assignment.substring(assignment.indexOf("=")).substring("=".length());
									if(compare.equals(">")) {
										if(Double.parseDouble(valueStr)>Double.parseDouble(valStr)) {
											//actionToTrigger
											
												if(inTransition.synchronisation!=null&&inTransition.synchronisation.indexOf("?")>0) {
													String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
													String actionToTriggerDot=action+"->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
													parse.write(graphvizFile, actionToTriggerDot, true);
													break;
												}
											
										}
										if(Double.parseDouble(valueStr)<Double.parseDouble(valStr)) {
											//负影响
											
												if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
													String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
													int flag=0;
													for(String act:rule.getAction()) {
														if(act.indexOf("for")>0) {
															act=act.substring(0, act.indexOf("for")).trim();
														}
														if(act.equals(action)) {
															flag=1;
														}
													}
													if(flag==0) {
														String actionToTriggerDot=action+"->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"-\",fontsize=\"24\"]";
														parse.write(graphvizFile, actionToTriggerDot, true);
													}
												
													break;
												}
											
										}
									}else if(compare.equals("<")) {
										if(Double.parseDouble(valueStr)<Double.parseDouble(valStr)) {
											//actionToTrigger
											//正影响
											
												if(inTransition.synchronisation!=null&&inTransition.synchronisation.indexOf("?")>0) {
													String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
													String actionToTriggerDot=action+"->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
													parse.write(graphvizFile, actionToTriggerDot, true);
													break;
												}
											
										}
										if(Double.parseDouble(valueStr)>Double.parseDouble(valStr)) {
											//负影响
											
												if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
													String action=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
													int flag=0;
													for(String act:rule.getAction()) {
														if(act.indexOf("for")>0) {
															act=act.substring(0, act.indexOf("for")).trim();
														}
														if(act.equals(action)) {
															flag=1;
														}
													}
													if(flag==0) {
														String actionToTriggerDot=action+"->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"-\",fontsize=\"24\"]";
														parse.write(graphvizFile, actionToTriggerDot, true);
													}
												
													break;
												}
											
										}
									}
									
								}
							}
						}
					}
				}
			}
		}
		
		
	}
	
	public void getRainEffect(TemplGraph rain,List<Rule> rules,Rule rule,String attribute,String valStr,String compare,File graphvizFile) throws IOException {
		//rain的影响
		GetTemplate parse=new GetTemplate();
		for(TemplGraphNode state:rain.templGraphNodes) {
			//invariant
			if(state.invariant!=null && state.invariant.indexOf(attribute)>=0) {
				String[] invariants=state.invariant.split("&&");
				for(String invariant:invariants) {
					int flag=0;
					if(invariant.indexOf(attribute)>=0) {
						String[] valueStr=invariant.split("==");
						if(Double.parseDouble(valueStr[1])>0) {
							//寻找trigger中包含该状态的
							if(state.name.equals("noRain")) {
								for(Rule ru:rules) {
									if(ru.getTriggers().indexOf("rain=0")>=0) {
										if(compare.equals(">")) {
											String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[style=\"dashed\",color=\"red\",label=\"+\",fontsize=\"18\"]";
											parse.write(graphvizFile, triggerToTriggerDot, true);
										}
										if(compare.equals("<")) {
											String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
											parse.write(graphvizFile, triggerToTriggerDot, true);
										}
										
									}
								}
								flag=1;
							}
							if(state.name.equals("isRain")) {
								for(Rule ru:rules) {
									if(ru.getTriggers().indexOf("rain=1")>=0) {
										if(compare.equals(">")) {
											String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[style=\"dashed\",color=\"red\",label=\"+\",fontsize=\"18\"]";
											parse.write(graphvizFile, triggerToTriggerDot, true);
										}
										if(compare.equals("<")) {
											String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
											parse.write(graphvizFile, triggerToTriggerDot, true);
										}
										
										
									}
								}
								flag=1;
							}
						}
						if(Double.parseDouble(valueStr[1])<0) {
							//寻找trigger中包含该状态的
							if(state.name.equals("noRain")) {
								for(Rule ru:rules) {
									if(ru.getTriggers().indexOf("rain=0")>=0) {
										if(compare.equals(">")) {
											String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
											parse.write(graphvizFile, triggerToTriggerDot, true);
										}
										if(compare.equals("<")) {
											String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[style=\"dashed\",color=\"red\",label=\"+\",fontsize=\"18\"]";
											parse.write(graphvizFile, triggerToTriggerDot, true);
										}
									}
								}
								flag=1;
							}
							if(state.name.equals("Rain")) {
								for(Rule ru:rules) {
									if(ru.getTriggers().indexOf("rain=1")>=0) {
										if(compare.equals(">")) {
											String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[style=\"dashed\",color=\"red\",label=\"-\",fontsize=\"24\"]";
											parse.write(graphvizFile, triggerToTriggerDot, true);
										}
										if(compare.equals("<")) {
											String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[style=\"dashed\",color=\"red\",label=\"+\",fontsize=\"18\"]";
											parse.write(graphvizFile, triggerToTriggerDot, true);
										}
									}
								}
								flag=1;
							}
						}
					}
					if(flag==1) {
						break;
					}
				}
			
			}
			if(state.inTransitions!=null) {
				for(TemplTransition inTransition:state.inTransitions) {
					//assignment
					int flag=0;
					if(inTransition.assignment!=null&&inTransition.assignment.indexOf(attribute)>=0) {
						String[] assignments=inTransition.assignment.split(",");
						for(String assignment:assignments) {
							if(assignment.indexOf(attribute)>=0 && assignment.indexOf("temp")<0) {
								String[] valueStr=assignment.split("=");
								if(Double.parseDouble(valueStr[1])>Double.parseDouble(valStr)) {
									//寻找trigger中包含该状态的
									if(state.name.equals("noRain")) {
										for(Rule ru:rules) {
											if(ru.getTriggers().indexOf("rain=0")>=0) {
												if(compare.equals(">")) {
													String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
													parse.write(graphvizFile, triggerToTriggerDot, true);
												}
												if(compare.equals("<")) {
													String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"-\",fontsize=\"24\"]";
													parse.write(graphvizFile, triggerToTriggerDot, true);
												}
											}
										}
										flag=1;
									}
									if(state.name.equals("isRain")) {
										for(Rule ru:rules) {
											if(ru.getTriggers().indexOf("rain=1")>=0) {
												if(compare.equals(">")) {
													String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
													parse.write(graphvizFile, triggerToTriggerDot, true);
												}
												if(compare.equals("<")) {
													String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"-\",fontsize=\"24\"]";
													parse.write(graphvizFile, triggerToTriggerDot, true);
												}
											}
										}
										flag=1;
									}
								}
								if(Double.parseDouble(valueStr[1])<Double.parseDouble(valStr)) {
									//寻找trigger中包含该状态的
									if(state.name.equals("noRain")) {
										for(Rule ru:rules) {
											if(ru.getTriggers().indexOf("rain=0")>=0) {
												if(compare.equals(">")) {
													String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"-\",fontsize=\"24\"]";
													parse.write(graphvizFile, triggerToTriggerDot, true);
												}
												if(compare.equals("<")) {
													String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
													parse.write(graphvizFile, triggerToTriggerDot, true);
												}
											}
										}
										flag=1;
									}
									if(state.name.equals("isRain")) {
										for(Rule ru:rules) {
											if(ru.getTriggers().indexOf("rain=1")>=0) {
												if(compare.equals(">")) {
													String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"-\",fontsize=\"24\"]";
													parse.write(graphvizFile, triggerToTriggerDot, true);
												}
												if(compare.equals("<")) {
													String triggerToTriggerDot=ru.getRuleName()+"trigger->"+rule.getRuleName()+"trigger"+"[color=\"red\",label=\"+\",fontsize=\"18\"]";
													parse.write(graphvizFile, triggerToTriggerDot, true);
												}
											}
										}
										flag=1;
									}
								}
							}
						}
					}
					if(flag==1) {
						break;
					}
				}
			}
		}

	}
	//templates list
	public void getInstance(String instanceStr,List<TemplGraph> templGraphs) {
		for(TemplGraph templGraph:templGraphs) {
			if(instanceStr.indexOf(templGraph.name)>=0) {
				String valueStr=instanceStr.substring(instanceStr.indexOf("("), instanceStr.indexOf(")")).substring("(".length());
				//对应参数值
				String[] values=valueStr.split(",");
				String[] parameters=getParameters(templGraph.parameter);
				for(int i=0;i<parameters.length;i++) {
					//将参数替换成值
					for(TemplGraphNode state:templGraph.templGraphNodes) {
						for(TemplTransition outTransition:state.outTransitions) {
							if(outTransition.assignment!=null) {
								String[] assignments=outTransition.assignment.split(",");
								for(String assignment:assignments) {
									if(assignment.indexOf("="+parameters[i])>=0) {
										String[] para=assignment.split("=");
										if(para[1].equals(parameters[i])) {
											outTransition.assignment=outTransition.assignment.replace("="+parameters[i], "="+values[i]);
											for(TemplTransition inTransition:outTransition.node.inTransitions) {
												if(inTransition.node==state) {
													inTransition.assignment=outTransition.assignment;
												}
											}
										}
									}
								}
								
							}
							if(outTransition.probability!=null) {
								if(outTransition.probability.indexOf(parameters[i])>=0) {
									outTransition.probability=outTransition.probability.replace(parameters[i], values[i]);
									for(TemplTransition inTransition:outTransition.node.inTransitions) {
										if(inTransition.node==state) {
											inTransition.probability=outTransition.probability;
										}
									}
								}
							}
							if(outTransition.guard!=null) {
								String[] guards=outTransition.guard.split("&&");
								for(String guard:guards) {
									if(guard.indexOf("="+parameters[i])>=0) {
										String[] para=guard.split("=");
										if(para[1].equals(parameters[i])) {
											outTransition.guard=outTransition.guard.replace(parameters[i], values[i]);
											for(TemplTransition inTransition:outTransition.node.inTransitions) {
												if(inTransition.node==state) {
													inTransition.assignment=outTransition.assignment;
												}
											}
										}
									}
								}
								
							}
						}
						if(state.invariant!=null) {
							String[] invariants=state.invariant.split("&&");
							for(String invariant:invariants) {
								if(invariant.indexOf("="+parameters[i])>=0) {
									if(invariant.indexOf("==")<0) {
										String[] para=invariant.split("=");
										if(para[1].equals(parameters[i])) {
											state.invariant=state.invariant.replace("="+parameters[i],"="+values[i]);
										}
									}
									if(invariant.indexOf("==")>0) {
										String[] para=invariant.split("==");
										if(para[1].equals(parameters[i])) {
											state.invariant=state.invariant.replace("="+parameters[i],"="+values[i]);
										}
									}
									
								}
							}
							
						}
					}
					
				}
				break;
			}
		}
	}
	
	//template
	public void getInst(String instanceStr,TemplGraph templGraph) {
		
			if(instanceStr.indexOf(templGraph.name)>=0) {
				String valueStr=instanceStr.substring(instanceStr.indexOf("("), instanceStr.indexOf(")")).substring("(".length());
				//对应参数值
				String[] values=valueStr.split(",");
				String[] parameters=getParameters(templGraph.parameter);
				for(int i=0;i<parameters.length;i++) {
					//将参数替换成值
					for(TemplGraphNode state:templGraph.templGraphNodes) {
						for(TemplTransition outTransition:state.outTransitions) {
							if(outTransition.assignment!=null) {
								String[] assignments=outTransition.assignment.split(",");
								for(String assignment:assignments) {
									if(assignment.indexOf("="+parameters[i])>=0) {
										String[] para=assignment.split("=");
										if(para[1].equals(parameters[i])) {
											outTransition.assignment=outTransition.assignment.replace("="+parameters[i], "="+values[i]);
											for(TemplTransition inTransition:outTransition.node.inTransitions) {
												if(inTransition.node==state) {
													inTransition.assignment=outTransition.assignment;
												}
											}
										}
									}
								}
								
							}
							if(outTransition.probability!=null) {
								if(outTransition.probability.indexOf(parameters[i])>=0) {
									outTransition.probability=outTransition.probability.replace(parameters[i], values[i]);
									for(TemplTransition inTransition:outTransition.node.inTransitions) {
										if(inTransition.node==state) {
											inTransition.probability=outTransition.probability;
										}
									}
								}
							}
							if(outTransition.guard!=null) {
								String[] guards=outTransition.guard.split("&&");
								for(String guard:guards) {
									if(guard.indexOf("="+parameters[i])>=0) {
										String[] para=guard.split("=");
										if(para[1].equals(parameters[i])) {
											outTransition.guard=outTransition.guard.replace(parameters[i], values[i]);
											for(TemplTransition inTransition:outTransition.node.inTransitions) {
												if(inTransition.node==state) {
													inTransition.assignment=outTransition.assignment;
												}
											}
										}
									}
								}
								
							}
						}
						if(state.invariant!=null) {
							String[] invariants=state.invariant.split("&&");
							for(String invariant:invariants) {
								if(invariant.indexOf("="+parameters[i])>=0) {
									if(invariant.indexOf("==")<0) {
										String[] para=invariant.split("=");
										if(para[1].equals(parameters[i])) {
											state.invariant=state.invariant.replace("="+parameters[i],"="+values[i]);
										}
									}
									if(invariant.indexOf("==")>0) {
										String[] para=invariant.split("==");
										if(para[1].equals(parameters[i])) {
											state.invariant=state.invariant.replace("="+parameters[i],"="+values[i]);
										}
									}
									
								}
							}
							
						}
					}
					
				}
				
			
		}
	}
}
