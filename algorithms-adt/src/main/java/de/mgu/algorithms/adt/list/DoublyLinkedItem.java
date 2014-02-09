package de.mgu.algorithms.adt.list;

public class DoublyLinkedItem<T> {
	
	private T data;
	
	private DoublyLinkedItem<T> pred = null;
	
	private DoublyLinkedItem<T> succ = null;
	
	public DoublyLinkedItem(T data) {
		this.data = data;
	}
	
	public T getData() {
		return this.data;
	}
	
	public void setPredecessor(DoublyLinkedItem<T> pred) {
		if (pred == null)
			throw new IllegalArgumentException();
		
		this.pred = pred;
	}
	
	public DoublyLinkedItem<T> getPredecessor() {
		return this.pred;
	}
	
	public void setSuccessor(DoublyLinkedItem<T> succ) {
		if (succ == null)
			throw new IllegalArgumentException();
		
		this.succ = succ;
	}
	
	public DoublyLinkedItem<T> getSuccessor() {
		return this.succ;
	}
	
	public void insert(DoublyLinkedItem<T> pred, DoublyLinkedItem<T> succ) {
		pred.setSuccessor(this);
		succ.setPredecessor(this);
	}
	
	public void remove() {
		pred.setSuccessor(this.succ);
		succ.setPredecessor(this.pred);
	}
}