package com.blocktopus.schema.model;

import java.io.Serializable;

public class Column implements Serializable{

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((containingTable == null) ? 0 : containingTable.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Column other = (Column) obj;
		if (containingTable == null) {
			if (other.containingTable != null)
				return false;
		} else if (!containingTable.equals(other.containingTable))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "["+containingTable.getName()+":"+name+"]";
	}
	private String name;
	private String dataType;
	private Table containingTable;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Table getContainingTable() {
		return containingTable;
	}
	public void setContainingTable(Table containingTable) {
		this.containingTable = containingTable;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
