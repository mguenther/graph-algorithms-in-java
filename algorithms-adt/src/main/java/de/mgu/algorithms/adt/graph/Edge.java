package de.mgu.algorithms.adt.graph;

public class Edge {

    public Vertex head;

    public Vertex tail;
    
    public void copyPrimitiveAttributesFrom(Object obj) {
    	if (!(obj instanceof Edge)) {
    		throw new IllegalArgumentException("Can only copy attributes from instances " + 
    				"of type " + this.getClass().getCanonicalName());
    	}
    }
}