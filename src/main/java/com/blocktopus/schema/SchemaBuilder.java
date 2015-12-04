package com.blocktopus.schema;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.blocktopus.oracle.OracleQuerier;
import com.blocktopus.schema.model.Column;
import com.blocktopus.schema.model.PrimaryKey;
import com.blocktopus.schema.model.Schema;
import com.blocktopus.schema.model.Table;

import static com.blocktopus.common.CollectionUtils.*;

public class SchemaBuilder {
	private static Logger logger = Logger.getLogger("SchemaBuilder");
	private OracleQuerier oracleQuerier;

	public Schema buildSchema(String schemaName) {
		String query = "SELECT table_name FROM all_tables t WHERE t.OWNER = upper(?)";
		List<Object> tableNamesResult = oracleQuerier.queryForSingleColumnList(
				query, schemaName);
		Set<String> tableNames = newSet();
		for (Object tnr : tableNamesResult) {
			tableNames.add((String) tnr);
		}
		return buildSchema(schemaName, tableNames);
	}

	public Schema buildSchema(String schemaName, Set<String> tableNames) {
		Schema s = new Schema();
		s.setName(schemaName);
		for (String tableName : tableNames) {
			Table t = getTableStructure(tableName, schemaName);

			s.addTable(t);
		}
		for (String tableName : tableNames) {
			logger.info("get foreign keys for " + tableName);
			String query = "SELECT fk.constraint_name cname, fk.table_name fktablename, reffed.table_name reftablename"
					+ "  FROM user_constraints fk, user_constraints reffed "
					+ " WHERE fk.constraint_type = 'R' "
					+ "   AND reffed.constraint_name = fk.r_constraint_name"
					+ "   AND reffed.owner = fk.owner"
					+ "   AND ((reffed.table_name = upper(?) AND reffed.owner = upper(?)) OR"
					+ "       (fk.table_name = upper(?) AND fk.owner = upper(?)))";
			List<Map<String, Object>> results = oracleQuerier.queryForMapList(
					query, tableName, schemaName,tableName,schemaName);

			for (Map<String, Object> row : results) {

				String constraintName = (String) row.get("cname");
				String fkTableName = (String) row.get("fktablename");
				String refTableName = (String) row.get("reftablename");
				logger.info("get foreign key columns for " + constraintName);
				String queryForColumns = "SELECT fkcol.column_name fkcolname, reffedcol.column_name refcolname"
						+ "  FROM user_constraints fk, user_cons_columns fkcol, user_constraints reffed, user_cons_columns reffedcol "
						+ " WHERE fk.constraint_type = 'R' "
						+ "   AND fkcol.constraint_name = fk.constraint_name "
						+ "   AND reffed.constraint_name = fk.r_constraint_name "
						+ "   AND reffedcol.constraint_name = reffed.constraint_name "
						+ "   AND reffedcol.position = fkcol.position "
						+ "   AND reffed.owner = fk.owner"
						+ "   AND fk.owner = fkcol.owner"
						+ "   AND reffed.owner = reffedcol.owner"
						+ "   AND fk.constraint_name = upper(?)"
						+ "   AND fk.owner = upper(?)"
						+ " ORDER BY fkcol.position";
				//logger.info(queryForColumns);

				List<Map<String, Object>> colResults = oracleQuerier
						.queryForMapList(queryForColumns, constraintName,schemaName);
				Map<String, String> references = newMap();
				for (Map<String, Object> colRow : colResults) {
					String fkColName = (String) colRow.get("fkcolname");
					String refColName = (String) colRow.get("refcolname");
					references.put(fkColName, refColName);
				}
				s.createForeignKey(constraintName, fkTableName, refTableName,
						references);
			}
		}
		return s;
	}

	private Table getTableStructure(String tableName, String schemaName) {
		logger.info("get table structure for " + tableName);
		String query = "SELECT column_name, data_type "
				+ "  FROM all_tab_cols atc " + " WHERE table_name = upper(?) "
				+ "   AND owner = upper(?) " + " ORDER BY atc.COLUMN_ID";
		List<Map<String, Object>> columnData = oracleQuerier.queryForMapList(
				query, tableName, schemaName);
		Table t = new Table();
		t.setName(tableName);
		List<Column> columns = newList();
		for (Map<String, Object> o : columnData) {
			String columnName = (String) o.get("column_name");
			String dataType = (String) o.get("data_type");
			Column c = new Column();
			c.setContainingTable(t);
			c.setName(columnName);
			c.setDataType(dataType);
			columns.add(c);
		}
		t.setColumns(columns);
		// find primary key
		logger.info("get primary key for " + tableName);
		query = "SELECT column_name "
				+ "  FROM all_constraints c, all_cons_columns cc "
				+ " WHERE c.TABLE_NAME = upper(?) "
				+ "   AND constraint_type = 'P' "
				+ "   AND cc.CONSTRAINT_NAME = c.CONSTRAINT_NAME "
				+ "   AND c.owner = upper(?) " + "   AND cc.owner = c.owner"
				+ " ORDER BY position ";
		List<Object> pkColumnNames = oracleQuerier.queryForSingleColumnList(
				query, tableName, schemaName);
		PrimaryKey pk = new PrimaryKey();
		for (Object pkColumnName : pkColumnNames) {
			pk.addColumn(t.getColumn((String) pkColumnName));
			
		}
		if (pk.getColumns().size() > 0) {
			t.setPrimaryKey(pk);
			pk.setTable(t);
		}
		return t;
	}

	public OracleQuerier getOracleQuerier() {
		return oracleQuerier;
	}

	public void setOracleQuerier(OracleQuerier oracleQuerier) {
		this.oracleQuerier = oracleQuerier;
	}

	public void dumpSchemaToFile(Schema s, File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(s);
		oos.close();
		fos.close();

	}

	public Schema loadSchemaFromFile(File file) throws IOException, ClassNotFoundException {
		FileInputStream fis=null;
		ObjectInputStream ois=null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			Schema s = (Schema) ois.readObject();
			return s;
		} finally {
			if (ois != null) {
				ois.close();
			}
			if (fis != null) {
				fis.close();
			}
		}

	}
}
