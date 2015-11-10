package cn.jc.common.utils;

import java.util.LinkedHashMap;

public class FixedSizeLinkedHashMap extends LinkedHashMap{

	/**
	 * 
	 */
	private static final long serialVersionUID = -17037127377571198L;
	
	private static int MAX_ENTRIES = 10;
	
	public static void setMAX_ENTRIES(int max_entries) {
		MAX_ENTRIES = max_entries;
	}
	
	
	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry eldest) {		
		return size() > MAX_ENTRIES;
	}
}
