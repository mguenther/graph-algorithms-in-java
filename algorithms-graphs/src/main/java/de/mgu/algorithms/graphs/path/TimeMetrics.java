package de.mgu.algorithms.graphs.path;

public class TimeMetrics {
	public long start;
	public long stop;
	
	public long diff() {
		return stop-start;
	}
}