package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CMD {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CMD cmd=new CMD();
		
	    //String commandStr = "ipconfig";
		String sourcePath="D:\\tools\\uppaal-4.1.24\\uppaal-4.1.24\\bin-Windows";
		String filePath="D:\\exp\\exp01083-2.xml";
		cmd.gCsvFile(sourcePath,filePath);
	    
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

}
