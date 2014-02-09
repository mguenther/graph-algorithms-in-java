package de.mgu.algorithms.adt.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Implements the <code>Graph</code> interface. Although this class does not hold any
 * abstract methods, the abstract modifier on class-definition-level prevents clients
 * from instantiating this class directly. Clients should work with implementations
 * of <code>UndirectedGraph</code> or <code>DirectedGraph</code>, preferably under the
 * base static type <code>Graph</code>.
 * 
 * @author mgu
 *
 * @param <V> some class that extends the basic <code>Vertex</code> implementation
 * @param <E> some class that extends the basic <code>Edge</code> implementation
 */
abstract public class AbstractBaseGraph<V extends Vertex, E extends Edge> implements Graph<V, E> {

    private boolean isDirected;

    private EdgeFactory<V, E> edgeFactory;

    protected Map<String, Set<E>> adjacencyMap = new HashMap<String, Set<E>>();
    
    protected Map<String, V> vertices = new HashMap<String, V>();

    protected int sizeOfE = 0;

    public AbstractBaseGraph(boolean isDirected, Class<? extends E> edgeClass) {
        this.isDirected = isDirected;
        this.edgeFactory = new EdgeFactory<V, E>(edgeClass);
    }

    @Override
    public int sizeOfV() {
        return this.adjacencyMap.keySet().size();
    }

    @Override
    public int sizeOfE() {
        return this.sizeOfE;
    }

    @Override
    public boolean isDirected() {
        return this.isDirected;
    }

    @Override
    public EdgeFactory<V, E> getEdgeFactory() {
        return this.edgeFactory;
    }

    @Override
    public boolean addVertex(V vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Given vertex cannot be null.");
        }
        
        String key = vertex.getId();
        boolean successful = false;

        if (!this.adjacencyMap.containsKey(key) && !this.vertices.containsKey(key)) {
        	this.adjacencyMap.put(key, new LinkedHashSet<E>());
            this.vertices.put(key, vertex);
            successful = true;
        }

        return successful;
    }

    @Override
    public boolean removeVertex(V vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException(ErrorMessages.VERTEX_IS_NULL);
        }

        if (!this.adjacencyMap.containsKey(vertex.getId())) {
            return false;
        }

        boolean successful = true;

        Collection<V> vertices = this.vertices();
        Collection<E> edgesToBeRemoved = new LinkedList<E>();
        for (V v : vertices) {
            Collection<E> edges = this.adjacencyMap.get(v.getId());
            for (E edge : edges) {
                if (edge.head.equals(vertex) || edge.tail.equals(vertex)) {
                    if (!edgesToBeRemoved.contains(edge)) {
                        edgesToBeRemoved.add(edge);
                    }
                }
            }
        }

        for (E edge : edgesToBeRemoved) {
            if (!this.disconnect(edge)) {
                successful &= false;
            }
        }

        this.adjacencyMap.remove(vertex.getId());
        this.vertices.remove(vertex);
        successful &= true;

        return successful;
    }
    
    @Override
    public V getVertexById(String id) {
    	if (id == null) {
    		throw new IllegalArgumentException(ErrorMessages.VERTEX_ID_IS_NULL);
    	}
    	
    	if (this.vertices.containsKey(id)) {
    		return this.vertices.get(id);
    	}
    	
    	return null;
    }

    @Override
    public E connect(V head, V tail) {
        assertVertexExists(head);
        assertVertexExists(tail);

        EdgeFactory<V, E> factory = getEdgeFactory();
        E edge = factory.connect(head, tail);

        this.adjacencyMap.get(head.getId()).add(edge);
        if (!this.isDirected() && (head != tail)) {
            this.adjacencyMap.get(tail.getId()).add(edge);
        }

        this.sizeOfE++;

        return edge;
    }

    @Override
    public boolean disconnectAll(V head, V tail) {
        assertVertexExists(head);
        assertVertexExists(tail);

        Set<E> allEdges = Graphs.shallowCopyOfEdges(this, head, tail);
        if (!this.isDirected()) {
            allEdges.addAll(Graphs.shallowCopyOfEdges(this, tail, head));
        }

        boolean result = true;
        for (E edge : allEdges) {
            result &= this.disconnect(edge);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean disconnect(E edge) {
        assertEdgeIsNotNull(edge);
        assertVertexExists((V) edge.head);
        assertVertexExists((V) edge.tail);

        boolean result = this.adjacencyMap.get(edge.head.getId()).remove(edge);
        if (!this.isDirected()) {
            result &= this.adjacencyMap.get(edge.tail.getId()).remove(edge);
        }

        this.sizeOfE--;

        return result;
    }

    abstract public E getEdge(V head, V tail);
    
    @Override
    public Set<E> edgesOfV(V vertex) {
        assertVertexExists(vertex);
        return Collections.unmodifiableSet(this.adjacencyMap.get(vertex.getId()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsEdge(E edge) {
        boolean result = false;
        V head = (V) edge.head;

        if (this.adjacencyMap.containsKey(head.getId())) {
            Collection<E> edgesOfHead = this.adjacencyMap.get(head.getId());
            result = edgesOfHead.contains(edge);
        }

        return result;
    }

    @Override
    public boolean containsVertex(V vertex) {
    	if (vertex == null) {
    		return false;
    	}
    	
        return this.adjacencyMap.containsKey(vertex.getId()) && this.vertices.containsKey(vertex.getId());
    }

    @Override
    public Set<E> edges() {
    	Set<E> edges = new LinkedHashSet<E>();
    	
    	for (V vertex : vertices()) {
    		edges.addAll(this.adjacencyMap.get(vertex.getId()));
    	}
    	
    	return Collections.unmodifiableSet(edges);
    }

    @Override
    public Collection<V> vertices() {
    	return Collections.unmodifiableCollection(this.vertices.values());
    }

    protected boolean assertVertexExists(V vertex) throws IllegalArgumentException, IllegalStateException {
        if (vertex == null) {
            throw new IllegalArgumentException(ErrorMessages.VERTEX_IS_NULL);
        }

        if (this.containsVertex(vertex)) {
            return true;
        } else {
            throw new IllegalStateException(ErrorMessages.VERTEX_NOT_FOUND);
        }
    }

    protected boolean assertEdgeIsNotNull(E edge) throws IllegalArgumentException {
        if (edge == null) {
            throw new IllegalArgumentException(ErrorMessages.EDGE_IS_NULL);
        }
        return true;
    }

    abstract public Set<E> getAllEdges(V head, V tail);
}