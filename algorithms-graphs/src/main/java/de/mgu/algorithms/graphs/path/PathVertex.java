package de.mgu.algorithms.graphs.path;

import java.awt.Point;

import de.mgu.algorithms.adt.graph.Vertex;

public class PathVertex extends Vertex {

    public static int DEFAULT_VALUE = Integer.MAX_VALUE;

    private int distance = DEFAULT_VALUE;
    
    private Point coordinates = new Point();
    
    public PathVertex(String id) {
    	super(id);
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
    
    public Point getCoordinates() {
    	return this.coordinates;
    }
    
    public void setCoordinates(Point coordinates) {
    	if (coordinates == null) {
    		throw new IllegalArgumentException("Expects a non-null Point reference.");
    	}
    	
    	this.coordinates = coordinates;
    }
    
    @Override
    public Object clone() {
    	PathVertex clone = new PathVertex(getId());
    	clone.setDistance(getDistance());
    	clone.setCoordinates(getCoordinates());
    	return clone;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertex#[");
        if (this.getId() != null) {
            sb.append("id=");
            sb.append(this.getId());
            sb.append(", ");
        }
        sb.append("distance=");
        sb.append(this.distance);
        sb.append("]");

        return sb.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
}