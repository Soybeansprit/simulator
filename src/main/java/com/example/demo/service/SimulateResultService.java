package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.example.demo.bean.DataTimeValue;
import com.example.demo.bean.Scene;
import com.example.demo.bean.SceneChild;
import com.example.demo.bean.ScenesTree;


@Service
public class SimulateResultService {

	public static void main(String[] args) throws IOException, DocumentException {
		// TODO Auto-generated method stub
		SimulateResultService simulateResultService=new SimulateResultService();
		GenerateSysDeclaration gSysDeclaration=new GenerateSysDeclaration();
		String initModelName="exp0108";	
		String filePath="D:\\exp";
		String uppaalBinPath="D:\\tools\\uppaal-4.1.24\\uppaal-4.1.24\\bin-Windows";
		int treeSize=84;
		List<String> modelNames=new ArrayList<String>();
		modelNames.add(initModelName+"-final-ifd-scene");
		for(int i=0;i<treeSize;i++) {
			modelNames.add(initModelName+"-final-random-scene"+i);
		}
		int dataNum=gSysDeclaration.getSimulateDataNum(filePath+"\\"+initModelName+"-final-ifd-scene");
		simulateResultService.getAllSimulateResultFile(filePath, modelNames, uppaalBinPath,dataNum);

	}
	
	

	
	public void getSimulateResultFile(String filePath,String modelName,String uppaalBinPath,int dataNum) throws IOException {
		CMD cmd=new CMD();
		CsvToTxt ct=new CsvToTxt();
		String modelPathStr=filePath+"\\"+modelName;
		File simulateResultFile=new File(modelPathStr+".txt");
		if(simulateResultFile.exists()) {
			return;
		}
		cmd.gCsvFile(uppaalBinPath, modelPathStr+".xml");
		String pathStr=modelPathStr+".xml-q0-e";
		List<String> pathList=new ArrayList<String>();
		for(int j=0;j<dataNum;j++) {
			pathList.add(pathStr+j+".csv");
		}
		ct.changeFile(pathList, modelPathStr+".txt");
		ct.deleteFile(pathList);
		ct.deleteFile(modelPathStr+".xml-q0.plot");
	}
	
	public void getAllSimulateResultFile(String filePath,List<String> modelNames,String uppaalBinPath,int dataNum) throws IOException {
		CMD cmd=new CMD();
		CsvToTxt ct=new CsvToTxt();
		for(String modelName:modelNames) {
			String modelPathStr=filePath+"\\"+modelName;
			File simulateResultFile=new File(modelPathStr+".txt");
			if(simulateResultFile.exists()) {
				continue;
			}
			cmd.gCsvFile(uppaalBinPath, modelPathStr+".xml");
			String pathStr=modelPathStr+".xml-q0-e";
			List<String> pathList=new ArrayList<String>();
			for(int j=0;j<dataNum;j++) {
				pathList.add(pathStr+j+".csv");
			}
			ct.changeFile(pathList, modelPathStr+".txt");
			ct.deleteFile(pathList);
			ct.deleteFile(modelPathStr+".xml-q0.plot");
		}
	}

}
