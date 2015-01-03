package ch.fhnw.cpib.compiler.generator;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public final class StoreTable {
	
	private Map<String, Object> storeMap;
	
	public StoreTable() {
		storeMap = new TreeMap<String, Object>();
	}
	
	public boolean addStore(final Store store) {
		if (!storeMap.containsKey(store.getIdent())) {
			storeMap.put(store.getIdent(), store);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean addStore(final String ident, final ArrayList<Store> stores) {
		if (!storeMap.containsKey(ident)) {
			storeMap.put(ident, stores);
			return true;
		} else {
			return false;
		}
	}
	
	public Object getStore(final String ident) {
		return storeMap.get(ident);
	}
	
	public Map<String, Object> getTable() {
		return storeMap;
	}
}
