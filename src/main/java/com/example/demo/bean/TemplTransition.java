package com.example.demo.bean;



public class TemplTransition {
	public String source=null;
	public String target=null;
	public TemplGraphNode node=new TemplGraphNode();
	public String assignment=null;
	public String synchronisation=null;
	public String guard=null;
	public String probability=null;     //source or target中有branchpoint则有这一项
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public TemplGraphNode getNode() {
		return node;
	}
	public void setNode(TemplGraphNode node) {
		this.node = node;
	}
	public String getAssignment() {
		return assignment;
	}
	public void setAssignment(String assignment) {
		this.assignment = assignment;
	}
	public String getSynchronisation() {
		return synchronisation;
	}
	public void setSynchronisation(String synchronisation) {
		this.synchronisation = synchronisation;
	}
	public String getGuard() {
		return guard;
	}
	public void setGuard(String guard) {
		this.guard = guard;
	}
	public String getProbability() {
		return probability;
	}
	public void setProbability(String probability) {
		this.probability = probability;
	}
}
