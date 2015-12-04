package com.blocktopus.oracle;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.Statement;

import com.blocktopus.oracle.types.Clob;
import com.blocktopus.oracle.types.LiteralString;
import com.blocktopus.oracle.types.NamedOutputParameter;
import com.blocktopus.oracle.types.OracleList;
import com.blocktopus.oracle.types.OutputParameter;
import com.blocktopus.oracle.types.SQLObjectConverter;

import oracle.jdbc.OracleConnection;

public class Common {
	
	public static PreparedStatement setParams(PreparedStatement s, Object... params) throws SQLException {
		Connection c=s.getConnection();
		int numLiteralStrings = 0; 
		for(int i=1;i<params.length+1;i++){
			Object o = params[i-1];
				if(o instanceof OracleList){
					OracleList l = (OracleList)o;
					s.setArray(i-numLiteralStrings,l.getArray(Common.unwrap(c)));
				} else if(o instanceof SQLData){
					s.setObject(i-numLiteralStrings, o);
				} else if(o instanceof Clob){
					s.setClob(i-numLiteralStrings, ((Clob) o).getClob(Common.unwrap(c)));
				}else if(o instanceof NamedOutputParameter){
					
					//ps.registerOutParameter(i-numLiteralStrings, ((NamedOutputParameter) o).getJDBCType(), ((NamedOutputParameter) o).getSqlTypeName());
				} else if(o instanceof OutputParameter){
					//ps.registerOutParameter(i-numLiteralStrings, ((OutputParameter) o).getJDBCType());
				} else if(o instanceof java.util.Date){
					s.setDate(i-numLiteralStrings, new java.sql.Date(((java.util.Date) o).getTime()));
					//cs.setTimestamp(i, new Timestamp(((java.util.Date) o).getTime()));
				} else if(o instanceof LiteralString){
					//do nothing here.
					numLiteralStrings++;
				} else {
					s.setObject(i-numLiteralStrings, o);
				}
				
				if(o instanceof SQLObjectConverter){
					c.getTypeMap().put(((SQLObjectConverter<?>) o).getObjectSqlTypeName(), ((SQLObjectConverter<?>) o).getSqlDataClass());
				}
				
		}
		return s;
	}
	
	public static OracleConnection unwrap(Connection c) {
		if (c instanceof OracleConnection) {
			return (OracleConnection) c;
		}
		try {
			Method m = c.getClass().getMethod("unwrap",
					new Class[] { Class.class });
			return (OracleConnection) m.invoke(c,
					new Object[] { OracleConnection.class });
		} catch (Exception e) {
			throw new RuntimeException("Unwrapping connections on java 1.5 is currently unsupported",e);
		}
	}

	public static void close(ResultSet rs){
		if(rs!=null){
			try{
			rs.close();
			} catch (SQLException e){
				
			}
		}
	}
	public static void close(Statement s){
		if(s!=null){
			try{
				s.close();
			} catch (SQLException e){
				
			}
		}
	}
	public static void close(Connection c){
		if(c!=null){
			try{
				c.close();
			} catch (SQLException e){
				
			}
		}
	}
}
