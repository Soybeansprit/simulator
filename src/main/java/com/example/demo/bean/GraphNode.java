package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class GraphNode {
	
	private String name=null;
	private String shape=null;
	private String fillcolor=null;
	private String label=null;
	public boolean flag=false;
	private List<GraphNodeArrow> pNodeList=new ArrayList<GraphNodeArrow>();
	private List<GraphNodeArrow> cNodeList=new ArrayList<GraphNodeArrow>();	
		
	
	public void addpNodeList(GraphNodeArrow pNodeList) {
		this.pNodeList.add(pNodeList);
	}
	
	public void addcNodeList(GraphNodeArrow cNodeList) {
		this.cNodeList.add(cNodeList);
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getShape() {
		return shape;
	}


	public void setShape(String shape) {
		this.shape = shape;
	}


	public String getFillcolor() {
		return fillcolor;
	}


	public void setFillcolor(String fillcolor) {
		this.fillcolor = fillcolor;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public List<GraphNodeArrow> getpNodeList() {
		return pNodeList;
	}


	public void setpNodeList(List<GraphNodeArrow> pNodeList) {
		this.pNodeList = pNodeList;
	}


	public List<GraphNodeArrow> getcNodeList() {
		return cNodeList;
	}


	public void setcNodeList(List<GraphNodeArrow> cNodeList) {
		this.cNodeList = cNodeList;
	}

}
