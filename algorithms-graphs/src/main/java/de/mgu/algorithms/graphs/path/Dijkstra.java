package de.mgu.algorithms.graphs.path;

import de.mgu.algorithms.adt.graph.DirectedGraph;

import java.util.Set;

abstract public class Dijkstra<V extends PathVertex, E extends PathEdge> {

    private DirectedGraph<V, E> graph;
    
    public TimeMetrics timeInit = new TimeMetrics();
    
    public TimeMetrics timeSearch = new TimeMetrics();
    
    public TimeMetrics timePostprocess = new TimeMetrics();
    
    public int pqOps;

    public void findShortestPath(DirectedGraph<V,E> dag, V start) {
    	pqOps = 0;
    	graph = dag;
    	timeInit.start = System.nanoTime();
        initialize(start);
        timeInit.stop = System.nanoTime();
        timeSearch.start = System.nanoTime();
        dijkstra();
        timeSearch.stop = System.nanoTime();
        timePostprocess.start = System.nanoTime();
        postprocess();
        timePostprocess.stop = System.nanoTime();
    }
    
    public void findShortestPath(DirectedGraph<V,E> dag, V start, V goal) {
    	pqOps = 0;
    	graph = dag;
    	timeInit.start = System.nanoTime();
        initialize(start, goal);
        timeInit.stop = System.nanoTime();
        timeSearch.start = System.nanoTime();
        dijkstra();
        timeSearch.stop = System.nanoTime();
        timePostprocess.start = System.nanoTime();
        postprocess();
        timePostprocess.stop = System.nanoTime();
    }

    private void dijkstra() {
        while (notFinished()) {
            V v = getNodeWithMinimumDistance();
            pqOps++;

            for (E edge : neighboursOf(v)) {
                relax(edge);
            }
        }
    }

    protected DirectedGraph<V, E> getGraph() {
        return this.graph;
    }
    
    protected void postprocess() {
    }
    
    abstract protected void initialize(V start);
    
    abstract protected void initialize(V start, V goal);

    abstract protected boolean notFinished();

    abstract protected V getNodeWithMinimumDistance();

    abstract protected Set<E> neighboursOf(V vertex);

    abstract protected void relax(E edge);
}