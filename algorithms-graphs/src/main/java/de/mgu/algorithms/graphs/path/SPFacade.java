package de.mgu.algorithms.graphs.path;

import de.mgu.algorithms.adt.graph.DirectedGraph;

public class SPFacade {

	public static SPResult dijkstraHeap(
			DirectedGraph<PathVertex, PathEdge> dag, 
			PathVertex start, 
			PathVertex target) {
		Dijkstra<PathVertex, PathEdge> dijkstra = new DijkstraHeap<PathVertex, PathEdge>();
		dijkstra.findShortestPath(dag, start, target);
		return buildResult(dijkstra);
	}
	
	public static SPResult dijkstraHeapBidir(
			DirectedGraph<PathVertex, PathEdge> dag,
			DirectedGraph<PathVertex, PathEdge> transposedDag,
			PathVertex start, 
			PathVertex target) {
		DijkstraHeapBidir<PathVertex, PathEdge> dijkstra = new DijkstraHeapBidir<PathVertex, PathEdge>();
		dijkstra.findShortestPath(dag, transposedDag, start, target);
		return buildResult(dijkstra);
	}
	
	public static SPResult dijkstraHeapGoal(
			DirectedGraph<PathVertex, PathEdge> dag,
			PathVertex start,
			PathVertex target) {
		Dijkstra<PathVertex, PathEdge> dijkstra = new DijkstraHeapGoalDirected<PathVertex, PathEdge>();
		dijkstra.findShortestPath(dag, start, target);
		return buildResult(dijkstra);
	}
	
	public static SPResult dijkstraDial(
			DirectedGraph<PathVertex, PathEdge> dag,
			PathVertex start,
			PathVertex target) {
		Dijkstra<PathVertex, PathEdge> dijkstra = new DijkstraDial<PathVertex, PathEdge>();
		dijkstra.findShortestPath(dag, start, target);
		return buildResult(dijkstra);
	}
	
	public static SPResult dijkstraDialBidir(
			DirectedGraph<PathVertex, PathEdge> dag,
			DirectedGraph<PathVertex, PathEdge> transposedDag,
			PathVertex start,
			PathVertex target) {
		DijkstraDialBidir<PathVertex, PathEdge> dijkstra = new DijkstraDialBidir<PathVertex, PathEdge>();
		dijkstra.findShortestPath(dag, transposedDag, start, target);
		return buildResult(dijkstra);
	}
	
	public static SPResult dijkstraDialGoal(
			DirectedGraph<PathVertex, PathEdge> dag,
			PathVertex start,
			PathVertex target) {
		Dijkstra<PathVertex, PathEdge> dijkstra = new DijkstraDialGoalDirected<PathVertex, PathEdge>();
		dijkstra.findShortestPath(dag, start, target);
		return buildResult(dijkstra);
	}
	
	private static SPResult buildResult(Dijkstra<PathVertex, PathEdge> dijkstra) {
		SPResult result = new SPResult();
		
		result.setPqOps(dijkstra.pqOps);
		result.setTimeInit(dijkstra.timeInit);
		result.setTimeSearch(dijkstra.timeSearch);
		result.setTimePost(dijkstra.timePostprocess);
		
		return result;
	}
}