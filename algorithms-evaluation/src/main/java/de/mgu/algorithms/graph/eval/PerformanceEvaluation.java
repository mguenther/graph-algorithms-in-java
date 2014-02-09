package de.mgu.algorithms.graph.eval;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.mgu.algorithms.adt.graph.DirectedGraph;
import de.mgu.algorithms.graph.io.ShortestPathGraphReader;
import de.mgu.algorithms.graphs.converter.TransposeGraph;
import de.mgu.algorithms.graphs.path.PathEdge;
import de.mgu.algorithms.graphs.path.PathVertex;
import de.mgu.algorithms.graphs.path.SPFacade;
import de.mgu.algorithms.graphs.path.SPResult;

public class PerformanceEvaluation {
	
	private static int REPETITIONS = 50;
	
	private final DirectedGraph<PathVertex, PathEdge> graph;
	
	private final DirectedGraph<PathVertex, PathEdge> reversedGraph;
	
	private final String graphFile;
	
	public PerformanceEvaluation(String graphFile) {
		TransposeGraph<PathVertex, PathEdge> transposer = new TransposeGraph<PathVertex, PathEdge>();
		
		this.graphFile = graphFile;
		this.graph = loadGraph(graphFile);
		this.reversedGraph = transposer.convert(this.graph);
	}
	
	public void evaluate() {
		performExperiment("NYC", "96008", "172944");
		performExperiment("NYC", "0", "5000");
		performExperiment("NYC", "0", "50000");
		performExperiment("NYC", "0", "100000");
	}
	
	private void performExperiment(String name, String sourceId, String targetId) {
		evaluateAlgorithm(name, "heap", sourceId, targetId);
		evaluateAlgorithm(name, "heap_bidir", sourceId, targetId);
		evaluateAlgorithm(name, "heap_goal", sourceId, targetId);
		evaluateAlgorithm(name, "dial", sourceId, targetId);
		evaluateAlgorithm(name, "dial_bidir", sourceId, targetId);
		evaluateAlgorithm(name, "dial_goal", sourceId, targetId);		
	}
	
	private void evaluateAlgorithm(String name, String algorithmName,
				String sourceId, String targetId) {
		
		StringBuilder result = new StringBuilder();
		result.append("# Scenario parameters:\n")
			  .append("# ")
			  .append(algorithmName)
			  .append("\t")
			  .append(REPETITIONS)
			  .append("\t")
			  .append(this.graphFile)
			  .append("\t")
			  .append(sourceId)
			  .append("\t")
			  .append(targetId);
		
		PathVertex s = this.graph.getVertexById(sourceId);
		PathVertex t = this.graph.getVertexById(targetId);
		
		for (int i = 0; i < REPETITIONS; i++) {
			System.out.println("[" + i + "]\t" + algorithmName);
			
			SPResult res = null;
			
			if (algorithmName.equals("heap")) {
				res = SPFacade.dijkstraHeap(this.graph, s, t);
			} else if (algorithmName.equals("heap_bidir")) {
				res = SPFacade.dijkstraHeapBidir(this.graph, this.reversedGraph, s, t);
			} else if (algorithmName.equals("heap_goal")) {
				res = SPFacade.dijkstraHeapGoal(this.graph, s, t);
			} else if (algorithmName.equals("dial")) {
				res = SPFacade.dijkstraDial(this.graph, s, t);
			} else if (algorithmName.equals("dial_bidir")) {
				res = SPFacade.dijkstraDialBidir(this.graph, this.reversedGraph, s, t);
			} else {
				res = SPFacade.dijkstraDialGoal(this.graph, s, t);
			}
						
			long initTime = res.getTimeInit().diff();
			long execTime = res.getTimeSearch().diff();
			long postTime = res.getTimePost().diff();
			int pqOps = res.getPqOps();

			result.append("\n")
				  .append(name)
				  .append("\t")
				  .append(this.graph.getVertexById(targetId).getDistance())
				  .append("\t")
				  .append(pqOps)
				  .append("\t")
				  .append(nanoToMilli(initTime))
				  .append("\t")
				  .append(nanoToMilli(execTime))
				  .append("\t")
				  .append(nanoToMilli(postTime))
				  .append("\t")
				  .append(initTime)
				  .append("\t")
				  .append(execTime)
				  .append("\t")
				  .append(postTime);
						
			resetVertices();
		}
		
		String resultFile = "results/" + name + "_" + algorithmName + "_" + sourceId + "_" + targetId + ".dat";
		writeResultsToFile(result.toString(), resultFile);
	}
	
	private void writeResultsToFile(String results, String filename) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File(filename)));
			writer.write(results);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private DirectedGraph<PathVertex, PathEdge> loadGraph(String filename) {
		System.out.print("Loading graph " + filename + "... ");
		long start = System.currentTimeMillis();
		ShortestPathGraphReader reader = new ShortestPathGraphReader();
		DirectedGraph<PathVertex, PathEdge> dag = reader.read(new File(filename));
		long finish = System.currentTimeMillis();
		System.out.println("took " + (finish-start) + " milliseconds.");
		return dag;
	}
	
	private void resetVertices() {
		for (PathVertex v : this.graph.vertices()) {
			v.setDistance(Integer.MAX_VALUE);
		}
		
		for (PathVertex v : this.reversedGraph.vertices()) {
			v.setDistance(Integer.MAX_VALUE);
		}
	}
	
	private double nanoToMilli(double time) {
		return (double) time / 1000000.0;
	}
	
	public static void main(String[] args) {
		new PerformanceEvaluation("data/NYC.sp").evaluate();
	}
}