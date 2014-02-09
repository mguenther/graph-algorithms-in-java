package de.mgu.algorithms.graphs.path;

public class SPResult {

	private TimeMetrics timeInit;
	
	private TimeMetrics timeSearch;
	
	private TimeMetrics timePost;
	
	private int pqOps;

	public TimeMetrics getTimeInit() {
		return timeInit;
	}

	public void setTimeInit(TimeMetrics timeInit) {
		this.timeInit = timeInit;
	}

	public TimeMetrics getTimeSearch() {
		return timeSearch;
	}

	public void setTimeSearch(TimeMetrics timeSearch) {
		this.timeSearch = timeSearch;
	}

	public TimeMetrics getTimePost() {
		return timePost;
	}

	public void setTimePost(TimeMetrics timePost) {
		this.timePost = timePost;
	}

	public int getPqOps() {
		return pqOps;
	}

	public void setPqOps(int pqOps) {
		this.pqOps = pqOps;
	}
}