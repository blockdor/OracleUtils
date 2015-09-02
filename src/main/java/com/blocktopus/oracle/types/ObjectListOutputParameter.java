package com.blocktopus.oracle.types;

import java.sql.SQLData;
import java.util.List;
import static com.blocktopus.common.CollectionUtils.*;
/**
 * for retrieving a nested table of sql objects from a procedures in an OUT parameter.
 * <p> requires that an {@link SQLData} representation of the sql object be made.
 * <p> If that's an issue try {@link ObjectListOutputParameterLazy}
 * @author Block
 *
 */
public class ObjectListOutputParameter<T extends SQLData> extends PrimitiveListOutputParameter<T> implements SQLObjectConverter<T>{

	private Class<T> sqlDataClass;
	private String objectSqlTypeName;

	public ObjectListOutputParameter(String tableSqlTypeName, String objectSqlTypeName, Class<T> sqlDataClass) {
		super(tableSqlTypeName,sqlDataClass);
		this.setObjectSqlTypeName(objectSqlTypeName.toUpperCase());
		this.sqlDataClass= sqlDataClass;
	}

	public Class<T> getSqlDataClass() {
		return sqlDataClass;
	}

	public void setSqlDataClass(Class<T> sqlDataClass) {
		this.sqlDataClass = sqlDataClass;
	}

	public String getObjectSqlTypeName() {
		return objectSqlTypeName;
	}

	public void setObjectSqlTypeName(String objectSqlTypeName) {
		this.objectSqlTypeName = objectSqlTypeName;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getParameter(){
		List<T> typedList = newList();
		for(Object o:super.getParameter()){
			typedList.add((T) o);
		}
		return typedList;
	}

}
