package com.blocktopus.oracle.types;

public class Response {
	private Long errorcode;
	private String errortext;
	private String returnCode;

	public Response(){

	}
	public Response(Long errorcode, String errortext, String returnCode) {
		super();
		this.errorcode = errorcode;
		this.errortext = errortext;
		this.returnCode = returnCode;
	}
	public Long getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(Long errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrortext() {
		return errortext;
	}
	public void setErrortext(String errortext) {
		this.errortext = errortext;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
}
