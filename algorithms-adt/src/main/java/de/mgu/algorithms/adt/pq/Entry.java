package de.mgu.algorithms.adt.pq;

public class Entry<K,V> extends PairEntry<K,V> {
	
	int index;
	
	public Entry(K key, V value, int index) {
		super(key, value);
		this.index = index;
	}
	
	@Override
	public K setKey(K newKey) {
		return super.setKey(newKey);
	}
}