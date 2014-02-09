package de.mgu.algorithms.graph.io;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import de.mgu.algorithms.adt.graph.DirectedGraph;
import de.mgu.algorithms.adt.graph.DirectedGraphImpl;
import de.mgu.algorithms.graphs.path.PathEdge;
import de.mgu.algorithms.graphs.path.PathVertex;

public class ShortestPathGraphReader {
	
	private Logger LOGGER = Logger.getLogger(this.getClass().getName());

	public DirectedGraph<PathVertex, PathEdge> read(File file) {
		DirectedGraph<PathVertex, PathEdge> dag = new DirectedGraphImpl<PathVertex, PathEdge>(PathEdge.class);
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(file));
			
			String line = br.readLine();
			String[] header = line.split(" ");
			
			if (header.length != 5) {
				throw new IllegalArgumentException("Malformed header in graph input file "
						+ file.getAbsolutePath());
			}
			
			boolean isDirectedInputGraph = header[0].equals("g");
			
			if (!isDirectedInputGraph) {
				throw new IllegalArgumentException("Malformed graph structure (undirected) in graph " +
						"input file " + file.getAbsolutePath());
			}
			
			int sizeOfV = Integer.parseInt(header[2]);
			int sizeOfE = Integer.parseInt(header[4]);
			
			for (int i = 0; i < sizeOfV; i++) {
				String[] vertexAttributes = br.readLine().split(" ");
				
				if (vertexAttributes.length != 4) {
					StringBuilder sb = new StringBuilder();
					sb.append("Malformed vertex description in graph input file: " + file.getAbsolutePath());
					sb.append("Found attributes: " + vertexAttributes);
					throw new IllegalArgumentException(sb.toString());
				}
				
				String vertexId = vertexAttributes[1];
				int x = Integer.parseInt(vertexAttributes[2]); // x-value, longitude
				int y = Integer.parseInt(vertexAttributes[3]); // y-value, latitude
				PathVertex vertex = new PathVertex(vertexId);
				vertex.setCoordinates(new Point(x, y));
				boolean success = dag.addVertex(vertex);
				
				if (!success) {
					throw new IllegalStateException("Could not add vertex to directed graph.");
				}
				
				LOGGER.fine("Added vertex with ID " + vertexId + ".");
			}
			
			for (int i = 0; i < sizeOfE; i++) {
				/* e <head> <tail> <cost> */
				String[] edgeAttributes = br.readLine().split(" ");
				PathVertex head = dag.getVertexById(edgeAttributes[1]);
				PathVertex tail = dag.getVertexById(edgeAttributes[2]);
				int cost = Integer.parseInt(edgeAttributes[3]);
				PathEdge edge = dag.connect(head,  tail);
				edge.setCost(cost);
				
				LOGGER.fine("Added edge (" + edgeAttributes[1] + "," + edgeAttributes[2] + ") to directed graph.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return dag;
	}
}