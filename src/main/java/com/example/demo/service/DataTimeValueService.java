package com.example.demo.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.bean.DataFunction;
import com.example.demo.bean.DataTimeValue;
import com.example.demo.bean.Function;




public class DataTimeValueService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	//获得输出文件中的各属性时间点和值
	public List<DataTimeValue> getDataTimes(String dataFilePath) throws IOException{
		List<DataTimeValue> datas=new ArrayList<DataTimeValue>();
		
		FileReader fr=new FileReader(dataFilePath);
		BufferedReader br=new BufferedReader(fr);
		String str=br.readLine();
		while(str!=null) {
			if(str!=null && str.indexOf("#1")>0 ) {
				DataTimeValue data=new DataTimeValue();
				String name=str.substring(str.indexOf("#"), str.indexOf("#1")).substring("#".length()).replace(" ", "");
				data.name=name.trim();
				str=br.readLine();
				while(str!=null && str!="" && str.indexOf("#1")<0) {
					String splitValue[]=str.split(" ");
					double[] timeValue=new double[2];
					timeValue[0]=Double.parseDouble(splitValue[0]);
					timeValue[1]=Double.parseDouble(splitValue[1]);
					data.timeValue.add(timeValue);
					str=br.readLine();
				}
				
				datas.add(data);
			} else if(str!=null){
				str=br.readLine();
			}
		}
		br.close();
		fr.close();
		
		return datas;
	}
	
	
	//分析数据，获得函数
	public List<DataFunction> analysDatas(DataTimeValue data){
		List<DataFunction> dataFunctions=new ArrayList<DataFunction>();
		for(int i=0;i<data.timeValue.size()-1;) {
			int icount=i;
			DataFunction dataFunction=new DataFunction();
			Double down=data.timeValue.get(i)[0];
			dataFunction.downTime=down;
			Double up=data.timeValue.get(i)[0];
			Double downValue=data.timeValue.get(i)[1];
			Double upValue=data.timeValue.get(i)[1];
			for(int j=i+1;j<data.timeValue.size();j++) {
				Double up1=data.timeValue.get(j)[0];
				Double upValue1=data.timeValue.get(j)[1];
				
				if(Math.abs((upValue1-downValue))<0.0005) {
					//相等
					up=up1;
					upValue=upValue1;
					icount=j;
					if(j==data.timeValue.size()-1) {
						Double a=0.0;
						Double b=upValue;
						dataFunction.downTime=down;
						dataFunction.upTime=up;
						dataFunction.downValue=downValue;
						dataFunction.upValue=upValue;
						Function function=new Function();
						function.a=a;
						function.b=b;
						dataFunction.function=function;
						dataFunctions.add(dataFunction);
						i=icount;
					}
				}else {					
					if(j==i+1) {
						up=up1;
						upValue=upValue1;
						icount=j;
					}
					Double a=null;
					Double b=null;
					dataFunction.downTime=down;
					dataFunction.upTime=up;
					dataFunction.downValue=downValue;
					dataFunction.upValue=upValue;
					if((up-down)>0.1) {
						a=(upValue-downValue)/(up-down);
						b=(downValue-a*down);
					}else {
						a=downValue;
						b=upValue;
					}
					Function function=new Function();
					function.a=a;
					function.b=b;
					dataFunction.function=function;
					dataFunctions.add(dataFunction);
					i=icount;
					break;
				}
			}
		}
		return dataFunctions;
	}

}
