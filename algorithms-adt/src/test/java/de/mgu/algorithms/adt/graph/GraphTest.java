package de.mgu.algorithms.adt.graph;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import static org.junit.Assert.*;

public class GraphTest {

    private Graph<DummyNode, DummyEdge> graph;

    private Graph<DummyNode, DummyEdge> directedGraph;

    @Before
    public void beforeTest() {
        this.graph = new UndirectedGraphImpl<DummyNode, DummyEdge>(DummyEdge.class);
        this.directedGraph = new DirectedGraphImpl<DummyNode, DummyEdge>(DummyEdge.class);
    }

    @Test
    public void testCorrectDirectionIfGraphIsUndirected() {
        Graph<DummyNode, DummyEdge> graph = new UndirectedGraphImpl<DummyNode, DummyEdge>(DummyEdge.class);
        assertFalse(graph.isDirected());
    }

    @Test
    public void testCorrectDirectionIfGraphIsDirected() {
        Graph<DummyNode, DummyEdge> graph = new DirectedGraphImpl<DummyNode, DummyEdge>(DummyEdge.class);
        assertTrue(graph.isDirected());
    }

    @Test
    public void testAddSingleVertex() {
        DummyNode dummy = new DummyNode(1);
        boolean successful = this.graph.addVertex(dummy);
        assertTrue(successful);
    }

    @Test
    public void testAddSameVertexTwice() {
        DummyNode dummy = new DummyNode(1);
        assertTrue(this.graph.addVertex(dummy));
        assertFalse(this.graph.addVertex(dummy));
        assertEquals(1, this.graph.sizeOfV());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVertexWithNullArgumentFails() {
        this.graph.addVertex(null);
    }

    @Test
    public void testRemoveExistingVertex() {
        DummyNode vertex = new DummyNode(1);
        this.graph.addVertex(vertex);
        assertEquals(1, this.graph.sizeOfV());
        assertTrue(this.graph.removeVertex(vertex));
        assertFalse(this.graph.containsVertex(vertex));
        assertEquals(0, this.graph.sizeOfV());
    }

    @Test
    public void testRemoveNonExistingVertex() {
        DummyNode vertex = new DummyNode(1);
        assertFalse(this.graph.removeVertex(vertex));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveVertexWithNullArgumentFails() {
        this.graph.removeVertex(null);
    }

    @Test
    public void testContainsVertexSucceedsProperly() {
        DummyNode vertex = new DummyNode(1);
        this.graph.addVertex(vertex);
        assertTrue(this.graph.containsVertex(vertex));
    }

    @Test
    public void testContainsVertexFailsProperly() {
        assertFalse(this.graph.containsVertex(new DummyNode(1)));
        assertFalse(this.graph.containsVertex(null));
    }

    @Test
    public void testCorrectSetOfVertices() {
        Collection<DummyNode> probeVertices = new LinkedList<DummyNode>();
        probeVertices.add(new DummyNode(1));
        probeVertices.add(new DummyNode(2));
        probeVertices.add(new DummyNode(3));

        for (DummyNode vertex : probeVertices) {
            assertTrue(this.graph.addVertex(vertex));
        }

        Collection<DummyNode> vertices = this.graph.vertices();

        assertNotNull(vertices);
        assertEquals(3, vertices.size());
        assertTrue(vertices.containsAll(probeVertices));
    }

    @Test
    public void testSetOfVerticesIsEmptyNotNull() {
        assertEquals(0, this.graph.sizeOfV());
        Collection<DummyNode> vertices = this.graph.vertices();
        assertNotNull(vertices);
        assertEquals(0, vertices.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetOfVerticesIsUnmodifiableRemove() {
        DummyNode vertex = new DummyNode(1);
        assertTrue(this.graph.addVertex(vertex));
        Collection<DummyNode> vertices = this.graph.vertices();
        vertices.remove(vertex);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetOfVerticesIsUnmodifiableAdd() {
        DummyNode v1 = new DummyNode(1);
        assertTrue(this.graph.addVertex(v1));
        Collection<DummyNode> vertices = this.graph.vertices();
        vertices.add(new DummyNode(2));
    }

    @Test
    public void testGetSingleEdge() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        assertTrue(this.graph.addVertex(v1));
        assertTrue(this.graph.addVertex(v2));
        DummyEdge edge = this.graph.connect(v1, v2);
        assertNotNull(edge);
        DummyEdge edgeProbe = this.graph.getEdge(v1, v2);
        assertEquals(edge, edgeProbe);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEdgeThrowsIllegalArgumentOnHeadNull() {
        DummyNode v2 = new DummyNode(1);
        this.graph.getEdge(null, v2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEdgeThrowsIllegalArgumentOnTailNull() {
        DummyNode v1 = new DummyNode(2);
        this.graph.addVertex(v1);
        this.graph.getEdge(v1, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetEdgeThrowsIllegalStateOnMissingHead() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        assertTrue(this.graph.addVertex(v1));
        this.graph.getEdge(v1, v2);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetEdgeThrowsIllegalStateOnMissingTail() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        assertTrue(this.graph.addVertex(v2));
        this.graph.getEdge(v1, v2);
    }

    @Test
    public void testGetEdgeReturnsNullIfEdgeNotPresent() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        assertTrue(this.graph.addVertex(v1));
        assertTrue(this.graph.addVertex(v2));
        DummyEdge edge = this.graph.getEdge(v1, v2);
        assertNull(edge);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAllEdgesThrowsIllegalArgumentOnHeadNull() {
        DummyNode v2 = new DummyNode(1);
        this.graph.getAllEdges(null, v2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAllEdgesThrowsIllegalArgumentOnTailNull() {
        DummyNode v1 = new DummyNode(2);
        this.graph.addVertex(v1);
        this.graph.getAllEdges(v1, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAllEdgesThrowsIllegalStateOnMissingHead() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        this.graph.addVertex(v2);
        this.graph.getAllEdges(v1, v2);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetAllEdgesThrowsIllegalStateOnMissingTail() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        this.graph.addVertex(v1);
        this.graph.getAllEdges(v1, v2);
    }

    @SuppressWarnings("unused")
    @Test(expected = UnsupportedOperationException.class)
    public void testGetAllEdgesIsUnmodifiable() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        DummyNode v3 = new DummyNode(3);

        this.graph.addVertex(v1);
        this.graph.addVertex(v2);
        this.graph.addVertex(v3);

        DummyEdge e1e2 = this.graph.connect(v1, v2);
        DummyEdge e2e3 = this.graph.connect(v2, v3);
        DummyEdge e2e1 = this.graph.connect(v2, v1);

        Set<DummyEdge> edgesBetweenV1V2 = this.graph.getAllEdges(v1, v2);
        edgesBetweenV1V2.remove(e1e2);
    }

    @Test
    public void testEdgesRetrievesWholeEdgeSet() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        DummyNode v3 = new DummyNode(3);

        this.graph.addVertex(v1);
        this.graph.addVertex(v2);
        this.graph.addVertex(v3);

        DummyEdge e1e2 = this.graph.connect(v1, v2);
        DummyEdge e2e3 = this.graph.connect(v2, v3);
        DummyEdge e2e1 = this.graph.connect(v2, v1);

        Set<DummyEdge> edges = this.graph.edges();
        assertEquals(3, edges.size());
        assertTrue(edges.contains(e1e2));
        assertTrue(edges.contains(e2e1));
        assertTrue(edges.contains(e2e3));
    }

    @SuppressWarnings("unused")
    @Test(expected = UnsupportedOperationException.class)
    public void testEdgesIsUnmodifiable() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        DummyNode v3 = new DummyNode(3);

        this.graph.addVertex(v1);
        this.graph.addVertex(v2);
        this.graph.addVertex(v3);

        DummyEdge e1e2 = this.graph.connect(v1, v2);
        DummyEdge e2e3 = this.graph.connect(v2, v3);
        DummyEdge e2e1 = this.graph.connect(v2, v1);

        Set<DummyEdge> edges = this.graph.edges();
        edges.remove(e1e2);
    }

    @Test
    public void testEdgesEmptyButNotNull() {
    	Set<DummyEdge> edges = this.graph.edges();
        assertNotNull(edges);
    }

    @Test
    public void testRemoveVertexDisconnectsAllRelations() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);

        this.graph.addVertex(v1);
        this.graph.addVertex(v2);
        DummyEdge e1 = this.graph.connect(v1, v2);
        DummyEdge e2 = this.graph.connect(v2, v1);

        assertTrue(this.graph.containsEdge(e1));
        assertTrue(this.graph.containsEdge(e2));
        assertEquals(2, this.graph.sizeOfE());

        this.graph.removeVertex(v1);

        assertFalse(this.graph.containsEdge(e1));
        assertFalse(this.graph.containsEdge(e2));
        assertEquals(0, this.graph.sizeOfE());
    }

    @Test
    public void testGetSingleEdgeReversedUndirected() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        assertTrue(this.graph.addVertex(v1));
        assertTrue(this.graph.addVertex(v2));
        DummyEdge edge = this.graph.connect(v1, v2);
        assertNotNull(edge);
        DummyEdge edgeProbe = this.graph.getEdge(v2, v1);
        assertEquals(edge, edgeProbe);
    }

    @Test
    public void testGetSingleEdgeReversedDirectedFails() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        assertTrue(this.directedGraph.addVertex(v1));
        assertTrue(this.directedGraph.addVertex(v2));
        DummyEdge edge = this.directedGraph.connect(v1, v2);
        assertNotNull(edge);
        DummyEdge edgeProbe = this.directedGraph.getEdge(v2, v1);
        assertNull(edgeProbe);
    }

    @Test
    public void testGetAllEdgesUndirected() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        DummyNode v3 = new DummyNode(3);

        this.graph.addVertex(v1);
        this.graph.addVertex(v2);
        this.graph.addVertex(v3);

        DummyEdge e1e2 = this.graph.connect(v1, v2);
        DummyEdge e2e3 = this.graph.connect(v2, v3);
        DummyEdge e2e1 = this.graph.connect(v2, v1);

        Set<DummyEdge> edgesBetweenV1V2 = this.graph.getAllEdges(v1, v2);
        assertTrue(edgesBetweenV1V2.contains(e1e2));
        assertTrue(edgesBetweenV1V2.contains(e2e1));
        assertFalse(edgesBetweenV1V2.contains(e2e3));
        assertEquals(2, edgesBetweenV1V2.size());
    }

    @Test
    public void testGetAllEdgesDirected() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        DummyNode v3 = new DummyNode(3);

        this.directedGraph.addVertex(v1);
        this.directedGraph.addVertex(v2);
        this.directedGraph.addVertex(v3);

        DummyEdge e1e2 = this.directedGraph.connect(v1, v2);
        DummyEdge e2e3 = this.directedGraph.connect(v2, v3);
        DummyEdge e2e1 = this.directedGraph.connect(v2, v1);

        Set<DummyEdge> edgesBetweenV1V2 = this.directedGraph.getAllEdges(v1, v2);
        assertTrue(edgesBetweenV1V2.contains(e1e2));
        assertFalse(edgesBetweenV1V2.contains(e2e1));
        assertFalse(edgesBetweenV1V2.contains(e2e3));
        assertEquals(1, edgesBetweenV1V2.size());
    }

    @Test
    public void testConnectSimpleUndirected() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        assertTrue(this.graph.addVertex(v1));
        assertTrue(this.graph.addVertex(v2));
        DummyEdge e1 = this.graph.connect(v1, v2);
        assertNotNull(e1);
        assertTrue(this.graph.containsEdge(e1));
        DummyEdge probeEdge1 = this.graph.getEdge(v1, v2);
        DummyEdge probeEdge2 = this.graph.getEdge(v2, v1);
        assertNotNull(probeEdge1);
        assertNotNull(probeEdge2);
        assertEquals(1, this.graph.sizeOfE());
    }

    @Test
    public void testConnectSimpleDirected() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        assertTrue(this.graph.addVertex(v1));
        assertTrue(this.graph.addVertex(v2));
        DummyEdge e1 = this.graph.connect(v1, v2);
        assertNotNull(e1);
        assertTrue(this.graph.containsEdge(e1));
        DummyEdge probeEdge = this.graph.getEdge(v1, v2);
        assertNotNull(probeEdge);
        assertEquals(1, this.graph.sizeOfE());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectThrowsIllegalArgumentOnHeadNull() {
        DummyNode v2 = new DummyNode(1);
        this.graph.addVertex(v2);
        this.graph.connect(null, v2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectThrowsIllegalArgumentOnTailNull() {
        DummyNode v1 = new DummyNode(1);
        this.graph.addVertex(v1);
        this.graph.connect(v1, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testConnectThrowsIllegalStateOnHeadMissing() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        this.graph.addVertex(v2);
        this.graph.connect(v1, v2);
    }

    @Test(expected = IllegalStateException.class)
    public void testConnectThrowsIllegalStateOnTailMissing() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        this.graph.addVertex(v1);
        this.graph.connect(v1, v2);
    }

    @Test
    public void testDisconnectAllEdgesBetweenToNodesUndirected() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);

        this.graph.addVertex(v1);
        this.graph.addVertex(v2);

        DummyEdge e1 = this.graph.connect(v1, v2);
        DummyEdge e2 = this.graph.connect(v2, v1);

        assertEquals(2, this.graph.sizeOfE());
        assertTrue(this.graph.containsEdge(e1));
        assertTrue(this.graph.containsEdge(e2));

        assertTrue(this.graph.disconnectAll(v1, v2));

        assertEquals(0, this.graph.sizeOfE());
        assertFalse(this.graph.containsEdge(e1));
        assertFalse(this.graph.containsEdge(e2));
    }

    @Test
    public void testDisconnectAllEdgesBetweenTwoNodesDirected() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);

        this.directedGraph.addVertex(v1);
        this.directedGraph.addVertex(v2);

        DummyEdge e1 = this.directedGraph.connect(v1, v2);
        DummyEdge e2 = this.directedGraph.connect(v2, v1);

        assertEquals(2, this.directedGraph.sizeOfE());
        assertTrue(this.directedGraph.containsEdge(e1));
        assertTrue(this.directedGraph.containsEdge(e2));

        assertTrue(this.directedGraph.disconnectAll(v1, v2));

        assertEquals(1, this.directedGraph.sizeOfE());
        assertFalse(this.directedGraph.containsEdge(e1));
        assertTrue(this.directedGraph.containsEdge(e2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisconnectAllThrowsIllegalArgumentOnHeadNull() {
        DummyNode v2 = new DummyNode(1);
        this.graph.disconnectAll(null, v2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisconnectAllThrowsIllegalArgumentOnTailNull() {
        DummyNode v1 = new DummyNode(1);
        this.graph.addVertex(v1);
        this.graph.disconnectAll(v1, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testDisconnectAllThrowsIllegalStateOnHeadMissing() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        this.graph.addVertex(v2);
        this.graph.disconnectAll(v1, v2);
    }

    @Test(expected = IllegalStateException.class)
    public void testDisconnectAllThrowsIllegalStateOnTailMissing() {
        DummyNode v1 = new DummyNode(1);
        DummyNode v2 = new DummyNode(2);
        this.graph.addVertex(v1);
        this.graph.disconnectAll(v1, v2);
    }
    
    @Test
    public void testIfVertexCanBeFetchedById() {
    	DummyNode v1 = new DummyNode(1);
    	this.graph.addVertex(v1);
    	DummyNode v2 = this.graph.getVertexById("1");
    	assertNotNull(v2);
    	assertEquals(v1, v2);
    }
    
    @Test
    public void testIfMissingVertexIdReturnsNull() {
    	assertNull(this.graph.getVertexById("1"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullVertexIdThrowsIllegalArgument() {
    	this.graph.getVertexById(null);
    }
}