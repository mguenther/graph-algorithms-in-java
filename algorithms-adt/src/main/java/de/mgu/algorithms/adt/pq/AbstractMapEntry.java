package de.mgu.algorithms.adt.pq;

public abstract class AbstractMapEntry<K,V> {
	public abstract K getKey();
	
	public abstract V getValue();
	
	public V setValue(V value) {
		throw new UnsupportedOperationException();
	}
	
	public K setKey(K key) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return ((getKey() == null) ? "null" : getKey().toString()) + " = " +
			   ((getValue() == null) ? "null" : getValue().toString());
	}
}