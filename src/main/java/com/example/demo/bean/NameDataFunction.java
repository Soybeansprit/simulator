package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;



public class NameDataFunction {
	public String dataName=null;
	public List<DataFunction> dataFunctions=new ArrayList<DataFunction>();
	public String getName() {
		return dataName;
	}
	public void setName(String dataName) {
		this.dataName = dataName;
	}
	public List<DataFunction> getDataFunctions() {
		return dataFunctions;
	}
	public void setDataFunctions(List<DataFunction> dataFunctions) {
		this.dataFunctions = dataFunctions;
	}
}
