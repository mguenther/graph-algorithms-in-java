package de.mgu.algorithms.adt.pq;

public class UnderflowException extends RuntimeException {
	private final static long serialVersionUID = 23849020L;
	
	public UnderflowException() {
		super("The queue is already empty.");
	}
}