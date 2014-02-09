package de.mgu.algorithms.graph.eval;

public class Summary {

	private final long execTime;
	
	private final long initTime;
	
	private final int pqOps;
	
	public Summary(long execTime, long initTime, int pqOps) {
		this.execTime = execTime;
		this.initTime = initTime;
		this.pqOps = pqOps;
	}

	public long getExecTime() {
		return execTime;
	}

	public long getInitTime() {
		return initTime;
	}

	public int getPqOps() {
		return pqOps;
	}
}