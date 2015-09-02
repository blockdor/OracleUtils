package com.blocktopus.oracle.types;

import java.sql.SQLException;

import oracle.jdbc.OracleConnection;
import oracle.sql.CLOB;

public class Clob {
	private String characterData;

	public Clob(String characterData){
		this.setCharacterData(characterData);
	}

	public String getCharacterData() {
		return characterData;
	}

	public void setCharacterData(String characterData) {
		this.characterData = characterData;
	}

	public java.sql.Clob getClob(OracleConnection c){
		CLOB theClob = null;
		try {
			
			if (characterData != null && characterData.length() != 0) {
				theClob = oracle.sql.CLOB.createTemporary(c,
						false,
						oracle.sql.CLOB.DURATION_SESSION);
				theClob.setString(1, characterData);
			}
			return theClob;
		}catch (SQLException e){
				throw new RuntimeException("Error creating the oracle.sql.CLOB", e);
		}
	}
}
