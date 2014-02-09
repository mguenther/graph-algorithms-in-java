package de.mgu.algorithms.graphs.converter;

import java.util.HashMap;
import java.util.Map;

import de.mgu.algorithms.adt.graph.DirectedGraph;
import de.mgu.algorithms.adt.graph.DirectedGraphImpl;
import de.mgu.algorithms.adt.graph.Edge;
import de.mgu.algorithms.adt.graph.Vertex;

public class TransposeGraph<V extends Vertex, E extends Edge> {
	
	@SuppressWarnings("unchecked")
	public DirectedGraph<V, E> convert(DirectedGraph<V, E> graph) {
		DirectedGraph<V, E> graph_ = new DirectedGraphImpl<V, E>(graph.getEdgeFactory().edgeClass());
		
		Map<String, V> vertices = new HashMap<String, V>();
		
		for (V vertex : graph.vertices()) {
			V clone = (V) vertex.clone();
			vertices.put(clone.getId(), clone);
			graph_.addVertex(clone);
		}
		
		for (E edge : graph.edges()) {
			V head_ = vertices.get(edge.tail.getId());
			V tail_ = vertices.get(edge.head.getId());
			E reversedEdge = graph_.connect(head_, tail_);
			reversedEdge.copyPrimitiveAttributesFrom(edge);
		}
		
		return graph_;
	}
	
	public DirectedGraph<V, E> undo(DirectedGraph<V, E> graph) {
		return convert(graph);
	}
}