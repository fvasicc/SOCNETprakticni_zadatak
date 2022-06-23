package clusterability;

import java.util.Map;

import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.graph.Graph;

public class ClusteringCoefficient<V, E> {
	
	Graph<V, E> graph = null;
	Map<V, Double> ccMap = null;
	
	public ClusteringCoefficient(Graph<V,E> graph) {
		this.graph = graph;
		ccMap = Metrics.clusteringCoefficients(this.graph);
	}

	public double averageClusteringCoeficient() {		
		double sum = 0.0;
		for (double d : this.ccMap.values()) {
			sum += d;
		}
		
		return sum / this.ccMap.size();
	}
	
	public V getNodeWithMaxClusteringCoefficient() {
		V ret = null;
		for (Map.Entry<V, Double> entry : this.ccMap.entrySet()) {
			if (ret == null) 
				ret = entry.getKey();
			else if (entry.getValue() > this.ccMap.get(ret))
				ret = entry.getKey();
		}
		return ret;
	}
	
	public double getClusteringCoefficientForNode(V node) {
		return this.ccMap.get(node);
	}
}
