package com.blocktopus.schema;

import static com.blocktopus.common.CollectionUtils.*;

import java.util.LinkedHashMap;

import com.blocktopus.schema.data.RowData;
import com.blocktopus.schema.data.PrimaryKeyId;

public class DataCache {
	
	public static DataCache getInstance(){
		return new DataCache();
	}
	
	private DataCache(){
		
	}
	private int cacheSize = 1000;

	public int getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}

	private LinkedHashMap<PrimaryKeyId, RowData> cache = newLinkedMap();

	public RowData getDataFromCache(PrimaryKeyId pkid) {
		if (cache.containsKey(pkid)) {
			return cache.get(pkid);
		}
		return null;
	}

	public void addToCache(PrimaryKeyId pkid, RowData dr) {
		cache.put(pkid, dr);
		if (cache.size() > cacheSize) {
			PrimaryKeyId key = cache.keySet().iterator().next();
			RowData d = cache.get(key);
			cache.remove(key);
		}
	}

}
