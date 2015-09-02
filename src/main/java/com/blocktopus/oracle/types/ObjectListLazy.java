package com.blocktopus.oracle.types;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleConnection;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import static com.blocktopus.common.CollectionUtils.*;

/**
 * Input type, for passing in an object array (nested table of sql objects) as a parameter. <br>
 * <p>extends {@link List} for ease of use
 *
 * <p>This is a lazy implementation such that you don't have to create an SQLData Object and can use a map of attributes for each object instead.<br> 
 * However you lose compile time checking of types and names.
 * <p> I would always suggest using {@link ObjectList} over this implementation
 * @author Block
 *
 */
@SuppressWarnings("serial")
public class ObjectListLazy extends ArrayList<Map<String,Object>> implements OracleList<Map<String,Object>>{
/**
 * 	Takes two params, the name of the table type and the name of the sql object type.
 * @param sqlTypeName e.g. "CRAMER.T_PPORTYPE"
 * @param objectSQLTypeName e.g. "CRAMER.O_PPORTTYPE"
 */
	public ObjectListLazy(String sqlTypeName, String objectSQLTypeName) {
		super();
		this.sqlTypeName = sqlTypeName;
		this.objectSQLTypeName = objectSQLTypeName;
	}

	private String sqlTypeName;
	private String objectSQLTypeName;
	
	public void setSqlTypeName(String sqlTypeName) {
		this.sqlTypeName = sqlTypeName;
	}

	public String getSqlTypeName() {
		return sqlTypeName;
	}

	public String getObjectSQLTypeName() {
		return objectSQLTypeName;
	}

	public void setObjectSQLTypeName(String objectSQLTypeName) {
		this.objectSQLTypeName = objectSQLTypeName;
	}

	public Array getArray(OracleConnection connection) throws SQLException {
		
		StructDescriptor sd = StructDescriptor.createDescriptor(getObjectSQLTypeName(), connection);
		List<STRUCT> structs = newList();
		for(Map<String,Object> attrs:this){
			//due to oracle being a bitch about uppercasing everything
			Map<String,Object> attrsUppered= newMap();
			for(String attrName:attrs.keySet()){
				attrsUppered.put(attrName.toUpperCase(), attrs.get(attrName));
			}
			
			STRUCT s = new STRUCT(sd, connection, attrsUppered);
			structs.add(s);
			
		}
		return connection.createARRAY(getSqlTypeName(),structs.toArray());
	}
	
}
