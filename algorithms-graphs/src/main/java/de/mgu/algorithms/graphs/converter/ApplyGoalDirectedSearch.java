package de.mgu.algorithms.graphs.converter;

import java.util.Iterator;

import de.mgu.algorithms.adt.graph.DirectedGraph;
import de.mgu.algorithms.graphs.path.PathEdge;
import de.mgu.algorithms.graphs.path.PathVertex;

/**
 * Applies a goal-directed strategy to a graph and provides methods which undo
 * all modifications that were introduced by that same strategy. The typical
 * usage of this class would be as follows:
 * 
 * <code>
 * Dijkstra<V, E> dijkstra = ...;
 * DirectedGraph<V, E> graph = ...;
 * V s = graph.getVertexById("0");
 * V t = graph.getVertexById("5000");
 * ApplyGoalDirectedSearch<V, E> strategy = new ApplyGoalDirectedSearch(graph, s, t);
 * strategy.convertEdgeWeights();
 * dijkstra.findShortestPath(graph, s, t);
 * strategy.undoEdgeWeights();
 * strategy.undoVertexDistances();
 * System.out.println(t.getDistance());
 * </code>
 * 
 * @author mgu
 *
 * @param <V>
 * @param <E>
 */
public class ApplyGoalDirectedSearch<V extends PathVertex, E extends PathEdge> {
	
	private final DirectedGraph<V, E> graph;
	
	private final PathVertex s;
	
	private final PathVertex t;
	
	private final MinuteDistanceConverter converter;

	public ApplyGoalDirectedSearch(DirectedGraph<V, E> graph, PathVertex s, PathVertex t) {
		this.graph = graph;
		this.s = s;
		this.t = t;
		this.converter = new MinuteDistanceConverter();
	}
	
	/**
	 * Modifies all edges e \in E by altering their edge costs according to the
	 * following formula: c' = c - dist(head, t) + dist(tail, t).
	 */
	public void convertEdgeWeights() {
		Iterator<E> iter = this.graph.edges().iterator();
		
		while (iter.hasNext()) {
			E edge = iter.next();
			
			PathVertex head = (PathVertex) edge.head;
			PathVertex tail = (PathVertex) edge.tail;
			
			int newCost = edge.getCost()
						  - converter.distance(head.getCoordinates(), this.t.getCoordinates())
						  + converter.distance(tail.getCoordinates(), this.t.getCoordinates());
			
			edge.setCost(newCost);
		}
	}
	
	/**
	 * Modifies all edges e \in E by altering their edge costs according to the
	 * following formuala: c = c' + dist(head, t) - dist(tail, t). Please note
	 * that this essentially reverts changes to a graph that were introduced
	 * by method convertEdgeWeights.
	 */
	public void undoEdgeWeights() {
		Iterator<E> iter = this.graph.edges().iterator();
		
		while (iter.hasNext()) {
			E edge = iter.next();
			
			PathVertex head = (PathVertex) edge.head;
			PathVertex tail = (PathVertex) edge.tail;
			
			int newCost = edge.getCost()
						  + converter.distance(head.getCoordinates(), this.t.getCoordinates())
						  - converter.distance(tail.getCoordinates(), this.t.getCoordinates());
			
			edge.setCost(newCost);
		}
	}
	
	/**
	 * This method provides the means to restore all vertex distances to their
	 * original format. This is necessary, because after we applied a goal-directed
	 * search strategy to a graph, the algorithm will use modified edge weights
	 * in order to calculate the shortest path. This essentially means that our
	 * vertex distances are biased by the goal-directed strategy and do not resemble
	 * the "real" distances between two nodes, but a distance that was obtained from
	 * some heuristic. In order to obtain the correct distance value for all vertices,
	 * we need to modify their distances w.r.t. to the goal-directed strategy applied
	 * to the edges before.
	 */
	public void undoVertexDistances() {
		int distStoT = converter.distance(this.s.getCoordinates(), this.t.getCoordinates());
		
		Iterator<V> iter = this.graph.vertices().iterator();
		
		while (iter.hasNext()) {
			V vertex = iter.next();
			
			if (vertex.getDistance() == PathVertex.DEFAULT_VALUE) {
				continue;
			}
			
			int distToT = converter.distance(vertex.getCoordinates(), this.t.getCoordinates());
			vertex.setDistance(vertex.getDistance() + distStoT - distToT);
		}
	}
}