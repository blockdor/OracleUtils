package com.blocktopus.schema.model;

import java.io.Serializable;
import java.util.Map;

import static com.blocktopus.common.CollectionUtils.*;
public class ForeignKey implements Serializable{
	
	private Table fkTable;
	private Table refTable;

	private Map<Column,Column> references = newMap();
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * fk column then ref column
	 * @return
	 */
	public Map<Column,Column> getReferences() {
		return references;
	}

	public void setReferences(Map<Column,Column> references) {
		this.references = references;
	}
	
	public void addReferences(Column fkColumn,Column referencedColumn) {
		references.put(fkColumn, referencedColumn);
	}

	public Table getRefTable() {
		return refTable;
	}

	public void setRefTable(Table refTable) {
		this.refTable = refTable;
	}

	public Table getFkTable() {
		return fkTable;
	}

	@Override
	public String toString() {
		return "ForeignKey [fkTable=" + fkTable + ", refTable=" + refTable
				+ ", references=" + references + "]";
	}

	public void setFkTable(Table fkTable) {
		this.fkTable = fkTable;
	}
}
