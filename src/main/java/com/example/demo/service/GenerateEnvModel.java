package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;

import com.example.demo.bean.Rule;
import com.example.demo.bean.TemplGraph;
import com.example.demo.bean.TemplGraphNode;
import com.example.demo.bean.TemplTransition;
import com.example.demo.service.GetTemplate.Template;
@Service
public class GenerateEnvModel {

	public static void main(String[] args) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		String path1="D:\\window21.xml";
		String path2="D:\\win21.xml";
		String xmlPath1="D:\\exp1.xml";
		String xmlPath="D:\\exp.xml";
		RuleService ruleService=new RuleService();
		
		GenerateEnvModel gModel=new GenerateEnvModel();
		TemplGraphService tGraph=new TemplGraphService();
		
		GetTemplate parse=new GetTemplate();
		parse.deletLine(path1, path2, 2);
		parse.deletLine(xmlPath1, xmlPath, 2);
		List<Template> templates=parse.getTemplate(path2);
		List<TemplGraph> templGraphs1=new ArrayList<TemplGraph>();
		for(Template template:templates) {
			templGraphs1.add(tGraph.getTemplGraph(template));
		}
		
		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
		for(TemplGraph templGraph:templGraphs1) {
			if(templGraph.declaration.indexOf("biddable")>=0) {
				List<List<TemplGraph>> allbpGraphs=new ArrayList<List<TemplGraph>>();
				for(TemplGraphNode node:templGraph.templGraphNodes) {
					if(node.style.equals("branchpoint")) {
						allbpGraphs.add(gModel.bpTemplGraphs(node, templGraph));
					}
				}
				List<TemplGraph> determinGraphs=gModel.stitchGraphs(allbpGraphs,templGraph);
				if(determinGraphs.size()==0) {
					//如果是确定的模型，不变化
				}else if(determinGraphs.size()==1) {
					//一个
					templGraphs.add(determinGraphs.get(0));
				}else {
					//多个确定模型
					if(templGraph.parameter==null) {
						gModel.finalDetermine(determinGraphs);
						for(TemplGraph deterGraph:determinGraphs) {
							templGraphs.add(deterGraph);
						}
					}else {
						gModel.finalDetermine(determinGraphs);
						for(TemplGraph deterGraph:determinGraphs) {
							templGraphs.add(deterGraph);
						}
					}
				}
			}
		}
		
		String rulePath="D:\\rules.txt";
		List<Rule> rules=ruleService.getRuleListFromTxt(rulePath);
		gModel.generateContrModel(path2, rules, templGraphs1);
		
		/////////////////////生成环境模型//////////////////////////////
		//将模型转成Template类型，再确定模型determine方法（有branchpoint则是不确定的）
		//获得biddable类型的多个确定模型后，确定最终的确定模型finalDetermine
		//最终的确定模型，写入xml文件中
		//删除最初的不确定模型
		///////////////////////////////////////////////////////////////
		
		
		
		
//		TemplateGraph tGraph=new TemplateGraph();
//		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
//		for(Template template:templates) {
//			templGraphs.add(tGraph.getTemplGraph(template));
//		}
//		
//		TemplGraph rain=tGraph.new TemplGraph();
//		TemplGraph person=tGraph.new TemplGraph();
//		for(Template template:templates) {
//			if(template.name.equals("Rain")) {
//				rain=tGraph.getTemplGraph(template);
//				break;
//			}			
//		}
//		for(Template template:templates) {
//			if(template.name.equals("PersonDistance")) {
//				person=tGraph.getTemplGraph(template);
//				break;
//			}			
//		}
		
		
		
//		String Rain="rain=Rain(8,2,65.0)";
//		String Person="person=PersonDistance(initial_time,in_time,start_time,end_time,out_time,4,1,rid,mc_rate,pro_rate)";
////		tDot.getInst(Rain, rain);
////		tDot.getInst(Person, person);
//		//将rain改成确定行为
//		
//		List<TemplGraph> rains=gModel.determine("Rain", templates);
//		for(TemplGraph r:rains) {
//			//参数值带入
//			tDot.getInst(Rain, r);
//		}
//		
//		List<TemplGraph> persons=gModel.determine("PersonDistance", templates);
//		for(TemplGraph p:persons) {
//			tDot.getInst(Person, p);
//		}
		
//		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
//		for(Template template:templates) {
//			List<TemplGraph> determinTemplGraphs=gModel.determine(template);
//			if(determinTemplGraphs.size()==0) {
//				//如果是确定的模型，不变化
//			}else if(determinTemplGraphs.size()==1) {
//				//一个
//				templGraphs.add(determinTemplGraphs.get(0));
//			}else {
//				//多个确定模型
//				if(template.parameter==null) {
//					gModel.finalDetermine(determinTemplGraphs);
//					for(TemplGraph templGraph:determinTemplGraphs) {
//						templGraphs.add(templGraph);
//					}
//				}else {
//					gModel.finalDetermine(determinTemplGraphs);
//					for(TemplGraph templGraph:determinTemplGraphs) {
//						templGraphs.add(templGraph);
//					}
//				}
//			}
//		}
		
		//确定化自治模型
		for(int i=0;i<templGraphs.size();i++) {
			gModel.generateBiddableModel(templGraphs.get(i), path2, path2, i);
		}
		
		//删除原本的不确定模型
		for(int i=0;i<templGraphs.size();i++) {
			gModel.deleteModel(path2, templGraphs.get(i).name);
		}
		
// 		for(TemplGraphNode node:rain.templGraphNodes) {
//			if(node.style.equals("branchpoint")) {
//				TemplTransition choosedTran=gModel.chooseTran(node);
//				Iterator<TemplTransition> outTransition=node.outTransitions.iterator();
//				while(outTransition.hasNext()) {
//					TemplTransition out=outTransition.next();
//					if(out!=choosedTran) {
//						//删掉该线
//						outTransition.remove();
//						Iterator<TemplTransition> inTransition=out.node.inTransitions.iterator();
//						while(inTransition.hasNext()) {
//							TemplTransition in=inTransition.next();
//							if(in.node==node) {
//								inTransition.remove();
//							}
//						}
//						gModel.deletTran(out.node);
//					}
//				}
//				
//			}
//		}
 		//确定模板，先判断是否覆盖了所有节点，如果都覆盖了则选择probability最大的模板，只有两个模板
// 		int[] rainNodeNum=new int[rains.size()];
// 		
// 		for(int i=0;i<rains.size();i++) {
// 			//判断是否覆盖了所有节点
// 			TemplGraph r=rains.get(i);
// 			int count=0;
// 			for(TemplGraphNode node:r.templGraphNodes) {
// 				if(node.inTransitions.size()>0||node.id.equals(r.init)) {
// 					count++;
// 				}
// 			}
// 			rainNodeNum[i]=count;
// 		}
// 		if(rainNodeNum[0]==rains.get(0).templGraphNodes.size()||rainNodeNum[1]==rains.get(0).templGraphNodes.size()) {
// 			if(rainNodeNum[0]==rainNodeNum[1]) {
// 				//如果都覆盖了则选择probability最大的模板
// 				for(int i=0;i<rains.get(0).templGraphNodes.size();i++) {
// 					if(rains.get(0).templGraphNodes.get(i).style.equals("branchpoint")) {
// 						TemplTransition outTransition0=rains.get(0).templGraphNodes.get(i).outTransitions.get(0);
// 						TemplTransition outTransition1=rains.get(1).templGraphNodes.get(i).outTransitions.get(0);
// 						
// 						if(!outTransition0.node.id.equals(outTransition1.node.id)) {
// 							String probability0=outTransition0.probability;
// 	 						String probability1=outTransition1.probability;
// 	 						int p0=Integer.parseInt(probability0);
// 	 						int p1=Integer.parseInt(probability1);
// 	 						if(p0>=p1) {
// 	 							rains.remove(1);
// 	 						}else {
// 	 							rains.remove(0);
// 	 						}
// 	 						
// 						}
// 					}
// 				}
// 			}else if(rainNodeNum[0]>rainNodeNum[1]) {
// 				rains.remove(1);
// 			}else {
// 				rains.remove(0);
// 			}
// 		}
// 		int[] personNodeNum=new int[persons.size()];
// 		for(int i=0;i<persons.size();i++) {
// 			TemplGraph p=persons.get(i);
// 			int count=0;
// 			for(TemplGraphNode node:p.templGraphNodes) {
// 				if(node.inTransitions.size()>0||node.id.equals(p.init)) {
// 					count++;
// 				}
// 			}
// 			personNodeNum[i]=count;
// 		}
// 		if(personNodeNum[0]==persons.get(0).templGraphNodes.size()||personNodeNum[1]==persons.get(0).templGraphNodes.size()) {
// 			if(personNodeNum[0]==personNodeNum[1]) {
// 				for(int i=0;i<persons.get(0).templGraphNodes.size();i++) {
// 					if(persons.get(0).templGraphNodes.get(i).style.equals("branchpoint")) {
// 						TemplTransition outTransition0=persons.get(0).templGraphNodes.get(i).outTransitions.get(0);
// 						TemplTransition outTransition1=persons.get(1).templGraphNodes.get(i).outTransitions.get(0);
// 						
// 						if(!outTransition0.node.id.equals(outTransition1.node.id)) {
// 							String probability0=outTransition0.probability;
// 	 						String probability1=outTransition1.probability;
// 	 						int p0=Integer.parseInt(probability0);
// 	 						int p1=Integer.parseInt(probability1);
// 	 						if(p0>=p1) {
// 	 							persons.remove(1);
// 	 						}else {
// 	 							persons.remove(0);
// 	 						}
// 	 						
// 						}
// 					}
// 				}
// 			}else if(personNodeNum[0]>personNodeNum[1]) {
// 				persons.remove(1);
// 			}else {
// 				persons.remove(0);
// 			}
// 		}
		
		
		
//		gModel.finalDetermine(rains);
//		gModel.finalDetermine(persons);
//		
// 		for(int i=0;i<rains.size();i++) {
// 			gModel.generateBiddableModel(rains.get(i), xmlPath,xmlPath,i);
// 		}
// 		for(int i=0;i<persons.size();i++) {
// 			gModel.generateBiddableModel(persons.get(i), xmlPath, xmlPath, i);
// 		}
// 		
//  		gModel.deleteModel(xmlPath,"Rule");
// 		String rulePath="D:\\rules.txt";
// 		List<Rule> rules=rt.getRules(rulePath);
// 		gModel.generateRuleModel(xmlPath, rules);

	}
	
	
	

	
	//拼接图形
	public List<TemplGraph> stitchGraphs(List<List<TemplGraph>> bpTemplGraphs,TemplGraph biddable){
		TemplGraphService tempG=new TemplGraphService();
		List<TemplGraph> finalGraphs=new ArrayList<TemplGraph>();
		
		for(List<TemplGraph> bpTemplGraph:bpTemplGraphs) {
			if(finalGraphs.size()==0) {
				for(TemplGraph currentGraph:bpTemplGraph) {
					TemplGraph ctGraph=tempG.cloneTemplGraph(currentGraph);
					tempG.connectNode(ctGraph);
					ctGraph.declaration=biddable.declaration;
					ctGraph.init=biddable.init;
					ctGraph.name=biddable.name;
					ctGraph.parameter=biddable.parameter;
					finalGraphs.add(ctGraph);
				}
			}else {
				List<TemplGraph> tempGraphs=finalGraphs;
				finalGraphs=new ArrayList<TemplGraph>();
				for(TemplGraph tempGraph:tempGraphs) {
					for(TemplGraph currentGraph:bpTemplGraph) {
						//clone
						TemplGraph tGraph=tempG.cloneTemplGraph(tempGraph);
						TemplGraph ctGraph=tempG.cloneTemplGraph(currentGraph);
						for(TemplGraphNode ctGraphNode:ctGraph.templGraphNodes) {
							boolean hasNode=false;
							for(TemplGraphNode tGraphNode:tGraph.templGraphNodes) {
								//如果ctGraphNode不在tGraph中，则添加进去
								if(tGraphNode.id.equals(ctGraphNode.id)) {
									hasNode=true;
									break;
								}
							}
							if(!hasNode) {
								tGraph.templGraphNodes.add(ctGraphNode);
							}
						}
						tempG.connectNode(tGraph);
						tGraph.declaration=biddable.declaration;
						tGraph.init=biddable.init;
						tGraph.name=biddable.name;
						tGraph.parameter=biddable.parameter;
						finalGraphs.add(tGraph);
					}
				}
			}
		}
		
		return finalGraphs;
	}
	
	//获得branchpoint所有确定的图
	public List<TemplGraph> bpTemplGraphs(TemplGraphNode branchpoint,TemplGraph templGraph){
		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
		TemplTransition chooseTran1=branchpoint.outTransitions.get(0);
		TemplTransition chooseTran2=branchpoint.outTransitions.get(1);
		if(!chooseTran1.node.id.equals(branchpoint.inTransitions.get(0).node.id)) {
			TemplGraph graph1=getGraph(branchpoint.id, chooseTran1);
			templGraphs.add(graph1);
		}
		for(TemplGraphNode allNode:templGraph.templGraphNodes) {
			if(allNode.flag) {
				allNode.flag=false;
			}
		}
		if(!chooseTran2.node.id.equals(branchpoint.inTransitions.get(0).node.id)) {
			TemplGraph graph2=getGraph(branchpoint.id, chooseTran2);
			templGraphs.add(graph2);
		}
		for(TemplGraphNode allNode:templGraph.templGraphNodes) {
			if(allNode.flag) {
				allNode.flag=false;
			}
		}
		return templGraphs;
	}
	
	//bfs  根据一个branchpoint及选择的边获得对应的部分确定图
	public TemplGraph getGraph(String bpid,TemplTransition chooseTran) {
		TemplGraphService tempG=new TemplGraphService();
 		TemplGraph templGraph=new TemplGraph();
		TemplGraphNode node=tempG.cloneTemplGraphNode(chooseTran.node);
		templGraph.templGraphNodes.add(node);
		Stack<TemplGraphNode> stack=new Stack<TemplGraphNode>();
		stack.push(node);
		chooseTran.node.flag=true;
		while(!stack.isEmpty()) {
			TemplGraphNode stackNode=stack.pop();
			Iterator<TemplTransition> outTransitions=stackNode.outTransitions.iterator();
			while(outTransitions.hasNext()) {
				TemplTransition outTransition=outTransitions.next();
				if((outTransition.node.style==null||!outTransition.node.style.equals("branchpoint"))&&!outTransition.node.flag) {
					//添加与他连接的其他节点，非branchpoint节点
					TemplGraphNode cloneNode=tempG.cloneTemplGraphNode(outTransition.node);
					stack.push(cloneNode);
					outTransition.node.flag=true;
					templGraph.templGraphNodes.add(cloneNode);
				}else if(outTransition.node.style!=null&&outTransition.node.style.equals("branchpoint")&&
						!outTransition.node.id.equals(bpid)) {
					//其他branchpoint删掉
					//删掉该outTransition
					outTransitions.remove();
				}
			}
			Iterator<TemplTransition> inTransitions=stackNode.inTransitions.iterator();
			while(inTransitions.hasNext()) {
				TemplTransition inTransition=inTransitions.next();
				if((inTransition.node.style==null||!inTransition.node.style.equals("branchpoint"))&& !inTransition.node.flag) {
					//添加与他连接的其他节点，非branchpoint节点
					TemplGraphNode cloneNode=tempG.cloneTemplGraphNode(inTransition.node);
					stack.push(cloneNode);
					inTransition.node.flag=true;
					templGraph.templGraphNodes.add(cloneNode);
				}else if(inTransition.node.id.equals(bpid) && stackNode.id.equals(node.id)) {
					//为本branchpoint节点，删掉另一条边，添加节点
					TemplGraphNode cloneNode=tempG.cloneTemplGraphNode(inTransition.node);
					stack.push(cloneNode);
					inTransition.node.flag=true;
					templGraph.templGraphNodes.add(cloneNode);
					//bp删掉另一条边
					for(TemplTransition outT:cloneNode.outTransitions) {
						if(!outT.node.id.equals(chooseTran.node.id)) {
							cloneNode.outTransitions.remove(outT);
							break;
						}
					}
				}else if((!inTransition.node.id.equals(bpid)&&inTransition.node.style!=null&&inTransition.node.style.equals("branchpoint"))||
						(inTransition.node.id.equals(bpid) && !stackNode.id.equals(node.id))) {
					//其他branchpoint节点，删掉边
					//如果非node又从branchpoint指向，删掉边
					//删掉该inTransition
					inTransitions.remove();
				}
			}
		}
		
		tempG.connectNode(templGraph);
		return templGraph;
	}
	
	
	public TemplTransition chooseTran(TemplGraphNode branchpoint) {
		//考虑只有两个分支的
		TemplGraphService tGraph=new TemplGraphService();
		TemplTransition choosedTran=new TemplTransition();
		String weight0Str=branchpoint.outTransitions.get(0).probability;
		String weight1Str=branchpoint.outTransitions.get(1).probability;
		int weight0=Integer.parseInt(weight0Str);
		int weight1=Integer.parseInt(weight1Str);
		if(weight0>=weight1) {
			if(branchpoint.outTransitions.get(0).node==branchpoint.inTransitions.get(0).node) {
				choosedTran=branchpoint.outTransitions.get(1);
			}else {
				choosedTran=branchpoint.outTransitions.get(0);
			}
			
		}else {
			if(branchpoint.outTransitions.get(1).node==branchpoint.inTransitions.get(0).node) {
				choosedTran=branchpoint.outTransitions.get(0);
			}else {
				choosedTran=branchpoint.outTransitions.get(1);
			}
		}
		
		return choosedTran;
	}
	
	//branchpoint到入边节点的出边
	public TemplTransition selfTran(TemplGraphNode branchpoint) {
		TemplGraphService tGraph=new TemplGraphService();
		TemplTransition selfT=new TemplTransition();
		int flag=0;
		for(TemplTransition outTransition:branchpoint.outTransitions) {
			if(outTransition.node==branchpoint.inTransitions.get(0).node) {
				selfT=outTransition;
				flag=1;
				break;
			}
		}
		if(flag==0) {
			selfT=null;
		}
		
		return selfT;
	}
	
	//对于非init节点，如果没有入边，则删除其所有出边
	public void deletTran(TemplGraphNode node) {
		
		if(node.inTransitions.size()==0) {
			Iterator<TemplTransition> outTransition=node.outTransitions.iterator();
			while(outTransition.hasNext()) {
				TemplTransition out=outTransition.next();
				outTransition.remove();
				Iterator<TemplTransition> inTransition=out.node.inTransitions.iterator();
				while(inTransition.hasNext()) {
					TemplTransition in=inTransition.next();
					if(in.node==node) {
						inTransition.remove();
						deletTran(out.node);
					}
				}
			}
		}
	}
	
	public List<TemplGraph> determineModel(Template template){
		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
		//如果有branchpoint则说明是不确定的模型，则需要确定化
		//只考虑branchpoint有俩边
		if(template.branchpoints.size()>0) {
			TemplGraphService tGraph=new TemplGraphService();
			TemplGraph templGraph=new TemplGraph();
			//template转templGraph
			templGraph=tGraph.getTemplGraph(template);
			templGraphs.add(templGraph);
			
			int count=1;
			for(TemplGraphNode node:templGraphs.get(0).templGraphNodes) {
				if(node.style.equals("branchpoint")) {
					int flag=0;
					//如果出边的目标节点不会到入边的节点，则这部分需要确定化，即需要俩模板
					for(TemplTransition outTransition:node.outTransitions) {
						if(outTransition.node==node.inTransitions.get(0).node) {
							flag=1;
						}
					}
					if(flag==0) {
						count*=2;
					}
				}
			}
			//获得相同模板
			while(count-1>0) {
				templGraph=tGraph.getTemplGraph(template);
				templGraphs.add(templGraph);
				count--;
			}
			for(TemplGraph templG:templGraphs) {
				//删掉branchpoint指向上一个节点的边
				for(TemplGraphNode templGraphNode:templG.templGraphNodes) {
					if(templGraphNode.style.equals("branchpoint")) {
						for(TemplTransition outTransition:templGraphNode.outTransitions) {
							if(outTransition.node==templGraphNode.inTransitions.get(0).node) {
								TemplGraphNode outNode=outTransition.node;
								templGraphNode.outTransitions.remove(outTransition);
								for(TemplTransition inTransition:outNode.inTransitions) {
									if(inTransition.node==templGraphNode) {
										outNode.inTransitions.remove(inTransition);
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
		return templGraphs;
			
	}
	
	public List<TemplGraph> determine(Template template){
		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
		//如果有branchpoint则说明是不确定的模型，则需要确定化
		if(template.branchpoints.size()>0) {
			TemplGraphService tGraph=new TemplGraphService();
			TemplGraph templGraph=new TemplGraph();
			//template转templGraph
			templGraph=tGraph.getTemplGraph(template);
			templGraphs.add(templGraph);
			
			int count=1;
			for(TemplGraphNode node:templGraphs.get(0).templGraphNodes) {
				if(node.style.equals("branchpoint")) {
					int flag=0;
					//如果出边的目标节点不会到入边的节点，则这部分需要确定化，即需要俩模板
					for(TemplTransition outTransition:node.outTransitions) {
						if(outTransition.node==node.inTransitions.get(0).node) {
							flag=1;
						}
					}
					if(flag==0) {
						count*=2;
					}
				}
			}
			//获得相同模板
			while(count-1>0) {
				templGraph=tGraph.getTemplGraph(template);
				templGraphs.add(templGraph);
				count--;
			}
			
			if(templGraphs.size()==1) {
				//branchpoint出边到入边节点，全删了
				for(int i=0;i<templGraphs.get(0).templGraphNodes.size();i++) {
					TemplGraphNode node=templGraphs.get(0).templGraphNodes.get(i);
					if(node.style.equals("branchpoint")) {
						for(TemplTransition outTransition:node.outTransitions) {
							if(outTransition.node==node.inTransitions.get(0).node) {
								TemplGraphNode outNode=outTransition.node;
								node.outTransitions.remove(outTransition);
								for(TemplTransition inTransition:outNode.inTransitions) {
									if(inTransition.node==node) {
										outNode.inTransitions.remove(inTransition);
										break;
									}
								}
								deletTran(outTransition.node);
								break;
							}
						}
					}
				}
			}
//			if(templGraphs.size()==2) {
//				for(int i=0;i<templGraphs.get(0).templGraphNodes.size();i++) {
//					TemplGraphNode node0=templGraphs.get(0).templGraphNodes.get(i);
//					TemplGraphNode node1=templGraphs.get(1).templGraphNodes.get(i);
//					if(node0.style.equals("branchpoint")) {
//						//对branchpoint节点的出边选择删除
//						
//					}
//				}
//			}
			if(templGraphs.size()==2) {
				for(int i=0;i<templGraphs.get(0).templGraphNodes.size();i++) {
					TemplGraphNode node0=templGraphs.get(0).templGraphNodes.get(i);
					TemplGraphNode node1=templGraphs.get(1).templGraphNodes.get(i);
					if(node0.style.equals("branchpoint")) {
						TemplTransition selfT0=selfTran(node0);
						TemplTransition selfT1=selfTran(node1);
						if(selfT0!=null) {
							Iterator<TemplTransition> outTransition0=node0.outTransitions.iterator();
							Iterator<TemplTransition> outTransition1=node1.outTransitions.iterator();
							//branchpoint出边到入边节点，则删掉该出边
							while(outTransition0.hasNext()) {
								TemplTransition out=outTransition0.next();
								if(out==selfT0) {
									//删掉该线
									outTransition0.remove();
									Iterator<TemplTransition> inTransition=out.node.inTransitions.iterator();
									while(inTransition.hasNext()) {
										TemplTransition in=inTransition.next();
										if(in.node==node0) {
											inTransition.remove();
										}
									}
									deletTran(out.node);
								}
							}
							while(outTransition1.hasNext()) {
								TemplTransition out=outTransition1.next();
								if(out==selfT1) {
									//删掉该线
									outTransition1.remove();
									Iterator<TemplTransition> inTransition=out.node.inTransitions.iterator();
									while(inTransition.hasNext()) {
										TemplTransition in=inTransition.next();
										if(in.node==node0) {
											inTransition.remove();
										}
									}
									deletTran(out.node);
								}
							}
						}else {
							//否则每个模板分别选一条出边，并删掉另一条出边
							TemplTransition choosedT0=node0.outTransitions.get(0);
							TemplTransition choosedT1=node1.outTransitions.get(1);
							Iterator<TemplTransition> outTransition0=node0.outTransitions.iterator();
							Iterator<TemplTransition> outTransition1=node1.outTransitions.iterator();
							//两个模板依次选择一条路径
							while(outTransition0.hasNext()) {
								TemplTransition out=outTransition0.next();
								if(out==choosedT0) {
									//删掉该线
									outTransition0.remove();
									Iterator<TemplTransition> inTransition=out.node.inTransitions.iterator();
									while(inTransition.hasNext()) {
										TemplTransition in=inTransition.next();
										if(in.node==node0) {
											inTransition.remove();
										}
									}
									deletTran(out.node);
								}
							}
							while(outTransition1.hasNext()) {
								TemplTransition out=outTransition1.next();
								if(out==choosedT1) {
									//删掉该线
									outTransition1.remove();
									Iterator<TemplTransition> inTransition=out.node.inTransitions.iterator();
									while(inTransition.hasNext()) {
										TemplTransition in=inTransition.next();
										if(in.node==node1) {
											inTransition.remove();
										}
									}
									deletTran(out.node);
								}
							}
						}
					}
				}
			}
		}
		
		return templGraphs;
	}
	
	//不确定行为确定化
	public List<TemplGraph> determine(String name,List<Template> templates) {
		List<TemplGraph> templGraphs=new ArrayList<TemplGraph>();
		
		TemplGraphService tGraph=new TemplGraphService();
		//目前只考虑两个模板，branchpoint只有两个出边
		
		for(Template template:templates) {
			if(template.name.equals(name)) {
				TemplGraph templGraph=new TemplGraph();
				templGraph=tGraph.getTemplGraph(template);
				templGraphs.add(templGraph);
				break;
			}			
		}
		
		if(templGraphs.size()>0) {
			int count=1;
			for(TemplGraphNode node:templGraphs.get(0).templGraphNodes) {
				if(node.style.equals("branchpoint")) {
					int flag=0;
					//如果出边的目标节点不会到入边的节点，则这部分需要确定化，即需要俩模板
					for(TemplTransition outTransition:node.outTransitions) {
						if(outTransition.node==node.inTransitions.get(0).node) {
							flag=1;
						}
					}
					if(flag==0) {
						count*=2;
					}
				}
			}
			//获得相同模板
			while(count-1>0) {
				for(Template template:templates) {
					if(template.name.equals(name)) {
						TemplGraph templGraph=new TemplGraph();
						templGraph=tGraph.getTemplGraph(template);
						templGraphs.add(templGraph);
						break;
					}			
				}
				count--;
			}
			if(templGraphs.size()==1) {
				//branchpoint出边到入边节点，全删了
				for(int i=0;i<templGraphs.get(0).templGraphNodes.size();i++) {
					TemplGraphNode node=templGraphs.get(0).templGraphNodes.get(i);
					if(node.style.equals("branchpoint")) {
						for(TemplTransition outTransition:node.outTransitions) {
							if(outTransition.node==node.inTransitions.get(0).node) {
								TemplGraphNode outNode=outTransition.node;
								node.outTransitions.remove(outTransition);
								for(TemplTransition inTransition:outNode.inTransitions) {
									if(inTransition.node==node) {
										outNode.inTransitions.remove(inTransition);
										break;
									}
								}
								deletTran(outTransition.node);
								break;
							}
						}
					}
				}
			}
			if(templGraphs.size()==2) {
				for(int i=0;i<templGraphs.get(0).templGraphNodes.size();i++) {
					TemplGraphNode node0=templGraphs.get(0).templGraphNodes.get(i);
					TemplGraphNode node1=templGraphs.get(1).templGraphNodes.get(i);
					if(node0.style.equals("branchpoint")) {
						TemplTransition selfT0=selfTran(node0);
						TemplTransition selfT1=selfTran(node1);
						if(selfT0!=null) {
							Iterator<TemplTransition> outTransition0=node0.outTransitions.iterator();
							Iterator<TemplTransition> outTransition1=node1.outTransitions.iterator();
							//branchpoint出边到入边节点，则删掉该出边
							while(outTransition0.hasNext()) {
								TemplTransition out=outTransition0.next();
								if(out==selfT0) {
									//删掉该线
									outTransition0.remove();
									Iterator<TemplTransition> inTransition=out.node.inTransitions.iterator();
									while(inTransition.hasNext()) {
										TemplTransition in=inTransition.next();
										if(in.node==node0) {
											inTransition.remove();
										}
									}
									deletTran(out.node);
								}
							}
							while(outTransition1.hasNext()) {
								TemplTransition out=outTransition1.next();
								if(out==selfT1) {
									//删掉该线
									outTransition1.remove();
									Iterator<TemplTransition> inTransition=out.node.inTransitions.iterator();
									while(inTransition.hasNext()) {
										TemplTransition in=inTransition.next();
										if(in.node==node0) {
											inTransition.remove();
										}
									}
									deletTran(out.node);
								}
							}
						}else {
							//否则每个模板分别选一条出边，并删掉另一条出边
							TemplTransition choosedT0=node0.outTransitions.get(0);
							TemplTransition choosedT1=node1.outTransitions.get(1);
							Iterator<TemplTransition> outTransition0=node0.outTransitions.iterator();
							Iterator<TemplTransition> outTransition1=node1.outTransitions.iterator();
							//两个模板依次选择一条路径
							while(outTransition0.hasNext()) {
								TemplTransition out=outTransition0.next();
								if(out==choosedT0) {
									//删掉该线
									outTransition0.remove();
									Iterator<TemplTransition> inTransition=out.node.inTransitions.iterator();
									while(inTransition.hasNext()) {
										TemplTransition in=inTransition.next();
										if(in.node==node0) {
											inTransition.remove();
										}
									}
									deletTran(out.node);
								}
							}
							while(outTransition1.hasNext()) {
								TemplTransition out=outTransition1.next();
								if(out==choosedT1) {
									//删掉该线
									outTransition1.remove();
									Iterator<TemplTransition> inTransition=out.node.inTransitions.iterator();
									while(inTransition.hasNext()) {
										TemplTransition in=inTransition.next();
										if(in.node==node1) {
											inTransition.remove();
										}
									}
									deletTran(out.node);
								}
							}
						}
					}
				}
			}
		}
		
		
		
		
		
		
		return templGraphs;
	}
	
	//没太大问题
	@SuppressWarnings("unchecked")
	public void generateBiddableModel(TemplGraph templGraph,String xmlPath,String newXmlPath,int num) throws DocumentException, IOException {
		if(templGraph!=null) {
			SAXReader reader= new SAXReader();
			Document document = reader.read(new File(xmlPath));
			Element rootElement=document.getRootElement();
			List<Element> templateElements=rootElement.elements("template");
			for(Element templateElement:templateElements) {
				Element nameElement=templateElement.element("name");
				if(nameElement.getTextTrim().equals(templGraph.name)) {
					//先复制一个template节点，在这个template基础上进行修改并修改name？
					Element cloneEelement=(Element) templateElement.clone();
					nameElement.setText(templGraph.name+"Instance"+num);
					templateElements.add(0,cloneEelement);
					List<Element> locationElements=templateElement.elements("location");
					List<Element> branchpointElements=templateElement.elements("branchpoint");
					List<Element> transitionElements=templateElement.elements("transition");
					for(TemplGraphNode node:templGraph.templGraphNodes) {
						//该模板中没有该节点,则删除该节点location和与之相关的transition
						if(!node.id.equals(templGraph.init) && node.inTransitions.size()==0) {
							for(Element locationElement:locationElements) {
								Attribute id=locationElement.attribute("id");
								if(id.getValue().equals(node.id)) {
									//删掉该location节点，break
									templateElement.remove(locationElement);
									break;
								}
							}
							
							for(Element transitionElement:transitionElements) {
								Element sourceElement=transitionElement.element("source");
								Element targetElement=transitionElement.element("target");
								Attribute sourceId=sourceElement.attribute("ref");
								Attribute targetId=targetElement.attribute("ref");
								if(sourceId.getValue().equals(node.id)||targetId.getValue().equals(node.id)) {
									//删除transition节点
									templateElement.remove(transitionElement);
								}
							}
						}
						if(node.style.equals("branchpoint")) {
							for(Element branchpointElement:branchpointElements) {
								Attribute id=branchpointElement.attribute("id");
								if(id!=null&&id.getValue().equals(node.id)) {
									//将该branchpoint节点改为location节点
									//添加location节点，复制id x y，改为committed节点<committed/>
									Element newLocationElement=DocumentHelper.createElement("location");
									branchpointElement.remove(id);
									newLocationElement.add(id);
									Attribute x=branchpointElement.attribute("x");
									Attribute y=branchpointElement.attribute("y");
									branchpointElement.remove(x);
									branchpointElement.remove(y);
									newLocationElement.add(x);
									newLocationElement.add(y);
									newLocationElement.addElement("committed");
									locationElements.add(0,newLocationElement);
									templateElement.remove(branchpointElement);
									
								}
							}
							for(Element transitionElement:transitionElements) {
								Element sourceElement=transitionElement.element("source");
								Element targetElement=transitionElement.element("target");
								Attribute sourceId=sourceElement.attribute("ref");
								
								if(sourceId.getValue().equals(node.id)) {
									Attribute targetId=targetElement.attribute("ref");
									if(!targetId.getValue().equals(node.outTransitions.get(0).node.id)) {
										//删除transition
										templateElement.remove(transitionElement);
									}
									//删除probability
									List<Element> labelElements=transitionElement.elements("label");
									for(Element labelElement:labelElements) {
										Attribute kind=labelElement.attribute("kind");
										if(kind.getValue().equals("probability")) {
											transitionElement.remove(labelElement);
											break;
										}
									}
								}
							}
							
						}
					}
					break;
				}
				
				
			}
			OutputStream os=new FileOutputStream(newXmlPath);
			OutputFormat format=OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			format.setTrimText(false); //保留换行，但是出现空行
			format.setNewlines(false);
			XMLWriter writer=new XMLWriter(os,format);
			writer.write(document);
			writer.close();
			os.close();
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void geneRuleModel(String xmlPath,List<Rule> rules) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(xmlPath));
		Element rootElement=document.getRootElement();
		List<Element> templateElements=rootElement.elements("template");
		for(Rule rule:rules) {
			//创建rule模型
			//xml中排版顺序为
			//<template>
			//	<name><name/>
			//	<declaration><declaration/>
			//	<location>
			//		<label><label/>
			//	<location/>
			//	<init/>
			//	<transition>
			//		<source/>
			//		<target/>
			//		<label><label/>
			//	<transition/>
			//<template/>
			Element ruleElement=DocumentHelper.createElement("template");
			templateElements.add(0,ruleElement);
			Element nameElement=ruleElement.addElement("name");
			//模型名为：Rule num
			nameElement.setText("Rule"+rule.getRuleName().substring("rule".length()));
			//模型的声明declaration
			Element declarationElement=ruleElement.addElement("declaration");
			declarationElement.setText("clock t;\n int[0,1] "+rule.getRuleName()+"=0;");
			//初始节点
			Element startElement=ruleElement.addElement("location");
			startElement.addAttribute("id", "id0");
			//初始节点位置
			startElement.addAttribute("x", "-300");
			startElement.addAttribute("y", "0");
			//初始节点具有不变式t<=3
			Element labelElement0=startElement.addElement("label");
			labelElement0.addAttribute("kind", "invariant");
			labelElement0.setText("t<=3");
			//第一个节点为初始节点
			Element initElement=ruleElement.addElement("init");
			initElement.addAttribute("ref", "id0");
			List<Element> locationElements=ruleElement.elements("location");
			//最后一个节点
			Element endElement=DocumentHelper.createElement("location");
			endElement.addAttribute("id", "id1");
			//end节点位置
			endElement.addAttribute("x", ""+(-300+150));
			endElement.addAttribute("y", "-100");
			//end节点具有不变式t<=10
			Element labelElement1=endElement.addElement("label");
			labelElement1.addAttribute("kind", "invariant");
			labelElement1.setText("t<=10");
			locationElements.add(0,endElement);
			//end->start transition
			Element transitionElement0=ruleElement.addElement("transition");
			Element sourceElement0=transitionElement0.addElement("source");
			Element targetElement0=transitionElement0.addElement("target");
			sourceElement0.addAttribute("ref", "id1");
			targetElement0.addAttribute("ref","id0");
			//end->start条件为t>=10
			Element guardElement0=transitionElement0.addElement("label");
			guardElement0.addAttribute("kind", "guard");
			guardElement0.addAttribute("x", ""+(-300+10));
			guardElement0.addAttribute("y", "-120");
			guardElement0.setText("t>=10");
			//同时assignment t=0
			Element assignmentElement0=transitionElement0.addElement("label");
			assignmentElement0.addAttribute("kind", "assignment");
			assignmentElement0.addAttribute("x", ""+(-300+10));
			assignmentElement0.addAttribute("y", "-90");
			assignmentElement0.setText("t=0");
			//nail为了美观
			Element nailElement0=transitionElement0.addElement("nail");
			nailElement0.addAttribute("x", "-300");
			nailElement0.addAttribute("y", "-100");
			List<Element> transitionElements=ruleElement.elements("transition");
			//中间节点
			
//			for(int i=0;i<rule.trigger.size();i++) {
//				if(rule.trigger.get(i).indexOf("number")>=0||rule.trigger.get(i).indexOf("rain")>=0) {
//					String temp=rule.trigger.get(i);
//					for(int j=i;j>0;j--) {
//						rule.trigger.set(j, rule.trigger.get(j-1));
//					}
//					rule.trigger.set(0, temp);
//				}
//			}
			
			int count=1; //节点计数
			//判断条件的节点
			for(int i=0;i<rule.getTrigger().size();i++) {
					//条件判断节点
					Element locationElement=DocumentHelper.createElement("location");
					locationElement.addAttribute("id", "id"+(1+count));
					locationElements.add(0,locationElement);
					//location位置
					locationElement.addAttribute("x", ""+(-300+(i+1)*150));
					locationElement.addAttribute("y", "0");
					//满足条件的transition
					Element transitionElement=DocumentHelper.createElement("transition");
					Element sourceElement=transitionElement.addElement("source");
					Element targetElement=transitionElement.addElement("target");
					//不满足条件的transition，从该节点指向初始节点
					Element unsatTransitionElement=DocumentHelper.createElement("transition");
					Element unsatSourceElement=unsatTransitionElement.addElement("source");
					Element unsatTargetElement=unsatTransitionElement.addElement("target");
					unsatSourceElement.addAttribute("ref", "id"+(1+count));
					unsatTargetElement.addAttribute("ref", "id0");
					if(rule.getTrigger().get(i).indexOf("number")>=0) {
						//如果节点条件与人数相关，
						//如果条件为number>0，则不满足条件的transition接收noPeople信号
						//如果条件为number==0，则不满足条件的transition接收hasPeople信号
						Element unsatSynElement=unsatTransitionElement.addElement("label");
						unsatSynElement.addAttribute("kind", "synchronisation");
						if(rule.getTrigger().get(i).indexOf(">")>0) {
							unsatSynElement.setText("noPeople?");
						}else {
							unsatSynElement.setText("hasPeople?");
						}	
						//label的位置
						unsatSynElement.addAttribute("x", ""+(-300+10));
						unsatSynElement.addAttribute("y", ""+((i+1)*90+10));
						
					}else if(rule.getTrigger().get(i).indexOf("rain")>=0) {
						//如果节点条件与rain相关
						//如果条件为rain=1，则不满足条件的transition接收notRain信号
						//如果条件为rain=0，则不满足条件的transition接收isRain信号
						Element unsatSynElement=unsatTransitionElement.addElement("label");
						unsatSynElement.addAttribute("kind", "synchronisation");
						if(rule.getTrigger().get(i).indexOf("=1")>0) {
							unsatSynElement.setText("notRain?");
						}else {
							unsatSynElement.setText("isRain?");
						}
						//label的位置
						unsatSynElement.addAttribute("x", ""+(-300+10));
						unsatSynElement.addAttribute("y", ""+((i+1)*90+10));
					}else {
						//如果节点为其他属性相关，则该节点类型为committed
						//不满足条件的guard为条件的反转
						Element committedElement=DocumentHelper.createElement("committed");
						locationElement.add(committedElement);
						Element unsatGuardElement=unsatTransitionElement.addElement("label");
						unsatGuardElement.addAttribute("kind", "guard");
						unsatGuardElement.setText(getReverseCon(rule.getTrigger().get(i)));
						//label位置
						unsatGuardElement.addAttribute("x", ""+(-300+10));
						unsatGuardElement.addAttribute("y", ""+((i+1)*90+10));
					}
					//不满足条件则将rule.num赋值为0，表示规则不触发
					//同时将t重新赋值为0
					Element unsatAssElement=unsatTransitionElement.addElement("label");
					unsatAssElement.addAttribute("kind", "assignment");
					//assignment label的位置
					unsatAssElement.addAttribute("x", ""+(-300+10));
					unsatAssElement.addAttribute("y", ""+((i+1)*90-20));
					unsatAssElement.setText("t=0,"+rule.getRuleName()+"=0");
					transitionElements.add(0,unsatTransitionElement);
					//增加两个nail
					Element unsatNailElement0=unsatTransitionElement.addElement("nail");
					unsatNailElement0.addAttribute("x", ""+(-300+(i+1)*150));
					unsatNailElement0.addAttribute("y", ""+(i+1)*90);
					Element unsatNailElement1=unsatTransitionElement.addElement("nail");
					unsatNailElement1.addAttribute("x", "-300");
					unsatNailElement1.addAttribute("y", ""+(i+1)*90);
					if(i==0) {
						//如果为第一个trigger判断节点，
						//则从初始节点到该节点有个transition
						//transition guard为t>=3
						sourceElement.addAttribute("ref", "id0");
						targetElement.addAttribute("ref", "id"+(1+count));
						Element guardElement=transitionElement.addElement("label");
						guardElement.addAttribute("kind", "guard");
						guardElement.setText("t>=3");						
					}else {
						//如果不是第一个判断节点
						//则从上一个判断节点到该节点有transition
						sourceElement.addAttribute("ref", "id"+count);
						targetElement.addAttribute("ref", "id"+(1+count));
						if(rule.getTrigger().get(i-1).indexOf("number")>=0) {
							//如果上一个节点条件涉及number
							Element satSynElement=transitionElement.addElement("label");
							satSynElement.addAttribute("kind", "synchronisation");
							if(rule.getTrigger().get(i-1).indexOf("=")>0) {
								satSynElement.setText("noPeople?");
							}else {
								satSynElement.setText("hasPeople?");
							}
						}else if(rule.getTrigger().get(i-1).indexOf("rain")>=0) {
							//如果上一个节点条件涉及rain
							Element satSynElement=transitionElement.addElement("label");
							satSynElement.addAttribute("kind", "synchronisation");
							if(rule.getTrigger().get(i-1).indexOf("=0")>0) {
								satSynElement.setText("notRain?");
							}else {
								satSynElement.setText("isRain?");
							}
						}else {
							//如果上一个节点条件涉及其他属性
							Element satGuardElement=transitionElement.addElement("label");
							satGuardElement.addAttribute("kind", "guard");
							satGuardElement.setText(getCondition(rule.getTrigger().get(i-1)));
						}
					}
					
					transitionElements.add(0,transitionElement);
					
					count++;
				
			}
			//action的节点  
			for(int i=0;i<rule.getAction().size();i++) {
				//action节点
				Element locationElement=DocumentHelper.createElement("location");
				locationElement.addAttribute("id", "id"+(1+count));
				locationElements.add(0,locationElement);
				//位置
				locationElement.addAttribute("x", ""+(-300+(rule.getTrigger().size()+(i+1))*150));
				locationElement.addAttribute("y", "0");
				//节点类型均为committed
				Element committedElement=DocumentHelper.createElement("committed");
				locationElement.add(committedElement);
				//上个节点到该节点的transition
				Element transitionElement=DocumentHelper.createElement("transition");
				Element sourceElement=transitionElement.addElement("source");
				Element targetElement=transitionElement.addElement("target");
				sourceElement.addAttribute("ref", "id"+count);
				targetElement.addAttribute("ref", "id"+(1+count));
				
				if(i==0) {
					//第一个action跟最后一个trigger连接
					Element assignmentElement=transitionElement.addElement("label");
					assignmentElement.addAttribute("kind", "assignment");
					if(rule.getTrigger().get(rule.getTrigger().size()-1).indexOf("number")>=0) {
						Element satSynElement=transitionElement.addElement("label");
						satSynElement.addAttribute("kind", "synchronisation");
						if(rule.getTrigger().get(rule.getTrigger().size()-1).indexOf("=")>0) {
							satSynElement.setText("noPeople?");
						}else {
							satSynElement.setText("hasPeople?");
						}
					}else if(rule.getTrigger().get(rule.getTrigger().size()-1).indexOf("rain")>=0) {
						Element satSynElement=transitionElement.addElement("label");
						satSynElement.addAttribute("kind", "synchronisation");
						if(rule.getTrigger().get(rule.getTrigger().size()-1).indexOf("=0")>0) {
							satSynElement.setText("notRain?");
						}else {
							satSynElement.setText("isRain?");
						}
					}else {
						Element satGuardElement=transitionElement.addElement("label");
						satGuardElement.addAttribute("kind", "guard");
						satGuardElement.setText(getCondition(rule.getTrigger().get(rule.getTrigger().size()-1)));
					}
					assignmentElement.setText("t=0,"+rule.getRuleName()+"=1");
					
				}else {
					//如果上一个节点为action节点，则transition发送信号为上一个action信号
					Element actionSynElement=transitionElement.addElement("label");
					actionSynElement.addAttribute("kind", "synchronisation");
					actionSynElement.setText(rule.getAction().get(i-1)+"!");
				}
				if(i==rule.getAction().size()-1) {
					//如果为action中最后一个节点，
					//则该节点与end节点之间有transition
					Element finalTransitionElement=DocumentHelper.createElement("transition");
					Element finalSourceElement=finalTransitionElement.addElement("source");
					Element finalTargetElement=finalTransitionElement.addElement("target");
					finalSourceElement.addAttribute("ref", "id"+(1+count));
					finalTargetElement.addAttribute("ref", "id1");
					Element actionSynElement=finalTransitionElement.addElement("label");
					actionSynElement.addAttribute("kind", "synchronisation");
					actionSynElement.addAttribute("x", ""+(-150+50));
					actionSynElement.addAttribute("y", "-90");
					actionSynElement.setText(rule.getAction().get(i)+"!");
					transitionElements.add(0,finalTransitionElement);
					Element finalNailElement=finalTransitionElement.addElement("nail");
					finalNailElement.addAttribute("x", ""+(-300+(rule.getTrigger().size()+(i+1))*150));
					finalNailElement.addAttribute("y", "-100");
				}
				transitionElements.add(0,transitionElement);
				count++;
			}
			
			
		}
		OutputStream os=new FileOutputStream(xmlPath);
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		format.setTrimText(false);
		format.setNewlines(true);
		XMLWriter writer=new XMLWriter(os,format);
		writer.write(document);
		writer.close();
		os.close();
		
		
	}
	
	//生成规则模型，控制器模型  需要改
	@SuppressWarnings("unchecked")
	public void generateRuleModel(String xmlPath,List<Rule> rules) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(xmlPath));
		Element rootElement=document.getRootElement();
		List<Element> templateElements=rootElement.elements("template");
		for(Rule rule:rules) {
			//创建rule模型
			//xml中排版顺序为
			//<template>
			//	<name><name/>
			//	<declaration><declaration/>
			//	<location>
			//		<label><label/>
			//	<location/>
			//	<init/>
			//	<transition>
			//		<source/>
			//		<target/>
			//		<label><label/>
			//	<transition/>
			//<template/>
			Element ruleElement=DocumentHelper.createElement("template");
			templateElements.add(0,ruleElement);
			Element nameElement=ruleElement.addElement("name");
			//模型名为：Rule num
			nameElement.setText("Rule"+rule.getRuleName().substring("rule".length()));
			//模型的声明declaration
			Element declarationElement=ruleElement.addElement("declaration");
			declarationElement.setText("clock t;\n int[0,1] "+rule.getRuleName()+"=0;");
			//初始节点
			Element startElement=ruleElement.addElement("location");
			startElement.addAttribute("id", "id0");
			//初始节点位置
			startElement.addAttribute("x", "-300");
			startElement.addAttribute("y", "0");
			//初始节点具有不变式t<=3
			Element labelElement0=startElement.addElement("label");
			labelElement0.addAttribute("kind", "invariant");
			labelElement0.setText("t<=3");
			//第一个节点为初始节点
			Element initElement=ruleElement.addElement("init");
			initElement.addAttribute("ref", "id0");
			List<Element> locationElements=ruleElement.elements("location");
			//最后一个节点
			Element endElement=DocumentHelper.createElement("location");
			endElement.addAttribute("id", "id1");
			//end节点位置
			endElement.addAttribute("x", ""+(-300+150));
			endElement.addAttribute("y", "-100");
			//end节点具有不变式t<=10
			Element labelElement1=endElement.addElement("label");
			labelElement1.addAttribute("kind", "invariant");
			labelElement1.setText("t<=10");
			locationElements.add(0,endElement);
			//end->start transition
			Element transitionElement0=ruleElement.addElement("transition");
			Element sourceElement0=transitionElement0.addElement("source");
			Element targetElement0=transitionElement0.addElement("target");
			sourceElement0.addAttribute("ref", "id1");
			targetElement0.addAttribute("ref","id0");
			//end->start条件为t>=10
			Element guardElement0=transitionElement0.addElement("label");
			guardElement0.addAttribute("kind", "guard");
			guardElement0.addAttribute("x", ""+(-300+10));
			guardElement0.addAttribute("y", "-120");
			guardElement0.setText("t>=10");
			//同时assignment t=0
			Element assignmentElement0=transitionElement0.addElement("label");
			assignmentElement0.addAttribute("kind", "assignment");
			assignmentElement0.addAttribute("x", ""+(-300+10));
			assignmentElement0.addAttribute("y", "-90");
			assignmentElement0.setText("t=0");
			//nail为了美观
			Element nailElement0=transitionElement0.addElement("nail");
			nailElement0.addAttribute("x", "-300");
			nailElement0.addAttribute("y", "-100");
			List<Element> transitionElements=ruleElement.elements("transition");
			//中间节点
			int count=1; //节点计数
			//判断条件的节点
			for(int i=0;i<rule.getTrigger().size();i++) {
					//条件判断节点
					Element locationElement=DocumentHelper.createElement("location");
					locationElement.addAttribute("id", "id"+(1+count));
					locationElements.add(0,locationElement);
					//location位置
					locationElement.addAttribute("x", ""+(-300+(i+1)*150));
					locationElement.addAttribute("y", "0");
					//满足条件的transition
					Element transitionElement=DocumentHelper.createElement("transition");
					Element sourceElement=transitionElement.addElement("source");
					Element targetElement=transitionElement.addElement("target");
					sourceElement.addAttribute("ref", "id"+(1+count));
					targetElement.addAttribute("ref", "id"+(2+count));
					//不满足条件的transition，从该节点指向初始节点
					Element unsatTransitionElement=DocumentHelper.createElement("transition");
					Element unsatSourceElement=unsatTransitionElement.addElement("source");
					Element unsatTargetElement=unsatTransitionElement.addElement("target");
					unsatSourceElement.addAttribute("ref", "id"+(1+count));
					unsatTargetElement.addAttribute("ref", "id0");
					if(rule.getTrigger().get(i).indexOf("number")>=0) {
						//如果节点条件与人数相关，
						//如果条件为number>0，则不满足条件的transition接收noPeople信号，满足条件 的transition接收hasPeople信号
						//如果条件为number==0，则不满足条件的transition接收hasPeople信号，满足条件的transition接收noPeople信号
						Element unsatSynElement=unsatTransitionElement.addElement("label");
						unsatSynElement.addAttribute("kind", "synchronisation");
						Element satSynElement=transitionElement.addElement("label");
						satSynElement.addAttribute("kind", "synchronisation");
						if(rule.getTrigger().get(i).indexOf(">")>0) {
							unsatSynElement.setText("noPeople?");
							satSynElement.setText("hasPeople?");
						}else {
							unsatSynElement.setText("hasPeople?");
							satSynElement.setText("noPeople?");
						}	
						//label的位置
						unsatSynElement.addAttribute("x", ""+(-300+10));
						unsatSynElement.addAttribute("y", ""+((i+1)*90+10));
						
					}else if(rule.getTrigger().get(i).indexOf("rain")>=0) {
						//如果节点条件与rain相关
						//如果条件为rain=1，则不满足条件的transition接收notRain信号，满足条件 的transition接收isRain信号
						//如果条件为rain=0，则不满足条件的transition接收isRain信号，满足条件 的transition接收notRain信号
						Element unsatSynElement=unsatTransitionElement.addElement("label");
						unsatSynElement.addAttribute("kind", "synchronisation");
						Element satSynElement=transitionElement.addElement("label");
						satSynElement.addAttribute("kind", "synchronisation");
						if(rule.getTrigger().get(i).indexOf("=1")>0) {
							unsatSynElement.setText("notRain?");
							satSynElement.setText("isRain?");
						}else {
							unsatSynElement.setText("isRain?");
							satSynElement.setText("notRain?");
						}
						//label的位置
						unsatSynElement.addAttribute("x", ""+(-300+10));
						unsatSynElement.addAttribute("y", ""+((i+1)*90+10));
					}else {
						//如果节点为其他属性相关，则该节点类型为committed
						//不满足条件的guard为条件的反转
						Element committedElement=DocumentHelper.createElement("committed");
						locationElement.add(committedElement);
						Element unsatGuardElement=unsatTransitionElement.addElement("label");
						unsatGuardElement.addAttribute("kind", "guard");
						Element satGuardElement=transitionElement.addElement("label");
						satGuardElement.addAttribute("kind", "guard");
						unsatGuardElement.setText(getReverseCon(rule.getTrigger().get(i)));
						satGuardElement.setText(getCondition(rule.getTrigger().get(i)));
						//label位置
						unsatGuardElement.addAttribute("x", ""+(-300+10));
						unsatGuardElement.addAttribute("y", ""+((i+1)*90+10));
					}
					//不满足条件则将rule.num赋值为0，表示规则不触发
					//同时将t重新赋值为0
					Element unsatAssElement=unsatTransitionElement.addElement("label");
					unsatAssElement.addAttribute("kind", "assignment");
					//assignment label的位置
					unsatAssElement.addAttribute("x", ""+(-300+10));
					unsatAssElement.addAttribute("y", ""+((i+1)*90-20));
					unsatAssElement.setText("t=0,"+rule.getRuleName()+"=0");
					transitionElements.add(0,unsatTransitionElement);
					//增加两个nail
					Element unsatNailElement0=unsatTransitionElement.addElement("nail");
					unsatNailElement0.addAttribute("x", ""+(-300+(i+1)*150));
					unsatNailElement0.addAttribute("y", ""+(i+1)*90);
					Element unsatNailElement1=unsatTransitionElement.addElement("nail");
					unsatNailElement1.addAttribute("x", "-300");
					unsatNailElement1.addAttribute("y", ""+(i+1)*90);
					
					
					if(i==0) {
						//如果为第一个trigger判断节点，
						//则从初始节点到该节点有个transition
						//transition guard为t>=3
						Element firstTransitionElement=DocumentHelper.createElement("transition");
						Element firstSourceElement=firstTransitionElement.addElement("source");
						Element firstTargetElement=firstTransitionElement.addElement("target");
						firstSourceElement.addAttribute("ref", "id0");
						firstTargetElement.addAttribute("ref", "id"+(1+count));
						Element guardElement=firstTransitionElement.addElement("label");
						guardElement.addAttribute("kind", "guard");
						guardElement.setText("t>=3");
						transitionElements.add(0,firstTransitionElement);
					}
					if(i==rule.getTrigger().size()-1) {
						//最后一个trigger满足
						Element assignmentElement=transitionElement.addElement("label");
						assignmentElement.addAttribute("kind", "assignment");
						assignmentElement.setText("t=0,"+rule.getRuleName()+"=1");
					}
					
					transitionElements.add(0,transitionElement);
					
					count++;
				
			}
			//action的节点  
			for(int i=0;i<rule.getAction().size();i++) {
				//action节点
				Element locationElement=DocumentHelper.createElement("location");
				locationElement.addAttribute("id", "id"+(1+count));
				locationElements.add(0,locationElement);
				//位置
				locationElement.addAttribute("x", ""+(-300+(rule.getTrigger().size()+(i+1))*150));
				locationElement.addAttribute("y", "0");
				//节点类型均为committed
				Element committedElement=DocumentHelper.createElement("committed");
				locationElement.add(committedElement);
				//上个节点到该节点的transition
				Element transitionElement=DocumentHelper.createElement("transition");
				Element sourceElement=transitionElement.addElement("source");
				Element targetElement=transitionElement.addElement("target");
				
				
				if(i<rule.getAction().size()-1) {
					sourceElement.addAttribute("ref", "id"+(1+count));
					targetElement.addAttribute("ref", "id"+(2+count));
					Element actionSynElement=transitionElement.addElement("label");
					actionSynElement.addAttribute("kind", "synchronisation");
					actionSynElement.setText(rule.getAction().get(i)+"!");
				}
				if(i==rule.getAction().size()-1) {
					//最后一个action节点与end连接
					sourceElement.addAttribute("ref", "id"+(1+count));
					targetElement.addAttribute("ref", "id1");
					Element actionSynElement=transitionElement.addElement("label");
					actionSynElement.addAttribute("kind", "synchronisation");
					actionSynElement.addAttribute("x", ""+(-150+50));
					actionSynElement.addAttribute("y", "-90");
					actionSynElement.setText(rule.getAction().get(i)+"!");
					Element finalNailElement=transitionElement.addElement("nail");
					finalNailElement.addAttribute("x", ""+(-300+(rule.getTrigger().size()+(i+1))*150));
					finalNailElement.addAttribute("y", "-100");
				}
				
				
				transitionElements.add(0,transitionElement);
				count++;
			}
			
			
		}
		OutputStream os=new FileOutputStream(xmlPath);
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		format.setTrimText(false);
		format.setNewlines(true);
		XMLWriter writer=new XMLWriter(os,format);
		writer.write(document);
		writer.close();
		os.close();
		
		
	}
	
	
	//////////////////////2020.12.18////////////////////////////////
	@SuppressWarnings("unchecked")
	public void generateContrModel(String xmlPath,List<Rule> rules,List<TemplGraph> templGraphs) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(xmlPath));
		Element rootElement=document.getRootElement();
		List<Element> templateElements=rootElement.elements("template");
		for(Rule rule:rules) {
			//创建rule模型
			//xml中排版顺序为
			//<template>
			//	<name><name/>
			//	<declaration><declaration/>
			//	<location>
			//		<label><label/>
			//	<location/>
			//	<init/>
			//	<transition>
			//		<source/>
			//		<target/>
			//		<label><label/>
			//	<transition/>
			//<template/>
			Element ruleElement=DocumentHelper.createElement("template");
			templateElements.add(0,ruleElement);
			Element nameElement=ruleElement.addElement("name");
			//模型名为：Rule num
			nameElement.setText("Rule"+rule.getRuleName().substring("rule".length()));
			//模型的声明declaration
			Element declarationElement=ruleElement.addElement("declaration");
			declarationElement.setText("clock t;");
			//初始节点
			Element startElement=ruleElement.addElement("location");
			startElement.addAttribute("id", "id0");
			//初始节点位置
			startElement.addAttribute("x", "-300");
			startElement.addAttribute("y", "0");
			//初始节点具有不变式t<=3
			Element labelElement0=startElement.addElement("label");
			labelElement0.addAttribute("kind", "invariant");
			labelElement0.setText("t<=3");
			//第一个节点为初始节点
			Element initElement=ruleElement.addElement("init");
			initElement.addAttribute("ref", "id0");
			List<Element> locationElements=ruleElement.elements("location");
			//最后一个节点
			Element endElement=DocumentHelper.createElement("location");
			endElement.addAttribute("id", "id1");
			//end节点位置
			endElement.addAttribute("x", ""+(-300+150));
			endElement.addAttribute("y", "-100");
			//end节点具有不变式t<=10
			Element labelElement1=endElement.addElement("label");
			labelElement1.addAttribute("kind", "invariant");
			labelElement1.setText("t<=10");
			locationElements.add(0,endElement);
			//end->start transition
			Element transitionElement0=ruleElement.addElement("transition");
			Element sourceElement0=transitionElement0.addElement("source");
			Element targetElement0=transitionElement0.addElement("target");
			sourceElement0.addAttribute("ref", "id1");
			targetElement0.addAttribute("ref","id0");
			//end->start条件为t>=10
			Element guardElement0=transitionElement0.addElement("label");
			guardElement0.addAttribute("kind", "guard");
			guardElement0.addAttribute("x", ""+(-300+10));
			guardElement0.addAttribute("y", "-120");
			guardElement0.setText("t>=10");
			//同时assignment t=0
			Element assignmentElement0=transitionElement0.addElement("label");
			assignmentElement0.addAttribute("kind", "assignment");
			assignmentElement0.addAttribute("x", ""+(-300+10));
			assignmentElement0.addAttribute("y", "-90");
			assignmentElement0.setText("t=0");
			//nail为了美观
			Element nailElement0=transitionElement0.addElement("nail");
			nailElement0.addAttribute("x", "-300");
			nailElement0.addAttribute("y", "-100");
			List<Element> transitionElements=ruleElement.elements("transition");
			//中间节点
			int count=1; //节点计数
			//判断条件的节点
			for(int i=0;i<rule.getTrigger().size();i++) {
				//条件判断节点
				Element locationElement=DocumentHelper.createElement("location");
				locationElement.addAttribute("id", "id"+(1+count));
				locationElements.add(0,locationElement);
				//location位置
				locationElement.addAttribute("x", ""+(-300+count*150));
				locationElement.addAttribute("y", "0");
				//满足条件的transition
				Element satTransitionElement=DocumentHelper.createElement("transition");
				Element sourceElement=satTransitionElement.addElement("source");
				Element targetElement=satTransitionElement.addElement("target");
				sourceElement.addAttribute("ref", "id"+(1+count));
				targetElement.addAttribute("ref", "id"+(2+count));
				//不满足条件的transition，从该节点指向初始节点
				Element unsatTransitionElement=DocumentHelper.createElement("transition");
				Element unsatSourceElement=unsatTransitionElement.addElement("source");
				Element unsatTargetElement=unsatTransitionElement.addElement("target");
				unsatSourceElement.addAttribute("ref", "id"+(1+count));
				unsatTargetElement.addAttribute("ref", "id0");
				Element committedElement=DocumentHelper.createElement("committed");
				locationElement.add(committedElement);
				Element unsatGuardElement=unsatTransitionElement.addElement("label");
				unsatGuardElement.addAttribute("kind", "guard");
				Element satGuardElement=satTransitionElement.addElement("label");
				satGuardElement.addAttribute("kind", "guard");
				List<String> conAndReverseCon=new ArrayList<String>();
				if(rule.getTrigger().get(i).indexOf("=")<0&&
						rule.getTrigger().get(i).indexOf("<")<0&&
						rule.getTrigger().get(i).indexOf(">")<0) {
					//如果节点为设备相关，则该节点类型为committed
					//不满足条件的guard为条件的反转
					conAndReverseCon=getConAndReverseCon(rule.getTrigger().get(i),templGraphs);
				}else {
					//如果节点为属性相关，则该节点类型为committed
					//不满足条件的guard为条件的反转
					conAndReverseCon=getConAndReverseCon(rule.getTrigger().get(i));
				}
				
				unsatGuardElement.setText(conAndReverseCon.get(1));
				satGuardElement.setText(conAndReverseCon.get(0));
				//label位置
				unsatGuardElement.addAttribute("x", ""+(-300+10));
				unsatGuardElement.addAttribute("y", ""+(count*90+10));
				//不满足条件则将rule.num赋值为0，表示规则不触发
				//同时将t重新赋值为0
				Element unsatAssElement=unsatTransitionElement.addElement("label");
				unsatAssElement.addAttribute("kind", "assignment");
				//assignment label的位置
				unsatAssElement.addAttribute("x", ""+(-300+10));
				unsatAssElement.addAttribute("y", ""+(count*90-20));
				unsatAssElement.setText("t=0,"+rule.getRuleName()+"=0");
				transitionElements.add(0,unsatTransitionElement);
				//增加两个nail
				Element unsatNailElement0=unsatTransitionElement.addElement("nail");
				unsatNailElement0.addAttribute("x", ""+(-300+(i+1)*150));
				unsatNailElement0.addAttribute("y", ""+count*90);
				Element unsatNailElement1=unsatTransitionElement.addElement("nail");
				unsatNailElement1.addAttribute("x", "-300");
				unsatNailElement1.addAttribute("y", ""+count*90);
				if(i==0) {
					//如果为第一个trigger判断节点，
					//则从初始节点到该节点有个transition
					//transition guard为t>=3
					Element firstTransitionElement=DocumentHelper.createElement("transition");
					Element firstSourceElement=firstTransitionElement.addElement("source");
					Element firstTargetElement=firstTransitionElement.addElement("target");
					firstSourceElement.addAttribute("ref", "id0");
					firstTargetElement.addAttribute("ref", "id"+(1+count));
					Element guardElement=firstTransitionElement.addElement("label");
					guardElement.addAttribute("kind", "guard");
					guardElement.setText("t>=3");
					transitionElements.add(0,firstTransitionElement);
				}
				if(i==rule.getTrigger().size()-1) {
					//最后一个trigger满足
					Element assignmentElement=satTransitionElement.addElement("label");
					assignmentElement.addAttribute("kind", "assignment");
					assignmentElement.setText("t=0,"+rule.getRuleName()+"=1");
				}				
				transitionElements.add(0,satTransitionElement);				
				count++;
			}
			
			//action的节点  
			for(int i=0;i<rule.getAction().size();i++) {
				//action节点
				Element locationElement=DocumentHelper.createElement("location");
				locationElement.addAttribute("id", "id"+(1+count));
				locationElements.add(0,locationElement);
				//位置
				locationElement.addAttribute("x", ""+(-300+count*150));
				locationElement.addAttribute("y", "0");
				//节点类型均为committed
				Element committedElement=DocumentHelper.createElement("committed");
				locationElement.add(committedElement);
				//该节点到下一节点的transition
				Element transitionElement=DocumentHelper.createElement("transition");
				Element sourceElement=transitionElement.addElement("source");
				Element targetElement=transitionElement.addElement("target");
				String[] actionTime=rule.getAction().get(i).split("for");
				actionTime[0]=actionTime[0].trim();
				if(rule.getAction().get(i).indexOf("for")>0) {
					actionTime[1]=actionTime[1].trim();
				}
				
				
				if(i<rule.getAction().size()-1) {
					//action 可能包含for表示经过多长时间后进行下一个
					//without for
					sourceElement.addAttribute("ref", "id"+(1+count));
					targetElement.addAttribute("ref", "id"+(2+count));
					Element actionSynElement=transitionElement.addElement("label");
					actionSynElement.addAttribute("kind", "synchronisation");
					
					actionSynElement.setText(actionTime[0]+"!");
					
					if(rule.getAction().get(i).indexOf("for")>0) {
						//for time 节点
						Element nextLocationElement=DocumentHelper.createElement("location");
						nextLocationElement.addAttribute("id", "id"+(2+count));
						locationElements.add(0,nextLocationElement);
						//位置
						nextLocationElement.addAttribute("x", ""+(-300+(count+1)*150));
						nextLocationElement.addAttribute("y", "0");
						//该节点到下一节点的transition
						Element nextTransitionElement=DocumentHelper.createElement("transition");
						Element nextSourceElement=nextTransitionElement.addElement("source");
						Element nextTargetElement=nextTransitionElement.addElement("target");
						nextSourceElement.addAttribute("ref", "id"+(2+count));
						nextTargetElement.addAttribute("ref", "id"+(3+count));
						String time=actionTime[1].substring(0, actionTime[1].indexOf("s"));
						Element invariantElement=nextLocationElement.addElement("label");
						invariantElement.addAttribute("kind", "invariant");
						invariantElement.setText("t<="+time);
						Element assignmentElement =nextTransitionElement.addElement("label");
						assignmentElement.addAttribute("kind", "assignment");
						assignmentElement.setText("t=0");
						Element guardElement=nextTransitionElement.addElement("label");
						guardElement.addAttribute("kind", "guard");
						guardElement.setText("t>="+time);
						transitionElements.add(0,nextTransitionElement);
						count++;
					}
				}
				if(i==rule.getAction().size()-1) {
					//最后一个action节点与end连接
					sourceElement.addAttribute("ref", "id"+(1+count));
					targetElement.addAttribute("ref", "id1");
					Element actionSynElement=transitionElement.addElement("label");
					actionSynElement.addAttribute("kind", "synchronisation");
					actionSynElement.addAttribute("x", ""+(-150+50));
					actionSynElement.addAttribute("y", "-90");
					actionSynElement.setText(actionTime[0]+"!");
					Element finalNailElement=transitionElement.addElement("nail");
					finalNailElement.addAttribute("x", ""+(-300+count*150));
					finalNailElement.addAttribute("y", "-100");
				}
				
				
				transitionElements.add(0,transitionElement);
				count++;
			}
			
			
		}
			
		
		OutputStream os=new FileOutputStream(xmlPath);
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		format.setTrimText(false);
		format.setNewlines(true);
		XMLWriter writer=new XMLWriter(os,format);
		writer.write(document);
		writer.close();
		os.close();
	}
	
	
	/////////////////////////////////////2020.12.17///////////////////////////////
	//获得属性条件和反转条件
	//////String[0]-----condition String[1]-----reverseCondition
	public List<String> getConAndReverseCon(String trigger){
		List<String> conAndReverseCon=new ArrayList<String>();
		String reverseCondition=null;
		String condition=trigger;
		if(trigger.indexOf("FOR")>=0) {
			condition=trigger.substring(0, trigger.indexOf("FOR")).trim();
		}
		if(trigger.indexOf(".")>0) {
			condition=condition.substring(condition.indexOf(".")).substring(".".length());
		}
		if(condition.indexOf(">=")>0) {
			reverseCondition=condition.replace(">=", "<");
		}else if(condition.indexOf(">")>0) {
			reverseCondition=condition.replace(">", "<=");
		}else if(condition.indexOf("<=")>0) {
			reverseCondition=condition.replace("<=", ">");
		}else if(condition.indexOf("<")>0) {
			reverseCondition=condition.replace("<", ">=");
		}else if(condition.indexOf("=")>0) {
			reverseCondition=condition.replace("=", "!=");
			condition=condition.replace("=", "==");
		}
		//距离感应
		if(reverseCondition.indexOf("distanceFrom")>=0) {
			if(reverseCondition.indexOf(">")>0) {
				reverseCondition=reverseCondition.replace(">", "Min>");
			}
			if(reverseCondition.indexOf("<")>0) {
				reverseCondition=reverseCondition.replace("<","Min<");
			}
		}
		if(condition.indexOf("distanceFrom")>=0) {
			if(condition.indexOf(">")>0) {
				condition=condition.replace(">", "Min>");
			}
			if(condition.indexOf("<")>0) {
				condition=condition.replace("<", "Min<");
			}
		}
		
		conAndReverseCon.add(condition);
		conAndReverseCon.add(reverseCondition);
		return conAndReverseCon;
		
	}
	//获得设备状态条件和反转条件
	//////String[0]-----condition String[1]-----reverseCondition
	public List<String> getConAndReverseCon(String trigger,List<TemplGraph> templGraphs){
		List<String> conAndReverseCon=new ArrayList<String>();
		String reverseCondition=null;
		String condition=null;
		String device=null;
		String state=null;
		if(trigger.indexOf(".")>0) {
			state=trigger.substring(trigger.indexOf(".")).substring(".".length());
			device=trigger.substring(0, trigger.indexOf("."));
		}
		//找到该设备状态对应的值/condition
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.name.equals(device)) {
				for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
					if(templGraphNode.name.equals(state)) {
						if(templGraphNode.inTransitions!=null) {
							if(templGraphNode.inTransitions.get(0).assignment!=null) {
								String[] assignments=templGraphNode.inTransitions.get(0).assignment.split(",");
								//设备参数，首字母小写
								String deviceParameter=device.substring(0, 1).toLowerCase()+device.substring(1);
								for(String assignment:assignments) {
									if(assignment.indexOf(deviceParameter)>=0) {
										condition=assignment;
										break;
									}
								}
							}
						}						
						break;
					}
				}				
				break;
			}
		}
		if(condition!=null && condition.indexOf("=")>0) {
			reverseCondition=condition.replace("=", "!=");
			condition=condition.replace("=", "==");
		}
		conAndReverseCon.add(condition);
		conAndReverseCon.add(reverseCondition);
		return conAndReverseCon;
	}
	
	
	
	//属性值反转条件
	public String getReverseCondition(String trigger) {
		String reverseCondition=null;
		String condition=trigger;
		if(trigger.indexOf(".")>0) {
			condition=trigger.substring(trigger.indexOf(".")).substring(".".length());
		}
		if(condition.indexOf(">=")>0) {
			reverseCondition=condition.replace(">=", "<");
		}else if(condition.indexOf(">")>0) {
			reverseCondition=condition.replace(">", "<=");
		}else if(condition.indexOf("<=")>0) {
			reverseCondition=condition.replace("<=", ">");
		}else if(condition.indexOf("<")>0) {
			reverseCondition=condition.replace("<", ">=");
		}else if(condition.indexOf("=")>0) {
			reverseCondition=condition.replace("=", "!=");
		}
		//距离感应
		if(reverseCondition.indexOf("distanceFrom")>=0) {
			if(reverseCondition.indexOf(">")>0) {
				reverseCondition=reverseCondition.replace(">", "Min>");
			}
			if(reverseCondition.indexOf("<")>0) {
				reverseCondition=reverseCondition.replace("<","Min<");
			}
		}
		return reverseCondition;
	}
	
	//设备状态反转条件
	public String getReverseCondition(String trigger,List<TemplGraph> templGraphs) {
		String reverseCondition=null;
		String condition=null;
		String device=null;
		String state=null;
		if(trigger.indexOf(".")>0) {
			state=trigger.substring(trigger.indexOf(".")).substring(".".length());
			device=trigger.substring(0, trigger.indexOf("."));
		}
		//找到该设备状态对应的值/condition
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.name.equals(device)) {
				for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
					if(templGraphNode.name.equals(state)) {
						if(templGraphNode.inTransitions!=null) {
							if(templGraphNode.inTransitions.get(0).assignment!=null) {
								String[] assignments=templGraphNode.inTransitions.get(0).assignment.split(",");
								//设备参数，首字母小写
								String deviceParameter=device.substring(0, 1).toLowerCase()+device.substring(1);
								for(String assignment:assignments) {
									if(assignment.indexOf(deviceParameter)>=0) {
										condition=assignment;
										break;
									}
								}
							}
						}
						
						break;
					}
				}
				
				break;
			}
		}
		if(condition!=null && condition.indexOf("=")>0) {
			reverseCondition=condition.replace("=", "!=");
		}
		
		return reverseCondition;
	}
	
	//属性值条件
	public String getCon(String trigger) {
		String condition=trigger;
		if(trigger.indexOf(".")>0) {
			condition=trigger.substring(trigger.indexOf(".")).substring(".".length());
		}
		if(condition.indexOf(">")<0||condition.indexOf("<")<0) {
			if(condition.indexOf("=")>0) {
				condition=condition.replace("=", "==");
			}
		}
		if(condition.indexOf("distanceFrom")>=0) {
			if(condition.indexOf(">")>0) {
				condition=condition.replace(">", "Min>");
			}
			if(condition.indexOf("<")>0) {
				condition=condition.replace("<", "Min<");
			}
		}
		
		return condition;
	}
	
	//设备状态条件
	public String getCon(String trigger,List<TemplGraph> templGraphs) {
		String condition=null;
		String device=null;
		String state=null;
		if(trigger.indexOf(".")>0) {
			state=trigger.substring(trigger.indexOf(".")).substring(".".length());
			device=trigger.substring(0, trigger.indexOf("."));
		}
		//找到该设备状态对应的值/condition
		for(TemplGraph templGraph:templGraphs) {
			if(templGraph.name.equals(device)) {
				for(TemplGraphNode templGraphNode:templGraph.templGraphNodes) {
					if(templGraphNode.name.equals(state)) {
						if(templGraphNode.inTransitions!=null) {
							if(templGraphNode.inTransitions.get(0).assignment!=null) {
								String[] assignments=templGraphNode.inTransitions.get(0).assignment.split(",");
								//设备参数，首字母小写
								String deviceParameter=device.substring(0, 1).toLowerCase()+device.substring(1);
								for(String assignment:assignments) {
									if(assignment.indexOf(deviceParameter)>=0) {
										condition=assignment;
										break;
									}
								}
							}
						}
						
						break;
					}
				}
				
				break;
			}
		}
		
		if(condition!=null && condition.indexOf("=")>0) {
			condition.replace("=", "==");
		}
		return condition;
	}
	
	///////////////////////////////////2020.12.17//////////////////////////////////////////
	
	//获得条件的转置
	public String getReverseCon(String trigger) {
		String reverse=null;
		String attribute=null;
		if(trigger.indexOf(".")>0) {
			attribute=trigger.substring(trigger.indexOf(".")).substring(".".length());
		}
		if(attribute.indexOf(">=")>0) {
			reverse=attribute.replace(">=","<");
		}else if(attribute.indexOf(">")>0) {
			reverse=attribute.replace(">", "<=");
		}else if(attribute.indexOf("<=")>0) {
			reverse=attribute.replace("<=", ">");
		}else if(attribute.indexOf("<")>0) {
			reverse=attribute.replace("<", ">=");
		}else if(attribute.indexOf("=")>0) {
			reverse=attribute.replace("=", "!=");
		}else {
			String device=trigger.substring(0,trigger.indexOf("."));
			device=device.substring(0, 1).toLowerCase()+device.substring(1);
			String state=trigger.substring(trigger.indexOf(".")).substring(".".length());
			if(state.indexOf("hotOn")>=0) {
				reverse=device+"!=2";
			}else if(state.indexOf("coldOn")>=0) {
				reverse=device+"!=1";
			}else if(state.indexOf("aoff")>=0) {
				reverse=device+"!=0";
			}else if(state.indexOf("on")>=0) {
				reverse=device+"!=1";
			}else if(state.indexOf("off")>=0) {
				reverse=device+"!=0";
			}
		}
		if(reverse.indexOf("distanceFrom")>=0) {
			if(reverse.indexOf(">")>0) {
				reverse=reverse.replace(">", "Min>");
			}
			if(reverse.indexOf("<")>0) {
				reverse=reverse.replace("<","Min<");
			}
		}
		return reverse;
	}
	
	//获得条件
	public String getCondition(String trigger) {
		String condition=null;
		String attribute=null;
		if(trigger.indexOf(".")>0) {
			attribute=trigger.substring(trigger.indexOf(".")).substring(".".length());
		}
		if(attribute.indexOf(">")>0||attribute.indexOf("<")>0) {
			condition=attribute;
		}else if(attribute.indexOf("=")>0) {
			condition=attribute.replace("=", "==");
		}else {
			String device=trigger.substring(0,trigger.indexOf("."));
			device=device.substring(0, 1).toLowerCase()+device.substring(1);
			String state=trigger.substring(trigger.indexOf(".")).substring(".".length());
			if(state.indexOf("hotOn")>=0) {
				condition=device+"==2";
			}else if(state.indexOf("coldOn")>=0) {
				condition=device+"==1";
			}else if(state.indexOf("aoff")>=0) {
				condition=device+"==0";
			}else if(state.indexOf("on")>=0) {
				condition=device+"==1";
			}else if(state.indexOf("off")>=0) {
				condition=device+"==0";
			}
		}
		if(condition.indexOf("distanceFrom")>=0) {
			if(condition.indexOf(">")>0) {
				condition=condition.replace(">", "Min>");
			}
			if(condition.indexOf("<")>0) {
				condition=condition.replace("<", "Min<");
			}
		}
		return condition;
	}
	
	//删除rule模型
	@SuppressWarnings("unchecked")
	public void deleteModel(String xmlPath,String name) throws DocumentException, IOException {
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(xmlPath));
		Element rootElement=document.getRootElement();
		List<Element> templateElements=rootElement.elements("template");
		for(Element templateElement:templateElements) {
			Element nameElement=templateElement.element("name");
			if(name.equals("Rule")) {
				if(nameElement.getTextTrim().indexOf(name)>=0) {
					rootElement.remove(templateElement);
				}
			}else {
				if(nameElement.getTextTrim().equals(name)) {
					rootElement.remove(templateElement);
				}
			}
			
		}
		OutputStream os=new FileOutputStream(xmlPath);
		OutputFormat format=OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		format.setTrimText(false);
		format.setNewlines(false);
		XMLWriter writer=new XMLWriter(os,format);
		writer.write(document);
		writer.close();
		os.close();
	}
	
	public void finalDetermine(List<TemplGraph> templGraphs) {
		if(templGraphs.size()==2) {
			int[] nodeNum=new int[2];
	 		
	 		for(int i=0;i<templGraphs.size();i++) {
	 			//判断是否覆盖了所有节点
	 			TemplGraph t=templGraphs.get(i);
	 			int count=0;
	 			for(TemplGraphNode node:t.templGraphNodes) {
	 				if(node.inTransitions.size()>0||node.id.equals(t.init)) {
	 					count++;
	 				}
	 			}
	 			nodeNum[i]=count;
	 		}
	 		if(nodeNum[0]==templGraphs.get(0).templGraphNodes.size()||nodeNum[1]==templGraphs.get(0).templGraphNodes.size()) {
	 			if(nodeNum[0]==nodeNum[1]) {
	 				//如果都覆盖了则选择probability最大的模板
	 				for(int i=0;i<templGraphs.get(0).templGraphNodes.size();i++) {
	 					if(templGraphs.get(0).templGraphNodes.get(i).style.equals("branchpoint")) {
	 						TemplTransition outTransition0=templGraphs.get(0).templGraphNodes.get(i).outTransitions.get(0);
	 						TemplTransition outTransition1=templGraphs.get(1).templGraphNodes.get(i).outTransitions.get(0);
	 						
	 						if(!outTransition0.node.id.equals(outTransition1.node.id)) {
	 							String probability0=outTransition0.probability;
	 	 						String probability1=outTransition1.probability;
	 	 						int p0=Integer.parseInt(probability0);
	 	 						int p1=Integer.parseInt(probability1);
	 	 						if(p0>=p1) {
	 	 							templGraphs.remove(1);
	 	 						}else {
	 	 							templGraphs.remove(0);
	 	 						}
	 	 						
	 						}
	 					}
	 				}
	 			}else if(nodeNum[0]>nodeNum[1]) {
	 				templGraphs.remove(1);
	 			}else {
	 				templGraphs.remove(0);
	 			}
	 		} 
		}
	}

}
