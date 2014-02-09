package de.mgu.algorithms.adt.graph;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class DirectedGraphTest {

    private DirectedGraph<DummyNode, DummyEdge> graph;

    private DummyNode[] vertices;

    private DummyEdge[] edges;

    @Before
    public void beforeTest() {
        this.graph = new DirectedGraphImpl<DummyNode, DummyEdge>(DummyEdge.class);
        this.vertices = new DummyNode[4];
        this.vertices[0] = new DummyNode(1);
        this.vertices[1] = new DummyNode(2);
        this.vertices[2] = new DummyNode(3);
        this.vertices[3] = new DummyNode(4);

        for (int i = 0; i < 4; i++) {
            this.graph.addVertex(vertices[i]);
        }

        this.edges = new DummyEdge[5];
        this.edges[0] = this.graph.connect(this.vertices[0], this.vertices[1]);
        this.edges[1] = this.graph.connect(this.vertices[0], this.vertices[2]);
        this.edges[2] = this.graph.connect(this.vertices[0], this.vertices[3]);
        this.edges[3] = this.graph.connect(this.vertices[1], this.vertices[3]);
        this.edges[4] = this.graph.connect(this.vertices[2], this.vertices[3]);
    }

    @Test
    public void testInDegreeOfVertices() {
        assertEquals(0, this.graph.inDegreeOf(this.vertices[0]));
        assertEquals(1, this.graph.inDegreeOf(this.vertices[1]));
        assertEquals(1, this.graph.inDegreeOf(this.vertices[2]));
        assertEquals(3, this.graph.inDegreeOf(this.vertices[3]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInDegreeOfVerticesFailsWithNullArgument() {
        this.graph.inDegreeOf(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testInDegreeOfVerticesFailsWithNonExistingVertex() {
        this.graph.inDegreeOf(new DummyNode(5));
    }

    @Test
    public void testOutDegreeOfVertices() {
        assertEquals(3, this.graph.outDegreeOf(this.vertices[0]));
        assertEquals(1, this.graph.outDegreeOf(this.vertices[1]));
        assertEquals(1, this.graph.outDegreeOf(this.vertices[2]));
        assertEquals(0, this.graph.outDegreeOf(this.vertices[3]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutDegreeOfVerticesFailsWithNullArgument() {
        this.graph.outDegreeOf(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testOutDegreeOfVerticesFailsWithNonExistingVertex() {
        this.graph.outDegreeOf(new DummyNode(5));
    }

    @Test
    public void testIncomingEdgesOfVertices() {
        Set<DummyEdge> edgesOfV1 = this.graph.incomingEdgesOf(this.vertices[0]);
        assertEquals(0, edgesOfV1.size());

        Set<DummyEdge> edgesOfV2 = this.graph.incomingEdgesOf(this.vertices[1]);
        assertEquals(1, edgesOfV2.size());
        assertTrue(edgesOfV2.contains(this.edges[0]));

        Set<DummyEdge> edgesOfV3 = this.graph.incomingEdgesOf(this.vertices[2]);
        assertEquals(1, edgesOfV3.size());
        assertTrue(edgesOfV3.contains(this.edges[1]));

        Set<DummyEdge> edgesOfV4 = this.graph.incomingEdgesOf(this.vertices[3]);
        assertEquals(3, edgesOfV4.size());
        assertTrue(edgesOfV4.contains(this.edges[2]));
        assertTrue(edgesOfV4.contains(this.edges[3]));
        assertTrue(edgesOfV4.contains(this.edges[4]));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIncomingEdgesSetIsUnmodifiable() {
    	Set<DummyEdge> unmodifiableEdgeSet = this.graph.incomingEdgesOf(this.vertices[1]);
        unmodifiableEdgeSet.remove(this.edges[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncomingEdgesSetThrowsIllegalArgumentOnVertexNull() {
        this.graph.incomingEdgesOf(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testIncomingEdgesSetThrowsIllegalStateOnVertexMissing() {
        this.graph.incomingEdgesOf(new DummyNode(5));
    }

    @Test
    public void testOutgoingEdgesOfVertices() {
    	Set<DummyEdge> edgesOfV1 = this.graph.outgoingEdgesOf(this.vertices[0]);
        assertEquals(3, edgesOfV1.size());
        assertTrue(edgesOfV1.contains(this.edges[0]));
        assertTrue(edgesOfV1.contains(this.edges[1]));
        assertTrue(edgesOfV1.contains(this.edges[2]));

        Set<DummyEdge> edgesOfV2 = this.graph.outgoingEdgesOf(this.vertices[1]);
        assertEquals(1, edgesOfV2.size());
        assertTrue(edgesOfV2.contains(this.edges[3]));

        Set<DummyEdge> edgesOfV3 = this.graph.outgoingEdgesOf(this.vertices[2]);
        assertEquals(1, edgesOfV3.size());
        assertTrue(edgesOfV3.contains(this.edges[4]));

        Set<DummyEdge> edgesOfV4 = this.graph.outgoingEdgesOf(this.vertices[3]);
        assertEquals(0, edgesOfV4.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOutgoingEdgesSetIsUnmodifiable() {
    	Set<DummyEdge> unmodifiableEdgeSet = this.graph.outgoingEdgesOf(this.vertices[0]);
        unmodifiableEdgeSet.remove(this.edges[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOutgoingEdgesSetThrowsIllegalArgumentOnVertexNull() {
        this.graph.outgoingEdgesOf(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testOutgoingEdgesSetThrowsIllegalStateOnVertexMissing() {
        this.graph.outgoingEdgesOf(new DummyNode(5));
    }
}