package com.blocktopus.oracle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import com.blocktopus.oracle.types.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.blocktopus.oracle.exception.DBCallException;
import com.blocktopus.oracle.exception.QueryResultException;
import com.blocktopus.oracle.exception.QueryResultException.QueryResultExceptionType;
import com.blocktopus.oracle.types.LiteralString;
import com.blocktopus.oracle.types.NamedOutputParameter;
import com.blocktopus.oracle.types.OracleList;
import com.blocktopus.oracle.types.OutputParameter;
import com.blocktopus.oracle.types.SQLObjectConverter;
import com.blocktopus.common.types.LinkedCaseInsensitiveMap;

import static com.blocktopus.common.CollectionUtils.*;
import static com.blocktopus.oracle.Common.close;

public class OracleQuerier {
	private DataSource dataSource;
	private Connection connection;
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public Connection getConnection() throws SQLException {
		if(connection==null){
			if(dataSource!=null){
				return dataSource.getConnection();
			} else {
				throw new RuntimeException("Must set either dataource or connection before use");
			}
		}
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void closeConnection(Connection connection){
		if(this.connection!=null){
			//managed elsewhere
		} else if(connection!=null){
			try{
				connection.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * calls nextval on the sequence specified and returns the number.
	 * 
	 * @param sequenceName
	 *            - the oracle sequence name
	 * @return the next number in the seqence
	 * @throws SQLException
	 */
	public Long getNextSequenceValue(String sequenceName) {
		String query = "select " + sequenceName + ".nextval from dual";
		return queryForLong(query);
	}	
	
	
	
	private void setParams(PreparedStatement ps, Object... parameters)
			throws SQLException {
		Connection c=ps.getConnection();
		int numLiteralStrings = 0; 
		for(int i=1;i<parameters.length+1;i++){
			try{
				Object o = parameters[i-1];
				if(o instanceof OracleList){
					OracleList<? extends Object> l = (OracleList)o;
					ps.setArray(i-numLiteralStrings,l.getArray(Common.unwrap(c)));
				} else if(o instanceof SQLData){
					ps.setObject(i-numLiteralStrings, o);
				} else if(o instanceof Clob){
					ps.setClob(i-numLiteralStrings, ((Clob) o).getClob(Common.unwrap(c)));
				}else if(o instanceof NamedOutputParameter){
					
					//ps.registerOutParameter(i-numLiteralStrings, ((NamedOutputParameter) o).getJDBCType(), ((NamedOutputParameter) o).getSqlTypeName());
				} else if(o instanceof OutputParameter){
					//ps.registerOutParameter(i-numLiteralStrings, ((OutputParameter) o).getJDBCType());
				} else if(o instanceof java.util.Date){
					ps.setDate(i-numLiteralStrings, new java.sql.Date(((java.util.Date) o).getTime()));
					//cs.setTimestamp(i, new Timestamp(((java.util.Date) o).getTime()));
				} else if(o instanceof LiteralString){
					//do nothing here.
					numLiteralStrings++;
				} else {
					ps.setObject(i-numLiteralStrings, o);
				}
				
				if(o instanceof SQLObjectConverter){
					c.getTypeMap().put(((SQLObjectConverter<?>) o).getObjectSqlTypeName(), ((SQLObjectConverter<?>) o).getSqlDataClass());
				}
			}catch(SQLException sqle){
				SQLException e = new SQLException("Parameter "+i+":"+parameters[i-1]);
				e.initCause(sqle);
				throw e;
			}
		}
	}
	public Object queryForSingleValue(String query, Object... params){
		Map<String,Object> map = queryForMap(query,params);
		if(map.size()==0){
			throw new QueryResultException("Query returned no columns",query,params,QueryResultExceptionType.NO_COLUMNS);
		}
		if(map.size()>1){
			throw new QueryResultException("Query returned more than one column, only one column can be returned for queryForSingleValue",query,params,QueryResultExceptionType.TOO_MANY_COLUMNS);
		}
		return map.values().iterator().next();
	}
	
	public String queryForString(String query, Object... params){
		return ""+queryForSingleValue(query, params);
	}
	
	public BigInteger queryForBigInteger(String query, Object... params){
		
		BigDecimal result = (BigDecimal)queryForSingleValue(query, params);
		if(result==null){
			return null;
		}
		try{
			return result.toBigIntegerExact();
		} catch (ArithmeticException ae){
			throw new QueryResultException("Value returned was not an integer", query, params,QueryResultExceptionType.INCORRECT_RETURN_TYPE);
		}
	}
	
	public Long queryForLong(String query, Object... params){
		BigInteger result = (BigInteger)queryForBigInteger(query, params);
		if(result==null) return null;	
		if(result.compareTo(BigInteger.valueOf(Long.MAX_VALUE))>0){
			throw new QueryResultException("Value returned is greateer than Long.MAX_VALUE use queryForBigInteger",query,params,QueryResultExceptionType.INCORRECT_RETURN_TYPE);
		}
		return result.longValue();
	}
	
	public Map<String,Object> queryForMap(String query, Object... params){
		List<Map<String,Object>> list = queryForMapList(query,params);
		if(list.size()==0){
			throw new QueryResultException("Query returned no data",query,params,QueryResultExceptionType.NO_DATA_FOUND);
		}
		if(list.size()>1){
			throw new QueryResultException("Query returned more than one row",query,params,QueryResultExceptionType.TOO_MANY_ROWS);
		}
		return list.get(0);
	}
	
	public List<Object> queryForSingleColumnList(String query, Object... params){
		List<Map<String,Object>> list = queryForMapList(query,params);
		if(list.size()==0){
			return Collections.emptyList();
		}
		Map map = list.get(0);
		if(map.size()==0){
			throw new QueryResultException("Query returned no columns",query,params,QueryResultExceptionType.NO_COLUMNS);
		}
		if(map.size()>1){
			throw new QueryResultException("Query returned more than one column, only one column can be returned for queryForSingleColumnList",query,params,QueryResultExceptionType.TOO_MANY_COLUMNS);
		}
		List<Object> returnList = newList();
		for(Map<String,Object> row :list){
			returnList.add(row.values().iterator().next());
		}
		return returnList;
	}
	
	public List<Map<String,Object>> queryForMapList(String query, Object... params){
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			c=getConnection();
			try{
				ps = c.prepareStatement(query);
				setParams(ps,params);
				rs = ps.executeQuery();
			} catch (SQLException e){
				throw new DBCallException("Failed To Execute Query",query,params,e);
			}
			ResultSetMetaData rsmd = rs.getMetaData();
			List<Map<String,Object>> results = newList();
			
			while(rs.next()){
				Map<String,Object> row = newCaseInsensitiveMap();
				for(int i=1; i<=rsmd.getColumnCount();i++){
					row.put(rsmd.getColumnName(i), getResultSetValue(rs,i));
				}
				results.add(row);
			}
			return results;
		} catch (SQLException e){
			throw new RuntimeException(e);
		}finally{
			close(rs);
			close(ps);
			closeConnection(c);
		}
	}
	
	
	/**
	 * Retrieve a JDBC column value from a ResultSet, using the most appropriate
	 * value type. The returned value should be a detached value object, not having
	 * any ties to the active ResultSet: in particular, it should not be a Blob or
	 * Clob object but rather a byte array or String respectively.
	 * <p>Uses the <code>getObject(index)</code> method, but includes additional "hacks"
	 * to get around Oracle 10g returning a non-standard object for its TIMESTAMP
	 * datatype and a <code>java.sql.Date</code> for DATE columns leaving out the
	 * time portion: These columns will explicitly be extracted as standard
	 * <code>java.sql.Timestamp</code> object.
	 * @param rs is the ResultSet holding the data
	 * @param index is the column index
	 * @return the value object
	 * @throws SQLException if thrown by the JDBC API
	 * @see java.sql.Blob
	 * @see java.sql.Clob
	 * @see java.sql.Timestamp
	 */
	public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
		Object obj = rs.getObject(index);
		String className = null;
		if (obj != null) {
			className = obj.getClass().getName();
		}
		if (obj instanceof Blob) {
			obj = rs.getBytes(index);
		}
		else if (obj instanceof Clob) {
			obj = rs.getString(index);
		}
		else if (className != null &&
				("oracle.sql.TIMESTAMP".equals(className) ||
				"oracle.sql.TIMESTAMPTZ".equals(className))) {
			obj = rs.getTimestamp(index);
		}
		else if (className != null && className.startsWith("oracle.sql.DATE")) {
			String metaDataClassName = rs.getMetaData().getColumnClassName(index);
			if ("java.sql.Timestamp".equals(metaDataClassName) ||
					"oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
				obj = rs.getTimestamp(index);
			}
			else {
				obj = rs.getDate(index);
			}
		}
		else if (obj != null && obj instanceof java.sql.Date) {
			if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
				obj = rs.getTimestamp(index);
			}
		}
		return obj;
	}

}
