package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;



public class TemplGraph {
	public String name=null;
	public String declaration=null;
	public String parameter=null;
	public String init=null;
	public List<TemplGraphNode> templGraphNodes=new ArrayList<TemplGraphNode>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeclaration() {
		return declaration;
	}
	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getInit() {
		return init;
	}
	public void setInit(String init) {
		this.init = init;
	}
	public List<TemplGraphNode> getTemplGraphNodes() {
		return templGraphNodes;
	}
	public void setTemplGraphNodes(List<TemplGraphNode> templGraphNodes) {
		this.templGraphNodes = templGraphNodes;
	}

}
