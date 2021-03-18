package com.example.demo.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.TemplGraphNode;
import com.example.demo.bean.TemplTransition;
import com.example.demo.service.GetTemplate.Branchpoint;
import com.example.demo.service.GetTemplate.Label;
import com.example.demo.service.GetTemplate.Location;
import com.example.demo.service.GetTemplate.Template;
import com.example.demo.service.GetTemplate.Transition;
@Service
public class TemplGraphService {

	public static void main(String[] args) throws DocumentException {
		// TODO Auto-generated method stub
		
		String path1="D:\\window16.xml";
		String path2="D:\\win16.xml";
		
		GetTemplate parse=new GetTemplate();
		parse.deletLine(path1, path2, 2);
		List<Template> templates=parse.getTemplate(path2);
		
		TemplGraphService tGraph=new TemplGraphService();
		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
		for(Template template:templates) {
			templGraphs.add(tGraph.getTemplGraph(template));
		}
		List<TemplGraph> controlledDevices=new ArrayList<TemplGraph>();
		List<TemplGraph> sensors=new ArrayList<TemplGraph>();
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.declaration.indexOf("controlled_device")>=0) {
				controlledDevices.add(templGraph);
			}
			if(templGraph.declaration.indexOf("sensor")>=0) {
				sensors.add(templGraph);
			}
		}

	}
	
	public List<TemplGraph> getTemplGraphs(File file) throws DocumentException{
		GetTemplate getTemplate=new GetTemplate();
		List<Template> templates=getTemplate.getTemplate(file);
		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
		for(Template template:templates) {
			TemplGraph templGraph=getTemplGraph(template);
			templGraphs.add(templGraph);
		}
		return templGraphs;
	}
	
	public List<TemplGraph> getTemplGraphs(String modelPathStr) throws DocumentException{
		GetTemplate getTemplate=new GetTemplate();
		List<Template> templates=getTemplate.getTemplate(modelPathStr);
		
		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
		for(Template template:templates) {
			TemplGraph templGraph=getTemplGraph(template);
			templGraphs.add(templGraph);
		}
		
		return templGraphs;
	}

	
	//template->templGraph
	public TemplGraph getTemplGraph(Template template) {
		TemplGraph templGraph=new TemplGraph();
		templGraph.name=template.name;
//		System.out.println("template");
//		System.out.println(template.name);
//		System.out.println(template.declaration);
		templGraph.declaration=template.declaration;
		templGraph.parameter=template.parameter;
		templGraph.init=template.init;
		List<TemplGraphNode> templGraphNodes=new ArrayList<TemplGraphNode>();
		for(Location location:template.locations) {
			TemplGraphNode templGraphNode=new TemplGraphNode();
			templGraphNode.name=location.name;
			templGraphNode.id=location.id;
			templGraphNode.invariant=location.invariant;
			templGraphNode.style=location.style;
			templGraphNodes.add(templGraphNode);
		}
		for(Branchpoint branchpoint:template.branchpoints) {
			TemplGraphNode templGraphNode=new TemplGraphNode();
			templGraphNode.id=branchpoint.id;
			templGraphNode.style="branchpoint";
			templGraphNodes.add(templGraphNode);
		}
		
		for(TemplGraphNode templGraphNode:templGraphNodes) {
			for(Transition transition:template.transitions) {
				if(transition.targetId.equals(templGraphNode.id)) {
					TemplTransition inTransition=new TemplTransition();
					inTransition.source=transition.sourceId;
					inTransition.target=transition.targetId;
					for(Label label:transition.labels) {
						if(label.kind.equals("assignment")) {
							inTransition.assignment=label.content;
						}
						if(label.kind.equals("synchronisation")) {
							inTransition.synchronisation=label.content;
						}
						if(label.kind.equals("probability")) {
							inTransition.probability=label.content;
						}
						if(label.kind.equals("guard")) {
							inTransition.guard=label.content;
						}
					}
					for(TemplGraphNode node:templGraphNodes) {
						if(transition.sourceId.equals(node.id)) {
							inTransition.node=node;
						}
					}
					templGraphNode.inTransitions.add(inTransition);
				}
				if(transition.sourceId.equals(templGraphNode.id)) {
					TemplTransition outTransition=new TemplTransition();
					outTransition.source=transition.sourceId;
					outTransition.target=transition.targetId;
					for(Label label:transition.labels) {
						if(label.kind.equals("assignment")) {
							outTransition.assignment=label.content;
						}
						if(label.kind.equals("synchronisation")) {
							outTransition.synchronisation=label.content;
						}
						if(label.kind.equals("probability")) {
							outTransition.probability=label.content;
						}
						if(label.kind.equals("guard")) {
							outTransition.guard=label.content;
						}
					}
					for(TemplGraphNode node:templGraphNodes) {
						if(transition.targetId.equals(node.id)) {
							outTransition.node=node;
						}
					}
					templGraphNode.outTransitions.add(outTransition);
				}
				
			}
		}
		templGraph.templGraphNodes=templGraphNodes;
		
		
		
		return templGraph;
	}
	
	//clone graph
	public TemplGraph cloneTemplGraph(TemplGraph templGraph) {
		TemplGraph cloneGraph=new TemplGraph();
		for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
			TemplGraphNode cloneNode=cloneTemplGraphNode(templGraphNode);
			cloneGraph.templGraphNodes.add(cloneNode);
		}
		connectNode(cloneGraph);
		return cloneGraph;
	}
	
	//connect
	public void connectNode(TemplGraph templGraph) {
		for(TemplGraphNode node:templGraph.templGraphNodes) {
			Iterator<TemplTransition> inTransitions=node.inTransitions .iterator();
			while(inTransitions.hasNext()) {
				TemplTransition inTransition=inTransitions.next();
				boolean hasNode=false;
				for(TemplGraphNode otherNode:templGraph.templGraphNodes) {
					if(inTransition.node.id.equals(otherNode.id)) {
						hasNode=true;
						inTransition.node=otherNode;
						boolean existTran=false;
						for(TemplTransition outTransition:otherNode.outTransitions) {
							if(outTransition.node.id.equals(node.id)) {
								existTran=true;
								outTransition.node=node;
								break;
							}
						}
						if(!existTran) {
							//如果没有这条边，就添加这条边
							TemplTransition outTransition=new TemplTransition();
							outTransition.assignment=inTransition.assignment;
							outTransition.guard=inTransition.guard;
							outTransition.probability=inTransition.probability;
							outTransition.synchronisation=inTransition.synchronisation;
							outTransition.source=inTransition.source;
							outTransition.target=inTransition.target;
							outTransition.node=node;
							otherNode.outTransitions.add(outTransition);
						}
						break;
					}
				}
				if(!hasNode) {
					//delet
					inTransitions.remove();
				}
			}
			Iterator<TemplTransition> outTransitions=node.outTransitions.iterator();
			while(outTransitions.hasNext()) {
				TemplTransition outTransition=outTransitions.next();
				boolean hasNode=false;
				for(TemplGraphNode otherNode:templGraph.templGraphNodes) {
					if(outTransition.node.id.equals(otherNode.id)) {
						hasNode=true;
						outTransition.node=otherNode;
						boolean existTran=false;
						for(TemplTransition inTransition:otherNode.inTransitions) {
							if(inTransition.node.id.equals(node.id)) {
								existTran=true;
								inTransition.node=node;
								break;
							}
						}
						if(!existTran) {
							//如果没有这条边，就添加这条边
							TemplTransition inTransition=new TemplTransition();
							inTransition.assignment=outTransition.assignment;
							inTransition.guard=outTransition.guard;
							inTransition.probability=outTransition.probability;
							inTransition.synchronisation=outTransition.synchronisation;
							inTransition.source=outTransition.source;
							inTransition.target=outTransition.target;
							inTransition.node=node;
							otherNode.inTransitions.add(inTransition);
						}
						break;
					}
				}
				if(!hasNode) {
					outTransitions.remove();
				}
			}
			
		}
	}
	
	//clone node
	public TemplGraphNode cloneTemplGraphNode(TemplGraphNode node) {
		TemplGraphNode cloneNode=new TemplGraphNode();
		cloneNode.id=node.id;
		cloneNode.invariant=node.invariant;
		cloneNode.name=node.name;
		cloneNode.style=node.style;
		for(TemplTransition inTransition:node.inTransitions) {
			TemplTransition cloneinT=new TemplTransition();
			cloneinT.assignment=inTransition.assignment;
			cloneinT.guard=inTransition.guard;
			cloneinT.node=inTransition.node;
			cloneinT.probability=inTransition.probability;
			cloneinT.source=inTransition.source;
			cloneinT.synchronisation=inTransition.synchronisation;
			cloneinT.target=inTransition.target;
			cloneNode.inTransitions.add(cloneinT);
		}
		for(TemplTransition outTransition:node.outTransitions) {
			TemplTransition cloneoutT=new TemplTransition();
			cloneoutT.assignment=outTransition.assignment;
			cloneoutT.guard=outTransition.guard;
			cloneoutT.node=outTransition.node;
			cloneoutT.probability=outTransition.probability;
			cloneoutT.source=outTransition.source;
			cloneoutT.synchronisation=outTransition.synchronisation;
			cloneoutT.target=outTransition.target;
			cloneNode.outTransitions.add(cloneoutT);
		}
		return cloneNode;
	}
	
	//删除节点的某条边
	public void deletNodeEdge(TemplTransition transition,TemplGraphNode node) {
		for(TemplTransition inTransition:node.inTransitions) {
			if(inTransition==transition) {
				node.inTransitions.remove(inTransition);
				return;
			}
		}
		for(TemplTransition outTransition:node.outTransitions) {
			if(outTransition==transition) {
				node.outTransitions.remove(outTransition);
				return;
			}
		}
	}
	
	//删除节点某条边以及对应节点该边
	public void deletEdge(TemplTransition transition,TemplGraphNode node) {
		deletNodeEdge(transition, node);
		TemplGraphNode anotherNode=transition.node;
		deletNodeEdge(transition, anotherNode);
	}

}
