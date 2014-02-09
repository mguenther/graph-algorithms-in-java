package de.mgu.algorithms.adt.graph;

import java.util.LinkedHashSet;
import java.util.Set;

public class Graphs {

    private Graphs() {}

    public static <V extends Vertex, E extends Edge> Set<V> shallowCopyOfVertices(Graph<V, E> graph) {
        Set<V> vertices = new LinkedHashSet<V>();
        vertices.addAll(graph.vertices());
        return vertices;
    }

    public static <V extends Vertex, E extends Edge> Set<E> shallowCopyOfEdges(Graph<V, E> graph, V head, V tail) {
        Set<E> edges = new LinkedHashSet<E>();
        edges.addAll(graph.getAllEdges(head, tail));
        return edges;
    }

    public static <V extends Vertex, E extends Edge> void dumpVerticesToStdout(Graph<V, E> graph) {
        for (V vertex : graph.vertices()) {
            System.out.println(vertex);
        }
    }
}