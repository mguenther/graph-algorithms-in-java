package de.mgu.algorithms.graphs.path;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.mgu.algorithms.adt.graph.DirectedGraph;
import de.mgu.algorithms.adt.pq.BinaryHeap;
import de.mgu.algorithms.adt.pq.Entry;
import de.mgu.algorithms.adt.pq.IntegerComparator;

public class DijkstraHeapBidir<V extends PathVertex, E extends PathEdge> extends Dijkstra<V, E> {
	
	private final BitSet LABELED_VIA_BOTH_SEARCHES = new BitSet(2);
	
	private BinaryHeap<Integer, V> forwardPQ;
	
	private BinaryHeap<Integer, V> backwardPQ;
	
	private Map<V, Entry<Integer, V>> forwardBackreferences;
	
	private Map<V, Entry<Integer, V>> backwardBackreferences;
	
	private Map<V, BitSet> labeledBy;
	
	private DirectedGraph<V, E> transposedGraph;
	
	private boolean pathComplete;
	
	private boolean isForwardSearch;
	
	private int distanceToSink = Integer.MAX_VALUE;
	
	private V currentVertex;
	
	private String targetId;
	
	public DijkstraHeapBidir() {
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
	
	protected void initialize(V start) {
		throw new UnsupportedOperationException("Bidirectional search requires start and " +
			" goal vertex.");
	};
	
	protected void initialize(V start, V goal) {
		if (start.equals(goal)) {
			throw new IllegalArgumentException("Start and goal vertices are the same.");
		}
		
		this.forwardBackreferences = new HashMap<V, Entry<Integer, V>>();
		this.backwardBackreferences = new HashMap<V, Entry<Integer, V>>();
		this.labeledBy = new HashMap<V, BitSet>();
		this.pathComplete = false;
		this.isForwardSearch = false;
		this.forwardPQ = BinaryHeap.createPQ(new IntegerComparator());
		this.backwardPQ = BinaryHeap.createPQ(new IntegerComparator());
		
		goal = this.transposedGraph.getVertexById(goal.getId());
		
		start.setDistance(0);
		goal.setDistance(0);
		this.forwardPQ.insert(0, start);
		this.backwardPQ.insert(0, goal);
		
		this.targetId = goal.getId();
	};
	
	@Override
	protected boolean notFinished() {
		if (!this.pathComplete
				&& this.forwardPQ.isEmpty()
				&& this.backwardPQ.isEmpty()) {
			throw new IllegalStateException("No path from s to t could be found.");
		}
		
		return !pathComplete;
	}
	
	@Override
	protected V getNodeWithMinimumDistance() {
		this.isForwardSearch = !this.isForwardSearch;
		this.currentVertex = activePQ().deleteMin().getValue();
		
		label(this.currentVertex);
		
		if (labeledViaBothSearchDirections(this.currentVertex)) {
			// both searches have met at the current vertex
			// this is termination case 1
			this.pathComplete = true;
			
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
	
	private void label(V vertex) {
		if (!this.labeledBy.containsKey(vertex)) {
			this.labeledBy.put(vertex, new BitSet(2));
		}
		
		if (isForwardSearch) {
			this.labeledBy.get(vertex).set(0);
		} else {
			this.labeledBy.get(vertex).set(1);
		}
	}
	
	protected Set<E> neighboursOf(V vertex) {
		return (this.isForwardSearch) 
				? this.getGraph().edgesOfV(vertex) 
				: this.transposedGraph.edgesOfV(vertex);
	};
	
	@SuppressWarnings("unchecked")
	@Override
	protected void relax(E edge) {
        V head = (V) edge.head;
        V tail = (V) edge.tail;

        int pathCostOverEdge = head.getDistance() + edge.getCost();
        
        if (pathCostOverEdge < tail.getDistance()) {
        	// check for termination case 2 (TODO: explain!)
			if (labeledViaComplementarySearch(tail)) {
				int c = pathCostOverEdge + complementaryGraph().getVertexById(tail.getId()).getDistance();
				if (c < this.distanceToSink) {
					this.distanceToSink = c;
				}
			}
        	
        	if (unvisited(tail)) {
        		// vertex was never seen before, put it in the resp. queue
        		// and store a backreference
        		activeReferenceMap().put(tail, activePQ().insert(pathCostOverEdge, tail));
        	} else {
        		// tail vertex is already in the resp. queue
        		activePQ().decreaseKey(activeReferenceMap().get(tail), pathCostOverEdge);
        	}
        	
        	tail.setDistance(pathCostOverEdge);
        }
	}
	
	private Map<V, Entry<Integer, V>> activeReferenceMap() {
		return (this.isForwardSearch) ? this.forwardBackreferences : this.backwardBackreferences;
	}
	
	private BinaryHeap<Integer, V> activePQ() {
		return (this.isForwardSearch) ? this.forwardPQ : this.backwardPQ;
	}

	private boolean unvisited(V vertex) {
		return (vertex.getDistance() == PathVertex.DEFAULT_VALUE);
	}
	
	private boolean labeledViaComplementarySearch(V vertex) {
		if (!this.labeledBy.containsKey(vertex)) return false;
		
		return (this.isForwardSearch && this.labeledBy.get(vertex).get(1)) ||
			   (!this.isForwardSearch && this.labeledBy.get(vertex).get(0));
	}
	
	private boolean labeledViaBothSearchDirections(V vertex) {
		return this.labeledBy.get(vertex).equals(LABELED_VIA_BOTH_SEARCHES);
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