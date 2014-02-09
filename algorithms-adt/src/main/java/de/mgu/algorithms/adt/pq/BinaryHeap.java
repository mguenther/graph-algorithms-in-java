package de.mgu.algorithms.adt.pq;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BinaryHeap<K, V> implements 
		PriorityQueue<K, V> {
	
	private Comparator<K> comparator;
	
	private List<Entry<K, V>> heap = new ArrayList<Entry<K,V>>();
	
	public static <K, V> BinaryHeap<K, V> createPQ(Comparator<K> comparator) {
		return new BinaryHeap<K, V>(comparator);
	}
	
	private BinaryHeap(Comparator<K> comparator) {
		this.comparator = comparator;
		heap.add(null); // dummy object, so that we can start at index 1
	}
	
	public Entry<K,V> find(K key, V value) {
		if (isEmpty())
			throw new UnderflowException();
		
		int j;
		int N = heap.size()-1;
		int k = 1;
		
		while (2*k <= N) {
			j = 2 * k;
			
			for (int i = j; i < 2; i++) {
				if (heap.get(j).getValue().equals(value) &&
					heap.get(j).getKey().equals(key))
					return heap.get(j);
			}
			
			if (!less(j, k) && heap.get(k).getValue().equals(value) &&
					heap.get(k).getKey().equals(key)) {
				return heap.get(j);
			}
			
			k = j;
		}
		
		return null;
	}
	
	private void sink(int k, int N) {
		while (2*k <= N) {
			int j = 2 * k;

			if (j < N && less(j+1, j)) j++;
			if (!less(j, k)) break;
			exchange(k, j); k = j;
		}
	}
	
	private boolean less(int i, int j) {
		return comparator.compare(heap.get(i).getKey(), heap.get(j).getKey()) < 0;
	}
	
	private void swim(int k) {
		while (k > 1 && less(k, k/2)) {
			exchange(k, k/2);
			k = k/2;
		}
	}

	private void exchange(int i, int j) {
		Entry<K, V> ei = heap.get(i);
		Entry<K, V> ej = heap.get(j);
		
		ei.index = j;
		ej.index = i;
		
		heap.set(i, ej);
		heap.set(j, ei);
	}
	
	public void clear() {
		this.heap = new ArrayList<Entry<K, V>>();
		this.heap.add(null);
	}
	
	public void decreaseKey(Entry<K, V> entry, K newKey) {
		if (comparator.compare(newKey, entry.setKey(newKey)) < 0) {
			swim(entry.index);
		} else {
			throw new IllegalArgumentException("new key > old key");
		}
	}
	
	public Entry<K, V> deleteMin() {
		if (isEmpty())
			throw new UnderflowException();
		
		exchange(1, heap.size()-1);
		sink(1, heap.size()-2);
		return heap.remove(heap.size()-1);
	}

	public Entry<K, V> insert(K key, V value) {
		Entry<K,V> newEntry = new Entry<K, V>(key, value, heap.size());
		heap.add(newEntry);
		swim(newEntry.index);
		
		return newEntry;
	}
	
	public boolean isEmpty() {
		return (this.heap.size() == 1);
	}
	
	public int size() {
		return this.heap.size();
	}
	
	public void dump() {
		for (Entry<K,V> entry : this.heap)
			System.out.println(entry);
	}
}