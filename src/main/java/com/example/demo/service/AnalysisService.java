package com.example.demo.service;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.bean.Rule;
@Service
public class AnalysisService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		

	}
	
	
	public void generateAllScenesModel(String ruleText,File file) {
		RuleService ruleService=new RuleService();
		List<Rule> rules=ruleService.getRuleList(ruleText);
		String fileName=file.getName();
		String storagePath="D:\\workspace";
		
	}

}
