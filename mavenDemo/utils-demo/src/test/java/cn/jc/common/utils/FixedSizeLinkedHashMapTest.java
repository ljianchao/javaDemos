package cn.jc.common.utils;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class FixedSizeLinkedHashMapTest {

	@Test
	public void testRemoveEldestEntryEntry() {
		Map<Integer, String> map = new FixedSizeLinkedHashMap();
		System.out.println(map.size());
		
		for (int i = 0; i < 50; i++) {
			map.put(i, "entry" + i);
			System.out.println(map.size());
			System.out.println(map);
		}
	}

}
