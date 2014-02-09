package de.mgu.algorithms.adt.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class DirectedGraphImpl<V extends Vertex, E extends Edge> extends AbstractBaseGraph<V, E> implements DirectedGraph<V, E> {

    public DirectedGraphImpl(Class<? extends E> edgeClass) {
        super(true, edgeClass);
    }

    @Override
    public int inDegreeOf(V vertex) {
        assertVertexExists(vertex);
        return incomingEdgesOf(vertex).size();
    }

    @Override
    public Set<E> incomingEdgesOf(V vertex) {
        assertVertexExists(vertex);

        Set<E> incomingEdges = new LinkedHashSet<E>();

        for (V v : this.vertices()) {
            if (vertex.equals(v)) {
                continue;
            }

            for (E edge : this.adjacencyMap.get(v.getId())) {
                if (edge.tail.equals(vertex)) {
                    incomingEdges.add(edge);
                }
            }
        }

        return Collections.unmodifiableSet(incomingEdges);
    }
    
    @Override
    public int outDegreeOf(V vertex) {
        assertVertexExists(vertex);
        return this.adjacencyMap.get(vertex.getId()).size();
    }

    @Override
    public Set<E> outgoingEdgesOf(V vertex) {
        return edgesOfV(vertex);
    }
    
    @Override
    public Set<E> getAllEdges(V head, V tail) {
        assertVertexExists(head);
        assertVertexExists(tail);

        Set<E> allEdges = new LinkedHashSet<E>();
        Collection<E> edgesOfHead = this.adjacencyMap.get(head.getId());
        for (E edge : edgesOfHead) {
            if (EdgeUtils.edgeEqualityDirected(edge, head, tail)) {
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
            if (EdgeUtils.edgeEqualityDirected(edge, head, tail)) {
                return edge;
            }
        }

        return null;
    }
}