package com.example.demo.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvToTxt {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CsvToTxt ct=new CsvToTxt();
		CMD cmd=new CMD();
		String sourcePath="D:\\tools\\uppaal-4.1.24\\uppaal-4.1.24\\bin-Windows";
		String str="D:\\exp\\exp01083-";
		///////////////////////////最终模型的xml文件////////////////////////////////
		List<String> xmlPaths=new ArrayList<String>();
		for(int i=0;i<=83;i++) {
			xmlPaths.add(str+i+".xml");
		}
		///////////////////////////////////////////////////////////////////////
		
		////////////////////////////对每个.xml文件进行仿真，获得各个数据的多个.////////////////////
		/////////////////////csv文件并将csv文件合并转为对应的仿真数据txt文件和规则触发txt文件///////////
		for(int i=0;i<=83;i++) {
			String xmlPath=xmlPaths.get(i);
			cmd.gCsvFile(sourcePath, xmlPath);
			String pathStr=str+i+".xml-q0-e";
			List<String> pathList=new ArrayList<String>();
			for(int j=0;j<=47;j++) {
				pathList.add(pathStr+j+".csv");
			}
			ct.changeFile(pathList, str+i+".txt",str+i+"-other.txt");
			ct.deleteFile(pathList);
			ct.deleteFile(str+i+".xml-q0.plot");
		}
		
		ct.changeFile("D:\\exp01083-ifd.txt", "D:\\exp01083-ifd-other.txt");
		//////////////////////////////////////////////////////////////////////////////

	}
	public void changeFile(String txtPath,String otherTxtPath) {
		String text="";
		
		File csvFile=new File(txtPath);
		if(csvFile.exists()) {
			try {
				FileInputStream fileInputStream = new FileInputStream(csvFile);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                 
                
                
                String str = null;
                List<String> ruleStrs=new ArrayList<String>();
                str = bufferedReader.readLine();
                boolean isRule=false;
                while((str = bufferedReader.readLine()) != null){
                	
                	if(str.indexOf("#1")>=0) {
                		if(str.indexOf("rule")>=0) {
                			
                			isRule=true;
                			System.out.println(str);
                    		str=str.replace("#1", "");
                    		str=str.replace("#", "");
                    		str=str.trim();
                    		//ruleName
                    		ruleStrs.add(str);
                    		
                    		
                		}else {
                			isRule=false;
                		}
                		
                		
                	}else {
                		if(isRule) {
                			//timeValue
                			str=str.replace(" ", ",");
                    		str="["+str+"],";
                    		ruleStrs.add(str);
                		}
                		
                	}
                	
                    
                    
                }
                str="";
                for(int i=0;i<ruleStrs.size();i++) {
                	if(ruleStrs.get(i).indexOf("rule")>=0) {
                		str=str+"{\r\nruleName: '"+ruleStrs.get(i)+"',\r\ntimeValue:[\r\n";
                	}else {
                		str=str+ruleStrs.get(i)+"\r\n";
                		if(i<ruleStrs.size()-1) {
                			if(ruleStrs.get(i+1).indexOf("rule")>=0) {
                				str=str+"]\r\n},\r\n";
                			}
                		}else {
                			str=str+"]\r\n}\r\n";
                		}
                	}
                }
                text=str;
                
                inputStreamReader.close();
                
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		
		
		File otherTxtFile=new File(otherTxtPath);
        FileOutputStream fileOutputStream=null;
        try {
        	if(!otherTxtFile.exists()) {
        		otherTxtFile.createNewFile();
        	}
        	fileOutputStream=new FileOutputStream(otherTxtFile);
        	fileOutputStream.write(text.getBytes());
        	fileOutputStream.flush();
        	fileOutputStream.close();
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
	}
	
	public void changeFile(List<String> csvPaths,String csvTxtPath,String otherTxtPath) throws IOException {
		String text="";
		String otherText="";
		for(String csvPath:csvPaths) {
			File csvFile=new File(csvPath);
			if(csvFile.exists()) {
				try {
					FileInputStream fileInputStream = new FileInputStream(csvFile);
	                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
	                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	                 
	                StringBuffer sb = new StringBuffer();
	                StringBuffer othersb=new StringBuffer();
	                String str = null;
	                String otherStr=null;
	                while((str = bufferedReader.readLine()) != null){
	                	otherStr=str;
	                	if(str.indexOf("# time,")>=0) {
	                		System.out.println(str);
	                		str=str.replace("# time,", "# ");
	                		str=str+" #1";
	                		otherStr=otherStr.replace("# time,", "ruleName:'");
	                		otherStr=otherStr+"',";
	                	}
	                	if(str.indexOf(",")>0) {
	                		str=str.replace(",", " ");
	                		otherStr="["+otherStr+"],";
	                	}
	                    sb.append(str+"\r\n");
	                    othersb.append(otherStr+"\r\n");
	                }
	                text=text+sb.toString();
	                otherText=otherText+othersb.toString();
	                inputStreamReader.close();
	                
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		
		
		File csvTxtFile=new File(csvTxtPath);
		File otherTxtFile=new File(otherTxtPath);
        FileOutputStream fileOutputStream=null;
        try {
        	if(!csvTxtFile.exists()) {
        		csvTxtFile.createNewFile();
        	}
        	fileOutputStream=new FileOutputStream(csvTxtFile);
        	fileOutputStream.write(text.getBytes());
        	fileOutputStream.flush();
        	fileOutputStream.close();
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
        
        try {
        	if(!otherTxtFile.exists()) {
        		otherTxtFile.createNewFile();
        	}
        	fileOutputStream=new FileOutputStream(otherTxtFile);
        	fileOutputStream.write(otherText.getBytes());
        	fileOutputStream.flush();
        	fileOutputStream.close();
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
	}
	
	public void changeFile(List<String> csvPaths,String csvTxtPath) throws IOException {
		String text="";
		for(String csvPath:csvPaths) {
			File csvFile=new File(csvPath);
			if(csvFile.exists()) {
				try {
					FileInputStream fileInputStream = new FileInputStream(csvFile);
	                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
	                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	                 
	                StringBuffer sb = new StringBuffer();
	                String str = null;
	                while((str = bufferedReader.readLine()) != null){
	                	if(str.indexOf("# time,")>=0) {
	                		System.out.println(str);
	                		str=str.replace("# time,", "# ");
	                		str=str+" #1";
	                	}
	                	if(str.indexOf(",")>0) {
	                		str=str.replace(",", " ");
	                	}
	                    sb.append(str+"\r\n");
	                }
	                text=text+sb.toString();
	                inputStreamReader.close();
	                
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		
		
		File csvTxtFile=new File(csvTxtPath);
        FileOutputStream fileOutputStream=null;
        try {
        	if(!csvTxtFile.exists()) {
        		csvTxtFile.createNewFile();
        	}
        	fileOutputStream=new FileOutputStream(csvTxtFile);
        	fileOutputStream.write(text.getBytes());
        	fileOutputStream.flush();
        	fileOutputStream.close();
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
		
		
			
		
	}
	
	public void deleteFile(String path) {
		File file=new File(path);
		if(!file.exists()) {
			System.out.println(path+"notExist!");
		}else {
			if(file.isFile()) {
				file.delete();
			}
		}
	}
	
	public void deleteFile(List<String> csvPaths) {
		for(String csvPath:csvPaths) {
			File file=new File(csvPath);
			if(!file.exists()) {
				System.out.println(csvPath+"notExist!");
			}else {
				if(file.isFile()) {
					file.delete();
				}
			}
		}
	}

}
