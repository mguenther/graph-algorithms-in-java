package de.mgu.algorithms.adt.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class UndirectedGraphImpl<V extends Vertex, E extends Edge> extends AbstractBaseGraph<V, E> implements UndirectedGraph<V, E> {

    public UndirectedGraphImpl(Class<? extends E> edgeClass) {
        super(false, edgeClass);
    }

	@Override
    public int degreeOf(V vertex) {
        assertVertexExists(vertex);
        return this.adjacencyMap.get(vertex.getId()).size();
    }
	
	@Override
	public Set<E> getAllEdges(V head, V tail) {
        assertVertexExists(head);
        assertVertexExists(tail);

        Set<E> allEdges = new LinkedHashSet<E>();
        Collection<E> edgesOfHead = this.adjacencyMap.get(head.getId());
        for (E edge : edgesOfHead) {
            if (EdgeUtils.edgeEqualityUndirected(edge, head, tail)) {
                allEdges.add(edge);
            }
        }

        Collection<E> edgesOfTail = this.adjacencyMap.get(tail.getId());
        for (E edge : edgesOfTail) {
            if (EdgeUtils.edgeEqualityUndirected(edge, head, tail)) {
                allEdges.add(edge);
            }
        }

        return Collections.unmodifiableSet(allEdges);
	}
	
	@Override
	public E getEdge(V head, V tail) {
        assertVertexExists(head);
        assertVertexExists(tail);

        Collection<E> edgesOfHead = this.adjacencyMap.get(head.getId());
        for (E edge : edgesOfHead) {
            if (EdgeUtils.edgeEqualityUndirected(edge, head, tail)) {
                return edge;
            }
        }

        return null;
	}
}