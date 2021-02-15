package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class SimulateResultService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public void getSimulateResultFile(String filePath,String modelName,String uppaalBinPath) throws IOException {
		CMD cmd=new CMD();
		CsvToTxt ct=new CsvToTxt();
		String modelPathStr=filePath+"\\"+modelName;
		cmd.gCsvFile(uppaalBinPath, modelPathStr+".xml");
		String pathStr=modelPathStr+".xml-q0-e";
		List<String> pathList=new ArrayList<String>();
		for(int j=0;j<=47;j++) {
			pathList.add(pathStr+j+".csv");
		}
		ct.changeFile(pathList, modelPathStr+".txt");
		ct.deleteFile(pathList);
		ct.deleteFile(modelPathStr+".xml-q0.plot");
	}
	
	public void getAllSimulateResultFile(String filePath,List<String> modelNames,String uppaalBinPath) throws IOException {
		CMD cmd=new CMD();
		CsvToTxt ct=new CsvToTxt();
		for(String modelName:modelNames) {
			String modelPathStr=filePath+"\\"+modelName;
			cmd.gCsvFile(uppaalBinPath, modelPathStr+".xml");
			String pathStr=modelPathStr+".xml-q0-e";
			List<String> pathList=new ArrayList<String>();
			for(int j=0;j<=47;j++) {
				pathList.add(pathStr+j+".csv");
			}
			ct.changeFile(pathList, modelPathStr+".txt");
			ct.deleteFile(pathList);
			ct.deleteFile(modelPathStr+".xml-q0.plot");
		}
	}

}
