package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.bean.Parameter;
import com.example.demo.service.TemplateGraph.TemplGraph;
import com.example.demo.service.TemplateGraph.TemplGraphNode;
import com.example.demo.service.TemplateGraph.TemplTransition;

//import com.simulate.GenerateSysDeclaration.Parameter;
//import com.simulate.TemplateGraph.TemplGraph;
//import com.simulate.TemplateGraph.TemplGraphNode;
//import com.simulate.TemplateGraph.TemplTransition;

public class ParameterService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public List<Parameter> getParameters(List<TemplGraph> templGraphs, List<String[]> allAttributeValue){
		List<Parameter> parameters=new ArrayList<Parameter>();
		for(String[] attributeValue:allAttributeValue) {
			Parameter parameter=new Parameter();
			parameter.setName(attributeValue[0]);
			parameter.setInitValue(attributeValue[1]);;
			parameter.setStyle("double");
			parameters.add(parameter);
		}
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.name.indexOf("Rule")>=0) {
				//规则参数
				Parameter parameter=new Parameter();
				String pname=templGraph.name.substring(0, 1).toLowerCase()+templGraph.name.substring(1);
				parameter.setName(pname);
				parameter.setStyle("int[0,1]");
				parameter.setInitValue("0");
				parameters.add(parameter);
			}else if(templGraph.declaration!=null && templGraph.declaration.indexOf("controlled_device")>=0) {
				//设备状态参数
				Parameter parameter=new Parameter();
				String pname=templGraph.name.substring(0, 1).toLowerCase()+templGraph.name.substring(1);
				parameter.setName(pname);
				int stateNum=templGraph.templGraphNodes.size();
				parameter.setStyle("int[0,"+(stateNum-1)+"]");
				parameter.setInitValue("0");
				parameters.add(parameter);
				for(TemplGraphNode stateNode:templGraph.templGraphNodes) {
					//设备会改变causal 属性，定义causal属性参数，无临时变量
					if(stateNode.invariant!=null) {
						String[] invariants=stateNode.invariant.split("&&");
						for(String invariant:invariants) {
							invariant=invariant.trim();
							if(invariant.indexOf("'==")>0) {
								String name=invariant.substring(0, invariant.indexOf("'=="));
								Parameter sameParameter=getSameParameter(parameters, name);
								if(sameParameter==null) {
									Parameter biddableParameter=new Parameter();
									biddableParameter.setName(name);
									biddableParameter.setStyle("clock");
									parameters.add(biddableParameter);
								}else {
									if(!sameParameter.getStyle().equals("clock")) {
										sameParameter.setStyle("clock");
									}
								}
							}
						}
					}
					for(TemplTransition inTransition:stateNode.inTransitions) {
						if(inTransition.synchronisation!=null && inTransition.synchronisation.indexOf("?")>0) {
							String name=inTransition.synchronisation.substring(0, inTransition.synchronisation.indexOf("?"));
							if(getSameParameter(parameters, name)==null) {
								Parameter synchroParameter=new Parameter();
								synchroParameter.setStyle("urgent broadcast chan");
								synchroParameter.setName(name);
								parameters.add(synchroParameter);
							}
						}
					}
					
				}
			}else if(templGraph.declaration!=null && templGraph.declaration.indexOf("biddable")>=0 && 
					templGraph.declaration.indexOf("sensor")<0) {
				if(templGraph.name.indexOf("Person")<0) {
					//与人无关的biddable类型实体
					Parameter parameter=new Parameter();
					int stateNum=0;
					for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
						if(templGraphNode.name!=null) {
							stateNum++;
						}
					}
					parameter.setStyle("int[0,"+(stateNum-1)+"]");
					parameter.setInitValue("0");
					for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
						if(templGraphNode.name!=null) {
							for(TemplTransition inTransition:templGraphNode.inTransitions) {
								if(inTransition.assignment!=null) {
									String[] assignments=inTransition.assignment.split(",");
									for(String assignment:assignments) {
										assignment=assignment.trim();
										String name=assignment.substring(0, assignment.indexOf("="));
										if(!name.equals("t") && getSameParameter(parameters, name)==null) {
											parameter.setName(name);
											break;
										}
									}
									break;
								}
							}
							break;
						}
					}
					parameters.add(parameter);
				}else {
					for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
						if(templGraphNode.invariant!=null) {
							String[] invariants=templGraphNode.invariant.split("&&"); 
							for(String invariant:invariants) {
								invariant=invariant.trim();
								String name=null;
								if(invariant.indexOf("'==")>0) {
									name=invariant.substring(0, invariant.indexOf("'=="));
								}else if(invariant.indexOf(">")>0) {
									name=invariant.substring(0, invariant.indexOf(">"));
								}else if(invariant.indexOf("<")>=0) {
									name=invariant.substring(0, invariant.indexOf("<"));								
								}
								if(name.equals("t")) {
									continue;
								}else {
									Parameter sameParameter=getSameParameter(parameters, name);
									if(sameParameter==null) {
										Parameter biddableParameter=new Parameter();
										biddableParameter.setName(name);
										biddableParameter.setStyle("clock");
										parameters.add(biddableParameter);
									}else {
										if(!sameParameter.getStyle().equals("clock")) {
											sameParameter.setStyle("clock");
										}
									}
								}
							}
						}
						for(TemplTransition inTransition:templGraphNode.inTransitions) {
							if(inTransition.assignment!=null) {
								String[] assignments=inTransition.assignment.split(",");
								for(String assignment:assignments) {
									assignment=assignment.trim();
									if(assignment.indexOf("++")>0) {
										String name=assignment.substring(0, assignment.indexOf("++"));
										if(getSameParameter(parameters, name)==null) {
											Parameter biddableParameter=new Parameter();
											biddableParameter.setName(name);
											biddableParameter.setStyle("int");
											biddableParameter.setInitValue("0");
											parameters.add(biddableParameter);
										}
									}else if(assignment.indexOf("--")>0) {
										String name=assignment.substring(0, assignment.indexOf("--"));
										if(getSameParameter(parameters, name)==null) {
											Parameter biddableParameter=new Parameter();
											biddableParameter.setName(name);
											biddableParameter.setStyle("int");
											biddableParameter.setInitValue("0");
											parameters.add(biddableParameter);
										}
									}
								}
							}
						}
					}
				}
				
			}else if(templGraph.declaration!=null && templGraph.declaration.indexOf("sensor")>=0) {
				for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
					boolean hasInvariantVar=false;
					if(templGraphNode.invariant!=null) {
						String[] invariants=templGraphNode.invariant.split("&&");
						for(String invariant:invariants) {
							invariant=invariant.trim();
							String name=null;
							if(invariant.indexOf("'==")>0) {
								name=invariant.substring(0, invariant.indexOf("'=="));
								
							}else if(invariant.indexOf(">")>0) {
								name=invariant.substring(0, invariant.indexOf(">"));
							}else if(invariant.indexOf("<")>0) {
								name=invariant.substring(0, invariant.indexOf("<"));
							}
							if(!name.equals("t") && getSameParameter(parameters, name)==null) {
								hasInvariantVar=true;
								Parameter parameter=new Parameter();
								parameter.setName(name);
								parameter.setStyle("clock");
								parameters.add(parameter);
							}
						}
					}
					if(!hasInvariantVar) {
						String[] declarations=templGraph.declaration.split("\n");
						String getString=null;
						for(String declaration:declarations) {
							declaration=declaration.trim();
							if(declaration.indexOf("get()")>0) {
								getString=declaration;
								break;
							}
						}
						for(TemplTransition inTransition:templGraphNode.inTransitions) {
							if(inTransition.assignment!=null) {
								String[] assignments=inTransition.assignment.split(",");
								for(String assignment:assignments) {
									assignment=assignment.trim();
									if(assignment.indexOf("get()")>0) {
										String name=assignment.substring(0,assignment.indexOf("=")).trim();
										if(!name.equals("t")&& getSameParameter(parameters, name)==null) {
											String style=getString.substring(0, getString.indexOf(" ")).trim();
											Parameter parameter=new Parameter();
											parameter.setName(name);
											parameter.setStyle(style);
											parameters.add(parameter);
										}
									}
									
								}
							}
						}
					}
				}
			}
		}
		
		return parameters;
	}
	
	public Parameter getSameParameter(List<Parameter> parameters,String name) {
		for(Parameter parameter:parameters) {
			if(parameter.getName().equals(name)) {
				return parameter;
			}
		}
		return null;
	}

}
