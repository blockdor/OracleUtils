package com.blocktopus.schema.data;

import java.util.Map;
import java.util.Map.Entry;

import com.blocktopus.schema.model.Column;
import com.blocktopus.schema.model.Table;

import static com.blocktopus.common.CollectionUtils.*;

public class RowData {

	private Table table;
	private Map<Column, Object> fields = newMap();
	private Map<Column, Object> updatedValues = newMap();
	private boolean isInitialised =false;
	private boolean updated = false;
	private boolean markedForDelete = false;
	
	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public void addDatum(String columnName, Object data) {
		/*if(isInitialised()){
			throw new RuntimeException("DataRow has been locked, to update values use update methods");
		}*/
		getFields().put(getTable().getColumn(columnName), data);
	}

	public void addMapData(Map<String, Object> row) {
		/*if(isInitialised()){
			throw new RuntimeException("DataRow has been locked, to update values use update methods");
		}*/
		for (Entry<String, Object> datum : row.entrySet()) {
			getFields().put(getTable().getColumn(datum.getKey()),
					datum.getValue());
		}
		//setInitialised();
	}

	public Map<Column, Object> getFields() {
		return fields;
	}

	/**
	 * returns the current value of the column
	 * @param columnName
	 * @return
	 */
	public Object getDataByColumnName(String columnName) {
		Column c = getTable().getColumn(columnName);
		if(updatedValues.containsKey(c)){
			return updatedValues.get(c);
		} else {
			return getFields().get(getTable().getColumn(columnName));
		}
	}

	/** 
	 * returns the initial value of the column
	 * @param columnName
	 * @return
	 */
	public Object getInitialDataByColumnName(String columnName) {
		return getFields().get(getTable().getColumn(columnName));
	}
	
	
	
	
/*	public boolean isInitialised() {
		return isInitialised;
	}

	public void setInitialised() {
		this.isInitialised = true;
	}
	public void updateDataValue(String columnName, Object value){
		updatedValues.put(getTable().getColumn(columnName), value);
		setUpdated();
	}*/


}
