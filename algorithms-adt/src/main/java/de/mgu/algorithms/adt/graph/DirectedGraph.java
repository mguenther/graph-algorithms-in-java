package de.mgu.algorithms.adt.graph;

import java.util.Set;

public interface DirectedGraph<V extends Vertex, E extends Edge> extends Graph<V, E> {
    /**
     * @param vertex object of generic type V
     * @throws IllegalArgumentException if the given vertex is null
     * @throws IllegalStateException if the given vertex does not belong to the graph
     * @return the number of edges in the *negative* neighborhood of the given vertex
     */
	int inDegreeOf(V vertex);

    /**
     * @param vertex object of generic type V
     * @throws IllegalArgumentException if the given vertex is of type null
     * @throws IllegalStateException if the given vertex does not belong to the graph
     * @return the unmodifiable set of edges that comprise the *negative* neighborhood
     *         of the given vertex
     */
	Set<E> incomingEdgesOf(V vertex);

    /**
     * @param vertex object of generic type V
     * @throws IllegalArgumentException if the given vertex is null
     * @throws IllegalStateException if the given vertex does not belong to the graph
     * @return the number of edges in the *positive* neighborhood of the given vertex
     */
	int outDegreeOf(V vertex);

    /**
     * @param vertex object of generic type V
     * @throws IllegalArgumentException if the given vertex is of type null
     * @throws IllegalStateException if the given vertex does not belong to the graph
     * @return the unmodifiable set of edges that comprise the *positive* neighborhood
     *         of the given vertex
     */
	Set<E> outgoingEdgesOf(V vertex);
}