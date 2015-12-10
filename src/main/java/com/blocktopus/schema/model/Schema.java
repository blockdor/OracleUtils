package com.blocktopus.schema.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.blocktopus.common.CollectionUtils.*;

public class Schema implements Serializable{
	private String name;
	private Set<Table> tables = newSet();

	public Collection<Table> getTables() {
		return tables;
	}

	public void setTables(Set<Table> tables) {
		this.tables = tables;
	}
	
	public void addTable(Table table){
		tables.add(table);
	}

	public boolean containsTable(String tableName){
	 return getTable(tableName)!=null;
	}
	public Table getTable(String tableName){
		if(tableName==null){
			return null;
		}
		for(Table table:tables){
			if(tableName.equalsIgnoreCase(table.getName())){
				return table;
			}
		}
		return null;
	}
	
	public void createForeignKey(String name, String fkTableName, String refTableName, Map<String,String> columnReferences){
		Table fkTable=getTable(fkTableName);
		Table refTable=getTable(refTableName);
		
		boolean fkTableEdge=(fkTable==null);
		boolean refTableEdge=(refTable==null);
		
		if(fkTableEdge){
			fkTable = new Table();
			fkTable.setName(fkTableName);
			fkTable.setEdge(true);
		}
		if(refTableEdge){
			refTable = new Table();
			refTable.setName(refTableName);
			refTable.setEdge(true);
		}
		
		ForeignKey fk=new ForeignKey();
		fk.setFkTable(fkTable);
		fk.setRefTable(refTable);
		for(String fkColumnName:columnReferences.keySet()){
			String refColumnName=columnReferences.get(fkColumnName);
			Column fkColumn;
			Column refColumn;
			if(fkTableEdge){
				fkColumn = new Column();
				fkColumn.setName(fkColumnName);
				fkColumn.setContainingTable(fkTable);
			} else {
				fkColumn = fkTable.getColumn(fkColumnName);
			}
			if(refTableEdge){
				refColumn = new Column();
				refColumn.setName(refColumnName);
				refColumn.setContainingTable(fkTable);
			} else {
				refColumn = refTable.getColumn(refColumnName);
			}
			fk.addReferences(fkColumn, refColumn);
		}
		fkTable.addForeignKey(fk);
		refTable.addRefKey(fk);
		fk.setName(name);

	}
	public Collection<ForeignKey> getEdges(){
		List<ForeignKey> fks = newList();
		for(Table t :getTables()){
			for(ForeignKey fk :t.getRefKeys()){
				Table fkt = fk.getFkTable();
				if(!getTables().contains(fkt)){
					fks.add(fk);
				}
			}
		}
		return fks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}
