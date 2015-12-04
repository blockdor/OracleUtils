package com.blocktopus.schema;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.blocktopus.oracle.OracleQuerier;
import com.blocktopus.schema.data.RowData;
import com.blocktopus.schema.data.PrimaryKeyId;
import com.blocktopus.schema.model.Column;
import com.blocktopus.schema.model.Table;

import static com.blocktopus.common.CollectionUtils.*;

public class RowDataExtractor {

	public static RowDataExtractor getInstance(){
		return new RowDataExtractor();
	}
	
	private void checkInitialised(){
		if(getOracleQuerier()==null){
			throw new RuntimeException("RowDataExtractor requires an OracleQuerier as a dependency");
		}
		if(getDataCache()==null){
			throw new RuntimeException("RowDataExtractor requires a DataCache as a dependency");
		}
	}
	
	private RowDataExtractor(){
		setDataCache(DataCache.getInstance());
	}
	
	private static Logger logger = Logger.getLogger(RowDataExtractor.class
			.getName());
	private OracleQuerier oracleQuerier;
	private DataCache dataCache;

	public OracleQuerier getOracleQuerier() {
		return oracleQuerier;
	}

	public void setOracleQuerier(OracleQuerier oracleQuerier) {
		this.oracleQuerier = oracleQuerier;
	}
	
	public DataCache getDataCache() {
		return dataCache;
	}

	public void setDataCache(DataCache dataCache) {
		this.dataCache = dataCache;
	}
	/**
	 * Queries the table for all its data!
	 * @param table - the table to query
	 * @return List of Rowdata
	 */
	public List<RowData> getAllDataFromTable(Table table) {
		checkInitialised();
		List<Column> cols = table.getColumns();
		String query = "Select " + getCommaSeperatedList(cols) + " from "
				+ table.getName();
		List<Map<String, Object>> result = getOracleQuerier().queryForMapList(
				query);

		List<Column> pkcols = table.getPrimaryKey().getColumns();
		
		List<RowData> tableData = newList();
		for (Map<String, Object> row : result) {
			RowData dr = new RowData();
			dr.setTable(table);
			dr.addMapData(row);
			tableData.add(dr);
			
			List<Object> id = newList();
			for(Column c:pkcols){
				id.add(dr.getDataByColumnName(c.getName()));
			}
			PrimaryKeyId pkid = table.getPrimaryKey().createPrimaryKeyId(id);
			getDataCache().addToCache(pkid, dr);
			
		}
		return tableData;
	}

	/**
	 * Uses simple for loop over getDataFromTableByPK, a more clever bulk
	 * operation may come later.
	 * 
	 * @param pkids - the Primary Key ids (can be from different tables)
	 * @return Set of Rowdata Objects
	 */
	public Set<RowData> getDataFromTableByPKs(Set<PrimaryKeyId> pkids) {
		checkInitialised();
		Set<RowData> tableData = newSet();
		for (PrimaryKeyId pkid : pkids) {
			tableData.add(getDataFromTableByPK(pkid));
		}
		return tableData;
	}

	public RowData getDataFromTableByPK(PrimaryKeyId pkid) {
		checkInitialised();
		RowData cachedRD = getDataCache().getDataFromCache(pkid);
		if (cachedRD != null) {
			return cachedRD;
		}

		Table table = pkid.getPrimaryKey().getTable();
		List<Column> cols = table.getColumns();
		String query = "Select " + getCommaSeperatedList(cols) + " from "
				+ table.getName() + " where "
				+ getSimpleWhereClause(pkid.getPrimaryKey().getColumns());
		Map<String, Object> result = getOracleQuerier().queryForMap(query, pkid.getId().toArray());

		RowData rowData = new RowData();
		rowData.setTable(table);
		rowData.addMapData(result);
		getDataCache().addToCache(pkid, rowData);
		return rowData;

	}

	private String getCommaSeperatedList(List<Column> cols) {
		StringBuilder sb = new StringBuilder();
		for (Column c : cols) {
			sb.append(c.getName());
			sb.append(",");
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();

	}

	private String getCommaSepColumnsWithPrefix(String prefix, List<Column> cols) {
		StringBuilder sb = new StringBuilder();
		for (Column c : cols) {
			sb.append(prefix + "." + c.getName());
			sb.append(",");
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	private String getSimpleWhereClause(List<Column> cols) {
		StringBuilder sb = new StringBuilder();
		for (Column c : cols) {
			sb.append(c.getName() + " = ? AND ");
		}
		if (sb.length() > 0)
			sb.delete(sb.length() - 5, sb.length());
		return sb.toString();
	}

}
