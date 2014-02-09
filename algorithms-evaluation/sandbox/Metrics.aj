package de.mgu.algorithms.graph.eval;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.mgu.algorithms.graphs.path.Dijkstra;

public aspect Metrics {

	private Map<Class<?>, List<Summary>> summaries = new HashMap<Class<?>, List<Summary>>();

	private int pqOps = 0;

	private long tsAlgorithmStarted;

	private long tsAlgorithmCompleted;

	private long tsInitStarted;

	private long tsInitCompleted;

	pointcut evaluation():
		execution(public static void main(..)) 
		&& within(de.mgu.algorithms.graph.eval..*);

	pointcut extractFromPQ():
		call(protected * Dijkstra+.getNodeWithMinimumDistance(..));

	pointcut executeSearch():
		call(private void Dijkstra+.dijkstra());

	pointcut executeInit():
		call(protected void Dijkstra+.init*(..));

	after(): extractFromPQ() {
		pqOps++;
	}

	before(): executeInit() {
		tsInitStarted = System.nanoTime();
	}

	after(): executeInit() {
		tsInitCompleted = System.nanoTime();
	}

	before(): executeSearch() {
		pqOps = 0;
		tsAlgorithmStarted = System.nanoTime();
	}

	after(): executeSearch() {
		tsAlgorithmCompleted = System.nanoTime();

		long execTime = tsAlgorithmCompleted - tsAlgorithmStarted;
		long initTime = tsInitCompleted - tsInitStarted;

		Class<?> clazz = thisJoinPoint.getTarget().getClass();

		keepSummary(execTime, initTime, clazz);
	}

	after(): evaluation() {
		Iterator<Class<?>> iter = this.summaries.keySet().iterator();

		while (iter.hasNext()) {
			Class<?> key = iter.next();
			buildReport(key);
		}
	}

	private void buildReport(Class<?> key) {
		List<Summary> summaries = this.summaries.get(key);
		
		int size = summaries.size();
		
		if (size == 0) return;
		
		int pqOps = summaries.get(0).getPqOps();
		
		long[] initTimes = new long[size];
		long[] execTimes = new long[size];
		
		for (int i = 0; i < size; i++) {
			Summary summary = summaries.get(i);
			initTimes[i] = summary.getInitTime();
			execTimes[i] = summary.getExecTime();
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append("\nMetrics for class: ")
			   .append(key.getCanonicalName())
			   .append(" (runs: ")
			   .append(size)
			   .append(")\n")
			   .append("  ")
			   .append("init time (avg): ")
			   .append(averageTime(initTimes))
			   .append(" ns [")
			   .append(nanoToMilli(averageTime(initTimes)))
			   .append(" ms]\n")
			   .append("  ")
			   .append("exec time (avg): ")
			   .append(averageTime(execTimes))
			   .append(" ns [")
			   .append(nanoToMilli(averageTime(execTimes)))
			   .append(" ms]")
			   .append(" \n")
			   .append("  pq ops (single exec): ")
			   .append(pqOps)
			   .append("\n\n");
		System.out.println(builder.toString());
	}

	private double nanoToMilli(double time) {
		return (double) time / 1000000.0;
	}

	private double averageTime(long[] times) {
		double sum = 0.0;
		for (int i = 0; i < times.length; i++) {
			sum += (double) times[i];
		}
		return sum / times.length;
	}

	private void keepSummary(long execTime, long initTime, Class<?> clazz) {
		Summary summary = new Summary(execTime, initTime, this.pqOps);

		if (!this.summaries.containsKey(clazz)) {
			this.summaries.put(clazz, new LinkedList<Summary>());
		}

		this.summaries.get(clazz).add(summary);
	}
}