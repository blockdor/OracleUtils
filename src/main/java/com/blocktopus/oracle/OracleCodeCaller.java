package com.blocktopus.oracle;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import com.blocktopus.oracle.exception.DBCallException;
import com.blocktopus.oracle.types.LiteralString;
import com.blocktopus.oracle.types.NamedOutputParameter;
import com.blocktopus.oracle.types.OutputParameter;
import com.blocktopus.oracle.types.Clob;
import com.blocktopus.oracle.types.OracleList;
import com.blocktopus.oracle.types.PrimitiveOutputParameter;
import com.blocktopus.oracle.types.Response;
import com.blocktopus.oracle.types.SQLObjectConverter;

import static com.blocktopus.common.CollectionUtils.*;
import static com.blocktopus.oracle.Common.*;

/**
 * For calling oracle code!<br/>
 * Give it some PLSQL and any bind params you wish to use and it'll do the rest.<br/>
 * There are many objects in com.blocktopus.oracle.types you can use as parameters to enable outputParameters, Lists, ListsofObjects, Clobs etc.<br/>
 * @author Block
 *
 */
public class OracleCodeCaller {
	
	public enum ResponseType{
		ErrorCodeAndErrorText (2),
		ErrorCodeErrorTextAndReturnCode(3),
		NoResponse(0);
		public int numOutputParams;
		private ResponseType(int numOutputParams){
			this.numOutputParams = numOutputParams;
		}
	}
	private DataSource dataSource;
	private Connection connection;
	private boolean autoCommit = true;
	public void setAutoCommit(boolean commit){
		autoCommit=commit;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public Connection getConnection() throws SQLException {
		if(connection==null){
			if(dataSource!=null){
				Connection c = dataSource.getConnection();
				//c.setAutoCommit(autoCommit);
				return c;
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

	private String buildProcedureString(String storedProcedureName,Object[] parameters){
		//build string
		StringBuilder plsql = new StringBuilder();
		plsql.append("begin "+storedProcedureName+"(");
		for(int i=0;i<parameters.length-1;i++){
			if(parameters[i] instanceof LiteralString){
				plsql.append(((LiteralString)parameters[i]).getLiteralString()+",");
			} else {
				plsql.append("?,");
			}
		}
		if(parameters.length>0){
			if(parameters[parameters.length-1] instanceof LiteralString){
				plsql.append(((LiteralString)parameters[parameters.length-1]).getLiteralString());
			} else {
				plsql.append("?");
			}
		}
		plsql.append("); end;");
		return plsql.toString();
	}
	private String buildFunctionString(String functionName,Object[] parameters){
		//build string
		StringBuilder plsql = new StringBuilder();
		plsql.append("begin ? := "+functionName+"(");
		if(parameters.length>=1){
			for(int i=0;i<parameters.length-1;i++){
				if(parameters[i] instanceof LiteralString){
					plsql.append(((LiteralString)parameters[i]).getLiteralString()+",");
				} else {
					plsql.append("?,");
				}
			}
			if(parameters[parameters.length-1] instanceof LiteralString){
				plsql.append(((LiteralString)parameters[parameters.length-1]).getLiteralString());
			} else {
				plsql.append("?");
			}
		}
		plsql.append("); end;");
		return plsql.toString();
	}
	
	public Response callCramerFunction(String functionName,OutputParameter functionOutput, ResponseType rt, Object... parameters){
		List<Object> params = newList();
		switch(rt){
		case ErrorCodeAndErrorText:
			params.add(new PrimitiveOutputParameter<BigDecimal>(BigDecimal.class));
			params.add(new PrimitiveOutputParameter<String>(String.class));
			break;
		case ErrorCodeErrorTextAndReturnCode:
			params.add(new PrimitiveOutputParameter<BigDecimal>(BigDecimal.class));
			params.add(new PrimitiveOutputParameter<String>(String.class));
			params.add(new PrimitiveOutputParameter<String>(String.class));
			break;
		case NoResponse:
		default:
			break;
		}
		params.addAll(Arrays.asList(parameters));
		callFunction(functionName,functionOutput,params);
		Response r = new Response();
		switch(rt){
		case ErrorCodeErrorTextAndReturnCode:
			r.setReturnCode(((PrimitiveOutputParameter<String>)params.get(3)).getParameter());
			//no break here as we reuse the errorcode and errortext code below.
		case ErrorCodeAndErrorText:
			params.get(0);
			BigDecimal errorCode = (BigDecimal)((PrimitiveOutputParameter<BigDecimal>)params.get(1)).getParameter();
			r.setErrorcode(errorCode==null?null:errorCode.longValue());
			r.setErrortext((String)((PrimitiveOutputParameter<String>)params.get(2)).getParameter());
			break;
		case NoResponse:
			break;
		}
		return r;
	}
	@Deprecated
	public Response callOracleFunction(String functionName,OutputParameter functionOutput, ResponseType rt, Object... parameters){
		return callCramerFunction(functionName, functionOutput, rt, parameters);
	}
	public List<OutputParameter> callFunction(String functionName,OutputParameter functionOutput,Object... parameters){

		List<Object> params = newList();
		String plsql = buildFunctionString(functionName, parameters);
		params.add(functionOutput);

		params.addAll(Arrays.asList(parameters));
		runOracleCode(plsql,params.toArray());
		functionOutput=(OutputParameter)params.get(0);
		
		List<OutputParameter> output = newList();
		for(Object p:params){
			if(p instanceof OutputParameter){
				output.add((OutputParameter)p);
			}
		}
		return output;
		
	}

	public Response callStoredProcedure(String procedureName, ResponseType rt, Object... parameters){
		Response r = new Response();
		List<Object> params = newList();
		switch(rt){
		case ErrorCodeAndErrorText:
			params.add(new PrimitiveOutputParameter<BigDecimal>(BigDecimal.class));
			params.add(new PrimitiveOutputParameter<String>(String.class));
			break;
		case ErrorCodeErrorTextAndReturnCode:
			params.add(new PrimitiveOutputParameter<BigDecimal>(BigDecimal.class));
			params.add(new PrimitiveOutputParameter<String>(String.class));
			params.add(new PrimitiveOutputParameter<String>(String.class));
			break;
		case NoResponse:
		default:
			break;
		}
		params.addAll(Arrays.asList(parameters));
		callStoredProcedure(procedureName,params.toArray());
		switch(rt){
		case ErrorCodeErrorTextAndReturnCode:
			r.setReturnCode(((PrimitiveOutputParameter<String>)params.get(2)).getParameter());
			//no break here as we reuse the errorcode and errortext code below.
		case ErrorCodeAndErrorText:
			BigDecimal errorCode = (BigDecimal)((PrimitiveOutputParameter<BigDecimal>)params.get(0)).getParameter();
			r.setErrorcode(errorCode==null?null:errorCode.longValue());
			r.setErrortext((String)((PrimitiveOutputParameter<String>)params.get(1)).getParameter());
			break;
		case NoResponse:
		default:
			break;
		}
		return r;
		
	}
	public List<OutputParameter> callStoredProcedure(String procedureName, Object... parameters){

		String plsql = buildProcedureString(procedureName, parameters);
		runOracleCode(plsql,parameters);
		
		List<OutputParameter> output = newList();
		for(Object p:parameters){
			if(p instanceof OutputParameter){
				output.add((OutputParameter)p);
			}
		}
		return output;
	}
	
	/**
	 * 
	 * @param plsql
	 * @param parameters
	 */
	public void runOracleCode(String plsql, Object... parameters){

		Connection c = null;
		CallableStatement cs = null;

		try{
			c=getConnection();
			cs = c.prepareCall(plsql);
			
			int numLiteralStrings = 0; 
			
			//register params
			for(int i=1;i<parameters.length+1;i++){
				Object o = parameters[i-1];
					if(o instanceof OracleList){
						OracleList<? extends Object> l = (OracleList)o;
						cs.setArray(i-numLiteralStrings,l.getArray(Common.unwrap(c)));
					} else if(o instanceof SQLData){
						cs.setObject(i-numLiteralStrings, o);
					} else if(o instanceof Clob){
						cs.setClob(i-numLiteralStrings, ((Clob) o).getClob(Common.unwrap(c)));
					}else if(o instanceof NamedOutputParameter){
						cs.registerOutParameter(i-numLiteralStrings, ((NamedOutputParameter) o).getJDBCType(), ((NamedOutputParameter) o).getSqlTypeName());
					} else if(o instanceof OutputParameter){
						cs.registerOutParameter(i-numLiteralStrings, ((OutputParameter) o).getJDBCType());
					} else if(o instanceof java.util.Date){
						cs.setDate(i-numLiteralStrings, new java.sql.Date(((java.util.Date) o).getTime()));
						//cs.setTimestamp(i, new Timestamp(((java.util.Date) o).getTime()));
					} else if(o instanceof LiteralString){
						//do nothing here.
						numLiteralStrings++;
					} else {
						cs.setObject(i-numLiteralStrings, o);
					}
					
					if(o instanceof SQLObjectConverter){
						c.getTypeMap().put(((SQLObjectConverter<?>) o).getObjectSqlTypeName(), ((SQLObjectConverter<?>) o).getSqlDataClass());
					}
					
			}
			
			//execute
			cs.execute();

			for(int i=1;i<parameters.length+1;i++){
				if(parameters[i-1] instanceof OutputParameter){
					((OutputParameter) parameters[i-1]).setParameter(getOutputParameterValue(cs, i));
				}
			}

		} catch (SQLException sqle){
			throw new DBCallException("Error executing Code", plsql,parameters,sqle);
		} finally {
			close(cs);
			closeConnection(c);
		}

	}
	
	/**
	 * Retrieve an Object from the statement, using the most appropriate
	 * value type. The returned value should be a detached value object, not having
	 * any ties to the active ResultSet: in particular, it should not be a Blob or
	 * Clob object but rather a byte array or String respectively.
	 * <p>Uses the <code>getObject(index)</code> method, but includes additional "hacks"
	 * to get around Oracle 10g returning a non-standard object for its TIMESTAMP
	 * datatype and a <code>java.sql.Date</code> for DATE columns leaving out the
	 * time portion: These columns will explicitly be extracted as standard
	 * <code>java.sql.Timestamp</code> object.
	 * @param cs is the ResultSet holding the data
	 * @param index is the column index
	 * @return the value object
	 * @throws SQLException if thrown by the JDBC API
	 * @see java.sql.Blob
	 * @see java.sql.Clob
	 * @see java.sql.Timestamp
	 */
	public static Object getOutputParameterValue(CallableStatement cs, int index) throws SQLException {
		
		Object obj = cs.getObject(index);
		String className = null;
		if (obj != null) {
			className = obj.getClass().getName();
		}
		if (obj instanceof java.sql.Blob) {
			obj = cs.getBytes(index);
		}
		else if (obj instanceof java.sql.Clob) {
			obj = cs.getString(index);
		}
		else if (className != null &&
				("oracle.sql.TIMESTAMP".equals(className) ||
				"oracle.sql.TIMESTAMPTZ".equals(className))) {
			obj = cs.getTimestamp(index);
		}
		else if (className != null && className.startsWith("oracle.sql.DATE")) {
			String metaDataClassName = cs.getMetaData().getColumnClassName(index);
			if ("java.sql.Timestamp".equals(metaDataClassName) ||
					"oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
				obj = cs.getTimestamp(index);
			}
			else {
				obj = cs.getDate(index);
			}
		}
		else if (obj != null && obj instanceof java.sql.Date) {
			if ("java.sql.Timestamp".equals(cs.getMetaData().getColumnClassName(index))) {
				obj = cs.getTimestamp(index);
			}
		}
		return obj;
	}
	
}
