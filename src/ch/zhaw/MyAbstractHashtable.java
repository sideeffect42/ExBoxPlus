package ch.zhaw;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class MyAbstractHashtable<K, V> implements Map<K, V> {
	@SuppressWarnings("unchecked")
	protected K[] keys = (K[])new Object[10];
	@SuppressWarnings("unchecked")
	protected V[] values = (V[])new Object[10];

	protected int hash(Object k) {
		int h = Math.abs(k.hashCode());
		return (h % this.keys.length);
	}

	// Removes all mappings from this map (optional operation).
	public abstract void clear();

	// Returns true if this map contains a mapping for the specified key.
	public abstract boolean containsKey(Object key);

	// Returns true if this map maps one or more keys to the specified value.
	public abstract boolean containsValue(Object value);

	// Returns a set view of the mappings contained in this map.
	public abstract Set<Entry<K, V>> entrySet();

	// Compares the specified object with this map for equality.
	public abstract boolean equals(Object o);

	// Returns the value to which this map maps the specified key.
	public abstract V get(Object key);

	// Returns the hash code value for this map.
	public abstract int hashCode();

	// Returns true if this map contains no key-value mappings.
	public abstract boolean isEmpty();

	// Returns a set view of the keys contained in this map.
	public abstract Set<K> keySet();

	// Associates the specified value with the specified key
	// in this map (optional operation).
	public abstract V put(K key, V value);

	// Copies all of the mappings from the specified map
	// to this map (optional operation).
	public abstract void putAll(Map<? extends K, ? extends V> t);

	// Removes the mapping for this key from this map
	// if present (optional operation).
	public abstract V remove(Object key);

	// Returns the number of key-value mappings in this map.
	public abstract int size();

	// Returns a collection view of the values contained in this map.
	public abstract Collection<V> values();
}
