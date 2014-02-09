package de.mgu.algorithms.adt.graph;

public class DummyEdge extends Edge {
    @Override
    public String toString() {
        return "(" + this.head + ", " + this.tail + ")";
    }
}