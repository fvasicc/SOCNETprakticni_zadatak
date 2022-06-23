package smallworld;

import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.graph.Graph;

public class SmallWorldCoefficent<V, E> {
	
	private Graph<V, E> graph = null;
	private double sum = 0.0;
	
	public SmallWorldCoefficent(Graph<V, E> graph) {
		this.graph = graph;
		UnweightedShortestPath<V, E> usp = new UnweightedShortestPath<>(graph);
		List<V> nodes = new ArrayList<>(this.graph.getVertices());
		for (int i = 0; i < this.graph.getVertexCount() - 1; i++) {
			for (int j = i + 1; j < this.graph.getVertexCount(); j++) {
				this.sum += usp.getDistance(nodes.get(i), nodes.get(j)).doubleValue();
			} 
		}
	}
	
	public double getSmallWorldCoeff() {
		return this.sum / (this.graph.getVertexCount() * (this.graph.getVertexCount() - 1));
	}
	
	public double getNetworkEfficent() {
		return (1 / this.sum) / (this.graph.getVertexCount() * (this.graph.getVertexCount() - 1));
	}
}
