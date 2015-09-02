package com.blocktopus.oracle.exception;
import static com.blocktopus.common.CollectionUtils.*;

public class DBCallException extends RuntimeException {
	
	/**
	 * 
	 * @param message - the message
	 * @param callString - sql/plsql being sent to the DB
	 * @param params - params of that sql/plsql
	 * @param cause - sql exception that caused the error
	 */
	public DBCallException(String message, String callString, Object[] params, Throwable cause){
		super(message,cause);
		this.callString=callString;
		this.params=params;
	}
	/**
	 * If not caused by a SQLException consider using a subclass to better show why the exception is raised
	 * @param message - the message
	 * @param callString - sql/plsql being sent to the DB
	 * @param params - params of that sql/plsql
	 */
	public DBCallException(String message, String callString, Object[] params){
		super(message);
		this.callString=callString;
		this.params=params;
	}
	public DBCallException(String message,Throwable cause){
		super(message,cause);
	}
	private String callString;
	private Object[] params;
	public String getQuery() {
		return callString;
	}
	
	public void setCallString(String callString) {
		this.callString = callString;
	}
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}

	@Override
	public String getMessage(){	
		return super.getMessage()+":"+callString+":"+(params!=null?arrayToString(params):"[]")+":"+(getCause()!=null?getCause().getMessage():"");
	}
}
