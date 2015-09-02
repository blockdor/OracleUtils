package com.blocktopus.oracle.types;

/**
 * Models a parameter whose value will be determined by the db during execution e.g. SYSDATE or CURRENT_TIMESTAMP
 * @author Block
 *
 */
public class LiteralString{
	private String literalString;
	public LiteralString(String literalString){
		this.literalString=literalString;
	}
	public String getLiteralString() {
		return literalString;
	}
	public void setLiteralString(String literalString) {
		this.literalString = literalString;
	}
}
