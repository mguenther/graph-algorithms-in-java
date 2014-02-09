package de.mgu.algorithms.graphs.basic;

import de.mgu.algorithms.adt.graph.Vertex;

public class StatefulVertex extends Vertex {

    public enum VertexState {
        UNVISITED, DISCOVERED, VISITED, FINISHED;
    }

    private VertexState state = VertexState.UNVISITED;

    private int discoveryTime = Integer.MIN_VALUE;

    private int finishingTime = Integer.MIN_VALUE;

    public StatefulVertex(String id) {
        super(id);
    }

    public VertexState getState() {
        return state;
    }

    public void setState(VertexState state) {
        this.state = state;
    }

    public int getDiscoveryTime() {
        return discoveryTime;
    }

    public void setDiscoveryTime(int discoveryTime) {
        this.discoveryTime = discoveryTime;
    }

    public int getFinishingTime() {
        return finishingTime;
    }

    public void setFinishingTime(int finishingTime) {
        this.finishingTime = finishingTime;
    }

    public String toString() {
        return "v[id=" + this.getId() + ", d(v)=" + this.discoveryTime + ", f(v)=" + this.finishingTime + "]";
    }
}