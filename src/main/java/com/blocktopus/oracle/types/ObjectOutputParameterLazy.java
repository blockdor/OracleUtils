package com.blocktopus.oracle.types;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

import oracle.sql.AttributeDescriptor;
import oracle.sql.STRUCT;
import static com.blocktopus.common.CollectionUtils.*;

/**
 * For when you need to retrieve an object from a procedure but can't be bothered to make an SQLdata implementation for it.
 * <p> instead returns the object as a map of string, value pairs.
 * @author Block
 *
 */
public class ObjectOutputParameterLazy extends OutputParameter {

	
	@Override
	public Map<String,Object> getParameter(){
		STRUCT s = (STRUCT)super.getParameter();
		Map<String,Object> mapRep = newMap();
		try{
			Object[] attrs = s.getAttributes();
			AttributeDescriptor[] descs = s.getDescriptor().getAttributesDescriptor();
			for(int i=0;i<attrs.length;i++){
				mapRep.put(descs[i].getAttributeName(), attrs[i]);
			}
			return mapRep;
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public int getJDBCType() {
		return Types.STRUCT;
	}

}
