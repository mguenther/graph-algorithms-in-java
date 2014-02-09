package de.mgu.algorithms.graphs.basic;

import de.mgu.algorithms.adt.graph.Edge;
import de.mgu.algorithms.adt.graph.ErrorMessages;
import de.mgu.algorithms.adt.graph.Graph;
import de.mgu.algorithms.adt.graph.Graphs;

import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

public class Scanner<V extends StatefulVertex, E extends Edge> {

    private Logger LOGGER = Logger.getLogger(this.getClass().getName());

    private Graph<V, E> graph;

    private Stack<V> Q;

    private int timer = 0;

    public Scanner(Graph<V, E> graph) {
        this.graph = graph;
    }

    public void scan(V startingVertex) {
        initialize(startingVertex);

        Set<V> vertices = Graphs.shallowCopyOfVertices(this.graph);

        while (vertices.size() > 0) {
            populateStackIfEmpty(vertices);
            V vertex = visitNextVertex();
            boolean discoveredUnvisitedNeighbour = walkOutgoingEdges(vertex);

           if (!discoveredUnvisitedNeighbour) {
                vertex.setState(StatefulVertex.VertexState.FINISHED);
                vertex.setFinishingTime(++this.timer);
                this.Q.remove(vertex);
                vertices.remove(vertex);
                LOGGER.fine("Finished vertex " + vertex + " at " + this.timer);
            }
        }
    }

    private void initialize(V startingNode) {
        if (!this.graph.containsVertex(startingNode)) {
            throw new IllegalStateException(ErrorMessages.VERTEX_NOT_FOUND);
        }

        resetStateOfAllVertices();
        startingNode.setDiscoveryTime(0);
        startingNode.setState(StatefulVertex.VertexState.DISCOVERED);

        this.timer = 0;
        this.Q = new Stack<V>();
        this.Q.add(startingNode);
    }

    private void resetStateOfAllVertices() {
        for (V vertex : this.graph.vertices()) {
            vertex.setState(StatefulVertex.VertexState.UNVISITED);
        }
    }

    private void populateStackIfEmpty(Set<V> vertices) {
        // in the presence of multiple connected components, the stack will be empty during the algorithm,
        // while there are still nodes that have not been discovered yet, since there are unreachable from
        // the current connected component. this method selects one of the remaining nodes indeterministically
        // and adds it to the stack, thus enabling the algorithm to continue labeling nodes of other
        // connected components
        if (this.Q.isEmpty()) {
            // just in case we have multiple connected components
            V vertexOfOtherComponent = vertices.iterator().next();
            vertexOfOtherComponent.setDiscoveryTime(++timer);
            this.Q.push(vertexOfOtherComponent);
        }
    }

    private V visitNextVertex() {
        V vertex = this.Q.peek();
        vertex.setState(StatefulVertex.VertexState.VISITED);
        return vertex;
    }

    @SuppressWarnings("unchecked")
    private boolean walkOutgoingEdges(V vertex) {
    	Set<E> edges = this.graph.edgesOfV(vertex);

        boolean discoveredNeighbours = false;

        LOGGER.fine("Walking through neighbours of vertex " + vertex);

        for (E edge : edges) {
            V neighbour = (V) edge.tail;

            LOGGER.fine("  Checking neighbour " + neighbour + " (state=" + neighbour.getState());

            if (neighbour.getState().equals(StatefulVertex.VertexState.UNVISITED)) {
                neighbour.setState(StatefulVertex.VertexState.DISCOVERED);
                neighbour.setDiscoveryTime(++this.timer);
                this.Q.push(neighbour);

                discoveredNeighbours = true;

                LOGGER.fine("  Pushed neighbour " + neighbour + " on top of the stack (discovery at " + this.timer + ")");
                break; // we will come back later, this is a DFS algorithm
            }
        }

        return discoveredNeighbours;
    }
}