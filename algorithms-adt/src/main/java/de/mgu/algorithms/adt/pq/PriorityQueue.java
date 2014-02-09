package de.mgu.algorithms.adt.pq;

public interface PriorityQueue<K,V> {
	/**
	 * Inserts a given key-value-pair into the queue.
	 * 
	 * @param key
	 * @param value
	 * @return The <code>Entry</code> object corresponding to the given key
	 * 		and pair
	 */
	Entry<K, V> insert(K key, V value);
	
	/**
	 * @return The <code>Entry</code> object with the minimum key. After the
	 * 		execution of <code>deleteMin</code>, a find operation for the
	 * 		returned object has to fail.
	 * @throws UnderflowException
	 * 		if the queue is empty.
	 */
	Entry<K, V> deleteMin();
	
	/**
	 * Looks for an <code>Entry<K,V></code> object in the queue, which matches
	 * the given <code>key</code> and <code>value</code>.
	 * 
	 * @param key
	 * @param value
	 * @return The requested <code>Entry</code> object. <code>null</code>
	 *		otherwise
	 * @throws UnderflowException
	 * 		if the queue is empty.
	 */
	Entry<K,V> find(K key, V value);
	
	/**
	 * Sets the key for the given entry to the key provided with
	 * <code>newKey</code>. <code>decreaseKey</code> must maintain the
	 * invariant that the queue remains correctly ordered after the operation.
	 * 
	 * @param entry
	 * @param newKey
	 * @throws IllegalArgumentException
	 */
	void decreaseKey(Entry<K, V> entry, K newKey);
	
	/**
	 * Removes all entries from the queue.
	 */
	void clear();
	
	/**
	 * @return True, if the queue has no elements in it. False otherwise.
	 */
	boolean isEmpty();
	
	/**
	 * @return The number of elements that reside currently in the queue.
	 */
	int size();
}