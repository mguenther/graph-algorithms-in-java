package de.mgu.algorithms.adt.graph;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UndirectedGraphTest {

    private UndirectedGraph<DummyNode, DummyEdge> graph;

    @Before
    public void beforeTest() {
        this.graph = new UndirectedGraphImpl<DummyNode, DummyEdge>(DummyEdge.class);
    }

    @Test
    public void testDegreeOfSimple() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        DummyNode v3 = new DummyNode(3);
        this.graph.addVertex(v1);
        this.graph.addVertex(v2);
        this.graph.addVertex(v3);
        DummyEdge e1 = this.graph.connect(v1, v2);
        this.graph.connect(v1, v3);
        this.graph.connect(v2, v3);

        assertEquals(2, this.graph.degreeOf(v1));
        assertEquals(2, this.graph.degreeOf(v2));
        assertEquals(2, this.graph.degreeOf(v3));

        this.graph.disconnect(e1);
        assertEquals(1, this.graph.degreeOf(v1));
        assertEquals(1, this.graph.degreeOf(v2));
        assertEquals(2, this.graph.degreeOf(v3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDegreeOfThrowsIllegalArgument() {
        this.graph.degreeOf(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testDegreeOfThrowsIllegalState() {
        this.graph.degreeOf(new DummyNode(1));
    }
}