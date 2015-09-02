package com.blocktopus.oracle.types;

import java.sql.Array;
import java.sql.SQLData;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.OracleConnection;

/**
 * Input type, for passing in an object array (nested table of sql objects) as a parameter.
 * <P> extends java.util.List for ease of use
 * @author Block
 *
 * @param <T> the SQLData type that describes the sql object.
 */
public class ObjectList<T extends SQLData> extends ArrayList<T> implements OracleList<T>{

	private static final long serialVersionUID = 1L;

	/**
	 * @param sqlTypeName e.g. "cramer.T_GENERAL_IDLIST"
	 */
	public ObjectList(String sqlTypeName){
		super();
		this.sqlTypeName=sqlTypeName;
	}
	private String sqlTypeName;

	public String getSqlTypeName() {
		return sqlTypeName;
	}

	public void setSqlTypeName(String sqlType) {
		this.sqlTypeName = sqlType;
	}

	public String getObjectSQLType() {
		if(this.size()>0){
			try {
				return get(0).getSQLTypeName();
			} catch (SQLException e) {
				return null;
			}
		} else {
			return null;
		}
	}
	public Class<T> getObjectClass(){
		if(this.size()>0){
				return (Class<T>)get(0).getClass();
		} else {
			return null;
		}
	}

	public Array getArray(OracleConnection connection) throws SQLException{
		if(size()>0){
			connection.getTypeMap().put(getObjectSQLType(), getObjectClass());
			return connection.createARRAY(getSqlTypeName(), toArray());
		} else {
			return connection.createARRAY(getSqlTypeName(), toArray());
		}

	}
}
