package de.mgu.algorithms.graphs.path;

import de.mgu.algorithms.graphs.converter.ApplyGoalDirectedSearch;

public class DijkstraHeapGoalDirected<V extends PathVertex, E extends PathEdge> extends DijkstraHeap<V, E> {

	private ApplyGoalDirectedSearch<V, E> proc;
	
	// breaks the Liskov-Substitution-Principle (LSP)
	@Override
	protected void initialize(V start) {
		throw new UnsupportedOperationException("Goal-directed search requires start and " +
				" goal vertex.");
	}
	
	@Override
	protected void initialize(V start, V goal) {
		this.proc = new ApplyGoalDirectedSearch<V, E>(getGraph(), start, goal);
		this.proc.convertEdgeWeights();
		super.initialize(start, goal);
	}
	
	@Override
	protected void postprocess() {
		this.proc.undoEdgeWeights();
		this.proc.undoVertexDistances();
	}
}