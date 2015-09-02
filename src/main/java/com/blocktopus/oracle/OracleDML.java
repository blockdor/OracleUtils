package com.blocktopus.oracle;

import static com.blocktopus.oracle.Common.close;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.blocktopus.oracle.exception.DBCallException;
import com.blocktopus.oracle.types.Clob;
import com.blocktopus.oracle.types.LiteralString;
import com.blocktopus.oracle.types.NamedOutputParameter;
import com.blocktopus.oracle.types.OracleList;
import com.blocktopus.oracle.types.OutputParameter;
import com.blocktopus.oracle.types.PrimitiveList;
import com.blocktopus.oracle.types.SQLObjectConverter;

public class OracleDML {
	private DataSource dataSource;
	private Connection connection;
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public Connection getConnection() {
		if(connection==null){
			if(dataSource!=null){
				try{
					return dataSource.getConnection();
				} catch (SQLException e){
					throw new RuntimeException("Could not get Connection from Datasource",e);
				}
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
		if(this.connection==connection){
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
	 * executes the dml statement in bulk using lists of values for each column,
	 * e.g. if the dml has 2 binding parameters then 2 lists of type String or Number must be passed in with the DML.
	 * These lists must be of the same size.
	 * Currently if lists were different sizes the first lists length would be used and only cause error if the other lists were shorter.
	 *
	 * @param dml - the dml statement to execute
	 * @param parametersToBind
	 *            , List<String> or List<? extends Number> only are supported.
	 * @throws SQLException
	 *
	 *
	 */
	@SuppressWarnings("rawtypes")
	public void executeForAll(String dml, PrimitiveList<?>... parametersToBind){
		if(parametersToBind==null)
			throw new NullPointerException("parametersToBind cannot be null");
		if(parametersToBind.length==0)
			throw new IllegalArgumentException("parametersToBind cannot have 0 length");
		if(dml==null)
			throw new NullPointerException("dml cannot be null");
		if(dml.equals("")){
			throw new RuntimeException("dml cannot be empty");
		}
		int length=-1;
		for (List c : parametersToBind) {
			
			if (c.isEmpty()) {
				throw new RuntimeException("All lists must contain values");
			} 
			if(length!=-1){
				if(length!=c.size()){
					throw new RuntimeException("All lists must be of same size");
				}
			}
			length = c.size();
		}
		Connection conn = null;
		CallableStatement cs = null;
		StringBuilder sb = new StringBuilder();
		List<Array> params = new ArrayList<Array>();
		try {
			conn = getConnection();
			sb.append("declare ");
			int v = 1;		
			for (PrimitiveList c : parametersToBind) {
				sb.append("v" + v++);
				sb.append(" "+c.getSqlTypeName()+" := ?; ");
				try{
					params.add(c.getArray(Common.unwrap(conn)));
				} catch (SQLException e){
					throw new RuntimeException("Failure creating oracle.sql.Array",e);
				}
			}
			sb.append("begin forall i in 1..v1.count ");
			sb.append(replaceQuestionMarks(dml));
			sb.append("; end;");
			cs = conn.prepareCall(sb.toString());
			setParams(cs, params);
			cs.execute();
		} catch (SQLException e) {
			throw new DBCallException("Error during forall",
					sb.toString(),
					parametersToBind,
					e);
		}finally {
			close(cs);
			closeConnection(conn);
		}
	}


	private String replaceQuestionMarks(String sql) {
		int v = 1;
		StringBuilder sb = new StringBuilder(sql);
		for (int i = 0; i < sb.length(); i++) {
			if (sb.charAt(i) == '?') {

				sb.deleteCharAt(i);
				sb.insert(i, "v" + v++ + "(i)");
			}
		}
		return sb.toString();
	}

	private void setParams(CallableStatement ps, List<Array> objects)
			throws SQLException {
		int x = 1;
		for (Array o : objects) {
			ps.setArray(x, o);
			x++;
		}
	}

	/**
	 * executes a simple dml statement
	 * @param dml
	 * @param params
	 */
	public void executeDML(String dml, Object... params) {
		PreparedStatement s = null;
		Connection conn = null;
		StringBuffer sb = new StringBuffer();
		sb.append(dml);
		int parameterIndex=0;
		for(int j=0;j<sb.length();j++){
			if(sb.charAt(j)=='?'){
				if(params[parameterIndex] instanceof LiteralString){
					LiteralString ls = (LiteralString)params[parameterIndex];
					sb.deleteCharAt(j);
					sb.insert(j, ls.getLiteralString());
				}
				parameterIndex++;
			}
		}

		try {
			conn = getConnection();
			s = conn.prepareStatement(sb.toString());
			setParams(s, params);

			s.execute();
		} catch (SQLException e) {
			throw new DBCallException("Error while executing DML", sb.toString(), params, e);
		}finally {
			close(s);
			closeConnection(conn);
		}
	}

	private void setParams(PreparedStatement ps, Object... parameters)
			throws SQLException {
		Connection c=ps.getConnection();
		int numLiteralStrings = 0; 
		for(int i=1;i<parameters.length+1;i++){
			Object o = parameters[i-1];
				if(o instanceof OracleList){
					OracleList l = (OracleList)o;
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
				
		}
	}
}
