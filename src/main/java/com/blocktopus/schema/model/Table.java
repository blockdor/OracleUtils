package com.blocktopus.schema.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import static com.blocktopus.common.CollectionUtils.*;

public class Table implements Serializable{

	private String name;
	private PrimaryKey primaryKey;
	private List<Column> columns;
	private Set<ForeignKey> foreignKeys = newSet();
	private Set<ForeignKey> refKeys = newSet();
	private boolean edge = false;
	
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Column getColumn(String columnName){
		for(Column c:columns){
			if(c.getName().equalsIgnoreCase(columnName)){
				return c;
			}
		}
		return null;
	}
	public ForeignKey getFK(String fkName){
		for(ForeignKey fk:foreignKeys){
			if(fk.getName().equalsIgnoreCase(fkName)){
				return fk;
			}
		}
		return null;
	}
	public ForeignKey getRefKey(String refKeyName){
		for(ForeignKey fk:refKeys){
			if(fk.getName().equalsIgnoreCase(refKeyName)){
				return fk;
			}
		}
		return null;
	}
	public Set<ForeignKey> getFKsToTable(Table refTable){
		Set<ForeignKey> fks = newSet();
		for(ForeignKey fk:foreignKeys){
			if(fk.getRefTable().equals(refTable)){
				fks.add(fk);
			}
		}
		return fks;
	}
	public Set<ForeignKey> getRefKeysFromTable(Table fkTable){
		Set<ForeignKey> fks = newSet();
		for(ForeignKey fk:refKeys){
			if(fk.getFkTable().equals(fkTable)){
				fks.add(fk);
			}
		}
		return fks;
	}
	
	
	public Set<ForeignKey> getRefKeys() {
		return refKeys;
	}
	public void addRefKey(ForeignKey refKey) {
		this.refKeys.add(refKey);
	}
	public Set<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}
	public void addForeignKey(ForeignKey foreignKey) {
		this.foreignKeys.add(foreignKey);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Table other = (Table) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}
	@Override
	public String toString() {
		return "Table [name=" + name + "]";
	}
	public boolean isEdge() {
		return edge;
	}
	public void setEdge(boolean edge) {
		this.edge = edge;
	}

	

	
}
