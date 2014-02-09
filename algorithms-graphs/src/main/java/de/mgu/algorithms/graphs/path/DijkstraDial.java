package de.mgu.algorithms.graphs.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.mgu.algorithms.adt.list.DoublyLinkedList;

public class DijkstraDial<V extends PathVertex, E extends PathEdge> extends Dijkstra<V, E> {

	private List<DoublyLinkedList<V>> buckets = new ArrayList<DoublyLinkedList<V>>();
	
	/**
	 * Represents the current bucket index
	 */
	private int k;
	
	/**
	 * Represents the number of buckets we use
	 */
	private int M;
	
	private SearchMode searchMode;
	
	private V goal;
	
	private V currentVertex;
	
	@Override
	protected void initialize(V start) {
		this.searchMode = SearchMode.SINGLE_SOURCE_ALL_SINKS;
		this.currentVertex = start;
		initialize();
	};
	
	@Override
	protected void initialize(V start, V goal) {
		this.searchMode = SearchMode.SINGLE_SOURCE_SINGLE_SINK;
		this.currentVertex = start;
		this.goal = goal;
		initialize();
	};
	
	private void initialize() {
		determineNumberOfBuckets();
		initializeBuckets();
		
		this.currentVertex.setDistance(0);
		this.buckets.get(0).insert(this.currentVertex);
		
		k = 0;		
	}
	
	private void determineNumberOfBuckets() {
		int C = Integer.MIN_VALUE;
		for (PathEdge e : getGraph().edges()) {
			int cost = e.getCost();
			if (cost > C) {
				C = cost;
			}
		}
		M = C + 1;
	}
	
	private void initializeBuckets() {
		this.buckets.clear();
		for (int i = 0; i <= M; i++) {
			this.buckets.add(new DoublyLinkedList<V>());
		}
	}
	
	@Override
	protected V getNodeWithMinimumDistance() {
		this.currentVertex = this.buckets.get(k).get(0);
		this.buckets.get(k).remove(this.currentVertex);
		return this.currentVertex;
	}
	
	@Override
	protected Set<E> neighboursOf(V vertex) {
		return this.getGraph().edgesOfV(vertex);
	};
	
	@Override
	protected boolean notFinished() {
		if (this.searchMode.equals(SearchMode.SINGLE_SOURCE_SINGLE_SINK) &&
			this.currentVertex.equals(this.goal)) {
			return false;
		}
		
		int offset = 0;
		
		while (buckets.get((offset+k) % M).size() == 0) {
			offset++;
			if (offset > M) return false;
		}
		k = (k+offset) % M;
		
		return true;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void relax(E edge) {
		V head = (V) edge.head;
		V tail = (V) edge.tail;
		
		int pathCostOverEdge = head.getDistance() + edge.getCost();
		
		if (pathCostOverEdge < tail.getDistance()) {
			if (unvisited(tail)) {
				// node was never seen before, move to correct place in the
				// bucket data structure
				buckets.get(pathCostOverEdge % M).insert(tail);
			} else {
				// node was seen before, so we have to move it to the new
				// bucket and remove it from the old one
				buckets.get(tail.getDistance() % M).remove(tail);
				buckets.get(pathCostOverEdge % M).insert(tail);
			}
			
			tail.setDistance(pathCostOverEdge);
		}
	};
	
    private boolean unvisited(V vertex) {
        return (vertex.getDistance() == PathVertex.DEFAULT_VALUE);
    }
}