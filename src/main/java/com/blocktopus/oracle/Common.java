package com.blocktopus.oracle;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleConnection;

public class Common {
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
