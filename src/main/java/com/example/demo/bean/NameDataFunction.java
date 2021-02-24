package com.example.demo.bean;

import java.util.ArrayList;
import java.util.List;



public class NameDataFunction {
	public String name=null;
	public List<DataFunction> dataFunctions=new ArrayList<DataFunction>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<DataFunction> getDataFunctions() {
		return dataFunctions;
	}
	public void setDataFunctions(List<DataFunction> dataFunctions) {
		this.dataFunctions = dataFunctions;
	}
}
