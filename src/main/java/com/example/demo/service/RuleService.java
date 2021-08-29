package com.example.demo.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.bean.Rule;

@Service
public class RuleService {
	
	public static void main(String[] args) throws IOException {
		RuleService ruleService=new RuleService();
		String ruleStr="IF SmartHomeSecurity.homeMode AND rain=1 THEN M.phloffPulse for 1s,M.phlwhitePulse";
		Rule rule=ruleService.getRule(ruleStr, 1);
		String ruleStrList="1. IF SmartHomeSecurity.homeMode AND temperature<=15 THEN Heater.turn_heat_on\r\n" + 
				"\r\n" + 
				"2. IF SmartHomeSecurity.homeMode AND temperature>=30 THEN AirConditioner.turn_ac_cool\r\n" + 
				"\r\n" + 
				"3. IF SmartHomeSecurity.homeMode AND humidity<20 THEN Humidifier.turn_hum_on\r\n" + 
				"\r\n" + 
				"4. IF SmartHomeSecurity.homeMode AND humidity>=45 THEN Humidifier.turn_hum_off\r\n" + 
				"\r\n" + 
				"5. IF SmartHomeSecurity.homeMode AND humidity>65 THEN Fan.turn_fan_on\r\n" + 
				"\r\n" + 
				"6. IF SmartHomeSecurity.homeMode AND temperature>28 THEN Fan.turn_fan_on\r\n" + 
				"\r\n" + 
				"7. IF SmartHomeSecurity.homeMode AND temperature<20 THEN Fan.turn_fan_off\r\n" + 
				"\r\n" + 
				"8. IF SmartHomeSecurity.homeMode AND rain=1 THEN PhilipsHueLight.turn_phl_white\r\n" + 
				"\r\n" + 
				"9. IF SmartHomeSecurity.homeMode AND temperature<=10 THEN PhilipsHueLight.turn_phl_blue\r\n" + 
				"\r\n" + 
				"10. IF SmartHomeSecurity.homeMode AND leak=1 THEN PhilipsHueLight.turn_phl_blue\r\n" + 
				"\r\n" + 
				"11. IF SmartHomeSecurity.awayMode THEN PhilipsHueLight.turn_phl_off\r\n" + 
				"\r\n" + 
				"12. IF SmartHomeSecurity.homeMode THEN PhilipsHueLight.turn_phl_white\r\n" + 
				"\r\n" + 
				"13. IF Door.dopen THEN PhilipsHueLight.turn_phl_white\r\n" + 
				"\r\n" + 
				"14. IF SmartHomeSecurity.homeMode AND co2ppm>=1000 THEN PhilipsHueLight.turn_phl_red\r\n" + 
				"\r\n" + 
				"15. IF SmartHomeSecurity.awayMode THEN Fan.turn_fan_on\r\n" + 
				"\r\n" + 
				"16. IF Door.dopen THEN Heater.turn_heat_off\r\n" + 
				"\r\n" + 
				"17. IF Window.wopen THEN Heater.turn_heat_off\r\n" + 
				"\r\n" + 
				"18. IF SmartHomeSecurity.awayMode THEN Heater.turn_heat_off,AirConditioner.turn_ac_off,Fan.turn_fan_off,Blind.close_blind,Bulb.turn_bulb_off\r\n" + 
				"\r\n" + 
				"19. IF SmartHomeSecurity.homeMode AND temperature<18 THEN AirConditioner.turn_ac_heat\r\n" + 
				"\r\n" + 
				"20. IF SmartHomeSecurity.homeMode AND temperature>30 THEN AirConditioner.turn_ac_cool\r\n" + 
				"\r\n" + 
				"21. IF SmartHomeSecurity.homeMode THEN Robot.dock_robot\r\n" + 
				"\r\n" + 
				"22. IF SmartHomeSecurity.awayMode THEN Robot.start_robot\r\n" + 
				"\r\n" + 
				"23. IF SmartHomeSecurity.awayMode THEN Window.close_window,Door.close_door\r\n" + 
				"\r\n" + 
				"24. IF number>0 THEN SmartHomeSecurity.turn_sms_home\r\n" + 
				"\r\n" + 
				"25. IF number=0 THEN SmartHomeSecurity.turn_sms_away\r\n" + 
				"\r\n" + 
				"26. IF SmartHomeSecurity.homeMode AND temperature>28 THEN Blind.open_blind\r\n" + 
				"\r\n" + 
				"27. IF SmartHomeSecurity.homeMode THEN Bulb.turn_on_bulb\r\n" + 
				"\r\n" + 
				"28. IF SmartHomeSecurity.homeMode AND co2ppm>=800 THEN Fan.turn_on_fan,Window.open_window\r\n" + 
				"\r\n" + 
				"29. IF AirConditioner.cool THEN Window.close_window\r\n" + 
				"\r\n" + 
				"30. IF AirConditioner.heat THEN Window.close_window";
		System.out.println(rule);
		List<Rule> rules=ruleService.getRuleList(ruleStrList);
		System.out.println(rules);
		
		String rulePath="D:\\rules0105new.txt";
		List<Rule> txtRules=ruleService.getRuleListFromTxt(rulePath);
		System.out.println(txtRules);
	}

	//////////////////////////////解析rule//////////////////////////////
	public Rule getRule(String ruleStr,int num) {
		Rule rule=null;
		ruleStr=ruleStr.trim();
		if(ruleStr!=null && ruleStr.toUpperCase().indexOf("IF ")>=0 &&
				ruleStr.toUpperCase().indexOf(" THEN ")>0) {
			rule=new Rule();
			int ifIndex;
			int thenIndex=0;
			if(ruleStr.indexOf("IF ")>=0) {
				ifIndex=ruleStr.indexOf("IF ");
			}else if(ruleStr.indexOf("if ")>=0) {
				ifIndex=ruleStr.indexOf("if ");
			}else if(ruleStr.indexOf("If ")>=0) {
				ifIndex=ruleStr.indexOf("If");
			}else {
				ifIndex=ruleStr.indexOf("iF ");
			}
			if(ruleStr.indexOf(" THEN ")>0) {
				thenIndex=ruleStr.indexOf(" THEN ");
			}else if(ruleStr.indexOf(" then ")>0) {
				thenIndex=ruleStr.indexOf(" then ");
			}
			if(ifIndex>=thenIndex) {
				System.out.println(ruleStr+"\n    not rule!");
				rule=null;
			}else {
				
				rule.setRuleName("rule"+num);
				rule.setRuleContent(ruleStr);
				String triggerStr=ruleStr.substring(ifIndex, thenIndex).substring("IF ".length()).trim();
				String actionStr=ruleStr.substring(thenIndex).substring(" then ".length()).trim();
				List<String> triggers=Arrays.asList(triggerStr.split("AND"));
				List<String> actions=Arrays.asList(actionStr.split(","));
				for(int i=0;i<triggers.size();i++) {
					String trigger=triggers.get(i).trim();
					triggers.set(i, trigger);					
				}
				rule.setTrigger(triggers);
				for(int i=0;i<actions.size();i++) {
					String action=actions.get(i).trim();
//					action=action.substring(action.indexOf(".")).substring(".".length()).trim();   2021/5/31更改，for newTAP
					actions.set(i, action);
				}
				rule.setAction(actions);
				rule.setTriggers(strJoint(triggers, "AND\\n"));
			}
			
			
		}
		return rule;
	}
	

	
	public List<Rule> getRuleList(List<String> ruleTextLines){
		List<Rule> rules=new ArrayList<Rule>();
		List<String> ruleStrList=new ArrayList<String>();;
		for(String ruleTextLine:ruleTextLines) {
			if(ruleTextLine!=null && ruleTextLine.toUpperCase().indexOf("IF ")>=0 &&
					ruleTextLine.toUpperCase().indexOf(" THEN ")>0) {
				ruleTextLine=ruleTextLine.substring(ruleTextLine.toUpperCase().indexOf("IF "));
				ruleStrList.add(ruleTextLine);
			}
		}
		for(int i=0;i<ruleStrList.size();i++) {
			String ruleStr=ruleStrList.get(i);
			Rule rule=getRule(ruleStr, i+1);
			if(rule!=null) {
				rules.add(rule);
			}			
		}
		return rules;
	}
	
	///////////////////////////获得所有规则//////////////////////////////////
	public List<Rule> getRuleList(String ruleTxt){
		List<String> ruleStrList=new ArrayList<String>();
		int changeLine=0;
		if(ruleTxt.indexOf("\r\n")>0) {
			changeLine=1;
		}else if(ruleTxt.indexOf("\n")>0) {
			changeLine=2;
		}
		//////////////////////换行了才能算///////////////////////
		List<String> strList=new ArrayList<String>();
		if(changeLine==1) {
			strList=Arrays.asList(ruleTxt.split("\r\n"));
		}else if(changeLine==2) {
			strList=Arrays.asList(ruleTxt.split("\n"));
		}
		for(String str:strList) {
			if(str!=null && str.toUpperCase().indexOf("IF ")>=0 &&
					str.toUpperCase().indexOf(" THEN ")>0) {
				ruleStrList.add(str);
			}
		}
		List<Rule> rules=new ArrayList<Rule>();
		for(int i=0;i<ruleStrList.size();i++) {
			String ruleStr=ruleStrList.get(i);
			Rule rule=getRule(ruleStr, i+1);
			if(rule!=null) {
				rules.add(rule);
			}			
		}
		return rules;
	}
	
	public List<Rule> getRuleListFromTxt(String path){
		List<String> ruleStrList=new ArrayList<String>();
		List<Rule> rules=new ArrayList<Rule>();
		try {
			FileReader fr=new FileReader(path);
			BufferedReader br=new BufferedReader(fr);
			
			String str="";
			while((str=br.readLine())!=null) {
				if(str!=null && str.toUpperCase().indexOf("IF ")>=0 &&
						str.toUpperCase().indexOf(" THEN ")>0) {
					ruleStrList.add(str);
				}
			}
			
			br.close();
			fr.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		for(int i=0;i<ruleStrList.size();i++) {
			String ruleStr=ruleStrList.get(i);
			Rule rule=getRule(ruleStr, i+1);
			if(rule!=null) {
				rules.add(rule);
			}			
		}		
		return rules;
	}
	
	///////////用字符串连接////////////////////
	public String strJoint(List<String> strs,String separator) {
		StringBuilder str=new StringBuilder();
		for(int i=0;i<strs.size();i++) {
			str.append(strs.get(i));
			if(i<strs.size()-1) {
				str.append(" "+separator+" ");
			}
		}
		return str.toString();
	}
	
	
}
