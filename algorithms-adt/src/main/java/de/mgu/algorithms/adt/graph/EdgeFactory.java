package de.mgu.algorithms.adt.graph;

public class EdgeFactory<V extends Vertex, E extends Edge> {
	
	private final Class<? extends E> edgeClass;
	
	public EdgeFactory(Class<? extends E> edgeClass) {
		this.edgeClass = edgeClass;
	}

	E connect(V head, V tail) {
		try {
            E e = edgeClass.newInstance();
            e.head = head;
            e.tail = tail;
			return e;
		} catch (Exception e) {
			throw new RuntimeException("Could not create edge using EdgeFactory", e);
		}
	}
	
	public Class<? extends E> edgeClass() {
		return this.edgeClass;
	}
}