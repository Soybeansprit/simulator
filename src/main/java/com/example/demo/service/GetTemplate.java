package com.example.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
@Service
public class GetTemplate {

	public static void main(String[] args) throws DocumentException {
//		// TODO Auto-generated method stub
//		String path1 = "D:\\window16.xml";
//		String path2="D:\\win16.xml";
//		//删除xml文件第二行
//		
//		GetTemplate parse=new GetTemplate();
//		
//		parse.deletLine(path1, path2, 2);
//		List<Template> templates=parse.getTemplate(path2);
//		List<ControlledDevice> controlledDevices=parse.getControlled(templates);
//		List<Sensor> sensors=parse.getSensor(templates);
//		Rain rain=parse.getRain(templates);
		
	}
//	////////////////sensor//////////////////////////////////////
//	class Sensor{
//		String name=null;
//		List<String> attributes=new ArrayList<String>();
//	}
//	//////////////controlled device/////////////////////////////
//	class ControlledDevice{
//		String name=null;
//		List<State> states=new ArrayList<State>();
//		List<Action> actions=new ArrayList<Action>();
//		}
//	
//	class State{
//		String name=null;
//		String id=null;
//		boolean init=false;
//		List<Effect> effects=new ArrayList<Effect>();
//		
//	}
//	
//	class Effect{
//		String attribute=null;
//		String method=null;
//		String  value=null;
//	}
//	
//	class Action{
//		String name=null;
//		String toState=null;
//		String fromState=null;
//	}
//	///////////////////////PeopleAndDist///////////////////////
//	
//	
//	///////////////////////rain////////////////////
//	class Rain{
//		List<RainState> rainState=new ArrayList<RainState>();
//	}
//	
//	class RainState{
//		String name=null;
//		String rainAttr=null;
//		List<RainEffect> rainEffect=new ArrayList<RainEffect>();
//	}
//	
//	class RainEffect{
//		String attribtue=null;
//		String method=null;
//		String value=null;
//	}
	
	
	////////////////Template///////////////////////////////
	public class Template{
		public String name=null; 
		public String declaration=null;
		public String parameter=null;
		public List<Location> locations= new ArrayList<Location>();
		public List<Branchpoint> branchpoints=new ArrayList<Branchpoint>();
		public String init=null;
		public List<Transition> transitions=new ArrayList<Transition>();
	}
	
	class Location{
		String id=null;
		String name=null;
		String invariant=null;
		String style=null;
	}
	
	class Branchpoint{
		String id=null;
	}
	
	class Transition{
		String sourceId=null;
		String targetId=null;
		List<Label> labels=new ArrayList<Label>();		
	}
	
	class Label{
		String kind=null;
		String content=null;
	}
//	//从template获得controlled devices
//	public List<ControlledDevice> getControlled(List<Template> templates){
//		List<ControlledDevice> controlledDevices=new ArrayList<ControlledDevice>();
//		if(templates!=null) {
//			
//			for(Template template:templates) {
//				if(template.declaration!=null) {
//					//controlled device
//					if(template.declaration.indexOf("controlled_device")>=0) {
//						ControlledDevice conDevice=new ControlledDevice();
//						//name
//						conDevice.name=template.name;
//						for(Location loc:template.locations) {
//							//states
//							State state=new State();
//							//state name
//							state.name=loc.name;
//							//state id
//							state.id=loc.id;
//							if(state.id.equals(template.init)){
//								state.init=true;
//							}
//							//state effects
//							Effect effect=new Effect();
//							if(loc.invariant!=null) {
//								List<String> attrs=splitStr(loc.invariant,"&&");
//								for(String attr:attrs) {
//									int index=attr.indexOf("'");
//									if(index>0) {
//										effect.method="rate";
//										effect.attribute=attr.substring(0, index);
//										effect.value=attr.substring(index).substring("'==".length());
//									}
//									state.effects.add(effect);
//								}
//								
//							}
//							
//							conDevice.states.add(state);
//						}
//						//actions
//						for(Transition tran:template.transitions) {
//							Action action=new Action();
//							for(State sta:conDevice.states) {
//								if(sta.id.equals(tran.sourceId)) {
//									action.fromState=sta.name;
//									
//								}
//								if(sta.id.equals(tran.targetId)) {
//									action.toState=sta.name;
//								}
//							}
//							for(Label label:tran.labels) {
//								if(label.kind.equals("synchronisation")) {
//									int index0=label.content.indexOf("[");
//									if(index0>0) {
//										action.name=label.content.substring(0, index0);
//									}else {
//										int index=label.content.indexOf("?");
//										if(index>0) {
//											action.name=label.content.substring(0, index);
//										}
//									}									
//								}
//								if(label.kind.equals("assignment")) {
//									List<String> cons=splitStr(label.content, ",");
//									for(String con:cons) {
//										String[] attrs=con.split("=");
//										if(template.declaration.indexOf(attrs[0])==-1) {
//											//则说明是属性，添加到state的effect中。TODO
//											//寻找该state，target、toState
//											for(int i=0;i<conDevice.states.size();i++) {
//												if(conDevice.states.get(i).name.equals(action.toState)) {
//													Effect eff=new Effect();
//													eff.attribute=attrs[0];
//													eff.method="toValue";
//													eff.value=attrs[1];  
//													//不能保证是数值怎么处理，到时候处理时先判断是不是temp，如果是temp则影响为0
//													conDevice.states.get(i).effects.add(eff);
//												}
//											}
//											
//											
//										}
//										
//									}
//								}
//							}
//							conDevice.actions.add(action);
//						}
//						
//						controlledDevices.add(conDevice);
//					}
//				}
//			}
//		}
//		return controlledDevices;
//	}
//	
//	//从templates中获得sensors
//	public List<Sensor> getSensor(List<Template> templates){
//		List<Sensor> sensors=new ArrayList<Sensor>();
//		if(templates!=null) {
//			for(Template template:templates) {
//				if(template.declaration!=null) {
//					//sensor
//					if(template.declaration.indexOf("sensor")>=0) {
//						Sensor sensor=new Sensor();
//						sensor.name=template.name;
//						for(Transition tran:template.transitions) {
//							for(Label label:tran.labels) {
//								if(label.kind.equals("assignment")) {
//									List<String> attrs=splitStr(label.content, ",");
//									for(String attr:attrs) {
//										int index0=attr.indexOf("[");
//										if(index0>0) {
//											String attribute=attr.substring(0, index0);
//											sensor.attributes.add(attribute);
//										}else {
//											int index=attr.indexOf("=");
//											if(index>0) {
//												String attribute=attr.substring(0, index);
//												sensor.attributes.add(attribute);
//											}
//										}
//									}
//								}
//							}
//						}
//						sensors.add(sensor);
//					}
//				}
//			}
//		}
//		return sensors;
//	}
//	
//	//获得rain  TODO
//	public Rain getRain(List<Template> templates) {
//		Rain rain=new Rain();
//		GetTemplate parse=new GetTemplate();
//		for(Template template:templates) {
//			if(template.name.equals("Rain")) {
//				for(Location location:template.locations) {
//					if(location.name!=null) {
//						RainState rainState=new RainState();
//						rainState.name=location.name;
//						for(Transition transition:template.transitions) {
//							if(transition.targetId.equals(location.id)) {
//								for(Label label:transition.labels) {
//									if(label.kind.equals("assignment")) {
//										List<String> attrs=parse.splitStr(label.content, ",");
//										for(String attr:attrs) {
//											String[] val=attr.split("=");
//											if(val[0].equals("rain")) {
//												rainState.rainAttr=attr;
//											}else if(template.declaration.indexOf(val[0])<0) {
//												RainEffect rainEffect=new RainEffect();
//												rainEffect.attribtue=val[0];
//												rainEffect.method="toValue";
//												rainEffect.value=val[1];
//												int i=0;
//												for(;i<rainState.rainEffect.size();i++) {
//													if(rainState.rainEffect.get(i).attribtue.equals(rainEffect.attribtue)) {
//														break;
//													}
//												}
//												if(i==rainState.rainEffect.size()) {
//													rainState.rainEffect.add(rainEffect);
//												}
//												
//											}
//											
//										}
//									}
//								}
//							}
//						}
//						rain.rainState.add(rainState);
//					}
//				}
//			}
//		}
//		
//		return rain;
//	}
	
	public List<Template> getTemplate(File file) throws DocumentException{
		List<Template> templates=new ArrayList<Template>();
		if(file.getName().indexOf(".xml")>=0) {
			SAXReader reader= new SAXReader();
			Document document = reader.read(file);
			Element rootElement=document.getRootElement();
			System.out.println(rootElement.getName());
			
			List<Element> templateElements=rootElement.elements("template");
			for(Element tempElement:templateElements) {
				System.out.println(tempElement.getName());
				//解析获得template
				Template template=new Template();
				//name
				Element nameElement=tempElement.element("name");
				template.name=nameElement.getTextTrim();
				System.out.println(nameElement.getName());
				//declaration
				Element declarElement=tempElement.element("declaration");
				if(declarElement!=null) {
					template.declaration=declarElement.getText();
					System.out.println(declarElement.getName());
				}
				//parameter
				Element paraElement=tempElement.element("parameter");
				if(paraElement!=null) {
					template.parameter=paraElement.getTextTrim();
					System.out.println(paraElement.getName());
				}
				
				//locations
				List<Element> locaElements=tempElement.elements("location");
				
				for(Element locaElement:locaElements) {
					System.out.println(locaElement.getName());
					Location location=new Location();
					Attribute attr=locaElement.attribute("id");
					location.id=attr.getValue();
					System.out.println();
					System.out.println(location.id);
					System.out.println();
					Element locaNameElement=locaElement.element("name");
					if(locaNameElement!=null) {
						location.name=locaNameElement.getTextTrim();
						System.out.println(locaElement.getName());
					}
					Element invaElement=locaElement.element("label");
					if(invaElement!=null) {
						location.invariant=invaElement.getTextTrim();
						System.out.println(invaElement.getName());
					}
					Element urgentElement=locaElement.element("urgent");
					if(urgentElement!=null) {
						location.style="urgent";
					}
					Element committedElement=locaElement.element("committed");
					if(committedElement!=null) {
						location.style="committed";
					}
					if(urgentElement==null&&committedElement==null) {
						location.style="location";
					}
					template.locations.add(location);
				}
				//branchpoint
				List<Element> branElements=tempElement.elements("branchpoint");
				if(branElements!=null) {
					for(Element branElement:branElements) {
						Branchpoint branchpoint=new Branchpoint();
						Attribute attr=branElement.attribute("id");
						branchpoint.id=attr.getValue();
						template.branchpoints.add(branchpoint);
						System.out.println(branElement.getName());
					}
				}
				//init
				Element initElement=tempElement.element("init");
				Attribute initAttr=initElement.attribute("ref");
				template.init=initAttr.getValue();
				System.out.println(initElement.getName());
				//transitions
				List<Element> tranElements=tempElement.elements("transition");
				for(Element tranElement:tranElements) {
					System.out.println(tranElement.getName());
					Transition transition=new Transition();
					Element sourceElement=tranElement.element("source");
					Attribute sourAttr=sourceElement.attribute("ref");
					transition.sourceId=sourAttr.getValue();
					System.out.println(sourceElement.getName());
					Element targetElement=tranElement.element("target");
					Attribute tarAttr=targetElement.attribute("ref");
					transition.targetId=tarAttr.getValue();
					System.out.println(targetElement.getName());
					List<Element> labelElements=tranElement.elements("label");
					if(labelElements!=null) {
						for(Element labelElement:labelElements) {
							System.out.println(labelElement.getName());
							Label label=new Label();
							Attribute kindAttr=labelElement.attribute("kind");
							label.kind=kindAttr.getValue();
							label.content=labelElement.getTextTrim();
							transition.labels.add(label);
						}
					}
					template.transitions.add(transition);
				}
				//添加template
				templates.add(template);
			}

		}else {
			System.out.println("please upload .xml file");
		}
		return templates;
	}
	
	//解析xml文件，获得template
	public List<Template> getTemplate(String path) throws DocumentException{
		SAXReader reader= new SAXReader();
		Document document = reader.read(new File(path));
		Element rootElement=document.getRootElement();
//		System.out.println(rootElement.getName());
		
		List<Element> templateElements=rootElement.elements("template");
		List<Template> templates=new ArrayList<Template>();
		for(Element tempElement:templateElements) {
//			System.out.println(tempElement.getName());
			//解析获得template
			Template template=new Template();
			//name
			Element nameElement=tempElement.element("name");
			template.name=nameElement.getTextTrim();
//			System.out.println(nameElement.getName());
			//declaration
			Element declarElement=tempElement.element("declaration");
			if(declarElement!=null) {
				template.declaration=declarElement.getText();
//				System.out.println(declarElement.getName());
			}
			//parameter
			Element paraElement=tempElement.element("parameter");
			if(paraElement!=null) {
				template.parameter=paraElement.getTextTrim();
//				System.out.println(paraElement.getName());
			}
			
			//locations
			List<Element> locaElements=tempElement.elements("location");
			
			for(Element locaElement:locaElements) {
//				System.out.println(locaElement.getName());
				Location location=new Location();
				Attribute attr=locaElement.attribute("id");
				location.id=attr.getValue();
//				System.out.println();
//				System.out.println(location.id);
//				System.out.println();
				Element locaNameElement=locaElement.element("name");
				if(locaNameElement!=null) {
					location.name=locaNameElement.getTextTrim();
//					System.out.println(locaElement.getName());
				}
				Element invaElement=locaElement.element("label");
				if(invaElement!=null) {
					location.invariant=invaElement.getTextTrim();
//					System.out.println(invaElement.getName());
				}
				Element urgentElement=locaElement.element("urgent");
				if(urgentElement!=null) {
					location.style="urgent";
				}
				Element committedElement=locaElement.element("committed");
				if(committedElement!=null) {
					location.style="committed";
				}
				if(urgentElement==null&&committedElement==null) {
					location.style="location";
				}
				template.locations.add(location);
			}
			//branchpoint
			List<Element> branElements=tempElement.elements("branchpoint");
			if(branElements!=null) {
				for(Element branElement:branElements) {
					Branchpoint branchpoint=new Branchpoint();
					Attribute attr=branElement.attribute("id");
					branchpoint.id=attr.getValue();
					template.branchpoints.add(branchpoint);
//					System.out.println(branElement.getName());
				}
			}
			//init
			Element initElement=tempElement.element("init");
			Attribute initAttr=initElement.attribute("ref");
			template.init=initAttr.getValue();
//			System.out.println(initElement.getName());
			//transitions
			List<Element> tranElements=tempElement.elements("transition");
			for(Element tranElement:tranElements) {
//				System.out.println(tranElement.getName());
				Transition transition=new Transition();
				Element sourceElement=tranElement.element("source");
				Attribute sourAttr=sourceElement.attribute("ref");
				transition.sourceId=sourAttr.getValue();
//				System.out.println(sourceElement.getName());
				Element targetElement=tranElement.element("target");
				Attribute tarAttr=targetElement.attribute("ref");
				transition.targetId=tarAttr.getValue();
//				System.out.println(targetElement.getName());
				List<Element> labelElements=tranElement.elements("label");
				if(labelElements!=null) {
					for(Element labelElement:labelElements) {
//						System.out.println(labelElement.getName());
						Label label=new Label();
						Attribute kindAttr=labelElement.attribute("kind");
						label.kind=kindAttr.getValue();
						label.content=labelElement.getTextTrim();
						transition.labels.add(label);
					}
				}
				template.transitions.add(transition);
			}
			//添加template
			templates.add(template);
		}
		return templates;
	}
	
	
	
	
	//删除文档中的特定行，并写到新文档
	public void deletLine(String path1,String path2,int lineNum) {
		try {
			FileReader fr= new FileReader(path1);
			BufferedReader br=new BufferedReader(fr);
			File file = new File(path2);
			if(!file.exists()) {
				try {
					file.createNewFile();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
			
			String line="";
			int count=1;
			write(file,br.readLine(),false);
			count++;
			while((line=br.readLine())!=null) {
				if(count==lineNum) {
					write(file,"",true);
					count++;
					continue;
				}
				write(file,line,true);
				count++;
			}
			
			br.close();
			fr.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	//写文档
	public void write(File file, String line, boolean conti) throws IOException {
		Writer writer=new FileWriter(file, conti);
		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append(line+"\r\n");
		writer.write(stringBuilder.toString());
		writer.close();
	}
	
	//split
	public List<String> splitStr(String str,String splitStr){
		return Arrays.asList(str.split(splitStr));
	}
 	
	
	

}
