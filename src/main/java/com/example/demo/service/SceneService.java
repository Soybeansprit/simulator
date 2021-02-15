package com.example.demo.service;

import com.example.demo.bean.Scene;

public class SceneService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	////////////////////////////////场景分析///////////////////////////////
	public Scene getSceneAnalysis(String filePath,String modelName) {
		Scene scene=new Scene();
		String modelPathStr=filePath+"\\"+modelName;
		
		return scene;
	}

}
