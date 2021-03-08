package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;

public class IFDModelParameters {
	public List<Parameter> parameters=new ArrayList<Parameter>();
	public List<String> instances=new ArrayList<String>();
	public List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
	public List<String> attributes=new ArrayList<String>();
	public List<Parameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	public List<String> getInstances() {
		return instances;
	}
	public void setInstances(List<String> instances) {
		this.instances = instances;
	}
	public List<TemplGraph> getControlledDevices() {
		return controlledDevices;
	}
	public void setControlledDevices(List<TemplGraph> controlledDevices) {
		this.controlledDevices = controlledDevices;
	}
	public List<String> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
}
