package com.blocktopus.oracle;

import java.math.BigDecimal;
import java.sql.SQLData;
import java.util.Date;

import com.blocktopus.oracle.types.ObjectListOutputParameter;
import com.blocktopus.oracle.types.ObjectListOutputParameterLazy;
import com.blocktopus.oracle.types.ObjectOutputParameter;
import com.blocktopus.oracle.types.PrimitiveListOutputParameter;
import com.blocktopus.oracle.types.PrimitiveOutputParameter;

public class ParameterFactory {
	public static PrimitiveOutputParameter<Long> getLongOutputParameter(){
		return new PrimitiveOutputParameter<Long>(Long.class);
	}
	public static PrimitiveOutputParameter<Double> getDoubleOutputParameter(){
		return new PrimitiveOutputParameter<Double>(Double.class);
	}
	public static PrimitiveOutputParameter<Date> getDateOutputParameter(){
		return new PrimitiveOutputParameter<Date>(Date.class);
	}
	public static PrimitiveOutputParameter<Integer> getIntegerOutputParameter(){
		return new PrimitiveOutputParameter<Integer>(Integer.class);
	}
	public static PrimitiveOutputParameter<String> getVarcharOutputParameter(){
		return new PrimitiveOutputParameter<String>(String.class);
	}
	public static PrimitiveOutputParameter<String> getClobOutputParameter(){
		return getVarcharOutputParameter();
	}
	public static PrimitiveListOutputParameter<Long> getListOfLongOutputParameter(String sqlTypeName){
		return new PrimitiveListOutputParameter<Long>(sqlTypeName, Long.class);
	}
	public static PrimitiveListOutputParameter<Double> getListOfDoubleOutputParameter(String sqlTypeName){
		return new PrimitiveListOutputParameter<Double>(sqlTypeName, Double.class);
	}
	public static PrimitiveListOutputParameter<String> getListOfVarcharOutputParameter(String sqlTypeName){
		return new PrimitiveListOutputParameter<String>(sqlTypeName, String.class);
	}
	public static PrimitiveListOutputParameter<Date> getListOfDateOutputParameter(String sqlTypeName){
		return new PrimitiveListOutputParameter<Date>(sqlTypeName, Date.class);
	}
	public static <T extends SQLData> ObjectOutputParameter<T> getObjectOutputParameter(String sqlTypeName, Class<T> sqlDataClass ){
		return new ObjectOutputParameter<T>(sqlTypeName, sqlDataClass);
	}
	public static <T extends SQLData> ObjectListOutputParameter<T> getObjectListOutputParameter(String tableSqlTypeName, String objectSqlTypeName, Class<T> sqlDataClass ){
		return new ObjectListOutputParameter<T>(tableSqlTypeName,objectSqlTypeName,sqlDataClass);
	}
	/**
	 * Will return the objects in the list as a {@link Map}<String,Object> where the string is the name of the attribute and the object is the value<br/>
	 * 
	 * @param tableSqlTypeName
	 * @return
	 */
	public static ObjectListOutputParameterLazy getObjectListOutputParameter(String tableSqlTypeName){
		return new ObjectListOutputParameterLazy(tableSqlTypeName);
	}
	}
