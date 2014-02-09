package de.mgu.algorithms.adt.pq;

public class PairEntry<K,V> extends AbstractMapEntry<K,V> {
	private K key;
	
	private V value;
	
	public PairEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return this.key;
	}
	
	@Override
	public V getValue() {
		return this.value;
	}
	
	@Override
	public K setKey(K newKey) {
		K old = this.key;
		key = newKey;
		return old;
	}
	
	@Override
	public V setValue(V value) {
		this.value = value;
		
		return this.value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PairEntry other = (PairEntry) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
}