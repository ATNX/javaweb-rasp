package org.javaweb.rasp.commons;

import java.util.LinkedHashMap;
import java.util.Map;

public class RASPLRUCache<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 1L;

	private final int maxSize;

	public RASPLRUCache(int maxSize) {
		this(100, maxSize);
	}

	public RASPLRUCache(int initialCapacity, int maxSize) {
		super(initialCapacity);
		this.maxSize = maxSize;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return this.size() > this.maxSize;
	}

}