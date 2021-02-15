package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class SceneChild {
	private String name=null;
	private List<AttributeValue> children=new ArrayList<AttributeValue>();
	
	
	public void addChildren(String attributeName,double value) {
		AttributeValue attributeValue=new AttributeValue();
		attributeValue.setName(attributeName);
		attributeValue.setValue(value);
		this.children.add(attributeValue);
	}
	public void addChildren(AttributeValue attributeValue) {
		this.children.add(attributeValue);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<AttributeValue> getChildren() {
		return children;
	}
	public void setChildren(List<AttributeValue> children) {
		this.children = children;
	}

}
