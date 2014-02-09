package de.mgu.algorithms.graphs.path;

import de.mgu.algorithms.adt.graph.Edge;

public class PathEdge extends Edge {

    private int cost;

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
    
    @Override
    public void copyPrimitiveAttributesFrom(Object obj) {
    	if (!(obj instanceof PathEdge)) {
    		throw new IllegalArgumentException("Can only copy attributes from instances " + 
    				"of type " + this.getClass().getCanonicalName());
    	}
    	
    	PathEdge source = (PathEdge)obj;
    	this.setCost(source.getCost());
    }
}