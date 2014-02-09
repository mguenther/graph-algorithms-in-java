package de.mgu.algorithms.graphs.path;

import org.junit.Test;

import de.mgu.algorithms.adt.graph.DirectedGraph;
import de.mgu.algorithms.adt.graph.DirectedGraphImpl;
import de.mgu.algorithms.graphs.converter.TransposeGraph;
import de.mgu.algorithms.graphs.path.Dijkstra;
import de.mgu.algorithms.graphs.path.DijkstraDial;
import de.mgu.algorithms.graphs.path.DijkstraDialBidir;
import de.mgu.algorithms.graphs.path.DijkstraHeap;
import de.mgu.algorithms.graphs.path.DijkstraHeapBidir;
import de.mgu.algorithms.graphs.path.PathEdge;
import de.mgu.algorithms.graphs.path.PathVertex;

import static org.junit.Assert.*;

public class DijkstraTest {

    private DirectedGraph<PathVertex, PathEdge> graph;

    public PathVertex[] constructTestGraphAndGetStartVertex() {
        this.graph = null;
        this.graph = new DirectedGraphImpl<PathVertex, PathEdge>(PathEdge.class);

        PathVertex[] vertices = new PathVertex[10];

        vertices[0] = new PathVertex("Frankfurt");
        vertices[1] = new PathVertex("Mannheim");
        vertices[2] = new PathVertex("Würzburg");
        vertices[3] = new PathVertex("Stuttgart");
        vertices[4] = new PathVertex("Kassel");
        vertices[5] = new PathVertex("Karlsruhe");
        vertices[6] = new PathVertex("Erfurt");
        vertices[7] = new PathVertex("Nürnberg");
        vertices[8] = new PathVertex("Augsburg");
        vertices[9] = new PathVertex("München");

        for (int i = 0; i < vertices.length; i++) {
            this.graph.addVertex(vertices[i]);
        }

        this.graph.connect(vertices[0], vertices[1]).setCost(85);       // Frankfurt -- Mannheim
        this.graph.connect(vertices[0], vertices[2]).setCost(217);      // Frankfurt -- Würzburg
        this.graph.connect(vertices[0], vertices[4]).setCost(173);      // Frankfurt -- Kassel
        this.graph.connect(vertices[1], vertices[5]).setCost(80);       // Mannheim -- Karlsruhe
        this.graph.connect(vertices[2], vertices[6]).setCost(186);      // Würzburg -- Erfurt
        this.graph.connect(vertices[2], vertices[7]).setCost(103);      // Würzburg -- Nürnberg
        this.graph.connect(vertices[3], vertices[7]).setCost(183);      // Stuttgart -- Nürnberg
        this.graph.connect(vertices[4], vertices[9]).setCost(502);      // Kassel -- München
        this.graph.connect(vertices[5], vertices[8]).setCost(250);      // Karlsruhe -- Augsburg
        this.graph.connect(vertices[7], vertices[9]).setCost(167);      // Nürnberg -- München
        this.graph.connect(vertices[8], vertices[9]).setCost(84);       // Augsburg -- München

        this.graph.connect(vertices[1], vertices[0]).setCost(85);
        this.graph.connect(vertices[2], vertices[0]).setCost(217);
        this.graph.connect(vertices[4], vertices[0]).setCost(173);
        this.graph.connect(vertices[5], vertices[1]).setCost(80);
        this.graph.connect(vertices[6], vertices[2]).setCost(186);
        this.graph.connect(vertices[7], vertices[2]).setCost(103);
        this.graph.connect(vertices[7], vertices[3]).setCost(183);
        this.graph.connect(vertices[9], vertices[4]).setCost(502);
        this.graph.connect(vertices[8], vertices[5]).setCost(250);
        this.graph.connect(vertices[9], vertices[7]).setCost(167);
        this.graph.connect(vertices[9], vertices[8]).setCost(84);

        return vertices;
    }

    @Test
    public void testDijkstraHeapImplementation() {
        PathVertex[] vertices = constructTestGraphAndGetStartVertex();

        Dijkstra<PathVertex, PathEdge> dijkstra = new DijkstraHeap<PathVertex, PathEdge>();
        dijkstra.findShortestPath(this.graph, vertices[0]);

        for (PathVertex vertex : this.graph.vertices()) {
            System.out.println(vertex);
        }

        correctNodeDistances(vertices);
    }
    
    @Test
    public void testDijkstraDialsImplementation() {
    	PathVertex[] vertices = constructTestGraphAndGetStartVertex();
    	Dijkstra<PathVertex, PathEdge> dijkstra = new DijkstraDial<PathVertex, PathEdge>();
    	dijkstra.findShortestPath(this.graph, vertices[0]);
    	correctNodeDistances(vertices);
    }
    
    @Test
    public void testDijkstraHeapBidirImplementation() {
    	PathVertex[] vertices = constructTestGraphAndGetStartVertex();
    	TransposeGraph<PathVertex, PathEdge> transposer = new TransposeGraph<PathVertex, PathEdge>();
    	DirectedGraph<PathVertex, PathEdge> transposedGraph = transposer.convert(this.graph);
    	DijkstraHeapBidir<PathVertex, PathEdge> dijkstra = new DijkstraHeapBidir<PathVertex, PathEdge>();
    	dijkstra.findShortestPath(this.graph, transposedGraph, vertices[0], vertices[9]);
    	assertEquals(487, dijkstra.getDistance());
    }
    
    @Test
    public void testDijkstraDialsBidir() {
    	PathVertex[] vertices = constructTestGraphAndGetStartVertex();
    	TransposeGraph<PathVertex, PathEdge> transposer = new TransposeGraph<PathVertex, PathEdge>();
    	DirectedGraph<PathVertex, PathEdge> transposedGraph = transposer.convert(this.graph);
    	DijkstraDialBidir<PathVertex, PathEdge> dijkstra = new DijkstraDialBidir<PathVertex, PathEdge>();
    	dijkstra.findShortestPath(this.graph, transposedGraph, vertices[0], vertices[9]);
    	assertEquals(487, dijkstra.getDistance());
    }

    private void correctNodeDistances(PathVertex[] vertices) {
        assertEquals(0, vertices[0].getDistance());
        assertEquals(85, vertices[1].getDistance());
        assertEquals(217, vertices[2].getDistance());
        assertEquals(503, vertices[3].getDistance());
        assertEquals(173, vertices[4].getDistance());
        assertEquals(165, vertices[5].getDistance());
        assertEquals(403, vertices[6].getDistance());
        assertEquals(320, vertices[7].getDistance());
        assertEquals(415, vertices[8].getDistance());
        assertEquals(487, vertices[9].getDistance());
    }
}