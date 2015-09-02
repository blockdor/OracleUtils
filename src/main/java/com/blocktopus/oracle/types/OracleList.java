package com.blocktopus.oracle.types;

import java.sql.Array;
import java.sql.SQLException;
import java.util.List;

import oracle.jdbc.OracleConnection;

public interface OracleList<T> extends List<T>{
	public String getSqlTypeName();
	public Array getArray(OracleConnection connection) throws SQLException;
}
