package de.mgu.algorithms.graphs.basic;

import de.mgu.algorithms.adt.graph.DirectedGraphImpl;
import de.mgu.algorithms.adt.graph.Edge;
import de.mgu.algorithms.adt.graph.Graph;

import de.mgu.algorithms.adt.graph.Graphs;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScannerTest {

    private StatefulVertex[] vertices;

    private Graph<StatefulVertex, Edge> graph;

    @Before
    public void init() {
        this.graph = new DirectedGraphImpl<StatefulVertex, Edge>(Edge.class);
        this.vertices = new StatefulVertex[6];

        this.vertices[0] = new StatefulVertex("1");
        this.vertices[1] = new StatefulVertex("2");
        this.vertices[2] = new StatefulVertex("3");
        this.vertices[3] = new StatefulVertex("4");
        this.vertices[4] = new StatefulVertex("5");
        this.vertices[5] = new StatefulVertex("6");

        for (int i = 0; i < 6; i++) {
            this.graph.addVertex(this.vertices[i]);
        }

        this.graph.connect(this.vertices[0], this.vertices[1]);
        this.graph.connect(this.vertices[1], this.vertices[2]);
        this.graph.connect(this.vertices[1], this.vertices[3]);
        this.graph.connect(this.vertices[1], this.vertices[4]);
        this.graph.connect(this.vertices[2], this.vertices[3]);
        this.graph.connect(this.vertices[2], this.vertices[5]);
        this.graph.connect(this.vertices[3], this.vertices[1]);
        this.graph.connect(this.vertices[3], this.vertices[5]);
        this.graph.connect(this.vertices[4], this.vertices[3]);
    }

    @Test
    public void testGraphScanningWithSmallGraph() {
        Scanner<StatefulVertex, Edge> scanner = new Scanner<StatefulVertex, Edge>(this.graph);
        scanner.scan(this.vertices[0]);

        for (StatefulVertex vertex : this.vertices) {
            assertEquals(StatefulVertex.VertexState.FINISHED, vertex.getState());
        }

        Graphs.dumpVerticesToStdout(this.graph);

        assertEquals(0, this.vertices[0].getDiscoveryTime());
        assertEquals(1, this.vertices[1].getDiscoveryTime());
        assertEquals(2, this.vertices[2].getDiscoveryTime());
        assertEquals(3, this.vertices[3].getDiscoveryTime());
        assertEquals(4, this.vertices[5].getDiscoveryTime());
        assertEquals(5, this.vertices[5].getFinishingTime());
        assertEquals(6, this.vertices[3].getFinishingTime());
        assertEquals(7, this.vertices[2].getFinishingTime());
        assertEquals(8, this.vertices[4].getDiscoveryTime());
        assertEquals(9, this.vertices[4].getFinishingTime());
        assertEquals(10, this.vertices[1].getFinishingTime());
        assertEquals(11, this.vertices[0].getFinishingTime());
    }
}