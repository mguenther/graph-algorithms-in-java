package de.mgu.algorithms.graphs.path;

import de.mgu.algorithms.adt.pq.BinaryHeap;
import de.mgu.algorithms.adt.pq.Entry;
import de.mgu.algorithms.adt.pq.IntegerComparator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DijkstraHeap<V extends PathVertex, E extends PathEdge> extends Dijkstra<V, E> {

    private BinaryHeap<Integer, V> pq;

    private Map<V, Entry<Integer, V>> backReferences;
    
    private SearchMode searchMode;
    
    private V goal;
    
    private V currentVertex;

    protected void initialize(V start) {
    	this.backReferences = new HashMap<V, Entry<Integer, V>>();
        this.searchMode = SearchMode.SINGLE_SOURCE_ALL_SINKS;
        start.setDistance(0);
        this.currentVertex = start;
        this.pq = BinaryHeap.createPQ(new IntegerComparator());
        this.pq.insert(start.getDistance(), start);
    }
    
    protected void initialize(V start, V goal) {
    	this.backReferences = new HashMap<V, Entry<Integer, V>>();
    	this.searchMode = SearchMode.SINGLE_SOURCE_SINGLE_SINK;
    	this.goal = goal;
    	start.setDistance(0);
    	this.currentVertex = start;
        this.pq = BinaryHeap.createPQ(new IntegerComparator());
        this.pq.insert(start.getDistance(), start);
    }

    protected boolean notFinished() {
    	if (searchMode.equals(SearchMode.SINGLE_SOURCE_ALL_SINKS)) {
    		return !this.pq.isEmpty();
    	} else {
    		return (!this.pq.isEmpty() && !(this.currentVertex.equals(this.goal)));
    	}
    }

    protected V getNodeWithMinimumDistance() {
    	this.currentVertex = this.pq.deleteMin().getValue();
    	return this.currentVertex;
    }

    protected Set<E> neighboursOf(V vertex) {
        return this.getGraph().edgesOfV(vertex);
    }

    @SuppressWarnings("unchecked")
    protected void relax(E edge) {
        V head = (V) edge.head;
        V tail = (V) edge.tail;

        int pathCostOverEdge = head.getDistance() + edge.getCost();

        if (pathCostOverEdge < tail.getDistance()) {
            if (unvisited(tail)) {
                this.backReferences.put(tail, this.pq.insert(pathCostOverEdge, tail));
            } else {
                this.pq.decreaseKey(this.backReferences.get(tail), pathCostOverEdge);
            }

            tail.setDistance(pathCostOverEdge);
        }
    }

    private boolean unvisited(V vertex) {
        return (vertex.getDistance() == PathVertex.DEFAULT_VALUE);
    }
}