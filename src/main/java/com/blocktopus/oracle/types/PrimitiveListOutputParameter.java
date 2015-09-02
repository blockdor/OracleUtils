package com.blocktopus.oracle.types;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import static com.blocktopus.common.CollectionUtils.*;

/**
 * for retrieving nested tables of oracle primitive vales e.g. number/date from an OUT parameter
 * @author Block
 *
 */
public class PrimitiveListOutputParameter<T> extends NamedOutputParameter {

	public PrimitiveListOutputParameter(String sqlTypeName,Class<T> listBaseType) {
	    setSqlTypeName(sqlTypeName.toUpperCase());
		this.listBaseType=listBaseType;
	}

	private Class<T> listBaseType;

	@Override
	public void setParameter(Object obj){
		Array array = (Array)obj;
		if(array==null){
			super.setParameter(null);
			return;
		}
		List<T> list = newList();
		try {
			for(Object o:(Object[])array.getArray()){
				if(o instanceof BigDecimal){
					T value = convertTo((BigDecimal)o, listBaseType);
					list.add(value);
				}
				if(o instanceof Timestamp){
					T value = convertTo((Timestamp)o,listBaseType);
					list.add(value);
				}
				list.add((T)o);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		super.setParameter(list);	
	}
	@Override
	public List<T> getParameter(){
		return (List<T>)super.getParameter();
	}
	
	public Array getRawParameter(){
		return (Array)super.getParameter();
	}

	public int getJDBCType() {
		return Types.ARRAY;
	}
	
	/**
	 * supported values for "to" <br/>
	 * Long<br/>
	 * Double<br/>
	 * Integer<br/>
	 * Float<br/>
	 * BigDecimal<br/>
	 * BigInteger<br/>
	 * 
	 * @param bd
	 * @param to
	 * @return
	 */
	private <N> N convertTo(BigDecimal n, Class<N> to){
		if(Long.class.equals(to)){
			return (N)Long.valueOf(n.longValueExact());
		}
		if(Integer.class.equals(to)){
			return (N)Integer.valueOf(n.intValueExact());
		}
		if(Double.class.equals(to)){
			Double d = n.doubleValue();
			if(d.isInfinite()){
				throw new ArithmeticException("Value too large for a double");
			}
			if(!n.equals(BigDecimal.valueOf(d))){
				throw new ArithmeticException("Precision lost in conversion to double");
			}
			return (N)d;
		}
		if(Float.class.equals(to)){
			Float f = Float.valueOf(n.floatValue());
			if(f.isInfinite()){
				throw new ArithmeticException("Value too large for a float");
			}
			if(!n.equals(BigDecimal.valueOf(f))){
				throw new ArithmeticException("Precision lost in conversion to float");
			}
			return (N)f;
		}
		if(BigInteger.class.equals(to)){
			return(N)n.toBigIntegerExact();
		}
		if(BigDecimal.class.equals(to)){
			return (N)n;
		}
		throw new RuntimeException("unsupported \"to\" class, checkjavadoc:"+to.getName());
	}
	private <N> N convertTo(Timestamp t, Class<N> to){
		if(Date.class.equals(to)){
			return (N)new Date(t.getTime());
		}
		if(Timestamp.class.equals(to)){
			return (N)t;
		}
		throw new RuntimeException("unsupported \"to\" class, checkjavadoc:"+to.getName());
	}
}
