package de.mgu.algorithms.adt.graph;

public interface UndirectedGraph<V extends Vertex, E extends Edge> extends Graph<V, E> {
    /**
     * @param vertex object of generic type V
     * @throws IllegalArgumentException if the given vertex is null
     * @throws IllegalStateException if the given vertex does not belong to the graph
     * @return the number of edges in the neighborhood of the given vertex
     */
	int degreeOf(V vertex);
}