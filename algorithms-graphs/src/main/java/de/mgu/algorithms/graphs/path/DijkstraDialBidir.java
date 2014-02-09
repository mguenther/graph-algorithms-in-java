package de.mgu.algorithms.graphs.path;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.mgu.algorithms.adt.graph.DirectedGraph;
import de.mgu.algorithms.adt.list.DoublyLinkedList;

public class DijkstraDialBidir<V extends PathVertex, E extends PathEdge> extends Dijkstra<V, E> {

	private final BitSet LABELED_VIA_BOTH_SEARCHES = new BitSet(2);
	
	private DirectedGraph<V, E> transposedGraph;
	
	private boolean isFinished;
	
	private int M;
	
	private int distanceToSink = Integer.MAX_VALUE;
	
	private V currentVertex;
	
	/**
	 * Represents the current bucket index w.r.t. the forward search
	 * data structure
	 */
	private int k;
	
	/**
	 * Represents the current bucket index w.r.t. the backward search
	 */
	private int l;
	
	private List<DoublyLinkedList<V>> forwardBuckets;
	
	private List<DoublyLinkedList<V>> backwardBuckets;
	
	private Map<V, BitSet> labels;
	
	private String targetId;
	
	private boolean isForwardSearch;
	
	public DijkstraDialBidir() {
		LABELED_VIA_BOTH_SEARCHES.set(0);
		LABELED_VIA_BOTH_SEARCHES.set(1);
	}
	
	public void findShortestPath(
			DirectedGraph<V, E> dag,
			DirectedGraph<V, E> transposedGraph,
			V start,
			V goal) {
		this.transposedGraph = transposedGraph;
		this.findShortestPath(dag, start, goal);
	}
	
	@Override
	protected void initialize(V start) {
		throw new UnsupportedOperationException("Bidirectional search requires start and " +
				" goal vertex.");
	};
	
	@Override
	protected void initialize(V start, V goal) {
		if (start.equals(goal)) {
			throw new IllegalArgumentException("Start and goal vertices are the same.");
		}
		
		this.forwardBuckets = new ArrayList<DoublyLinkedList<V>>();
		this.backwardBuckets = new ArrayList<DoublyLinkedList<V>>();
		this.labels = new HashMap<V, BitSet>();
		
		this.targetId = goal.getId();
		
		determineNumberOfBuckets();
		initializeBuckets();
		initializeAlgorithm(start, goal);
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
		this.forwardBuckets.clear();
		this.backwardBuckets.clear();
		
		for (int i = 0; i <= M; i++) {
			this.forwardBuckets.add(new DoublyLinkedList<V>());
			this.backwardBuckets.add(new DoublyLinkedList<V>());
		}
	}
	
	private void initializeAlgorithm(V start, V goal) {
		goal = this.transposedGraph.getVertexById(goal.getId());
		
		start.setDistance(0);
		goal.setDistance(0);
		
		this.isForwardSearch = false;
		this.currentVertex = start;
		this.forwardBuckets.get(0).insert(start);
		this.backwardBuckets.get(0).insert(goal);
		this.k = 0;
		this.l = 0;
		this.isFinished = false;
	};
	
	@Override
	protected V getNodeWithMinimumDistance() {
		this.isForwardSearch = !this.isForwardSearch;
		
		int k = currentIndex();
		int offset = 0;
		
		while ((offset < M) && activeBuckets().get((k+offset) % M).isEmpty()) {
			offset++;
		}
		
		k = (k+offset) % M;
		setCurrentIndex(k);
		
		this.currentVertex = activeBuckets().get(k).get(0);
		label(this.currentVertex);
		activeBuckets().get(k).remove(this.currentVertex);
		
		if (labeledViaBothSearchDirections(this.currentVertex)) {
			// both searches have met at the currente vertex
			// this is termination case 1
			this.isFinished = true;
			// vertex ids for both graphs must be the same. we can 
			// simply lookup the distance value for both vertices with
			// the same id and update the total distance
			
			int forwardDistance = getGraph().getVertexById(this.currentVertex.getId()).getDistance();
			int backwardDistance = transposedGraph.getVertexById(this.currentVertex.getId()).getDistance();
			int totalDistance = forwardDistance + backwardDistance;
			
			if (totalDistance < this.distanceToSink) {
				this.distanceToSink = totalDistance;
			}
			
		}
		
		return this.currentVertex;
	}
	
	@Override
	protected Set<E> neighboursOf(V vertex) {
		return (this.isForwardSearch)
				? this.getGraph().edgesOfV(vertex)
				: this.transposedGraph.edgesOfV(vertex);
	};
	
	@Override
	protected boolean notFinished() {
		if (this.isFinished) return false;
		
		// the search is still running if we have at least one vertex
		// in the complementary bucket list.
		// TODO: maybe we can advance the bucket index in here alone?
		
		// we have to check the bucket list that will be used
		// in the next step. in order to do this, we fake the
		// isForwardSearch boolean for the remainder of this method
		boolean storeSearchDirection = this.isForwardSearch;
		this.isForwardSearch = !this.isForwardSearch;
		
		int k = currentIndex();
		
		for (int offset = 0; offset < M; offset++) {
			if (!activeBuckets().get((k+offset) % M).isEmpty()) {
				setCurrentIndex((k+offset) % M);
				// restore search direction
				this.isForwardSearch = storeSearchDirection;
				return true;
			}
		}
		
		// otherwise, we do not have a path between s and t in the graph
		throw new IllegalStateException("No path from s to t could be found.");
	}
	
	private int currentIndex() {
		return (this.isForwardSearch) ? this.k : this.l;
	}
	
	private void setCurrentIndex(int index) {
		if (this.isForwardSearch) {
			this.k = index;
		} else {
			this.l = index;
		}
	}
	
	private List<DoublyLinkedList<V>> activeBuckets() {
		return (this.isForwardSearch) ? this.forwardBuckets : this.backwardBuckets;
	}
	
	private void label(V vertex) {
		if (!this.labels.containsKey(vertex)) {
			this.labels.put(vertex, new BitSet(2));
		}
		
		if (isForwardSearch) {
			this.labels.get(vertex).set(0);
		} else {
			this.labels.get(vertex).set(1);
		}
	}
	
	private boolean labeledViaBothSearchDirections(V vertex) {
		return this.labels.get(vertex).equals(LABELED_VIA_BOTH_SEARCHES);
	}
	
	private boolean labeledViaComplementarySearch(V vertex) {
		if (!this.labels.containsKey(vertex)) return false;
		
		return (this.isForwardSearch && this.labels.get(vertex).get(1)) ||
			   (!this.isForwardSearch && this.labels.get(vertex).get(0));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void relax(E edge) {
		V head = (V) edge.head;
		V tail = (V) edge.tail;
		
		int pathCostOverEdge = head.getDistance() + edge.getCost();
		
		// if the new path is not better than the old one, we do not have
		// to update anything
		if (tail.getDistance() <= pathCostOverEdge) return;
		
		//if seen before, remove from the current bucket and insert in the new one
		if (visited(tail)) {
			activeBuckets().get(tail.getDistance() % M).remove(tail);
		}
		
		tail.setDistance(pathCostOverEdge);
		activeBuckets().get(pathCostOverEdge % M).insert(tail);
		
		// we have to check if the tail vertex was labeled during
		// the complementary search direction. in that case, we encountered
		// termination case 2
		
		if (labeledViaComplementarySearch(tail)) {
			int c = pathCostOverEdge + complementaryGraph().getVertexById(tail.getId()).getDistance();
			if (c < this.distanceToSink) {
				this.distanceToSink = c;
			}
		}
	};
	
	private boolean visited(V vertex) {
		return vertex.getDistance() != PathVertex.DEFAULT_VALUE;
	}
	
	private DirectedGraph<V, E> complementaryGraph() {
		return (this.isForwardSearch) ? this.transposedGraph : this.getGraph();
	}
	
	public int getDistance() {
		return this.distanceToSink;
	}
	
	@Override
	protected void postprocess() {
		getGraph().getVertexById(this.targetId).setDistance(getDistance());
	}
}