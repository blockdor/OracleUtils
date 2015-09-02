package com.blocktopus.oracle.types;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.OracleConnection;
/**
 * Input type, for passing in a list of primitive (non object) values  e.g. Date,Number,String as a parameter.
 * <P> extends java.util.List for ease of use
 * @author Block
 *
 * @param <T> the java type e.g. {@link java.util.Date} or {@link Number}.
 */
public class PrimitiveList<T> extends ArrayList<T> implements OracleList<T>{

	private static final long serialVersionUID = 1L;

	/**
	 * @param sqlType - the SQL type name of the list
	 */
	public PrimitiveList(String sqlType){
		super();
		this.sqlType = sqlType;
	}
	private String sqlType;

	public String getSqlTypeName() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}
	public Array getArray(OracleConnection connection) throws SQLException{
	        return connection.createARRAY(getSqlTypeName(), toArray());
	}
}
