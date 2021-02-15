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
		String ruleStrList="1. IF SmartHomeSecurity.homeMode AND temperature<=15 THEN M.heatonPulse\r\n" + 
				"\r\n" + 
				"2. IF SmartHomeSecurity.homeMode AND temperature>=30 THEN M.accoolPulse\r\n" + 
				"\r\n" + 
				"3. IF SmartHomeSecurity.homeMode AND humidity<20 THEN M.honPulse\r\n" + 
				"\r\n" + 
				"4. IF SmartHomeSecurity.homeMode AND humidity>=45 THEN M.hoffPulse\r\n" + 
				"\r\n" + 
				"5. IF SmartHomeSecurity.homeMode AND humidity>65 THEN M.fonPulse\r\n" + 
				"\r\n" + 
				"6. IF SmartHomeSecurity.homeMode AND temperature>28 THEN M.fonPulse\r\n" + 
				"\r\n" + 
				"7. IF SmartHomeSecurity.homeMode AND temperature<20 THEN M.foffPulse\r\n" + 
				"\r\n" + 
				"8. IF SmartHomeSecurity.homeMode AND rain=1 THEN M.phloffPulse for 1s,M.phlwhitePulse\r\n" + 
				"\r\n" + 
				"9. IF SmartHomeSecurity.homeMode AND temperature<=10 THEN M.phlbluePulse\r\n" + 
				"\r\n" + 
				"10. IF SmartHomeSecurity.homeMode AND leak=1 THEN M.phlbluePulse\r\n" + 
				"\r\n" + 
				"11. IF SmartHomeSecurity.awayMode THEN M.phloffPulse\r\n" + 
				"\r\n" + 
				"12. IF SmartHomeSecurity.homeMode THEN M.phlwhitePulse\r\n" + 
				"\r\n" + 
				"13. IF Door.dopen THEN M.phlwhitePulse\r\n" + 
				"\r\n" + 
				"14. IF SmartHomeSecurity.homeMode AND co2ppm>=1000 THEN M.phlredPulse\r\n" + 
				"\r\n" + 
				"15. IF SmartHomeSecurity.awayMode THEN M.foffPulse\r\n" + 
				"\r\n" + 
				"16. IF Door.dopen THEN M.heatoffPulse\r\n" + 
				"\r\n" + 
				"17. IF Window.wopen THEN M.heatoffPulse\r\n" + 
				"\r\n" + 
				"18. IF SmartHomeSecurity.awayMode THEN M.heatoffPulse,M.acoffPulse,M.foffPulse,M.bclosePulse,M.boffPulse\r\n" + 
				"\r\n" + 
				"19. IF SmartHomeSecurity.homeMode AND temperature<18 THEN M.acheatPulse\r\n" + 
				"\r\n" + 
				"20. IF SmartHomeSecurity.homeMode AND temperature>30 THEN M.accoolPulse\r\n" + 
				"\r\n" + 
				"21. IF SmartHomeSecurity.homeMode THEN M.rdockPulse\r\n" + 
				"\r\n" + 
				"22. IF SmartHomeSecurity.awayMode THEN M.rstartPulse\r\n" + 
				"\r\n" + 
				"23. IF SmartHomeSecurity.awayMode THEN M.wclosePulse,M.dclosePulse\r\n" + 
				"\r\n" + 
				"24. IF number>0 THEN M.homeModePulse\r\n" + 
				"\r\n" + 
				"25. IF number=0 THEN M.awayModePulse\r\n" + 
				"\r\n" + 
				"26. IF SmartHomeSecurity.homeMode AND temperature>28 THEN M.bopenPulse\r\n" + 
				"\r\n" + 
				"27. IF SmartHomeSecurity.homeMode THEN M.bonPulse\r\n" + 
				"\r\n" + 
				"28. IF SmartHomeSecurity.homeMode AND co2ppm>=800 THEN M.fonPulse,M.wopenPulse\r\n" + 
				"\r\n" + 
				"29. IF AirConditioner.cool THEN M.wclosePulse\r\n" + 
				"\r\n" + 
				"30. IF AirConditioner.heat THEN M.wclosePulse";
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
					String action=actions.get(i);
					action=action.substring(action.indexOf(".")).substring(".".length()).trim();
					actions.set(i, action);
				}
				rule.setAction(actions);
				rule.setTriggers(strJoint(triggers, "AND\\n"));
			}
			
			
		}
		return rule;
	}
	///////////////////////////获得所有规则//////////////////////////////////
	public List<Rule> getRuleList(String ruleTxt){
		List<String> ruleStrList=new ArrayList<String>();
		List<String> strList=Arrays.asList(ruleTxt.split("\r\n"));
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
