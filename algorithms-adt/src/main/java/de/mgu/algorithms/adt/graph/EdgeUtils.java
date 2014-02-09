package de.mgu.algorithms.adt.graph;

public class EdgeUtils {

    public static boolean edgeEqualityDirected(Edge edge1, Edge edge2) {
        return edge1.head.equals(edge2.head) && edge1.tail.equals(edge2.tail);
    }

    public static boolean edgeEqualityUndirected(Edge edge1, Edge edge2) {
        return (edge1.head.equals(edge2.head) && edge1.tail.equals(edge2.tail) ||
                edge1.tail.equals(edge2.head) && edge1.head.equals(edge2.tail));
    }

    public static boolean edgeEqualityDirected(Edge edge, Object head, Object tail) {
        return edge.head.equals(head) && edge.tail.equals(tail);
    }

    public static boolean edgeEqualityUndirected(Edge edge, Object head, Object tail) {
        return (edge.head.equals(head) && edge.tail.equals(tail) ||
                edge.tail.equals(head) && edge.head.equals(tail));
    }
}