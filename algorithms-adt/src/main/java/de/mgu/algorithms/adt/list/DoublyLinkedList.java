package de.mgu.algorithms.adt.list;

import java.util.NoSuchElementException;

public class DoublyLinkedList<T> {
	
	private DoublyLinkedItem<T> head = new DoublyLinkedItem<T>(null);
	
	private DoublyLinkedItem<T> tail = new DoublyLinkedItem<T>(null);
	
	private int itemCounter;
	
	public DoublyLinkedList() {
		head = new DoublyLinkedItem<T>(null);
		head.setSuccessor(tail);
		tail.setPredecessor(head);
		this.itemCounter = 0;
	}
	
	public boolean isEmpty() {
		return (itemCounter == 0);
	}
	
	public void insert(T data) {
		DoublyLinkedItem<T> newItem = new DoublyLinkedItem<T>(data);
		
		newItem.setSuccessor(tail);
		newItem.setPredecessor(tail.getPredecessor());
		tail.getPredecessor().setSuccessor(newItem);
		tail.setPredecessor(newItem);
		
		itemCounter++;
	}
	
	public void remove(T data) {
		DoublyLinkedItem<T> removal = find(data);
		
		if (removal == null)
			throw new NoSuchElementException();
		
		removal.getPredecessor().setSuccessor(removal.getSuccessor());
		removal.getSuccessor().setPredecessor(removal.getPredecessor());
		
		itemCounter--;
	}
	
	private DoublyLinkedItem<T> find(T data) {
		DoublyLinkedItem<T> search = head;
		while ((search = search.getSuccessor()) != tail) {
			if (search.getData().equals(data)) {
				return search;
			}
		}
		
		return null;
	}
	
	public int size() {
		return this.itemCounter;
	}
	
	public T get(int index) {
		if (index >= itemCounter || index < 0)
			throw new IndexOutOfBoundsException();
		
		DoublyLinkedItem<T> search = head;
		for (int i = 0; i <= index; i++) {
			search = search.getSuccessor();
		}
		
		return search.getData();
	}
}