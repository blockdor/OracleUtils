package com.blocktopus.oracle.types;

import java.sql.SQLData;

public interface SQLObjectConverter<T extends SQLData> {
	public Class<T> getSqlDataClass();
	public String getObjectSqlTypeName();
}
