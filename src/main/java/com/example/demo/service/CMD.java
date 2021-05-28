package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.springframework.stereotype.Service;
@Service
public class CMD {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CMD cmd=new CMD();
		
	    //String commandStr = "ipconfig";
		String sourcePath="D:\\tools\\uppaal-4.1.24\\uppaal-4.1.24\\bin-Windows";
		String filePath="D:\\exp\\exp0108-final-random-scene1.xml";
//		cmd.gCsvFile(sourcePath,filePath);
		
		
//		System.out.println(execCommand("ipconfig"));
		System.out.println(cmd.getSimulationResult(sourcePath, filePath));
	    
	}
	
	
	
	  public static void exeCmd(String commandStr) {
		    BufferedReader br = null;
		    try {
		      Process p = Runtime.getRuntime().exec(commandStr);
		      br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		      String line = null;
		      StringBuilder sb = new StringBuilder();
		      while ((line = br.readLine()) != null) {
		        sb.append(line + "\n");
		      }
		      System.out.println(sb.toString());
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		    finally
		    {
		      if (br != null)
		      {
		        try {
		          br.close();
		        } catch (Exception e) {
		          e.printStackTrace();
		        }
		      }
		    }
		  }
		  
	  
	  public String gCsvFile(String sourcePath,String filePath) {
		  	InputStream error = null;
		  	try {
		  		StringBuffer command = new StringBuffer();
		  		command.append("cmd /c d: ");
		  		//这里的&&在多条语句的情况下使用，表示等上一条语句执行成功后在执行下一条命令，
		  		//也可以使用&表示执行上一条后台就立刻执行下一条语句
		  		command.append(String.format(" && cd %s", sourcePath));
		  		command.append(" && verifyta.exe -O csv "+filePath);
		  		Process p = Runtime.getRuntime().exec(command.toString());
		  		error = p.getErrorStream();
		  		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(error));
		  		StringBuffer buffer = new StringBuffer();
		  		String s = "";
		  		while ((s = bufferedReader.readLine()) != null) {
		  			buffer.append(s);
		  		}
		  		bufferedReader.close();
		  		p.waitFor();
		  		if (p.exitValue() != 0) {
		  			return buffer.toString();
		  		} else {
		  			return "";
		  		}
		  	} catch (Exception ex) {
		  		if (error != null) {
		  			try {
		  				error.close();
		  			} catch (IOException e) {
		  				e.printStackTrace();
		  			}
		  		}
		  		return ex.getMessage();
		  	}
		  }
	  
	  public String getSimulationResult(String sourcePath,String filePath) {
		  
		  InputStream error = null;
		  	try {
		  		StringBuffer command = new StringBuffer();
		  		command.append("cmd /c d: ");
		  		//这里的&&在多条语句的情况下使用，表示等上一条语句执行成功后在执行下一条命令，
		  		//也可以使用&表示执行上一条后台就立刻执行下一条语句
		  		command.append(String.format(" && cd %s", sourcePath));
		  		command.append(" && verifyta.exe -O std "+filePath);
//		  		System.out.println(command.toString());
		  		Process process = Runtime.getRuntime().exec(command.toString());
		  		error = process.getErrorStream();
//		  		long startTime0=System.currentTimeMillis();
		  		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(),Charset.forName("UTF-8")));
//		  		long endTime0=System.currentTimeMillis();
//	  			System.out.println("simulationData time: "+(endTime0-startTime0));
		  		StringBuffer resultBuffer = new StringBuffer();
		  		String s = "";
		  		s = bufferedReader.readLine();
		  		if(s==null) {
		  			System.out.println("null");
		  		}
		  		while (s != null) {
//		  			System.out.println(s);
		  			resultBuffer.append(s+"\n");
//		  			long startTime=System.currentTimeMillis();
		  			s = bufferedReader.readLine();
//		  			long endTime=System.currentTimeMillis();
//		  			System.out.println("readline time: "+(endTime-startTime));
//		  			if(s!=null) {
//		  				System.out.println(s.toString());	
//		  			}
		  			
		  		}
		  		bufferedReader.close();
		  		process.waitFor();
		  		String result=resultBuffer.toString();
		  		int formulaIsSatisfiedIndex=result.indexOf("Formula is satisfied.");
		  		if(formulaIsSatisfiedIndex>=0) {
		  			result=result.substring(formulaIsSatisfiedIndex).substring("Formula is satisfied.".length());
		  		}
		  		return result;
		  	} catch (Exception ex) {
		  		if (error != null) {
		  			try {
		  				error.close();
		  			} catch (IOException e) {
		  				e.printStackTrace();
		  			}
		  		}
		  		return ex.getMessage();
		  	}
		  
	  }
	  
	  public static String execCommand(String command) {
		  String line = "";
	        StringBuilder sb = new StringBuilder();
	 
	        try (BufferedReader bufferedReader = new BufferedReader(
	                new InputStreamReader(
	                        Runtime.getRuntime().exec(command).getInputStream(), Charset.forName("GBK")));)
	        {
	            while ((line = bufferedReader.readLine()) != null)
	                sb.append(line + "\r\n");
	        } catch (IOException e) { 
	        	return "Invalid command."; }
	        
	        return sb.toString();
	
	  }

}
