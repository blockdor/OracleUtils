package com.blocktopus.oracle.types;

public abstract class OutputParameter {
	private Object parameter;
	private String name;
	public abstract int getJDBCType();
	public Object getParameter(){
		return parameter;
	}
	public void setParameter(Object parameter){
		this.parameter=parameter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
