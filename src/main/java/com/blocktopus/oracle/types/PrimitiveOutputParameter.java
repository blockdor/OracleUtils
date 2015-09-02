package com.blocktopus.oracle.types;

import java.sql.Types;

/**
 * for retrieving a value in an OUT parameter from a procedure.
 * @author Block
 *
 */
public class PrimitiveOutputParameter<T> extends OutputParameter{
	
	private Class<T> parameterClass;
	
	public PrimitiveOutputParameter(Class<T> parameterClass){
		this.parameterClass=parameterClass;
	}

	public void setParameter(Object o){
		try{
			T object = (T)o;
		} catch (ClassCastException cce){
			throw new ClassCastException("Parameter must be of type:"+parameterClass.getName()+" instead of "+o.getClass());
		}
		super.setParameter(o);
		
	}
	public T getParameter(){
		return (T)super.getParameter();
	}
	public int getJDBCType() {
		//so what types do we support!
		if(parameterClass!=null){
			if("java.lang.String".equals(parameterClass.getName())){
				return Types.VARCHAR;
			}
			if("java.lang.Long".equals(parameterClass.getName())){
				return Types.BIGINT;
			}
			if("java.math.BigDecimal".equals(parameterClass.getName())){
				return Types.NUMERIC;
			}
			if("java.lang.Integer".equals(parameterClass.getName())){
				return Types.INTEGER;
			}
			if("java.lang.Double".equals(parameterClass.getName())){
				return Types.DOUBLE;
			}
			if("java.lang.Float".equals(parameterClass.getName())){
				return Types.REAL;
			}	
			if("java.util.Date".equals(parameterClass.getName())){
				return Types.DATE;
			}
			if("java.sql.Timestamp".equals(parameterClass.getName())){
				return Types.TIMESTAMP;
			}
		}
		throw new RuntimeException(parameterClass.getName()+" is unsupported");
	}

	public String toString(){
		return "Param:"+getParameter()+" type:"+getTypeName()+" JavaClass:"+parameterClass.getName();
	}
	
	private String getTypeName(){

		switch(getJDBCType()){
		case Types.NUMERIC:
			return "Numeric";
		case Types.BIGINT:
			return "BigInt";
		case Types.DECIMAL:
			return "Decimal";
		case Types.INTEGER:
			return "Integer";
		case Types.DOUBLE:
			return "Double";
		case Types.FLOAT:
			return "Float";
		case Types.SMALLINT:
			return "SmallInt";
		case Types.TINYINT:
			return "TinyInt";
		case Types.VARCHAR:
			return "Varchar";
		case Types.DATE:
			return "Date";
		case Types.TIMESTAMP:
			return "Tiemestamp";
		default:
			return "Type ID:"+getJDBCType();		
		}
	}

	

}
