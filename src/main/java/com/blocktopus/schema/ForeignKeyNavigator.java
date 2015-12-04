package com.blocktopus.schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.blocktopus.oracle.OracleQuerier;
import com.blocktopus.schema.data.PrimaryKeyId;
import com.blocktopus.schema.data.RowData;
import com.blocktopus.schema.model.Column;
import com.blocktopus.schema.model.ForeignKey;
import com.blocktopus.schema.model.PrimaryKey;

import static com.blocktopus.common.CollectionUtils.*;

public class ForeignKeyNavigator {
	
	public static ForeignKeyNavigator getInstance(){
		return new ForeignKeyNavigator();
	}
	private ForeignKeyNavigator(){
		setRowDataExtractor(RowDataExtractor.getInstance());
	}
	
	private static Logger logger = Logger.getLogger(RowDataExtractor.class
			.getName());
	private OracleQuerier oracleQuerier;
	private RowDataExtractor rowDataExtractor;

	public OracleQuerier getOracleQuerier() {
		return oracleQuerier;
	}

	public void setOracleQuerier(OracleQuerier oracleQuerier) {
		this.oracleQuerier = oracleQuerier;
	}

	public RowDataExtractor getRowDataExtractor() {
		return rowDataExtractor;
	}

	public void setRowDataExtractor(RowDataExtractor rowDataExtractor) {
		this.rowDataExtractor = rowDataExtractor;
	}

	// node2location
	public PrimaryKeyId navigateToRefTable(RowData row, ForeignKey fk) {
		if(fk.getRefTable().isEdge()){
			throw new RuntimeException(fk.getRefTable().getName()+" has not been included in schema, include it before trying to navigate to it");
		}
		if (!fk.getFkTable().equals(row.getTable())) {
			throw new RuntimeException(
					"RowData is from the wrong table! passed:" + row.getTable()
							+ " , required:" + fk.getFkTable());
		}
		List<Object> id = newList();
		// get value from correct columns
		for (Column c : fk.getReferences().keySet()) {
			id.add(row.getDataByColumnName(c.getName()));
		}

		// make a pk
		return fk.getRefTable().getPrimaryKey().createPrimaryKeyId(id);
	}

	// Location2node
	public Set<PrimaryKeyId> navigateToFKTable(RowData row, ForeignKey fk) {
		if (!fk.getRefTable().equals(row.getTable())) {
			throw new RuntimeException(
					"RowData is from the wrong table! passed:" + row.getTable()
							+ " , required:" + fk.getRefTable());
		}
		if(fk.getFkTable().isEdge()){
			throw new RuntimeException(fk.getFkTable().getName()+" has not been included in schema, include it before trying to navigate to it");
		}

		// select nodeid,rpplanid from node where node2location=? and rpplanid=?

		StringBuilder query = new StringBuilder();
		query.append("SELECT ");

		List<Column> selectCols = fk.getFkTable().getPrimaryKey().getColumns();
		List<Column> whereCols = newList();
		for (Column c : fk.getReferences().keySet()) {
			whereCols.add(c);
		}
		query.append(getCommaSeperatedList(selectCols));
		query.append(" FROM " + fk.getFkTable().getName() + " WHERE ");
		query.append(getSimpleWhereClause(whereCols));

		logger.info(query.toString());
		List<Object> id = newList();
		// get value from correct columns
		for (Column c : fk.getReferences().keySet()) {
			id.add(row.getDataByColumnName(fk.getReferences().get(c).getName()));
		}
		List<Map<String, Object>> results = getOracleQuerier().queryForMapList(
				query.toString(), id.toArray());
		
		logger.info(results.toString());
		Set<PrimaryKeyId> pkids = newSet();
		for (Map<String, Object> result : results) {
			pkids.add(convertToPrimaryKey(fk.getFkTable().getPrimaryKey(),
					result));
		}
		return pkids;
	}

	protected PrimaryKeyId convertToPrimaryKey(PrimaryKey pk,
			Map<String, Object> id) {
		List<Object> ids = newList();
		for (Column c : pk.getColumns()) {
			ids.add(id.get(c.getName()));
		}
		return pk.createPrimaryKeyId(ids.toArray());
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
