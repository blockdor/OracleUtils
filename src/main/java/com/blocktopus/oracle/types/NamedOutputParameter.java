package com.blocktopus.oracle.types;

public abstract class NamedOutputParameter extends OutputParameter{
	private String sqlTypeName;
	public String getSqlTypeName() {
		return sqlTypeName;
	}

	public void setSqlTypeName(String sqlTypeName) {
		this.sqlTypeName = sqlTypeName;
	}
}
