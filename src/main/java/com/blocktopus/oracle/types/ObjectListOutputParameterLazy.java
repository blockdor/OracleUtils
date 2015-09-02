package com.blocktopus.oracle.types;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import oracle.sql.ARRAY;
import oracle.sql.AttributeDescriptor;
import oracle.sql.STRUCT;
import static com.blocktopus.common.CollectionUtils.*;

/**
 * For when you need to retrieve an object from a procedure but can't be bothered to make an SQLdata implementation for it.
 * <p> instead returns the object as a map of string, value pairs.
 * @author Block
 *
 */
public class ObjectListOutputParameterLazy extends NamedOutputParameter{

	public ObjectListOutputParameterLazy(String sqlTypeName) {
		setSqlTypeName(sqlTypeName);
	}
	
	/**
	 * returns a List of map objects holding the attributes of the sql Objects returned.
	 */
	@Override
	public List<Map<String,Object>> getParameter(){
		try{
			Array a = (Array)super.getParameter();
			Object[] structs = (Object[])a.getArray();
			List<Map<String,Object>> results = newList();
			for(Object o:structs){
				STRUCT s = (STRUCT)o;
				Map<String,Object> mapRep = newCaseInsensitiveMap();
				Object[] attrs = s.getAttributes();
				AttributeDescriptor[] descs = s.getDescriptor().getAttributesDescriptor();
				for(int i=0;i<attrs.length;i++){
					mapRep.put(descs[i].getAttributeName(), attrs[i]);
				}
				results.add(mapRep);
			}
			return results;
		} catch (SQLException e){
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * gets the raw param returned by the oracle jdbc drivers.<br/>
	 * Will be an {@link java.sql.Array} of {@link oracle.sql.STRUCT}.
	 * <p> for a simpler life use getParameter
	 * @return {@link java.sql.Array}
	 */
	public Array getRawParameter(){
		return (Array)super.getParameter();
	}

	@Override
	public int getJDBCType() {
		return Types.ARRAY;
	}

}
