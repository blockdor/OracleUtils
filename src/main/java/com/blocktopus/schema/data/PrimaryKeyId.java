package com.blocktopus.schema.data;

import java.util.Arrays;
import java.util.List;

import com.blocktopus.common.exceptions.BadConstructorParameterException;
import com.blocktopus.schema.model.PrimaryKey;

import static com.blocktopus.common.CollectionUtils.*;

public class PrimaryKeyId {
	private PrimaryKey primaryKey;

	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public PrimaryKeyId(PrimaryKey primaryKey, List<Object> id) {
		super();
		if(primaryKey==null){
			throw new BadConstructorParameterException("PrimaryKey Object cannot be null in constructor of PrimaryKeyId");
		}
		if(id==null){
			throw new BadConstructorParameterException("Id List cannot be null in constructor of PrimaryKeyId");
		}
		for(Object o:id){
			if(o==null){
				throw new BadConstructorParameterException("Id in id list cannot be null in constructor of PrimaryKeyId");
			}
		}
		this.primaryKey = primaryKey;
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrimaryKeyId other = (PrimaryKeyId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}

	public PrimaryKeyId(PrimaryKey primaryKey, Object... id) {
		super();
		if(primaryKey==null){
			throw new BadConstructorParameterException("PrimaryKey Object cannot be null in constructor of PrimaryKeyId");
		}
		if(id==null){
			throw new BadConstructorParameterException("Id List cannot be null in constructor of PrimaryKeyId");
		}
		for(Object o:id){
			if(o==null){
				throw new BadConstructorParameterException("Id in id list cannot be null in constructor of PrimaryKeyId");
			}
		}
		if(primaryKey.getColumns().size()!= id.length){
			throw new BadConstructorParameterException("Incorrect number of objects for id in constructor of PrimaryKeyId (given "+id.length+",expected "+ primaryKey.getColumns().size()+")");
		}
		this.primaryKey = primaryKey;
		this.id = newList();
		this.id.addAll(Arrays.asList(id));
	}
	public void setPrimaryKey(PrimaryKey primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public List<Object> getId() {
		return id;
	}

	public void setId(List<Object> id) {
		if(id.size()!=primaryKey.size()){
			throw new RuntimeException("number of ids do not match the number of columns in primary key");
		}
		this.id = id;
	}

	private List<Object> id;
	
	
	
}
