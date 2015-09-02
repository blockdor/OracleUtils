package com.blocktopus.oracle.types;

import java.sql.SQLData;
import java.sql.Types;

/**
 * for retrieving an sql object from a procedures in an OUT parameter.
 * <p> requires that an {@link SQLData} representation of the sql object be made.
 * <p> If that's an issue try {@link ObjectOutputParameterLazy}
 * @author Block
 *
 */
public class ObjectOutputParameter<T extends SQLData> extends NamedOutputParameter implements SQLObjectConverter<T>{

	private Class<T> sqlDataClass;

	public Class<T> getSqlDataClass() {
		return sqlDataClass;
	}
	public void setSqlDataClass(Class<T> sqlDataClass) {
		this.sqlDataClass = sqlDataClass;
	}
	public ObjectOutputParameter(String sqlTypeName, Class<T> sqlDataClass) {
		this.setSqlTypeName(sqlTypeName);
		this.sqlDataClass = sqlDataClass;
	}
	@Override
	public int getJDBCType() {
		return Types.STRUCT;
	}

	@Override
	public T getParameter(){
		return (T)super.getParameter();
	}
	
	public String toString(){
		return "Param:"+getParameter()+" type:STRUCT JavaClass:"+sqlDataClass.getName();
	}
	public String getObjectSqlTypeName() {
		// TODO Auto-generated method stub
		return getSqlTypeName();
	}
}
