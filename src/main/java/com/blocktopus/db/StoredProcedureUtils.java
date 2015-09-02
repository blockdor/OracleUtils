package com.blocktopus.db;

import static com.blocktopus.common.CollectionUtils.*;

import java.sql.Connection;
import java.sql.SQLData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleDriver;

public class StoredProcedureUtils {
	public static Connection getCurrentConnection() throws SQLException{
		return new OracleDriver().defaultConnection();
	}

	/**
	 * converts a sql array to a list of objects.\n
	 * Useful when passing arrays of oracle objects to a java stored procedure.\n
	 * Note - the java class must implement sqldata.
	 * @param array - the array to convert
	 * @param sqlType - the oracle sql type name e.g. o_id_pairs
	 * @param sqlDataClass - the java sqldata class that the oracle objects should be converted to
	 * @return
	 * @throws SQLException
	 */
	public static <T extends SQLData> List<T> convertArrayToSQLData(oracle.sql.ARRAY array,String sqlType, Class<T> sqlDataClass) throws SQLException{
		if(array==null) return newList();
		Map<String, Class<?>> m = getCurrentConnection().getTypeMap();
		m.put(sqlType, sqlDataClass);
		Object[] hos = (Object[])array.getArray(m);

		List<T> theList = newList();
		for(Object o:hos){
			try{
				theList.add((T)o);
			} catch (ClassCastException cce){
				throw new RuntimeException("Array was not converted correctly to "+sqlDataClass.getName(),cce);
			}
		}
		return theList;
	}
}
