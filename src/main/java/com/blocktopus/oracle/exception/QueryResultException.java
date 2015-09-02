package com.blocktopus.oracle.exception;

public class QueryResultException extends DBCallException {

	public QueryResultException(
			String message,
			String callString,
			Object[] params,
			QueryResultExceptionType type) {
		super(message, callString, params);
		this.type = type;
	}
	public enum QueryResultExceptionType{
		NO_DATA_FOUND, //data
		TOO_MANY_ROWS, //data
		NO_COLUMNS, //query error/incorrect method used
		TOO_MANY_COLUMNS, //query error/incorrect method used
		INCORRECT_RETURN_TYPE //query error/incorrect method used
	}
	private QueryResultExceptionType type;
	public QueryResultExceptionType getType() {
		return type;
	}
	public void setType(QueryResultExceptionType type) {
		this.type = type;
	}

	
}
