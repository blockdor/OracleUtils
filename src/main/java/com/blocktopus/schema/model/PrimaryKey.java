package com.blocktopus.schema.model;

import java.io.Serializable;
import java.util.List;

import com.blocktopus.schema.data.PrimaryKeyId;

import static com.blocktopus.common.CollectionUtils.*;


public class PrimaryKey implements Serializable{
	private List<Column> columns = newList();
	private Table table;
	public List<Column> getColumns() {
		return columns;
	}
	public void addColumn(Column c){
		columns.add(c);
	}
	@Override
	public String toString() {
		return "PrimaryKey [columns=" + columns + "]";
	}
	
	public int size(){
		return getColumns().size();
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}

	public PrimaryKeyId createPrimaryKeyId(Object... id){
		return new PrimaryKeyId(this, id);
	}
	public PrimaryKeyId createPrimaryKeyId(List<Object> id){
		return new PrimaryKeyId(this, id);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrimaryKey other = (PrimaryKey) obj;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}
}
