package com.example.demo.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.bean.GraphNode;
import com.example.demo.bean.GraphNodeArrow;


public class ToNode {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dotPath="D:\\graphdot2.dot";
		ToNode toNode=new ToNode();
		List<GraphNode> graphNodes=new ArrayList<GraphNode>();
		graphNodes=toNode.getNodes(dotPath);
		
		/////////////////////get IFD node//////////////////////////
		//从IFD中获得GraphNode类型的node
		///////////////////////////////////////////////////////////
	}
	
	///删除IFD中节点的边
	public void deletEdge(GraphNodeArrow arrow,GraphNode graphNode) {
		GraphNode otherGraphNode=arrow.getGraphNode();
		int flag=0;
		for(GraphNodeArrow pArrow:otherGraphNode.getpNodeList()) {
			if(pArrow.getGraphNode()==graphNode) {
				graphNode.getcNodeList().remove(arrow);
				otherGraphNode.getpNodeList().remove(pArrow);
				flag=1;
				break;
			}
		}
		if(flag==0) {
			for(GraphNodeArrow cArrow:otherGraphNode.getcNodeList()) {
				if(cArrow.getGraphNode()==graphNode) {
					graphNode.getpNodeList().remove(arrow);
					otherGraphNode.getcNodeList().remove(cArrow);
					break;
				}
			}
		}
		
	}
	
	public List<GraphNode> getNodes(String dotPath){
		List<GraphNode> graphNodes=new ArrayList<GraphNode>();
		List<String> strings=new ArrayList<String>();
		try {
			FileReader fr=new FileReader(dotPath);
			BufferedReader br=new BufferedReader(fr);
			
			String str="";
			while((str=br.readLine())!=null) {
				//获得各节点
				strings.add(str);
				if(str.indexOf("[")>0) {
					if(str.indexOf("->")<0) {
						GraphNode graphNode=new GraphNode();
						String nodeName=str.substring(0, str.indexOf("[")).trim();
						graphNode.setName(nodeName);
						String attr=str.substring(str.indexOf("["), str.indexOf("]")).substring("[".length());
						String[] features=attr.split(",");
						for(String feature:features) {
							String[] featureName=feature.split("=\"");
							featureName[1]=featureName[1].substring(0, featureName[1].indexOf("\""));
							if(featureName[0].equals("shape")) {
								graphNode.setShape(featureName[1]);
							}
							if(featureName[0].equals("label")) {
								graphNode.setLabel(featureName[1]);
							}
							if(featureName[0].equals("fillcolor")) {
								graphNode.setFillcolor(featureName[1]);
							}
						}
						graphNodes.add(graphNode);
					}
					
				}
				
				
			}
			for(String string:strings) {
				if(string.indexOf("->")>0) {
					String arrow=null;
					String attrbutes=null;
					String[] features=null;
					if(string.indexOf("[")>0) {
						arrow=string.substring(0, string.indexOf("["));
						attrbutes=string.substring(string.indexOf("["), string.indexOf("]")).substring("[".length());
						features=attrbutes.split(",");
					}else {
						arrow=string;
					}
					String[] nodes=arrow.split("->");
					for(int i=0;i<nodes.length;i++) {            
						nodes[i]=nodes[i].trim();
					}
					for(int i=0;i<graphNodes.size();i++) {
						if(nodes[0].equals(graphNodes.get(i).getName())){
							for(int j=0;j<graphNodes.size();j++) {
								if(nodes[1].equals(graphNodes.get(j).getName())){
									
									GraphNodeArrow pNode=new GraphNodeArrow();
									GraphNodeArrow cNode=new GraphNodeArrow();
									pNode.setGraphNode(graphNodes.get(i));
									cNode.setGraphNode(graphNodes.get(j));
									if(features!=null) {
										for(String feature:features) {
											String[] featureValue=feature.split("=");
											if(featureValue[0].equals("label")) {
												pNode.setLabel(featureValue[1]);
												cNode.setLabel(featureValue[1]);
											}
											if(featureValue[0].equals("style")) {
												pNode.setStyle(featureValue[1]);
												cNode.setStyle(featureValue[1]);
											}
											if(featureValue[0].equals("color")) {
												pNode.setColor(featureValue[1]);
												cNode.setColor(featureValue[1]);
											}
										}
									}
									graphNodes.get(i).addcNodeList(cNode);
									graphNodes.get(j).addpNodeList(pNode);
									break;
								}
							}
							break;
						}
					}
				}
			}
			
			br.close();
			fr.close();
			return graphNodes;
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
