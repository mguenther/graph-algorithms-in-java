package de.mgu.algorithms.adt.graph;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EdgeUtilsTest {

    private DummyEdge e1;

    private DummyEdge e2;

    private DummyEdge e3;

    private DummyNode v1;

    private DummyNode v2;

    @Before
    public void setup() {
        this.v1 = new DummyNode(1);
        this.v2 = new DummyNode(2);
        this.e1 = new DummyEdge();
        this.e2 = new DummyEdge();
        this.e3 = new DummyEdge();
        this.e1.head = v1;
        this.e1.tail = v2;
        this.e2.head = v2;
        this.e2.tail = v1;
        this.e3.head = v1;
        this.e3.tail = v2;
    }

    @Test
    public void testEdgeEqualityDirected() {
        assertFalse(EdgeUtils.edgeEqualityDirected(e1, e2));
        assertFalse(EdgeUtils.edgeEqualityDirected(e1, v2, v1));
        assertTrue(EdgeUtils.edgeEqualityDirected(e1, e3));
        assertTrue(EdgeUtils.edgeEqualityDirected(e1, v1, v2));
    }

    @Test
    public void testEdgeEqualityUndirected() {
        assertTrue(EdgeUtils.edgeEqualityUndirected(e1, e2));
        assertTrue(EdgeUtils.edgeEqualityUndirected(e1, e3));
        assertTrue(EdgeUtils.edgeEqualityUndirected(e1, v1, v2));
        assertTrue(EdgeUtils.edgeEqualityUndirected(e1, v2, v1));
    }
}