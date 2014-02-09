package de.mgu.algorithms.adt.graph;

import java.util.Collection;
import java.util.Set;

/**
 * Defines the basic interface for the graph ADT.
 * 
 * @author mgu
 *
 * @param <V> some class that extends the basic <code>Vertex</code> implementation
 * @param <E> some class that extends the basic <code>Edge</code> implementation
 */
public interface Graph<V extends Vertex, E extends Edge> {
    /**
     * @return The amount of nodes |V| in the graph
     */
	int sizeOfV();

    /**
     * @return The amount of edges |E| in the graph
     */
	int sizeOfE();

    /**
     * @return Boolean value, indicating whether the graph is directed
     */
	boolean isDirected();

    /**
     *
     * @return The EdgeFactory<V,E> which is used to connect a pair of V
     */
	EdgeFactory<V, E> getEdgeFactory();

    /**
     * Adds the given vertex to the graph, if it is not already present.
     *
     * @param vertex object of generic type V representing a vertex
     * @throws IllegalArgumentException if the given vertex is <code>null</code>
     * @return True, if the vertex was successfully added to the graph. False,
     *         if the vertex was already present in the graph.
     */
	boolean addVertex(V vertex);

    /**
     * Removes the given vertex from the graph. This also removes all edges in which
     * this vertex participates (as head or as tail).
     *
     * @param vertex object of generic type V representing a vertex
     * @throws IllegalArgumentException if the given vertex is of type <code>null</code>
     * @return True, if the vertex was successfully removed from the graph. False, otherwise
     */
    boolean removeVertex(V vertex);
    
    /**
     * Searches the set of vertices for a vertex that can be uniquely identified
     * by the given id. If this vertex is present in the graph ADT, then it will
     * be returned. Otherwise, this method returns <code>null</code>.
     * @param id
     * @throws IllegalArgumentException if the given id is <code>null</code>
     * @return Instance of <code>V</code> if there is a vertex which corresponds to the given id
     */
    V getVertexById(String id);

    /**
     * Connects two given vertices <code>head</code> and <code>tail</code> by
     * linking them logically via an object of the generic type E.
     *
     * @param head object of generic type V representing the head vertex
     * @param tail object of generic type V representing the tail vertex
     * @throws IllegalArgumentException if one of the vertices is of type null
     * @throws IllegalStateException if one of the vertices does not belong to the graph
     * @return The created edge of generic type E
     */
    E connect(V head, V tail);

    /**
     * Disconnects two given vertices <code>head</code> and <code>tail</code> by
     * removing all logical links of the generic type E between them.
     *
     * @param head object of generic type V, representing the head vertex
     * @param tail object of generic type V, representing the tail vertex
     * @throws IllegalArgumentException if one of either vertices is of type null
     * @throws IllegalStateException if one of either vertices does not belong to the graph
     * @return Boolean value, indicating whether the given vertices were
     *         properly disconnected
     */
    boolean disconnectAll(V head, V tail);

    /**
     * Removes the given edge from the graph.
     *
     * @param edge object of generic type E
     * @throws IllegalArgumentException if the given edge is of type <code>null</code>
     * @throws IllegalStateException if one of the edge's vertices is of type <code>null</code>
     * @return Boolean value, indicating whether the given edge was removed successfully
     */
    boolean disconnect(E edge);

    /**
     * Returns the first found edge which links the pair of vertices (head, tail).
     * In the presence of multiple edges e=(head, tail), the user of this interface
     * cannot rely on a particular ordering of such edges. In an undirected graph,
     * this method may return an edge of the form e'=(tail, head).
     *
     * @param head object of generic type V, representing the head vertex
     * @param tail object of generic type V, representing the tail vertex
     * @throws IllegalArgumentException if one of the vertices is of type null
     * @throws IllegalStateException if one of the vertices does not belong to the graph
     * @return edge object of generic type E, representing a connection between
     *         the vertices head and tail. null, if there is no such edge.
     */
    E getEdge(V head, V tail);

    /**
     * Returns the unmodifiable set of edges which connects the pair of vertices
     * (head, tail). In an undirected graph, this method may return edges of the
     * form e'=(tail, head) as well.
     *
     * @param head object of generic type V, representing the head vertex
     * @param tail object of generic type V, representing the tail vertex
     * @throws IllegalArgumentException if one of the vertices is of type null
     * @throws IllegalStateException if one of the vertices does not belong to the graph
     * @return the unmodifiable set of edges that connect head and tail
     */
	Set<E> getAllEdges(V head, V tail);

    /**
     * @param vertex object of generic type V
     * @throws IllegalArgumentException if the given vertex is of type null
     * @throws IllegalStateException if the given vertex does not belong to the graph
     * @return the unmodifiable set of edges that comprise the neighborhood
     *         of the given vertex
     */
    Set<E> edgesOfV(V vertex);

    /**
     * @param edge object of generic type E
     * @throws IllegalArgumentException if <code>edge</code> is null
     * @return Boolean value, indicating whether the graph contains the given edge
     */
	boolean containsEdge(E edge);

    /**
     * @param vertex object of generic type V
     * @return Boolean value, indicating whether the graph contains the given vertex
     */
	boolean containsVertex(V vertex);

    /**
     * @return the unmodifiable <code>Set</code> of all the graphs edges
     */
    Set<E> edges();

    /**
     * @return the unmodifiable <code>Collection</code> of all the graphs vertices
     */
    Collection<V> vertices();
}